package edu.uprm.capstone.areatech.linkingserver.connection.log.eventdata;

import java.util.Calendar;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.uprm.capstone.areatech.linkingserver.connection.log.eventdata.LoggingEnums.EventType;
import edu.uprm.capstone.areatech.linkingserver.connection.log.eventdata.LoggingEnums.WaterSensorTrigger;
import edu.uprm.capstone.areatech.linkingserver.db.DBInserter;


public class EventLog
{
	
	private Calendar calendar;
	private LoggingEnums.EventType eventType;
	private LoggingEnums.WaterSensorTrigger waterSensorTriggerInfo;
	private AccelerationVector accelerationVector;
	private WeightContainer weightContainer;
	private int tachometerValue;
	
	final static Logger LOGGER = LoggerFactory.getLogger(EventLog.class);
	
	public EventLog(Calendar calendar, EventType eventType,
			WaterSensorTrigger waterSensorTriggerInfo,
			AccelerationVector accelerationVector,
			WeightContainer weightContainer, int tachometerValue)
	{
		super();
		this.calendar = calendar;
		this.eventType = eventType;
		this.waterSensorTriggerInfo = waterSensorTriggerInfo;
		this.accelerationVector=accelerationVector;
		
		LOGGER.debug("Weight container: "+weightContainer.weightString());
		this.weightContainer = weightContainer;
		this.tachometerValue = tachometerValue;
	}

	public Calendar getCalendar()
	{
		return calendar;
	}

	public LoggingEnums.EventType getEventType()
	{
		return eventType;
	}

	public LoggingEnums.WaterSensorTrigger getWaterSensorTriggerInfo()
	{
		return waterSensorTriggerInfo;
	}

	public double getAccelerationX()
	{
		return accelerationVector.getAccelerationX();
	}

	public double getAccelerationY()
	{
		return accelerationVector.getAccelerationY();
	}

	public double getAccelerationZ()
	{
		return accelerationVector.getAccelerationZ();
	}

	public double getWeightLeftArmrest()
	{
		LOGGER.debug("Left Armrest in event:"+this.weightContainer.getWeightLeftArmrest());
		return this.weightContainer.getWeightLeftArmrest();
	}
	
	public double getWeightRightArmrest()
	{
		LOGGER.debug("Right Armrest in event:"+this.weightContainer.getWeightRightArmrest());
		return this.weightContainer.getWeightRightArmrest();
	}
	
	public double getWeightSeat()
	{
		LOGGER.debug("Seat Armrest in event:"+this.weightContainer.getWeightSeat());
		return this.weightContainer.getWeightSeat();
	}

	public int getTachometerValue()
	{
		return tachometerValue;
	}

	@Override
	public String toString()
	{
		return "EventLog [calendar:\n" + calendar + ", \neventType:\n" + eventType + ", \nwaterSensorTriggerInfo:\n" + waterSensorTriggerInfo + ", \naccelerationVector:\n" + accelerationVector
				+ ", \nweightContainer:\n" + weightContainer + ", \nrevolutionTime:\n" + tachometerValue + "]";
	}
		
	

}
