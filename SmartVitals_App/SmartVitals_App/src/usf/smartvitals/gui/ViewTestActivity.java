package usf.smartvitals.gui;

import usf.smartvitals.R;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.TextView;

public class ViewTestActivity extends Activity {

	public static final String PULSE = "avgPulse";
	public static final String ECG = "avgECG";
	public static final String BLOOD_PRESSURE = "avgBloodPressure";
	public static final String ACCELERATION = "avgAcceleration";
	public static final String TOTAL_TIME = "totalTime";
	public static final String LOCATION = "location";
	public static final String POST_LOCATION = "post_location";
	
	private ImageButton btnBack;
	private TextView lblAvgPulse, lblAvgECG, lblAvgBloodPressure, lblAvgAcceleration, lblTotalTime;
	
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.view_test);
        
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
        
        btnBack = (ImageButton) findViewById(R.id.btnBack);
        btnBack.setOnClickListener(new OnClickListener(){
        
        	@Override
        	public void onClick(View v) {
        		onBackPressed();
			
        	}});      
    }
}
