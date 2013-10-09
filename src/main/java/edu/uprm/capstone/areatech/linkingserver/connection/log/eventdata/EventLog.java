package edu.uprm.capstone.areatech.linkingserver.connection.log.eventdata;

import java.util.Calendar;

import edu.uprm.capstone.areatech.linkingserver.connection.log.eventdata.LoggingEnums.EventType;
import edu.uprm.capstone.areatech.linkingserver.connection.log.eventdata.LoggingEnums.WaterSensorTrigger;


public class EventLog
{
	
	private Calendar calendar;
	private LoggingEnums.EventType eventType;
	private LoggingEnums.WaterSensorTrigger waterSensorTriggerInfo;
	private AccelerationVector accelerationVector;
	private WeightContainer weightContainer;
	private int revolutionTime;
	
	//TODO CHANGE THIS, this constructor is just too big.  Put all possibly related data into smarter objects.
	public EventLog(Calendar calendar, EventType eventType,
			WaterSensorTrigger waterSensorTriggerInfo,
			AccelerationVector accelerationVector,
			WeightContainer weightContainer, int revolutionTime)
	{
		super();
		this.calendar = calendar;
		this.eventType = eventType;
		this.waterSensorTriggerInfo = waterSensorTriggerInfo;
		this.accelerationVector=accelerationVector;
		this.weightContainer = weightContainer;
		this.revolutionTime = revolutionTime;
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
		return this.weightContainer.getWeightLeftArmrest();
	}
	
	public double getWeightRightArmrest()
	{
		return this.weightContainer.getWeightRightArmrest();
	}
	
	public double getWeightSeat()
	{
		return this.weightContainer.getWeightSeat();
	}

	public int getRevolutionTime()
	{
		return revolutionTime;
	}

	@Override
	public String toString()
	{
		return "EventLog [calendar:\n" + calendar + ", \neventType:\n" + eventType + ", \nwaterSensorTriggerInfo:\n" + waterSensorTriggerInfo + ", \naccelerationVector:\n" + accelerationVector
				+ ", \nweightContainer:\n" + weightContainer + ", \nrevolutionTime:\n" + revolutionTime + "]";
	}
		
	

}
