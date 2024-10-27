package AllServlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;

import javax.print.attribute.DateTimeSyntax;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.el.parser.ParseException;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Servlet implementation class AssignFilmServlet
 */
@WebServlet("/AssignFilmServlet")
public class AssignFilmServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    
    public AssignFilmServlet() {
        super();
    }

	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html; charset=UTF-8");
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		
		String filmTitle = request.getParameter("filmTitle");
		String CinemaName = request.getParameter("CinemaName");
		String DateTime = request.getParameter("DateTime");
		
		
		boolean Film = false;
		boolean Cinema = false;
		try {
			Film = checkFilm(filmTitle);
			Cinema = checkCinema(CinemaName);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		
		try {
			if(!Film){
				alertBox(response,"The Film !"+filmTitle+"doesn't exist. Enter another one!");
			}else if(!Cinema) {
				alertBox(response,"The Cinema !"+CinemaName+"doesn't exist. Enter another one!");
			}else if(Film && Cinema){
				int rowsAffected = 0;
				try {
					rowsAffected = assignFilm(filmTitle,CinemaName,DateTime);
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				}
				if(rowsAffected > 0) {
					alertBox(response,"Film Assigned to cinema: "+CinemaName);
				}else if(rowsAffected == 0) {
					alertBox(response,"A problem occured! Please try again!");
				}
			}
		} catch (Exception e) {
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
	
	private boolean checkFilm(String filmTitle) throws ClassNotFoundException {
		try {
		    Class.forName("com.mysql.cj.jdbc.Driver");
		    Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/mydb", "root", "Patata.12");
		    
		    // Check if the film exists
		    String selectCommand = "SELECT filmTitle FROM films WHERE filmTitle = ?";
		    PreparedStatement searchFilm = con.prepareStatement(selectCommand);
		    searchFilm.setString(1, filmTitle);
		    ResultSet resultSet = searchFilm.executeQuery();
		    
		    boolean findFilm = resultSet.next();
		    resultSet.close();
		    searchFilm.close();
		    con.close();
		    return findFilm;
		    
		} catch (ClassNotFoundException | SQLException e) {
		    e.printStackTrace();
		}
		return false;
	}
	
	private boolean checkCinema(String CinemaName) throws ClassNotFoundException {
		try {
		    Class.forName("com.mysql.cj.jdbc.Driver");
		    Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/mydb", "root", "Patata.12");
		    
		    // Check if the film exists
		    String selectCommand = "SELECT CinemaName FROM cinemas WHERE CinemaName = ?";
		    PreparedStatement searchCinema = con.prepareStatement(selectCommand);
		    searchCinema.setString(1, CinemaName);
		    ResultSet resultSet = searchCinema.executeQuery();
		    
		    boolean  findCinema = resultSet.next();
		    resultSet.close();
		    searchCinema.close();
		    con.close();
		    
		    return findCinema;
		} catch (ClassNotFoundException | SQLException e) {
		    e.printStackTrace();
		}
		return false;
	}
	
	private int assignFilm(String filmTitle, String CinemaName, String DateTime) throws ClassNotFoundException {
		
		try {
		    Class.forName("com.mysql.cj.jdbc.Driver");
		    Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/mydb", "root", "Patata.12");
		    // Check if the film exists
		    String selectCommand = "INSERT INTO provoles(Films_FilmTitle, Cinemas_CinemaName, DateTime) VALUES (?, ?, ?)";
		    PreparedStatement assignFilm = con.prepareStatement(selectCommand);
		    assignFilm.setString(1, filmTitle);
	        assignFilm.setString(2, CinemaName);
	        assignFilm.setString(3, DateTime);
	        int rowsAffected = assignFilm.executeUpdate();
	        
		    assignFilm.close();
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
