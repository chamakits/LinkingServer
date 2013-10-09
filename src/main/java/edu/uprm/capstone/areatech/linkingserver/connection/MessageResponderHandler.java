package edu.uprm.capstone.areatech.linkingserver.connection;

import java.net.InetSocketAddress;

import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.uprm.capstone.areatech.linkingserver.ClientManager;
import edu.uprm.capstone.areatech.linkingserver.utilities.ConnectionUtilities;

public class MessageResponderHandler extends IoHandlerAdapter
{


	public static final ClientDeviceMap CLIENT_DEVICE_MAP;
	public static final int THREAD_TIME_OUT=90*1000;
	final static Logger LOGGER = LoggerFactory.getLogger(MessageResponderHandler.class);
	final static int MAX_IDLE_COUNT=THREAD_TIME_OUT/(10000);
	final static String SESSION_MESSAGE="Session Message";

	static
	{
		CLIENT_DEVICE_MAP= ClientDeviceMap.getInstance();
	}
	@Override
	public void sessionCreated(IoSession session) throws Exception
	{
		super.sessionCreated(session);
		InetSocketAddress remoteAddress = (InetSocketAddress) session.getRemoteAddress();
		LOGGER.debug("Connection opened from:"+remoteAddress.getAddress().getHostAddress());
		LOGGER.debug("Hostname:"+remoteAddress.getHostName());
		LOGGER.debug("On port:"+remoteAddress.getPort());
	}
	@Override
	public void messageReceived(IoSession session, Object message) throws Exception
	{
		/**
		 * Plan2:
		 * 1)If there is already a connection with the same identifying number:
		 * 	A)If it is of the same type (Application or Device) then respond to newest with "ERR" keyword and data "Previous connection of same type already in 
		 * 		process."
		 * 	B)If it isn't of the same type, then determine action according to type:
		 * 		i)If of type "Application" see keyword:
		 * 			a)For keyword "DAT" unregister previously existing "Device"
		 * 			and if all data has been parsed already, then notify "Application".
		 * 			Remove registered "Device".
		 * 			b)For keyword "GPS" get previously existing "Device"
		 * 			check the data sent, process it and convert it according to GPS 
		 * 			formula, and send to "Application."
		 * 			Remove registered "Device".
		 * 			c)For keyword "DIA" get previously existing "Device"
		 * 			check the data sent, process it and convert it according to
		 * 			Diagnostic formatting and send to "Application".
		 * 			Remove registered "Device".
		 * 		ii)If of type "Device" see keyword:
		 * 			a)For keyword "DAT" find file uploaded, parse it, and upload to DB.  Then notify
		 * 			the existing device that data is ready.
		 * 			Remove registered "Application".
		 * 			b)For keyword "GPS" parse GPS data sent, and send it to already registered application.
		 * 			Remove registered "Application".
		 * 			c)For keyword "DIA" parse  DIA data sent, and send it to the already registered application.
		 * 			Remove registered "Application".
		 * 			d)For keyword "WEK" is for weekly data update.  Parse file, update to DB, and done.
		 * 			No need to remove registered "Application", there SHOULDN'T be one. 
		 * 		
		 * 2)If it isn't determine the type of the connection received.
		 * 	A)Send it to process according to type.
		 * 		i)If of type "Application" see keyword:
		 * 			a)For keyword "DAT" register and wait until Device connects.  Then see B-ii-a.
		 * 			b)For keyword "GPS" register and wait until Device connects.  Then see B-ii-b.
		 * 			c)For keyword "DIA" register and wait until Device connects.  Then see B-ii-c.
		 * 		ii)If of type "Device" see keyword:
		 * 			a)For keyword "DAT" take message, see data to get filename, retrieve file from FTP, 
		 * 			parse data into Java objects, insert Java objects into DB.  Then verify if there is 
		 * 			something registered.  If not, then register.  If there is, verify if of type application, and notify.
		 * 			b)For keyword "GPS" register, and wait for B-i-b.
		 * 			c)For keyword "DIA" register, and wait for B-i-c.
		 * 			d)For keyword "WEK" do same as for "DAT" in 2-A-ii-a, but don't notify or register.
		 * 	
		 **/
		ConnectionMessageBuilder serverResponseBuilder = ConnectionMessageBuilder.createNewConnectionMessageBuilder();
		ConnectionMessage messageReceived = (ConnectionMessage)message;

		session.setAttribute(SESSION_MESSAGE, messageReceived);

		ConnectionMessage serverResponse =null;
		String receivedNumber = messageReceived.getIdentifyingNumber();
		serverResponseBuilder.setIdentifyingNumber(receivedNumber).setType(ConnectionType.SERVER); 
		if(ConnectionUtilities.isBadlyFormattedMessage(messageReceived))
		{
			LOGGER.debug("Badly formatted message.");
			serverResponseBuilder.setKeyword(Keyword.ERRONEOUS).setData("Message received badly formatted.");
			serverResponse=(serverResponseBuilder.finalizeObject());
		}

		//Determine if found of same type.
		ConnectionMessage messageWithSameKey= ConnectionUtilities.findSameKey(receivedNumber, CLIENT_DEVICE_MAP);

		//When a previous connection isn't found.
		if(messageWithSameKey==null)
		{
			serverResponse = UniqueConnectionProcessor.processFirstMessageAccordingToType(messageReceived,serverResponseBuilder);
			LOGGER.debug("Didn't find message with same key.  Response "+serverResponse);
		}
		//When previous connection found of same type.
		else if(messageWithSameKey.getType()==messageReceived.getType())
		{
			serverResponseBuilder.setKeyword(Keyword.ERRONEOUS).setData("Previously found message of same type.").setType(ConnectionType.SERVER);
			serverResponse= (serverResponseBuilder.finalizeObject());
			LOGGER.debug("Found message with same key and same type.  Response "+serverResponse);
		}
		//When previous connection found not of the same type.
		else
		{
			serverResponse = PreviousConnectionProcessor.processPreviousMessageAccordingToType(messageReceived, messageWithSameKey,serverResponseBuilder);
			LOGGER.debug("Found message with same key but not of same type.  Response "+ serverResponse);
		}

		if(serverResponse!=null)
		{

			LOGGER.debug("Responding with:"+serverResponse);
			if(messageReceived.getType() == ConnectionType.DEVICE)
			{
				ConnectionUtilities.prepareResponseForDevice(serverResponse);
			}
			session.write(serverResponse);
		}

		LOGGER.debug("Ending processing of: "+messageReceived);

	}

	@Override
	public void exceptionCaught(IoSession session, Throwable cause)
	throws Exception 
	{
		super.exceptionCaught(session, cause);
		if(ClientManager.infiniteDebug)
		{
			LOGGER.debug("EXCEPTION CAUGHT. Shutting down.");
			System.exit(1);
		}
	}

	@Override
	public void sessionIdle( IoSession session, IdleStatus status ) throws Exception
	{
		if(session.getIdleCount(status)>=MAX_IDLE_COUNT)
		{
			session.close(true);

			ConnectionMessage message = (ConnectionMessage) session.getAttribute(SESSION_MESSAGE);
			if(message !=null)
			{
				synchronized (CLIENT_DEVICE_MAP) 
				{
					CLIENT_DEVICE_MAP.removeMessage(message.getIdentifyingNumber());
				}
			}
		}
		System.out.println( "IDLE " + session.getIdleCount( status ));
	}


}
