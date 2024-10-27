<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<title>Film Program</title>
 	<link rel="stylesheet" type="text/css" href="./css/cinema.css" />
</head>
<body>
	<div class="topnav">
		  <a href="Customer.jsp">Home</a>
		  <a class="active" href="FilmProgram.jsp">Film Program</a>
		  <a href="Reservations.jsp">Make a reservation</a>
		  <a href="/WebCinemaApp/reservationhistory">Reservation's History</a>
		  <a href="/WebCinemaApp/logout">Logout</a>
	</div>
	
	<div class="login-form">
        <form action = "CustomerServlet" method = "post">
            <h2>Film Program</h2>
            <p><center>Fill in the criteria of your preference!</center></p>
            <div class="form-group">
                <input type="text" class="form-control" placeholder=" Film Title" name = "filmTitle">
            </div>
            <div class="form-group">
                <input type="datetime" class="form-control" placeholder=" From DateTime ex.2023-05-30 16:00:00" name = "fromDateTime">
            </div>
            <div class="form-group">
                <input type="datetime" class="form-control" placeholder=" Until DateTime ex.2023-05-31 18:00:00" name = "untilDateTime">
            </div>
            <div class="form-group">
                <button type="submit" class="btn btn-primary">Search</button>
            </div>
        </form>
    </div>
</body>
</html>