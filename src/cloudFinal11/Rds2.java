package cloudFinal11;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;


public class Rds2 {
	static String url = "jdbc:mysql://tweetdata.c83g2woipxnr.us-east-1.rds.amazonaws.com:3306/tweetData";
	static String usrDB = "leogogo1023";
	static String password = "greenmilktea";
    static Connection conn = Rds2.connectDB(url, usrDB, password);
    
    public static Connection connectDB(String url,String usr,String password){
    	String driver = "com.mysql.jdbc.Driver";
    	Statement statement = null;
    	Connection conn = null;
    	try {
			Class.forName(driver);
		} catch (ClassNotFoundException e1) {
			e1.printStackTrace();
		}        	
    	try {
			conn = DriverManager.getConnection(url, usr, password);
			statement = conn.createStatement();
			if(!conn.isClosed()){			
				System.out.println("Connected!");}
		} catch (SQLException e) {
			e.printStackTrace();
		} 
    	return conn;
    }

}