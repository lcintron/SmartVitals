package usf.smartvitals.sensor;

public class ByteOperations {

	public final static byte STX = 0x02; /* Start of Message */

	public final static byte ETX = 0x03; /* End of Message */

	public static byte CRC8 = 0x5e;
	public final static byte ACK = 0x06;
	public final static byte NACK = 0x21;
	public final static int checksumPolynomial = 0x8C;

	/** CRC check taken from Zephyr PDF's */
	public static int getCurrentCRC(int currentChecksum, int newByte) {
		currentChecksum = (currentChecksum ^ newByte);
		for (int bit = 0; bit < 8; bit++) {
			if ((currentChecksum & 1) == 1) {
				currentChecksum = ((currentChecksum >> 1) ^ checksumPolynomial);
			} else {
				currentChecksum = (currentChecksum >> 1);
			}
		}
		return currentChecksum;
	}

	public static byte[] createLifeSignMessage() {
		byte[] data = new byte[6];
		data[0] = STX;
		data[1] = 0x23;
		data[2] = 0x00;
		data[3] = 0x5e;
		data[4] = ETX;
		return data;
	}

	public static byte[] setGeneralDataPacketEnabled(boolean val) {
		byte[] data = { STX, 0x14, 0x01, (val ? (byte) 0x01 : 0x00), CRC8, ETX };
		return data;
	}

	public static byte[] setAccelerometerPacketEnabled(boolean val) {
		byte[] data = { STX, 0x1E, 0x01, val ? (byte) 0x01 : 0x00, 0x5e, ETX };
		return data;
	}

	public static byte[] setECGPacketEnabled(boolean val) {
		byte[] data = { STX, 0x16, 0x01, val ? (byte) 0x01 : 0x00, 0x5e, ETX };
		return data;
	}

	public static byte[] setHRPacketEnabled(boolean val) {
		byte[] data = { STX, (byte) -112, 0x01, val ? (byte) 0x01 : 0x00, 0x5e,
				ETX };
		return data;
	}

	public static String byteToHex(byte data) {
		StringBuffer buf = new StringBuffer();
		buf.append(toHexChar((data >>> 4) & 0x0F));
		buf.append(toHexChar(data & 0x0F));
		return buf.toString();
	}

	public static char toHexChar(int i) {
		if ((0 <= i) && (i <= 9)) {
			return (char) ('0' + i);
		} else {
			return (char) ('a' + (i - 10));
		}
	}

	/**
	 * Merge two bytes into a signed 2's complement integer
	 * 
	 * @param low
	 *            byte is LSB
	 * @param high
	 *            byte is the MSB
	 * @return a signed int value
	 */
	public static int mergeUnsigned(byte low, byte high) {
		int lower = (0x000000FF & ((int) low));
		int upper = (0x000000FF & ((int) high));
		return lower + 256 * upper;
	}

	/**
	 * Merge two bytes into a unsigned integer
	 * 
	 * @param low
	 *            byte is LSB
	 * @param high
	 *            byte is the MSB
	 * @return an unsigned int value
	 */
	public static int merge(byte low, byte high) {
		int b = 0;
		int lower = (0x000000FF & ((int) low));
		b += (high << 8) + lower;
		if ((high & 0x80) != 0) {
			b = -(0xffffffff - b);
		}
		return b;
	}

	public static boolean[] bytesToBits(byte[] bytes) {
		boolean[] bits = new boolean[bytes.length * 8];
		int k = 0;
		int mask = 1;
		for (int i = 0; i < bytes.length; i++) {
			int b = ByteOperations.mergeUnsigned(bytes[i], (byte) 0);
			mask = 1;
			for (int j = 0; j < 8; j++) {
				bits[k++] = (b & mask) > 0;
				// System.out.println(mask + " & " + b + ": " + bits[k-1]);
				mask *= 2;
			}
		}
		// printBits(bits);

		return bits;
	}

	public static void printBits(boolean[] bits) {
		int h = 0;
		for (int i = 0; i < bits.length; i++) {
			System.out.print(bits[i] ? 1 : 0);
			if (++h % 8 == 0) {
				System.out.print(" ");
			}
		}
	}

	public static int binaryToInt(boolean[] bits) {
		/*
		 * System.err.println("Bits: " + bits.length); for (int i = 0; i <
		 * bits.length; i++) { System.err.print(bits[i] ? "1 " : "0 " ); } try {
		 * Thread.sleep(1000); } catch (Exception e) {
		 * 
		 * } int low = 0; for (int i = 0; i < 8; i++) { if (bits[i]) { low += 1
		 * << i; } } int high = 0; for (int i = 8; i < bits.length; i++) { if
		 * (bits[i]) { high += 1 << (i - 8); } } System.err.println ("Low: " +
		 * low + ". High: " + high); int value = merge((byte) low, (byte) high);
		 * 
		 * System.out.println("Value = " + value); return value;
		 */
		// System.out.println("\n");
		// for (int i = 0; i < bits.length; i++) {
		// System.err.print(bits[i] ? "1 " : "0 " );
		// }
		try {
			// Thread.sleep(500);
		} catch (Exception e) {

		}
		int unsigned = 0;
		for (int i = 0; i < bits.length; i++) {
			if (bits[i]) {
				unsigned += 1 << i;
			}
		}
		// System.out.println("Unsigned = " + unsigned);
		@SuppressWarnings("unused")
		int value = 0;
		int phase = 1 << (bits.length - 1);
		if (unsigned >= phase) {
			value = -phase + (unsigned - phase);
		} else {
			value = unsigned;
		}
		return unsigned;
	}

	public static long CRC8(int[] bytes) {
		long pol = 0x8C;
		long polsize = 8;
		long all = 0;
		long exp = 0;
		long bit = 8 * bytes.length - 1;
		for (int i = 0; i < bytes.length; i++) {
			all += bytes[i] * (1 >> (8 * exp++));
		}

		// bit = 13;
		// all = 13548;
		// pol = 11;
		// polsize = 4;

		System.out.println("All: " + all);

		while (bit >= polsize - 1) {
			if (bitAt(all, bit)) {
				long div = pol << (bit - polsize + 1);
				System.out.print("Bit: " + bit + ". All: " + all + ". Div: "
						+ div);
				all = all ^ div;
				System.out.println(". XOR: " + all);
			} else {

			}
			bit--;
		}
		return all;
	}

	public static boolean bitAt(long value, long bit) {
		return (value & 1 << bit) > 0;
	}

	public static byte[] appendByteAtBeginning(byte[] bytes, byte b) {
		if (bytes == null) {
			return null;
		}
		for (int i = bytes.length - 1; i > 0; i--) {
			bytes[i] = bytes[i - 1];
		}
		bytes[0] = b;
		return bytes;
	}
}
