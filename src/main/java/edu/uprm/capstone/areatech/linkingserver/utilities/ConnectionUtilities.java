package edu.uprm.capstone.areatech.linkingserver.utilities;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.SocketException;
import java.net.URL;
import java.net.URLConnection;
import java.sql.Connection;
import java.sql.SQLException;

import org.apache.commons.net.ftp.FTPClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.uprm.capstone.areatech.linkingserver.connection.ClientDeviceMap;
import edu.uprm.capstone.areatech.linkingserver.connection.ConnectionMessage;
import edu.uprm.capstone.areatech.linkingserver.connection.ConnectionMessageBuilder;
import edu.uprm.capstone.areatech.linkingserver.connection.Keyword;
import edu.uprm.capstone.areatech.linkingserver.connection.MessageResponderHandler;
import edu.uprm.capstone.areatech.linkingserver.db.DBInserter;
import edu.uprm.capstone.areatech.linkingserver.db.DBManager;

public class ConnectionUtilities 
{
	final static Logger LOGGER = LoggerFactory.getLogger(ConnectionUtilities.class);

	public static ConnectionMessage findSameKey(String identifyingNumber, final ClientDeviceMap CLIENT_DEVICE_MAP) 
	{
		ConnectionMessage findMessage=null;
		synchronized(CLIENT_DEVICE_MAP)
		{
			findMessage=CLIENT_DEVICE_MAP.getPreviousConnectionMessage(identifyingNumber);
		}
		return findMessage;
	}

	public static boolean isBadlyFormattedMessage(ConnectionMessage messageReceived) 
	{
		boolean erroneous = false;
		if(messageReceived.getIdentifyingNumber().length() !=ConnectionMessage.IDENTIFYING_NUMBER_SIZE)
			erroneous=true;
		return erroneous;

	}

	public static ConnectionMessage produceUnknownConnectionTypeMessage(
			ConnectionMessage messageReceived,
			ConnectionMessageBuilder serverResponseBuilder) 
	{
		serverResponseBuilder.setKeyword(Keyword.ERRONEOUS).setData("TYP;Unrecognized Type="+messageReceived.getType().getFlag());
		return serverResponseBuilder.finalizeObject();
	}

	public static InputStream retrieveFTPInputStream(ConnectionMessage messageReceived) throws IOException,MalformedURLException {

		URL url = new URL("ftp://diameter_device:diapass@areatech.org/"+messageReceived.getIdentifyingNumber()+".txt;type=i");
		URLConnection urlc = url.openConnection();
		InputStream is = urlc.getInputStream(); 

		return is;
	}

	public static void uploadToDB(InputStream inputStream, ConnectionMessage message) 
	{
		Connection connection = DBManager.getConnection();

		try 
		{
			DBInserter.uploadToDB(connection, inputStream, message);
		} 
		catch (SQLException e) 
		{
			LOGGER.debug("SQLException detected.");
		} 
		catch (IOException e) 
		{
			LOGGER.debug("IOException detected.");
		}
		finally
		{
			try {
				inputStream.close();
				connection.close();
			} catch (SQLException e) {
				if(LOGGER.isTraceEnabled())
				{
					LOGGER.debug("SQLException detected.");
				}
			} catch (IOException e) {
				if(LOGGER.isTraceEnabled())
				{
					LOGGER.debug("IOException detected.");
				}
			}
		}

	}

	public static ConnectionMessage produceUnknownKeywordMessage(
			ConnectionMessage messageReceived,
			ConnectionMessageBuilder serverResponseBuilder) 
	{
		serverResponseBuilder.setKeyword(Keyword.ERRONEOUS).setData("KEY;Unrecognized Key="+messageReceived.getKeyword().getKeyword());
		return serverResponseBuilder.finalizeObject();
	}

