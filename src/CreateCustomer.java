import oauth.signpost.OAuthConsumer;
import oauth.signpost.commonshttp.CommonsHttpOAuthConsumer;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class CreateCustomer {
	
	static String consumerKeyStr = "AtL2ARWmFUs7PdUWyIhCoVEXN";
	static String consumerSecretStr = "2fes5DNxHrtmAwUwnkzDQD1INXTkRQTU0pVRHBewHV3mfhwbH2";
	static String accessTokenStr = "2212631916-CUrT1Dq1MJ8UrAXCCphg5AhOuFL7h84kcejdq6n";
	static String accessTokenSecretStr = "ZaYcqMFD7VfuGcBOI4nwOO0XyDid5exYsgVZXcdfDGXeH";


	public static void main(String[] args) throws Exception {
		OAuthConsumer oAuthConsumer = new CommonsHttpOAuthConsumer(consumerKeyStr,consumerSecretStr);
		oAuthConsumer.setTokenWithSecret(accessTokenStr, accessTokenSecretStr);

		/*HttpPost httpPost = new HttpPost(
				"https://api.twitter.com/1.1/statuses/update.json?status=Test%20Twitter%20API.");
        */
		
		
		HttpGet httpGet = new HttpGet("https://api.twitter.com/1.1/statuses/mentions_timeline.json?count=20&include_rts=false&trim_user=true");
		oAuthConsumer.sign(httpGet);
		JSONParser arrayparser = new JSONParser();
		HttpClient httpClient = new DefaultHttpClient();
		HttpResponse httpResponse = httpClient.execute(httpGet);
		HttpEntity responseEntity = httpResponse.getEntity();
	    String response = EntityUtils.toString(responseEntity);
	    JSONObject jsonobject = (JSONObject) arrayparser.parse(response);
	    System.out.println(response);
	    Set<String> keys = json_obj.keySet();
	    
	    for (String key : keys) {
	     System.out.println(key + " : " + json_obj.get(key)); 
	    }
	    
        
	}
}