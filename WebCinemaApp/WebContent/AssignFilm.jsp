<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
	<title>Assign Film to Cinema</title>
 	<link rel="stylesheet" type="text/css" href="./css/cinema.css" />
</head>
<body>
	<div class="topnav">
		  <a href="ContentAdmin.jsp">Home</a>
		  <a href="/WebCinemaApp/availablefilms">Available Films</a>
		  <a href="InsertFilm.jsp">Insert a new Film</a>
		  <a class="active" href="AssignFilm.jsp">Assign Film to Cinema</a>
		  <a href="/WebCinemaApp/logout">Logout</a>
	</div>
	
	<div class="login-form">
        <form action = "AssignFilmServlet" method = "post">
            <h2>Assign Film to Cinema</h2>
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
                <button type="submit" class="btn btn-primary">Assign</button>
            </div>
        </form>
    </div>
</body>
</html>