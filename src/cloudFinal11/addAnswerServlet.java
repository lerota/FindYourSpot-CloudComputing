package cloudFinal11;


import java.io.*;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class addAnswerServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String qId = request.getParameter("qId");
		HttpSession session=request.getSession();  
        session.setAttribute("qId",qId);

		System.out.println(qId);
		request.getRequestDispatcher("answer.jsp").forward(request, response);  

    }
   public void doGet(HttpServletRequest request, 
                       HttpServletResponse response){
   } 
}