package usf.smartvitals.gui;

import usf.smartvitals.R;
import usf.smartvitals.utils.Values;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageButton;

public class SelectActivityActivity extends Activity{

ImageButton btnSettings, btnHome, btnWalking, btnRunning, btnOther, btnBack;
	
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.selectactivity);
        
        btnSettings = (ImageButton) findViewById(R.id.btnSettings);
        btnSettings.setOnClickListener(new LinkClickListener(this, PreferencesActivity.class));
        
        btnWalking = (ImageButton) findViewById(R.id.btnWalking);
        btnWalking.setOnClickListener(new OnClickListener(){
    	    	@Override
    	    	public void onClick(View v) {
    	    		Values.ACTIVITY_TYPE= Values.WALKING;
    				Intent intent = new Intent(SelectActivityActivity.this, StartActivity.class);
    				startActivity(intent);
    				finish();		
    	    	}
    	    });
        
        btnRunning = (ImageButton) findViewById(R.id.btnRunning);
        btnRunning.setOnClickListener(new OnClickListener(){
	    	@Override
	    	public void onClick(View v) {
	    		Values.ACTIVITY_TYPE= Values.RUNNING;
				Intent intent = new Intent(SelectActivityActivity.this, StartActivity.class);
				startActivity(intent);
				finish();		
	    	}
	    });
        
        btnOther = (ImageButton) findViewById(R.id.btnOther);
        btnOther.setOnClickListener(new OnClickListener(){
	    	@Override
	    	public void onClick(View v) {
	    		Values.ACTIVITY_TYPE= Values.OTHER;
				Intent intent = new Intent(SelectActivityActivity.this, StartActivity.class);
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
    }
}
