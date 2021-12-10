package usf.smartvitals.sensor;

import java.util.Calendar;

import android.util.Log;

/** 
* A structure that represents data collected by the Bioharness sensor 
*/
public class BioHarnessGeneralPacket extends Packet {

	private int sequenceNumber;
	private long date;

	private int heartRate;
	private int respirationRate;
	private int skinTemperature;

	private int posture;
	private int vmu;

	private int peakAccel;
	private int batteryVoltage;

	private int brearthAmplitude;
	private int ecgAmplitude;
	private int ecgNoise;

	private int verticalAxeMin;
	private int verticalAxePeak;

	private int lateralAxeMin;
	private int lateralAxePeak;

	private int sagitalAxeMin;
	private int sagitalAxePeak;

	private int zephySystemChannel;

	private int GSR;
	private int SPo2;
	private int bloodPressure;

	private int ROG;
	private int ALARM;

	private boolean isPhysiologicalSensorWorn;
	private boolean isButtonPressed;

	private int battDischargeLevel;

	private float latitude, longitude;

	private byte[] udpData;

	private double gpsSpeed;

	private int year, month, day, millis;

	public static final int GEN_PACKET_SIZE = 58;

	public BioHarnessGeneralPacket(byte[] rawData) {
		super(rawData);
		Log.i("BioHarness Address", "In BioharnessGeneralPacket Constructor");
		decode();
	}
	

	public BioHarnessGeneralPacket() {}


	public void decode() {
		sequenceNumber = Integer.parseInt("" + rawData[3]);

		year = ByteOperations.mergeUnsigned(rawData[4], rawData[5]);
		month = Integer.parseInt("" + rawData[6]) - 1;
		day = Integer.parseInt("" + rawData[7]);
		millis = (int) (rawData[8] + ((rawData[9] & 0xFF) << 8) + ((rawData[10]& 0xFF) << 16) + ((rawData[11] & 0xFF) << 24));
		Calendar calendar = Calendar.getInstance();

		calendar.set(Calendar.YEAR, year);
		calendar.set(Calendar.MONTH, month);
		calendar.set(Calendar.DATE, day);
		calendar.set(Calendar.HOUR, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);

		timeStamp = calendar.getTime().getTime() + millis;
		heartRate = ByteOperations.mergeUnsigned(rawData[12], rawData[13]);
		respirationRate = ByteOperations
				.mergeUnsigned(rawData[14], rawData[15]);
		skinTemperature = ByteOperations
				.mergeUnsigned(rawData[16], rawData[17]);
		posture = ByteOperations.merge(rawData[18], rawData[19]);
		vmu = ByteOperations.mergeUnsigned(rawData[20], rawData[21]);
		peakAccel = ByteOperations.mergeUnsigned(rawData[22], rawData[23]);
		batteryVoltage = ByteOperations.mergeUnsigned(rawData[24], rawData[25]);
		brearthAmplitude = ByteOperations.mergeUnsigned(rawData[26],
				rawData[27]);
		ecgAmplitude = ByteOperations.mergeUnsigned(rawData[28], rawData[29]);
		ecgNoise = ByteOperations.mergeUnsigned(rawData[30], rawData[31]);
		verticalAxeMin = ByteOperations.merge(rawData[32], rawData[33]);
		verticalAxePeak = ByteOperations.merge(rawData[34], rawData[35]);
		lateralAxeMin = ByteOperations.merge(rawData[36], rawData[37]);
		lateralAxePeak = ByteOperations.merge(rawData[38], rawData[39]);
		sagitalAxeMin = ByteOperations.merge(rawData[40], rawData[41]);
		sagitalAxePeak = ByteOperations.merge(rawData[42], rawData[43]);
		zephySystemChannel = ByteOperations.mergeUnsigned(rawData[44],
				rawData[45]);
		GSR = ByteOperations.mergeUnsigned(rawData[46], rawData[47]);
		SPo2 = ByteOperations.mergeUnsigned(rawData[48], rawData[49]);
		bloodPressure = ByteOperations.mergeUnsigned(rawData[50], rawData[51]);

		battDischargeLevel = ByteOperations.mergeUnsigned(rawData[54],
				(byte) 0x00);// Integer.parseInt(""+buff[54]);

		isPhysiologicalSensorWorn = (rawData[55] & 0x80) == 0x80;

		isButtonPressed = (rawData[55] & 0x40) == 0x40;
		System.out.println(this);
	}

	/**
	 * @return the sequenceNumber
	 */
	public int getSequenceNumber() {
		return sequenceNumber;
	}

	public float getLatitude() {
		return latitude;
	}

	public float getLongitude() {
		return longitude;
	}

	public void setLatitude(float lat) {
		this.latitude = lat;
	}

	public void setLongitude(float lon) {
		this.longitude = lon;
	}

	/**
	 * @param sequenceNumber
	 *            the sequenceNumber to set
	 */
	public void setSequenceNumber(int sequenceNumber) {
		this.sequenceNumber = sequenceNumber;
	}

