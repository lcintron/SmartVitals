package usf.smartvitals.json.Models;
import com.google.gson.annotations.*;

public class AuthenticationToken
{
	@SerializedName("IsAuthenticated")
    public boolean IsAuthenticated;
	
	@SerializedName("Message")
    public String Message;
	
	@SerializedName("Role")
    public String Role;

	@SerializedName("Profile")
	public UserInfo Profile;
	
	@SerializedName("SessionId")
	public String SessionId;
}
