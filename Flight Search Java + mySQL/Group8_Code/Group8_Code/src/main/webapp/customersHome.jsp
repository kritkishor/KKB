<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1" import="com.cs336.pkg.*"%>
<%@ page import="java.io.*,java.util.*,java.sql.*"%>
<%@ page import="javax.servlet.http.*,javax.servlet.*" %>


<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0-beta3/css/all.min.css">
<link href="https://fonts.googleapis.com/css?family=PT+Sans:400" rel="stylesheet">

<link type="text/css" rel="stylesheet" href="customersHome.css" />
<link href="https://cdn.jsdelivr.net/npm/remixicon@2.5.0/fonts/remixicon.css" rel="stylesheet">
	
	<head>
	<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
	
	

	<title>Welcome</title>
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
	
	
	<div class="side-bar">
			<a href="" name="accountLink">My Account</a>
			<a href="customersHome.jsp">Flight Search</a>
			<a href="" name="messagesLink">Messages</a>		
			<%
				out.print("<a href='customersHome.jsp?reservations='my'>Reservations</a>");
			%>
			
			<a href="customersHome.jsp?faqsLink='faqs'" >FAQs</a>
			<a href="login.jsp" >Logout</a>
			
	</div>
	
	<%
	//Get the database connection
	try {
		String user = (new Cookies()).getCurrentUser();
		
		ApplicationDB db = new ApplicationDB();	
		Connection con = db.getConnection();

		PreparedStatement ps;
	
		//Create a SQL statement
		Statement stmt = con.createStatement();
		ResultSet faqs;
		
		String customerId = user;
		out.print("<div class='dashboard-items'><p>Logged in as: " + user + "</p></div>");
		// FAQS
		if(request.getParameter("searchByKeyword") != null || request.getParameter("faqsLink") != null )
		{
			out.print("<p><div class='dashboard-items'> <h1> FAQS: </h1>");
			out.print("<form action='customersHome.jsp'>"
					+ "<div class='rows'>"
					+ "<div class='elements'><label for='searchKeyword'>Keyword: </label><input type='text' name='searchKeyword'></div>"
					+ "<div class='elements'><input type='submit' name='searchByKeyword' value='Search By Keyword'></div>"
					+ " </form></div></p>");
			if(request.getParameter("searchByKeyword") != null){
				String keyword = request.getParameter("searchKeyword");
				faqs = stmt.executeQuery("select * from faqs where question like '%" + keyword +"%' or answer like '%" + keyword +"%'" );
				out.print("<div class='rows'> <p><b>Keyword Used: </b> '" + keyword + "'</p></div>");
			}
			
			else
			{
						faqs = stmt.executeQuery("select * from faqs");
			}
			
			
			while(faqs.next())
			{
				out.print("<p>"
						+ "<div class='rows'> <b> Q: </b> " + faqs.getString("question") + "</div>"
						+ "<div class='rows'> <b> A: </b>" + faqs.getString("answer") + "</div>"
						+ "</p>"
					);
			}
			
			out.print("<form action='customersHome.jsp' name='questionForm'>"
					+ "<div class='rows'><label for='Question'>Question: </label><br><textarea form='questionForm' rows='10' cols='50' name='question'>Enter your question</textarea><br></div>"
					+ "<div class='rows'><input type='submit' name='submitQuestion' value='Submit'></div>"
					+ " </form></p>");
			
						return;
		}
		
		if(request.getParameter("submitQuestion") != null && !request.getParameter("question").isBlank())
		{
			String question = request.getParameter("question");
			
			ps = con.prepareStatement("insert into faqs (question) values (?)");
			ps.setString(1,question);
			ps.executeUpdate();
			out.println("<p style='background-color:green;color:black;left:50%;'>Success!</p>");	
			
		}
		if(request.getParameter("reservations") != null)
		{
			String query = "select * from tickets join reservations using (ticketNumber) join flights using(airlineId,flightNumber) join fees using (ticketNumber) where customerId = ? ";
			String upcoming = query + "and flightDate >= ?";
			ps = con.prepareStatement(upcoming);
			ps.setString(1,customerId);
			String datenow = (new DateParser()).getDateNow();
			ps.setString(2,datenow);
			ResultSet results = ps.executeQuery();
			out.print("<div class='dashboard-items'><p><b>Upcoming Reservations</b><div class='rows'><table><tr><td>TicketNo</td><td>Reservation Date</td><td>Flight Date</td><td>Flight</td><td>From</td><td>Departure</td><td>To</td><td>Arrival</td>"
					+ "<td>Duration(Hours)</td><td>Fare</td><td>Fees</td></tr>");
			while(results.next())
			{
				out.print("<tr>"
						+ "<td>" + results.getString("ticketNumber") + "</td>"
						+ "<td>" + results.getString("reservationDate") + "</td>"
						+ "<td>" + results.getString("flightDate") + "</td>"
						+ "<td>" + results.getString("airlineId") + results.getString("flightNumber") + "</td>"
						+ "<td>" + results.getString("departureAirport") + "</td>"
						+ "<td>" + results.getString("departureTime") + "</td>"
						+ "<td>" + results.getString("arrivalAirport") + "</td>"
						+ "<td>" + results.getString("arrivalTime") + "</td>"
						+ "<td>" + results.getString("Duration") + "</td>"
						+ "<td>" + results.getString("cost") + "</td>"
						+ "<td> <div>" + results.getString("description") + "</div><div>" +results.getString("fee") + "</div></td>"
						);
			}
			out.print("</table></div>");
			String past = query + "and flightDate < ?";
			ps = con.prepareStatement(past);
			ps.setString(1,customerId);	
			ps.setString(2,datenow);
			results = ps.executeQuery();
			
			out.print("<p><b>Past Reservations</b><div class='rows'><table><tr><td>TicketNo</td><td>Reservation Date</td><td>Flight Date</td><td>Flight</td><td>From</td><td>Departure</td><td>To</td><td>Arrival</td>"
					+ "<td>Duration(Hours)</td><td>Fare</td><td>Fees</td></tr>");
			while(results.next())
			{
				out.print("<tr>"
						+ "<td>" + results.getString("ticketNumber") + "</td>"
						+ "<td>" + results.getString("reservationDate") + "</td>"
						+ "<td>" + results.getString("flightDate") + "</td>"
						+ "<td>" + results.getString("airlineId") + results.getString("flightNumber") + "</td>"
						+ "<td>" + results.getString("departureAirport") + "</td>"
						+ "<td>" + results.getString("departureTime") + "</td>"
						+ "<td>" + results.getString("arrivalAirport") + "</td>"
						+ "<td>" + results.getString("arrivalTime") + "</td>"
						+ "<td>" + results.getString("Duration") + "</td>"
						+ "<td>" + results.getString("cost") + "</td>"
						+ "<td> <div>" + results.getString("description") + "</div><div>" +results.getString("fee") + "</div></td></div>"
						);
			}
			out.print("</table></div>");
			return;
		}
		
	}
	catch(Exception ex)
	{
		out.print(ex);
		out.print("exception");
	}
	
	%>
	
		<div class="main-display">
		
				
				<div class="dashboard-items">
					
					<p> Search Flights: </p>
					<form action="flightSearch.jsp">
					<%
							out.print("<input type='hidden' name='customerId' value='" + request.getParameter("username") +"'>");
					%>
					<input type="hidden" value="${customerId}" name="customerId">
					
					<div class="booking-form">
					<div class="rows">
						<div class="elements">
							<input type="radio" id="roundTrip" name="tripType" value="roundTrip" required>
							<label for="tripType">Round Trip</label>
						</div>
						<div class="elements">
							<input type="radio" id="oneWay" name="tripType" value="oneWay">
							<label for="tripType">One Way</label>
						</div>	
					</div>
					<div class="rows">
						<div class="elements">
							<label for="departureAirport">From</label>
							<input type="text" name="departureAirport" required>
						</div>
						<div class="elements">
							<label for="arrivalAirport">To</label>
							<input type="text" name="arrivalAirport" required>
						</div>
					</div>
					
					<div class="rows">
						<div class="elements">
							<label for="fromDate">Date1</label>
							<input type="date" name="fromDate" required>
						</div>
						<div class="elements">
							<label for="toDate">Date2</label>
							<input type="date" name="toDate">
						</div>
				
					</div>
					<div class="rows">
						<div class="elements">
							<input type="radio" id="dateType" name="dateType" value="exact" required>
							<label for="dateType">Exact Dates</label>
						</div>
						<div class="elements">
							<input type="radio" id="dateType" name="dateType" value="flexible">
							<label for="dateType"> + - 3 days</label>
						</div>	
					</div>
					<div class="rows">
						<div class="elements">
							<label for="passengers">No. Of Passengers</label>
							<select name="passengers">
								<option>1</option>
								<option>2</option>
								<option>3</option>
								<option>4</option>
								<option>5+</option>
							</select>
						</div>
						<div class="elements">
							<label for="classType">Class</label>
							<select name="classType">
								<option>Economy</option>
								<option>First</option>
								<option>Business</option>
							</select>
						</div>
				</div>
				<div class="rows">
					<button class="" name="searchButton"><i class="fa-solid fa-magnifying-glass"></i><span style="font-size:18px">Search</span></button>
				</div>
		
	
			</div>
			</form>
		</div>
	
		
		
	</div>
</body>

</html>