	/**
	 * @return the date
	 */
	public long getDate() {
		return date;
	}

	/**
	 * @param date
	 *            the date to set
	 */
	public void setDate(long date) {
		this.date = date;
	}

	/**
	 * @return the heartRate
	 */
	public int getHeartRate() {
		return heartRate;
	}

	/**
	 * @param heartRate
	 *            the heartRate to set
	 */
	public void setHeartRate(int heartRate) {
		this.heartRate = heartRate;
	}

	/**
	 * @return the respirationRate
	 */
	public int getRespirationRate() {
		return respirationRate;
	}

	/**
	 * @param respirationRate
	 *            the respirationRate to set
	 */
	public void setRespirationRate(int respirationRate) {
		this.respirationRate = respirationRate;
	}

	/**
	 * @return the skinTemperature
	 */
	public int getSkinTemperature() {
		return skinTemperature;
	}

	/**
	 * @param skinTemperature
	 *            the skinTemperature to set
	 */
	public void setSkinTemperature(int skinTemperature) {
		this.skinTemperature = skinTemperature;
	}

	/**
	 * @return the posture
	 */
	public int getPosture() {
		return posture;
	}

	/**
	 * @param posture
	 *            the posture to set
	 */
	public void setPosture(int posture) {
		this.posture = posture;
	}

	/**
	 * @return the vmu
	 */
	public int getVmu() {
		return vmu;
	}

	/**
	 * @param vmu
	 *            the vmu to set
	 */
	public void setVmu(int vmu) {
		this.vmu = vmu;
	}

	/**
	 * @return the peakAccel
	 */
	public int getPeakAccel() {
		return peakAccel;
	}

	/**
	 * @param peakAccel
	 *            the peakAccel to set
	 */
	public void setPeakAccel(int peakAccel) {
		this.peakAccel = peakAccel;
	}

	/**
	 * @return the batteryVoltage
	 */
	public int getBatteryVoltage() {
		return batteryVoltage;
	}

	/**
	 * @param batteryVoltage
	 *            the batteryVoltage to set
	 */
	public void setBatteryVoltage(int batteryVoltage) {
		this.batteryVoltage = batteryVoltage;
	}

	/**
	 * @return the brearthAmplitude
	 */
	public int getBreathAmplitude() {
		return brearthAmplitude;
	}

	/**
	 * @param brearthAmplitude
	 *            the brearthAmplitude to set
	 */
	public void setBrearthAmplitude(int brearthAmplitude) {
		this.brearthAmplitude = brearthAmplitude;
	}

	/**
	 * @return the ecgAmplitude
	 */
	public int getEcgAmplitude() {
		return ecgAmplitude;
	}

	/**
	 * @param ecgAmplitude
	 *            the ecgAmplitude to set
	 */
	public void setEcgAmplitude(int ecgAmplitude) {
		this.ecgAmplitude = ecgAmplitude;
	}

	/**
	 * @return the ecgNoise
	 */
	public int getEcgNoise() {
		return ecgNoise;
	}

	/**
	 * @param ecgNoise
	 *            the ecgNoise to set
	 */
	public void setEcgNoise(int ecgNoise) {
		this.ecgNoise = ecgNoise;
	}

	/**
	 * @return the verticalAxeMin
	 */
	public int getVerticalAxeMin() {
		return verticalAxeMin;
	}

	/**
	 * @param verticalAxeMin
	 *            the verticalAxeMin to set
	 */
	public void setVerticalAxeMin(int verticalAxeMin) {
		this.verticalAxeMin = verticalAxeMin;
	}

	/**
	 * @return the verticalAxePeak
	 */
	public int getVerticalAxePeak() {
		return verticalAxePeak;
	}

	/**
	 * @param verticalAxePeak
	 *            the verticalAxePeak to set
	 */
	public void setVerticalAxePeak(int verticalAxePeak) {
		this.verticalAxePeak = verticalAxePeak;
	}

	/**
	 * @return the lateralAxeMin
	 */
	public int getLateralAxeMin() {
		return lateralAxeMin;
	}

	/**
	 * @param lateralAxeMin
	 *            the lateralAxeMin to set
	 */
	public void setLateralAxeMin(int lateralAxeMin) {
		this.lateralAxeMin = lateralAxeMin;
	}

	/**
	 * @return the lateralAxePeak
	 */
	public int getLateralAxePeak() {
		return lateralAxePeak;
	}

	/**
	 * @param lateralAxePeak
	 *            the lateralAxePeak to set
	 */
	public void setLateralAxePeak(int lateralAxePeak) {
		this.lateralAxePeak = lateralAxePeak;
	}

	/**
	 * @return the sagitalAxeMin
	 */
	public int getSagitalAxeMin() {
		return sagitalAxeMin;
	}

