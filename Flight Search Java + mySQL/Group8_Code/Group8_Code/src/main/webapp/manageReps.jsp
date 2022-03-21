<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1" import="com.cs336.pkg.*"%>
<%@ page import="java.io.*,java.util.*,java.sql.*"%>
<%@ page import="javax.servlet.http.*,javax.servlet.*" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0-beta3/css/all.min.css">

	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
		<title>Representatives</title>
		<style>
      	 table,
     	 th,
     	 td {
       	 padding: 10px;
      	 border: 1px solid black;
       	 border-collapse: collapse;
      }
    </style>
	</head>
	
	<body>
	<table>
		<tr>
			<td style="padding:0 100px 0 15px;">
				<form action="adminHome.jsp">
					<input type="submit" name="backButton" value="Go Back">
				</form>
				
			</td>
			<td style="padding:0 100px 0 15px;">
				<h1> Reps Management</h1>
			</td>
			<td>
				<form action="login.jsp">
					<input type="submit" value="LOGOUT">
				</form>
				
			</td>
		</tr>
	</table>	
				
	<%
	try {
				//Get the database connection
				ApplicationDB db = new ApplicationDB();	
				Connection con = db.getConnection();

				PreparedStatement ps;
				PreparedStatement searchStatement = con.prepareStatement("select * from users where id = ?");
				//Create a SQL statement
				Statement stmt = con.createStatement();
				String str,
						id,name,password;
				
				
				int i;
				ResultSet Representatives; 
				
				// Saving edits for a user
				if(request.getParameter("saveEdit") != null)
				{
					id = request.getParameter("id");
					PreparedStatement editSearch = con.prepareStatement("select id,name,password from users join reps using (id) where id=?");
					editSearch.setString(1,id);
					Representatives = editSearch.executeQuery();
					String oldName="",oldPassword="";
					while(Representatives.next())
					{
						oldName = Representatives.getString(2);
						oldPassword = Representatives.getString(3);
					}
					String newName, newPassword;
					if(request.getParameter("name") != null){
						newName= request.getParameter("name");
						if(!newName.isBlank() && newName.compareToIgnoreCase(oldName) != 0)
						{
							ps = con.prepareStatement("update reps set name = ? where id = ?");
							ps.setString(1,newName);
							ps.setString(2,id);
							ps.executeUpdate();
							out.println("<p style='background-color:lightgreen;color:black;'><b>Successfully edited Name!</b></p>");	
						}
						else
						{
							 out.println("<p style='color:black;background-color:red;'><b>Name not edited!</b></p>");
						}
					}
					if(request.getParameter("password") !=null){
						newPassword = request.getParameter("password");
						if(!newPassword.isBlank() && newPassword.compareToIgnoreCase(oldPassword) != 0)
						{
							ps = con.prepareStatement("update users set password = ? where id = ?");
							ps.setString(1,newPassword);
							ps.setString(2,id);
							ps.executeUpdate();
							out.println("<p style='background-color:lightgreen;color:black;'><b>Successfully edited Password!</b></p>");	
						}
						else
						{
							 out.println("<p style='color:black;background-color:red;'><b>Password not edited!</b></p>");
						}
					}
					
					
					
					
		
					
					
					
				}
				
				
				// Saving new User after details are typed in
				if(request.getParameter("saveNew") != null)
				{
					//SEARCH IF EXISTS
					id = request.getParameter("id");
					searchStatement.setString(1,id);
					Representatives = searchStatement.executeQuery();
					// IF THE ENTERED USERNAME DOES NOT EXISTS
					if(!Representatives.next())
					{
						password = request.getParameter("password");
						// INSERT INTO users
						ps = con.prepareStatement("insert into users values(?,?,?)");
						ps.setString(1,id);
						ps.setString(2,"reps");
						ps.setString(3,password);
						ps.executeUpdate();
						// INSERT INTO Representatives
						ps = con.prepareStatement("insert into reps values(?,?)");
					
						name = request.getParameter("name");
						ps.setString(1, id);
						ps.setString(2,name);
						
						ps.executeUpdate();
						out.println("<p style='background-color:green;color:black;'>Success!</p>");	
						
					}
					else
					{
						 out.println("<p style='color:black;background-color:red;'><b>User with that id already exists!</b></p>");
					}
				}
				
				// Deleting a User on DELETE BUTTON CLICK
				if(request.getParameter("deleteButton") != null)
				{
					
					id = request.getParameter("id");
					
					String del = "delete from users where id=" + "?"; 
					ps = con.prepareStatement(del);
					ps.setString(1, id);
					ps.executeUpdate();
					out.println("<p style='background-color:lightgreen;color:black;'><b>Success!</b></p>");	
					
				}
				
				out.println(" <h3><FONT COLOR=blue> Current List of Representatives: </font></h3>");
				
				// IF NO BUTTONS HAVE BEEN PRESSED, SHOW ADD BUTTON
				if(request.getParameter("editButton") == null && request.getParameter("addButton") == null)
				{
					out.print("<form action='manageReps.jsp'> <button type='submit' name ='addButton' class='btn'><i class='fa-solid fa-square-plus'>Add</i></button></form><br>");
				}
				
				// IF EDIT BUTTON WAS PRESSED
				if(request.getParameter("editButton") != null)
				{
					out.print("<h3> Edit a Representative:</h3>"
							+ "<p> Edit for Id: " + request.getParameter("id") +"</p>"
							 + "<form action='manageReps.jsp'>"
							 + "<p style='color:brown;'><i>Leave a field empty to keep it unchanged.</i></p>"
									 + "<input type='hidden' name='id' value='"+request.getParameter("id")+"'>"
						
				  			 + "<label for='name'>New Name:</label>"
				  			 + "<input type='text' id='name' name='name' placeholder='Jane Doe' ><br><br>"
				  			 + "<label for='password'>New Password:</label>"
				  			 + "<input type='text' name='password' placeholder='8 characters' pattern='[A-Za-z0-9]{8}'><br><br>"
				  			 + "<input type='submit'name='saveEdit' value='Save'><br><br>"
							 + "</form>"
						     );
				
				}
				// IF ADD BUTTON WAS PRESSED
				if(request.getParameter("addButton") != null)
				{
						out.print("<h3> Add a Representative:</h3>"
								 + "<form action='manageReps.jsp'>"
								 + "<label for='id'> ID:</label>"
					  			 + "<input type='text' name='id' placeholder='abc@abc.com' required><br><br>"
					  			 + "<label for='owner'>Name:</label>"
					  			 + "<input type='text' name='name' placeholder='Jane Doe' required><br><br>"
					  			 + "<label for='password'>Password:</label>"
							  	 + "<input type='text' name='password' placeholder='8 characters' pattern='[A-Za-z0-9]{8}' required><br><br>"
							  	
					  			+ "<input type='submit' value='Save' name='saveNew'>"
								+ "</form><br>"
							     );
					
					
				}
				
				// DISPLAY CURRENT DATA
				str = "select * from reps join users using(id)";
				Representatives = stmt.executeQuery(str);
				
				i = 1;
				//Make an HTML table to show the results in:
				out.print("<table>");

				//make a row
				out.print("<tr>");
				out.print("<td style='padding:0 10px 0 15px;'>");
				out.print("<b>Edit<b>");
				out.print("</td>");
				//make a column
				out.print("<td style='padding:0 10px 0 15px;'>");
				out.print("<b>S.N.<b>");
				out.print("</td>");
				//make a column
				out.print("<td style='padding:0 30px 0 15px;'>");
				out.print("<b>Email/Username<b>");
				out.print("</td>");
				//make a column
				out.print("<td style='padding:0 150px 0 15px;'>");
				out.print("<b>Name <b>");
				out.print("</td>");
				//make a column
				out.print("<td style='padding:0 150px 0 15px;'>");
				out.print("<b>Password<b>");
				out.print("</td>");
				
				
				out.print("<td style='padding:0 10px 0 15px;'>");
				out.print("<b>Delete<b>");
				out.print("</td>");
				
				out.print("</tr>");

			//	List<String> customerIds = new ArrayList<String>();
				//parse out the results
				while (Representatives.next()) {
					//i++;
					//display a row
					out.print("<tr>");
					out.print("<td>");
					out.print("<form action='manageReps.jsp' >"
							+ "<input type='hidden' name='id' value='" 
							+ Representatives.getString("id") 
							+ "'>"  
							+ "<button type='submit' name ='editButton' class='btn'><i class='fa-solid fa-pen-to-square'></i></button>"
							+ "</form>");
					out.print("</td>");
					//display a column
					out.print("<td style='padding:0 30px 0 15px;'>");
					out.print(i);
					out.print("</td>");
					//display a column
					out.print("<td>");
					out.print(Representatives.getString("id"));
					out.print("</td>");
					//display a column
					out.print("<td>");
					out.print(Representatives.getString("name"));
					out.print("</td>");
					//display a column
					out.print("<td>");
					out.print(Representatives.getString("password"));
					out.print("</td>");
					
					
					out.print("<td>");
					out.print("<form action='manageReps.jsp'>"
							+ "<input type='hidden' name='id' value='" 
							+ Representatives.getString("id") 
							+ "'>"  
							+ "<button type='submit' name ='deleteButton' class='btn'><i class='fa-solid fa-trash-can'></i></button>"
							+ "</form>");
					
					out.print("</td>");
					out.print("</tr>");
					i++;

				}
				
				out.print("</table>");
				
	
				
				//close the connection.
				con.close();
				
			} catch (Exception ex) {
				out.print(ex);
				out.print("insert failed");
			}
						
		%>
		
	
	
	</body>

</html>