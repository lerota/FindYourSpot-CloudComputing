package cloudFinal11;




import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.sql.*;

@WebServlet("/auth")
public class authServer extends HttpServlet {

	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//     // select put here !!!!!!!!!
		//		response.setContentType("text/html");  
		//		PrintWriter out = response.getWriter();
		//		out.println("In dispatcherServlet <BR>");
		// 
		//		RequestDispatcher rd = request.getRequestDispatcher("test.html");
		//		rd.include(request, response);
	}
	@SuppressWarnings("resource")
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String[] email = request.getParameterValues("Email");
		String[] password = request.getParameterValues("Password");
		String rlt= "";
		String checkRoom= null;
		try {
			String str = "Select Password, Uname from User where Email ='" +  email[0]  + "'";
			System.out.println(str);
			ResultSet rset  = Rds.stmt.executeQuery(str);
			response.setContentType("text/html");
			PrintWriter out=response.getWriter();

			if(!rset.next())
			{	request.getRequestDispatcher("signin.html").include(request, response);
				out.print("<p align = \"center\" style=\"color:#fff;\"> Wrong Email or Password, Please try again!");
			}
			else
			{
				if( rset.getString(1).equals(password[0]) ){  
					String currentEmail = email[0];
					String usrStr = rset.getString(2);
					//check if the user logged in before
					rset = Rds.stmt.executeQuery("select email from dailyCheckIn where email = '"+currentEmail+"'");
					if(!rset.next()){
						str = "INSERT INTO dailyCheckIn values ('" +currentEmail+ "', " + null+")";
						System.out.println(str);
						Rds.stmt.executeUpdate(str);
					}
					try {//get current checked room for current user
						rset  = Rds.stmt.executeQuery("SELECT checkedRoom FROM dailyCheckIn WHERE email = "+"'"+currentEmail+"'");
						while(rset.next()){
							if(rset.getString(1)!=null){
								checkRoom=rset.getString(1);
							};
						}						
						//get all roomCheck count
					} catch (SQLException e) {
						e.printStackTrace();
					}					   
					HttpSession session=request.getSession();  
					session.setAttribute("email",email[0]);
					session.setAttribute("usr", usrStr);
					session.setAttribute("usrName", usrStr);
					session.setAttribute("checkRoom", checkRoom);

					response.sendRedirect("libinfo.jsp");
				}
				else{
					request.getRequestDispatcher("signin.html").include(request, response);
					out.print("<p align = \"center\" style=\"color:#fff;\"> Wrong Email or Password, Please try again! New user? <a href=\"register.html\">Sign Up Now!</a></p>");
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
			rlt = "failure";
		}
	}	


}


