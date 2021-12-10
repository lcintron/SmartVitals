package usf.smartvitals.gui;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import usf.smartvitals.R;
import usf.smartvitals.comm.BluetoothConnection;
import usf.smartvitals.json.WebMethods;
import usf.smartvitals.json.Models.JsonResponse;
import usf.smartvitals.json.Models.Sample;
import usf.smartvitals.json.Models.Test;
import usf.smartvitals.sensor.BioHarnessAccelerometerPacket;
import usf.smartvitals.sensor.BioHarnessECGPacket;
import usf.smartvitals.sensor.BioHarnessGeneralPacket;
import usf.smartvitals.sensor.GPSListener;
import usf.smartvitals.sensor.Packet;
import usf.smartvitals.sensor.SensorObserver;
import usf.smartvitals.utils.Utils;
import usf.smartvitals.utils.Values;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.Chronometer;
import android.widget.Chronometer.OnChronometerTickListener;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

public class StartActivity extends Activity {

	private ImageButton btnSettings, btnHome, btnBack, btnPlay, btnStop;
	
	private TextView lblHeartRate, lblECG, lblSkin;
	private String heartRate, ecg, skin;
	
	private Chronometer chronometer;
	private long beginTime;
	
	LinearLayout layoutECG;
	LinearLayout layoutACC;
	private ECGGraphView ECGGraphView;
	private AccGraphview ACCGraphView;
	public Handler handler;
	
	private SensorObserver sensorObserver;
	public LocationManager locationManager;
	private GPSListener gpsListener;
	private boolean geolocationOn = false;
	private boolean syncToWeb = false;
	ProgressDialog pd;
	
	private Test test;
	private ArrayList<Sample> samples;
	
