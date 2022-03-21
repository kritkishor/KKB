<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1" import="com.cs336.pkg.*"%>
<%@ page import="java.io.*,java.util.*,java.sql.*"%>
<%@ page import="javax.servlet.http.*,javax.servlet.*" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0-beta3/css/all.min.css">

	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
		<title>Flights</title>
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
				<h1> Flights Management</h1>
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
				
				
				//Create a SQL statement
				Statement stmt = con.createStatement();
				String str,id,name,address,phone;
				String flightNumber, flightType, airlineId, aircraftId,
						departureAirport, arrivalAirport,
						departureDate, departureTime,
						arrivalDate, arrivalTime,cost,
						arrivalTimePlusDays;
				
				PreparedStatement searchStatement = con.prepareStatement("select * from flights where airlineId = ? and flightNumber = ?");
				
				
				int i;
				ResultSet airports,airlines; 
				ResultSet flights,days;

				if(request.getParameter("editButton") == null && request.getParameter("addButton") == null)
				{
					out.print("<br> <br><form action='flights.jsp'> <button type='submit' name ='addButton' class='btn'><i class='fa-solid fa-square-plus'>Add</i></button></form>");
					out.print("<br> <br><form action='flights.jsp'> <button type='submit' name ='search' class='btn'>Search</button> <input type='text' name='airport'></form>");
				}
				if(request.getParameter("editButton") != null)
				{
					out.print("<h3> Edit a Flight:</h3>"
							 + "<p style='color:brown;'><i>Leave a field empty to keep it unchanged.</i></p>"
							 + "Edit for Flight: " + request.getParameter("airlineId") + request.getParameter("flightNumber")
							 + "<form action='flights.jsp'>"
							 + "<br><label for='flightType'>Flight Type:</label>"
						  	 + "<select name='flightType'><option>Domestic</option><option>International</option></select> "
							 
				  			 + "<input type='hidden' name='airlineId' value='"+ request.getParameter("airlineId")+"'>"
				  			 
						  	 + "<input type='hidden' id='flightNumber' name='flightNumber' value='"+request.getParameter("flightNumber")+"'><br><br>"
						  		 
						  			 
						  	 + "<label for='aircraftId'>Aircraft ID:</label>"
							 + "<input type='text' name='aircraftId' placeholder='000'><br><br>"
						  	 + "<label for='name'>Departure Airport Code:</label>"
						  	 + "<input type='text' name='departureAirport' placeholder='ABC' pattern=[A-Z]{3} ><br><br>"
				  			 + "<label for='departureDate'>Departure Date:</label>"
				  			 + "<input type='date' name='departureDate' placeholder='YYYY-MM-DD' pattern=[0-9]{4}-[0-9]{2}-[0-9]{2}><br><br>" // EDIT required
				  			 + "<label for='departureTime'>Departure Time:</label>"
				  			 + "<input type='text' name='departureTime' placeholder='HH-MM' pattern:'[0-9]{2}:[0-9]{2}' ><br><br>"
				  			 + "<label for='name'>Arrival Airport Code:</label>"
							 + "<input type='text' name='arrivalAirport' placeholder='ABC' pattern=[A-Z]{3} ><br><br>"
				  			 + "<label for='arrivalDate'>Arrival Date:</label>"
						  	 + "<input type='date' name='arrivalDate' placeholder='YYYY-MM-DD' pattern=[0-9]{4}-[0-9]{2}-[0-9]{2}><br><br>" // EDIT required
						  	 + "<label for='arrivalTime'>Arrival Time:</label>"
						  	 + "<input type='text' name='arrivalTime' placeholder='HH-MM' pattern:'[0-9]{2}:[0-9]{2}' ><br><br>"
						  			+ "<label for='cost'>Cost:</label>"
						  			 + "<input type='text' name='cost' pattern:'[0-9]' ><br><br>"
						  					+ "<label for='arrivalTimePlusDays'>Arrival Time + hours:</label>"
								  			 + "<input type='text' name='arrivalTimePlusDays' placeholder='HH-MM' pattern:'[0-9]{2}:[0-9]{2}'><br><br>"
						  			 + "<label>Operation Days:</label><br><br>"
								  	 + "<table>"
								  		+"<tr><td><label><b>Day</b></label></td> <td><b>Operates</b></td> <td><b>Does not Operate</b></td></tr>"
								     	+"<tr><td><label>Sunday</label></td> <td><input type='radio' name='sunday' value='yes' ><td><input type='radio' name='sunday' value='no'></td></tr>"
								  		+"<tr><td><label>Monday</label></td> <td><input type='radio' name='monday' value='yes'><td><input type='radio' name='monday' value='no'></td></tr>"
										+"<tr><td><label>Tuesday</label></td> <td><input type='radio' name='tuesday' value='yes'><td><input type='radio' name='tuesday' value='no'></td></tr>"
										+"<tr><td><label>Wednesday</label></td> <td><input type='radio' name='wednesday' value='yes' ><td><input type='radio' name='wednesday' value='no'></td></tr>"
										+"<tr><td><label>Thursday</label></td> <td><input type='radio' name='thursday' value='yes' ><td><input type='radio' name='thursday' value='no'></td></tr>"
										+"<tr><td><label>Friday</label></td> <td><input type='radio' name='friday' value='yes' ><td><input type='radio' name='friday' value='no'></td></tr>"
								  		+"<tr><td><label>Saturday</label></td> <td><input type='radio' name='saturday' value='yes' ><td><input type='radio' name='saturday' value='no'></td></tr>"
									+ "</table><br>"
				  			 + "<input type='submit' name='saveEdit' value='Save'>"
							 + "</form>"
						     );
				
				}
				
				if(request.getParameter("addButton") != null)
				{
						out.print("<h3> Add a Flight:</h3>"
								 + "<form action='flights.jsp'>"
								 + "<label for='flightType'>Flight Type:</label>"
							  	 + "<select name='flightType'><option>Domestic</option><option>International</option></select> <br><br>"
								 + "<label for='airlineId'>Airlines Code:</label>"
					  			 + "<input type='text' name='airlineId' placeholder='AB' pattern='[A-Z]{2}' required><br><br>"
					  			 + "<label for='flightNumber'>Flight Number:</label>"
							  	 + "<input type='text' id='flightNumber' name='flightNumber' placeholder='000' required><br><br>"
							  	 + "<label for='aircraftId'>Aircraft ID:</label>"
								 + "<input type='text' name='aircraftId' placeholder='000' required><br><br>"
							  	 + "<label for='name'>Departure Airport Code:</label>"
							  	 + "<input type='text' name='departureAirport' placeholder='ABC' pattern=[A-Z]{3} required><br><br>"
					  			 + "<label for='departureTime'>Departure Time:</label>"
					  			 + "<input type='text' name='departureTime' placeholder='HH-MM' pattern:'[0-9]{2}:[0-9]{2}' required><br><br>"
					  			 + "<label for='name'>Arrival Airport Code:</label>"
								 + "<input type='text' name='arrivalAirport' placeholder='ABC' pattern=[A-Z]{3} required><br><br>"
							  	 + "<label for='arrivalTime'>Arrival Time:</label>"
							  	 + "<input type='text' name='arrivalTime' placeholder='HH-MM' pattern:'[0-9]{2}:[0-9]{2}' required><br><br>"
							  	 + "<label for='arrivalTimePlusDays'>Arrival Time + days:</label>"
							  	 + "<input type='text' name='arrivalTimePlusDays' placeholder='00' pattern:'[0-9]' required><br><br>"
							  	 + "<label for='cost'>Cost:</label>"
								 + "<input type='text' name='cost' pattern:'[0-9]' required><br><br>"
										 
							  			 + "<label>Operation Days:</label><br><br>"
									  	 + "<table>"
									  		+"<tr><td><label><b>Day</b></label></td> <td><b>Operates</b></td> <td><b>Does not Operate</b></td></tr>"
									     	+"<tr><td><label>Sunday</label></td> <td><input type='radio' name='sunday' value='yes' required><td><input type='radio' name='sunday' value='no'></td></tr>"
									  		+"<tr><td><label>Monday</label></td> <td><input type='radio' name='monday' value='yes'required><td><input type='radio' name='monday' value='no'></td></tr>"
											+"<tr><td><label>Tuesday</label></td> <td><input type='radio' name='tuesday' value='yes'required><td><input type='radio' name='tuesday' value='no'></td></tr>"
											+"<tr><td><label>Wednesday</label></td> <td><input type='radio' name='wednesday' value='yes' required><td><input type='radio' name='wednesday' value='no'></td></tr>"
											+"<tr><td><label>Thursday</label></td> <td><input type='radio' name='thursday' value='yes' required><td><input type='radio' name='thursday' value='no'></td></tr>"
											+"<tr><td><label>Friday</label></td> <td><input type='radio' name='friday' value='yes' required><td><input type='radio' name='friday' value='no'></td></tr>"
									  		+"<tr><td><label>Saturday</label></td> <td><input type='radio' name='saturday' value='yes' required><td><input type='radio' name='saturday' value='no'></td></tr>"
										+ "</table><br>"
					  			 + "<input type='submit' name='saveNew' value='Save'>"
								 + "</form>"
							     );
					
					
				}
				if(request.getParameter("saveNew") != null)
				{
					airlineId = request.getParameter("airlineId");
					flightNumber = request.getParameter("flightNumber");
					
				
				
					searchStatement.setString(1, airlineId);
					searchStatement.setString(2,flightNumber);
					flights = searchStatement.executeQuery();
					
					if(flights.next())
					{
						 out.println("<p style='color:black;background-color:red;'>Flight already exists!</p>");
					}
					
					else
					{
						aircraftId = request.getParameter("aircraftId");
						
						ps = con.prepareStatement("select * from aircrafts where aircraftId = ? and owner = ?");
						ps.setString(1,aircraftId);
						ps.setString(2,airlineId);
						ResultSet aircrafts = ps.executeQuery();
						if(aircrafts.next())
						{
							
							
							departureAirport =request.getParameter("departureAirport");
							arrivalAirport = request.getParameter("arrivalAirport");
							ps = con.prepareStatement("select * from operationAirports where airportId = ? and airlineId = ?");
							
							ps.setString(1,departureAirport);
							ps.setString(2, airlineId);
							airports = ps.executeQuery();
							if(airports.next())
							{
								ps = con.prepareStatement("select * from operationAirports where airportId = ? and airlineId = ?");
								
								ps.setString(1,arrivalAirport);
								ps.setString(2, airlineId);
								airports = ps.executeQuery();
								if(airports.next())
								{
									flightType = request.getParameter("flightType");
									departureTime = request.getParameter("departureTime");
							
									arrivalTime = request.getParameter("arrivalTime");
									cost = request.getParameter("cost");
									arrivalTimePlusDays = request.getParameter("arrivalTimePlusDays");
									ps = con.prepareStatement("insert into flights values (?,?,?,?,?,?,?,?,?,?)");
									ps.setString(1, flightNumber);
									ps.setString(2, flightType);
									ps.setString(3, airlineId);
									ps.setString(4, aircraftId);
									ps.setString(5, departureAirport);
									ps.setString(6, arrivalAirport);
									ps.setString(7, departureTime);
									ps.setString(8, arrivalTime);
									
									float duration = (new DateParser()).findDuration(departureTime,arrivalTime,Integer.valueOf(arrivalTimePlusDays));
									ps.setFloat(9,duration);
									ps.setString(10,cost);
									ps.executeUpdate();
									
									// INSERT INTO operationDays
									ps = con.prepareStatement("insert into operationDays values(?,?,?)");
									ps.setString(2, airlineId);
									ps.setString(3, flightNumber);
									String[] operatingDays = {"sunday","monday","tuesday","wednesday","thursday","friday","saturday"};
									int j = 1;
									for(String s : operatingDays)
									{
										
										if(request.getParameter(s).equals("yes"))
										{
											ps.setString(1, String.valueOf(j));
											ps.executeUpdate();
										}
										j++;
									}
									out.println("<p style='background-color:green;color:black;'>Successfully added the new flight!</p>");	
								}
								else
								{
									out.println("<p style='color:black;background-color:red;'>No such Airline and Airport Operation Combination exists!</p>");
								}
							}
							else
							{
								out.println("<p style='color:black;background-color:red;'>No such Airline and Airport Operation Combination exists!</p>");
							}
							

						}
						else
						{
							out.println("<p style='color:black;background-color:red;'>No such Airline and Aircraft Combination exists!</p>");
						}
						
					}
					
				}
				if(request.getParameter("deleteButton") != null)
				{
					
					flightNumber = request.getParameter("flightNumber");
					airlineId = request.getParameter("airlineId");
					String del = "delete from flights where airlineId = ? and flightNumber = ? "; 
						ps = con.prepareStatement(del);
						ps.setString(1,airlineId);
						ps.setString(2,flightNumber);
						ps.executeUpdate();
						out.println("<p style='background-color:lightgreen;color:black;'><b>Deletion Successful!</b></p>");	
					
				}
				
				out.println(" <h3><FONT COLOR=blue> Current List of Flights: </font></h3>");
				
				str = "select * from flights ";
				
				if(request.getParameter("search")!=null)
				{
					String key = request.getParameter("airport");
				//	out.print(key);
					str += "where departureAirport = '" + key +"' or arrivalAirport = '" + key + "'" ;
				}
				
				
				flights = stmt.executeQuery(str);
				
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
				out.print("<b>Flight Number<b>");
				out.print("</td>");
				//make a column
				out.print("<td style='padding:0 50px 0 15px;'>");
				out.print("<b>Type<b>");
				out.print("</td>");
				
				out.print("<td style='padding:0 50px 0 15px;'>");
				out.print("<b>Aircraft<b>");
				out.print("</td>");
				
				//make a column
				out.print("<td style='padding:0 100px 0 15px;'>");
				out.print("<b>From<b>");
				out.print("</td>");
				
				out.print("<td style='padding:0 100px 0 15px;'>");
				out.print("<b>Departure<b>");
				out.print("</td>");
				
				out.print("<td style='padding:0 100px 0 15px;'>");
				out.print("<b>To<b>");
				out.print("</td>");
				
				out.print("<td style='padding:0 50px 0 15px;'>");
				out.print("<b>Arrival<b>");
				out.print("</td>");
				
				out.print("<td style='padding:0 50px 0 15px;'>");
				out.print("<b>Duration (Hours)<b>");
				out.print("</td>");
				
				out.print("<td style='padding:0 10px 0 15px;'>");
				out.print("<b>Number Of Seats<b>");
				out.print("</td>");
				
				//display a column
				out.print("<td>");
				out.print("<b>Operation Days</b>");
				out.print("</td>");
				
				out.print("<td style='padding:0 10px 0 15px;'>");
				out.print("<b>Delete<b>");
				out.print("</td>");
				
				out.print("</tr>");

				
				//parse out the results
				while (flights.next()) {
					
					//display a row
					out.print("<tr>");
					out.print("<td>");
					
					out.print("<form action='flights.jsp'>"
							+ "<input type='hidden' name='flightNumber' value='" 
							+ flights.getString("flightNumber") 
							+ "'>"  
							+ "<input type='hidden' name='airlineId' value='" 
							+ flights.getString("airlineId") 
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
					out.print(flights.getString("airlineId") + flights.getString("flightNumber"));
					out.print("</td>");
					//display a column
					out.print("<td>");
					out.print(flights.getString("flightType"));
					out.print("</td>");
					//display a column
					out.print("<td>");
					out.print(flights.getString("aircraftId"));
					out.print("</td>");
					
					//display a column
					out.print("<td>");
					out.print(flights.getString("departureAirport"));
					out.print("</td>");
				
					
				
					//display a column
					out.print("<td>");
					out.print(flights.getString("departureTime"));
					out.print("</td>");
					
					//display a column
					out.print("<td>");
					out.print(flights.getString("arrivalAirport"));
					out.print("</td>");
					
				
					//display a column
					out.print("<td>");
					out.print(flights.getString("arrivalTime"));
					out.print("</td>");
				
					//display a column
					out.print("<td>");
					out.print(flights.getString("duration"));
					
					out.print("</td>");
					//display a column
					out.print("<td>");
					out.print("N/A");
					out.print("</td>");
					
				
					out.print("<td>");
					days = (con.createStatement()).executeQuery("select day from operationDays where flightNumber = '" + flights.getString("flightNumber") +"'and airlineId='" + flights.getString("airlineId") + "'");
					while(days.next())
					{
						out.print((new DateParser()).getDay(Integer.parseInt(days.getString("day")))+",");
					}
					out.print("</td>");
					
					
					out.print("<td>");
					out.print("<form action='flights.jsp'>"
							+ "<input type='hidden' name='flightNumber' value='" 
							+ flights.getString("flightNumber") 
							+ "'>" 
							+ "<input type='hidden' name='airlineId' value='" 
							+ flights.getString("airlineId") 
							+ "'>"
							+ "<button type='submit' name ='deleteButton' class='btn'><i class='fa-solid fa-trash-can'></i></i></button>"
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
		
	
	<br> 
	
	</body>

</html>