	/**
	 * @param sagitalAxeMin
	 *            the sagitalAxeMin to set
	 */
	public void setSagitalAxeMin(int sagitalAxeMin) {
		this.sagitalAxeMin = sagitalAxeMin;
	}

	/**
	 * @return the sagitalAxePeak
	 */
	public int getSagitalAxePeak() {
		return sagitalAxePeak;
	}

	/**
	 * @param sagitalAxePeak
	 *            the sagitalAxePeak to set
	 */
	public void setSagitalAxePeak(int sagitalAxePeak) {
		this.sagitalAxePeak = sagitalAxePeak;
	}

	/**
	 * @return the zephySystemChannel
	 */
	public int getZephySystemChannel() {
		return zephySystemChannel;
	}

	/**
	 * @param zephySystemChannel
	 *            the zephySystemChannel to set
	 */
	public void setZephySystemChannel(int zephySystemChannel) {
		this.zephySystemChannel = zephySystemChannel;
	}

	/**
	 * @return the GSR
	 */
	public int getGSR() {
		return GSR;
	}

	/**
	 * @param GSR
	 *            the GSR to set
	 */
	public void setGSR(int GSR) {
		this.GSR = GSR;
	}

	/**
	 * @return the SPo2
	 */
	public int getSPo2() {
		return SPo2;
	}

	/**
	 * @param SPo2
	 *            the SPo2 to set
	 */
	public void setSPo2(int SPo2) {
		this.SPo2 = SPo2;
	}

	/**
	 * @return the bloodPressure
	 */
	public int getBloodPressure() {
		return bloodPressure;
	}

	/**
	 * @param bloodPressure
	 *            the bloodPressure to set
	 */
	public void setBloodPressure(int bloodPressure) {
		this.bloodPressure = bloodPressure;
	}

	/**
	 * @return the ROG
	 */
	public int getROG() {
		return ROG;
	}

	/**
	 * @param ROG
	 *            the ROG to set
	 */
	public void setROG(int ROG) {
		this.ROG = ROG;
	}

	/**
	 * @return the ALARM
	 */
	public int getALARM() {
		return ALARM;
	}

	/**
	 * @param ALARM
	 *            the ALARM to set
	 */
	public void setALARM(int ALARM) {
		this.ALARM = ALARM;
	}

	/**
	 * @return the isPhysiologicalSensorWorn
	 */
	public boolean isIsPhysiologicalSensorWorn() {
		return isPhysiologicalSensorWorn;
	}

	/**
	 * @param isPhysiologicalSensorWorn
	 *            the isPhysiologicalSensorWorn to set
	 */
	public void setIsPhysiologicalSensorWorn(boolean isPhysiologicalSensorWorn) {
		this.isPhysiologicalSensorWorn = isPhysiologicalSensorWorn;
	}

	/**
	 * @return the battDischargeLevel
	 */
	public int getBattDischargeLevel() {
		return battDischargeLevel;
	}

	/**
	 * @param battDischargeLevel
	 *            the battDischargeLevel to set
	 */
	public void setBattDischargeLevel(int battDischargeLevel) {
		this.battDischargeLevel = battDischargeLevel;
	}

	/**
	 * @return the isButtonPressed
	 */
	public boolean isIsButtonPressed() {
		return isButtonPressed;
	}

	/**
	 * @param isButtonPressed
	 *            the isButtonPressed to set
	 */
	public void setIsButtonPressed(boolean isButtonPressed) {
		this.isButtonPressed = isButtonPressed;
	}

	public String toString() {
		String string = "HR: " + heartRate + ". RR: " + respirationRate
				+ ". Temp: " + this.skinTemperature + ". Posture: "
				+ this.posture + ". Charge: " + this.battDischargeLevel;

		return string;
	}

	public byte[] getRawData() {
		return rawData;
	}

	public String getAccelData() {
		double avgLateral = (lateralAxeMin + lateralAxePeak) / 2;
		double avgVertical = (verticalAxeMin + verticalAxePeak) / 2;
		double avgSagital = (sagitalAxeMin + sagitalAxePeak) / 2;
		
		return avgLateral/10 + ", " + avgVertical/10 + ", " + avgSagital;
	}

	public void setUDPData(byte[] udpData) {
		this.udpData = udpData;
	}

	public byte[] getUDPData() {
		return udpData;
	}

	public double getGPSSpeed() {
		return gpsSpeed;
	}

	public void setGPSSpeed(double gpsSpeed) {
		this.gpsSpeed = gpsSpeed;
	}

	public void setYear(int year) {
		this.year = year;
	}

	public void setMonth(int month) {
		this.month = month;
	}

	public void setDay(int day) {
		this.day = day;
	}

	public void setMillis(int millis) {
		this.millis = millis;
	}

	public int getMillis() {
		return millis;
	}

	public int getDay() {
		return day;
	}

	public int getMonth() {
		return month;
	}

	public int getYear() {
		return year;
	}

	public int getPacketSize() {
		return GEN_PACKET_SIZE;
	}
}