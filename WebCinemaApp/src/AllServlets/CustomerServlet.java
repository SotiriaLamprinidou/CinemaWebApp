package AllServlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;
import java.util.ArrayList;
import java.util.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import FilmSelection.Reservations;
import FilmSelection.Provoles;

import java.text.ParseException;
import java.text.SimpleDateFormat;


/**
 * Servlet implementation class ClientServlet
 */
@WebServlet("/CustomerServlet")
public class CustomerServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public CustomerServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html; charset=UTF-8");
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		
		String username = null;
		
		HttpSession session = request.getSession(false);
        if (session != null && session.getAttribute("isLoggedIn") != null && (boolean) session.getAttribute("isLoggedIn")) {
            username = (String) session.getAttribute("username");
        }

        String referer = request.getHeader("referer");
        if (referer != null) {
           
        String jspFileName = extractJspFileName(referer);
        
        if ("Reservations".equals(jspFileName)) {
        	String filmTitle = request.getParameter("filmTitle");
     		String CinemaName = request.getParameter("CinemaName");
     		String DateTime = request.getParameter("DateTime");
     		String numberOfReservationsParameter = request.getParameter("NumberOfReservations");
     		int numberOfReservations = 0;

     		if (numberOfReservationsParameter != null && !numberOfReservationsParameter.isEmpty()) {
     		    try {
     		        numberOfReservations = Integer.parseInt(numberOfReservationsParameter);
     		    } catch (NumberFormatException e) {
     		         e.printStackTrace();
     		    }
     		}
     		
     		boolean Provoli = false;
     		boolean AvailableSeats = false;
     		
     		try {
     			Provoli = checkProvoli(filmTitle,CinemaName,DateTime);
     		} catch (ClassNotFoundException e) {
     			e.printStackTrace();
     		}
     		
     		try {
     			if(!Provoli){
     				alertBox(response,"Insufficient data. Please  try again!");
     			}else if(Provoli){
     				AvailableSeats = checkSeatAvailability(CinemaName,numberOfReservations);
     				if(!AvailableSeats) {
     					alertBox(response,"Insufficient seats for the reservation. Please make another one!");
     				}else if(AvailableSeats) {
     					
     					int rowsAffected = 0;
     					rowsAffected = makeReservation(filmTitle,CinemaName,username,numberOfReservations);
     					if(rowsAffected > 0) {
     						alertBox(response,"Successfull Reservation!");
     					}else if(rowsAffected == 0) {
     						alertBox(response,"A problem occured! Pleasy try again!");
     					}
     				}
     			}
     		} catch (Exception e) {
     			e.printStackTrace();
     		}
        }else if ("FilmProgram".equals(jspFileName)) {
            String filmTitle = request.getParameter("filmTitle");
            String fromDateTime = request.getParameter("fromDateTime");
            String untilDateTime = request.getParameter("untilDateTime");

            List<Provoles> provoles = searchProvoles(filmTitle, fromDateTime, untilDateTime);

            PrintWriter out = response.getWriter();
            out.println("<html>");
            out.println("<head>");
            out.println("<style>");
            out.println("body {");
            out.println("    background-color: #f2f2f2;");
            out.println("    font-family: Arial, sans-serif;");
            out.println("    color: #333333;");
            out.println("}");
            out.println("h1 {");
            out.println("    color: #0066cc;");
            out.println("}");
            out.println("table {");
            out.println("    border-collapse: collapse;");
            out.println("    width: 100%;");
            out.println("}");
            out.println("th, td {");
            out.println("    padding: 8px;");
            out.println("    text-align: left;");
            out.println("    border-bottom: 1px solid #dddddd;");
            out.println("}");
            out.println("tr:nth-child(even) {");
            out.println("    background-color: #f9f9f9;");
            out.println("}");
            out.println("</style>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Provoles</h1>");
            out.println("<table>");
            out.println("<tr><th>Film Title</th><th>Cinema Name</th><th>Date and Time</th></tr>");

            for (Provoles provole : provoles) {
                out.println("<tr>");
                out.println("<td>" + provole.getFilmTitle() + "</td>");
                out.println("<td>" + provole.getCinemaName() + "</td>");
                out.println("<td>" + provole.getDateTime() + "</td>");
                out.println("</tr>");
            }

            out.println("</table>");
            out.println("</body>");
            out.println("</html>");

        }
      }
    }

	private String extractJspFileName(String referer) {
        String[] parts = referer.split("/");
        String lastPart = parts[parts.length - 1];
        String[] fileParts = lastPart.split("\\.");
        return fileParts[0];
    }

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		try {
			doGet(request, response);
		} catch (ServletException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	
	private boolean checkProvoli(String filmTitle, String cinemaName, String dateTime) throws ClassNotFoundException {
	    String command = "SELECT * FROM provoles WHERE Films_FilmTitle=? AND Cinemas_CinemaName=? AND DateTime=?";
	    boolean provoliExists = false;

	    try (Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/mydb", "root", "Patata.12");
	         PreparedStatement checkProvoli = con.prepareStatement(command)) {

	        checkProvoli.setString(1, filmTitle);
	        checkProvoli.setString(2, cinemaName);
	        checkProvoli.setString(3, dateTime);
	        ResultSet resultSet = checkProvoli.executeQuery();

	        if (resultSet.next()) {
	            provoliExists = true;
	        }

	        resultSet.close();
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }

	    return provoliExists;
	}
	
	public boolean checkSeatAvailability(String cinemaName, int numberOfReservations) {
	    try {
	        Class.forName("com.mysql.cj.jdbc.Driver");
	        Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/mydb", "root", "Patata.12");

	        // Get the total number of seats in the cinema
	        String getCinemaSeatsQuery = "SELECT CinemaNumberOfSeats FROM cinemas WHERE CinemaName = ?";
	        PreparedStatement getCinemaSeatsStatement = connection.prepareStatement(getCinemaSeatsQuery);
	        getCinemaSeatsStatement.setString(1, cinemaName);
	        ResultSet cinemaSeatsResultSet = getCinemaSeatsStatement.executeQuery();

	        if (cinemaSeatsResultSet.next()) {
	            int totalSeats = cinemaSeatsResultSet.getInt("CinemaNumberOfSeats");

	            // Get the number of reserved seats for the given cinema
	            String getReservedSeatsQuery = "SELECT SUM(NumberOfSeats) AS TotalReservedSeats FROM reservations " +
	                    "WHERE Provoles_Cinemas_CinemaName = ?";
	            PreparedStatement getReservedSeatsStatement = connection.prepareStatement(getReservedSeatsQuery);
	            getReservedSeatsStatement.setString(1, cinemaName);
	            ResultSet reservedSeatsResultSet = getReservedSeatsStatement.executeQuery();

	            if (reservedSeatsResultSet.next()) {
	                int reservedSeats = reservedSeatsResultSet.getInt("TotalReservedSeats");

	                // Calculate the available seats
	                int availableSeats = totalSeats - reservedSeats;

	                // Check if there are enough seats for the reservations
	                if (availableSeats >= numberOfReservations) {
	                    // Close the result sets, statements, and connection
	                    cinemaSeatsResultSet.close();
	                    getCinemaSeatsStatement.close();
	                    reservedSeatsResultSet.close();
	                    getReservedSeatsStatement.close();
	                    connection.close();

	                    return true;  // Enough seats are available
	                }
	            }
	        }

	        // Close the result sets, statements, and connection
	        cinemaSeatsResultSet.close();
	        getCinemaSeatsStatement.close();
	        connection.close();
	    } catch (ClassNotFoundException | SQLException e) {
	        e.printStackTrace();
	    }

	    return false;  // Not enough seats are available
	}


	
	public int makeReservation(String filmTitle, String cinemaName, String username, int numberOfReservations) {
	    int rowsAffected = 0;

	    try {
	        Class.forName("com.mysql.cj.jdbc.Driver");
	        Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/mydb", "root", "Patata.12");

	        // Insert the reservation into the database
	        String insertReservationQuery = "INSERT INTO reservations (NumberOfSeats, Provoles_Films_FilmTitle, " +
	                "Provoles_Cinemas_CinemaName, Customers_Users_Username) VALUES (?, ?, ?, ?)";
	        PreparedStatement insertReservationStatement = connection.prepareStatement(insertReservationQuery);
	        insertReservationStatement.setInt(1, numberOfReservations);
	        insertReservationStatement.setString(2, filmTitle);
	        insertReservationStatement.setString(3, cinemaName);
	        insertReservationStatement.setString(4, username);

	        rowsAffected = insertReservationStatement.executeUpdate();

	        // Close the statement and connection
	        insertReservationStatement.close();
	        connection.close();
	    } catch (ClassNotFoundException | SQLException e) {
	        e.printStackTrace();
	    }

	    return rowsAffected;
	}
	
	

	public List<Provoles> searchProvoles(String filmTitle, String fromDateTime, String untilDateTime) {
	    StringBuilder queryBuilder = new StringBuilder("SELECT * FROM provoles WHERE 1=1");

	    if (filmTitle != null && !filmTitle.isEmpty()) {
	        queryBuilder.append(" AND Films_FilmTitle LIKE ?");
	    }

	    if (fromDateTime != null && !fromDateTime.isEmpty()) {
	        queryBuilder.append(" AND DateTime >= ?");
	    }

	    if (untilDateTime != null && !untilDateTime.isEmpty()) {
	        queryBuilder.append(" AND DateTime <= ?");
	    }

	    List<Provoles> provolesList = new ArrayList<>();

	    try {
	        Class.forName("com.mysql.cj.jdbc.Driver");
	        Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/mydb", "root", "Patata.12");

	        PreparedStatement statement = con.prepareStatement(queryBuilder.toString());
	        int parameterIndex = 1;

	        if (filmTitle != null && !filmTitle.isEmpty()) {
	            statement.setString(parameterIndex++, "%" + filmTitle + "%");
	        }

	        if (fromDateTime != null && !fromDateTime.isEmpty()) {
	            statement.setTimestamp(parameterIndex++, parseDateTime(fromDateTime));
	        }

	        if (untilDateTime != null && !untilDateTime.isEmpty()) {
	            statement.setTimestamp(parameterIndex, parseDateTime(untilDateTime + " 23:59:59")); // Consider the entire untilDateTime day
	        }

	        ResultSet resultSet = statement.executeQuery();

	        while (resultSet.next()) {
	            String dateTime = resultSet.getString("DateTime");

	            if (isDateTimeInRange(dateTime, fromDateTime, untilDateTime)) {
	                Provoles provoles = new Provoles();
	                provoles.setIdProvoles(resultSet.getInt("idProvoles"));
	                provoles.setFilmTitle(resultSet.getString("Films_FilmTitle"));
	                provoles.setCinemaName(resultSet.getString("Cinemas_CinemaName"));
	                provoles.setDateTime(dateTime);

	                provolesList.add(provoles);
	            }
	        }
	    } catch (SQLException | ClassNotFoundException | ParseException e) {
	        e.printStackTrace();
	    }

	    return provolesList;
	}

	private boolean isDateTimeInRange(String dateTime, String fromDateTime, String untilDateTime) throws ParseException {
	    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	    Date date = format.parse(dateTime);

	    if (fromDateTime != null && !fromDateTime.isEmpty()) {
	        Date fromDate = format.parse(fromDateTime);
	        if (date.before(fromDate)) {
	            return false;
	        }
	    }

	    if (untilDateTime != null && !untilDateTime.isEmpty()) {
	        Date untilDate = format.parse(untilDateTime + " 23:59:59");
	        if (date.after(untilDate)) {
	            return false;
	        }
	    }

	    return true;
	}

	private Timestamp parseDateTime(String dateTime) throws ParseException {
	    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	    Date parsedDate = format.parse(dateTime);
	    return new Timestamp(parsedDate.getTime());
	}


	
	private void alertBox(HttpServletResponse response,String message) throws IOException {
		response.setContentType("text/html; charset=UTF-8");
		response.setCharacterEncoding("UTF-8");
		
		PrintWriter out = response.getWriter();
		out.println("<script type=\"text/javascript\">");
		out.println("alert('" + message + "');");
        out.println("window.location.href='Reservations.jsp';"); // Redirect to an error page
        out.println("</script>");
        
	}
}
