package usf.smartvitals.sensor;

import java.util.Random;

public class DummySensor extends Sensor {

	int[] heartRate = {60, 70, 80,90,100,110,120,130,140,150,160,170};//{98,97,96};
	int[] respRate = {280,279,278};
	int[] skin_temp = {300,310,320};
	int[] posture = {-22,-23,-22};
	int[] ecg_amp = {36,37,38};
	int[] gsr = {0,0,0};
	int[] spo2 = {0,0,0};
	int[] breath_amp = {46,49,52};
	int[] lateral_min = {-16,-14,-20};
	int[] lateral_max = {-16,-14,-20};
	int[] vertical_min = {-147,-158,-141};
	int[] vertical_max = {-57,-57,-56};
	int[] sagittal_min = {10,6,11};
	int[] sagittal_max = {72,69,81};
	int[] activity_level = {27,27,28};
	int[] peak_accel = {62,67,51};
	int[] blood_pressure = {0,0,0};
	int[] battery_charge_level = {31,31,31};
	
	public DummySensor() {
		super(500, 1000);
	}

	@Override
	public boolean connect() {
		active = true;
		return true;
	}

	@Override
	public void finalize() {
		active = false;
	}

	@Override
	public Packet readData() {
		BioHarnessGeneralPacket packet = new BioHarnessGeneralPacket();
		Random rand = new Random();
		
		packet.setBattDischargeLevel(battery_charge_level[rand.nextInt(3)]);
		packet.setBloodPressure(blood_pressure[rand.nextInt(3)]);
		packet.setBrearthAmplitude(breath_amp[rand.nextInt(3)]);
		packet.setEcgAmplitude(ecg_amp[rand.nextInt(3)]);
		packet.setGSR(gsr[rand.nextInt(3)]);
		packet.setHeartRate(heartRate[rand.nextInt(12)]);
		packet.setLateralAxeMin(lateral_min[rand.nextInt(3)]);
		packet.setLateralAxePeak(lateral_max[rand.nextInt(3)]);
		packet.setPeakAccel(peak_accel[rand.nextInt(3)]);
		packet.setPosture(posture[rand.nextInt(3)]);
		packet.setRespirationRate(respRate[rand.nextInt(3)]);
		packet.setSagitalAxeMin(sagittal_min[rand.nextInt(3)]);
		packet.setSagitalAxePeak(sagittal_max[rand.nextInt(3)]);
		packet.setSkinTemperature(skin_temp[rand.nextInt(3)]);
		packet.setSPo2(spo2[rand.nextInt(3)]);
		packet.setVerticalAxeMin(vertical_min[rand.nextInt(3)]);
		packet.setVerticalAxePeak(vertical_max[rand.nextInt(3)]);
		
		packet.setTimeStamp(System.currentTimeMillis());
		
		return packet;
	}

}
