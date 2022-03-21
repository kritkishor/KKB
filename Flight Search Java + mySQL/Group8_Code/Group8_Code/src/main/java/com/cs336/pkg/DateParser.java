package com.cs336.pkg;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;

public class DateParser {

	protected static final String[] days = {"sunday","monday","tuesday","wednesday","thursday","friday","saturday"};
	public String getDay(int day)
	{
			return(days[day-1]);
	}
	public int getDay(String day)
	{
		
		if(day.contains("-"))
		{
			LocalDate date = LocalDate.parse(day);
			return getDay(date.getDayOfWeek().toString());
		}
		for(int i=0; i<7; i++)
		{
			if(day.toLowerCase().equals(days[i]))
			{
				return i+1;
			}
		}
		return 0;
	}
	public String getDateTimeNow()
	{
		String res = LocalDateTime.now().toString();
		res = res.replace('T', ' ');
		res = res.substring(0,19);
		return res;
	}
	public String getDateNow()
	{
		return LocalDate.now().toString();
	}
	public String addDays(String date, int days )
	{
		LocalDate thisDate = LocalDate.parse(date);
		thisDate.plusDays(days);
		return thisDate.toString();
	}
	public String findArrivalDate(String date, String departureTime,String arrivalTime,String hours)
	{
		LocalDateTime d = LocalDateTime.parse(date+"T"+departureTime);
		d.plusHours(Integer.valueOf(hours.substring(0,2)));
		
		return null;
	}
	public String findArrivalDate(String date,String time,long duration)
	{
		LocalDateTime d = LocalDateTime.parse(date+"T"+time);
		LocalDateTime res = d.plusHours(duration);
		
		return res.toLocalDate().toString();
	}
	public float findDuration(String d,String a,int p)
	{
		/*
		LocalTime dt = LocalTime.parse(d);
		LocalTime at = LocalTime.parse(a);
		
		
		LocalTime res = at.minusHours(dt.getHour());
		res = res.minusMinutes(dt.getMinute());
		res = res.plusHours(p*24);
		
		return res.toString();
		*/
		int h1 = Integer.valueOf(d.substring(0,2));
		int m1 = Integer.valueOf(d.substring(3,5));
		int h2 = Integer.valueOf(a.substring(0,2));
		int m2 = Integer.valueOf(a.substring(3,5));
		
		float res = (h2-h1) + (m2-m1)/60 + p*24;
		
		return Math.abs(res);
		
	
		
	}
	
	
	public boolean compareDates(String date1,String time1,String date2)
	{
		LocalDateTime fromDateTime = LocalDateTime.parse(date1 + "T" + time1);
		LocalDateTime toDateTime = LocalDateTime.parse(date2);
		LocalDateTime tempDateTime = LocalDateTime.from( fromDateTime );
		
		long years = tempDateTime.until( toDateTime, ChronoUnit.YEARS );
		if(years > 0 )
		{
			return true;
		}
		tempDateTime = tempDateTime.plusYears( years );

		long months = tempDateTime.until( toDateTime, ChronoUnit.MONTHS );
		if(months > 0)
		{
			return true;
		}
		
		tempDateTime = tempDateTime.plusMonths( months );

		long days = tempDateTime.until( toDateTime, ChronoUnit.DAYS );
		if(days > 0) {
			return true;
		}
		tempDateTime = tempDateTime.plusDays( days );


		long hours = tempDateTime.until( toDateTime, ChronoUnit.HOURS );
		if(hours > 0)
		{
			return true;
		}
		tempDateTime = tempDateTime.plusHours( hours );

		long minutes = tempDateTime.until( toDateTime, ChronoUnit.MINUTES );
		if(minutes > 30)
		{
			return true;
		}
		tempDateTime = tempDateTime.plusMinutes( minutes );

		long seconds = tempDateTime.until( toDateTime, ChronoUnit.SECONDS );
		
		
		
		
		return false;
	}
}
