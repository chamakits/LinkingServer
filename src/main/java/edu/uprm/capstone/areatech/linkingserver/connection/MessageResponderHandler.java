package edu.uprm.capstone.areatech.linkingserver.connection;

import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IoSession;

public class MessageResponderHandler extends IoHandlerAdapter
{
	
	private static final ClientDeviceMap CLIENT_DEVICE_MAP;
	
	static
	{
		CLIENT_DEVICE_MAP= ClientDeviceMap.getInstance();
	}
	@Override
	public void sessionCreated(IoSession session) throws Exception
	{
		// TODO Auto-generated method stub
		super.sessionCreated(session);
	}
	@Override
	public void messageReceived(IoSession session, Object message) throws Exception
	{
		ConnectionMessage messageReceived = (ConnectionMessage)message;
//		String identifyingNumber = messageReceived.getIdentifyingNumber(); 
		
		ConnectionMessage serverResponse =null;
		/**
		 * Plan:
		 * 1)Determine the type of connection received.
		 * 	A)If its an application, then I need to verify if the connecting device notification is there.
		 * 		i)If the device notification isn't there yet, then register on datastructure, and wait until it is.
		 * 			*)How to wait needs to be determined, if a simple "sleep" will suffice, or if put to wait until notified.
		 * 		ii)If the device notification is there, then take it out, and notify the application that sent the incoming
		 * 		message
		 * 	B)If its a device, then begin the process of database updating, verify datastructure.
		 * 		i)If the application is there, then take it out, and notify it.
		 * 		ii)If there is nothing there, register, and exit thread.
		 * 	C)If its neither, then......I don't know right now honestly.
		 * 2)Close up things.
		 */
		
		
//		System.out.println("DEBUG:THREAD:BEFORE="+Thread.currentThread());
		switch(messageReceived.getType())
		{
			case APPLICATION:
				
				//TODO verify if what is found is truly a device.
				serverResponse = checkIdentifyingStatusForDevice(session, messageReceived);			
				break;
			case DEVICE:
				updateDatabase(session,messageReceived);
				//TODO verify if what is found is truly an application
				serverResponse = checkIdentifyingStatusForApplication(session,messageReceived);
				break;
			default:
				throw new AssertionError("Haven't implemented behaviour for when an unexpected type is determined.");
		}
//		System.out.println("DEBUG:THREAD:AFTER="+Thread.currentThread());
//		Thread.sleep(10000);
		session.write(serverResponse);
		
		
	}
	
	private ConnectionMessage checkIdentifyingStatusForDevice(IoSession session, ConnectionMessage connectionMessage) throws InterruptedException
	{
		//TODO moves this somewhere smarter more generic.
		String identifyingNumber= connectionMessage.getIdentifyingNumber();
		ConnectionMessageBuilder builder = ConnectionMessageBuilder.createNewClientBuilder();
		builder.setType(ConnectionType.SERVER).setIdentifyingNumber(identifyingNumber).setKeyword("RDY").setData("NONE");
		ConnectionMessage newClientConnection = builder.finalizeObject();
		
		boolean clientOnWait=false;
		
		synchronized (CLIENT_DEVICE_MAP)
		{
			if(MessageResponderHandler.CLIENT_DEVICE_MAP.hasPreviousConnection(identifyingNumber))
			{
				MessageResponderHandler.CLIENT_DEVICE_MAP.removeMessage(identifyingNumber);
//				return builder.createClientConnection();
			}
			else
			{
				MessageResponderHandler.CLIENT_DEVICE_MAP.insertConnection(connectionMessage);
				clientOnWait = true;
			}
		}
		
		if(clientOnWait)
		{
			synchronized (connectionMessage)
			{
//				System.out.println("DEBUG:checkIdentifyingStatusForDevice:IN:SYNCHRONIZED:PRE:WAIT="+connectionMessage);
				connectionMessage.wait();
//				System.out.println("DEBUG:checkIdentifyingStatusForDevice:IN:SYNCHRONIZED:POST:WAIT="+connectionMessage);
			}
//			System.out.println("DEBUG:checkIdentifyingStatusForDevice:OUT:SYNCHRONIZED="+connectionMessage);
//			return builder.createClientConnection();
		}
		return newClientConnection;
		
	}
	private ConnectionMessage checkIdentifyingStatusForApplication(IoSession session, ConnectionMessage connectionMessage)
	{
		String identifyingNumber= connectionMessage.getIdentifyingNumber();
		
		ConnectionMessageBuilder builder = ConnectionMessageBuilder.createNewClientBuilder();
		builder.setType(ConnectionType.SERVER).setIdentifyingNumber(identifyingNumber).setKeyword("ACK").setData("NONE");
		
		synchronized (CLIENT_DEVICE_MAP)
		{
			ConnectionMessage applicationMessage ;
			if(MessageResponderHandler.CLIENT_DEVICE_MAP.hasPreviousConnection(identifyingNumber))
			{
				applicationMessage =MessageResponderHandler.CLIENT_DEVICE_MAP.removeMessage(identifyingNumber);
				synchronized(applicationMessage)
				{
//					System.out.println("DEBUG:checkIdentifyingStatusForApplication:IN:SYNCHRONIZED:PRE:NOTIFY="+connectionMessage);
					applicationMessage.notifyAll();
//					System.out.println("DEBUG:checkIdentifyingStatusForApplication:IN:SYNCHRONIZED:POST:NOTIFY="+connectionMessage);
				}
//				System.out.println("DEBUG:checkIdentifyingStatusForApplication:OUT:SYNCHRONIZED="+connectionMessage);
				return builder.finalizeObject();
			}
			else
			{
				MessageResponderHandler.CLIENT_DEVICE_MAP.insertConnection(connectionMessage);
				
				return builder.finalizeObject();
			}
		}
	}
	
	private void updateDatabase(IoSession session, ConnectionMessage connectionMessage)
	{
		System.out.println("DEBUG:DATABASE_INSERTION_AND_UPDATING_GOES_HERE");
	}
	
	@Override
	public void exceptionCaught(IoSession session, Throwable cause) throws Exception
	{
		super.exceptionCaught(session, cause);
		
		//TODO this isn't appropriate handling of error.
		session.close(false);
		System.exit(1);
	}

}
