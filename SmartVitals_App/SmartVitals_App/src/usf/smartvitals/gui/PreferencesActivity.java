package usf.smartvitals.gui;

import usf.smartvitals.R;
import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.CheckBox;
import android.widget.ImageButton;
import usf.smartvitals.utils.Values;

public class PreferencesActivity extends Activity {

	ImageButton btnSettings, btnBack;
	CheckBox chkGeolocationOn, chkGeolocationCaching, chkHistoryCaching, chkSyncToWeb;
	

	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.preferences);
        
        btnSettings = (ImageButton) findViewById(R.id.btnSettings);    
        
        //restore saved settings
        SharedPreferences settings = getSharedPreferences(Values.PREFS_FILE_NAME, 0);
        chkGeolocationOn = (CheckBox) findViewById(R.id.chkGeolocationOn);
        chkGeolocationOn.setChecked(settings.getBoolean(Values.geolocationOnSetting, false));
        chkGeolocationCaching = (CheckBox) findViewById(R.id.chkGeolocationCaching);
        chkGeolocationCaching.setChecked(settings.getBoolean(Values.geolocationCachingSetting, false));
        chkHistoryCaching = (CheckBox) findViewById(R.id.chkHistoryCaching);
        chkHistoryCaching.setChecked(settings.getBoolean(Values.historyCachingSetting, false));
        chkSyncToWeb = (CheckBox) findViewById(R.id.chkSyncToWeb);
        chkSyncToWeb.setChecked(settings.getBoolean(Values.syncToWebSetting, false));
        
        btnBack = (ImageButton) findViewById(R.id.btnBack);
        btnBack.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				onBackPressed();
				
			}});
    }
    
    @Override
    public void onBackPressed() {
    	SharedPreferences settings = getSharedPreferences(Values.PREFS_FILE_NAME, 0);
        SharedPreferences.Editor editor = settings.edit();
        
        editor.putBoolean(Values.geolocationOnSetting, chkGeolocationOn.isChecked());
        editor.putBoolean(Values.geolocationCachingSetting, chkGeolocationCaching.isChecked());
        editor.putBoolean(Values.historyCachingSetting, chkHistoryCaching.isChecked());
        editor.putBoolean(Values.syncToWebSetting, chkSyncToWeb.isChecked());
        
        editor.commit();
        
    	super.onBackPressed();
    }
}
