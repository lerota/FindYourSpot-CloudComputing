package cloudFinal11;


import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import org.json.JSONException;
import org.json.JSONObject;

import com.amazonaws.AmazonClientException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClient;
import com.amazonaws.services.sqs.model.CreateQueueRequest;
import com.amazonaws.services.sqs.model.ListQueuesResult;
import com.amazonaws.services.sqs.model.SendMessageRequest;

@ServerEndpoint(value = "/aws")
public class Server {

	static String dbName = "tweetData";
	static String userName = "leogogo1023";
	static String password = "greenmilktea";
	static String hostname = "tweetdata.c83g2woipxnr.us-east-1.rds.amazonaws.com";
	static String port = "3306";
	static String jdbcUrl = "jdbc:mysql://" + hostname + ":" +
					port + "/" + dbName + "?user=" + userName + "&password=" + password;
	static Connection conn = null;
	static Statement setupStatement = null;
	static ResultSet resultSet = null;
	static String selectSQL = null;
	
	String keyword = null;
	
	public static AmazonSQS sqs;
	public static String Url;
	
	private static final Logger LOGGER = 
            Logger.getLogger(Server.class.getName());
	private static Set<Session> sessionslist = Collections.synchronizedSet(new HashSet<Session>());
	
	static AmazonS3 s3client;
    
