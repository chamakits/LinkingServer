package edu.uprm.capstone.areatech.linkingserver.connection.log.eventdata;

import java.util.Calendar;

import edu.uprm.capstone.areatech.linkingserver.connection.log.EventLogParser;

//TODO this seems shady and horrible, too much stuff in one object.  I should abstract out acceleration info and weight info.
public class EventLogBuilder extends EventLogParser
{
	//	private boolean finalized;
	private Calendar calendar;
	private LoggingEnums.EventType eventType;
	private LoggingEnums.WaterSensorTrigger waterSensorTriggerInfo;
	private AccelerationVector accelerationVector;
	private WeightContainer weightContainer;
	private int revolutionTime;


	public EventLogBuilder()
	{
		//		this.finalized=false;
		this.accelerationVector= new AccelerationVector();
		this.weightContainer= new WeightContainer();
	}
	
	//TODO Verify if I should use the boolean value check or not worth it.
	public EventLog finalizeObject()
	{
		//		if(!finalized)
		return new EventLog(calendar,eventType,waterSensorTriggerInfo, 
				accelerationVector,weightContainer,
				revolutionTime);
	}

	public EventLogBuilder setCalendar(Calendar calendar)
	{
		this.calendar = calendar;
		return this;
	}

	public EventLogBuilder setEventType(int byteNumber)
	{
		this.eventType = LoggingEnums.EventType.getEvenType(byteNumber);
		return this;
	}

	public EventLogBuilder setWaterSensorTriggerInfo(int byteNumber)
	{
		this.waterSensorTriggerInfo = LoggingEnums.WaterSensorTrigger.getWaterSensorTrigger(byteNumber);
		return this;
	}

	public EventLogBuilder setAccelerationX(double accelerationX)
	{
		this.accelerationVector.setAccelerationX(accelerationX);
		return this;
	}

	public EventLogBuilder setAccelerationY(double accelerationY)
	{
		this.accelerationVector.setAccelerationY(accelerationY);
		return this;
	}

	public EventLogBuilder setAccelerationZ(double accelerationZ)
	{
		this.accelerationVector.setAccelerationZ(accelerationZ);
		return this;
	}

	public EventLogBuilder setWeightLeftArmrest(double weightLeftArmrest)
	{
		this.weightContainer.setWeightLeftArmrest(weightLeftArmrest);
		return this;
	}

	public EventLogBuilder setWeightRightArmrest(double weightRightArmrest)
	{
		this.weightContainer.setWeightRightArmrest(weightRightArmrest);
		return this;
	}
	public EventLogBuilder setWeightSeat(double weightSeat)
	{
		this.weightContainer.setWeightSeat(weightSeat);
		return this;
	}

	public EventLogBuilder setRevolutionTime(int revolutionTime)
	{
		this.revolutionTime = revolutionTime;
		return this;
	}

}



