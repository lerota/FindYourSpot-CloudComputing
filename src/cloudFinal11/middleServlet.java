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

@WebServlet("/middleServlet")
public class middleServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    static Statement setupStatement = null;
	static ResultSet resultSet = null;	
	
    @Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		

    }
   public void doGet(HttpServletRequest request, 
                       HttpServletResponse response) throws ServletException, IOException{
	   	HttpSession session=request.getSession();  
	   	JSONObject messageObject = new JSONObject();
		String selectSQL = null;
		selectSQL = "SELECT * FROM User u1 WHERE u1.Email = '" + session.getAttribute("email").toString() + "';";
		try {
			messageObject = selectPoints(selectSQL);
		} catch (Exception e) {
			e.printStackTrace();
		}
	    session.setAttribute("points", messageObject.toString());
		request.getRequestDispatcher("allquestions2.jsp").forward(request, response);
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