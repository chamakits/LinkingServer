package edu.uprm.capstone.areatech.linkingserver.sandbox;

import java.sql.Time;
import java.text.DateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class CalendarPlaying 
{
	
	public static void main(String[] args) 
	{
		
		Calendar calendar = new GregorianCalendar(1987,5+1,25,11,30,25);
		
		StringBuilder totalMessageBuilder = new StringBuilder("");
		
		totalMessageBuilder.append(calendar.get(Calendar.YEAR));
		totalMessageBuilder.append(",");
		totalMessageBuilder.append(calendar.get(Calendar.MONTH));
		totalMessageBuilder.append(",");
		totalMessageBuilder.append(calendar.get(Calendar.DAY_OF_MONTH));
		totalMessageBuilder.append(";");
		
		totalMessageBuilder.append(calendar.get(Calendar.HOUR));
		totalMessageBuilder.append(",");
		totalMessageBuilder.append(calendar.get(Calendar.MINUTE));
		totalMessageBuilder.append(",");
		totalMessageBuilder.append(calendar.get(Calendar.SECOND));
//		stringBuilder.append(",");
		
		System.out.println(totalMessageBuilder.toString());
		
		Time time = new Time(calendar.getTime().getTime());
		
		System.out.println(calendar.getTime().getTime());
		System.out.println(calendar.get(Calendar.YEAR));
		System.out.println(calendar.get(Calendar.MONTH));
		System.out.println(calendar.get(Calendar.DAY_OF_MONTH));
		
		
		
		
		System.out.println(time);
		
		
	}

}
