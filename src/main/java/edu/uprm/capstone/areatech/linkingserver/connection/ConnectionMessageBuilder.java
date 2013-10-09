package edu.uprm.capstone.areatech.linkingserver.connection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ConnectionMessageBuilder
{
	private String identifyingNumber="0000000";
//	private String keyword="ERR";
	private Keyword keyword=Keyword.ERRONEOUS;
	private String data="";
	private ConnectionType type=ConnectionType.ERRONEOUS;
	
	final static Logger logger = LoggerFactory.getLogger(ConnectionMessageBuilder.class);
	
	public static ConnectionMessageBuilder createNewConnectionMessageBuilder()
	{
		return new ConnectionMessageBuilder();
	}
	
	public ConnectionMessageBuilder setIdentifyingNumber(String identifyingNumber)
	{
		this.identifyingNumber= identifyingNumber;
		return this;
	}
	
	public ConnectionMessageBuilder setKeyword(Keyword keyword)
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
		ConnectionMessage finalMessage = new ConnectionMessage(identifyingNumber,keyword,data,type);
		logger.debug("Finalizing message:"+finalMessage);
		return finalMessage;
	}
	
	
	
	

}
