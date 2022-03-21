<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1" import="com.cs336.pkg.*"%>
<%@ page import="java.io.*,java.util.*,java.sql.*"%>
<%@ page import="javax.servlet.http.*,javax.servlet.*" %>


<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
		<title>Login</title>
		<link rel="stylesheet" type="text/css" href="login.css" />
	</head>
<body>
<%
		
		// Activity Started by LOGIN or REGISTER
		if(request.getParameter("username") != null || request.getParameter("regButton") != null)
		{
			try {

				//Get the database connection
				ApplicationDB db = new ApplicationDB();	
				Connection con = db.getConnection();

				PreparedStatement ps;
				//Create a SQL statement
				Statement stmt = con.createStatement();
				String str,
						name,username,password,email,identity;
				ResultSet result;
				
				//Get parameters from the HTML form at login.jsp
				password = request.getParameter("password");
				
				// Register a User after Registration form is filled
				if(request.getParameter("regButton") != null)
				{
					email = request.getParameter("email");
					name = request.getParameter("name");
					
					str = "select * from users where id = ?";
					ps = con.prepareStatement(str);
					ps.setString(1,email);
					result = ps.executeQuery();
					if(!result.next())
					{
						str = "insert into users values (?,?,?)";
						ps = con.prepareStatement(str);
						ps.setString(1,email);
						// ONLY CUSTOMERS ARE ALLOWED TO REGISTER FROM HERE
						ps.setString(2,"customers");
						ps.setString(3,password);
						ps.executeUpdate();
						
						// ONCE USER TABLE IS FILLED MOVE ON TO FILL CUSTOMERS TABLE
						str = "insert into customers values(?,?)";
						ps = con.prepareStatement(str);
						ps.setString(1, email);
						ps.setString(2, name);
						ps.executeUpdate();
						
						out.println("<p style='background-color:lightgreen;color:black;'><b>Success! You can Login now!<b></p>");	
					}
					// IF there is existing records for that username, display ERROR
					else
					{
						 out.println("<p style='color:black;background-color:red;'><b>E-mail already exists!</b></p>");
					}
				}
				
				// If user was trying to log in, validate and redirect
				else
				{
					
					username = request.getParameter("username");
					str = "select * from users where id ='" + username + "' " + "and password='" + password + "'";
					result = stmt.executeQuery(str);
					if(result.next())
					{
						Cookies cookies = new Cookies(username);
						RequestDispatcher rd = request.getRequestDispatcher(result.getString(2).toLowerCase()+"Home.jsp");
						rd.forward(request, response);
					}
					else
					{
						out.println("<p style='color:black;background-color:red;'>Invalid Username or Password!</p>");
					}
				}
			
				con.close();
		
			} catch (Exception ex) {
				out.print(ex);
				out.print("insert failed");
			}
			
		}

		// Provide Registration form after REGISTER is clicked
		if(request.getParameter("register") != null)
		{
			out.print("<form action='login.jsp'><div class='registrationForm'>"
					+ "<div class='elems'><label for='name'>Name:</label><input type='text' name='name' placeholder='Jane Doe' required></div>"
					+ "<div class='elems'><label for='email'>E-mail:</label><input type='text' name='email' placeholder='jane@rutgers.edu' required></div>"
					+ "<div class='elems'><label for='password'>Password:</label><input type='text' name='password' placeholder='8-Digits' required></div>"
					+ "<div class='elems'><input type='submit' name='regButton' value='Register Me!'></div>"
					+ "</div></form>"
					);
			
		}
		
		// Provide Login Form if the activity was started off as default
		if(request.getParameter("register") == null )
		{
			out.print("<div class='container'><section id='content'>"
					   + "<form action='login.jsp'>"
					   + "<h1>Login</h1>"
					   + "<div><input type='text' placeholder='Username' name='username' id='username' required/></div>"
					   + "<div><input type='password' placeholder='Password' name='password' id='password' required/></div>"
					   + "<div><input type='submit' value='Log in' /><a href='#'>Lost your password?</a><a href='login.jsp?register=true'>Register</a></div>"
					   + "</form></section></div>");
		}
		

	%>

</body>
</html>