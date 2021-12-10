package usf.smartvitals.json;
import java.io.Reader;
import java.lang.reflect.Type;
import com.google.gson.Gson;
import com.google.gson.reflect.*;
import usf.smartvitals.json.Models.AuthenticationToken;
import usf.smartvitals.json.Models.JsonResponse;
import usf.smartvitals.json.Models.Test;
import usf.smartvitals.json.Models.UserInfo;
import usf.smartvitals.utils.Values;

public class WebMethods {

	public static JsonResponse<AuthenticationToken> Login(String userName, String password, String deviceId)
	{
		 String url = Values.LOGIN + 
				 "?username="+userName+
				 "&password="+password+
				 "&deviceId=" + deviceId;
		 			
		 Gson gson = new Gson();
		 Reader reader = Helpers.GetReader(url);
		 Type tokenType = new TypeToken<JsonResponse<AuthenticationToken>>(){}.getType();
		 JsonResponse<AuthenticationToken> token = gson.fromJson(reader, tokenType);
		 return token;
	}
	
	public static JsonResponse<Boolean> Logout(String SessionGuid)
	{
		String url = Values.LOGOUT + 
				 "?SessionGuid="+SessionGuid;
		 			
		 Gson gson = new Gson();
		 Reader reader = Helpers.GetReader(url);
		 Type tokenType = new TypeToken<JsonResponse<Boolean>>(){}.getType();
		 JsonResponse<Boolean> token = gson.fromJson(reader, tokenType);
		 return token;
	}
	
	public static JsonResponse<Boolean> UpdateProfile(String SessionGuid, UserInfo Profile)
	{
		String url = Values.UPDATE_PROFILE+"?SessionGuid="
				+SessionGuid;
		Gson gson = new Gson();
		String json = gson.toJson(Profile);
		Reader reader = Helpers.PostReader(url,json);
		Type tokenType = new TypeToken<JsonResponse<Boolean>>(){}.getType();
		JsonResponse<Boolean> token = gson.fromJson(reader, tokenType);
		return token;
	}

	public static JsonResponse<AuthenticationToken> getAuthenticationToken(String SessionGuid) {
		String url = Values.AUTH_TOKEN+"?SessionGuid="
				+SessionGuid;
		Gson gson = new Gson();
		Reader reader = Helpers.GetReader(url);
		Type tokenType = new TypeToken<JsonResponse<AuthenticationToken>>(){}.getType();
		JsonResponse<AuthenticationToken> token = gson.fromJson(reader, tokenType);
		return token;
	}
	
	public static JsonResponse<Boolean> AddTest(String SessionGuid, Test Test) {
		String url = Values.ADD_TEST+"?SessionGuid="
				+SessionGuid;
		Gson gson = new Gson();
		String json = gson.toJson(Test);
		Reader reader = Helpers.PostReader(url,json);
		Type tokenType = new TypeToken<JsonResponse<Boolean>>(){}.getType();
		JsonResponse<Boolean> token = gson.fromJson(reader, tokenType);
		return token;
	}
}
