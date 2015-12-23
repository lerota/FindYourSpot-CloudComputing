package cloudFinal11;




import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
@WebServlet("/signoutServer")
public class signoutServer extends HttpServlet {
	private static final long serialVersionUID = 1L;
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		 response.setContentType("text/html");  
         PrintWriter out=response.getWriter();  
         request.getRequestDispatcher("index.html").include(request, response);  
         HttpSession session=request.getSession();  
         String sql;
         if(session.getAttribute("checkRoom")!=null){
         sql = "update dailyCheckIn set checkedRoom ='"+session.getAttribute("checkRoom")+ "' where email = '"+session.getAttribute("email")+"';";
         }
         else{
             sql = "update dailyCheckIn set checkedRoom ="+session.getAttribute("checkRoom")+ " where email = '"+session.getAttribute("email")+"';";
             }
			
			try {
				Rds.stmt.executeUpdate(sql);
			} catch (SQLException e) {
				e.printStackTrace();
			}
         session.invalidate();  
           
         out.print("<p align = \"center\" style=\"color:#fff;\">You have successfully logged out!");  
           
         out.close();  
	}

}
