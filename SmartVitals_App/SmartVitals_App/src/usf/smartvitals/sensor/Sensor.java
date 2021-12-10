package usf.smartvitals.sensor;
import java.util.Observable;
import usf.smartvitals.utils.Values;
import android.os.Looper;
import android.util.Log;
/**
 *  A sensor collects time series from an actual device
 */
public abstract class Sensor extends Observable implements Runnable {
	
	protected int trials;
	
	/** The maximum number of reconnections */
	protected int maxReconnections;

	/** The state of the sensor */
	protected boolean active = true;

	/** The connection state of the sensor */
	protected boolean connected = false;

	/** The time the sensor should wait before reading the next packet */
	protected int sleepTime;

	/** The last read packet */
	private Packet lastReading;

	public Sensor(int sleepTime, int maxReconnections) {
		this.sleepTime = sleepTime;
		this.maxReconnections = maxReconnections;
	}

	/**
	 * Connect to the sensor and define any other operations before starting to
	 * read from the sensor
	 */
	public abstract boolean connect();

	/**
	 * All the operations that are required after the sensor finalizes the
	 * reading process, i.e., when the sensor becomes inactive.
	 */
	public abstract void finalize();

	/** Returns the last reading */
	public Packet getLastReading() {
		return lastReading;
	}

	/** All the operations required to read data from the sensor */
	public abstract Packet readData();
	
	/**
	 * Turns on the sensor to start getting data
	 */
	public void sense() {
		Log.i("Sensor", "Trying to connect");
		boolean shouldReconnect = false;
		connected=connect();
		if (connected) {
			Log.i("Sensor", "Bluetooth is connected");
			listen();
			if (active && !connected) {
				shouldReconnect = true;
			}
			Log.i("Sensor", "End connection");
			finalize();
		} else if (active) {
			Log.i("Sensor", "Not connected");
			shouldReconnect = true;
		}
		if(shouldReconnect) {
			reconnect();
			shouldReconnect = false;
		}
	}
	
	/**
	 * Gets location data
	 */
	public void listen() {
		while (active && connected) {
			Packet p = readData();
			if (p != null) {
				setChanged();
				notifyObservers(p);
			} 
			try {
				if (sleepTime > 0) {
					Thread.sleep(sleepTime);
				}
			} catch (Exception e) {
				
			}
		}
	}
	
	@SuppressWarnings("static-access")
	public void run() {
		trials = 0;
		/**THIS IS REQUIRED TO RUN THREADS! DO NOT REMOVE!**/
		Looper.myLooper().prepare();
		sense();
	}
	
	/**
	 * Tries to reconnect the application to the physical sensor in case it is suddenly disconnected
	 */
	public void reconnect() {
		try {
			trials++;
			if(trials <= Values.NUMBER_OF_RECONNECTIONS){
				Thread.sleep(Values.TIME_BETWEEN_RECONNECTIONS);
				Log.d("Sensor", "Reconnecting...");
				sense();
			}
		} catch (Exception e) {
			Log.e("Sensor", "Cannot reconnect: " + e, e);
		}
	}
	
	/**
	 * 
	 * @return true if the sensor is connected to the application, false otherwise
	 */
	public boolean isConnected() {
		return connected;
	}
	
	/**
	 * 
	 * @return
	 */
	public boolean isActive() {
		return active;
	}

	/**
	 * 
	 * @param active
	 */
	public void setActive(boolean active) {
		this.active = active;
	}

}
