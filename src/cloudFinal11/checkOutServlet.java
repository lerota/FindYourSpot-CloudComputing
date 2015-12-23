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

@WebServlet("/checkOutServlet")
public class checkOutServlet extends HttpServlet{
	private static final long serialVersionUID = 1L;
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try{
			if(request.getParameter("checkout").equals("yes")){
				String sql = "select count from checkcount where library = '"+request.getSession().getAttribute("checkRoom")+"';";
				ResultSet rset;
				rset = Rds.stmt.executeQuery(sql);
				rset.next();
				int count = Integer.parseInt(rset.getString(1));
				if(count>0){count = count-1;}
				sql = "update checkcount set count ="+count+ " where library = '"+request.getSession().getAttribute("checkRoom")+"';";
				Rds.stmt.executeUpdate(sql);
				
				request.getSession().setAttribute("checkRoom", null);
				
				String referer = request.getHeader("referer");
				response.sendRedirect(referer);
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	}
}
