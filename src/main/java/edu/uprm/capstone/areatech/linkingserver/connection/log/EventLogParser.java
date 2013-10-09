package edu.uprm.capstone.areatech.linkingserver.connection.log;

import java.util.Calendar;
import java.util.GregorianCalendar;

import org.apache.commons.lang.ArrayUtils;
import org.apache.mina.proxy.utils.ByteUtilities;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import utils.UnitConverter;
import edu.uprm.capstone.areatech.linkingserver.connection.log.eventdata.EventLog;
import edu.uprm.capstone.areatech.linkingserver.connection.log.eventdata.EventLogBuilder;
import edu.uprm.capstone.areatech.linkingserver.utilities.Converter;

public class EventLogParser
{
	private static final int BASE_TEN = 10;
//	private static final int LEAST_SIGNIFICANT_HALF_BYTE_MASK = (1<<0)+(1<<1)+(1<<2)+(1<<3);
	private static final int LEAST_SIGNIFICANT_HALF_BYTE_MASK = Converter.bitsToByte(0,1,2,3);
//	private static final int MOST_SIGNIFICANT_HALF_BYTE_MASK = (1<<4)+(1<<5)+(1<<6)+(1<<7);
	private static final int MOST_SIGNIFICANT_HALF_BYTE_MASK = Converter.bitsToByte(4,5,6,7);

	private static final String ERRONEOUS_BYTE_AMOUNT_MESSAGE = "The amount of bytes for %s required is %d, and %s were given.";
	
	final static Logger LOGGER = LoggerFactory.getLogger(EventLogParser.class);

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
	private static final double V_REF_HI = 3.3;
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
	public static Calendar parseAsDate(byte[] dateBytes) throws IllegalArgumentException
	{
		if(LOGGER.isDebugEnabled())
		{
			LOGGER.debug("Parsing date from:"+Converter.bitString(dateBytes));
		}
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
//		UnitConverter.adcToVoltage();
//		despues voltageToWeight()
		double currentVin; 
		byte[] currentAdcBytes;
		long currentAdcLong;
		
		currentAdcBytes=ArrayUtils.subarray(accelerationBytes, byteIndex, byteIndex+BYTES_FOR_ADC_CODE);
//		currentAdcLong=;
//		currentVin=EventLogParser.adcCodeToVin(currentAdcBytes);
		currentAdcLong = Converter.unsignedByteArrayToLongReverse(currentAdcBytes);
		currentVin=UnitConverter.adcToVoltage((int) currentAdcLong);
		eventLogBuilder.setAccelerationX(voltageToAcceleration(currentVin));
		byteIndex+=BYTES_FOR_ADC_CODE;

		currentAdcBytes=ArrayUtils.subarray(accelerationBytes, byteIndex, byteIndex+BYTES_FOR_ADC_CODE);
//		currentVin=EventLogParser.adcCodeToVin(currentAdcBytes);
		currentAdcLong = Converter.unsignedByteArrayToLongReverse(currentAdcBytes);
		currentVin=UnitConverter.adcToVoltage((int) currentAdcLong);
		eventLogBuilder.setAccelerationY(voltageToAcceleration(currentVin));
		byteIndex+=BYTES_FOR_ADC_CODE;
		
		currentAdcBytes=ArrayUtils.subarray(accelerationBytes, byteIndex, byteIndex+BYTES_FOR_ADC_CODE);
//		currentVin=EventLogParser.adcCodeToVin(currentAdcBytes);
		currentAdcLong = Converter.unsignedByteArrayToLongReverse(currentAdcBytes);
		currentVin=UnitConverter.adcToVoltage((int) currentAdcLong);
		eventLogBuilder.setAccelerationZ(voltageToAcceleration(currentVin));

		return eventLogBuilder;
	}
	
	private static double voltageToAcceleration(double vin)
	{
		return (vin - 1.59)/.38;
	}

