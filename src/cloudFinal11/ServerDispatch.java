package cloudFinal11;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.net.URL;

import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import org.json.JSONException;
import org.json.JSONObject;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.HttpMethod;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import com.amazonaws.services.s3.model.ListObjectsRequest;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClient;
import com.amazonaws.services.sqs.model.CreateQueueRequest;
import com.amazonaws.services.sqs.model.SendMessageRequest;

@ServerEndpoint(value = "/realtime")
public class ServerDispatch {

	String keyword = null;

	public static AmazonSQS sqs;
	public static String Url;

	private static final Logger LOGGER = 
			Logger.getLogger(ServerDispatch.class.getName());
	private static Set<Session> sessionslist = Collections.synchronizedSet(new HashSet<Session>());
	
	static AmazonS3 s3client;
	private static String bucketName = "columbia.6998.cloudfinal";

	@OnOpen
	public void onOpen(Session session) throws IOException {
		LOGGER.log(Level.INFO, "New connection with client: {0}", 
				session.getId());
	}

	public static void broadcastData(String locOBJ){
		for (Session s : sessionslist){
		
			try {
				s.getBasicRemote().sendText(locOBJ);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	@OnMessage
	public void onMessage(String message, Session session) throws IOException, InterruptedException, JSONException, SQLException {
		
		sessionslist.add(session);		
		JSONObject jsonObject = new JSONObject(message);
		if (jsonObject.getString("Button") != null) {
			keyword = jsonObject.getString("Button");
		} else {
			keyword = jsonObject.getString("flag");
		}
			System.out.println("sending ...");
			try {
				if (keyword.equals("Question")){
					sendText(message);    
					System.out.println("Question submitted");
				} else {//if (keyword.equals("Answer")){
					sendText(message);    
					System.out.println("Answer submitted");
				} 
			} catch (JSONException e) {
				e.printStackTrace();
			}

			Thread.sleep(2000);
	}

	@OnClose
	public void onClose(Session session) {
		sessionslist.remove(session);
		LOGGER.log(Level.INFO, "Close connection for client: {0}", 
				session.getId());
	}

	@OnError
	public void onError(Throwable exception, Session session) {
		LOGGER.log(Level.INFO, "Error for client: {0}", session.getId());
	}


	public static void sendText(String message) throws JSONException, SQLException {
		
		//get text, timestamp, userid from message
		JSONObject jsonMessage = new JSONObject(message);

		AWSCredentials credentials = null;
		// Load the AWS credentials file
		try {
			credentials = new BasicAWSCredentials("************","************");
		} catch (Exception e) {
			throw new AmazonClientException(
					"Cannot load the credentials from the credential profiles file. " +
							"Please make sure that your credentials file is at the correct, and is in valid format.", e);
		}

		sqs = new AmazonSQSClient(credentials);
		Region usEast1 = Region.getRegion(Regions.US_EAST_1);
		sqs.setRegion(usEast1);
		CreateQueueRequest createQueueRequest = new CreateQueueRequest("TextQueue");
		Url = sqs.createQueue(createQueueRequest).getQueueUrl();
		JSONObject jsonObject = new JSONObject();
		if (jsonMessage.getString("Button") == "Question") {
			try {
				jsonObject.put("flag", jsonMessage.getString("Button"));
				jsonObject.put("text", jsonMessage.getString("Text"));
				jsonObject.put("id", jsonMessage.getString("Id"));
				jsonObject.put("time", jsonMessage.getString("Timestamp"));
				jsonObject.put("messageid", jsonMessage.getString("Mid"));
			} catch (JSONException e) {
				e.printStackTrace();
			}
		} else {

			try{			
				Thread.sleep(3000);			
			}catch(InterruptedException ex){
				Thread.currentThread().interrupt();
			}
			
			s3client = new AmazonS3Client(credentials);
			String messageid = jsonMessage.getString("Mid");
			String answerid = jsonMessage.getString("Aid");
			List<String> keyName = new ArrayList<String>();
			List<String> URLs = new ArrayList<String>();
			String urls = null;
			
			
//			String str ="INSERT INTO test3 VALUES ('" + messageid +"', '" + answerid +"')"	;
//    		Rds.stmt.executeUpdate(str);

			try {
				System.out.println("Listing objects");

				ListObjectsRequest listObjectsRequest = new ListObjectsRequest()
				.withBucketName(bucketName)
				.withPrefix("img/");
				ObjectListing objectListing;

				do {
					objectListing = s3client.listObjects(listObjectsRequest);
					for (S3ObjectSummary objectSummary : 
						objectListing.getObjectSummaries()) {
						if (objectSummary.getKey().contains(messageid) && objectSummary.getKey().contains(answerid)) {
							keyName.add(objectSummary.getKey());
							
//							String str2 ="INSERT INTO test4 VALUES ('" + objectSummary.getKey() +"')"	;
//				    		Rds.stmt.executeUpdate(str2);
						}
					}
					listObjectsRequest.setMarker(objectListing.getNextMarker());
				} while (objectListing.isTruncated());
			} catch (AmazonServiceException ase) {
				System.out.println("Caught an AmazonServiceException, " +
						"which means your request made it " +
						"to Amazon S3, but was rejected with an error response " +
						"for some reason.");
				System.out.println("Error Message:    " + ase.getMessage());
				System.out.println("HTTP Status Code: " + ase.getStatusCode());
				System.out.println("AWS Error Code:   " + ase.getErrorCode());
				System.out.println("Error Type:       " + ase.getErrorType());
				System.out.println("Request ID:       " + ase.getRequestId());
			} catch (AmazonClientException ace) {
				System.out.println("Caught an AmazonClientException, " +
						"which means the client encountered " +
						"an internal error while trying to communicate" +
						" with S3, " +
						"such as not being able to access the network.");
				System.out.println("Error Message: " + ace.getMessage());
			}

			try {
				System.out.println("Generating pre-signed URL.");
				java.util.Date expiration = new java.util.Date();
				long milliSeconds = expiration.getTime();
				milliSeconds += 1000 * 60 * 60; // Add 1 hour.
				expiration.setTime(milliSeconds);

				for (String objectKey : keyName) {
					GeneratePresignedUrlRequest generatePresignedUrlRequest = 
							new GeneratePresignedUrlRequest(bucketName, objectKey);
					generatePresignedUrlRequest.setMethod(HttpMethod.GET); 
					generatePresignedUrlRequest.setExpiration(expiration);

					URL url = s3client.generatePresignedUrl(generatePresignedUrlRequest);

					//problem
					URLs.add(url.toString());

					System.out.println("Pre-Signed URL = " + url.toString());
				}
			} catch (AmazonServiceException exception) {
				System.out.println("Caught an AmazonServiceException, " +
						"which means your request made it " +
						"to Amazon S3, but was rejected with an error response " +
						"for some reason.");
				System.out.println("Error Message: " + exception.getMessage());
				System.out.println("HTTP  Code: "    + exception.getStatusCode());
				System.out.println("AWS Error Code:" + exception.getErrorCode());
				System.out.println("Error Type:    " + exception.getErrorType());
				System.out.println("Request ID:    " + exception.getRequestId());
			} catch (AmazonClientException ace) {
				System.out.println("Caught an AmazonClientException, " +
						"which means the client encountered " +
						"an internal error while trying to communicate" +
						" with S3, " +
						"such as not being able to access the network.");
				System.out.println("Error Message: " + ace.getMessage());
			}

			for (String url : URLs) {
				urls = urls + "$," + url;
			}
    		
			try {
				jsonObject.put("flag", jsonMessage.getString("Button"));
				jsonObject.put("text", jsonMessage.getString("Text"));
				jsonObject.put("id", jsonMessage.getString("Id"));
				jsonObject.put("time", jsonMessage.getString("Timestamp"));
				jsonObject.put("messageid", jsonMessage.getString("Mid"));
				//image stored in S3
				jsonObject.put("url", urls);
				jsonObject.put("aid", jsonMessage.getString("Aid"));
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		sqs.sendMessage(new SendMessageRequest(Url, jsonObject.toString() ));

		//start startupinit process
		Runnable startup = new StartupInit();
		ExecutorService executor = Executors.newFixedThreadPool(2);
		executor.execute(startup);
	}

} 