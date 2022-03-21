<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1" import="com.cs336.pkg.*"%>
<%@ page import="java.io.*,java.util.*,java.sql.*"%>
<%@ page import="javax.servlet.http.*,javax.servlet.*" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0-beta3/css/all.min.css">

	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
		<title>Aircrafts</title>
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
				<form action="repsHome.jsp">
					<input type="submit" name="backButton" value="Go Back">
				</form>
				
			</td>
			<td style="padding:0 100px 0 15px;">
				<h1> Aircrafts Management</h1>
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
				PreparedStatement searchStatement = con.prepareStatement("select * from aircrafts where aircraftId = ? ");
				//Create a SQL statement
				Statement stmt = con.createStatement();
				String str,
						aircraftId,
						numberOfSeats,
						owner;
				
				
				int i;
				ResultSet aircrafts,airlines; 
				
				if(request.getParameter("editButton") != null)
				{
					out.print("<h3> Edit an Aircraft:</h3>"
							+ "Edit for Aircraft ID: " + request.getParameter("aircraftId")	 
							+ "<form action='aircrafts.jsp'>"
							 + "<p style='color:brown;'><i>Leave a field empty to keep it unchanged.</i></p>"
							
				  			 + "<input type='hidden'  name='aircraftId' value='" + request.getParameter("aircraftId")   +"'>"
				  			 + "<label for='owner'>New Owner Id:</label>"
				  			 + "<input type='text' name='owner' placeholder='2-Letter ID' pattern='[A-Z]{2}'><br><br>"
				  			 + "<label for='numberOfSeats'>New Number Of Seats:</label>"
				  			 + "<input type='number' name='numberOfSeats' placeholder='1234' pattern='[0-9]'><br><br>"
				  			 + "<input type='submit' name='saveEdit' value='Save'>"
							 + "</form>"
						     );
				
				}
				if(request.getParameter("saveNew") != null)
				{
					//SEARCH IF EXISTS
					aircraftId = request.getParameter("aircraftId");
					searchStatement.setString(1,aircraftId);
					aircrafts = searchStatement.executeQuery();
					// IF NOT EXISTS
					if(!aircrafts.next())
					{
						// Check if airlines exists
						owner = request.getParameter("owner");
						airlines = stmt.executeQuery("select * from airlines where airlineId ='" + owner + "'");
						
						if(airlines.next())
						{
							// INSERT INTO AIRCRAFTS
							ps = con.prepareStatement("insert into aircrafts values(?,?,?)");
							numberOfSeats = request.getParameter("numberOfSeats");
							ps.setString(1, aircraftId);
							ps.setString(2,numberOfSeats);
							ps.setString(3,owner);
							ps.executeUpdate();		
							out.println("<p style='background-color:lightgreen;color:black;'><b>Successfully added an aircraft!</b></p>");	
							
						}
						
						else
						{
							 out.println("<p style='color:black;background-color:red;'><b>Owner Airlines does not exist!</b></p>");
						}
						
					}
					else
					{
						 out.println("<p style='color:black;background-color:red;'><b>Aircraft with that id already exists!</b></p>");
					}
				}
				if(request.getParameter("saveEdit") != null)
				{
					aircraftId = request.getParameter("aircraftId");
					searchStatement.setString(1,aircraftId);
					aircrafts = searchStatement.executeQuery();
					String oldOwner="",oldSeats="";
					while(aircrafts.next())
					{
						oldOwner = aircrafts.getString(3);
						oldSeats = aircrafts.getString(2);
					}
					owner = request.getParameter("owner");
					numberOfSeats = request.getParameter("numberOfSeats");
					
					if(owner != null && !owner.isBlank() && owner.compareToIgnoreCase(oldOwner) != 0)
					{

						// Check if airlines exists
						owner = request.getParameter("owner");
						airlines = stmt.executeQuery("select * from airlines where airlineId ='" + owner + "'");
						
						if(airlines.next())
						{
							str = "update aircrafts set owner = ? where aircraftId = ?";
							ps = con.prepareStatement(str);
							ps.setString(1,owner);
							ps.setString(2,aircraftId);
							ps.executeUpdate();
							out.println("<p style='background-color:lightgreen;color:black;'><b>Successfully edited the owner Airlines!</b></p>");	
							
						}
						
						else
						{
							 out.println("<p style='color:black;background-color:red;'><b>Owner Airlines does not exist!</b></p>");
						}
						
						
					}
					else
					{
						out.println("<p style='color:black;background-color:red;'><b>Owner Airlines was not edited!</b></p>");
					}
					
					
					
					if(numberOfSeats != null && !numberOfSeats.isBlank() && numberOfSeats.compareToIgnoreCase(oldSeats) != 0)
					{
						str = "update aircrafts set numberOfSeats = ? where aircraftId = ?";
						ps = con.prepareStatement(str);
						ps.setString(1,numberOfSeats);
						ps.setString(2,aircraftId);
						ps.executeUpdate();
						out.println("<p style='background-color:lightgreen;color:black;'><b>Successfully edited the Number Of Seats!</b></p>");
					}
					else
					{
						out.println("<p style='color:black;background-color:red;'><b> Number Of Seats was not edited!</b></p>");
					}

				}
				
				if(request.getParameter("deleteButton") != null)
				{
					
					aircraftId = request.getParameter("aircraftId");
					
					String del = "delete from aircrafts where aircraftId=" + "?"; 
					ps = con.prepareStatement(del);
					ps.setString(1, aircraftId);
					ps.executeUpdate();
					out.println("<p style='background-color:lightgreen;color:black;'><b>Successfully deleted the aircraft!</b></p>");	
					
				}
				
				out.println(" <h3><FONT COLOR=blue> Current List of Aircrafts: </font></h3>");
				
				// If no activity, present add button
				if(request.getParameter("editButton") == null && request.getParameter("addButton") == null)
				{
					out.print("<form action='aircrafts.jsp'> <button type='submit' name ='addButton' class='btn'><i class='fa-solid fa-square-plus'>Add</i></button></form><br>");
				}
				// If add button pressed, give new aircraft form
				if(request.getParameter("addButton") != null)
				{
						out.print("<h3> Add an Aircraft:</h3>"
								 + "<form action='aircrafts.jsp'>"
								 + "<label for='id'>Aircraft ID:</label>"
					  			 + "<input type='text' name='aircraftId' placeholder='Airlines12' required><br><br>"
					  			 + "<label for='owner'>Owner Airlines:</label>"
					  			 + "<input type='text' name='owner' placeholder='XX' pattern='[A-Z]{2}' required><br><br>"
					  			 + "<label for='numberOfSeats'>Total Number Of Seats:</label>"
							  	 + "<input type='number' name='numberOfSeats' placeholder='123' required><br><br>"
							  	
					  			+ "<input type='submit' value='Save' name='saveNew'>"
								+ "</form><br>"
							     );
					
					
				}
				// Display Up to Date List of Aircrafts
				str = "select * from aircrafts";
				aircrafts = stmt.executeQuery(str);
				
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
				out.print("<b>ID<b>");
				out.print("</td>");
				//make a column
				out.print("<td style='padding:0 150px 0 15px;'>");
				out.print("<b>Owner Airlines<b>");
				out.print("</td>");
				//make a column
				out.print("<td style='padding:0 150px 0 15px;'>");
				out.print("<b>Number Of Seats<b>");
				out.print("</td>");
				
				
				out.print("<td style='padding:0 10px 0 15px;'>");
				out.print("<b>Delete<b>");
				out.print("</td>");
				
				out.print("</tr>");

				
				while (aircrafts.next()) {
					
					//display a row
					out.print("<tr>");
					out.print("<td>");
					out.print("<form action='aircrafts.jsp'>"
							+ "<input type='hidden' name='aircraftId' value='" 
							+ aircrafts.getString("aircraftId") 
							+ "'>"  
							+ "<button type='submit' name ='editButton' class='btn'><i class='fa-solid fa-pen-to-square'></i></button>"
							+ "</form>");
					//out.print("<button onclick='delete(this);' class='btn'><i class='fa fa-trash'></i></button>");
					//out.print()
					out.print("</td>");
					//display a column
					out.print("<td style='padding:0 30px 0 15px;'>");
					out.print(i);
					out.print("</td>");
					//display a column
					out.print("<td>");
					out.print(aircrafts.getString("aircraftId"));
					out.print("</td>");
					//display a column
					out.print("<td>");
					out.print(aircrafts.getString("owner"));
					out.print("</td>");
					//display a column
					out.print("<td>");
					out.print(aircrafts.getString("numberOfSeats"));
					out.print("</td>");
					
					
					out.print("<td>");
					out.print("<form action='aircrafts.jsp'>"
							+ "<input type='hidden' name='aircraftId' value='" 
							+ aircrafts.getString("aircraftId") 
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