    @OnOpen
    public void onOpen(Session session) throws IOException {
      	conn = DBcontrol.getConn(jdbcUrl);
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
    public void onMessage(String message, Session session) throws Exception {  
    	
    	sessionslist.add(session);
    	JSONObject jsonObject = new JSONObject(message);
    	keyword = jsonObject.getString("Button");
    	JSONObject messageObject = new JSONObject();
    	
                System.out.println("sending ...");
                try {
                	  if (keyword.equals("Question")){
             
                	    sendToDB(message);
                	    sendText(message);
                        System.out.println("Question submitted");
                	} else if (keyword.equals("Answer")){
                		sendToDB(message);
                		sendText(message);
                        System.out.println("Answer submitted");
                	} else { //keyword == "all"
                		selectSQL = "SELECT * FROM Answers;";
                		ResultSet result = null;
    					result = DBcontrol.doSelect(conn, selectSQL, setupStatement, result);
                		if (result.next()) {
                			selectSQL = "SELECT * FROM Questions q1, Answers a1, Q_A t1 WHERE q1.Question_id = t1.Question_id and a1.Answer_id = t1.Answer_id;";
                		} else {
                			selectSQL = "SELECT * FROM Questions q1;";
                		}
                		try {
    						messageObject = Notification.selectAll(selectSQL);
    					} catch (SQLException e) {
    						e.printStackTrace();
    					} catch (JSONException e) {
    						e.printStackTrace();
    					}
        				String mes = messageObject.toString();

    					Server.broadcastData(mes);
                	}

        		} catch (JSONException e) {
        			e.printStackTrace();
        		}
                Thread.sleep(2000);
    }
    
    @OnClose
    public void onClose(Session session) {
    	sessionslist.remove(session);
    	DBcontrol.disConn(conn, setupStatement, resultSet);
        LOGGER.log(Level.INFO, "Close connection for client: {0}", 
                session.getId());
    }
    
    @OnError
    public void onError(Throwable exception, Session session) {
        LOGGER.log(Level.INFO, "Error for client: {0}", session.getId());
    }
    
    public static void sendToDB(String message) throws Exception {
    	JSONObject jsonMessage = new JSONObject(message);  	
    	try {
    		String id = jsonMessage.getString("Id").toString();
    		String qid = jsonMessage.getString("Qid").toString();
    		String text = jsonMessage.getString("Text").toString();
    		String timestamp=jsonMessage.getString("Timestamp").toString();
    		String aid = jsonMessage.getString("Aid").toString();

    		if (jsonMessage.getString("Button").equals("Question") ) {
        		String str ="INSERT INTO Questions VALUES ('" + id +"', '" + qid +"', '" +text +"', '"+ timestamp +"')"	;
        		String str2 = "INSERT INTO Q_A VALUES('"+qid+ "', NULL)";
        		Rds.stmt.executeUpdate(str);
        		Rds.stmt.executeUpdate(str2);
        		
        		
	    		selectSQL = "SELECT points from User WHERE Email = '" + jsonMessage.getString("Id").toString() + "';";
	    		resultSet = DBcontrol.doSelect(conn, selectSQL, setupStatement, resultSet);
	    		while(resultSet.next()){
		    		int points = resultSet.getInt("points");
		    		int temp = (points-2);
		    		String str3 ="UPDATE User SET points = "+ temp +" WHERE Email = '" + id +"'";
		    		Rds.stmt.executeUpdate(str3);
	    		}
    		} else if (jsonMessage.getString("Button").equals("Answer")) {//button = "Answer"
    			String str4 = "INSERT INTO Answers VALUES('" +id+ "', '" +aid +"', '" +text +"', '"+ timestamp +"', '" + qid +"')";
				Rds.stmt.executeUpdate(str4);
				
    			//update table Q_A			 
				
    			selectSQL = "SELECT Answer_id from Q_A WHERE Question_id = '" + jsonMessage.getString("Qid").toString() + "';";
    			resultSet = DBcontrol.doSelect(conn, selectSQL, setupStatement, resultSet);
	    		while(resultSet.next()){
	    			
	    			String Answerid = resultSet.getString("Answer_id").toString();
	    			//if(resultSet.wasNull()){
	    			if (Answerid == null) {
	    				String str5 = "UPDATE Q_A SET Answer_id = '"+ aid +"' WHERE Question_id = '" + qid +"'";
						Rds.stmt.executeUpdate(str5);
	    			}
	    		}
    			//update points in table User
    			selectSQL = "SELECT points from User WHERE Email = " + jsonMessage.getString("Id") + ";";
	    		resultSet = DBcontrol.doSelect(conn, selectSQL, setupStatement, resultSet);
	    		while(resultSet.next()){
	    			int points = resultSet.getInt("points");
		    		int temp = (points+1);
		    		String str6 ="UPDATE User SET points = "+ temp +" WHERE Email = '" + id +"'";
		    		Rds.stmt.executeUpdate(str6);
	    		}
    		}
    	}
    	 catch (JSONException e) {
			e.printStackTrace();
    	}
    }

    public static void sendText(String message) throws JSONException {
    	//get text, timestamp, userid from message
    	JSONObject jsonMessage = new JSONObject(message);
    	
    	AWSCredentials credentials = null;
		// Load the AWS credentials file
		try {
		 	credentials = new BasicAWSCredentials("**********","**********");
		} catch (Exception e) {
		 	throw new AmazonClientException(
                    "Cannot load the credentials from the credential profiles file. " +
                    "Please make sure that your credentials file is at the correct, and is in valid format.", e);
		}
		 
		if(sqs == null){
			sqs = new AmazonSQSClient(credentials);
	  	    Region usEast1 = Region.getRegion(Regions.US_EAST_1);
	  	    sqs.setRegion(usEast1);
	  	}
	  	try{
	  		ListQueuesResult queue = sqs.listQueues("NewQueue");
	  		Url = queue.getQueueUrls().get(0);
	  	} catch (IndexOutOfBoundsException ase) {
	  		CreateQueueRequest createQueueRequest = new CreateQueueRequest("NewQueue");
	  	    Url = sqs.createQueue(createQueueRequest).getQueueUrl();
	  	}
    	
    	JSONObject jsonObject = new JSONObject();
    	if (jsonMessage.getString("Button") == "Question") {
	        try {
	      	    jsonObject.put("flag", jsonMessage.getString("Button"));
	      	    jsonObject.put("questionId", jsonMessage.getString("Qid"));
				jsonObject.put("id", jsonMessage.getString("Id"));
				jsonObject.put("time", jsonMessage.getString("Timestamp"));
	        } catch (JSONException e) {
				e.printStackTrace();
	        }
    	} else {    		
    		try {
	      	    jsonObject.put("flag", jsonMessage.getString("Button"));
	      	    jsonObject.put("answerId", jsonMessage.getString("Aid"));
				jsonObject.put("id", jsonMessage.getString("Id"));
				jsonObject.put("time", jsonMessage.getString("Timestamp"));
				jsonObject.put("questionId", jsonMessage.getString("Qid"));
	        } catch (JSONException e) {
				e.printStackTrace();
	        }
    	}
        sqs.sendMessage(new SendMessageRequest(Url, jsonObject.toString() ));
        
        //start startupinit process
        Runnable startup = new Startup();
        ExecutorService executor = Executors.newFixedThreadPool(2);
    	executor.execute(startup);
    }
    
    
} 