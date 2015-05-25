import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.sun.org.apache.*;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.RequestEntity;
import org.apache.commons.httpclient.methods.StringRequestEntity;


public class CallMaaSWebService {

	public String callws()
	{

		String authToken=null;
		try
		{
			String xml = null; 
			HashMap<String, String> params = new HashMap<String, String>();
			params.put("billingID","2026947");
			params.put("platformID","3");
			params.put("appID","auto.app.com");
			params.put("appAccessKey","Ew9EhERTzq");
			params.put("userName", "2026947_wraza");
			params.put("password", "Admin@123");
			params.put("appVersion","1");
			PostMethod post = new PostMethod("https://iq3services.fiberlink.com/auth-apis/auth/1.0/authenticate/2026947");
			xml = createAuthTemplateXML(params);
			post.addRequestHeader("Host", "iq3service.fiberlink.com");
			post.addRequestHeader("Connection", "keep-alive");
			post.addRequestHeader("Content-Type", "application/xml");
			post.addRequestHeader("Accept-Charset", "UTF-8");
			RequestEntity requestEntity = new StringRequestEntity(xml, "application/xml", "UTF-8");
			post.setRequestEntity(requestEntity);
			HttpClient client =  new HttpClient();
			int statusCode = client.executeMethod(post);
			String responseBody = post.getResponseBodyAsString();
			authToken = getPatternMatches("<authToken>(.+)</authToken>", responseBody, false);
			System.out.println(authToken);


		}

		catch (Exception e)
		{
			e.getMessage();
		}

		return(authToken);
	} 

	public final String createAuthTemplateXML(Map<String, String> params) {
		StringBuilder sb = new StringBuilder();
		sb.append("<authRequest>").append("\n").append("<").append("maaS360AdminAuth").append(">").append("\n");

		for (String key : params.keySet()) {
			sb.append("<").append(key).append(">").append(params.get(key)).append("</").append(key).append(">").append("\n");
		}

		sb.append("</").append("maaS360AdminAuth").append(">").append("\n").append("</authRequest>").append("\n");

		return sb.toString();
	}

	/**
	 * getPatternMatches method matches the patternToMatch in response of the request 
	 * 
	 * @param patternToMatch  String patternToMatch
	 * @param response    String response of the reqeuest
	 * @param relpaceWhiteSpaces flag used whether to replace white spaces in the response or not
	 * 
	 * @return
	 * 
	 * @throws Exception 
	 */
	public String getPatternMatches(String patternToMatch, String response, boolean relpaceWhiteSpaces) throws Exception {
		Pattern pattern = null;
		String patternValue = null;
		try {
			patternToMatch = "(?i)"+patternToMatch+"(?i)";
			if(relpaceWhiteSpaces) {
				response = response.replaceAll("\\s","");
			}

			pattern = Pattern.compile(patternToMatch);

			// Now create matcher object.
			Matcher matcher = pattern.matcher(response);
			while (matcher.find()) {
				patternValue = matcher.group(1);
				break;
			}
			if(null != patternValue) {
				if(patternValue.contains("</(.+)>")) {
					patternValue = patternValue.split("</(.+)>")[0];
				}
			} 
		} catch (Exception e) {
			//log.error("Exception in getPatternMatches() :: ", e);
			throw new Exception("Exception in getPatternMatches() :: " + e.getMessage());
		}
		return patternValue;
	}
	public void wsCreateCustomer(String authToken,String email) throws Exception {
			

		try 
		{
			PostMethod postMethod = new PostMethod("https://iq3services.fiberlink.com/account-provisioning/account/1.0/createCustomerAccount/2026947");

			// Set request parameters
			postMethod.setParameter("accountType", "Customer");
			Date date= new Date();
			SimpleDateFormat sdf = new SimpleDateFormat("ddmmyy");
			postMethod.setParameter("accountName","testflk_twitter_" +sdf.format(date));
			postMethod.setParameter("adminEmailAddress",email);
			postMethod.setParameter("forceAdminAcceptEULA", "1");
			postMethod.setParameter("allowPortalLogin", "1");

			postMethod.setParameter("billingId", "2026947");
			//postMethod.setParameter("password", "admin@123");
			postMethod.addRequestHeader("Connection","Keep-Alive" );
			postMethod.addRequestHeader("Content-Type", "application/x-www-form-urlencoded");
			postMethod.addRequestHeader("Accept-Charset", "UTF-8");
			postMethod.addRequestHeader("Authorization", "MaaS token=\""+authToken+"\"");
			//RequestEntity requestEntity = new StringRequestEntity(xml, "application/xml", "UTF-8");
			//postMethod.setRequestEntity(requestEntity);
			HttpClient client =  new HttpClient();
			int statusCode = client.executeMethod(postMethod);
			String responseBody = postMethod.getResponseBodyAsString();

			String billingId = getPatternMatches("<billingId>(.+)</billingId>", responseBody, false);



		} catch (Exception e) {

			e.getMessage();
			//return false;
		}
		//return isCustomerRegistrationSuccessful;
	}

}


