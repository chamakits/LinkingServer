package edu.uprm.capstone.areatech.linkingserver.connection;

import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class ClientDeviceMap
{
	private static final ClientDeviceMap map;
	private static final Logger LOGGER = LoggerFactory.getLogger(ClientDeviceMap.class);
	
	private final HashMap<String, ConnectionMessage> connectionMapping;
//	private IoSession applicationSession;
	
	static
	{
		map = new ClientDeviceMap();
	}
	
	public static ClientDeviceMap getInstance()
	{
		return ClientDeviceMap.map;
	}
	
	private ClientDeviceMap()
	{
		this.connectionMapping = new HashMap<String, ConnectionMessage>();
	}
	
	public ConnectionMessage getPreviousConnectionMessage(String identifyingString)
	{
		return this.connectionMapping.get(identifyingString);
	}
	
	public boolean hasPreviousConnection(String identifyingString)
	{
		return this.connectionMapping.containsKey(identifyingString);
	}
	
	public void insertConnection(ConnectionMessage connectionMessage)
	{
		LOGGER.debug("Inserting:"+connectionMessage);
		this.connectionMapping.put(connectionMessage.getIdentifyingNumber(), connectionMessage);
	}
	
	public ConnectionMessage removeMessage(String identifyingString)
	{
		LOGGER.debug("Removing according to:"+identifyingString);
		ConnectionMessage removed = this.connectionMapping.remove(identifyingString);
		if(LOGGER.isDebugEnabled())
		{
			if(removed == null)
			{
				LOGGER.debug("Didn't find anything to remove.");
			}
			else
			{
				LOGGER.debug("Found and removed:"+removed.toString());
			}
		}
		return removed;
	}
	
}
