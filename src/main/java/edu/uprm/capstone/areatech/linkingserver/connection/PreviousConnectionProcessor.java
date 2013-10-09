package edu.uprm.capstone.areatech.linkingserver.connection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.uprm.capstone.areatech.linkingserver.utilities.ConnectionUtilities;

public class PreviousConnectionProcessor 
{
	final static Logger LOGGER = LoggerFactory.getLogger(PreviousConnectionProcessor.class);
	
	public static ConnectionMessage processPreviousMessageAccordingToType
	(ConnectionMessage messageReceived, ConnectionMessage messageWithSameKey, 
			ConnectionMessageBuilder serverResponseBuilder) 
	{
		
		ConnectionMessage serverResponse = null;
		//Verify type of message previously found.
		switch(messageWithSameKey.getType())
		{
		case APPLICATION:
			LOGGER.debug("Previously found application.");
			serverResponse=
				actOnApplicationFoundKeyword
				(messageReceived,messageWithSameKey, serverResponseBuilder);
			break;
		case DEVICE:
			LOGGER.debug("Previously found device.");
			serverResponse=
				actOnDeviceFoundKeyword
				(messageReceived,messageWithSameKey,serverResponseBuilder);
			break;
		case ERRONEOUS:
		default:
			serverResponse = ConnectionUtilities.produceUnknownConnectionTypeMessage(messageReceived,serverResponseBuilder);
			break;
		}
//		synchronized (M) {
//			
//		}
		LOGGER.debug("Done with: "+messageReceived+", responding with "+serverResponse);
		return serverResponse;
	}

	private static ConnectionMessage actOnApplicationFoundKeyword(
			ConnectionMessage deviceMessage,
			ConnectionMessage applicationMessage,
			ConnectionMessageBuilder serverResponseBuilder) 
	{
		if(deviceMessage.getKeyword()!=applicationMessage.getKeyword())
		{
			LOGGER.debug("Non matching keywords found.");
			serverResponseBuilder.setKeyword(Keyword.ERRONEOUS)
			.setData("Non matching keywords found APP;"+applicationMessage.getKeyword().getKeyword()+",DEV;"+deviceMessage.getKeyword().getKeyword()+".");
			return serverResponseBuilder.finalizeObject();
		}
		
		ConnectionMessage serverResponse = null;
		switch(deviceMessage.getKeyword())
		{
		case REQUEST:
			LOGGER.debug("Device:"+deviceMessage.toString()+" getting according to "+Keyword.REQUEST);
			serverResponse=ConnectionUtilities.parseFileDataAndDatabaseUpload(deviceMessage,serverResponseBuilder);
			break;
		case GPS:
			LOGGER.debug("Device:"+deviceMessage.toString()+" getting according to "+Keyword.GPS);
			serverResponse=ConnectionUtilities.parseAndInsertGPSData(deviceMessage,serverResponseBuilder);
			break;
		case DIAGNOSTIC:
			LOGGER.debug("Device:"+deviceMessage.toString()+" getting according to "+Keyword.DIAGNOSTIC);
			serverResponse=ConnectionUtilities.parseAndInsertDiagnosticData(deviceMessage,serverResponseBuilder);
			break;
		default:
			serverResponse=ConnectionUtilities.produceUnknownKeywordMessage(deviceMessage, serverResponseBuilder);
			
		}
		
		synchronized(MessageResponderHandler.CLIENT_DEVICE_MAP)
		{
			MessageResponderHandler.CLIENT_DEVICE_MAP
			.removeMessage(deviceMessage.getIdentifyingNumber());
		}
		synchronized(applicationMessage)
		{
			applicationMessage.setResponded();
			applicationMessage.setData(serverResponse.getData());
			applicationMessage.notifyAll();			
		}
		return serverResponse;

	}

	private static ConnectionMessage actOnDeviceFoundKeyword(
			ConnectionMessage applicationMessage,
			ConnectionMessage deviceMessage,
			ConnectionMessageBuilder serverResponseBuilder) 
	{
		if(deviceMessage.getKeyword()!=applicationMessage.getKeyword())
		{
			serverResponseBuilder.setKeyword(Keyword.ERRONEOUS)
			.setData("Non matching keywords found APP;"+applicationMessage.getKeyword().getKeyword()+",DEV;"+deviceMessage.getKeyword().getKeyword()+".");
			return serverResponseBuilder.finalizeObject();
		}
		
		ConnectionMessage serverResponse = null;
		switch(applicationMessage.getKeyword())
		{
		case REQUEST:
			serverResponse = getRequestResponse(deviceMessage,serverResponseBuilder);
			break;
		case GPS:
			serverResponse = getGPSResponse(deviceMessage,serverResponseBuilder);
			break;
		case DIAGNOSTIC:
			serverResponse = getDiagnosticRespone(deviceMessage,serverResponseBuilder);
			break;
		default:
			serverResponse=ConnectionUtilities.produceUnknownKeywordMessage(deviceMessage, serverResponseBuilder);
		}
		
		synchronized(MessageResponderHandler.CLIENT_DEVICE_MAP)
		{
			MessageResponderHandler.CLIENT_DEVICE_MAP
			.removeMessage(deviceMessage.getIdentifyingNumber());
		}
		
		return serverResponse;
		
	}

	private static ConnectionMessage getRequestResponse(
			ConnectionMessage deviceMessage,
			ConnectionMessageBuilder serverResponseBuilder) 
	{
		
		ConnectionUtilities.removeMessage(deviceMessage.getIdentifyingNumber());
		
		return ConnectionUtilities.produceSuccessMessage(deviceMessage, serverResponseBuilder,"RSR;Responded request successfully.");
	}

	private static ConnectionMessage getGPSResponse(
			ConnectionMessage deviceMessage,
			ConnectionMessageBuilder serverResponseBuilder) {
		
		return ConnectionUtilities.parseGPSData(deviceMessage, serverResponseBuilder);
	}

	private static ConnectionMessage getDiagnosticRespone(
			ConnectionMessage deviceMessage,
			ConnectionMessageBuilder serverResponseBuilder) {
		
		serverResponseBuilder.setKeyword(Keyword.SUCCESS).setData(deviceMessage.getData());	

		ConnectionUtilities.removeMessage(deviceMessage.getIdentifyingNumber());
		
		return serverResponseBuilder.finalizeObject();
	}
}
