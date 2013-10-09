package edu.uprm.capstone.areatech.linkingserver.connection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ServerCleanupThread implements Runnable 
{

	final static Logger LOGGER = LoggerFactory.getLogger(ServerCleanupThread.class);
	
	private ConnectionMessage messageReceived;
	private int threadTimeOut;

	@SuppressWarnings("unused")
	private ServerCleanupThread()
	{
		throw new AssertionError("Do not use this initializer.");
	}
	
	public ServerCleanupThread(ConnectionMessage messageReceived,int threadTimeOut) 
	{
		this.messageReceived = messageReceived;
		this.threadTimeOut = threadTimeOut;
	}

	@Override
	public void run() 
	{
		try 
		{
			Thread.sleep(threadTimeOut);
		} 
		catch (InterruptedException e) 
		{
			LOGGER.debug("Clean up thread has been interrupted");
		}
		ConnectionMessage removed =null;
		synchronized (MessageResponderHandler.CLIENT_DEVICE_MAP) 
		{
			removed = MessageResponderHandler.CLIENT_DEVICE_MAP.removeMessage(messageReceived.getIdentifyingNumber());
			
		}
		if(LOGGER.isDebugEnabled())
		{
			if(removed!=null)
			{
				LOGGER.debug("Removed succesfully:"+removed);
			}
			else
			{
				LOGGER.debug("Nothing to remove found.");
			}
		}
	}

}