	private long firstSampleTime = 0;
	
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.startactivity);
        
        samples = new ArrayList<Sample>();
        test = new Test();
        test.Description=Values.ACTIVITY_TYPE;
        test.UserName = Values.USERTOKEN.Profile.UserName;
        test.Latitude = 28.058641;
        test.Longitude = -82.415496;
        test.Samples = samples;
        
        
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        sensorObserver = SensorObserver.getSensorObserver(this);
        gpsListener = GPSListener.getGPSListener(this);
        
        lblHeartRate = (TextView) findViewById(R.id.lblHeartRate);
        lblECG = (TextView) findViewById(R.id.lblECG);
        lblSkin = (TextView) findViewById(R.id.lblSkin);
        heartRate = ecg = skin = "0";
        
        SharedPreferences settings = getSharedPreferences(Values.PREFS_FILE_NAME, 0);
        geolocationOn = settings.getBoolean(Values.geolocationOnSetting, false);
        syncToWeb = settings.getBoolean(Values.syncToWebSetting, false);
        
        handler = new Handler();
        ECGGraphView = new ECGGraphView(this);
        layoutECG = (LinearLayout) findViewById(R.id.LayoutECG);
        layoutECG.addView(ECGGraphView);
        
        ACCGraphView = new AccGraphview(this);
        layoutACC = (LinearLayout) findViewById(R.id.LayoutACC);
        layoutACC.addView(ACCGraphView, LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT);
        
        chronometer = (Chronometer) findViewById(R.id.chronometer);
        chronometer.setOnChronometerTickListener(new OnChronometerTickListener() {

			@Override
			public void onChronometerTick(Chronometer c) {
				lblHeartRate.setText("" + heartRate + " " + Values.PULSE_UNITS);
		    	lblECG.setText("" + ecg + " " + Values.ECG_UNITS);
		    	lblSkin.setText("" + skin + " " + Values.SKIN_TEMPERATURE_UNITS);
			}});
        
        btnSettings = (ImageButton) findViewById(R.id.btnSettings);
        btnSettings.setOnClickListener(new LinkClickListener(this, PreferencesActivity.class));
        
        btnHome = (ImageButton) findViewById(R.id.btnHome);
        btnHome.setOnClickListener(new OnClickListener(){
        
	    	@Override
	    	public void onClick(View v) {
				Intent intent = new Intent(StartActivity.this, HomeActivity.class);
				startActivity(intent);
				finish();		
	    	}
	    });
        
        btnBack = (ImageButton) findViewById(R.id.btnBack);
        btnBack.setOnClickListener(new OnClickListener(){        
        	@Override
        	public void onClick(View v) {
        		onBackPressed();
        	}});
        
        btnPlay = (ImageButton) findViewById(R.id.btnPlay);
        btnPlay.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				beginTime = System.currentTimeMillis();
				if (Values.DUMMY_SENSOR_ACTIVE) {
					test.Date = new Date(beginTime);
					sensorObserver.startSensor();
					chronometer.setBase(SystemClock.elapsedRealtime());
					chronometer.start();
					
					if (geolocationOn) {
						if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
							AlertDialog dialog = createDialog("GPS is turned off. Geolocation will be deactivated.");
							dialog.show();
						} else {
							locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, gpsListener);
						}
					}
					return;
				}
				if (!BluetoothConnection.isBluetoothEnabled()) {
					AlertDialog dialog = createDialog("Bluetooth is necessary for the stress test. Please enable it and try again");
					dialog.show();
					return;
				}
				
				if (geolocationOn) {
					if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
						AlertDialog dialog = createDialog("GPS is turned off. Geolocation will be deactivated.");
						dialog.show();
					} else {
						locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 20000, 50, gpsListener);
					}
				}
				
				test.Date = new Date(beginTime);
				sensorObserver.startSensor();
				chronometer.setBase(SystemClock.elapsedRealtime());
				chronometer.start();
			}});
        
        btnStop = (ImageButton) findViewById(R.id.btnStop);
        btnStop.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {		
				
				if (sensorObserver.sensorOn()) {
					chronometer.stop();
					pd = ProgressDialog.show(StartActivity.this, "Loading", "", true, false);
					//stop sensors and chronometer, and save session to file
					sensorObserver.stopSensor();
					Criteria crit = new Criteria();
					crit.setAccuracy(Criteria.ACCURACY_FINE);
					setTestLocation(locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER));
					locationManager.removeUpdates(gpsListener);
					saveTestToFile();
					
					//send summary to next activity
					long time = 0;
					double[] averageValues = getAverageValues();
					if (samples.size() > 0) {
						String[] strTime = samples.get(samples.size()-1).Time.split("\\.");
						time = Integer.parseInt(strTime[0])*3600 + Integer.parseInt(strTime[1])*60 + Integer.parseInt(strTime[2]);
					}
							
					Bundle bundle = new Bundle();
					bundle.putString(Summary.PULSE, Utils.twoDecimalPlaces(averageValues[0]));
					bundle.putString(Summary.ECG, Utils.twoDecimalPlaces(averageValues[1]));
					bundle.putString(Summary.BLOOD_PRESSURE, Utils.twoDecimalPlaces(averageValues[2]));
					bundle.putString(Summary.ACCELERATION, Utils.twoDecimalPlaces(averageValues[3]) + ", "
													  + Utils.twoDecimalPlaces(averageValues[4]) + ", "
													  + Utils.twoDecimalPlaces(averageValues[5]));
					
					bundle.putString(Summary.TOTAL_TIME, Utils.timeToString(time));
					bundle.putString(Summary.LOCATION, Utils.getAddress(StartActivity.this, test.Latitude, test.Longitude));
					bundle.putString(Summary.POST_LOCATION, Utils.getAddress(StartActivity.this, test.Latitude, test.Longitude));
					
					Intent intent = new Intent(StartActivity.this, Summary.class);
					intent.putExtras(bundle);
					startActivity(intent);
					finish();
				}
			}});
    }
    
    public void updateVitalSigns(BioHarnessGeneralPacket packet) {
    	//update view
    	heartRate = Integer.toString(packet.getHeartRate());
    	ecg = Integer.toString(packet.getEcgAmplitude());
    	skin = Double.toString(packet.getSkinTemperature()/10.0);

    	//add new sample
    	Sample s = new Sample();
    	s.BloodPressure = packet.getBloodPressure();
    	s.EKG = packet.getEcgAmplitude();
    	s.HeartRate = packet.getHeartRate();
    	s.SkinTemperature = packet.getSkinTemperature()/10.0;
    	s.RespirationRate = packet.getRespirationRate();
    	s.Posture = packet.getPosture();
    	s.AccX = ((packet.getLateralAxeMin() + packet.getLateralAxePeak())/10.0)/2;
    	s.AccY = ((packet.getVerticalAxeMin() + packet.getVerticalAxePeak())/10.0)/2;
    	s.AccZ = ((packet.getSagitalAxeMin() + packet.getSagitalAxePeak())/10.0)/2;
    	
    	if (samples.size() == 0) {
    		firstSampleTime = packet.getTimeStamp();
    	}
    	
		long seconds = (packet.getTimeStamp() - firstSampleTime)/1000;
		int hours = (int) (seconds / 3600);
		seconds -= hours*3600;
		int minutes = (int) (seconds / 60);
		seconds -= minutes*60;
		
    	String strHours = hours < 10? "0" + hours : "" + hours;
    	String strMinutes = minutes < 10? "0" + minutes : "" + minutes;
    	String strSeconds = seconds < 10? "0" + seconds : "" + seconds;
    	
    	s.Time = strHours + "." + strMinutes + "." + strSeconds;    	
    	samples.add(s);
    }
    
    public void updateGraph(final Packet packet) {
    	Runnable r = new Runnable() {
    		public void run() {
    			if (packet instanceof BioHarnessAccelerometerPacket)
    				ACCGraphView.updateGraph((BioHarnessAccelerometerPacket) packet);
    			else if (packet instanceof BioHarnessECGPacket)
    				ECGGraphView.updateGraph((BioHarnessECGPacket) packet);
    		}
    	};
    	handler.post(r);
    }
    
    public void setTestLocation(Location location) {
    	if (location!=null) {
			test.Latitude = location.getLatitude();
			test.Longitude = location.getLongitude();
		}
    }

    private double[] getAverageValues() {
    	
    	double avgPulse = 0, avgBloodPressure = 0, avgECG = 0, 
    		   avgAccX = 0, avgAccY = 0, avgAccZ = 0;
    		   
    	for (Sample s : samples) {
    		avgPulse += s.HeartRate;
    		avgECG += s.EKG;
    		avgBloodPressure += s.BloodPressure;
    		avgAccX += s.AccX;
    		avgAccY += s.AccY;
    		avgAccZ += s.AccZ;
    	}
    	int n = samples.size();
    	double[] values = {avgPulse/n, avgECG/n, avgBloodPressure/n, avgAccX/n, avgAccY/n, avgAccZ/n};
    	
    	return values;
    }
    
    private void saveTestToFile() {
    	Runnable webcall = new Runnable() {
			public void run() {
		    	String[] fileList = fileList();
		    	if (fileList.length >= Values.MAX_NUMBER_OF_FILES) {
		    		Arrays.sort(fileList);
		    		deleteFile(fileList[0]);
		    	}				
		    	Calendar sessionDate = Calendar.getInstance();
		    	sessionDate.setTime(test.Date);
		    	
		    	String filename = "" + sessionDate.getTimeInMillis();
		    	Gson gson = new Gson();
		    	String  json = gson.toJson(test);
		    	
		    	FileOutputStream fos = null;
				try {
					fos = openFileOutput(filename, Context.MODE_PRIVATE);
					fos.write(json.getBytes());
					fos.close();
				} catch (FileNotFoundException e) {
					Toast.makeText(StartActivity.this, e.getMessage(), Toast.LENGTH_SHORT);
				}
				catch (IOException e) {
					Toast.makeText(StartActivity.this, e.getMessage(), Toast.LENGTH_SHORT);
				}
				
				if (syncToWeb) {
					JsonResponse<Boolean> response= WebMethods.AddTest(Values.USERTOKEN.SessionId, test);
					if(response.Response)Toast.makeText(StartActivity.this, "Test Uploaded!", Toast.LENGTH_SHORT);
				} else {
					Values.LAST_TEST = test;
				}
				pd.dismiss();
			}
		};
		handler.post(webcall);
    }
    
    @Override
    public void onBackPressed() {
    	sensorObserver.stopSensor();
    	locationManager.removeUpdates(gpsListener);
    	super.onBackPressed();
    }
    
    private AlertDialog createDialog(String message) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage(message)
				.setNeutralButton("OK", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {}
				});
		return builder.create();
	}
    
}
