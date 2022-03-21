<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1" import="com.cs336.pkg.*"%>
<%@ page import="java.io.*,java.util.*,java.sql.*,java.time.LocalDate"%>
<%@ page import="javax.servlet.http.*,javax.servlet.*" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0-beta3/css/all.min.css">

	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
		<title>Reservations</title>
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
	
			<%
			try{
				
				String customerId = (new Cookies()).getCurrentUser();
				ApplicationDB db = new ApplicationDB();	
				Connection con = db.getConnection();

				PreparedStatement ps;
				
				//Create a SQL statement
				Statement stmt = con.createStatement();	
					
					out.print("Customer: "+ customerId);
					
					out.print("<h1> Upcoming Reservations: </h1>");
					
					LocalDate today = LocalDate.now();
							
					out.print("<p>Todays Date: " + today + "</p>");
					
					
					
					
					ResultSet upcoming,past;
					upcoming = stmt.executeQuery("select * from reservations join flights using (airlineId,flightNumber) where customerId = '" + customerId + "'and date >= '" + today + "'");
					out.print("<table>"
							+ "<tr> <td> Date </td>"
							+ "<td> FlightNumber </td>"
							+ "<td> From </td>"
							+ "<td> Departure Time </td>"
							+ "<td> To </td>"
							+ "<td> Arrival Time </td>"
							+ "</tr>"
							);
					while(upcoming.next())
						{
							out.print("<tr> <td>" + upcoming.getString("date")+"</td>"
									+ "<td>"+upcoming.getString("airlineId")+upcoming.getString("flightNumber")+"</td>"
									+ "<td>"+upcoming.getString("departureAirport")+"</td>"
									+ "<td>"+upcoming.getString("departureTime")+"</td>"
									+ "<td>"+upcoming.getString("arrivalAirport")+"</td>"
									+ "<td>"+upcoming.getString("arrivalTime")+"</td>"
									+ "</tr>"
							);
						}
					
					out.print("</table>");
					out.print("<p><h2> Past Reservations: </h2></p>");
					past = stmt.executeQuery("select * from bookings join flights using (airlineId,flightNumber) where customerId = '" + customerId + "'and date < '" + today + "'");
					out.print("<table>"
							+ "<tr> <td> Date </td>"
							+ "<td> FlightNumber </td>"
							+ "<td> From </td>"
							+ "<td> Departure Time </td>"
							+ "<td> To </td>"
							+ "<td> Arrival Time </td>"
							+ "</tr>"
							);
					while(past.next())
						{
							out.print("<tr> <td>" + past.getString("date")+"</td>"
									+ "<td>"+past.getString("airlineId")+past.getString("flightNumber")+"</td>"
									+ "<td>"+past.getString("departureAirport")+"</td>"
									+ "<td>"+past.getString("departureTime")+"</td>"
									+ "<td>"+past.getString("arrivalAirport")+"</td>"
									+ "<td>"+past.getString("arrivalTime")+"</td>"
									+ "</tr>"
							);
						}
				
			}
			catch(Exception ex)
			{
				out.print(ex + "failed");
			}
			
			%>
	
	
	
	</body>
	</html>