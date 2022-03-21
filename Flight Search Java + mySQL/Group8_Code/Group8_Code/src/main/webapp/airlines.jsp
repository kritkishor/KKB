<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1" import="com.cs336.pkg.*"%>
<%@ page import="java.io.*,java.util.*,java.sql.*"%>
<%@ page import="javax.servlet.http.*,javax.servlet.*" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0-beta3/css/all.min.css">

	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
		<title>Airlines</title>
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
				<h1> Airlines Management</h1>
			</td>
			<td>
				<form action="login.jsp">
					<input type="submit" value="LOGOUT">
				</form>
				
			</td>
		</tr>
	</table>	
				<form action = "airlines.jsp">
				
				<br>
				<br>
				<button type="submit" name="airportAndAirlines">
						Manage Operation Airports
				</button>
				
				</form>
				<br>
				<br>
				
	<%
	try {
				//Get the database connection
				ApplicationDB db = new ApplicationDB();	
				Connection con = db.getConnection();

				PreparedStatement ps;
				PreparedStatement searchStatement = con.prepareStatement("select * from airlines where airlineId = ? ");
				//Create a SQL statement
				Statement stmt = con.createStatement();
				String str,
						airlineId,name,phone;
			
				int i;
				
				ResultSet operations,airports,airlines; 
				String airport,airline;
				
				if(request.getParameter("editButton") != null)
				{
					out.print("<h3> Edit an Airlines:</h3>"
							 + "<form action='airlines.jsp'>"
							 + "<p style='color:brown;'><i>Leave a field empty to keep it unchanged.</i></p>"
							 + "Edit for Airline ID: " + request.getParameter("airlineId")
				  			 + "<br><br><input type='hidden' name='airlineId' value='"+ request.getParameter("airlineId")+"'>"
				  			 + "<label for='name'>New Name:</label>"
				  			 + "<input type='text'  name='name' placeholder='ABCDEF' ><br><br>"
				  			 + "<label for='phone'>New Phone number:</label>"
				  			 + "<input type='tel' name='phone' placeholder='1-234-567-7890' pattern='[0-9]{1}-[0-9]{3}-[0-9]{3}-[0-9]{4}' ><br><br>"
				  			 + "<input type='submit' name='editSave' value='Save'>"
							 + "</form>"
						     );
				
				}
				
				//IF OPERATING CONDITIONS ARE TO BE CHANGED
				if(request.getParameter("addOperation") != null || request.getParameter("deleteOperation") != null)
				{
						
					airport = request.getParameter("airport");
					airline = request.getParameter("airlines");
					
					operations = (con.createStatement()).executeQuery("select * from operationAirports where airlineId ='" + airline + "'and airportId='" + airport +"'");
					if(request.getParameter("deleteOperation") != null)
					{
						if(operations.next())
						{
							ps = con.prepareStatement("delete from operationAirports where airportId = ? and airlineId = ?");
							ps.setString(1,airport);
							ps.setString(2,airline);
							ps.executeUpdate();
							out.println("<p style='background-color:lightgreen;color:black;'><b>Successfully deleted the operating Airlines/Airport!</b></p>");	
						}
						else
						{
							 out.println("<p style='color:black;background-color:red;'><b>Operation Combination does not exist!</b></p>");

						}
						
					}
					if(request.getParameter("addOperation") != null)
					{
						if(!operations.next())
						{
							ps = con.prepareStatement("insert into operationAirports values(?,?)");
							ps.setString(1,airline);
							ps.setString(2,airport);
							ps.executeUpdate();
							out.println("<div class='dashboard-items'><div class='rows'><p style='background-color:lightgreen;color:black;'><b>Successfully added operating Airlines/Airport!</b></p></div></div>");	
						}
						else
						{
							 out.println("<p style='color:black;background-color:red;'><b>Operation Combination already exists!</b></p>");

						}
					}
					
				}
				// POPULATING OPERATION SELECTION BOX
				if(request.getParameter("airportAndAirlines")!=null)
				{
					airlines = (con.createStatement()).executeQuery("select airlineId from airlines");
					airports = (con.createStatement()).executeQuery("select airportId from airports");
					out.print("Select AIRLINES and AIRPORT to ADD/DELETE an operating Airlines for that airport:");
					out.print("<form action='airlines.jsp'>");
					out.print("<table>");
					out.print("<tr>");
					out.print("<td>");
					out.print("Airlines");
					out.print("</td>");
					out.print("<td>");
					out.print("Airport");
					out.print("</td>");
					out.print("</tr>");
					out.print("<tr>");
					out.print("<td>");
					out.print("<select name='airlines'>");
					while(airlines.next())
					{
						out.print("<option value='" + airlines.getString("airlineId") + "'>" + airlines.getString("airlineId") +"</option>");
					}
					out.print("</select>");
					out.print("</td>");
					out.print("<td>");
					out.print("<select name='airport'>");
					while(airports.next())
					{
						out.print("<option value='" + airports.getString("airportId") + "'>" + airports.getString("airportId") +"</option>");
					}
					out.print("</select>");
					out.print("</td>");
					out.print("</tr>");
					
					out.print("</table>");
					out.print("<br>");
					out.print("<input type='submit' name='addOperation' value='Add'>");
					out.print("\t\t "+" \t\t");
					out.print("<input type='submit' name='deleteOperation' value='Delete'>");
					out.print("</form>");
					
					out.print("<br>");
				}
				if(request.getParameter("addButton") != null)
				{
					   
						out.print("<h3> Add an Airlines:</h3>"
								 + "<form action='airlines.jsp'>"
								 + "<label for='airlineId'>Airline ID:</label>"
					  			 + "<input type='text' name='airlineId' placeholder='2-Letter ID' pattern='[A-Z]{2}' required><br><br>"
					  			 + "<label for='name'>Name:</label>"
					  			 + "<input type='text' name='name' placeholder='ABC Airlines' required><br><br>"
					  			 + "<label for='phone'>Phone number:</label>"
					  			 + "<input type='text' name='phone' placeholder='1-234-567-7890' required><br><br>"
					  			 + "<input type='submit' name='saveNew' value='Save'>"
								 + "</form>"
							     );
					
					
				}
				if(request.getParameter("saveNew") != null)
				{
					airlineId = request.getParameter("airlineId");
					searchStatement.setString(1, airlineId);
					airlines = searchStatement.executeQuery();
					if(!airlines.next())
					{
						ps = con.prepareStatement("insert into airlines value(?,?,?)");
						ps.setString(1,airlineId);
						ps.setString(2, request.getParameter("name"));
						ps.setString(3,request.getParameter("phone"));
						ps.executeUpdate();
						out.println("<p style='background-color:lightgreen;color:black;'><b>Successfully added the airlines!</b></p>");	
					}
					else
					{
						 out.println("<p style='color:black;background-color:red;'><b>Airlines with that id already exists!</b></p>");
					}
					
					
				}
				
				if(request.getParameter("editSave") != null)
				{
					airlineId = request.getParameter("airlineId");
					name = request.getParameter("name");
					phone = request.getParameter("phone");
					searchStatement.setString(1,airlineId);
					airlines = searchStatement.executeQuery();
					String oldName = "", oldPhone="";
					while(airlines.next())
					{
						oldName = airlines.getString(2);
						oldPhone = airlines.getString(3);
					}
					
					if(name != null && !name.isBlank() && name.compareToIgnoreCase(oldName) != 0)
					{
						str = "update airlines set name = ? where airlineId = ?";
						ps = con.prepareStatement(str);
						ps.setString(1,name);
						ps.setString(2,airlineId);
						ps.executeUpdate();
						out.println("<p style='background-color:lightgreen;color:black;'><b>Successfully changed Name for the airlines!</b></p>");	
					}
					else
					{
						 out.println("<p style='color:black;background-color:red;'><b>Name was not edited!</b></p>");
					}
					
					if(phone != null && !phone.isBlank() && phone.compareToIgnoreCase(oldPhone) != 0)
					{
						str = "update airlines set phone = ? where airlineId = ?";
						ps = con.prepareStatement(str);
						ps.setString(1,phone);
						ps.setString(2,airlineId);
						ps.executeUpdate();
						out.println("<p style='background-color:lightgreen;color:black;'><b>Successfully changed Phone for the airlines!</b></p>");	
					}
					else
					{
						 out.println("<p style='color:black;background-color:red;'><b>Phone was not edited!</b></p>");
					}
				}
				if(request.getParameter("deleteButton") != null)
				{
					
					airlineId = request.getParameter("airlineId");
					String del = "delete from airlines where airlineId=" + "?"; 
					ps = con.prepareStatement(del);
					ps.setString(1, airlineId);
					ps.executeUpdate();
					out.println("<p style='background-color:green;color:black;'><b>Successfully deleted the airlines!</b></p>");	
					
				}
				
				out.println(" <h3><FONT COLOR=blue> Current List of Airlines: </font></h3>");
				if(request.getParameter("editButton") == null && request.getParameter("addButton") == null)
				{
					out.print("<form action='airlines.jsp'> <button type='submit' name ='addButton' class='btn'><i class='fa-solid fa-square-plus'>Add</i></button></form><br>");
				}
				str = "select * from airlines";
				airlines = stmt.executeQuery(str);
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
				out.print("<b>Airline ID<b>");
				out.print("</td>");
				//make a column
				out.print("<td style='padding:0 150px 0 15px;'>");
				out.print("<b>Name<b>");
				out.print("</td>");
				
				
				//make a column
				out.print("<td style='padding:0 100px 0 15px;'>");
				out.print("<b>Phone<b>");
				out.print("</td>");
				
				//make a column
				out.print("<td style='padding:0 100px 0 15px;'>");
				out.print("<b>Operating Airports<b>");
				out.print("</td>");
				
				
				out.print("<td style='padding:0 10px 0 15px;'>");
				out.print("<b>Delete<b>");
				out.print("</td>");
				
				out.print("</tr>");

				//parse out the results
				while (airlines.next()) {
					
					//display a row
					out.print("<tr>");
					out.print("<td>");
					
					out.print("<form action='airlines.jsp'>"
							+ "<input type='hidden' name='airlineId' value='" 
							+ airlines.getString("airlineId") 
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
					out.print(airlines.getString("airlineId"));
					out.print("</td>");
					//display a column
					out.print("<td>");
					out.print(airlines.getString("name"));
					out.print("</td>");
					
					//display a column
					out.print("<td>");
					out.print(airlines.getString("phone"));
					out.print("</td>");
				
					
					
					//FETCH AIRPORTS
					airports = (con.createStatement()).executeQuery("select airportId from operationAirports where airlineId='" + airlines.getString("airlineId") + "'");
					out.print("<td>");
					while(airports.next())
					{	
						out.print(airports.getString("airportId") + ",");
					}
					out.print("</td>");					
					
					out.print("<td>");
					out.print("<form action='airlines.jsp'>"
							+ "<input type='hidden' name='airlineId' value='" 
							+ airlines.getString("airlineId") 
							+ "'>"  
							+ "<button type='submit' name ='deleteButton' class='btn'><i class='fa-solid fa-trash-can'></i></i></button>"
							+ "</form>");
					//out.print("<button onclick='delete(this);' class='btn'><i class='fa fa-trash'></i></button>");
					//out.print()
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
	
	<br> 
	
	</body>

</html>