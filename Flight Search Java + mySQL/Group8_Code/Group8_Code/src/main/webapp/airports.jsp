<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1" import="com.cs336.pkg.*"%>
<%@ page import="java.io.*,java.util.*,java.sql.*"%>
<%@ page import="javax.servlet.http.*,javax.servlet.*" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0-beta3/css/all.min.css">

	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
		<title>Airports</title>
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
				<h1> Airports Management</h1>
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
				PreparedStatement searchStatement = con.prepareStatement("select * from airports where airportId = ?");
				//Create a SQL statement
				Statement stmt = con.createStatement();
				String str,
						airportId,name,
						streetAddress,city,state,zipcode,country,
						phone;
			
				int i;
				ResultSet airports; 
				
				if(request.getParameter("editButton") != null)
				{	 
							 out.print("<h3> Edit an Airport:</h3>"
									 + "<form action='airports.jsp'>"
									 + "<p style='color:brown;'><i>Leave a field empty to keep it unchanged.</i></p>"
									 + "Edit For Airport Code: " + request.getParameter("airportId")
						  			 + "<input type='hidden' name='airportId' value='"+request.getParameter("airportId")+"'><br><br>"
						  			 + "<label for='name'>Name:</label>"
						  			 + "<input type='text' name='name' placeholder='Newark Liberty International' ><br><br>"
						  			 + "<label for='streetAddress'>Street Address:</label>"
						  			 + "<input type='text' name='streetAddress' placeholder='123 Airport Street' ><br><br>"
						  			 + "<label for='city'>City:</label>"
								  	 + "<input type='text' name='city' placeholder='City' ><br><br>"
								  	 + "<label for='state'>State:</label>"
								  	 + "<input type='text' name='state' placeholder='NJ' ><br><br>"
								  	 + "<label for='zipcode'>Zipcode:</label>"
								  	 + "<input type='text' name='zipcode' placeholder='12345' ><br><br>"
								  	 + "<label for='country'>Country:</label>"
								  	 + "<input type='text' name='country' placeholder='USA' ><br><br>"
								  	 		 
								  	 + "<label for='phone'>Phone number:</label>"
						  			 + "<input type='tel' id='phone' name='phone' placeholder='1-234-567-7890' ><br><br>"
						  			 + "<input type='submit' name='saveEdit' value='Save'>"
									 + "</form><br>"
								     );
				}
				
				if(request.getParameter("saveNew") != null)
				{
					airportId = request.getParameter("airportId");
					searchStatement.setString(1,airportId);
					airports = searchStatement.executeQuery();
					if(!airports.next())
					{
						ps = con.prepareStatement("insert into airports values(?,?,?,?,?,?,?,?)");
						name = request.getParameter("name");
						phone = request.getParameter("phone");
						streetAddress = request.getParameter("streetAddress");
						city = request.getParameter("city");
						state = request.getParameter("state");
						zipcode = request.getParameter("zipcode");
						country = request.getParameter("country");
						
						ps.setString(1, airportId);
						ps.setString(2,name);
						ps.setString(3,phone);
						ps.setString(4,streetAddress);
						ps.setString(5,city);
						ps.setString(6,state);
						ps.setString(7,zipcode);
						ps.setString(8,country);
						
						ps.executeUpdate();
						out.println("<p style='background-color:green;color:black;'>Successfully added the airport!</p>");	
					}
					else
					{
						 out.println("<p style='color:black;background-color:red;'><b>Airport with that code already exists!</b></p>");
					}
				}
				
				if(request.getParameter("saveEdit") != null)
				{
					airportId = request.getParameter("airportId");
					name = request.getParameter("name");
					phone = request.getParameter("phone");
					streetAddress = request.getParameter("streetAddress");
					city = request.getParameter("city");
					state = request.getParameter("state");
					zipcode = request.getParameter("zipcode");
					country = request.getParameter("country");
					
					searchStatement.setString(1,airportId);
					airports = searchStatement.executeQuery();
					
					String oldName = "" , oldPhone = "" , oldStreet = "" , oldCity = "", oldState = "", oldZip = "", oldCountry = "";
					
					while(airports.next())
					{
						oldName = airports.getString(2);
						oldPhone = airports.getString(3);
						oldStreet = airports.getString(4);
						oldCity = airports.getString(5);
						oldState = airports.getString(6);
						oldZip = airports.getString(7);
						oldCountry = airports.getString(8);
						
					}
						
						if(name != null && !name.isBlank() && name.compareToIgnoreCase(oldName) != 0)
						{
							
							ps = con.prepareStatement("update airports set name = ? where airportId = ?");
							ps.setString(1,name);
							ps.setString(2,airportId);
							ps.executeUpdate();
							out.println("<p style='background-color:lightgreen;color:black;'><b>Successfully edited the name!</b></p>");
						}
						else
						{
							 out.println("<p style='color:black;background-color:red;'><b>Name was not edited!</b></p>");
						}
						if(phone != null && !phone.isBlank() && phone.compareToIgnoreCase(oldPhone) != 0)
						{
							
							ps = con.prepareStatement("update airports set phone = ? where airportId = ?");
							ps.setString(1,phone);
							ps.setString(2,airportId);
							ps.executeUpdate();
							out.println("<p style='background-color:lightgreen;color:black;'><b>Successfully edited the phone!</b></p>");
						}
						else
						{
							 out.println("<p style='color:black;background-color:red;'><b>Phone was not edited!</b></p>");
						}
						if(streetAddress != null && !streetAddress.isBlank() && streetAddress.compareToIgnoreCase(oldStreet) != 0)
						{
							
							ps = con.prepareStatement("update airports set streetAddress = ? where airportId = ?");
							ps.setString(1,streetAddress);
							ps.setString(2,airportId);
							ps.executeUpdate();
							out.println("<p style='background-color:lightgreen;color:black;'><b>Successfully edited the Street Address!</b></p>");
						}
						else
						{
							 out.println("<p style='color:black;background-color:red;'><b>Street Address was not edited!</b></p>");
						}
						if(city != null && !city.isBlank() && city.compareToIgnoreCase(oldCity) != 0)
						{
							
							ps = con.prepareStatement("update airports set city = ? where airportId = ?");
							ps.setString(1,city);
							ps.setString(2,airportId);
							ps.executeUpdate();
							out.println("<p style='background-color:lightgreen;color:black;'><b>Successfully edited the city!</b></p>");
						}
						else
						{
							 out.println("<p style='color:black;background-color:red;'><b>City was not edited!</b></p>");
						}
						
						if(state != null && !state.isBlank() && state.compareToIgnoreCase(oldState) != 0)
						{
							
							ps = con.prepareStatement("update airports set state = ? where airportId = ?");
							ps.setString(1,state);
							ps.setString(2,airportId);
							ps.executeUpdate();
							out.println("<p style='background-color:lightgreen;color:black;'><b>Successfully edited the state!</b></p>");
						}
						else
						{
							 out.println("<p style='color:black;background-color:red;'><b>State was not edited!</b></p>");
						}
						
						if(zipcode != null && !zipcode.isBlank() && zipcode.compareToIgnoreCase(oldZip) != 0)
						{
							
							ps = con.prepareStatement("update airports set zipcode = ? where airportId = ?");
							ps.setString(1,zipcode);
							ps.setString(2,airportId);
							ps.executeUpdate();
							out.println("<p style='background-color:lightgreen;color:black;'><b>Successfully edited the zipcode!</b></p>");
						}
						else
						{
							 out.println("<p style='color:black;background-color:red;'><b>Zipcode was not edited!</b></p>");
						}
						if(country != null && !country.isBlank() && country.compareToIgnoreCase(oldCountry) != 0)
						{
							
							ps = con.prepareStatement("update airports set country = ? where airportId = ?");
							ps.setString(1,country);
							ps.setString(2,airportId);
							ps.executeUpdate();
							out.println("<p style='background-color:lightgreen;color:black;'><b>Successfully edited the Country!</b></p>");
						}
						else
						{
							 out.println("<p style='color:black;background-color:red;'><b>Country was not edited!</b></p>");
						}
					}
					
				
				if(request.getParameter("deleteButton") != null)
				{
					
					airportId = request.getParameter("airportId");
					String del = "delete from airports where airportId=" + "?"; 
					ps = con.prepareStatement(del);
					ps.setString(1, airportId);
					ps.executeUpdate();
					out.println("<p style='background-color:lightgreen;color:black;'><b>Successfully deleted the airport!</b></p>");	
					
				}
				
				out.println(" <h3><FONT COLOR=blue> Current List of Airports: </font></h3>");
				if(request.getParameter("editButton") == null && request.getParameter("addButton") == null)
				{
					out.print("<form action='airports.jsp'> <button type='submit' name ='addButton' class='btn'><i class='fa-solid fa-square-plus'>Add</i></button> </form><br>");
				}
				if(request.getParameter("addButton") != null)
				{
						out.print("<h3> Add an Airport:</h3>"
								 + "<form action='airports.jsp'>"
								 + "<label for='airportId'>Airport Code:</label>"
					  			 + "<input type='text' name='airportId' placeholder='3-Letter ID' pattern='[A-Z]{3}' required><br><br>"
					  			 + "<label for='name'>Name:</label>"
					  			 + "<input type='text' name='name' placeholder='Newark Liberty International' required><br><br>"
					  			 + "<label for='streetAddress'>Street Address:</label>"
					  			 + "<input type='text' name='streetAddress' placeholder='123 Airport Street' ><br><br>"
					  			 + "<label for='city'>City:</label>"
							  	 + "<input type='text' name='city' placeholder='City' required><br><br>"
							  	 + "<label for='state'>State:</label>"
							  	 + "<input type='text' name='state' placeholder='NJ' ><br><br>"
							  	 + "<label for='zipcode'>Zipcode:</label>"
							  	 + "<input type='text' name='zipcode' placeholder='12345' ><br><br>"
							  	 + "<label for='country'>Country:</label>"
							  	 + "<input type='text' name='country' placeholder='USA' required><br><br>"
							  	 		 
							  	 + "<label for='phone'>Phone number:</label>"
					  			 + "<input type='tel' id='phone' name='phone' placeholder='1-234-567-7890' required><br><br>"
					  			 + "<input type='submit' name='saveNew' value='Save'>"
								 + "</form><br>"
							     );
					
					
				}
				str = "select * from airports";
				airports = stmt.executeQuery(str);
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
				out.print("<b>Name<b>");
				out.print("</td>");
				//make a column
				out.print("<td style='padding:0 150px 0 15px;'>");
				out.print("<b>Street Address<b>");
				out.print("</td>");
				//display a column
				out.print("<td>");
				out.print("<b>City</b>");
				out.print("</td>");
				//display a column
				out.print("<td>");
				out.print("<b>State</b>");
				out.print("</td>");
				//display a column
				out.print("<td>");
				out.print("<b>Zipcode</b>");
				out.print("</td>");
				//display a column
				out.print("<td>");
				out.print("<b>Country</b>");
				out.print("</td>");
				//make a column
				out.print("<td style='padding:0 100px 0 15px;'>");
				out.print("<b>Phone<b>");
				out.print("</td>");
				
				out.print("<td style='padding:0 10px 0 15px;'>");
				out.print("<b>Delete<b>");
				out.print("</td>");
				
				out.print("</tr>");

				List<String> airportIds = new ArrayList<String>();
				//parse out the results
				while (airports.next()) {
					airportIds.add(airports.getString(1));
					//i++;
					//display a row
					out.print("<tr>");
					out.print("<td>");
					
					out.print("<form action='airports.jsp'>"
							+ "<input type='hidden' name='airportId' value='" 
							+ airports.getString("airportId") 
							+ "'>"  
							+ "<button type='submit' name ='editButton' class='btn'><i class='fa-solid fa-pen-to-square'></i></button>"
							+ "</form>");
					
					//out.print()
					out.print("</td>");
					//display a column
					out.print("<td style='padding:0 30px 0 15px;'>");
					out.print(i);
					out.print("</td>");
					//display a column
					out.print("<td>");
					out.print(airports.getString("airportId"));
					out.print("</td>");
					//display a column
					out.print("<td>");
					out.print(airports.getString("name"));
					out.print("</td>");
					//display a column
					out.print("<td>");
					out.print(airports.getString("streetAddress"));
					out.print("</td>");
					//display a column
					out.print("<td>");
					out.print(airports.getString("city"));
					out.print("</td>");
					//display a column
					out.print("<td>");
					out.print(airports.getString("state"));
					out.print("</td>");
					//display a column
					out.print("<td>");
					out.print(airports.getString("zipcode"));
					out.print("</td>");
					//display a column
					out.print("<td>");
					out.print(airports.getString("country"));
					out.print("</td>");
					
					
					out.print("<td>");
					out.print(airports.getString("phone"));
					out.print("</td>");
					
					out.print("<td>");
					out.print("<form action='airports.jsp' method='post'>"
							+ "<input type='hidden' name='airportId' value='" 
							+ airports.getString("airportId") 
							+ "'>"  
							+ "<button type='submit' name ='deleteButton' class='btn'><i class='fa-solid fa-trash-can'></i></i></button>"
							+ "</form>");
					
					out.print("</td>");
					out.print("</tr>");
					i++;

				}
				
				out.print("</table>");
				
				request.setAttribute("airportIds", airportIds);
				
				//close the connection.
				con.close();
				
			} catch (Exception ex) {
				out.print(ex);
				out.print("insert failed");
			}
// here
			
			
			
		%>
		
	
	<br> 
	
	</body>

</html>