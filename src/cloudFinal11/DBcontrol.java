package cloudFinal11;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DBcontrol {
	//Connect to DB
	public static Connection getConn(String jdbcUrl){
		try {
		    System.out.println("Loading driver...");
		    Class.forName("com.mysql.jdbc.Driver");
		    System.out.println("Driver loaded!");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		    throw new RuntimeException("Cannot find the driver in the classpath!", e);
		}
		Connection conn=null;
		try {
			conn = DriverManager.getConnection(jdbcUrl);
		} catch (SQLException ex) {
			System.out.println("SQLException: " + ex.getMessage());
		    System.out.println("SQLState: " + ex.getSQLState());
		    System.out.println("VendorError: " + ex.getErrorCode());
		}
		return conn;
	}
	
	//Disconnect to DB
	public static void disConn(Connection conn, Statement st, ResultSet rs){
		try{
			if (rs!=null){
				rs.close();
				rs = null;
			}
			if (st!=null){
				st.close();
				st = null;
			}
		}catch(SQLException ex){
			System.out.println("SQLException: " + ex.getMessage());
		    System.out.println("SQLState: " + ex.getSQLState());
		    System.out.println("VendorError: " + ex.getErrorCode());
		}
		System.out.println("Closing the connection.");
	    if (conn != null) try { conn.close(); } catch (SQLException ignore) {}
	    System.out.println("Connection closed!");
	}
	
	//Insert data to DB
	public static void doInsert(Connection conn, String insertSQL, Statement st){
		try {
			st = conn.createStatement();
			st.addBatch(insertSQL);
			st.executeBatch();
		} catch (SQLException ex) {
			System.out.println("SQLException: " + ex.getMessage());
		    System.out.println("SQLState: " + ex.getSQLState());
		    System.out.println("VendorError: " + ex.getErrorCode());
		}
	}
	
	//Delete data from DB
	public static void doDelete(Connection conn, String deleteSQL, Statement st){
		try {
			st = conn.createStatement();
			st.addBatch(deleteSQL);
			st.executeBatch();
		} catch (SQLException ex) {
			System.out.println("SQLException: " + ex.getMessage());
		    System.out.println("SQLState: " + ex.getSQLState());
		    System.out.println("VendorError: " + ex.getErrorCode());
		}
	}
	
	//Select data from DB
	public static ResultSet doSelect(Connection conn, String selectSQL, Statement st, ResultSet rs){
		try {
			st = conn.createStatement();
			rs = st.executeQuery(selectSQL);
		} catch (SQLException ex) {
			System.out.println("SQLException: " + ex.getMessage());
		    System.out.println("SQLState: " + ex.getSQLState());
		    System.out.println("VendorError: " + ex.getErrorCode());
		}
		return rs;
	}
	
}