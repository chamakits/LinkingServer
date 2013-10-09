package edu.uprm.capstone.areatech.linkingserver.db;

import java.io.IOException;
import java.io.InputStream;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.EventListenerProxy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import utils.UnitConverter;

import edu.uprm.capstone.areatech.linkingserver.connection.ConnectionMessage;
import edu.uprm.capstone.areatech.linkingserver.connection.log.EventLogParser;
import edu.uprm.capstone.areatech.linkingserver.connection.log.eventdata.EventLog;
import edu.uprm.capstone.areatech.linkingserver.utilities.ConnectionUtilities;
import edu.uprm.capstone.areatech.linkingserver.utilities.Converter;
import edu.uprm.capstone.areatech.linkingserver.utilities.NumberPadding;

public class DBInserter 
{
	private final static String INSERT_STATEMENT_FORMAT="INSERT INTO `diameter`.`event`"+
	"(`time`,`device_id`,`tachometer_speed`," +
	"`left_armrest_weight`,`right_armrest_weight`," +
	"`seat_weight`,`water_event_left`,`water_event_right`," +
	"`impact_x_axis`,`impact_y_axis`,`impact_z_axis`," +
	"`event_type`,`wheelchair_reseller`,`wheelchair_serial_id`,`user_hash`) VALUES(%s)";

	private final static String GET_DEVICE_ID = "call get_device_id(?)";

	private final static String GET_PROFILE_VIEW="call get_profile_view(?)";

	private final static String GET_WHEEL_DIAMETER= "call get_wheel_diameter(?)";

	final static Logger LOGGER = LoggerFactory.getLogger(DBInserter.class);

	final static String VALUES_STRING=
		"\'%s\',%d,%s," +//Date, device id, tachometer as string
		"%f,%f,%f," + //weight left armrest,weight right armrest, weight seat
		"%d,%d,%f," + //water sensor trigger1, water sensor trigger2, acceleration x
		"%f,%f,\'%s\'," +//acceleration y, acceleration z, event type name
		"\'%s\',\'%s\',\'%s\'";//reseller, wheelchair serial id, user hash

	public static void uploadToDB(Connection connect, InputStream inputStream, ConnectionMessage message) throws SQLException, IOException
	{
		System.out.println("TRYING DB UPLOAD!");
		byte[] oneLogBuffer= new byte[EventLogParser.BYTES_IN_LOG];
		int totalBytesRead=0;
		int currentBytesRead=0;
		String valueInsert="";

		CallableStatement deviceIdCall = connect.prepareCall(GET_DEVICE_ID);
		deviceIdCall.setString(1, message.getIdentifyingNumber());
		ResultSet resultSet = deviceIdCall.executeQuery();
		int deviceId = 0;
		if(resultSet.next())
		{
			deviceId=resultSet.getInt("device_id");
		}
		resultSet.close();


		CallableStatement wheelDiamaterCall= connect.prepareCall(GET_WHEEL_DIAMETER);
		wheelDiamaterCall.setString(1, message.getIdentifyingNumber());
		resultSet = wheelDiamaterCall.executeQuery();
		double diameter=0;
		if(resultSet.next())
			diameter = resultSet.getDouble("diameter");
		resultSet.close();


		CallableStatement profileViewCall = connect.prepareCall(GET_PROFILE_VIEW);
		profileViewCall.setString(1, message.getIdentifyingNumber());
		resultSet = profileViewCall.executeQuery();

		String reseller = "";
		String wheelchairSerialId ="";
		String userHash ="";

		if(resultSet.next())
		{
			reseller = resultSet.getString("wheelchair_reseller");
			wheelchairSerialId = resultSet.getString("wheelchair_serial_id");
			userHash = resultSet.getString("user_hash");
		}
		resultSet.close();

		PreparedStatement continousInsert = null;
		connect.setAutoCommit(false);
		int waterSensorLeft=0;
		int waterSensorRight=0;
		String valueInserting ="";
		boolean dummyRead=true;
		do
		{
			currentBytesRead=0;
			currentBytesRead=inputStream.read(oneLogBuffer,currentBytesRead,EventLogParser.BYTES_IN_LOG-currentBytesRead);
			if(dummyRead)
			{
				totalBytesRead=0;
				dummyRead=false;
				LOGGER.debug("Passed dummy log.");
				continue;
			}
			if(LOGGER.isDebugEnabled())
			{	
				LOGGER.debug(String.format("Read %d bytes.",currentBytesRead));
				LOGGER.debug("Read:"+Converter.bitString(oneLogBuffer));
			}


			totalBytesRead+=currentBytesRead;
			if(totalBytesRead>=EventLogParser.BYTES_IN_LOG)
			{
				totalBytesRead=0;
				EventLog log =null;
				try
				{
					log = EventLogParser.parseLog(oneLogBuffer);
				}
				catch(IllegalArgumentException e)
				{
					LOGGER.warn("File found not properly formatted.");
//					e.printStackTrace();
					break;
				}

				String tachoSpeedAsString = Double.toString(tachoSpeedFromValue(log.getTachometerValue(),diameter));
				if(tachoSpeedAsString.equals("Infinity"))
				{
					tachoSpeedAsString = "0.0";
				}


				switch(log.getWaterSensorTriggerInfo())
				{
				case SENSOR_LEFT:
					waterSensorLeft=1;
					waterSensorRight=0;
					break;
				case SENSOR_RIGHT:
					waterSensorLeft=0;
					waterSensorRight=1;
					break;
				case BOTH:
					waterSensorLeft=1;
					waterSensorRight=1;
					break;
				default:
					waterSensorLeft=0;
					waterSensorRight=0;
				}

				valueInserting = String.format(VALUES_STRING, 
						getTime(log.getCalendar()),deviceId,tachoSpeedAsString,
						log.getWeightLeftArmrest(),log.getWeightRightArmrest(),log.getWeightSeat(),
						waterSensorLeft,waterSensorRight,log.getAccelerationX(),
						log.getAccelerationY(),log.getAccelerationZ(),log.getEventType().getName(),
						reseller,wheelchairSerialId,userHash);

				valueInsert = String.format(INSERT_STATEMENT_FORMAT, valueInserting);
				LOGGER.debug("Executing SQL:");
				LOGGER.debug(valueInsert);
				continousInsert = connect.prepareStatement(valueInsert);
				continousInsert.execute();
			}
		}while(currentBytesRead>0);
		connect.commit();
		connect.setAutoCommit(true);

	}



	private static double tachoSpeedFromValue(int tachometerValue, double wheelDiameter) {
		return UnitConverter.ATuToMPH(tachometerValue, wheelDiameter);
	}

	private static String getTime(Calendar calendar) {
		String year = Integer.toString(calendar.get(Calendar.YEAR),10);
		String month = NumberPadding.zeroPaddedNumber(calendar.get(Calendar.MONTH), 2, false);
		String day = NumberPadding.zeroPaddedNumber(calendar.get(Calendar.DAY_OF_MONTH), 2, false);

		String hour = NumberPadding.zeroPaddedNumber(calendar.get(Calendar.HOUR), 2, false);
		String minute = NumberPadding.zeroPaddedNumber(calendar.get(Calendar.MINUTE), 2, false);
		String second = NumberPadding.zeroPaddedNumber(calendar.get(Calendar.SECOND), 2, false);



		return String.format("%s-%s-%s %s:%s:%s", year,month,day,hour,minute,second);
	}
}
