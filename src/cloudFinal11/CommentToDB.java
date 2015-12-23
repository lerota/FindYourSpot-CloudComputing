package cloudFinal11;


import java.sql.*;
public class CommentToDB {
	public static Connection createConnection() throws Exception{
		Connection conn = null;
		String userName = "cloudFinal";
		String password = "cloudcomputing";
		String hostname = "cs6998cloud.ctevoy1kkrec.us-east-1.rds.amazonaws.com";
		String port = "3306";
		String dbName = "cloudFinal";
		String jdbcUrl = "jdbc:mysql://" + hostname + ":" + port + "/" + dbName + "?user=" + userName + "&password=" + password;
		DriverManager.registerDriver(new com.mysql.jdbc.Driver());
		Class.forName("com.mysql.jdbc.Driver");
		conn = DriverManager.getConnection(jdbcUrl);
		return conn;
	}
	public static void closeConnection(Connection con) {
		if (con != null) {
			try {
				con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
}