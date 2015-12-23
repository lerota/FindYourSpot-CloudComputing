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

public class SendToSNS implements Runnable{

    private AmazonSNSClient snsClient;
	 
    private String Text;
    
    public SendToSNS(String pText){
    	Text = pText;
    	AWSCredentials credentials = new BasicAWSCredentials("*********","*********");
        
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
    	
    	String QuestionId = null;
		try {
			QuestionId = jsonObject.getString("questionId");
		} catch (JSONException e2) {
			e2.printStackTrace();
		}
		
		String AnswerId = null;
		try {
			if (jsonObject.getString("answerId") != null) {
				AnswerId = jsonObject.getString("answerId");
			}
		} catch (JSONException e2) {
				e2.printStackTrace();
		}
		
		String TimeStamp = null;
		try {
			TimeStamp = jsonObject.getString("time");
		} catch (JSONException e2) {
			e2.printStackTrace();
		}
		
		String UserId = null;
		try {
			UserId = jsonObject.getString("id");
		} catch (JSONException e2) {
			e2.printStackTrace();
		}
            	
		try {			
			String topicArn = "arn:aws:sns:us-east-1:994295696375:NewTopic";
			JSONObject json = new JSONObject();
            try {
          	    json.put("flag", Type);
          	    json.put("userId", UserId);
          	    json.put("time", TimeStamp);
          	    json.put("aId", AnswerId);
          	    if (QuestionId != null) {
          	    	json.put("qId", QuestionId);
          	    }
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