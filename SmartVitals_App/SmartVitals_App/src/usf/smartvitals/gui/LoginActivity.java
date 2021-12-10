package usf.smartvitals.gui;
import usf.smartvitals.R;
import usf.smartvitals.json.WebMethods;
import usf.smartvitals.json.Models.AuthenticationToken;
import usf.smartvitals.json.Models.JsonResponse;
import usf.smartvitals.utils.Utils;
import usf.smartvitals.utils.Values;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

public class LoginActivity extends Activity {

	EditText txtUserName, txtPassword;
	ImageButton btnLogin;
	String deviceId = "";
	ProgressDialog pd;
	JsonResponse<AuthenticationToken> token;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.login);
		SharedPreferences settings = getSharedPreferences(
				Values.PREFS_FILE_NAME, 0);

		deviceId = Utils.getDeviceId(this);
		txtUserName = (EditText) findViewById(R.id.userName);
		txtPassword = (EditText) findViewById(R.id.password);
		btnLogin = (ImageButton) findViewById(R.id.btnLogin);
		btnLogin.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				String USERNAME = txtUserName.getText().toString();
				String PASSWORD = txtPassword.getText().toString();

				if (!Utils.isNetworkAvailable(LoginActivity.this))
					Toast.makeText(LoginActivity.this, "Network Error",
							Toast.LENGTH_SHORT).show();
				else {
					JsonResponse<AuthenticationToken> token = WebMethods.Login(
							USERNAME, PASSWORD, deviceId);
					if (token.Success && token.Response.IsAuthenticated) {
						Values.USERTOKEN = token.Response;
						SharedPreferences settings = getSharedPreferences(
								Values.PREFS_FILE_NAME, 0);
						SharedPreferences.Editor editor = settings.edit();
						editor.putString("SessionId",
								Values.USERTOKEN.SessionId.toString());
						editor.commit();
						Intent intent = new Intent(LoginActivity.this,
								HomeActivity.class);
						startActivity(intent);
						finish();
					} else {
						Toast.makeText(LoginActivity.this, token.Message,
								Toast.LENGTH_SHORT).show();
					}
				}
			}
		});
		if (settings != null && !settings.getString("SessionId", "").equals("")) {

			Values.USERTOKEN.SessionId = settings.getString("SessionId", "");
			if (!Utils.isNetworkAvailable(LoginActivity.this))
				Toast.makeText(LoginActivity.this, "Network Error",
						Toast.LENGTH_SHORT).show();
			else {

				JsonResponse<AuthenticationToken> token = WebMethods
						.getAuthenticationToken(Values.USERTOKEN.SessionId
								.toString());
				if (token.Success) {
					if (token.Response == null) {
						Toast.makeText(LoginActivity.this, token.Message,
								Toast.LENGTH_SHORT).show();
					} else {
						Values.USERTOKEN = token.Response;
						Intent intent = new Intent(LoginActivity.this,
								HomeActivity.class);
						startActivity(intent);
						finish();
					}
				} else {
					Toast.makeText(LoginActivity.this, token.Message,
							Toast.LENGTH_SHORT).show();
				}

			}
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		SharedPreferences settings = getSharedPreferences(
				Values.PREFS_FILE_NAME, 0);
		if (!settings.getString("SessionId", "").equals("")) {
			Values.USERTOKEN.SessionId = settings.getString("SessionId", "");
			if (!Utils.isNetworkAvailable(LoginActivity.this))
				Toast.makeText(LoginActivity.this, "Network Error",
						Toast.LENGTH_SHORT).show();
			else {
				token = WebMethods
						.getAuthenticationToken(Values.USERTOKEN.SessionId
								.toString());
				if (token.Success && token.Response != null) {
					Values.USERTOKEN = token.Response;
					Intent intent = new Intent(LoginActivity.this,
							HomeActivity.class);
					startActivity(intent);
					finish();
				} else {
					Toast.makeText(LoginActivity.this, token.Message,
							Toast.LENGTH_SHORT).show();
				}
			}
		}
	}
}