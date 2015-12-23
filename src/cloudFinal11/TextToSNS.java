package cloudFinal11;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.sns.AmazonSNSClient;
import com.amazonaws.services.sns.model.PublishRequest;
import com.amazonaws.services.sns.model.PublishResult;
import com.amazonaws.util.json.JSONException;
import com.amazonaws.util.json.JSONObject;

public class TextToSNS implements Runnable{

    private AmazonSNSClient snsClient;
	 
    private String Text;
    
    public TextToSNS(String pText){
    	Text = pText;
    	AWSCredentials credentials = new BasicAWSCredentials("*************","*************");
        
	    snsClient = new AmazonSNSClient(credentials);		                           
	    snsClient.setRegion(Region.getRegion(Regions.US_EAST_1));
	    
	    
    }
    
    public void run() {
    	
    	JSONObject jsonObject = null;
		try {
			jsonObject = new JSONObject(Text);
		} catch (JSONException e4) {
			e4.printStackTrace();
		}
		
		String Type = null;
		try {
			Type = jsonObject.getString("flag");
		} catch (JSONException e2) {
			e2.printStackTrace();
		}
    	
    	String MessageText = null;
		try {
			MessageText = jsonObject.getString("text");
		} catch (JSONException e2) {
			e2.printStackTrace();
		}
		
		String TimeStamp = null;
		try {
			String TimeStamp1 = jsonObject.getString("time");
			TimeStamp = TimeStamp1.substring(0, 10) + " " + TimeStamp1.substring(11,19);

		} catch (JSONException e2) {
			e2.printStackTrace();
		}
		
		String UserId = null;
		try {
			UserId = jsonObject.getString("id");
		} catch (JSONException e2) {
			e2.printStackTrace();
		}
		
		String MessageId = null;
		try {
			MessageId = jsonObject.getString("messageid");
		} catch (JSONException e2) {
			e2.printStackTrace();
		}
		
		String URL = null;
		if(Type.equals("Answer")){
			try {
				if (jsonObject.getString("url") != null) {
					URL = jsonObject.getString("url");
				}
			} catch (JSONException e2) {
				e2.printStackTrace();
			}
		}
		
		String AId = null;
		try {
			if (jsonObject.getString("aid") != null) {
				AId = jsonObject.getString("aid");
			}
		} catch (JSONException e2) {
			e2.printStackTrace();
		}
            	
		try {
			
			String topicArn = "arn:aws:sns:us-east-1:994295696375:MyTopic";
			JSONObject json = new JSONObject();
            try {
          	    json.put("flag", Type);
          	    json.put("text", MessageText);
          	    json.put("userId", UserId);
          	    json.put("time", TimeStamp);
          	    json.put("mId", MessageId);
          	    json.put("Url", URL);
          	    json.put("aId", AId);
            } catch (JSONException e) {
				e.printStackTrace();
            }
            PublishRequest publishRequest = new PublishRequest(topicArn, json.toString());
			PublishResult publishResult = snsClient.publish(publishRequest);
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (Exception e) {
 			e.printStackTrace();
		}
    }
}