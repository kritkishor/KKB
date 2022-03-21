<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1" import="com.cs336.pkg.*"%>
<%@ page import="java.io.*,java.util.*,java.sql.*,java.time.LocalDate"%>
<%@ page import="javax.servlet.http.*,javax.servlet.*" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0-beta3/css/all.min.css">

	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
		<title>Flight Search</title>
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
				<form action="customersHome.jsp">
				<% 
					out.print("<input type='hidden' name='username' value='" + request.getParameter("username") + "'>" );
				%>
					<input type="submit" name="backButton" value="Go Back">
				</form>
				
			</td>
			<td style="padding:0 100px 0 15px;">
				<h1> Search Results</h1>
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

				PreparedStatement ps,ps1,ps2;
				
				//Create a SQL statement
				Statement stmt = con.createStatement();
				ResultSet flights,flights1,flights2,airport1,airport2;
				
				String oneWay, roundTrip, oneStop, twoStops;
				String reservationDate = (new DateParser()).getDateTimeNow();
				String customerId = (new Cookies()).getCurrentUser();
				
				String str,
					departingFlights, arrivingFlights, departureAirport, arrivalAirport,
					nonstop,
					fromDate, toDate,
					cityOrAirport,
					tripType, dateType, classType, passengers;
			
				String[] departureAirports = new String[50]; String[] arrivalAirports = new String[50];
				int day;
				String[][] search = new String[8][50];
				if(request.getParameter("book1stop") != null)
				{
					
					String book = "insert into tickets (customerId, reservationDate, hasStops) values (?,?,?)";
					ps = con.prepareStatement(book);
					ps.setString(1,customerId);
					
					
					// HERE EDIT For demo
					ps.setString(2,reservationDate);
				
					ps.setString(3,"1");
					ps.executeUpdate();
					
					book = "select ticketNumber from tickets where customerId = ? and reservationDate = ?";
					
					ps = con.prepareStatement(book);
					ps.setString(1,customerId);
					ps.setString(2,reservationDate);
					
					ResultSet ticket = ps.executeQuery();
					
					int ticketNumber = 0;
					
					while(ticket.next())
					{
						ticketNumber = Integer.valueOf(ticket.getString("ticketNumber"));
					}
					
					
					
					book = "insert into fees values (?,?,?)";
					ps = con.prepareStatement(book);
					ps.setInt(1,ticketNumber);
					ps.setString(2,"Booking Fee");	
					ps.setString(3,"50.0");
					ps.executeUpdate();
					
					String flight1 = request.getParameter("connection1of2");
					String flight2 = request.getParameter("connection2of2");
					String airline1 = flight1.substring(0,2);
					String number1 = flight1.substring(2);
					String airline2 = flight2.substring(0,2);
					String number2 = flight2.substring(2);
					String date1 =  request.getParameter("date1of2");
					String date2 = request.getParameter("date2of2");
					
					book = "insert into reservations values (?,?,?,?,?)";
					ps = con.prepareStatement(book);
					ps.setInt(1,ticketNumber);
					ps.setString(2,airline1);
					ps.setString(3,number1);
					ps.setString(4,date1);
					ps.setString(5, "0");
					ps.executeUpdate();
					
					book = "insert into reservations values (?,?,?,?,?)";
					ps = con.prepareStatement(book);
					ps.setInt(1,ticketNumber);
					ps.setString(2,airline2);
					ps.setString(3,number2);
					ps.setString(4,date2);
					ps.setString(5, "0");
					ps.executeUpdate();
				}
				if(request.getParameter("book2stops") != null)
				{
					String book = "insert into tickets (customerId, reservationDate, hasStops) values (?,?,?)";
				
					ps = con.prepareStatement(book);
					ps.setString(1,customerId);
					// HERE EDIT For demo
					ps.setString(2,reservationDate);
				
					ps.setString(3,"1");
					ps.executeUpdate();
					
					book = "select ticketNumber from tickets where customerId = ? and reservationDate = ?";
					
					ps = con.prepareStatement(book);
					ps.setString(1,customerId);
					ps.setString(2,reservationDate);
					
					ResultSet ticket = ps.executeQuery();
					
					int ticketNumber = 0;
					
					while(ticket.next())
					{
						ticketNumber = Integer.valueOf(ticket.getString("ticketNumber"));
					}
					
					
					
					book = "insert into fees values (?,?,?)";
					ps = con.prepareStatement(book);
					ps.setInt(1,ticketNumber);
					ps.setString(2,"Booking Fee");	
					ps.setString(3,"50.0");
					ps.executeUpdate();
					
					String flight1 = request.getParameter("connection1of3");
					String flight2 = request.getParameter("connection2of3");
					String flight3 = request.getParameter("connection3of3");
					String airline1 = flight1.substring(0,2);
					String number1 = flight1.substring(2);
					String airline2 = flight2.substring(0,2);
					String number2 = flight2.substring(2);
					String airline3 = flight3.substring(0,2);
					String number3 = flight3.substring(2);
					String date1 =  request.getParameter("date1of3");
					String date2 = request.getParameter("date2of3");
					String date3 = request.getParameter("date3of3");
					
					book = "insert into reservations values (?,?,?,?,?)";
					ps = con.prepareStatement(book);
					ps.setInt(1,ticketNumber);
					ps.setString(2,airline1);
					ps.setString(3,number1);
					ps.setString(4,date1);
					ps.setString(5, "0");
					ps.executeUpdate();
					
					book = "insert into reservations values (?,?,?,?,?)";
					ps = con.prepareStatement(book);
					ps.setInt(1,ticketNumber);
					ps.setString(2,airline2);
					ps.setString(3,number2);
					ps.setString(4,date2);
					ps.setString(5, "0");
					ps.executeUpdate();
					
					
					book = "insert into reservations values (?,?,?,?,?)";
					ps = con.prepareStatement(book);
					ps.setInt(1,ticketNumber);
					ps.setString(2,airline3);
					ps.setString(3,number3);
					ps.setString(4,date3);
					ps.setString(5, "0");
					ps.executeUpdate();
				}
				if(request.getParameter("searchButton") != null)
				{
					// Get user input data
					search[0][0] = tripType = request.getParameter("tripType");
					
					//FIND airportId according to the User input
					str = "select * from airports where airportId = ? or name like ? or city like ?";
					
					ps = con.prepareStatement(str);
					ps.setString(1, request.getParameter("departureAirport"));
					ps.setString(2, request.getParameter("departureAirport"));
					ps.setString(3, request.getParameter("departureAirport"));
					airport1 = ps.executeQuery();
					
					ps = con.prepareStatement(str);
					ps.setString(1, request.getParameter("arrivalAirport"));
					ps.setString(2, request.getParameter("arrivalAirport"));
					ps.setString(3, request.getParameter("arrivalAirport"));
					airport2 = ps.executeQuery();
					
				
					int i = 0;
					while(airport1.next())
					{
						search[1][i] = departureAirports[i] = airport1.getString("airportId");
						i++;
					}
					i = 0;
					while(airport2.next())
					{
						search[2][i] = arrivalAirports[i] = airport2.getString("airportId");
						i++;
					}
					
					
					search[3][0] = fromDate = request.getParameter("fromDate");
					
					if(tripType.equals("oneWay"))
					{
						search[4][0] = toDate = "0000-00-00";
					}
					else
					{
						if(request.getParameter("toDate") ==  null || (request.getParameter("toDate").isBlank()))
						{
							out.println("<p style='color:black;background-color:red;'><b>You must enter a return date for round trip flights</b></p>");
							out.print("<form action='customersHome.jsp'><button type='submit'>Understood</button></form>");
						}
						search[4][0] = toDate = request.getParameter("toDate");
					}
					search[5][0] = dateType = request.getParameter("dateType");
					search[6][0] = passengers = request.getParameter("passengers");
					search[7][0] = classType = request.getParameter("classType");
					departureAirport = departureAirports[0];
					arrivalAirport = arrivalAirports[0];
					//Save Cookies
					new Cookies(search);
					
					
				}
				// Get User Input Search Categories from cookies if this page was not directly opened after filling up search form
				else
				{
					search = (new Cookies()).getSearchData();
					
					tripType = search[0][0];
					int i = 0;
					while(search[1][i] != null && !search[1][i].isBlank())
					{
						departureAirports[i] = search[1][i];
						i++;
					}
					i = 0;
					while(search[2][i] != null && !search[2][i].isBlank())
					{
						arrivalAirports[i] = search[2][i];
						i++;
					}
					fromDate = search[3][0];
					toDate = search[4][0]; 
					dateType = search[5][0]; 
					passengers = search[6][0]; 
					classType = search[7][0]; 
					
					
				}
					
				nonstop = "select * from flights join operationDays using (airlineId,flightNumber) where (";
				oneStop = "select * from flights f join flights g on (f.arrivalAirport = g.departureAirport) where (";
				twoStops = "select * from flights f join flights g on (f.arrivalAirport = g.departureAirport) join flights h on (h.departureAirport = g.arrivalAirport) where (";
				
				
				int i = 0;
				while(departureAirports[i] != null && !departureAirports[i].isBlank())
				{
					nonstop += "departureAirport = '" + departureAirports[i] + "' ";
					oneStop += "f.departureAirport = '" + departureAirports[i] + "' ";
					twoStops += "f.departureAirport = '" + departureAirports[i] + "' ";
			//		out.println(nonstop);
					if(departureAirports[i+1] != null && !departureAirports[i+1].isBlank())
					{
						nonstop += "or ";
						oneStop += "or ";
						twoStops += "or ";
					}
					i++;
				}
				
				i = 0;
				nonstop += ") and (";
				oneStop += ") and (";
				twoStops += ") and (";
				while(arrivalAirports[i] != null && !arrivalAirports[i].isBlank())
				{
					nonstop += "arrivalAirport = '" + arrivalAirports[i] + "' ";
					oneStop += "g.arrivalAirport = '" + arrivalAirports[i] + "' ";
					twoStops += "h.arrivalAirport = '" + arrivalAirports[i] + "' ";
			//		out.println(nonstop);
					if(arrivalAirports[i+1] != null && !arrivalAirports[i+1].isBlank())
					{
						nonstop += "or ";
						oneStop += "or ";
						twoStops += "or ";
					}
					i++;
				}
				nonstop += ") ";
				oneStop += ") ";
				twoStops += ") ";
				
					//IF EXACT DATES is selected
					if(dateType.equals("exact"))
					{
						
						nonstop += "and (day = ?)"; 
						oneStop += "and (f.day = ?)"; 
						twoStops += "and (f.day = ?)"; 
						
						
					}
					
					
					if(request.getParameter("bookButton") == null)
					{
						// SORT BUTTON
						out.print("<p><form action='flightSearch.jsp'>Sort By<select name='sortCategory'><option value='cost ASC'>Price(Low-High)</option>"
								+"<option value='cost DESC'>Price(High-Low)</option><option value='departureTime ASC'>Take Off(Early-Late)</option>"
								+"<option value='departureTime DESC'>Take Off(Late-Early)</option><option value='arrivalTime ASC'>Landing(Early-Late)</option>"
								+"<option value='arrivalTime DESC'>Landing(Late-Early)</option><option value='duration ASC'>Duration(Short-Long)</option>"
								+"<option value='duration DESC'>Duration(Long-Short)</option></select>"
								+"<input type='submit' name='sortButton' value='sort'></form></p>");
					}
					
					// ALSO SORT CONDITIONS ADD TO QUERY 
					if(request.getParameter("sortButton") != null)
					{
						nonstop += " order by " + request.getParameter("sortCategory");
						oneStop += " order by " + request.getParameter("sortCategory");
						twoStops += " order by " + request.getParameter("sortCategory");
						out.print("Currently Using Sort By: " + request.getParameter("sortCategory"));
					}
					
					// IF no leaving flight has been selected
					if(request.getParameter("select1") == null && request.getParameter("bookButton") == null  && request.getParameter("book2stop") == null && request.getParameter("book2stops") == null)
					{
						//QUERY AND DISPLAY RESULTS
						out.print("<p> <b> Leaving Flights: <b>");
			
						
						//			out.println(twoStops);
						ps = con.prepareStatement(nonstop);
						ps1 = con.prepareStatement(oneStop);
						ps2 = con.prepareStatement(twoStops);
						
						day = (new DateParser()).getDay(fromDate);
						if(dateType.equals("exact")){
							ps.setInt(1,day);
							ps1.setInt(1,day);
							ps2.setInt(1,day);
						}
						
						flights = ps.executeQuery();
					
					//	flights1 = ps1.executeQuery();
				//		flights2 = ps2.executeQuery();
						
						out.print("<table> <tr> <td> Airlines </td>  <td> From </td>   <td> Departure </td> <td> To </td>  <td> Arrival </td> <td> Duration (Hours) </td> <td> cost </td> <td> Select </td>  </tr>");
					
						String arrivalDate;
						while(flights.next())
						{
							arrivalDate = (new DateParser()).findArrivalDate(fromDate,flights.getString("departureTime"),Long.valueOf(flights.getString("duration")));
							out.print("<tr>"
									
									+ "<td>" + flights.getString("airlineId") + "</td>"
									+ "<td>" + flights.getString("departureAirport") + "</td>"
									+ "<td><div>" + fromDate +"</div><div>" + flights.getString("departureTime") + "</div></td>"
									+ "<td>" + flights.getString("arrivalAirport") + "</td>"
									+ "<td><div>" + arrivalDate + "</div>" + flights.getString("arrivalTime") +"</div></td>"
									+ "<td>" + flights.getString("duration") + "</td>"
									+ "<td>" + flights.getString("cost") + "</td>"
									+ "<td><form action='flightSearch.jsp'>"
									+ "<input type='hidden' name='selected1' value='" + flights.getString("airlineId") + flights.getString("flightNumber") + "'>"
									+ "<input type='hidden' name='date1' value='" + fromDate +"'>"
									+"<button type='submit' name='select1'>Select</button></form></td>"
									+ "</tr>"
									);
						}
					/*
						while(flights1.next())
						{
							
							
							
					//		out.print("<tr><td><table>");
							arrivalDate = (new DateParser()).findArrivalDate(fromDate,flights1.getString("f.departureTime"),Long.valueOf(flights1.getString("f.duration")));
							
							
							 
							
							out.print("<tr>"
									
									+ "<td>" + flights1.getString("f.airlineId") + "</td>"
									+ "<td>" + flights1.getString("f.departureAirport") + "</td>"
									+ "<td><div>" + fromDate +"</div><div>" + flights1.getString("f.departureTime") + "</div></td>"
									+ "<td>" + flights1.getString("f.arrivalAirport") + "</td>"
									+ "<td><div>" + arrivalDate + "</div>" + flights1.getString("f.arrivalTime") +"</div></td>"
									+ "<td>" + flights1.getString("f.duration") + "</td>"
									+ "<td>" + flights1.getString("f.cost") + "</td>"
									+ "</tr>"
									);
						
							out.print("<tr>"
									
									+ "<td>" + flights1.getString("g.airlineId") + "</td>"
									+ "<td>" + flights1.getString("g.departureAirport") + "</td>"
									+ "<td><div>" + fromDate +"</div><div>" + flights1.getString("g.departureTime") + "</div></td>"
									+ "<td>" + flights1.getString("g.arrivalAirport") + "</td>"
									+ "<td><div>" + arrivalDate + "</div>" + flights1.getString("g.arrivalTime") +"</div></td>"
									+ "<td>" + flights1.getString("g.duration") + "</td>"
									+ "<td>" + flights1.getString("g.cost") + "</td>"
									+ "<td><form action='flightSearch.jsp'>"
									+ "<input type='hidden' name='connection1of2' value='" + flights1.getString("f.airlineId") + flights1.getString("f.flightNumber") + "'>"
									+ "<input type='hidden' name='connection2of2' value='" + flights1.getString("g.airlineId") + flights1.getString("g.flightNumber") + "'>"
									+ "<input type='hidden' name='date1of2' value='" + fromDate +"'>"
									+ "<input type='hidden' name='date2of2' value='" + arrivalDate +"'>"
									+"<button type='submit' name='book1stop'>Select</button></form></td>"
									+ "</tr>"
									);
						}
						
						
						while(flights2.next())
						{
							arrivalDate = (new DateParser()).findArrivalDate(fromDate,flights2.getString("f.departureTime"),Long.valueOf(flights2.getString("f.duration")));
							out.print("<tr>"
									
									+ "<td>" + flights2.getString("f.airlineId") + "</td>"
									+ "<td>" + flights2.getString("f.departureAirport") + "</td>"
									+ "<td><div>" + fromDate +"</div><div>" + flights2.getString("f.departureTime") + "</div></td>"
									+ "<td>" + flights2.getString("f.arrivalAirport") + "</td>"
									+ "<td><div>" + arrivalDate + "</div>" + flights2.getString("f.arrivalTime") +"</div></td>"
									+ "<td>" + flights2.getString("f.duration") + "</td>"
									+ "<td>" + flights2.getString("f.cost") + "</td>"
									+ "</tr>"
									);
						
							out.print("<tr>"
									
									+ "<td>" + flights2.getString("g.airlineId") + "</td>"
									+ "<td>" + flights2.getString("g.departureAirport") + "</td>"
									+ "<td><div>" + fromDate +"</div><div>" + flights2.getString("g.departureTime") + "</div></td>"
									+ "<td>" + flights2.getString("g.arrivalAirport") + "</td>"
									+ "<td><div>" + arrivalDate + "</div>" + flights2.getString("g.arrivalTime") +"</div></td>"
									+ "<td>" + flights2.getString("g.duration") + "</td>"
									+ "<td>" + flights2.getString("g.cost") + "</td>"
									+ "</tr>"
									);
						
							out.print("<tr>"
								
									+ "<td>" + flights2.getString("h.airlineId") + "</td>"
									+ "<td>" + flights2.getString("h.departureAirport") + "</td>"
									+ "<td><div>" + fromDate +"</div><div>" + flights2.getString("h.departureTime") + "</div></td>"
									+ "<td>" + flights2.getString("h.arrivalAirport") + "</td>"
									+ "<td><div>" + arrivalDate + "</div>" + flights2.getString("h.arrivalTime") +"</div></td>"
									+ "<td>" + flights2.getString("h.duration") + "</td>"
									+ "<td>" + flights2.getString("h.cost") + "</td>"
									+ "<td><form action='flightSearch.jsp'>"
											+ "<input type='hidden' name='connection1of3' value='" + flights2.getString("f.airlineId") + flights2.getString("f.flightNumber") + "'>"
											+ "<input type='hidden' name='connection2of3' value='" + flights2.getString("g.airlineId") + flights2.getString("g.flightNumber") + "'>"
													+ "<input type='hidden' name='connection3of3' value='" + flights2.getString("h.airlineId") + flights2.getString("h.flightNumber") + "'>"
											+ "<input type='hidden' name='date1of3' value='" + fromDate +"'>"
											+ "<input type='hidden' name='date2of3' value='" + arrivalDate +"'>"
													+ "<input type='hidden' name='date3of3' value='" + arrivalDate +"'>"
									+"<button type='submit' name='book2stops'>Select</button></form></td>"
									+ "</tr>"
									);
						}
						*/
						out.print("</table></p>");
						
					}
					
					else if(request.getParameter("select1") != null && request.getParameter("select2") == null && request.getParameter("bookButton") == null)
					{
						String[] flight1 = new String[5];
						flight1[0] = request.getParameter("selected1");
						String[] date1 = new String[5];
						date1[0] = request.getParameter("date1");
						out.print("<p> You Selected Leaving Flight : " + flight1[0] + " for: " + date1[0]  );
						(new Cookies()).flight1Cookies(flight1, date1);
					}
					
					// IF ROUNDTRIP SEARCH RETURNING FLIGHTS
					if(tripType.equals("roundTrip"))
					{
						if(request.getParameter("select2") == null && request.getParameter("bookButton") == null)
						{
							out.print("<p> <b> Returning Flights: <b>");
					
							
							nonstop = "select * from flights join operationDays using (airlineId,flightNumber) where (";
							oneStop = "select * from flights f join flights g on (f.arrivalAirport = g.departureAirport) where (";
							twoStops = "select * from flights f join flights g on (f.arrivalAirport = g.departureAirport) join flights h on (h.departureAirport = g.arrivalAirport) where (";
							
							
							i = 0;
							while(arrivalAirports[i] != null && !arrivalAirports[i].isBlank())
							{
								nonstop += "departureAirport = '" + arrivalAirports[i] + "' ";
								oneStop += "f.departureAirport = '" + arrivalAirports[i] + "' ";
								twoStops += "f.departureAirport = '" + arrivalAirports[i] + "' ";
						//		out.println(nonstop);
								if(arrivalAirports[i+1] != null && !arrivalAirports[i+1].isBlank())
								{
									nonstop += "or ";
									oneStop += "or ";
									twoStops += "or ";
								}
								i++;
							}
							
							nonstop += ") and (";
							oneStop += ") and (";
							twoStops += ") and (";
							
							i = 0;
							while(departureAirports[i] != null && !departureAirports[i].isBlank())
							{
								nonstop += "arrivalAirport = '" + departureAirports[i] + "' ";
								oneStop += "g.arrivalAirport = '" + departureAirports[i] + "' ";
								twoStops += "h.arrivalAirport = '" + departureAirports[i] + "' ";
						
								if(departureAirports[i+1] != null && !departureAirports[i+1].isBlank())
								{
									nonstop += "or ";
									oneStop += "or ";
									twoStops += "or ";
								}
								i++;
							}
							nonstop += ") ";
							oneStop += ") ";
							twoStops += ") ";
							
								//IF EXACT DATES is selected
								if(dateType.equals("exact"))
								{
									
									nonstop += "and (day = ?)"; 
									oneStop += "and (f.day = ?)"; 
									twoStops += "and (f.day = ?)"; 
									
									
								}
								
							
							ps = con.prepareStatement(nonstop);
							
							ps1 = con.prepareStatement(oneStop);
							out.print(twoStops);
							ps2 = con.prepareStatement(twoStops);
							if(dateType.equals("exact")){
								day = (new DateParser()).getDay(toDate);
								ps.setInt(1,day);
								ps1.setInt(1, day);
								ps2.setInt(1, day);
							}
							
							flights = ps.executeQuery();
							
							
							flights1 = ps1.executeQuery();
							
							
					
							flights2 = ps2.executeQuery();
							
							out.print("<table> <tr> <td> Airlines </td>  <td> From </td>   <td> Departure </td> <td> To </td>  <td> Arrival </td> <td> duration </td><td> cost </td>  <td> Select </td></tr>");
							
							
							
							while(flights.next())
							{
								String arrivalDate = (new DateParser()).findArrivalDate(toDate,flights.getString("departureTime"),Long.valueOf(flights.getString("duration")));
								out.print("<tr>"
										
										+ "<td>" + flights.getString("airlineId") + "</td>"
										+ "<td>" + flights.getString("departureAirport") + "</td>"
										+ "<td><div>" + toDate +"</div><div>" + flights.getString("departureTime") + "</div></td>"
										+ "<td>" + flights.getString("arrivalAirport") + "</td>"
										+ "<td><div>" + arrivalDate + "</div>" + flights.getString("arrivalTime")+"</div></td>"
										+ "<td>" + flights.getString("duration") + "</td>"
										+ "<td>" + flights.getString("cost") + "</td>"
										+ "<td><form action='flightSearch.jsp'>"
										+ "<input type='hidden' name='select1' value='dummy'>"
										+ "<input type='hidden' name='selected2' value='" + flights.getString("airlineId") + flights.getString("flightNumber") + "'>"
										+ "<input type='hidden' name='date2' value='" + arrivalDate +"'>"
										+"<button type='submit' name='select2'>Select</button></form></td>"
										+ "</tr>"
										);
							}
							while(flights1.next())
							{
								
								
								
						//		out.print("<tr><td><table>");
								String arrivalDate = (new DateParser()).findArrivalDate(fromDate,flights1.getString("f.departureTime"),Long.valueOf(flights1.getString("f.duration")));
								
								
								 
								
								out.print("<tr>"
										
										+ "<td>" + flights1.getString("f.airlineId") + "</td>"
										+ "<td>" + flights1.getString("f.departureAirport") + "</td>"
										+ "<td><div>" + fromDate +"</div><div>" + flights1.getString("f.departureTime") + "</div></td>"
										+ "<td>" + flights1.getString("f.arrivalAirport") + "</td>"
										+ "<td><div>" + arrivalDate + "</div>" + flights1.getString("f.arrivalTime") +"</div></td>"
										+ "<td>" + flights1.getString("f.duration") + "</td>"
										+ "<td>" + flights1.getString("f.cost") + "</td>"
										+ "</tr>"
										);
							
								out.print("<tr>"
										
										+ "<td>" + flights1.getString("g.airlineId") + "</td>"
										+ "<td>" + flights1.getString("g.departureAirport") + "</td>"
										+ "<td><div>" + fromDate +"</div><div>" + flights1.getString("g.departureTime") + "</div></td>"
										+ "<td>" + flights1.getString("g.arrivalAirport") + "</td>"
										+ "<td><div>" + arrivalDate + "</div>" + flights1.getString("g.arrivalTime") +"</div></td>"
										+ "<td>" + flights1.getString("g.duration") + "</td>"
										+ "<td>" + flights1.getString("g.cost") + "</td>"
										+ "<td><form action='flightSearch.jsp'>"
										+ "<input type='hidden' name='connection1of2' value='" + flights1.getString("f.airlineId") + flights1.getString("f.flightNumber") + "'>"
										+ "<input type='hidden' name='connection2of2' value='" + flights1.getString("g.airlineId") + flights1.getString("g.flightNumber") + "'>"
										+ "<input type='hidden' name='date1of2' value='" + fromDate +"'>"
										+ "<input type='hidden' name='date2of2' value='" + arrivalDate +"'>"
										+"<button type='submit' name='book1stop'>Select</button></form></td>"
										+ "</tr>"
										);
							}
							
							
							while(flights2.next())
							{
								String arrivalDate = (new DateParser()).findArrivalDate(fromDate,flights2.getString("f.departureTime"),Long.valueOf(flights2.getString("f.duration")));
								out.print("<tr>"
										
										+ "<td>" + flights2.getString("f.airlineId") + "</td>"
										+ "<td>" + flights2.getString("f.departureAirport") + "</td>"
										+ "<td><div>" + fromDate +"</div><div>" + flights2.getString("f.departureTime") + "</div></td>"
										+ "<td>" + flights2.getString("f.arrivalAirport") + "</td>"
										+ "<td><div>" + arrivalDate + "</div>" + flights2.getString("f.arrivalTime") +"</div></td>"
										+ "<td>" + flights2.getString("f.duration") + "</td>"
										+ "<td>" + flights2.getString("f.cost") + "</td>"
										+ "</tr>"
										);
							
								out.print("<tr>"
										
										+ "<td>" + flights2.getString("g.airlineId") + "</td>"
										+ "<td>" + flights2.getString("g.departureAirport") + "</td>"
										+ "<td><div>" + fromDate +"</div><div>" + flights2.getString("g.departureTime") + "</div></td>"
										+ "<td>" + flights2.getString("g.arrivalAirport") + "</td>"
										+ "<td><div>" + arrivalDate + "</div>" + flights2.getString("g.arrivalTime") +"</div></td>"
										+ "<td>" + flights2.getString("g.duration") + "</td>"
										+ "<td>" + flights2.getString("g.cost") + "</td>"
										+ "</tr>"
										);
							
								out.print("<tr>"
									
										+ "<td>" + flights2.getString("h.airlineId") + "</td>"
										+ "<td>" + flights2.getString("h.departureAirport") + "</td>"
										+ "<td><div>" + fromDate +"</div><div>" + flights2.getString("h.departureTime") + "</div></td>"
										+ "<td>" + flights2.getString("h.arrivalAirport") + "</td>"
										+ "<td><div>" + arrivalDate + "</div>" + flights2.getString("h.arrivalTime") +"</div></td>"
										+ "<td>" + flights2.getString("h.duration") + "</td>"
										+ "<td>" + flights2.getString("h.cost") + "</td>"
										+ "<td><form action='flightSearch.jsp'>"
												+ "<input type='hidden' name='connection1of3' value='" + flights2.getString("f.airlineId") + flights2.getString("f.flightNumber") + "'>"
												+ "<input type='hidden' name='connection2of3' value='" + flights2.getString("g.airlineId") + flights2.getString("g.flightNumber") + "'>"
														+ "<input type='hidden' name='connection3of3' value='" + flights2.getString("h.airlineId") + flights2.getString("h.flightNumber") + "'>"
												+ "<input type='hidden' name='date1of3' value='" + fromDate +"'>"
												+ "<input type='hidden' name='date2of3' value='" + arrivalDate +"'>"
														+ "<input type='hidden' name='date3of3' value='" + arrivalDate +"'>"
										+"<button type='submit' name='book2stops'>Select</button></form></td>"
										+ "</tr>"
										);
							} 
							out.print("</table></p>");
							out.print("</table></p>");
						}
						else if(request.getParameter("bookButton") == null)
						{
							String[] flight2 = new String[5];
							flight2[0] = request.getParameter("selected2");
							String[] date2 = new String[5];
							date2[0] = request.getParameter("date2");
							out.print("<p> You Selected Returning Flight : " + flight2[0] + " for: " + date2[0] +"</p>" );
							(new Cookies()).flight2Cookies(flight2, date2);
						}
						else
						{
							out.print("<p> You Selected Returning Flight : " + ((new Cookies()).getFlight2Data())[0][0] + " for: " + ((new Cookies()).getFlight2Data())[0][1] +"</p>"  );
						}
					}
					
					if(request.getParameter("bookButton") == null){
						out.print("<p><form action='flightSearch.jsp'><button type='submit' name='bookButton'>Book</button></form></p>");
					}
					
					
					
				
					
				if(request.getParameter("bookButton") != null)
				{
					customerId = (new Cookies()).getCurrentUser();		
					
					
						String airline,fNumber;	
						String[][] flight;
						flight = (new Cookies()).getFlight1Data();
						airline = flight[0][0].substring(0,2);
						fNumber = flight[0][0].substring(2);
						
						String date = flight[0][1];
						
						
						
						String book = "insert into tickets (customerId, reservationDate, hasStops) values (?,?,?)";
						ps = con.prepareStatement(book);
						ps.setString(1,customerId);
						
						
						// HERE EDIT For demo
						ps.setString(2,reservationDate);
					
						ps.setString(3,"0");
						ps.executeUpdate();
						
						book = "select ticketNumber from tickets where customerId = ? and reservationDate = ?";
						
						ps = con.prepareStatement(book);
						ps.setString(1,customerId);
						ps.setString(2,reservationDate);
						
						ResultSet ticket = ps.executeQuery();
						
						int ticketNumber = 0;
						
						while(ticket.next())
						{
							ticketNumber = Integer.valueOf(ticket.getString("ticketNumber"));
						}
						
						
						
						book = "insert into fees values (?,?,?)";
						ps = con.prepareStatement(book);
						ps.setInt(1,ticketNumber);
						ps.setString(2,"Booking Fee");	
						ps.setString(3,"50.0");
						ps.executeUpdate();
						
						book = "insert into reservations values (?,?,?,?,?)";
						ps = con.prepareStatement(book);
						ps.setInt(1,ticketNumber);
						ps.setString(2,airline);
						ps.setString(3,fNumber);
						ps.setString(4,date);
						ps.setString(5, "0");
						ps.executeUpdate();
						if(tripType.equals("roundTrip"))
						{		
			
							flight = (new Cookies()).getFlight2Data();
							airline = flight[0][0].substring(0,2);
							fNumber = flight[0][0].substring(2);
							date = flight[0][1];
							book = "insert into reservations values (?,?,?,?,?)";
							
							ps.setInt(1,ticketNumber);
							ps.setString(2,airline);
							ps.setString(3,fNumber);
							ps.setString(4,date);
							ps.setString(5, "0");
							ps.executeUpdate();
						}
						
					
					
					
				}
				if(request.getParameter("bookButton") != null){
					out.println("<p> You have successfully booked the flight(s)! <a href='customersHome.jsp?reservations='my'>Click Here</a> to view your reservations!");
				}
				
				
				
				/*
		
				
				
				date1 = LocalDate.parse(fromDate);
				day1 = ((date1.getDayOfWeek()).toString()).toLowerCase();
				
				
				
				
				
				
				
				
			
				
				
				
			
			
				
					if(request.getParameter("sortButton") == null)
					{	
						if(dateType.equals("exact"))
						{
							oneWay = "select * from flights " 
								 + "join operationDays " 
								 + "using(airlineId,flightNumber) "
								 + "where departureAirport = ? and arrivalAirport= ? and day= ?";
						}
						else
						{
							oneWay = "select * from flights " 
									 + "join operationDays " 
									 + "using(airlineId,flightNumber) "
									 + "where departureAirport = ? and arrivalAirport= ? and day between ? and ?";
						}
					}
					else
					{
						String sortCategory = request.getParameter("sortCategory");
						
						if(dateType.equals("exact"))
						{
							oneWay = "select * from flights " 
									 + "join operationDays " 
									 + "using(airlineId,flightNumber) "
									 + "where departureAirport = ? and arrivalAirport= ? and day= ?"
									 + "order by " + sortCategory;
						}
						else
						{
							oneWay = "select * from flights " 
									 + "join operationDays " 
									 + "using(airlineId,flightNumber) "
									 + "where departureAirport = ? and arrivalAirport= ? and day between ? and ?"
									 + "order by " + sortCategory;
						}
					}
					
					LocalDate flex1 = date1.minusDays(3);
					LocalDate flex2 = date1.plusDays(3);
					
					String fday1 = ((flex1.getDayOfWeek()).toString()).toLowerCase();
					String fday2 = ((flex2.getDayOfWeek()).toString()).toLowerCase();
					
					ps = con.prepareStatement(oneWay);
					ps.setString(1, departureAirport);
					ps.setString(2, arrivalAirport);
					if(dateType.equals("flexible"))
					{
						ps.setString(3, fday1);
						ps.setString(4, fday2);
						
					}
					else{
						ps.setString(3,day1);
					}
					flights = ps.executeQuery();
			
				
				
					
					out.print("<p><form action='flightSearch.jsp'>Sort By<select name='sortCategory'><option value='cost ASC'>Price(Low-High)</option>"
							+"<option value='cost DESC'>Price(High-Low)</option><option value='departureTime ASC'>Take Off(Early-Late)</option>"
							+"<option value='departureTime DESC'>Take Off(Late-Early)</option><option value='arrivalTime ASC'>Landing(Early-Late)</option>"
							+"<option value='arrivalTime DESC'>Landing(Late-Early)</option><option value='arrivalTime - departureTime ASC'>Duration(Short-Long)</option>"
							+"<option value='arrivalTime - departureTime DESC'>Duration(Long-Short)</option></select>"
							+ "<input type='hidden' name='tripType' value='" + tripType + "'>" 
							+ "<input type='hidden' name='departureAirport' value='" + departureAirport + "'>"
							+ "<input type='hidden' name='arrivalAirport' value='" + arrivalAirport + "'>"
							+ "<input type='hidden' name='fromDate' value='" + fromDate + "'>"
							);
							if(tripType.equals("roundTrip"))
							{
								toDate = request.getParameter("toDate");
								out.print("<input type='hidden' name='toDate' value='" + toDate + "'>");					
							}
					
							out.print("<input type='hidden' name='dateType' value='" + dateType + "'>"
							+ "<input type='hidden' name='passengers' value='" + passengers + "'>"
							+ "<input type='hidden' name='classType' value='" + classType + "'>"
							+"\t"+"<input type='submit' name='sortButton' value='sort'></form></p>");
					
					
							
							
							
							
							
					//RESULTING FLIGHTS		
					
					
					out.print("<p>Departing Flights:");
					
					out.print("<br><table>"
							+ "<tr>"
							+ "<td>Select</td>"
							+ "<td>From</td>"
							+ "<td>Departure</td>"
							+ "<td>To</td>"
							+ "<td>Arrival</td>"
							+ "<td>Airlines</td>"
							+ "<td>Cost</td>"
							+ "</tr>");
			
					while(flights.next())
					{
						out.print("<form action='flightSearch.jsp'>");
						
						out.print("<input type='hidden' name='customerId' value='" + request.getParameter("customerId") +"'>");
						out.print("<tr>"
								+ "<input type='hidden' name='customerId' value='" + request.getParameter("customerId") + "'>"
								+ "<td><input type='hidden' name='flightDetails' value='"+flights.getString("airlineId")+flights.getString("flightNumber")+"'>"
								+ "<input type='hidden' name='date' value='" + fromDate + "'>"
								+ "<input type='submit' name = 'bookButton' value='Book' required></td>"
								+ "<td>" + flights.getString("departureAirport") + "</td>" //
								+ "<td>" + flights.getString("departureTime") + "</td>"
								+ "<td>" + flights.getString("arrivalAirport") + "</td>"
								+ "<td>" + flights.getString("arrivalTime") + "</td>"
								+ "<td>" + flights.getString("airlineId") + "</td>"
								+ "<td>" + flights.getString("cost") + "</td>"
								+ "</tr>");
						out.print("</form>");
					}
					out.print("</table>");
					out.print("</p>");
					
					
						
						
				if(tripType.equals("roundTrip"))
				{
					toDate = request.getParameter("toDate");
					date2 = LocalDate.parse(toDate);
					day2 = ((date2.getDayOfWeek()).toString()).toLowerCase();
					
					
					 flex1 = date2.minusDays(3);
					 flex2 = date2.plusDays(3);
					
					 fday1 = ((flex1.getDayOfWeek()).toString()).toLowerCase();
					 fday2 = ((flex2.getDayOfWeek()).toString()).toLowerCase();
					
					ps = con.prepareStatement(oneWay);
					ps.setString(1, arrivalAirport);
					ps.setString(2, departureAirport);
					if(dateType.equals("flexible"))
					{
						ps.setString(3, fday1);
						ps.setString(4, fday2);
						
					}
					else{
						ps.setString(3,day2);
					}
					
					
					
					
					
					flights = ps.executeQuery();
					out.print("<p>Returning Flights:");
					out.print("</p>");
					out.print("<br><table>"
							+ "<tr>"
							+ "<td>Book</td>"
							
							+ "<td>From</td>"
							+ "<td>Departure</td>"
							+ "<td>To</td>"
							+ "<td>Arrival</td>"
							+ "<td>Airlines</td>"
							+ "<td>Cost</td>"
							+ "</tr>");
					while(flights.next())
					{
						out.print("<form action='flightSearch.jsp'>");
						
						out.print("<input type='hidden' name='customerId' value='" + request.getParameter("customerId") +"'>");
						out.print("<tr>"
								+ "<td><input type='hidden' name='flightDetails' value='"+flights.getString("airlineId")+flights.getString("flightNumber")+"'>"
								+ "<input type='hidden' name='date' value='" + request.getParameter("toDate") + "'>"
								+ "<input type='submit' name = 'bookButton' value='Book' required></td>"
								+ "<td>" + flights.getString("departureAirport") + "</td>"
								+ "<td>" + flights.getString("departureTime") + "</td>"
								+ "<td>" + flights.getString("arrivalAirport") + "</td>"
								+ "<td>" + flights.getString("arrivalTime") + "</td>"
								+ "<td>" + flights.getString("airlineId") + "</td>"
								+ "<td>" + flights.getString("cost") + "</td>"
								+ "</tr>");
						out.print("</form>");
					}
					out.print("</table>");
					
				
					
				}
				
				
				*/
				
	}
	catch(Exception ex)
	{
		out.print(ex);
		out.print("insert failed");
	}
				
				
			
		%>
		
	
	
	</body>

</html>