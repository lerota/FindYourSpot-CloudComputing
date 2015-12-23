package cloudFinal11;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import org.apache.tomcat.util.http.fileupload.IOUtils;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.PutObjectRequest;
 
@MultipartConfig
public class uploadServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    static AWSCredentials credentials = new BasicAWSCredentials("************","************");

    static AmazonS3 s3client = new AmazonS3Client(credentials);
   	private static String bucketName     = "columbia.6998.cloudfinal";
   	public static final String PREFIX = "stream2file";
    public static final String SUFFIX = ".tmp";

    public static File stream2file (InputStream in) throws IOException {
        final File tempFile = File.createTempFile(PREFIX, SUFFIX);
        tempFile.deleteOnExit();
        try (FileOutputStream out = new FileOutputStream(tempFile)) {
            IOUtils.copy(in, out);
        }
        return tempFile;
    }
    
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String input = request.getHeader("My-Awesome-Header");
		for (Part part : request.getParts()) {
		    InputStream fileContent = part.getInputStream();	    
		    String sub = generateRandomKey(20);
			String keyName        = "img/"+ input + "_" + sub;
			
	        try {
	            System.out.println("Uploading a new object to S3 from a file\n");
	           				     
	            s3client.putObject(new PutObjectRequest(
	            		                 bucketName, keyName, stream2file(fileContent)));

	         } catch (AmazonServiceException ase) {
	            System.out.println("Caught an AmazonServiceException, which " +
	            		"means your request made it " +
	                    "to Amazon S3, but was rejected with an error response" +
	                    " for some reason.");
	            System.out.println("Error Message:    " + ase.getMessage());
	            System.out.println("HTTP Status Code: " + ase.getStatusCode());
	            System.out.println("AWS Error Code:   " + ase.getErrorCode());
	            System.out.println("Error Type:       " + ase.getErrorType());
	            System.out.println("Request ID:       " + ase.getRequestId());
	        } catch (AmazonClientException ace) {
	            System.out.println("Caught an AmazonClientException, which " +
	            		"means the client encountered " +
	                    "an internal error while trying to " +
	                    "communicate with S3, " +
	                    "such as not being able to access the network.");
	            System.out.println("Error Message: " + ace.getMessage());
	        }
	        
	        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
	        String timeStamp = dateFormat.format(new Date());       
	        
	        try {
	        	String str ="INSERT INTO commentAndImg VALUES ('Butler', '301', 'user1','" +timeStamp +"', 'NULL', '"+ keyName + "')";
	        	Rds.stmt.executeUpdate(str);
			} catch (Exception e) {
				e.printStackTrace();
			}
        }
	}
   public void doGet(HttpServletRequest request, 
                       HttpServletResponse response){
   } 
   
	private static String generateRandomKey(int len){
		final String AB = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
		Random random = new Random();
		StringBuilder sb = new StringBuilder(len);
		for( int i = 0; i < len; i++ ) 
	      sb.append( AB.charAt( random.nextInt(AB.length()) ) );
		return sb.toString();
	}
}