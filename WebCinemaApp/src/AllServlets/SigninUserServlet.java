package AllServlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.security.SecureRandom;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.*;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.mindrot.jbcrypt.BCrypt;


@WebServlet("/SigninUserServlet")
public class SigninUserServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
  
    public SigninUserServlet() {
        super();
       
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		response.setContentType("text/html; charset=UTF-8");
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		
		String name = request.getParameter("name");
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		String email = request.getParameter("email");
		String role = request.getParameter("role");
		
		
		
		// Generate salt and hash the password using jBCrypt
		String salt = BCrypt.gensalt();
		String hashedPassword = BCrypt.hashpw(password, salt);
		
	    
		int rowsAffected = 0;
		try {
			rowsAffected = signinUser(name,username,hashedPassword,salt,email,role);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		if(rowsAffected > 0) {
			HttpSession session = request.getSession(true);
	        session.setAttribute("username", username);
	        session.setAttribute("isLoggedIn", true);
	        if(role.equals("Content Admin")) {
	        	RequestDispatcher dispatcher = request.getRequestDispatcher("ContentAdmin.jsp");
			    dispatcher.forward(request, response);
	        }else if(role.equals("Admin")) {
	        	RequestDispatcher dispatcher = request.getRequestDispatcher("Admin.jsp");
			    dispatcher.forward(request, response);
	        }else if(role.equals("Customer")) {
	        	RequestDispatcher dispatcher = request.getRequestDispatcher("Customer.jsp");
			    dispatcher.forward(request, response);
	        }
		}else if(rowsAffected == 0) {
			createDynPage(response,"User "+username+" already exists!");
		}
	}

	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		doGet(request, response);
	}
	
	private int signinUser(String name, String username, String password, String salt, String email, String role) throws ClassNotFoundException {
		
		String command = "SELECT * FROM users WHERE username=? OR email=?";
		String command2 = "INSERT INTO users(Name,Username,Password,Salt,Email,Role) VALUES (?,?,?,?,?,?)";


		boolean findUser = false;
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/mydb", "root","Patata.12");
		    PreparedStatement loginUser = con.prepareStatement(command);
		    loginUser.setString(1, username);
		    loginUser.setString(2, email);
		    ResultSet resultSet = loginUser.executeQuery();
		    
		    if (resultSet.next()) {
	            findUser = true;
	        }

	        resultSet.close();
	        loginUser.close();
		} catch (SQLException | ClassNotFoundException e) {
	        e.printStackTrace(); // Print the stack trace
	    }
		
		int rowsAffected = 0;
		if(!findUser) {
			try {
				Class.forName("com.mysql.cj.jdbc.Driver");
				Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/mydb", "root","Patata.12");
			    PreparedStatement signinUser = con.prepareStatement(command2);
			    signinUser.setString(1, name);
			    signinUser.setString(2, username);
			    signinUser.setString(3, password);
			    signinUser.setString(4, salt);
			    signinUser.setString(5, email);
			    signinUser.setString(6, role);
			    
			    rowsAffected = signinUser.executeUpdate();
		        signinUser.close();
		        
			 } catch (SQLException | ClassNotFoundException e) {
		            e.printStackTrace();
		     }
		}
		
		return rowsAffected;
		
	}
	 
	private void createDynPage(HttpServletResponse response, String message) throws IOException {
		response.setContentType("text/html; charset=UTF-8");
		response.setCharacterEncoding("UTF-8");
		
		PrintWriter out = response.getWriter();
        out.println("<!DOCTYPE html>");
        out.println("<html>");
        out.println("<head>");
        out.println("<title>Login Result</title>");
        out.println("</head>");
        out.println("<body>");
        out.println("<p>"+message+"</p>");
        out.println("</body>");
        out.println("</html>");
	}
	

}
