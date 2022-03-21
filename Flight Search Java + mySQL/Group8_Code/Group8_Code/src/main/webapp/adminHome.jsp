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
		<title>Login</title>
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

	<h2>Administration</h2>
	
	
	<%
	try{
		//Get the database connection
		ApplicationDB db = new ApplicationDB();	
		Connection con = db.getConnection();

		PreparedStatement ps;
		//Create a SQL statement
		Statement stmt = con.createStatement();
		String str;
		ResultSet reservations;
		
		if(request.getParameter("reservations") != null)
		{
			
			
			str = "select * from reservations join tickets using (ticketNumber) join fees using (ticketNumber) join customers c on (c.id =customerId) ";
			
		
			
			
			if(request.getParameter("searchN") != null){
				str += "where name like '%" + request.getParameter("keyword") + "%'";
				ps = con.prepareStatement(str);
				reservations = ps.executeQuery();
			}
			else if(request.getParameter("searchF") != null){
				str += "where airlineId = ? and flightNumber = ?";
				ps = con.prepareStatement(str);
				ps.setString(1,(request.getParameter("keyword")).substring(0,2));
				ps.setString(2,(request.getParameter("keyword")).substring(2));
				reservations = ps.executeQuery();
			}
			else
			{
				reservations = stmt.executeQuery(str);
			}
			out.print("<form action='adminHome.jsp'><input type = 'hidden' name='reservations'> <label> Name </label> <input type='text' name='keyword'><input type='submit' name='searchN' value='search'></form>");
			
			out.print("<form action='adminHome.jsp'><input type = 'hidden' name='reservations'> <label> Flight Number </label> <input type='text' name='keyword'><input type='submit' name='searchF' value='search'></form>");
			out.print("<table><tr><td>Customer</td><td>Reservation Date</td><td>ticketNumber</td><td>Flight</td><td>FlightDate</td><td>Fees Collected</td></tr>");
			while(reservations.next())
			{
				out.print("<tr>"
						+ "<td>" + reservations.getString("customerId") + ": " + reservations.getString("name") + "</td>"
						+ "<td>" + reservations.getString("reservationDate") + "</td>"
						+ "<td>" + reservations.getString("ticketNumber") + "</td>"
						+ "<td>" + reservations.getString("airlineId") + reservations.getString("FlightNumber")+ "</td>"
						+ "<td>" + reservations.getString("flightDate") + "</td>"
						+ "<td>" + reservations.getString("fee") + "</td>"
						+ "</tr>"
						);
			}
			out.print("</table>");
			return;
			
		}
		if(request.getParameter("reports") != null)
		{
			str = "select count";
		}
		
	}
	catch (Exception ex)
	{
		out.print(ex);
	}
	%>
	<br>
    	<div class="box">
      		<div>
      			<form action="manageCustomers.jsp">
      				<button type="submit" class="buttons" name="customersButton"> <i class="fa-solid fa-users"></i><span>Customers</span></button>
      			</form>		
      		</div>
      		<div>
      			<form action="manageReps.jsp">
      				<button class="buttons" name="repsButton"><i class="ri-customer-service-2-fill"></i><span style="font-size:20px">Employees</span></button>
      			</form>
      		</div>
      		<div>
      			<form action="adminHome.jsp">
      				<button class="buttons" name="reports"><i class="ri-file-chart-fill"></i><span style="font-size:20px">Reports</span></button>
      			</form>
      		</div>
      		<div>
      			<form action="adminHome.jsp">
      				<button type="submit" class="buttons" name="reservations"> <i class="fa-solid fa-users"></i><span>Reservations</span></button>
      			</form>		
      		</div>
    	</div>
    	
    	
    
</body>
</html>