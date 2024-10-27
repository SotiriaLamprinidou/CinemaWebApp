package AllServlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.SQLException;

/**
 * Servlet implementation class AdminServlet
 */
@WebServlet("/AdminServlet")
public class AdminServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
  
    public AdminServlet() {
        super();
    }

	
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html; charset=UTF-8");
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");

        String username = request.getParameter("Username");
        boolean userExists = false;
        boolean adminExists = false;

        String referer = request.getHeader("referer");
        if (referer != null) {
            String jspFileName = extractJspFileName(referer);

            if ("AddContentAdmin".equals(jspFileName)) {
                try {
                    adminExists = checkAdmin(username);
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                } catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
                if(!adminExists) {
                	try {
                        userExists = checkUser(username);
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }

                    try {
                        if (!userExists) {
                        	alertBox(response, "User not found!");
                        } else if (userExists) {
                            int rowsAffected = 0;
                            try {
                                rowsAffected = addCA(username);
                            } catch (ClassNotFoundException e) {
                                e.printStackTrace();
                            }
                            if (rowsAffected > 0) {
                                alertBox(response, "Content Admin Added!");
                            } else if (rowsAffected == 0) {
                                alertBox(response, "A problem occurred! Please try again!");
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }else {
                	 alertBox(response, "Admin " + username + " already exist. Enter another one!");
                }
            } else if ("DeleteContentAdmin".equals(jspFileName)) {
                try {
                    adminExists = checkAdmin(username);
                } catch (ClassNotFoundException | SQLException e) {
                    e.printStackTrace();
                }

                try {
                    if (!adminExists) {
                    	alertBox(response, "Content Admin not found!");
                    } else if (adminExists) {
                        int rowsAffected = 0;
                        try {
                        	rowsAffected = deleteCA(username);
                        } catch (ClassNotFoundException | SQLException e) {
                        	e.printStackTrace();
                        }

                        if (rowsAffected > 0) {
                            alertBox(response, "Content Admin Deleted!");
                        } else if (rowsAffected == 0) {
                            alertBox(response, "A problem occurred! Please try again!");
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		doGet(request, response);
	}

	
	private String extractJspFileName(String referer) {
	    int lastSlashIndex = referer.lastIndexOf("/");
	    int lastDotIndex = referer.lastIndexOf(".");
	    
	    if (lastSlashIndex >= 0 && lastDotIndex > lastSlashIndex) {
	        return referer.substring(lastSlashIndex + 1, lastDotIndex);
	    }
	    return null;
	}
	
	private boolean checkUser(String username) throws ClassNotFoundException {
	    String command = "SELECT * FROM users WHERE username=?";
	    boolean findUser = false;
	    
	    try {
	        Class.forName("com.mysql.cj.jdbc.Driver");
	        Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/mydb", "root", "Patata.12");
	        PreparedStatement loginUser = con.prepareStatement(command);
	        loginUser.setString(1, username);
	        ResultSet resultSet = loginUser.executeQuery();

	        if (resultSet.next()) {
	            findUser = true;
	        }

	        resultSet.close();
	        loginUser.close();
	        con.close();
	    } catch (SQLException sqle) {
	        sqle.printStackTrace();
	    }
	    return findUser;
	}

	private boolean checkAdmin(String username) throws ClassNotFoundException, SQLException {
	    String command = "SELECT * FROM contentadmins WHERE Users_Username=?";
	    boolean findAdmin = false;

	    try (Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/mydb", "root", "Patata.12");
	         PreparedStatement loginUser = con.prepareStatement(command)) {

	        loginUser.setString(1, username);
	        ResultSet resultSet = loginUser.executeQuery();

	        if (resultSet.next()) {
	            findAdmin = true;
	        }

	        resultSet.close();
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }

	    return findAdmin;
	}

	
	private int addCA(String Username) throws ClassNotFoundException {
	    int rowsAffected = 0;

	    try {
	        Class.forName("com.mysql.cj.jdbc.Driver");
	        Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/mydb", "root", "Patata.12");
	        // Check if user exists
	        String selectCommand = "UPDATE users SET Role = 'Content Admin' WHERE Username = ?";
	        PreparedStatement updateToContentAdmin = con.prepareStatement(selectCommand);
	        updateToContentAdmin.setString(1, Username);
	        rowsAffected = updateToContentAdmin.executeUpdate();

	        updateToContentAdmin.close();
	        con.close();
	    } catch (ClassNotFoundException e) {
	        e.printStackTrace();
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }

	    return rowsAffected;
	}


	private int deleteCA(String Username) throws ClassNotFoundException, SQLException {
	    int rowsAffected = 0;

	    try {
	        Class.forName("com.mysql.cj.jdbc.Driver");
	        Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/mydb", "root", "Patata.12");
	        // Check if user exists
	        String selectCommand = "DELETE FROM contentadmins WHERE Users_Username = ?";
	        PreparedStatement deleteContentAdmin = con.prepareStatement(selectCommand);
	        deleteContentAdmin.setString(1, Username);
	        rowsAffected = deleteContentAdmin.executeUpdate();

	        deleteContentAdmin.close();
	        con.close();
	    } catch (ClassNotFoundException e) {
	        e.printStackTrace();
	        throw e;
	    } catch (SQLException e) {
	        e.printStackTrace();
	        throw e;
	    }

	    return rowsAffected;
	}

	private void alertBox(HttpServletResponse response,String message) throws IOException {
		response.setContentType("text/html; charset=UTF-8");
		response.setCharacterEncoding("UTF-8");
		
		PrintWriter out = response.getWriter();
		out.println("<script type=\"text/javascript\">");
		out.println("alert('" + message + "');");
        out.println("window.location.href='Admin.jsp';");
        out.println("</script>");
        
	}

}
