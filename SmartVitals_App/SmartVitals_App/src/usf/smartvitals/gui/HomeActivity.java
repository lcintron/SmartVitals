package usf.smartvitals.gui;

import usf.smartvitals.R;
import usf.smartvitals.json.WebMethods;
import usf.smartvitals.utils.Utils;
import usf.smartvitals.utils.Values;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.TextView;
import android.widget.ImageButton;

public class HomeActivity extends Activity {
	
	ImageButton btnLogout, btnSettings, btnSelectActivity, btnViewHistory, btnEdit;
	TextView lblName, lblSex, lblHeight, lblWeight, lblTotalTime;
	String deviceId = "";
	
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.home);
        deviceId = Utils.getDeviceId(this);
        btnLogout = (ImageButton) findViewById(R.id.btnLogout);
        btnLogout.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				WebMethods.Logout(deviceId);
		    	SharedPreferences settings = getSharedPreferences(Values.PREFS_FILE_NAME, 0);
		        SharedPreferences.Editor editor = settings.edit();
		        editor.putString("SessionId", "");			        
		        editor.commit();
		        Values.USERTOKEN = null;
				Intent intent = new Intent(HomeActivity.this, LoginActivity.class);
				startActivity(intent);
				this.finish();//to kill the previous HomeActivity
			}

			private void finish() {
				android.os.Process.killProcess(android.os.Process.myPid());
			}});
        
        btnSettings = (ImageButton) findViewById(R.id.btnSettings);
        btnSettings.setOnClickListener(new LinkClickListener(this, PreferencesActivity.class));
        
        btnSelectActivity = (ImageButton) findViewById(R.id.btnSelectActivity);
        btnSelectActivity.setOnClickListener(new LinkClickListener(this, SelectActivityActivity.class));
        
        btnViewHistory = (ImageButton) findViewById(R.id.btnViewHistory);
        btnViewHistory.setOnClickListener(new LinkClickListener(this, HistoryActivity.class)); 
        
        btnEdit = (ImageButton) findViewById(R.id.btnEdit);
        btnEdit.setOnClickListener(new LinkClickListener(this, EditUserInfo.class)); 
        
        lblName = (TextView) findViewById(R.id.lblName);
        lblName.setText(Values.USERTOKEN.Profile.Name);
        
        lblSex = (TextView) findViewById(R.id.lblSex);
        lblSex.setText(Values.USERTOKEN.Profile.Sex);
        
        lblWeight = (TextView) findViewById(R.id.lblWeight);
        lblWeight.setText(""+Values.USERTOKEN.Profile.Weight);

        lblHeight = (TextView) findViewById(R.id.lblHeight);
        lblHeight.setText(Values.USERTOKEN.Profile.Height.Feet + "' " + Values.USERTOKEN.Profile.Height.Inches+ "''");
        
        lblTotalTime = (TextView) findViewById(R.id.lblTotalTime);
        lblTotalTime.setText("Not SET");
    }
    
    @Override
    public void onResume() {
    	lblName.setText(Values.USERTOKEN.Profile.Name);
    	lblSex.setText(Values.USERTOKEN.Profile.Sex);
    	lblWeight.setText(""+Values.USERTOKEN.Profile.Weight);
    	lblHeight.setText(Values.USERTOKEN.Profile.Height.Feet + "' " +Values.USERTOKEN.Profile.Height.Inches + "''");
    	lblTotalTime.setText("NOT SET");
    	
    	super.onResume();
    }
}
