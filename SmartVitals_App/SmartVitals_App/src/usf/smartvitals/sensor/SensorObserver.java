package usf.smartvitals.sensor;
import java.util.Observable;
import java.util.Observer;

import usf.smartvitals.gui.StartActivity;
import usf.smartvitals.utils.Values;

public class SensorObserver implements Observer {

	private StartActivity ui;
	
	private Sensor sensor;
	private Thread BH;
	protected static SensorObserver observer = null;
	
	private SensorObserver() {
		if (Values.DUMMY_SENSOR_ACTIVE) {
			sensor = new DummySensor();
		} else {
			sensor = new BioHarnessSensor();
		}
	}

	public static SensorObserver getSensorObserver(StartActivity ui) {
		if (observer == null)
			observer = new SensorObserver();
		observer.ui = ui;
		return observer;
	}
	
	public void startSensor() {
		sensor.addObserver(this);
		BH = new Thread(sensor);
		BH.start();
	}
	
	public void stopSensor() {
		sensor.setActive(false);
	}
	
	@Override
	public void update(Observable sensor, Object packet) {
		if (packet != null) {
			ui.updateGraph((Packet) packet);
			if (packet instanceof BioHarnessGeneralPacket) 
				ui.updateVitalSigns((BioHarnessGeneralPacket) packet);
		}
	}
	
	public boolean sensorOn() {
		return sensor.isConnected();
	}
}
