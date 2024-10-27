package AllServlets;

import java.io.IOException;

import java.io.PrintWriter;
import java.security.SecureRandom;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

import javax.naming.InitialContext;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;

import org.mindrot.jbcrypt.BCrypt;

import java.util.HashMap;


@WebServlet("/LoginUserServlet")
public class LoginUserServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;


	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	response.setContentType("text/html; charset=UTF-8");
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		
		Map<String, Object> loginResult;
		boolean answer = false;
		String role = null;
		try {
			loginResult = findUser(username, password);
			answer = (boolean) loginResult.get("findUser");
			role = (String) loginResult.get("role");
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if(answer == true) {
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
			
		}else {
			alertBox(response, "User "+ username + " not found! Please try again!");
		}
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Handle POST requests if needed
        doGet(request, response);
    }
    
    


    private Map<String, Object> findUser(String username, String password) throws ClassNotFoundException {
    	 Map<String, Object> result = new HashMap<>();
         boolean findUser = false;
         String role = "";
         String command = "SELECT * FROM users WHERE Username = ?";
        
         try {
             Class.forName("com.mysql.cj.jdbc.Driver");
             Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/mydb", "root","Patata.12");
             PreparedStatement loginUser = con.prepareStatement(command);
             loginUser.setString(1, username);
             ResultSet resultSet = loginUser.executeQuery();
             
             	if (resultSet.next()) {
            	    String storedPassword = resultSet.getString("Password");
            	    String salt = resultSet.getString("Salt");
            	    
            	    // Append the salt to the entered password
            	    String passwordWithSalt = BCrypt.hashpw(password, salt);
            	    
            	    boolean passwordMatch = passwordWithSalt.equals(storedPassword);

            	    if (passwordMatch) {
            	        findUser = true;
            	        role = resultSet.getString("Role");
            	    }
            	}
             resultSet.close();
             loginUser.close();
         } catch (SQLException | ClassNotFoundException sqle) {
             sqle.printStackTrace();
         }
         
         result.put("findUser", findUser);
         result.put("role", role);
         return result;
    }
    
    private void alertBox(HttpServletResponse response,String message) throws IOException {
		response.setContentType("text/html; charset=UTF-8");
		response.setCharacterEncoding("UTF-8");
		
		PrintWriter out = response.getWriter();
		out.println("<script type=\"text/javascript\">");
		out.println("alert('" + message + "');");
        out.println("window.location.href='login.html';"); // Redirect to an error page
        out.println("</script>");
        
	}
}
