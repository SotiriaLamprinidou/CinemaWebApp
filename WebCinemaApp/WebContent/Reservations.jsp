<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<title>Reservations</title>
 	<link rel="stylesheet" type="text/css" href="./css/cinema.css" />
</head>
<body>
	<div class="topnav">
		  <a href="Customer.jsp">Home</a>
		  <a href="FilmProgram.jsp">Film Program</a>
		  <a class="active" href="Reservations.jsp">Make a reservation</a>
		  <a href="/WebCinemaApp/reservationhistory">Reservation's History</a>
		  <a href="/WebCinemaApp/logout">Logout</a>
	</div>
	
	<div class="login-form">
        <form action = "CustomerServlet" method = "post">
            <h2>Make a Reservation</h2>
            <div class="form-group">
                <input type="text" class="form-control" placeholder=" Film Title" required="required" name = "filmTitle">
            </div>
            <div class="form-group">
                <input type="text" class="form-control" placeholder=" Cinema Name" required="required" name = "CinemaName">
            </div>
            <div class="form-group">
                <input type="datetime" class="form-control" placeholder=" DateTime ex.2023-05-30 16:00:00" required="required" name = "DateTime">
            </div>
            <div class="form-group">
                <input type="text" class="form-control" placeholder=" Number of Reservations" required="required" name = "NumberOfReservations">
            </div>
            <div class="form-group">
                <button type="submit" class="btn btn-primary">Reserve</button>
            </div>
        </form>
    </div>
</body>
</html>