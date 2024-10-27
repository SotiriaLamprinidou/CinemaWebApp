package AllServlets;

import java.io.IOException;
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
import FilmSelection.Reservations;

/**
 * Servlet implementation class ReservationHistoryServlet
 */
@WebServlet("/ReservationHistoryServlet")
public class ReservationHistoryServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ReservationHistoryServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		String username = null;
		
		HttpSession session = request.getSession(false);
        if (session != null && session.getAttribute("isLoggedIn") != null && (boolean) session.getAttribute("isLoggedIn")) {
            username = (String) session.getAttribute("username");
        }
        
      
            
       String query = "SELECT * FROM reservations WHERE Customers_Users_Username = ?";
		 try {
		        Class.forName("com.mysql.cj.jdbc.Driver");
		        Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/mydb", "root", "Patata.12");

		        PreparedStatement statement = connection.prepareStatement(query);
		        statement.setString(1, username);

		        ResultSet resultSet = statement.executeQuery();
		        List<Reservations> reservations = new ArrayList<>();
		        while (resultSet.next()) {
		            int numberOfSeats = resultSet.getInt("NumberOfSeats");
		            String filmTitle = resultSet.getString("Provoles_Films_FilmTitle");
		            String cinemaName = resultSet.getString("Provoles_Cinemas_CinemaName");

		            // Create a Reservation object and add it to the list
		            Reservations reservation = new Reservations(numberOfSeats, filmTitle, cinemaName);
		            reservations.add(reservation);
		        }

		        session.setAttribute("reservations", reservations);
	            response.sendRedirect("History.jsp");
	            

		    } catch (ClassNotFoundException | SQLException e) {
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
