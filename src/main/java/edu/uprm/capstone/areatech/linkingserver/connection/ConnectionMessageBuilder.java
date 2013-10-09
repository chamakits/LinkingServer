package edu.uprm.capstone.areatech.linkingserver.connection;

public class ConnectionMessageBuilder
{
	private String identifyingNumber;
	private String keyword;
	private String data;
	private ConnectionType type;
	
	public static ConnectionMessageBuilder createNewClientBuilder()
	{
		return new ConnectionMessageBuilder();
		
	}
	
	public ConnectionMessageBuilder setIdentifyingNumber(String identifyingNumber)
	{
		this.identifyingNumber= identifyingNumber;
		return this;
	}
	
	public ConnectionMessageBuilder setKeyword(String keyword)
	{
		this.keyword=keyword;
		return this;
	}
	
	public ConnectionMessageBuilder setData(String data)
	{
		this.data=data;
		return this;
	}
	
	public ConnectionMessageBuilder setType(ConnectionType type)
	{
		this.type = type;
		return this;
	}
	
	public ConnectionMessage finalizeObject()
	{
		return new ConnectionMessage(identifyingNumber,keyword,data,type);
	}
	
	
	
	

}
