package usf.smartvitals.json.Models;

import com.google.gson.annotations.SerializedName;

public class UserInfo {
	@SerializedName("UserName")
	public String UserName;
	
	@SerializedName("Name")
    public String Name;
	
	@SerializedName("Email")
    public String Email;
	
	@SerializedName("Sex")
    public String Sex;
	
	@SerializedName("Height")
    public Height Height;
	
	@SerializedName("Weight")
    public int Weight;
	
	@SerializedName("Age")
    public int Age;
	
	@SerializedName("Phone")
    public String Phone;
	
	@SerializedName("Preferences")
    public String Preferences;
}
