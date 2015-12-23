package cloudFinal11;


import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/iniServer")
public class iniServer extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String[] library ={"Butler"};
		ResultSet rset;
		String jsonStr;
		
		for (int i=0; i<library.length;i++)
		{
			String sql = "select Room,count(*) from Library where Lname = \'"+ library[i]+"\' group by Room";
			try {
				rset = Rds.stmt.executeQuery(sql);
				jsonStr ="{ \""+ library[i] +"\": {";
				while(rset.next())
				{
				    jsonStr += "\"" + rset.getString(1)+"\":"  + rset.getString(2) +", ";
				    
				}
				jsonStr = jsonStr.substring(0,jsonStr.length()-2) +"}}";
				PrintWriter out=response.getWriter();
					out.print(jsonStr);
				out.close();  
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	
	}

}
