package usf.smartvitals.gui;

import usf.smartvitals.R;
import usf.smartvitals.json.WebMethods;
import usf.smartvitals.json.Models.JsonResponse;
import usf.smartvitals.utils.Values;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class Summary extends Activity{
	
	public static final String PULSE = "avgPulse";
	public static final String ECG = "avgECG";
	public static final String BLOOD_PRESSURE = "avgBloodPressure";
	public static final String ACCELERATION = "avgAcceleration";
	public static final String TOTAL_TIME = "totalTime";
	public static final String LOCATION = "location";
	public static final String POST_LOCATION = "post_location";
	
	private ImageButton btnSettings, btnBack, btnRestartTest, btnSyncResults;
	private TextView lblAvgPulse, lblAvgECG, lblAvgBloodPressure, lblAvgAcceleration, lblTotalTime;
	
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.summary);
        
        Bundle bundle = getIntent().getExtras();
        
        lblAvgPulse = (TextView) findViewById(R.id.lblAvgPulse);
        lblAvgPulse.setText(bundle.getString(PULSE));
        lblAvgECG = (TextView) findViewById(R.id.lblAvgECG);
        lblAvgECG.setText(bundle.getString(ECG));
        lblAvgBloodPressure = (TextView) findViewById(R.id.lblAvgBloodPressure);
        lblAvgBloodPressure.setText(bundle.getString(BLOOD_PRESSURE));
        lblAvgAcceleration = (TextView) findViewById(R.id.lblAvgAcceleration);
        lblAvgAcceleration.setText(bundle.getString(ACCELERATION));
        lblTotalTime = (TextView) findViewById(R.id.lblTotalTime);
        lblTotalTime.setText(bundle.getString(TOTAL_TIME));
        
        btnSettings = (ImageButton) findViewById(R.id.btnSettings);
        btnSettings.setOnClickListener(new LinkClickListener(this, PreferencesActivity.class));
    
        btnBack = (ImageButton) findViewById(R.id.btnBack);
        btnBack.setOnClickListener(new OnClickListener(){
        
        	@Override
        	public void onClick(View v) {
        		onBackPressed();
			
        	}});
        
        btnRestartTest = (ImageButton) findViewById(R.id.btnRestartTest);
        btnRestartTest.setOnClickListener(new LinkClickListener(this, StartActivity.class));
        
        btnSyncResults = (ImageButton) findViewById(R.id.btnSyncResults);
        btnSyncResults.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				syncToWeb();
			}});
    }
    
    private void syncToWeb() {
    	if (Values.LAST_TEST != null) {
    		JsonResponse<Boolean> token = WebMethods.AddTest(Values.USERTOKEN.SessionId, Values.LAST_TEST);
    		Toast.makeText(this, token.Message, Toast.LENGTH_SHORT).show();
    		Values.LAST_TEST = null;
    	} else {
    		Toast.makeText(this, "Nothing to sync", Toast.LENGTH_SHORT).show();
    	}
    }

}
