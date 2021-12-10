package usf.smartvitals.json;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;

public class Helpers {

	 public static Reader GetReader(String url) {

		DefaultHttpClient client = new DefaultHttpClient();
		InputStream stream = null;
		HttpGet getRequest = new HttpGet(url);

		try {

			HttpResponse getResponse = client.execute(getRequest);
			final int statusCode = getResponse.getStatusLine().getStatusCode();

			if (statusCode == HttpStatus.SC_OK) {
				HttpEntity getResponseEntity = getResponse.getEntity();
				stream = getResponseEntity.getContent();
			}

		} catch (IOException e) {
			getRequest.abort();
			return null;
		}
		finally
		{
			client.getConnectionManager().shutdown();
		}
		Reader reader = new InputStreamReader(stream);
		return reader;

	}
	

	public static String URLEncode(String url) {
		String queryVars = url.split("?").length > 1 ? url.split("?")[1] : "";
		try {
			if (queryVars != "") {
				queryVars = java.net.URLEncoder.encode(queryVars, "ISO-8859-1");
				return url.split("?")[0] + queryVars;
			} else
				return url;
		} catch (UnsupportedEncodingException e) {
			return url;
		}
	}
	
	public static Reader PostReader(String url, String json)
	{
	    HttpClient httpClient = new DefaultHttpClient();
	    InputStream stream = null;
	    try {
	        HttpPost request = new HttpPost(url);
	        StringEntity params = new StringEntity(json);
	        request.addHeader("content-type", "application/json");
	        request.setEntity(params);
	        HttpResponse response = httpClient.execute(request);
	        final int statusCode = response.getStatusLine().getStatusCode();

			if (statusCode == HttpStatus.SC_OK) {
				HttpEntity getResponseEntity = response.getEntity();
				stream = getResponseEntity.getContent();
			}

			Reader reader = new InputStreamReader(stream);
			return reader;
			
	    }catch (Exception ex) {
	        return null;
	    } finally {
	        httpClient.getConnectionManager().shutdown();
	    }
	}

}
