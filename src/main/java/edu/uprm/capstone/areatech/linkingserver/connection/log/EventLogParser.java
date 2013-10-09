package edu.uprm.capstone.areatech.linkingserver.connection.log;

import java.util.Calendar;
import java.util.GregorianCalendar;

import org.apache.commons.lang.ArrayUtils;

import edu.uprm.capstone.areatech.linkingserver.connection.log.eventdata.AccelerationVector;
import edu.uprm.capstone.areatech.linkingserver.connection.log.eventdata.EventLog;
import edu.uprm.capstone.areatech.linkingserver.connection.log.eventdata.EventLogBuilder;
import edu.uprm.capstone.areatech.linkingserver.connection.log.eventdata.LoggingEnums;
import edu.uprm.capstone.areatech.linkingserver.utilities.Converter;
/*
 * TODO There are alot of subarray calls here, that may slow things down.  If the application is too slow, look into
 * doing this without subarray, just taking the index at a certain position.
 */
public class EventLogParser
{
	private static final int BASE_TEN = 10;
//	private static final int LEAST_SIGNIFICANT_HALF_BYTE_MASK = (1<<0)+(1<<1)+(1<<2)+(1<<3);
	private static final int LEAST_SIGNIFICANT_HALF_BYTE_MASK = Converter.bitsToByte(0,1,2,3);
//	private static final int MOST_SIGNIFICANT_HALF_BYTE_MASK = (1<<4)+(1<<5)+(1<<6)+(1<<7);
	private static final int MOST_SIGNIFICANT_HALF_BYTE_MASK = Converter.bitsToByte(4,5,6,7);

	private static final String ERRONEOUS_BYTE_AMOUNT_MESSAGE = "The amount of bytes for %s required is %d, and %s were given.";

	private static int[] parseAsBCD(byte numberAsBCD)
	{

		int byteAsInt= (int) Converter.unsignedByteToLong(numberAsBCD);

		int leastSignificantHalf = ((byteAsInt) & (LEAST_SIGNIFICANT_HALF_BYTE_MASK));
		if(leastSignificantHalf>9)
			throw new IllegalArgumentException("Least significant half of byte isn't in BCD format.");

		int mostSignificantHalf = ((byteAsInt&MOST_SIGNIFICANT_HALF_BYTE_MASK)>>4);
		if(mostSignificantHalf>9)
			throw new IllegalArgumentException("Most significant half of byte isn't in BCD format.");
		int[] bcdAsInt= new int[2];
		bcdAsInt[0]= mostSignificantHalf;
		bcdAsInt[1]= leastSignificantHalf;
		return bcdAsInt;

	}

	private static int bcdIntsToInt(int[] bcdInts)
	{
		int current_base=1;
		int totalValue=0;

		for(int i=bcdInts.length-1 ; i>=0; --i, current_base*= BASE_TEN)
		{
			totalValue+=(bcdInts[i]*current_base);
		}
		return totalValue;
	}

	private static final int BYTES_FOR_ADC_CODE=2;
	private static final double V_REF_HI = 3.6;
	private static final double V_REF_LOW = 0;
	private static final int BIT_AMOUNT = 10;
	private static double adcCodeToVin(byte[] adc2bytes)
	{
		if(adc2bytes.length!=BYTES_FOR_ADC_CODE)
		{
			//			throw new IllegalArgumentException("The amount of bytes for ADC code to Vin conversion required is 2, and "+adc2bytes.length+" were given.");
			throw new IllegalArgumentException(String.format(ERRONEOUS_BYTE_AMOUNT_MESSAGE, "ADC code to Vin conversion", BYTES_FOR_ADC_CODE, adc2bytes.length));
		}
		long adcCode = Converter.unsignedByteArrayToLong(adc2bytes);
		/*
		 * Formula is as follows:
		 * Vin = ADCcode*((Vrefhi -Vreflow)/(2^bit_amount)) - Vreflow
		 *
		 */
		double vin = adcCode*((V_REF_HI-V_REF_LOW)/(Math.pow(2, BIT_AMOUNT))) - V_REF_LOW;
		return vin;
	}

	private static final  int DATE_BYTE_AMOUNT = 6;
	public static Calendar parseAsDate(byte[] dateBytes)
	{
		if(dateBytes.length!=DATE_BYTE_AMOUNT)
		{
			//			throw new IllegalArgumentException("The amount of bytes for date should be " +DATE_BYTE_AMOUNT+ " bytes, and received "+dateBytes.length+" bytes.");
			throw new IllegalArgumentException(String.format(ERRONEOUS_BYTE_AMOUNT_MESSAGE, "date", DATE_BYTE_AMOUNT, dateBytes.length));
		}

		int month = 
			EventLogParser.bcdIntsToInt(
					EventLogParser.parseAsBCD(dateBytes[0]));

		int day =
			EventLogParser.bcdIntsToInt(
					EventLogParser.parseAsBCD(dateBytes[1]));

		int year = 2000+
			EventLogParser.bcdIntsToInt(
					EventLogParser.parseAsBCD(dateBytes[2]));

		int hour =
			EventLogParser.bcdIntsToInt(
					EventLogParser.parseAsBCD(dateBytes[3]));

		int min=
			EventLogParser.bcdIntsToInt(
					EventLogParser.parseAsBCD(dateBytes[4]));
		int sec=
			EventLogParser.bcdIntsToInt(
					EventLogParser.parseAsBCD(dateBytes[5]));

		return new GregorianCalendar(year,month,day, hour,min,sec);
	}

