package usf.smartvitals.comm;

import static usf.smartvitals.utils.Values.*;


import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.lang.reflect.Method;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.util.Log;

public class BluetoothConnection {
	private BluetoothSocket socket;
	private BluetoothDevice device;
	private DataInputStream inStream=null;
	private DataOutputStream outStream=null;
	private BluetoothAdapter mBluetoothAdapter;
	private Method m;
	
	private boolean connected = false;
	
	public BluetoothConnection(){
	}
	
	/**
	 * Connect to BioHarness Sensor
	 */
	public void connect(){
		try{
			cancel();
			
			mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
			
			String bhAddress = BIOHARNESS_MAC_ADDRESS;
			
			device = mBluetoothAdapter.getRemoteDevice(bhAddress);
			
	        // Get a BluetoothSocket to connect with the given BluetoothDevice
            // MY_UUID is the app's UUID string, also used by the server code
	        m = device.getClass().getMethod("createRfcommSocket", new Class[] {int.class});
	        socket = (BluetoothSocket) m.invoke(device, Integer.valueOf(1));
            socket.connect();
            inStream = new DataInputStream(socket.getInputStream());
            outStream = new DataOutputStream(socket.getOutputStream());
           	connected = true;
        } catch (Throwable e) {
        	Log.e(BIOHARNESS_ADDRESS, "Cannot connect to bluetooth", e);
			return;
        }
	}

	/** 
	 * Will cancel an in-progress connection, and close the socket 
	 */
	public void cancel() {
		if (inStream != null) {
			try {inStream.close();} catch (Exception e) {Log.e(BIOHARNESS_ADDRESS, "Cannot close mmSocket", e);}
	        inStream = null;
	    }
	
	    if (outStream != null) {
	    	try {outStream.close();} catch (Exception e) {Log.e(BIOHARNESS_ADDRESS, "Cannot close mmSocket", e);}
	        outStream = null;
	    }
	
	    if (socket != null) {
	        try {socket.close();} catch (Exception e) {Log.e(BIOHARNESS_ADDRESS, "Cannot close mmSocket", e);}
	        socket = null;
	    }
	    
	    connected = false;
	}
	
	/**
	 * Check if phone is connected to BioHarness sensor
	 * 
	 * @return true if the phone is connected to the sensor, false otherwise 
	 */
	public boolean isConnected() {
		return connected;
	}

	/**
	 * Read from BioHarness Sensor
	 * 
	 * @return The next byte in the sensor's buffer
	 */
	public byte readByte() {
		try {
			return inStream.readByte();
		} catch (IOException e) {
			Log.e(BIOHARNESS_ADDRESS, "Cannot read byte from input", e);
			connected = false;
			return 0;
		}
	}

	/**
	 * Tell BioHarness Sensor which packets to begin sending
	 */
	public void sendBytesToDevice(byte[] message) {
		if ((outStream != null)) {
			try {
				outStream.write(message);
			} catch (IOException ex) {
				Log.e(BIOHARNESS_ADDRESS, "Cannot send bytes to device", ex);
				connected = false;
			}
		}
	}
	
	public static boolean isBluetoothEnabled() {
		BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
		return (adapter.isEnabled());
	}
}