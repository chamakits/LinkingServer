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
		//ENUM('water','tachometer','weight','impact')
		
		IMPACT(1,"impact"), WEIGHT(2,"weight"), WATER(4,"water"), TACHOMETER(8,"tachometer"), ERRONEOUS(99,"impact");
		
		private int byteNumber;
		private String name;
		
		EventType(int byteNumber,String name)
		{
			this.byteNumber=byteNumber;
			this.name = name;
		}
		
		public String getName()
		{
			return name;
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
		SENSOR_LEFT(4), SENSOR_RIGHT(2), BOTH(8), NONE(1), ERRONEOUS(99);
		
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
