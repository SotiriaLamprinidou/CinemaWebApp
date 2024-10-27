<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ page import="java.util.*" %>
<%@ page import="java.util.List" %>
<%@ page import="FilmSelection.Films" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
	<title>Available Films</title>
	<link rel="stylesheet" type="text/css" href="./css/cinema.css" />
</head>
<body>
	<div class="topnav">
		  <a href="ContentAdmin.jsp">Home</a>
		  <a class="active" href="/WebCinemaApp/availablefilms">Available Films</a>
		  <a href="InsertFilm.jsp">Insert a new Film</a>
		  <a href="AssignFilm.jsp">Assign Film to cinema</a>
		  <a href="/WebCinemaApp/logout">Logout</a>
		  
	</div>
	<br><br><br>
	<h1><center>Available Films</center></h1>
	<br>
	<div class="films-container">
		<ul>
    	<% 
        List<Films> availablefilms = (List<Films>) session.getAttribute("films");
        if (availablefilms != null && !availablefilms.isEmpty()) {
            for (Films film : availablefilms) {
    	%>
    		<li><%= film.getTitle() %></li>
    		<% 
         	}
        	} else {
    	%>
    		<li>No films available</li>
    	<% 
        	}
    	%>
    	</ul>
	</div>

</body>
</html>