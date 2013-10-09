package edu.uprm.capstone.areatech.linkingserver.connection.log.eventdata;

public class LoggingEnums
{
	
	public enum TachometerDetection
	{
		TRIGGERED(1), SILENT(0), ERRONEOUS(99);
		
		private int byteNumber;
		
		public int getByteNumber()
		{
			return byteNumber;
		}

		TachometerDetection(int byteNumber)
		{
			this.byteNumber=byteNumber;
		}
		
		public static TachometerDetection getEvenType(int byteNumber)
		{
			for(TachometerDetection trigger: TachometerDetection .values())
			{
				if(trigger.byteNumber == byteNumber)
					return trigger;
			}
			return ERRONEOUS;
			
		}
		
	}
	
	public enum EventType
	{
		
		IMPACT(1), WEIGHT(2), WATER(3), TACHOMETER(4), ERRONEOUS(99);
		
		private int byteNumber;
		
		EventType(int byteNumber)
		{
			this.byteNumber=byteNumber;
		}
		
		public int getEventNumber()
		{
			return byteNumber;
		}	
		
		public static EventType getEvenType(int byteNumber)
		{
			for(EventType type: EventType.values())
			{
				if(type.byteNumber == byteNumber)
					return type;
			}
			return ERRONEOUS;
			
		}

	}
	
	public enum WaterSensorTrigger
	{
		SENSOR_ONE(1), SENSOR_TWO(2), BOTH(3), NONE(0), ERRONEOUS(99);
		
		private int byteNumber;
		
		WaterSensorTrigger( int byteNumber)
		{
			this.byteNumber=byteNumber;
		}
		
		public int getByteNumber()
		{
			return byteNumber;
		}
		
		public static WaterSensorTrigger getWaterSensorTrigger(int byteNumber)
		{
			for(WaterSensorTrigger trigger: WaterSensorTrigger.values())
			{
				if(trigger.byteNumber==byteNumber)
					return trigger;
			}
			return ERRONEOUS;
		}

	}

}
