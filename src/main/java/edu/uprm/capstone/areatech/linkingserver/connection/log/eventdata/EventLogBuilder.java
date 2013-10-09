package edu.uprm.capstone.areatech.linkingserver.connection.log.eventdata;

import java.util.Calendar;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.uprm.capstone.areatech.linkingserver.connection.log.EventLogParser;

public class EventLogBuilder extends EventLogParser
{
	private Calendar calendar;
	private LoggingEnums.EventType eventType;
	private LoggingEnums.WaterSensorTrigger waterSensorTriggerInfo;
	private AccelerationVector accelerationVector;
	private WeightContainer weightContainer;
	private int tachometerValue;
	
	final static Logger LOGGER = LoggerFactory.getLogger(EventLogBuilder.class);


	public EventLogBuilder()
	{
		this.accelerationVector= new AccelerationVector();
		this.weightContainer= new WeightContainer();
	}
	
	public EventLog finalizeObject()
	{
		LOGGER.debug("Finalizing event log builder:"+this.weightContainer.weightString());
		return new EventLog(calendar,eventType,waterSensorTriggerInfo, 
				accelerationVector,weightContainer,
				tachometerValue);
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
		LOGGER.debug("Setting weight on left:"+weightLeftArmrest);
		this.weightContainer.setWeightLeftArmrest(weightLeftArmrest);
		LOGGER.debug("Has been set to:"+this.weightContainer.getWeightLeftArmrest());
		return this;
	}

	public EventLogBuilder setWeightRightArmrest(double weightRightArmrest)
	{
		LOGGER.debug("Setting weight on right:"+weightRightArmrest);
		this.weightContainer.setWeightRightArmrest(weightRightArmrest);
		LOGGER.debug("Has been set to:"+this.weightContainer.getWeightRightArmrest());
		return this;
	}
	public EventLogBuilder setWeightSeat(double weightSeat)
	{
		LOGGER.debug("Setting weight on seat:"+weightSeat);
		this.weightContainer.setWeightSeat(weightSeat);
		LOGGER.debug("Has been set to:"+this.weightContainer.getWeightSeat());
		return this;
	}

	public EventLogBuilder setTachometerValue(int tachometerValue)
	{
		this.tachometerValue = tachometerValue;
		return this;
	}

}



