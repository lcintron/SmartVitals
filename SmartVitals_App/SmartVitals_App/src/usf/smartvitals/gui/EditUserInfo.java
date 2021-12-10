package usf.smartvitals.gui;
import static usf.smartvitals.utils.Values.USERTOKEN;
import usf.smartvitals.R;
import usf.smartvitals.json.WebMethods;
import usf.smartvitals.json.Models.JsonResponse;
import usf.smartvitals.json.Models.UserInfo;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

public class EditUserInfo extends Activity{
	
	ImageButton btnSettings, btnHome, btnBack;
	EditText txtName, txtAge, txtWeight, txtFeet, txtInches;
	
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.edituserinfo);
        UserInfo Profile = USERTOKEN.Profile;
        txtName = (EditText) findViewById(R.id.txtName);
        txtName.setText(Profile.Name);
        
        txtAge = (EditText) findViewById(R.id.txtAge);
    	txtAge.setText(Profile.Age+"");
        
        txtWeight = (EditText) findViewById(R.id.txtWeight);
		txtWeight.setText(Profile.Weight+"");
        
        txtFeet = (EditText) findViewById(R.id.txtFeet);
		txtFeet.setText(Profile.Height.Feet+"");
		
        txtInches = (EditText) findViewById(R.id.txtInches);
        txtInches.setText(Profile.Height.Inches+"");
        
		btnSettings = (ImageButton) findViewById(R.id.btnSettings);
        btnSettings.setOnClickListener(new LinkClickListener(this, PreferencesActivity.class));
    
        btnBack = (ImageButton) findViewById(R.id.btnBack);
        btnBack.setOnClickListener(new OnClickListener(){
        
        	@Override
        	public void onClick(View v) {
        		onBackPressed();
			
        	}});
        
    }
    
    @Override
    public void onBackPressed() {
    	USERTOKEN.Profile.Name=txtName.getText().toString();
    	USERTOKEN.Profile.Age=Integer.parseInt(txtAge.getText().toString());
    	USERTOKEN.Profile.Height.Feet=Integer.parseInt(txtFeet.getText().toString());
    	USERTOKEN.Profile.Height.Inches=Integer.parseInt(txtInches.getText().toString());
    	USERTOKEN.Profile.Weight=Integer.parseInt(txtWeight.getText().toString());
    	
    	//WebServices.updateUser(USER);
    	JsonResponse<Boolean>  token = WebMethods.UpdateProfile(USERTOKEN.SessionId.toString(), USERTOKEN.Profile);
    	Toast.makeText(this, token.Message, Toast.LENGTH_SHORT).show();
    	super.onBackPressed();
    }

}