	public static ConnectionMessage parseFileDataAndDatabaseUpload(
			ConnectionMessage deviceMessage,
			ConnectionMessageBuilder serverResponseBuilder) 
	{

		LOGGER.debug("Will parse file data and upload to database.");		

		InputStream ftpFile;
		try 
		{
			ftpFile = ConnectionUtilities.retrieveFTPInputStream(deviceMessage);
			ConnectionUtilities.uploadToDB(ftpFile,deviceMessage);
			serverResponseBuilder.setKeyword(Keyword.SUCCESS).setData("DBU;Data Uploaded succefully.");

			LOGGER.warn("REMEMBER TO INCLUDE THE FTP FILE DELETION");

			//			deleteFTP(deviceMessage);



		} 
		catch (MalformedURLException e) 
		{
			LOGGER.info("Badly formed ftp url.");
			LOGGER.info("Error:"+e.getMessage());
			serverResponseBuilder.setKeyword(Keyword.ERRONEOUS).setData("Malformed FTP URL.");

		} 
		catch (IOException e) 
		{
			LOGGER.info("FTP IO exception.");
			LOGGER.info("Error:"+e.getMessage());
			serverResponseBuilder.setKeyword(Keyword.ERRONEOUS).setData("FTP file IO exception.");
		}


		return serverResponseBuilder.finalizeObject();

	}

	public static void deleteFTP(ConnectionMessage deviceMessage) throws SocketException, IOException
	{
		String fileToDelete=deviceMessage.getIdentifyingNumber()+".txt";
		FTPClient client = new FTPClient();
		client.connect("chamakits.com");
		client.login("diameter_device", "diapass");
		boolean deleted = client.deleteFile(fileToDelete);
		if(deleted)
		{
			LOGGER.debug("Succesfully deleted file:"+fileToDelete);
		}
		else
		{
			LOGGER.debug("Could not delete FTP file:"+fileToDelete);
		}
		client.logout();
		client.disconnect();
	}



	public static ConnectionMessage parseAndInsertDiagnosticData(
			ConnectionMessage deviceMessage,
			ConnectionMessageBuilder serverResponseBuilder) {

		ConnectionMessage newMessage =serverResponseBuilder.setKeyword(Keyword.SUCCESS).setData("SCC;Processed Succesfully.").finalizeObject(); 


		return newMessage;
	}

	public static ConnectionMessage parseGPSData(ConnectionMessage deviceMessage,
			ConnectionMessageBuilder serverResponseBuilder) 
	{
		if(deviceMessage.getData().equalsIgnoreCase("NONE"))
		{
			serverResponseBuilder.setKeyword(Keyword.ERRONEOUS).setData("NONE");
			
			
		}
		else
		{
			String getResponseData =GPSParser.convertGPSMessage(deviceMessage.getData());
			serverResponseBuilder.setKeyword(Keyword.SUCCESS).setData(getResponseData);
		}

		return serverResponseBuilder.finalizeObject();
	}


	public static ConnectionMessage parseAndInsertGPSData(ConnectionMessage deviceMessage,
			ConnectionMessageBuilder serverResponseBuilder) {

		ConnectionMessage newMessage = parseGPSData(deviceMessage, serverResponseBuilder);


		return newMessage;
	}

	public static ConnectionMessage produceSuccessMessage(
			ConnectionMessage messageReceived,
			ConnectionMessageBuilder serverResponseBuilder,
			String data) {
		return serverResponseBuilder.setKeyword(Keyword.SUCCESS).setData(data).finalizeObject();
	}

	public static void insertMessage(ConnectionMessage newMessage)
	{
		synchronized (MessageResponderHandler.CLIENT_DEVICE_MAP) 
		{
			LOGGER.debug("CLIENT_DEVICE_MAP:Inserting: "+newMessage);
			MessageResponderHandler.CLIENT_DEVICE_MAP.insertConnection(newMessage);			
		}

	}

	public static ConnectionMessage removeMessage(String identifyingString)
	{
		ConnectionMessage messageFound=null;
		synchronized (MessageResponderHandler.CLIENT_DEVICE_MAP) 
		{
			LOGGER.debug("CLIENT_DEVICE_MAP:Removing: "+identifyingString);

			messageFound=MessageResponderHandler.CLIENT_DEVICE_MAP.removeMessage(identifyingString);
			LOGGER.debug("CLIENT_DEVICE_MAP:Found: "+messageFound);
		}
		return messageFound;

	}

	public static void prepareResponseForDevice(ConnectionMessage serverResponse) 
	{
		if(serverResponse.getData().length()>=99)
		{
			serverResponse.setData(serverResponse.getData().substring(0,98)+"*");
		}
		else
		{
			serverResponse.setData(serverResponse.getData()+"*");
		}

	}
}
