package edu.uprm.capstone.areatech.linkingserver.utilities;

import java.util.Calendar;
import java.util.GregorianCalendar;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.uprm.capstone.areatech.linkingserver.connection.PreviousConnectionProcessor;

public class GPSParser 
{
	
	public final static int LAT_2_INTEGER_AMOUNT=2;
	public final static int LAT_POINT_AMOUNT=1;
	public final static int LAT_REST_AMOUNT=6;
	public final static int NORTH_SOUTH_AMOUNT=1;
	
	public final static int LONG_3_INTEGER_AMOUNT=3;
	public final static int LONG_POINT_AMOUNT=1;
	public final static int LONG_REST_AMOUNT=6;
	public final static int EAST_WEST_AMOUNT=1;
	
	
	public final static int DAY_AMOUNT =2;
	public final static int MONTH_AMOUNT = 2;
	public final static int YEAR_AMOUNT=2;
	
	public final static int HOUR_AMOUNT=2;
	public final static int MINUTE_AMOUNT=2;
	public final static int SECOND_AMOUNT=2;
	
	public final static int CURRENT_MILLENIA=2000;
	
	public final static int TOTAL_CHARACTER_AMOUNT= 
		LAT_2_INTEGER_AMOUNT+ 
		LAT_POINT_AMOUNT+
		LAT_REST_AMOUNT+ 
		NORTH_SOUTH_AMOUNT+
		
		LONG_3_INTEGER_AMOUNT+
		LONG_POINT_AMOUNT+
		LONG_REST_AMOUNT+
		EAST_WEST_AMOUNT+
		
		DAY_AMOUNT+
		MONTH_AMOUNT+
		YEAR_AMOUNT+
		
		HOUR_AMOUNT+
		MINUTE_AMOUNT+
		SECOND_AMOUNT
		;
	
	final static Logger logger = LoggerFactory.getLogger(GPSParser.class);
	
	public static String convertGPSMessage(String gpsMessage)
	{
		if(gpsMessage.equalsIgnoreCase("NONE"))
		{
			gpsMessage="0000.0000S00000.0000W000000000000";
		}
		int characterIndex =0;
		StringBuilder totalMessageBuilder= new StringBuilder("");
		int multiplier=1;
		
		int lat2Integer =Integer.valueOf(
			gpsMessage.substring(characterIndex,LAT_2_INTEGER_AMOUNT));
		characterIndex+=LAT_2_INTEGER_AMOUNT;
		logger.debug("lat2Integer:"+lat2Integer);
		
		double latRest = Double.valueOf(
				gpsMessage.substring(characterIndex,characterIndex+LAT_REST_AMOUNT+LAT_POINT_AMOUNT
						));
		characterIndex+=LAT_REST_AMOUNT+LAT_POINT_AMOUNT;
		logger.debug("latRest:"+latRest);
		
		logger.debug("characterIndex:"+characterIndex);
		logger.debug("N_S_AMOUNT:"+NORTH_SOUTH_AMOUNT);
		logger.debug("gpsMessageLength:"+gpsMessage.length());
		
		if(gpsMessage.substring(characterIndex,characterIndex+NORTH_SOUTH_AMOUNT).equalsIgnoreCase("S"))
		{
			multiplier=-1;
		}
		characterIndex+=NORTH_SOUTH_AMOUNT;
		
		String latTotal= String.format("%.4f", multiplier*(lat2Integer+(latRest/60.0)));
		totalMessageBuilder.append(latTotal);
		totalMessageBuilder.append(";");
		multiplier=1;
		
		
		int long3Integer = Integer.valueOf(
				gpsMessage.substring(characterIndex,characterIndex+LONG_3_INTEGER_AMOUNT),10);
		characterIndex+=LONG_3_INTEGER_AMOUNT;
		
		
		double longRest = Double.valueOf(
				gpsMessage.substring(characterIndex,characterIndex+LONG_REST_AMOUNT+LONG_POINT_AMOUNT
						));
		characterIndex+=LONG_REST_AMOUNT+LONG_POINT_AMOUNT;
		
		if(gpsMessage.substring(characterIndex,characterIndex+NORTH_SOUTH_AMOUNT).equalsIgnoreCase("W"))
		{
			multiplier=-1;
		}
		characterIndex+=EAST_WEST_AMOUNT;
		
		String longTotal = String.format("%.4f", multiplier*(long3Integer+(longRest/60.0)));
		totalMessageBuilder.append(longTotal);
		totalMessageBuilder.append(";");
		multiplier=1;
		
		int day = Integer.valueOf(
				gpsMessage.substring(characterIndex,characterIndex+DAY_AMOUNT));
		characterIndex+=DAY_AMOUNT;
		
		int month= Integer.valueOf(
				gpsMessage.substring(characterIndex,characterIndex+MONTH_AMOUNT));
		characterIndex+=MONTH_AMOUNT; 
		
		int year= Integer.valueOf(
				gpsMessage.substring(characterIndex,characterIndex+YEAR_AMOUNT))+CURRENT_MILLENIA;
		characterIndex+=YEAR_AMOUNT;
		
		int hour = Integer.valueOf(
				gpsMessage.substring(characterIndex,characterIndex+HOUR_AMOUNT));
		characterIndex+=HOUR_AMOUNT; 
		
		int minute= Integer.valueOf(
				gpsMessage.substring(characterIndex,characterIndex+MINUTE_AMOUNT));
		characterIndex+=MINUTE_AMOUNT;
		
		int second= Integer.valueOf(
				gpsMessage.substring(characterIndex,characterIndex+SECOND_AMOUNT));
		characterIndex+=SECOND_AMOUNT;
		
		
		Calendar calendar = new GregorianCalendar
		(year, month, day,hour,minute,second);
		
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
		
		return totalMessageBuilder.toString();
	}

}

class GPSData
{
	GPSData() 
	{
		

	}
}
