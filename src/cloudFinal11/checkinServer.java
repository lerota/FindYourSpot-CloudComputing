package cloudFinal11;


import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/checkin")
public class checkinServer extends HttpServlet {
	private static final long serialVersionUID = 1L;
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	}
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String[] checkin = request.getParameterValues("checkin");	
		String[] str = requestAnalyze(checkin[0]);
		HttpSession session=request.getSession(false);  
		if(session!=null){  
			String email=(String)session.getAttribute("email");
			String sql = "INSERT INTO Library values ('" +str[0]+ "', '" + str[1] +"', '" + email +"')";
			String sql2 = "Select Count(*) from Library where Lname = '" + str[0] +"' and Room = '"+str[1]+"'";
			try {
				Rds.stmt.executeUpdate(sql);
				ResultSet rset  = Rds.stmt.executeQuery(sql2);
				while(rset.next())
				{
					response.getWriter().println(rset.getString(1));
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
	}
	private String[] requestAnalyze(String str){
		String[] rlt;
		rlt = str.split("-");
		return rlt;				
	}

}
