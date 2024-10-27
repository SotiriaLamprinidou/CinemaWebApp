<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
	<title>Insert a new Film</title>
	<link rel="stylesheet" type="text/css" href="./css/cinema.css" />
</head>
<body>
	<div class="topnav">
		  <a href="ContentAdmin.jsp">Home</a>
		  <a href="/WebCinemaApp/availablefilms">Available Films</a>
		  <a class="active" href="InsertFilm.jsp">Insert a new Film</a>
		  <a href="AssignFilm.jsp">Assign Film to Cinema</a>
		  <a href="/WebCinemaApp/logout">Logout</a>
	</div>
	
	<div class="login-form">
        <form action = "InsertFilmServlet" method = "post">
            <h2>Insert Film</h2>
            <div class="form-group">
                <input type="text" class="form-control" placeholder=" Film Title" required="required" name = "filmTitle">
            </div>
            <div class="form-group">
                <input type="text" class="form-control" placeholder=" Film Category" required="required" name = "filmCategory">
            </div>
            <div class="form-group">
                <input type="text" class="form-control" placeholder=" Film Description" required="required" name = "filmDescription">
            </div>
            <div class="form-group">
                <button type="submit" class="btn btn-primary">Insert</button>
            </div>
        </form>
    </div>
</body>
</html>