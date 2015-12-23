package cloudFinal11;


import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.security.Signature;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.Scanner;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.binary.Base64;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class NotificationServer extends HttpServlet{
	private static final long serialVersionUID = 1L;
	
	public NotificationServer() {
		super();
	}  
    
	protected void doPost(final HttpServletRequest request,
			final HttpServletResponse response) throws ServletException,
			IOException {
		
    			//Get the message type header.  	
    			String messagetype = request.getHeader("x-amz-sns-message-type");
    			System.out.println("Got a message: " + messagetype);
    			//If message doesn't have the message type header, don't process it.
    			if (messagetype == null)
    				return;
    			// Parse the JSON message in the message body
    			// and hydrate a Message object with its contents 
    			// so that we have easy access to the name/value pairs 
    			// from the JSON message.
				Scanner scan = new Scanner(request.getInputStream());
    			StringBuilder builder = new StringBuilder();
    			while (scan.hasNextLine()) {
    				builder.append(scan.nextLine());
    			}

    			SNSMessage msg = readMessageFromJson(builder.toString());

    			// The signature is based on SignatureVersion 1. 
    			// If the sig version is something other than 1, 
    			// throw an exception.
    			if (msg.getSignatureVersion().equals("1")) {
    				// Check the signature and throw an exception if the signature verification fails.
    				if (isMessageSignatureValid(msg))
    					System.out.println(">>Signature verification succeeded");
    				else {
    					System.out.println(">>Signature verification failed");
    					throw new SecurityException("Signature verification failed.");
    				}
    			}
    			else {
    				System.out.println(">>Unexpected signature version. Unable to verify signature.");
    				throw new SecurityException("Unexpected signature version. Unable to verify signature.");
    			}
    			
    			// Process the message based on type.
    			if (messagetype.equals("Notification")) {
    				String logMsgAndSubject = ">>Notification received from topic " + msg.getTopicArn();
    				if (msg.getSubject() != null)
    					logMsgAndSubject += " Subject: " + msg.getSubject();
    				logMsgAndSubject += " Message: " + msg.getMessage();
    				String mes = msg.getMessage();

					ServerDispatch.broadcastData(mes);

    				System.out.println(logMsgAndSubject);
    					
    				}
    			else if (messagetype.equals("SubscriptionConfirmation"))
    			{
    				Scanner sc = new Scanner(new URL(msg.getSubscribeURL()).openStream());
    				StringBuilder sb = new StringBuilder();
    				while (sc.hasNextLine()) {
    					sb.append(sc.nextLine());
    				}
    				System.out.println(">>Subscription confirmation (" + msg.getSubscribeURL() +") Return value: " + sb.toString());
    				//TODO: Process the return value to ensure the endpoint is subscribed.
    				SNSHelper.INSTANCE.confirmTopicSubmission(msg);
    			}
    			else if (messagetype.equals("UnsubscribeConfirmation")) {
    				System.out.println(">>Unsubscribe confirmation: " + msg.getMessage());
    			}
    			else {
    				//TODO: Handle unknown message type.
    				System.out.println(">>Unknown message type.");
    			}
    			System.out.println(">>Done processing message: " + msg.getMessageId());
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
	}
	
	private boolean isMessageSignatureValid(SNSMessage msg) {

		try {
			URL url = new URL(msg.getSigningCertUrl());
			InputStream inStream = url.openStream();
			CertificateFactory cf = CertificateFactory.getInstance("X.509");
			X509Certificate cert = (X509Certificate)cf.generateCertificate(inStream);
			inStream.close();

			Signature sig = Signature.getInstance("SHA1withRSA");
			sig.initVerify(cert.getPublicKey());
			sig.update(getMessageBytesToSign(msg));
			return sig.verify(Base64.decodeBase64(msg.getSignature().getBytes()));
		}
		catch (Exception e) {
			throw new SecurityException("Verify method failed.", e);

		}
	}

	private byte[] getMessageBytesToSign(SNSMessage msg) {

		byte [] bytesToSign = null;
		if (msg.getType().equals("Notification"))
			bytesToSign = buildNotificationStringToSign(msg).getBytes();
		else if (msg.getType().equals("SubscriptionConfirmation") || msg.getType().equals("UnsubscribeConfirmation"))
			bytesToSign = buildSubscriptionStringToSign(msg).getBytes();
		return bytesToSign;
	}

	//Build the string to sign for Notification messages.
	private static String buildNotificationStringToSign( SNSMessage msg) {
		String stringToSign = null;

		//Build the string to sign from the values in the message.
		//Name and values separated by newline characters
		//The name value pairs are sorted by name 
		//in byte sort order.
		stringToSign = "Message\n";
		stringToSign += msg.getMessage() + "\n";
		stringToSign += "MessageId\n";
		stringToSign += msg.getMessageId() + "\n";
		if (msg.getSubject() != null) {
			stringToSign += "Subject\n";
			stringToSign += msg.getSubject() + "\n";
		}
		stringToSign += "Timestamp\n";
		stringToSign += msg.getTimestamp() + "\n";
		stringToSign += "TopicArn\n";
		stringToSign += msg.getTopicArn() + "\n";
		stringToSign += "Type\n";
		stringToSign += msg.getType() + "\n";
		return stringToSign;
	}

	//Build the string to sign for SubscriptionConfirmation 
	//and UnsubscribeConfirmation messages.
	private static String buildSubscriptionStringToSign(SNSMessage msg) {
		String stringToSign = null;
		//Build the string to sign from the values in the message.
		//Name and values separated by newline characters
		//The name value pairs are sorted by name 
		//in byte sort order.
		stringToSign = "Message\n";
		stringToSign += msg.getMessage() + "\n";
		stringToSign += "MessageId\n";
		stringToSign += msg.getMessageId() + "\n";
		stringToSign += "SubscribeURL\n";
		stringToSign += msg.getSubscribeURL() + "\n";
		stringToSign += "Timestamp\n";
		stringToSign += msg.getTimestamp() + "\n";
		stringToSign += "Token\n";
		stringToSign += msg.getToken() + "\n";
		stringToSign += "TopicArn\n";
		stringToSign += msg.getTopicArn() + "\n";
		stringToSign += "Type\n";
		stringToSign += msg.getType() + "\n";
		return stringToSign;
	}


	private SNSMessage readMessageFromJson(String string) {
		ObjectMapper mapper = new ObjectMapper(); 
		SNSMessage message = null;
		try {
			message = mapper.readValue(string, SNSMessage.class);
		} catch (JsonParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return message;
	}
	

}