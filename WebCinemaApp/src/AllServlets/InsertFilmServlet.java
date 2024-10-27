package AllServlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import FilmSelection.Films;

/**
 * Servlet implementation class InsertFilmServlet
 */
@WebServlet("/InsertFilmServlet")
public class InsertFilmServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
   
    public InsertFilmServlet() {
        super();
       
    }

	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html; charset=UTF-8");
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		
		String filmTitle = request.getParameter("filmTitle");
		String filmCategory = request.getParameter("filmCategory");
		String filmDescription = request.getParameter("filmDescription");
		String username = null;
		
		HttpSession session = request.getSession(false);
        if (session != null && session.getAttribute("isLoggedIn") != null && (boolean) session.getAttribute("isLoggedIn")) {
            username = (String) session.getAttribute("username");
        }
        
		int rowsAffected = 0;
		try {
			rowsAffected = insertFilm(filmTitle,filmCategory,filmDescription,username);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		if(rowsAffected > 0) {
			alertBox(response,"Film Inserted");
		}else if(rowsAffected == 0) {
			alertBox(response,"Film already exist! Enter a new film!");
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}
	
	
	private int insertFilm(String filmTitle, String filmCategory, String filmDescription, String username) throws ClassNotFoundException {
		
		try {
		    Class.forName("com.mysql.cj.jdbc.Driver");
		    Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/mydb", "root", "Patata.12");
		    
		    // Check if the film already exists
		    String selectCommand = "SELECT filmTitle FROM films WHERE filmTitle = ?";
		    PreparedStatement selectFilm = con.prepareStatement(selectCommand);
		    selectFilm.setString(1, filmTitle);
		    ResultSet resultSet = selectFilm.executeQuery();
		    
		    boolean findFilm = resultSet.next();
		    resultSet.close();
		    selectFilm.close();
		    
		    int rowsAffected = 0;
		    if (!findFilm) {
		    	
		        String insertCommand = "INSERT INTO films(filmTitle, filmCategory, filmDescription, ContentAdmins_UserNameContentAdmins) VALUES (?, ?, ?, ?)";
		        PreparedStatement insertFilm = con.prepareStatement(insertCommand);
		        insertFilm.setString(1, filmTitle);
		        insertFilm.setString(2, filmCategory);
		        insertFilm.setString(3, filmDescription);
		        insertFilm.setString(4, username);
		        
		        rowsAffected = insertFilm.executeUpdate();
		        insertFilm.close();
		    }
		    
		    con.close();
		    
		    return rowsAffected;
		} catch (ClassNotFoundException | SQLException e) {
		    e.printStackTrace();
		}

		return 0;

	}
	
	private void alertBox(HttpServletResponse response,String message) throws IOException {
		response.setContentType("text/html; charset=UTF-8");
		response.setCharacterEncoding("UTF-8");
		
		PrintWriter out = response.getWriter();
		out.println("<script type=\"text/javascript\">");
		out.println("alert('" + message + "');");
        out.println("window.location.href='InsertFilm.jsp';"); // Redirect to an error page
        out.println("</script>");
        
	}

}
