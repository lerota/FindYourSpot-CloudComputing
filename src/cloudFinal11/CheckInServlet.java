package cloudFinal11;



import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.sql.*;
import java.util.Enumeration;

@WebServlet("/checkInServlet")
public class CheckInServlet extends HttpServlet{
	private static final long serialVersionUID = 1L;
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		try{
			String choice = request.getParameter("rooms");
				if(request.getSession().getAttribute("checkRoom")!=null){
					String sql = "select count from checkcount where library = '"+request.getSession().getAttribute("checkRoom")+"';";
					ResultSet rset;
					rset = Rds.stmt.executeQuery(sql);
					rset.next();
					int count = Integer.parseInt(rset.getString(1));
					count=count-1;
					sql = "update checkcount set count ="+count+ " where library = '"+request.getSession().getAttribute("checkRoom")+"';";
					Rds.stmt.executeUpdate(sql);					
					request.getSession().setAttribute("checkRoom", choice);
				}
					String sql = "select count from checkcount where library = '"+choice+"';";
					ResultSet rset;
					rset = Rds.stmt.executeQuery(sql);
					rset.next();
					int count = Integer.parseInt(rset.getString(1));
					count = count+1;
					sql = "update checkcount set count ="+count+ " where library = '"+choice+"';";
					Rds.stmt.executeUpdate(sql);
					
					request.getSession().setAttribute("checkRoom", choice);
					
					String referer = request.getHeader("referer");
					response.sendRedirect(referer);
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	}
}
