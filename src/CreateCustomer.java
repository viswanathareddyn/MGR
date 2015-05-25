import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import oauth.signpost.OAuthConsumer;
import oauth.signpost.commonshttp.CommonsHttpOAuthConsumer;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

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
		JSONParser jsonparser = new JSONParser();
		HttpClient httpClient = new DefaultHttpClient();
		HttpResponse httpResponse = httpClient.execute(httpGet);
		HttpEntity responseEntity = httpResponse.getEntity();
		String response = EntityUtils.toString(responseEntity);
		//HashMap<String,String> email = new HashMap<String, String>();  
		Set<String> email = new HashSet<String>();
		//System.out.println("String: " + response.replace('[', '\0').replace(']', '\0'));
		//System.out.println("String: " + response);
		//System.out.println("json str : " + jsonarray.get(0).toString());
		//System.out.println(jsonarray.get(0).toString().equals(response));
		try {
			JSONArray jsonarray = (JSONArray) jsonparser.parse(response);
			for (int i = 0; i <jsonarray.size();i++)
			{
				JSONObject jsonObject = (JSONObject) jsonarray.get(i);
				System.out.println(jsonObject.get("text"));
				String temp =(String)jsonObject.get("text");
				String patternValue = null;
				String patternToMatch = "[a-zA-Z0-9_.+-]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+";
				if(temp.contains("#trymaas"))
				{
					Pattern pattern = Pattern.compile(patternToMatch);

					// Now create matcher object.
					Matcher matcher = pattern.matcher(temp);
					while (matcher.find())
					{
						patternValue = matcher.group(0);
						email.add(patternValue);
						break;
					}

				}

			}

			System.out.println(email);
			CallMaaSWebService cmws = new CallMaaSWebService();
			String authtoken = cmws.callws();
			System.out.println("emailscount: "+email.size());
		
			for ( String s : email)
			{
				System.out.println(s);
				cmws.wsCreateCustomer(authtoken,s);
			}
////			for (Iterator<String> it = email.iterator(); it.hasNext(); )
//			{
//					System.out.println("test111");
//				cmws.wsCreateCustomer(authtoken,it.toString());
//
//
//			}

				

		} 

		catch (Exception E) 
		{
			E.printStackTrace();

		}	


	}	    


}



