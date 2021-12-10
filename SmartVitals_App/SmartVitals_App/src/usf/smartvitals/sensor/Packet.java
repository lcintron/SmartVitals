package usf.smartvitals.sensor;

/**
 * A packet contains environmental data (vital signs, acceleration, etc) 
 * that is used for Human Activity Recognition
 */
public abstract class Packet {

	protected byte[] rawData;
	
	protected long timeStamp;
	
	/**
	 * 
	 * @param rawData Data collected by some sensor
	 */
	public Packet(byte[] rawData) {
		if (rawData == null) {
			throw new IllegalArgumentException("Raw data cannot be null!");
		}
		this.rawData = new byte[rawData.length];
		for (int i = 0; i < rawData.length; i++) {
			this.rawData[i] = rawData[i];
		}
	}

	public Packet() {}
	
	/**
	 * Converts the byte representation of data into the appropriate data type (int, long, float, etc)
	 */
	public abstract void decode();

	/**
	 * @return The size of the packet in bytes
	 */
	public abstract int getPacketSize();

	public byte[] getRawData() {
		return rawData;
	}

	/**
	 * 
	 * @return The time where the data contained in the packet was obtained
	 */
	public long getTimeStamp() {
		return timeStamp;
	}

	/**
	 * 
	 * @param timeStamp The time where the data contained in the packet was obtained
	 */
	public void setTimeStamp(long timeStamp) {
		this.timeStamp = timeStamp;
	}
}
