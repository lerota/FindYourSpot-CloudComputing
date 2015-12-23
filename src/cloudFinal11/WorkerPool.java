package cloudFinal11;


import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClient;
import com.amazonaws.services.sqs.model.CreateQueueRequest;
import com.amazonaws.services.sqs.model.DeleteMessageRequest;
import com.amazonaws.services.sqs.model.ListQueuesResult;
import com.amazonaws.services.sqs.model.Message;
import com.amazonaws.services.sqs.model.ReceiveMessageRequest;

public class WorkerPool implements Runnable{

	private AmazonSQS sqs;
    private String Url;
    private ExecutorService taskExecutor;
    
	public WorkerPool(int N)
    {
		AWSCredentials credentials = new BasicAWSCredentials("************","*************");
	    
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
	    
	    taskExecutor = Executors.newFixedThreadPool(N);
    }
	
	
	public void run()
	{
		    while (!taskExecutor.isShutdown()) {
		    	try{
		    		ReceiveMessageRequest receiveMessageRequest = new ReceiveMessageRequest(Url);
		    		List<Message> messages = sqs.receiveMessage(receiveMessageRequest).getMessages();
			        for (Message message : messages) {
			        	String Text = message.getBody();
			            Runnable worker = new SendToSNS(Text);
			            String messageReceiptHandle = message.getReceiptHandle();
			            sqs.deleteMessage(new DeleteMessageRequest(Url, messageReceiptHandle));
			            taskExecutor.execute(worker);
			        }
			        Thread.sleep(2000);
		    	}catch (InterruptedException e) {
		    	}
		    }
		}
}