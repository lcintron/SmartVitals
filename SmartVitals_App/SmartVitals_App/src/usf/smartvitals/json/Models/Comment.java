package usf.smartvitals.json.Models;

import com.google.gson.annotations.SerializedName;

public class Comment
{
	@SerializedName("CommentId")
    public int CommentId;
    
	@SerializedName("TestId")
	public int TestId;
	
	@SerializedName("Test")
    public Test Test;
	
	@SerializedName("Comment")
    public String Message;
	
	@SerializedName("UserName")
    public String UserName;
	
	@SerializedName("Date")
    public String Date;
}
