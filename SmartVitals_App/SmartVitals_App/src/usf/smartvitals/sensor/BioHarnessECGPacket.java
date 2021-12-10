package usf.smartvitals.sensor;

import java.util.Calendar;

/** 
 * A structure that represents the ECG data collected by the Bioharness sensor 
 */
public class BioHarnessECGPacket extends Packet {

	private byte[] udpData; // The message to be sent to the server
	private byte[] ecgData; // The data from accelerometer (w/0 headers, crc,
							// stx, etc)
	private int samples[];
	public static final int ECG_PCKT_SIZE = 93;

	/**
	 * Creates a BioharnessECGPacket that contains the given raw data
	 * 
	 * @param rawData a byte array containing ECG data
	 */
	public BioHarnessECGPacket(byte[] rawData) {
		super(rawData);
		this.ecgData = new byte[79/*******/
		];
		for (int i = 12; i < 91; i++) {
			ecgData[i - 12] = rawData[i];
		}
		samples = new int[63];
		decode();
	}

	public void decode() {
		int year = ByteOperations.mergeUnsigned(rawData[4], rawData[5]);
		int month = Integer.parseInt("" + rawData[6]) - 1;
		int day = Integer.parseInt("" + rawData[7]);
		int millis = (int) (rawData[8] + ((rawData[9] & 0xFF) << 8)
				+ ((rawData[10] & 0xFF) << 16) + ((rawData[11] & 0xFF) << 24));

		Calendar calendar = Calendar.getInstance();

		calendar.set(Calendar.YEAR, year);
		calendar.set(Calendar.MONTH, month);
		calendar.set(Calendar.DATE, day);
		calendar.set(Calendar.HOUR, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);

		timeStamp = calendar.getTime().getTime() + millis;
		System.out.println("Year: " + year);
		System.out.println("Month: " + month);
		System.out.println("Day: " + day);
		boolean[] bits = ByteOperations.bytesToBits(ecgData);

		int k = 0;
		for (int i = 0; i < bits.length - 10; i += 10) {
			boolean[] field = new boolean[10];
			for (int j = 0; j < 10; j++) {
				field[j] = bits[i + j];
			}
			samples[k] = ByteOperations.binaryToInt(field);
			k++;
		}
	}

	public byte[] getRawData() {
		return rawData;
	}

	public void setUDPData(byte[] udpData) {
		this.udpData = udpData;
	}

	public byte[] getUDPData() {
		return udpData;
	}

	public int[] getSamples() {
		return samples;
	}

	public int getPacketSize() {
		return ECG_PCKT_SIZE;
	}
}