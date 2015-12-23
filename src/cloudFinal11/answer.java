package cloudFinal11;


import java.io.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.JSONException;
import org.json.JSONObject;

@WebServlet("/answer")
public class answer extends HttpServlet {
    private static final long serialVersionUID = 1L;
	static Statement setupStatement = null;
	static ResultSet resultSet = null;	
    
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session=request.getSession(); 
        String userId = session.getAttribute("email").toString();
		
		String qId = session.getAttribute("qId").toString();
		String aId = request.getParameter("aId");
		String text = request.getParameter("text");
		String time = request.getParameter("time");
        
		JSONObject messageObject = new JSONObject();
		try {
			messageObject.put("Id",userId);
		} catch (JSONException e1) {
			e1.printStackTrace();
		}
		try {
			messageObject.put("Aid",aId);
		} catch (JSONException e1) {
			e1.printStackTrace();
		}
		try {
			messageObject.put("Text",text);
		} catch (JSONException e1) {
			e1.printStackTrace();
		}
		try {
			messageObject.put("Timestamp",time);
		} catch (JSONException e1) {
			e1.printStackTrace();
		}
		try {
			messageObject.put("Qid",qId);
		} catch (JSONException e1) {
			e1.printStackTrace();
		}

		String JSONstring =  messageObject.toString();

		try {
			answertoDB(JSONstring);
		} catch (Exception e) {
			e.printStackTrace();
		}	
		
		request.getRequestDispatcher("allquestions.jsp").forward(request, response);

    }
   public void doGet(HttpServletRequest request, 
                       HttpServletResponse response) throws IOException, ServletException{

   } 	
   public static void answertoDB(String message) throws Exception{
		String selectSQL = null;
 
	   	JSONObject jsonMessage = new JSONObject(message);
	   	
	   	String Id = jsonMessage.getString("Id").toString();
		String aId = jsonMessage.getString("Aid").toString();
		String text = jsonMessage.getString("Text").toString();
		String time = jsonMessage.getString("Timestamp").toString();
		String qid = jsonMessage.getString("Qid").toString();
		
	   	try {
    		final Connection conn = Rds2.conn;
    		
//    		PreparedStatement preparedStatement = null;
    		
			String str = "INSERT INTO Answers VALUES('" +Id +"', '" + aId +"', '" + text +"', '" + time +"', '" + qid +"')";
			Rds.stmt.executeUpdate(str);
			
			//update table Q_A
			selectSQL = "SELECT Answer_id from Q_A WHERE Question_id = '" + jsonMessage.getString("Qid").toString() + "' and Answer_id is NULL;";
			resultSet = DBcontrol.doSelect(conn, selectSQL, setupStatement, resultSet);
			
    		while(resultSet.next()){
    			
    			//String Answerid = resultSet.getString("Answer_id").toString();
    			
    			//String strtest2 = "INSERT INTO test4 VALUES('" + Answerid + "')";
				//Rds.stmt.executeUpdate(strtest2);
				
    			//if (Answerid == null) {
				//if (resultSet.getString("Answer_id").equals(null)){
    			//if (resultSet.wasNull()) {
    				
    				String strtest = "INSERT INTO test VALUES(1)";
    				Rds.stmt.executeUpdate(strtest);
    				
    				String str2 = "UPDATE Q_A SET Answer_id ='" +aId +"' WHERE Question_id = '" + qid +"'";
					Rds.stmt.executeUpdate(str2);
    			//}
    		}
			//update points in table User
			selectSQL = "SELECT points from User WHERE Email = '" + jsonMessage.getString("Id") + "';";
    		resultSet = DBcontrol.doSelect(conn, selectSQL, setupStatement, resultSet);
    		while(resultSet.next()){

	    		int points = resultSet.getInt("points");
	    		int temp = (points+1);
	    		
	    		String str3 = "UPDATE User SET points = "+ temp +" WHERE Email = '" + Id +"'";
				Rds.stmt.executeUpdate(str3);
//	    		preparedStatement = conn.prepareStatement("UPDATE User SET points = ? WHERE Email = ?");
//				preparedStatement.setInt(1, points + 1);
//				preparedStatement.setString(2, jsonMessage.getString("Id"));
//				preparedStatement.executeUpdate();
    		}
    	}
    	 catch (JSONException e) {
			e.printStackTrace();
    	}
   }
   
   public static JSONObject selectUserAnswer(String selectSQL) throws Exception{
	   final Connection conn = Rds2.conn;
		int countMes = 1;
		
		resultSet = DBcontrol.doSelect(conn, selectSQL, setupStatement, resultSet);
		JSONObject mesOBJ = new JSONObject();
		while (resultSet.next()) {
			String qId = resultSet.getString("Question_id");
			String qtext = resultSet.getString("Question");
			String quserId = resultSet.getString("QUser");
			String qtime = resultSet.getString("QTimestamp");
			String aId = resultSet.getString("Answer_id");
			String atext = resultSet.getString("Answer");
			String auserId = resultSet.getString("AUser");
			String atime = resultSet.getString("ATimestamp");
			JSONObject mes = new JSONObject();
			
			mes.put("flag", "myanswer");
			mes.put("qId", qId);
			mes.put("qtext", qtext);
			mes.put("quserId", quserId);
			mes.put("qtime", qtime);
			mes.put("aId", aId);
			mes.put("atext", atext);
			mes.put("auserId", auserId);
			mes.put("atime", atime);
			mesOBJ.put("mes" + countMes, mes);
			countMes++;
		}
		return mesOBJ; 	
  }

}