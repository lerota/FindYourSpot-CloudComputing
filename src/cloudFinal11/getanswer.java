package cloudFinal11;


import java.io.*;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.JSONObject;

@WebServlet("/getanswer")
public class getanswer extends HttpServlet {
    private static final long serialVersionUID = 1L;
	static Statement setupStatement = null;
	static ResultSet resultSet = null;	
    
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
    }
   public void doGet(HttpServletRequest request, 
                       HttpServletResponse response) throws IOException, ServletException{
	   HttpSession session=request.getSession(); 
       JSONObject messageObject = new JSONObject();
       JSONObject messageObject2 = new JSONObject();

		String selectSQL = null;
		selectSQL = "SELECT * FROM Questions q1, Answers a1, Q_A t1 WHERE q1.Question_id = t1.Question_id AND a1.Question_id = t1.Question_id AND a1.AUser = '" + session.getAttribute("email").toString() + "';";
		try {
			messageObject = selectUserAnswer(selectSQL);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		String selectSQL2 = null;
		selectSQL2 = "SELECT * FROM User u1 WHERE u1.Email = '" + session.getAttribute("email").toString() + "';";
		try {
			messageObject2 = selectPoints(selectSQL2);
		} catch (Exception e) {
			e.printStackTrace();
		}

       request.setAttribute("data", messageObject.toString());
       request.setAttribute("points", messageObject2.toString());

       request.getRequestDispatcher("useranswer.jsp").forward(request, response); 
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
			String qtime1 = resultSet.getString("QTimestamp");
			String qtime = qtime1.substring(0, 10) + " " + qtime1.substring(11,19);
			String aId = resultSet.getString("Answer_id");
			String atext = resultSet.getString("Answer");
			String auserId = resultSet.getString("AUser");
			String atime1 = resultSet.getString("ATimestamp");
			String atime = atime1.substring(0, 10) + " " + atime1.substring(11,19);
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
   
   
   public static JSONObject selectPoints(String selectSQL) throws Exception{
	   final Connection conn = Rds2.conn;		
		resultSet = DBcontrol.doSelect(conn, selectSQL, setupStatement, resultSet);
		JSONObject mesOBJ = new JSONObject();
		while (resultSet.next()) {
			Integer points = resultSet.getInt("points");
		    String pointsToString = points.toString();			
			mesOBJ.put("points", pointsToString);
		}
		return mesOBJ; 	
  }

}