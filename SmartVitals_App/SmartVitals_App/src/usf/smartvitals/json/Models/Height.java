package usf.smartvitals.json.Models;
import com.google.gson.annotations.*;

public class Height {
	@SerializedName("Feet")
	public int Feet;
	
	@SerializedName("Inches")
	public int Inches;
}
