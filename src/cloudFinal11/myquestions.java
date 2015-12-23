package cloudFinal11;

import java.io.*;
import java.sql.ResultSet;
import java.sql.Connection;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.JSONObject;

import com.mysql.jdbc.Statement;

@WebServlet("/myquestions")
public class myquestions extends HttpServlet {
	private static final long serialVersionUID = 1L;
	static Statement setupStatement = null;
	static ResultSet resultSet = null;
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
	}
	
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		String selectSQL = null;
		JSONObject messageObject = null;
		Connection conn = null;
		HttpSession session=request.getSession();  

		try {
			conn = Rds2.conn;
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		selectSQL = "SELECT t1.Answer_id FROM Q_A t1, Questions q1 WHERE q1.Question_id = t1.Question_id AND q1.QUser = '" + session.getAttribute("email").toString() + "';";
		ResultSet result = null;
		result = DBcontrol.doSelect(conn, selectSQL, setupStatement, result);
		try {
			while (result.next()) {
				String answer_id = result.getString("Answer_id");
				if (answer_id!="NULL") {
					selectSQL = "SELECT * FROM Questions q1, Answers a1, Q_A t1 WHERE q1.Question_id = t1.Question_id AND a1.Question_id = t1.Question_id AND q1.QUser = '" + session.getAttribute("email").toString() + "';";
				}
			}
		} catch (SQLException e1) {
			e1.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			messageObject = selectMyquestion(selectSQL);
		} catch (Exception e) {
			e.printStackTrace();
		}
		request.setAttribute("data", messageObject.toString());
		request.getRequestDispatcher("userquestion.jsp").forward(request, response);
	}
	
	public static JSONObject selectMyquestion(String selectSQL) throws Exception{
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
			String aId = null;
            String atext = null;
            String auserId = null;
            String atime = null;
			if (resultSet.getMetaData().getColumnCount() > 4) {
				if (resultSet.getString("Answer_id") != null) {
					aId = resultSet.getString("Answer_id");
				}
				
				if (resultSet.getString("Answer") != null) {
					atext = resultSet.getString("Answer");
				}
				
				if (resultSet.getString("AUser") != null) {
					auserId = resultSet.getString("AUser");
				}
				
				if (resultSet.getString("ATimestamp") != null) {
					String atime1 = resultSet.getString("ATimestamp");
					atime = atime1.substring(0, 10) + " " + atime1.substring(11,19);				}
			}
			JSONObject mes = new JSONObject();
			
			//add flag "location" to JSON object
			mes.put("flag", "all");
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