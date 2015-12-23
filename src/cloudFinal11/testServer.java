package cloudFinal11;



import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@SuppressWarnings("serial")
@WebServlet("/testServer")
public class testServer  extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html");  
		PrintWriter out=response.getWriter();  

		HttpSession session=request.getSession(false);  
		if(session!=null){  
			String email=(String)session.getAttribute("email");  

			out.print("Hello, "+email+" Welcome to Profile");  
		}  
		else{
			request.getRequestDispatcher("signin.html").include(request, response);  
		}  
		out.close();  
	}
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

	}

}
