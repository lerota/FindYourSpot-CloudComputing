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

import org.json.JSONObject;

@WebServlet("/unansweredquestions")
public class unansweredquestions extends HttpServlet {
    private static final long serialVersionUID = 1L;
    static Connection conn = null;
	static Statement setupStatement = null;
	static ResultSet resultSet = null;	
    
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
   public void doGet(HttpServletRequest request, 
                       HttpServletResponse response) throws IOException, ServletException{

			String selectSQL = null;
			JSONObject messageObject = null;

			selectSQL = "SELECT * from Questions q1, Q_A t1 WHERE q1.Question_id = t1.Question_id AND t1.Answer_id IS NULL";
			try {
				messageObject = selectUnanswered(selectSQL);
			} catch (Exception e) {
				e.printStackTrace();
			}
			
	        request.setAttribute("data", messageObject.toString());
	        request.getRequestDispatcher("unansweredquestions.jsp").forward(request, response); 

   } 	
   public static JSONObject selectUnanswered(String selectSQL) throws Exception{
		final Connection conn = Rds2.conn;
	   	int countMes = 1;

	   	resultSet = DBcontrol.doSelect(conn, selectSQL, setupStatement, resultSet);
	   	JSONObject mesOBJ = new JSONObject();
			while(resultSet.next()){
				String qId = resultSet.getString("Question_id");
				String qtext = resultSet.getString("Question");
				String quserId = resultSet.getString("QUser");
				String qtime1 = resultSet.getString("QTimestamp");
				String qtime = qtime1.substring(0, 10) + " " + qtime1.substring(11,19);
				
				JSONObject mes = new JSONObject();
				
				//add flag "location" to JSON object
				mes.put("flag", "unanswered");
				mes.put("qId", qId);
				mes.put("qtext", qtext);
				mes.put("quserId", quserId);
				mes.put("qtime", qtime);
				
				mesOBJ.put("mes" + countMes, mes);
				countMes++;
			}
			return mesOBJ; 	
   }

}