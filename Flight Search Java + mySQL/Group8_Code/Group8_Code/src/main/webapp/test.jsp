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
		
		String[][] search = (new Cookies()).getSearchData();
		
		String tripType = search[0][0];
		String[] departureAirports = new String[50];
		int i = 0;
		while(search[1][i] != null && !search[1][i].isBlank())
		{
			departureAirports[i] = search[1][i];
			i++;
		}
		i = 0;
		String[] arrivalAirports = new String[50];
		while(search[2][i] != null && !search[2][i].isBlank())
		{
			arrivalAirports[i] = search[2][i];
			i++;
		}
		String fromDate = search[3][0];
		String toDate = search[4][0]; 
		String dateType = search[5][0]; 
		String passengers = search[6][0]; 
		String classType = search[7][0]; 
		String departureAirport = search[1][0];
		String arrivalAirport = search[2][0];
		
		
			out.println(tripType);
			out.println(fromDate);
			out.println(toDate);
			out.println(dateType);
			out.println(passengers);
			out.println(classType);
			i=0;
			while(departureAirports[i]!=null && !departureAirports[i].isBlank() )
			{
				out.print(departureAirports[i]);
				i++;
			}
			i=0;
			while(arrivalAirports[i]!=null && !arrivalAirports[i].isBlank() )
			{
				out.print(arrivalAirports[i]);
				i++;
			}
	}
	catch(Exception ex)
	{
		out.print(ex);
		out.print("insert failed");
	}
				
				
			
		%>
		
	
	
	</body>

</html>