package usf.smartvitals.utils;

import java.io.IOException;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

import android.content.Context;
import android.location.Geocoder;
import android.location.Address;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.TelephonyManager;
import android.widget.Toast;

public class Utils {
	
	public static String timeToString(long time) {
		int hours = (int) (time / 3600);
		time -= hours*3600;
		int minutes = (int) (time/60);
		time -= minutes*60;
		
		String strHours = hours < 10? "0" + hours : "" + hours;
		String strMinutes = minutes < 10? "0" + minutes : "" + minutes;
		String strSeconds = time < 10? "0" + time : "" + time;
		
		return "" + strHours + ":" + strMinutes + ":" + strSeconds;
	}
	
	public static String dateToString(long t) {
		Date d = new Date(t);
		DateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss");
		return formatter.format(d);
	}
	
	public static String twoDecimalPlaces(double d) {
			
		String s;
		DecimalFormat formatter = new DecimalFormat("0.00");
		s = formatter.format(d);
		
		return s;
	}
	
	public static String getAddress(Context c, double latitude, double longitude) {
		Geocoder geocoder = new Geocoder(c, Locale.ENGLISH);
		List<Address> addresses = null;
		
		try {
			addresses = geocoder.getFromLocation(latitude, longitude, 1);
		} catch (IOException e) {
			Toast.makeText(c, e.getMessage(), Toast.LENGTH_LONG);
		}
		
		if (addresses != null && !addresses.isEmpty()) {
			Address address = addresses.get(0);
			String strAddress = "";
			
			for (int i = 0; i < address.getMaxAddressLineIndex(); i++) {
				strAddress += address.getAddressLine(i) + "\n";
			}
			
			return strAddress;
		}
		
		return "";
	}
	
	public static boolean isNetworkAvailable(Context c) {
	    ConnectivityManager cm =
	        (ConnectivityManager) c.getSystemService(Context.CONNECTIVITY_SERVICE);

	    NetworkInfo netInfo = cm.getActiveNetworkInfo();
	    if (netInfo != null && netInfo.isConnectedOrConnecting()) {
	        return true;
	    }
	    return false;
	}
	
	public static String getDeviceId(Context c)
	{
		final TelephonyManager tm = (TelephonyManager) c.getSystemService(Context.TELEPHONY_SERVICE);

	    final String tmDevice, tmSerial, androidId;
	    tmDevice = "" + tm.getDeviceId();
	    tmSerial = "" + tm.getSimSerialNumber();
	    androidId = "" + android.provider.Settings.Secure.getString(c.getContentResolver(), android.provider.Settings.Secure.ANDROID_ID);

	    UUID deviceUuid = new UUID(androidId.hashCode(), ((long)tmDevice.hashCode() << 32) | tmSerial.hashCode());
	    String deviceId = deviceUuid.toString();
	    return deviceId;
	}
}
