package usf.smartvitals.sensor;

import usf.smartvitals.gui.StartActivity;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;

public class GPSListener implements LocationListener {

	private StartActivity ui;
	private static GPSListener singleton = null;
	
	
	private GPSListener() {}

	public static GPSListener getGPSListener(StartActivity ui) {
		if (singleton == null)
			singleton = new GPSListener();
		singleton.ui = ui;
		return singleton;
	}
	
	@Override
	public void onLocationChanged(Location l) {
		ui.setTestLocation(l);
		ui.locationManager.removeUpdates(this);
	}

	@Override
	public void onProviderDisabled(String arg0) {}

	@Override
	public void onProviderEnabled(String arg0) {}

	@Override
	public void onStatusChanged(String arg0, int arg1, Bundle arg2) {}

}
