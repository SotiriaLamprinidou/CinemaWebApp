<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ page import="java.util.*" %>
<%@ page import="java.util.List" %>
<%@ page import="FilmSelection.Reservations" %>
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<title>Reservation's History</title>
 	<link rel="stylesheet" type="text/css" href="./css/cinema.css" />
</head>
<body>
	<div class="topnav">
		  <a href="Customer.jsp">Home</a>
		  <a href="FilmProgram.jsp">Film Program</a>
		  <a href="Reservations.jsp">Make a reservation</a>
		  <a class="active" href="/WebCinemaApp/reservationhistory">Reservation's History</a>
		  <a href="/WebCinemaApp/logout">Logout</a>
	</div>
	
	<br><br><br>
	<h1><center>Reservation's History</center></h1>
	<br>
	<div class="films-container">

	
   <ul>
    <% 
    List<Reservations> reservations = (List<Reservations>) session.getAttribute("reservations");
    if (reservations != null && !reservations.isEmpty()) {
        for (Reservations reservation : reservations) {
    %>
    <li>
       <div style="margin-bottom: 20px;">
        <span style="display: inline-block; width: 120px; font-weight: bold;">Film Title:</span>
        <span style="display: inline-block; width: 200px;"><%= reservation.getFilmTitle() %></span><br>
        <span style="display: inline-block; width: 120px; font-weight: bold;">Cinema Name:</span>
        <span style="display: inline-block; width: 200px;"><%= reservation.getCinemaName() %></span><br>
        <span style="display: inline-block; width: 120px; font-weight: bold;">Number of Seats:</span>
        <span style="display: inline-block; width: 200px;"><%= reservation.getNumberOfSeats() %></span><br>
        </div>
    </li>
    <% 
        }
    } else {
    %>
    <li>No reservations made!</li>
    <% 
        }
    %>
</ul>

</div>


</body>
</html>