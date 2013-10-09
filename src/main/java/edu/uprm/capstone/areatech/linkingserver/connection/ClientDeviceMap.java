package edu.uprm.capstone.areatech.linkingserver.connection;

import java.util.HashMap;


public class ClientDeviceMap
{
	private static final ClientDeviceMap map;
	
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
		this.connectionMapping.put(connectionMessage.getIdentifyingNumber(), connectionMessage);
	}
	
	public ConnectionMessage removeMessage(String identifyingString)
	{
		return this.connectionMapping.remove(identifyingString);
	}
//	
//	public void appendApplicationSession(IoSession session)
//	{
//		this.applicationSession = session;
//	}
//	
//	public IoSession getApplicationSession()
//	{
//		return this.applicationSession;
//	}

}
