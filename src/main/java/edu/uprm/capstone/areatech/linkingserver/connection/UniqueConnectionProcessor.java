package edu.uprm.capstone.areatech.linkingserver.connection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.uprm.capstone.areatech.linkingserver.utilities.ConnectionUtilities;

public class UniqueConnectionProcessor 
{
	final static Logger LOGGER = LoggerFactory.getLogger(UniqueConnectionProcessor.class);

	public static ConnectionMessage processFirstMessageAccordingToType(ConnectionMessage messageReceived,	ConnectionMessageBuilder serverResponseBuilder) 
	{
		ConnectionMessage serverResponse = null;
		switch(messageReceived.getType())
		{
		case APPLICATION:
			serverResponse = actOnApplicationKeyword(messageReceived,serverResponseBuilder);
			break;
		case DEVICE:
			serverResponse = actOnDeviceKeyword(messageReceived,serverResponseBuilder);
			break;
		case ERRONEOUS:
		default:
			serverResponse = ConnectionUtilities.produceUnknownConnectionTypeMessage(messageReceived,serverResponseBuilder);
			break;
		}

		LOGGER.debug("NOT DONE processFirstMessageAccordingToType not fully implemented.");
		return serverResponse;
	}

	private static ConnectionMessage actOnApplicationKeyword(
			ConnectionMessage messageReceived,
			ConnectionMessageBuilder serverResponseBuilder) 
	{
		ConnectionMessage serverResponse=null;
		LOGGER.debug("Acting on application Keyword.");
		switch(messageReceived.getKeyword())
		{
		case REQUEST:
			LOGGER.debug("Application says "+Keyword.REQUEST.getKeyword());
			registerApplicationAndWait(messageReceived,serverResponseBuilder);
			serverResponse=getDeviceRequest(messageReceived, serverResponseBuilder);
			break;
		case GPS:
			LOGGER.debug("Application says "+Keyword.GPS.getKeyword());
			registerApplicationAndWait(messageReceived,serverResponseBuilder);
			serverResponse=getDeviceGPS(messageReceived,serverResponseBuilder);
			break;
		case DIAGNOSTIC:
			LOGGER.debug("Application says "+Keyword.DIAGNOSTIC.getKeyword());
			registerApplicationAndWait(messageReceived,serverResponseBuilder);
			serverResponse=getDeviceDiagnostic(messageReceived,serverResponseBuilder);
			break;
		case ERRONEOUS:
		default:
			ConnectionUtilities.produceUnknownKeywordMessage(messageReceived, serverResponseBuilder);
		}
		
		return serverResponse;
	}

	private static void registerApplicationAndWait(
			ConnectionMessage messageReceived,
			ConnectionMessageBuilder serverResponseBuilder)
	{

		LOGGER.debug("Registering application.");	
		ConnectionUtilities.insertMessage(messageReceived);

		synchronized (messageReceived) 
		{
			try 
			{
				LOGGER.debug("Application waiting.");
				messageReceived.wait(MessageResponderHandler.THREAD_TIME_OUT);
			} 
			catch (InterruptedException e) 
			{
				LOGGER.debug("Wait interrupted.");
			}
			LOGGER.debug("Application thread waking up.");
		}

	}

	private static ConnectionMessage getDeviceRequest(
			ConnectionMessage messageReceived,
			ConnectionMessageBuilder serverResponseBuilder) 
	{
		LOGGER.debug("Getting device request.");

		if(messageReceived.getResponded())
		{
			LOGGER.debug("Message found has been responded.  Success message being sent.");
			serverResponseBuilder.setKeyword(Keyword.SUCCESS).setData("DRS;Request success.");
		}
		else
		{
			LOGGER.debug("Message was never responded.  Timeout passed.");
			serverResponseBuilder.setKeyword(Keyword.ERRONEOUS).setData("Timeout time has passed.");
		}

		ConnectionMessage serverResponse =serverResponseBuilder.finalizeObject(); 
		if(serverResponse.getKeyword() == Keyword.ERRONEOUS)
		{
			synchronized (MessageResponderHandler.CLIENT_DEVICE_MAP) 
			{
				LOGGER.debug("Removing because of time out. Removing:"+messageReceived);
				MessageResponderHandler.CLIENT_DEVICE_MAP.removeMessage(serverResponse.getIdentifyingNumber());
			}
		}

		return serverResponse;

	}

