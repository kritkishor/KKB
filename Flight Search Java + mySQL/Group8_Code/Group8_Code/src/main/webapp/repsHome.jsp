<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1" import="com.cs336.pkg.*"%>
<%@ page import="java.io.*,java.util.*,java.sql.*"%>
<%@ page import="javax.servlet.http.*,javax.servlet.*" %>


<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0-beta3/css/all.min.css">
<link href="https://cdn.jsdelivr.net/npm/remixicon@2.5.0/fonts/remixicon.css" rel="stylesheet">
<link type="text/css" rel="stylesheet" href="adminHome.css" />
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
		<title>Home</title>
	</head>
	
	<body bgcolor=yellow>
	<table>
		<tr>
			
			<td style="padding:0 100px 0 15px;">
				<h1> Customer Representative</h1>
			</td>
			<td>
				<form action="welcome.jsp">
					<input type="submit" value="LOGOUT">
				</form>
				
			</td>
		</tr>
	</table>	
	<%
	
	try{
		String user = (new Cookies()).getCurrentUser();
		
		ApplicationDB db = new ApplicationDB();	
		Connection con = db.getConnection();

		PreparedStatement ps;
	
		//Create a SQL statement
		Statement stmt = con.createStatement();
		ResultSet faqs;
		
		if(request.getParameter("reservations") != null)
		{
			out.print("<p> ADD a Reservation: </p>");
			out.print("<form action='repsHome.jsp'><label>CustomerId</label><input type='text' name='customerId'><label>AirlineID</label><input type='text' name='airlineId'><label>"
					+ "FlightNumber</label><input type='text' name='flightNumber'><label>Date</label><input type='text' name='date'><input type='submit' name='book' value='book'></form>"
					);
			return;
			
		}
		
		
		if(request.getParameter("book") != null)
		{
			String customerId = request.getParameter("customerId");	
			
			String reservationDate = (new DateParser()).getDateNow();
			String airline = request.getParameter("airlineId");
			String fNumber = request.getParameter("flightNumber");	
			
			
			String date = request.getParameter("date");
			
			
			
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
			
		}
		String customerId = user;
		out.print("<div class='dashboard-items'><p>Logged in as: " + user + "</p></div>");
		if(request.getParameter("faqsButton") != null)
		{
			out.print("<p><div class='dashboard-items'> <h1> FAQS: </h1>");
			out.print("<form action='repsHome.jsp'>"
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
						+ "<form action='repsHome.jsp'>"
						+ "<div class='rows'> <b> Q: </b> " + faqs.getString("question") 
						+ "<div class= 'elements'><button type='submit' name='answerQuestion'>Update</button></div>"
								+ "</div>"
						+ "<div class='rows'> <b> A: </b>" + faqs.getString("answer") + "</div>"
						+ "<input type='hidden' name='faqId' value='" + faqs.getString("faqId") + "'>"
						
						+"</form>"
						+ "</p>"
					);
			}
			return;
			
		}
		if(request.getParameter("answerQuestion") != null)
		{
			faqs = stmt.executeQuery("select * from faqs where faqId = '" + request.getParameter("faqId") + "'");
 			while(faqs.next())
 			{	out.print("<form action='repsHome.jsp' name='answerForm'>"
					+ "<div class='rows'><label for='Question'><b>Question: </b></label>"
					+ faqs.getString("question") + "</div>"
					+ "<input type='hidden' name='faqId' value='" + request.getParameter("faqId") +"'>"
					+ "<div class='rows'><textarea form='answerForm' rows='10' cols='50' name='answer'>Enter your answer</textarea><br></div>"
					+ "<div class='rows'><input type='submit' name='submitAnswer' value='Submit'>"
					+ "<div class='elements'><input type='submit' name='deleteQuestion' value='Delete this Question'></div></div>"
					+ " </form></p>");
					
	
 			}
 			return;	
		}
		if(request.getParameter("submitAnswer") != null && !request.getParameter("answer").isBlank())
		{
			String answer = request.getParameter("answer");
			
			ps = con.prepareStatement("update faqs set answer = ? where faqId = ?");
			ps.setString(1,answer);
			ps.setString(2,request.getParameter("faqId"));
			ps.executeUpdate();
		
			out.println("<div class='dashboard-items'><p style='background-color:green;color:black;left:50%;'>Success!</p></div>");	
			
			out.println("<form action='repsHome.jsp'><div class='dashboard-items'><input type='submit' name='faqsButton' value='Thank You!'></div></form>");
			return;
		}
		
		if(request.getParameter("deleteQuestion") != null )
		{
			String faqId = request.getParameter("faqId");
			
			ps = con.prepareStatement("delete from faqs where faqId = ?");
			ps.setString(1,faqId);
			ps.executeUpdate();
		
			out.println("<div class='dashboard-items'><p style='background-color:green;color:black;left:50%;'>Success!</p></div>");	
			
			out.println("<form action='repsHome.jsp'><div class='dashboard-items'><input type='submit' name='faqsButton' value='Thank You!'></div></form>");
			return;
		}
			
		
	}
	catch(Exception ex)
	{
		out.print("crashed because: " + ex);
	}
		
	%>
	
	<div class="box">
      		<div>
      			<form action="airports.jsp">
      				<button class="buttons" name="airportsButton"><i class="fa-solid fa-passport"></i><span>Airports</span></button>
      			</form>
      		</div>
      		<div>
      			<form action="airlines.jsp">
      				<button class="buttons" name="airlinesButton"><i class="fa-solid fa-cart-flatbed-suitcase"></i><span>Airlines</span></button>
      			</form>
      		</div>
      		<div>
      			<form action="aircrafts.jsp">
      				<button type="submit" class="buttons" name="aircraftsButton"> <i class="fa-solid fa-plane"></i><span>Aircrafts</span></button>
      			</form>		
      		</div>
      		
      		<div>
      			<form action="flights.jsp">
      				<button class="buttons" name="flightsButton"><i class="fa-solid fa-plane-departure"></i><span>Flights</span></button>
      			</form>
      		</div>
      		<div>
      			<form action="repsHome.jsp">
      				<button type="submit" class="buttons" name="reservations"> <i class="fa-solid fa-users"></i><span>Reservations</span></button>
      			</form>		
      		</div>
      		<div>
      			<form action="repsHome.jsp">
      				<button class="buttons" name="faqsButton"><i class="fa-solid fa-circle-question"></i><span>FAQs</span></button>
      			</form>
      		</div>
    	</div>
	
	
	
	
	<br>
	<input type="submit" value="Client List">
	<br>
	<br>
	<input type="submit" value="Flight Search">
	<br>
	<br>
	<input type="submit" value="Messaging">
	
	<br>
	<input type="submit" value="Lookup waiting list">
	
	
	</body>

</html>