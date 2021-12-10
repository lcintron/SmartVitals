package usf.smartvitals.json.Models;

import com.google.gson.annotations.SerializedName;

public class Sample {
	
	@SerializedName("SampleID")
	public int SampleID;
	
	@SerializedName("TestId")
    public int TestID;
	
	@SerializedName("Time")
    public String Time;
	
	@SerializedName("HeartRate")
    public double HeartRate;
	
	@SerializedName("EKG")
    public double EKG;
	
	@SerializedName("BloodPressure")
    public double BloodPressure;
	
	@SerializedName("SkinTemperature")
    public double SkinTemperature;
	
	@SerializedName("RespirationRate")
    public double RespirationRate;
	
	@SerializedName("Posture")
    public double Posture;
	
	@SerializedName("AccX")
    public double AccX;
	
	@SerializedName("AccY")
    public double AccY;
	
	@SerializedName("AccZ")
    public double AccZ;
}