	private static ConnectionMessage getDeviceGPS(
			ConnectionMessage messageReceived,
			ConnectionMessageBuilder serverResponseBuilder) 
	{

		ConnectionMessage serverResponse =null;
		if(messageReceived.getResponded())
		{
			serverResponse = serverResponseBuilder.setKeyword(Keyword.SUCCESS).setData(messageReceived.getData()).finalizeObject();
		}
		else
		{
			serverResponse = serverResponseBuilder.setKeyword(Keyword.ERRONEOUS).setData("Timeout time has passed.").finalizeObject();

		}

		synchronized(MessageResponderHandler.CLIENT_DEVICE_MAP)
		{
			MessageResponderHandler.CLIENT_DEVICE_MAP.removeMessage(messageReceived.getIdentifyingNumber());
		}

		return serverResponse;
	}


	private static ConnectionMessage getDeviceDiagnostic(
			ConnectionMessage messageReceived,
			ConnectionMessageBuilder serverResponseBuilder) 
	{

		ConnectionMessage serverResponse = null;
		if(messageReceived.getResponded())
		{
			serverResponse = serverResponseBuilder.setKeyword(Keyword.SUCCESS).setData(messageReceived.getData()).finalizeObject();
		}
		else
		{
			serverResponse = serverResponseBuilder.setKeyword(Keyword.ERRONEOUS).setData("Timeout time has passed.").finalizeObject();

		}

		synchronized(MessageResponderHandler.CLIENT_DEVICE_MAP)
		{
			MessageResponderHandler.CLIENT_DEVICE_MAP.removeMessage(messageReceived.getIdentifyingNumber());
		}

		return serverResponse;
	}

	private static ConnectionMessage actOnDeviceKeyword(
			ConnectionMessage messageReceived,
			ConnectionMessageBuilder serverResponseBuilder) 
	{
		ConnectionMessage serverResponse =null;
		Thread cleanUpThread = new Thread(new ServerCleanupThread(messageReceived,MessageResponderHandler.THREAD_TIME_OUT));
		cleanUpThread.start();
		switch(messageReceived.getKeyword())
		{
		case REQUEST:
			serverResponse = parseFileDataAndDatabaseUploadAndRegisterDevice(messageReceived,serverResponseBuilder);
			break;
		case GPS:
		case DIAGNOSTIC:
		case UPLOADED:
			verifyOrRegisterDevice(messageReceived);
			serverResponse = ConnectionUtilities.produceSuccessMessage(messageReceived,serverResponseBuilder,"SCC;Processed Succesfully.");
			break;
		default:
			serverResponse = ConnectionUtilities.produceUnknownKeywordMessage(messageReceived,serverResponseBuilder);


		}
		return serverResponse;
	}

	//DONE
	private static ConnectionMessage parseFileDataAndDatabaseUploadAndRegisterDevice(
			ConnectionMessage messageReceived,
			ConnectionMessageBuilder serverResponseBuilder) 
	{

		ConnectionMessage serverResponse = ConnectionUtilities.parseFileDataAndDatabaseUpload(messageReceived,serverResponseBuilder);
		verifyOrRegisterDevice(messageReceived);

		return serverResponse;
	}

	private static void verifyOrRegisterDevice(ConnectionMessage messageReceived) 
	{
		LOGGER.debug("Verifying and registering device.");
		ConnectionMessage found=null;
		synchronized (MessageResponderHandler.CLIENT_DEVICE_MAP) 
		{
			LOGGER.debug("Looking for previous connection matching with ",messageReceived);
			found = MessageResponderHandler.CLIENT_DEVICE_MAP.getPreviousConnectionMessage(messageReceived.getIdentifyingNumber());
			if(found != null)
			{
				LOGGER.debug("Previous connection found.  Will remove:",found);
				MessageResponderHandler.CLIENT_DEVICE_MAP.removeMessage(found.getIdentifyingNumber());
			}
			else
			{
				LOGGER.debug("No previous connection found.  Registering.");
				MessageResponderHandler.CLIENT_DEVICE_MAP.insertConnection(messageReceived);
			}

		}
		//Did this here instead of the other "if" to do as little as possible inside synchronized block.
		if(found!=null)
		{
			//Notifies the thread on wait according to continue unlocking the thread.
			LOGGER.debug("Freeing locked response thread.");
			synchronized(found)
			{
				found.setResponded();
				found.notifyAll();
			}
		}

	}

}
