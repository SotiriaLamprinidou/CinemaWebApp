<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<title>Add Content Admin</title>
 	<link rel="stylesheet" type="text/css" href="./css/cinema.css" />
</head>
<body>
	<div class="topnav">
		  <a href="Admin.jsp">Home</a>
		  <a class="active" href="AddContentAdmin.jsp">Add Content Admin</a>
		  <a href="DeleteContentAdmin.jsp">Delete Content Admin</a>
		  <a href="/WebCinemaApp/logout">Logout</a>
	</div>
	
	<div class="login-form">
        <form action = "AdminServlet" method = "post">
            <h2>Add Content Admin</h2>
            <p><center>Enter the username of an already existing user to became Content Admin!</center></p>
            <div class="form-group">
                <input type="text" class="form-control" placeholder=" Username" required="required" name = "Username">
            </div>
            <div class="form-group">
                <button type="submit" class="btn btn-primary">Add</button>
            </div>
        </form>
    </div>
</body>
</html>