	private static final int WEIGHT_BYTE_AMOUNT = 3*2;
	public static EventLogBuilder parseWeightIntoEventLogBuilder(byte[] weightBytes, EventLogBuilder eventLogBuilder)
	{
		if(weightBytes.length!=WEIGHT_BYTE_AMOUNT)
		{
			throw new IllegalArgumentException(String.format(ERRONEOUS_BYTE_AMOUNT_MESSAGE, "log event", WEIGHT_BYTE_AMOUNT, weightBytes.length));
		}

		int byteIndex=0;
		
		
		double currentVin; 
		byte[] currentAdcBytes;
		long currentAdcLong;
		double currentWeight=0;
		
		currentAdcBytes=ArrayUtils.subarray(weightBytes, byteIndex, byteIndex+BYTES_FOR_ADC_CODE);
		currentAdcLong = Converter.unsignedByteArrayToLongReverse(currentAdcBytes);
		currentVin=UnitConverter.adcToVoltage((int) currentAdcLong);
		LOGGER.debug("Setting for seat weight in vin:"+currentVin);
		currentWeight=UnitConverter.voltageToWeight(currentVin);
		LOGGER.debug("Converted to:"+currentWeight);
		if(Double.isNaN(currentWeight) || Double.isInfinite(currentWeight))
		{
			System.out.println("Changing:"+currentWeight+" to 0.");
			LOGGER.debug("Double needs changing:"+currentWeight);
			currentWeight=0;
		}
		eventLogBuilder.setWeightSeat(currentWeight);
		byteIndex+=BYTES_FOR_ADC_CODE;

		currentAdcBytes=ArrayUtils.subarray(weightBytes, byteIndex, byteIndex+BYTES_FOR_ADC_CODE);
		currentAdcLong = Converter.unsignedByteArrayToLongReverse(currentAdcBytes);
		currentVin=UnitConverter.adcToVoltage((int) currentAdcLong);
		LOGGER.debug("Setting for right armrest weight in vin:"+currentVin);
		//Because arms are more sensitive.
		currentWeight=UnitConverter.voltageToWeight(currentVin)/10;
		LOGGER.debug("Converted to:"+currentWeight);
		if(Double.isNaN(currentWeight) || Double.isInfinite(currentWeight))
		{
			System.out.println("Changing:"+currentWeight+" to 0.");
			LOGGER.debug("Double needs changing:"+currentWeight);
			currentWeight=0;
		}
		eventLogBuilder.setWeightRightArmrest(currentWeight);
		byteIndex+=BYTES_FOR_ADC_CODE;
		
		currentAdcBytes=ArrayUtils.subarray(weightBytes, byteIndex, byteIndex+BYTES_FOR_ADC_CODE);
		currentAdcLong = Converter.unsignedByteArrayToLongReverse(currentAdcBytes);
		currentVin=UnitConverter.adcToVoltage((int) currentAdcLong);
		LOGGER.debug("Setting for left armrest weight in vin:"+currentVin);
		//Because arms are more sensitive.
		currentWeight=UnitConverter.voltageToWeight(currentVin)/10;
		LOGGER.debug("Converted to:"+currentWeight);
		if(Double.isNaN(currentWeight) || Double.isInfinite(currentWeight))
		{
			System.out.println("Changing:"+currentWeight+" to 0.");
			LOGGER.debug("Double needs changing:"+currentWeight);
			currentWeight=0;
		}
		eventLogBuilder.setWeightLeftArmrest(currentWeight);

		LOGGER.debug("Done parsing weight.");
		
		return eventLogBuilder;
	}

	public static final int BYTES_IN_LOG = 22;
	private static final int TACHOMETER_BYTE_AMOUNT = 2;
	public static EventLog parseLog(byte[] logBytes) throws IllegalArgumentException
	{
				
		ArrayUtils.reverse(logBytes);
		logBytes=Converter.reverseBytes(logBytes);
		
		if(logBytes.length!=BYTES_IN_LOG)
		{
			LOGGER.debug("Log of lenght:"+logBytes.length+" instead of "+BYTES_IN_LOG);
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
		
		eventLogBuilder.setTachometerValue((int) Converter.unsignedByteToLong(logBytes[byteIndex]));
		byteIndex+=TACHOMETER_BYTE_AMOUNT;
		
		parseWeightIntoEventLogBuilder(ArrayUtils.subarray(logBytes,byteIndex,byteIndex+WEIGHT_BYTE_AMOUNT), eventLogBuilder);
		byteIndex+=WEIGHT_BYTE_AMOUNT;
		
		parseAccelerationIntoEventLogBuilder(ArrayUtils.subarray(logBytes,byteIndex,byteIndex+ACCELERATION_BYTE_AMOUNT), eventLogBuilder);
		byteIndex+=ACCELERATION_BYTE_AMOUNT;

		
		
		
		
		eventLogBuilder.setWaterSensorTriggerInfo(logBytes[byteIndex]);
		//		LoggingEnums.WaterSensorTrigger waterTrigger = LoggingEnums.WaterSensorTrigger.getWaterSensorTrigger(logBytes[byteIndex]);
		++byteIndex;
		
		return eventLogBuilder.finalizeObject();


	}

}
