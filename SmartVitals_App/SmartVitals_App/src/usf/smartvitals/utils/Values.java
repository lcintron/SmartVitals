package usf.smartvitals.utils;
import java.util.UUID;
import usf.smartvitals.json.Models.AuthenticationToken;
import usf.smartvitals.json.Models.Test;

public class Values {
	/*Activity types*/
	public static final String WALKING = "Walking";
	public static final String RUNNING = "Running";
	public static final String OTHER = "Other";
	
	public static String ACTIVITY_TYPE="";
	public static AuthenticationToken USERTOKEN = new AuthenticationToken();
	public static UUID SessionId = null;
	public static Test LAST_TEST = null;
	public static String BIOHARNESS_ADDRESS = "00001101-0000-1000-8000-0007809D8AE8";
	public static String BIOHARNESS_MAC_ADDRESS = "00:07:80:88:F3:95";
	

	public static boolean DUMMY_SENSOR_ACTIVE = false;
	public static boolean BH_ACC_SENSOR_ACTIVE = true;
	public static boolean BH_EKG_SENSOR_ACTIVE = true;

//	public static boolean DUMMY_SENSOR_ACTIVE = true;
//	public static boolean BH_ACC_SENSOR_ACTIVE = false;
//	public static boolean BH_EKG_SENSOR_ACTIVE = false;

	public static boolean BH_VITALS_SENSOR_ACTIVE = true;
	public static final long TIMEOUT_BEACON = 500;
	public static final int MAX_NUMBER_OF_FILES = 5;
	
	public static int SELECTED_ACTIVITY_ID;
	
	public static final int NUMBER_OF_RECONNECTIONS = 5;
	public static final int TIME_BETWEEN_RECONNECTIONS = 10;	
	
	public static final String PULSE_UNITS = "bpm";
	public static final String RESPIRATION_RATE_UNITS = "bpm";
	public static final String BREATH_AMPLITUDE_UNITS = "U";
	public static final String POSTURE_UNITS = "\u00B0";
	public static final String SKIN_TEMPERATURE_UNITS = "\u2103";
	public static final String SPO2_UNITS = "%";
	public static final String ECG_UNITS = "U";
	public static final String GSR_UNITS = "U";
	public static final String GALVANIC_SKIN_RESPONSE = "U";
	public static final String ACCELERATION_UNITS = "m/s^2";
	public static final String COORDINATE_UNITS = "\u00B0";
	
	/*Preferences*/
	public static final String PREFS_FILE_NAME = "preferences";
	public static final String geolocationOnSetting = "geolocationOn";
	public static final String geolocationCachingSetting = "geolocationCachingSetting";
	public static final String historyCachingSetting = "historyCachingSetting";
	public static final String syncToWebSetting = "syncToWebSetting";
	
	//localhost
//    public static final String LOGIN ="http://10.0.2.2:1234/json/AuthenticateUser";
//    public static final String UPDATE_PROFILE = "http://10.0.2.2:1234/json/UpdateProfile";
//    public static final String LOGOUT = "http://10.0.2.2:1234/json/DeauthenticateUser";
//    public static final String AUTH_TOKEN = "http://10.0.2.2:1234/json/GetAuthenticationToken";
//    public static final String ADD_TEST = "http://10.0.2.2:1234/json/AddTest";
    
    
	//live
    public static final String LOGIN ="http://smartvitals.net.dotnet-host.com/json/AuthenticateUser";
    public static final String UPDATE_PROFILE = "http://smartvitals.net.dotnet-host.com/json/UpdateProfile";
    public static final String LOGOUT = "http://smartvitals.net.dotnet-host.com/json/DeauthenticateUser";
    public static final String AUTH_TOKEN = "http://smartvitals.net.dotnet-host.com/json/GetAuthenticationToken";
    public static final String ADD_TEST = "http://smartvitals.net.dotnet-host.com/json/AddTest";

    
}
