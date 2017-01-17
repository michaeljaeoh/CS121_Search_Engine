// Mohammad Zahed, Samuel Thompson, Mayra Yareli Gamboa, Michael Oh 

package searchservlets;

import searchclasses.Score;
import searchclasses.SearchQuery;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class SearchEngine
 */
@WebServlet("/SearchEngine")
public class SearchEngine extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private List<Score> ranking = new ArrayList<Score>();	
	
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		//response.getWriter().append("Served at: ").append(request.getContextPath());
		String query = request.getParameter("query");
				
		ranking = SearchQuery.search(query);
		
		response.setContentType("text/html");
		
		Connection conn = null;
		
		try {
				
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection("jdbc:mysql://localhost/search_index","user","user123");
			
			PrintWriter output = response.getWriter();		
			
			String dbQuery = null;
			PreparedStatement ps = null;			
			ResultSet rs = null;
			
			output.println("<html><body><h3>Results for: <em>"+query+"</em></h3><br/><br/>");
			
	        for(int i = 0; i < Math.min(10,ranking.size()); i++){
	        	dbQuery = "SELECT title,url FROM Document WHERE did = " + ranking.get(i).getID();
				ps = conn.prepareStatement(dbQuery);
				rs = ps.executeQuery();
				while(rs.next()) {					
					String url = rs.getString("url");
					output.println("<p><a href=\""+url+"\">"+url+"</a></p><br/>");
				}
				rs.close();
	        }
		
			output.println("</body></html>");
			conn.close();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}
}