	private static final int ACCELERATION_BYTE_AMOUNT = 3*2;
	public static EventLogBuilder parseAccelerationIntoEventLogBuilder(byte[] accelerationBytes, EventLogBuilder eventLogBuilder)
	{
		if(accelerationBytes.length!=ACCELERATION_BYTE_AMOUNT)
		{
			throw new IllegalArgumentException(String.format(ERRONEOUS_BYTE_AMOUNT_MESSAGE, "log event", ACCELERATION_BYTE_AMOUNT, accelerationBytes.length));
		}

		int byteIndex=0;
		eventLogBuilder.setAccelerationX(EventLogParser.adcCodeToVin(ArrayUtils.subarray(accelerationBytes, byteIndex, byteIndex+BYTES_FOR_ADC_CODE)));
		byteIndex+=BYTES_FOR_ADC_CODE;

		eventLogBuilder.setAccelerationY(EventLogParser.adcCodeToVin(ArrayUtils.subarray(accelerationBytes, byteIndex, byteIndex+BYTES_FOR_ADC_CODE)));
		byteIndex+=BYTES_FOR_ADC_CODE;

		return eventLogBuilder.setAccelerationZ(EventLogParser.adcCodeToVin(ArrayUtils.subarray(accelerationBytes, byteIndex, byteIndex+BYTES_FOR_ADC_CODE)));
	}

	private static final int WEIGHT_BYTE_AMOUNT = 3*2;
	public static EventLogBuilder parseWeightIntoEventLogBuilder(byte[] weightBytes, EventLogBuilder eventLogBuilder)
	{
		if(weightBytes.length!=WEIGHT_BYTE_AMOUNT)
		{
			throw new IllegalArgumentException(String.format(ERRONEOUS_BYTE_AMOUNT_MESSAGE, "log event", WEIGHT_BYTE_AMOUNT, weightBytes.length));
		}

		int byteIndex=0;
		eventLogBuilder.setWeightLeftArmrest(EventLogParser.adcCodeToVin(ArrayUtils.subarray(weightBytes, byteIndex, byteIndex+BYTES_FOR_ADC_CODE)));
		byteIndex+=BYTES_FOR_ADC_CODE;

		eventLogBuilder.setWeightRightArmrest(EventLogParser.adcCodeToVin(ArrayUtils.subarray(weightBytes, byteIndex, byteIndex+BYTES_FOR_ADC_CODE)));
		byteIndex+=BYTES_FOR_ADC_CODE;

		return eventLogBuilder.setWeightSeat(EventLogParser.adcCodeToVin(ArrayUtils.subarray(weightBytes, byteIndex, byteIndex+BYTES_FOR_ADC_CODE)));
	}

	private static final int BYTES_IN_LOG = 21;
	public static EventLog parseLog(byte[] logBytes)
	{
		if(logBytes.length!=BYTES_IN_LOG)
		{
			//			throw new IllegalArgumentException("The amount of bytes for the log should be "+BYTES_IN_LOG+" and received "+logBytes.length+" bytes.");
			throw new IllegalArgumentException(String.format(ERRONEOUS_BYTE_AMOUNT_MESSAGE, "log event", BYTES_IN_LOG, logBytes.length));
		}
		EventLogBuilder eventLogBuilder = new EventLogBuilder();

		int byteIndex=0;
		Calendar calendar = parseAsDate(ArrayUtils.subarray(logBytes, byteIndex,byteIndex+DATE_BYTE_AMOUNT));
		eventLogBuilder.setCalendar(calendar);
		byteIndex+=DATE_BYTE_AMOUNT;

		eventLogBuilder.setEventType(logBytes[byteIndex]);
		//		LoggingEnums.EventType eventType = LoggingEnums.EventType.getEvenType(logBytes[byteIndex]);
		++byteIndex;

		eventLogBuilder.setWaterSensorTriggerInfo(logBytes[byteIndex]);
		//		LoggingEnums.WaterSensorTrigger waterTrigger = LoggingEnums.WaterSensorTrigger.getWaterSensorTrigger(logBytes[byteIndex]);
		++byteIndex;
	
		parseWeightIntoEventLogBuilder(ArrayUtils.subarray(logBytes,byteIndex,byteIndex+WEIGHT_BYTE_AMOUNT), eventLogBuilder);
		byteIndex+=WEIGHT_BYTE_AMOUNT;

		parseAccelerationIntoEventLogBuilder(ArrayUtils.subarray(logBytes,byteIndex,byteIndex+ACCELERATION_BYTE_AMOUNT), eventLogBuilder);
		byteIndex+=ACCELERATION_BYTE_AMOUNT;
		
		eventLogBuilder.setRevolutionTime((int) Converter.unsignedByteToLong(logBytes[byteIndex]));
		
		return eventLogBuilder.finalizeObject();


	}

}
