package usf.smartvitals.sensor;

import usf.smartvitals.comm.BluetoothConnection;
import static usf.smartvitals.utils.Values.*;
import android.util.Log;

/**Called from SensorManager.java. Its main purpose is to handle everything
 * incoming and outgoing to BioHarness sensor
 */
public class BioHarnessSensor extends Sensor {

	private BluetoothConnection btCon;
	private byte[] inputBuffer = new byte[256];
	private byte[] theLifeSignMessage = ByteOperations.createLifeSignMessage();
	private byte t = 0x00;
	private long currentDate = System.currentTimeMillis();
	private long lastDateSend = System.currentTimeMillis();
	private long testval = 0;

	public BioHarnessSensor() {
		super(0, NUMBER_OF_RECONNECTIONS);
		Log.i("SensorManager", "In BioHarnessSensor");
	}

	/**Connect to BioHarness sensor**/
	public boolean connect() {
		try {
			/**
			 * Open Android bluetooth and connections.
			 */
			btCon = new BluetoothConnection();
			btCon.connect();
			if (!btCon.isConnected()) {
				Log.e(BIOHARNESS_ADDRESS, "btCon is not connected", null);
				throw new Exception("Bluetooth wasn't connected");
			} else {
				active = true;
			}
			
			/* Send start packets to the sensor.*/
			if (BH_VITALS_SENSOR_ACTIVE) {
				setGenDataTransmitState(true);
			}
			if (BH_ACC_SENSOR_ACTIVE) {
				setAccDataTransmitState(true);
			}
			if (BH_EKG_SENSOR_ACTIVE) {
				setEcgDataTransmitState(true);
			}
			return true;
		} catch (Exception ex) {
			Log.e(BIOHARNESS_ADDRESS, "Exception thrown", ex);
			return false;
		}
	}

	/**Close connections to BioHarness sensor**/
	public void finalize() {
		setAccDataTransmitState(false);
		setGenDataTransmitState(false);
		setEcgDataTransmitState(false);
		
		btCon.cancel();
		active = false;
	}

	/**Check if phone is connected to BioHarness sensor**/
	@Override
	public boolean isConnected() {
		return btCon.isConnected();
	}
	/**
	 * This method is iteratively called from a runnable object.
	 * All the code to get the data from the sensor is implemented here.
	 */
	public Packet readData() {
		Packet packet = null;
		int i = 0;
		for (int j = 0; j < inputBuffer.length; j++) {
			inputBuffer[j] = 0x00;
		}
		try {
			while ((t = btCon.readByte()) != ByteOperations.STX && t != 0);
			if (t != 0) {
				inputBuffer[0] = ByteOperations.STX;
				inputBuffer[1] = btCon.readByte();
				inputBuffer[2] = btCon.readByte();
				int dlc = Integer.parseInt("" + inputBuffer[2]);
				i = 3;
				for (int j = 0; j < dlc; j++) {
					inputBuffer[j + 3] = btCon.readByte();
					i++;
				}
				inputBuffer[i] = btCon.readByte();

				i++;
				inputBuffer[i] = btCon.readByte();

				i++;

				if (inputBuffer[i - 1] == ByteOperations.ACK) {
					packet = decodePacket(inputBuffer);
				}
				lastDateSend = System.currentTimeMillis();

				testval = lastDateSend - currentDate;
				if (testval > TIMEOUT_BEACON) {
					currentDate = lastDateSend;
					/**Ping the BioHarness sensor to keep the connection alive**/
					if (theLifeSignMessage != null) {
						btCon.sendBytesToDevice(theLifeSignMessage);
					}
				}
			} else {
				//reconnect();  Sensor should not reconnect. Only set connected to false, so that the super class checks
				connected = false;				
			}
		} catch (Exception ex) {
			Log.e("BioHarnessSensor", "Error in decoding process", ex);
			active = false;
		}
		return packet;
	}
	
	/**Decipher the type of packet from the BioHarness sensor and decode it**/
	private Packet decodePacket(byte[] buff) {
		try {			
			if (buff[1] == 0x20) {
				return new BioHarnessGeneralPacket(trim(buff, BioHarnessGeneralPacket.GEN_PACKET_SIZE));
			} else if (buff[1] == 0x25) {
				return new BioHarnessAccelerometerPacket(trim(buff, BioHarnessAccelerometerPacket.ACCEL_PCKT_SIZE));
			} else if (buff[1] == 0x22) {
				return new BioHarnessECGPacket(trim(buff, BioHarnessECGPacket.ECG_PCKT_SIZE));
			}
		} catch (Throwable e) {
			Log.e(BIOHARNESS_ADDRESS, "Packet = null", e);
		}
		return null;
	}

	/**Tell BioHarness sensor to begin sending data for these packets**/
	public void setGenDataTransmitState(final boolean val) {
		byte theBytes[] = ByteOperations.setGeneralDataPacketEnabled(val);
		btCon.sendBytesToDevice(theBytes);
	}

	/**Tell BioHarness sensor to begin sending data for these packets**/
	public void setAccDataTransmitState(final boolean val) {
		byte theBytes[] = ByteOperations.setAccelerometerPacketEnabled(val);
		btCon.sendBytesToDevice(theBytes);
	}

	/**Tell BioHarness sensor to begin sending data for these packets**/
	public void setEcgDataTransmitState(final boolean val) {
		byte theBytes[] = ByteOperations.setECGPacketEnabled(val);
		btCon.sendBytesToDevice(theBytes);
	}
	
	private byte[] trim(byte[] arr, int size) {
		byte[] trimmedArr = new byte[size];
		for (int i = 0; i < size; i++) {
			trimmedArr[i] = arr[i];
		}
		return trimmedArr;
	}
}