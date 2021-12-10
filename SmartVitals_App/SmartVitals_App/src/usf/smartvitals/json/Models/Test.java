package usf.smartvitals.json.Models;

import java.util.ArrayList;

import com.google.gson.annotations.SerializedName;

public class Test
{
	@SerializedName("TestId")
    public int TestId;
	
	@SerializedName("UserName")
    public String UserName;
	
	@SerializedName("Samples")
    public ArrayList<Sample> Samples;
	
	@SerializedName("Description")
    public String Description;
	
	@SerializedName("Comments")
    public ArrayList<Comment> Comments;
    
	@SerializedName("Latitude")
	public double Latitude;
    
	@SerializedName("Longitude")
    public double Longitude;
	
	@SerializedName("Date")
	public java.util.Date Date;
}
