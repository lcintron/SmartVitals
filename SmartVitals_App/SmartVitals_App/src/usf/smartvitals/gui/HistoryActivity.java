package usf.smartvitals.gui;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import usf.smartvitals.R;
import usf.smartvitals.json.Models.Sample;
import usf.smartvitals.json.Models.Test;
import usf.smartvitals.utils.Utils;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

public class HistoryActivity extends Activity {

	private LinearLayout layoutButtons;
	private ImageButton btnHome;
	
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.history);
    
        btnHome = (ImageButton) findViewById(R.id.btnHome);
        btnHome.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				onBackPressed();
			}});
        
        String[] files = fileList();
        layoutButtons = (LinearLayout) findViewById(R.id.layoutButtons);
        
        for (final String file : files) {        	
        	Button b = new Button(this);
        	b.setText(Utils.dateToString(Long.parseLong(file)));
        	b.setLayoutParams(new LayoutParams(
        				ViewGroup.LayoutParams.MATCH_PARENT,
        				ViewGroup.LayoutParams.WRAP_CONTENT
			));
        	b.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					Bundle bundle = new Bundle();
					
					Test test = getTest(file);
					
					if (test == null)
						return;
					
					ArrayList<Sample> samples = test.Samples;
					double[] averageValues = getAverageValues(samples);
					
					bundle.putString(ViewTestActivity.PULSE, Utils.twoDecimalPlaces(averageValues[0]));
					bundle.putString(ViewTestActivity.ECG, Utils.twoDecimalPlaces(averageValues[1]));
					bundle.putString(ViewTestActivity.BLOOD_PRESSURE, Utils.twoDecimalPlaces(averageValues[2]));
					bundle.putString(ViewTestActivity.ACCELERATION, Utils.twoDecimalPlaces(averageValues[3]) + ", "
													  + Utils.twoDecimalPlaces(averageValues[4]) + ", "
													  + Utils.twoDecimalPlaces(averageValues[5]));
					
					bundle.putString(ViewTestActivity.TOTAL_TIME, getTotalTime(samples));
					bundle.putString(ViewTestActivity.TOTAL_TIME, Utils.getAddress(HistoryActivity.this, test.Latitude, test.Longitude));
					bundle.putString(ViewTestActivity.TOTAL_TIME, Utils.getAddress(HistoryActivity.this, test.Latitude, test.Longitude));
					
					Intent intent = new Intent(HistoryActivity.this, ViewTestActivity.class);
					intent.putExtras(bundle);
					startActivity(intent);
				}
				});
        	layoutButtons.addView(b);
        }
    }    
    
    private Test getTest(String filename) {
    	FileInputStream stream = null;
    	StringBuffer content = new StringBuffer("");
    	int i;
    	
    	try {
			stream = openFileInput(filename);
		} catch (FileNotFoundException e) {
			Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT);
		}
    	
    	try {
			while ((i=stream.read()) != -1) {
				content.append((char)i);
			}
		} catch (IOException e) {
			Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT);
		}
    	try {
			stream.close();
		} catch (IOException e) {
			Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT);
		}
    	
    	Gson gson = new Gson();
    	Type objectType= new TypeToken<Test>(){}.getType();
    	
    	try {
    		Test t = gson.fromJson(content.toString(), objectType);
    		return t;
    	} catch(Exception e) {
    		Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT);
    	}
    	
    	return null;
    }
    
    private double[] getAverageValues(ArrayList<Sample> samples) {
    	
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

    private String getTotalTime(ArrayList<Sample> samples) {
    	String[] strEndTime = samples.get(samples.size()-1).Time.split("\\.");
    	long time = (Integer.parseInt(strEndTime[0])*3600 + Integer.parseInt(strEndTime[1])*60 + Integer.parseInt(strEndTime[2]));
		return Utils.timeToString(time);
    }
}
