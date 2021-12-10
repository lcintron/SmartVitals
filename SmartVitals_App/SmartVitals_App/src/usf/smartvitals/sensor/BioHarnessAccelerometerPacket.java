package usf.smartvitals.sensor;

import java.util.Calendar;

/** 
 * A structure that represents the acceleration data collected by the Bioharness sensor 
 */
public class BioHarnessAccelerometerPacket extends Packet {

	private byte[] udpData; // The message to be sent to the server
	private byte[] accelData; // The data from 3d accel (w/0 headers, crc, stx,
								// etc)
	private int x[], y[], z[];
	private int maxX, maxY, maxZ;
	private int minX, minY, minZ;
	public static final int ACCEL_PCKT_SIZE = 89;
	private int year, month, day;
	private long millis;

	/**
	 * Creates a BioharnessAccelerometerPacket that contains the given raw data
	 * 
	 * @param rawData a byte array containing accelerometer data
	 */
	public BioHarnessAccelerometerPacket(byte[] rawData) {
		super(rawData);
		this.accelData = new byte[75];
		for (int i = 12; i < 87; i++) {
			accelData[i - 12] = rawData[i];
		}
		x = new int[20];
		y = new int[20];
		z = new int[20];
		decode();
		computeMaxMin();
	}

	public void decode() {
		year = ByteOperations.mergeUnsigned(rawData[4], rawData[5]);
		month = Integer.parseInt("" + rawData[6]) - 1;
		day = Integer.parseInt("" + rawData[7]);
		millis = (int) (rawData[8] + ((rawData[9] & 0xFF) << 8) + ((rawData[10] & 0xFF) << 16) + ((rawData[11] & 0xFF) << 24));

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
		boolean[] bits = ByteOperations.bytesToBits(accelData);

		int k = 0;
		for (int i = 0; i < bits.length - 10; i += 10) {
			boolean[] field = new boolean[10];
			for (int j = 0; j < 10; j++) {
				field[j] = bits[i + j];
			}
			int value = ByteOperations.binaryToInt(field);

			switch (k % 3) {
			case 0:
				x[k / 3] = value;
				break;
			case 1:
				y[k / 3] = value;
				break;
			case 2:
				z[k / 3] = value;
				break;
			}
			k++;
		}
	}

	public void computeMaxMin() {
		maxX = Integer.MIN_VALUE;
		maxY = Integer.MIN_VALUE;
		maxZ = Integer.MIN_VALUE;

		minX = Integer.MAX_VALUE;
		minY = Integer.MAX_VALUE;
		minZ = Integer.MAX_VALUE;

		for (int i = 0; i < 20; i++) {
			if (x[i] < minX) {
				minX = x[i];
			}
			if (x[i] > maxX) {
				maxX = x[i];
			}
			if (y[i] < minY) {
				minY = y[i];
			}
			if (y[i] > maxY) {
				maxY = y[i];
			}
			if (z[i] < minZ) {
				minZ = z[i];
			}
			if (z[i] > maxZ) {
				maxZ = z[i];
			}
		}
	}

	public int[] getX() {
		return x;
	}

	public int[] getY() {
		return y;
	}

	public int[] getZ() {
		return z;
	}

	public int getMaxX() {
		return maxX;
	}

	public int getMaxY() {
		return maxY;
	}

	public int getMaxZ() {
		return maxZ;
	}

	public int getMinX() {
		return minX;
	}

	public int getMinY() {
		return minY;
	}

	public int getMinZ() {
		return minZ;
	}

	public String getAccelerometerBounds() {
		return minX + ", " + maxX + ", " + minY + ", " + maxY + ", " + minZ
				+ ", " + maxZ;
	}

	public String getAllData() {
		String str = "";
		for (int i = 0; i < 20; i++) {
			str += "\n(" + x[i] + ", " + y[i] + ", " + z[i] + ")";
		}
		return str;
	}

	public String getBounds() {
		return "(" + maxX + ", " + maxY + ", " + maxZ + ")";
	}

	public void setUDPData(byte[] udpData) {
		this.udpData = udpData;
	}

	public byte[] getUDPData() {
		return udpData;
	}

	public int getYear() {
		return year;
	}

	public int getDay() {
		return day;
	}

	public int getMonth() {
		return month;
	}

	public long getMillis() {
		return millis;
	}

	public String toString() {
		return "Max: (" + maxX + ", " + maxY + ", " + maxZ + "). Min: " + minX
				+ ", " + minY + ", " + minZ + ").";
	}

	@Override
	public int getPacketSize() {
		return ACCEL_PCKT_SIZE;
	}
}