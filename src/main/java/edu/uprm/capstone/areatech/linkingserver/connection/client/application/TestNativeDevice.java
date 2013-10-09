package edu.uprm.capstone.areatech.linkingserver.connection.client.application;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sun.corba.se.impl.orbutil.threadpool.TimeoutException;

import edu.uprm.capstone.areatech.linkingserver.connection.ConnectionMessage;
import edu.uprm.capstone.areatech.linkingserver.connection.ConnectionMessageBuilder;
import edu.uprm.capstone.areatech.linkingserver.connection.ConnectionType;
import edu.uprm.capstone.areatech.linkingserver.connection.Keyword;

public class TestNativeDevice 
{
	
	private static final Logger LOGGER = LoggerFactory.getLogger(TestNativeDevice.class);
	
	public static void main(String[] args) throws IOException, TimeoutException 
	{
//		String host="localhost";
		String host="50.17.176.127";
		int port = 6189;

		ConnectionType connectionType = ConnectionType.DEVICE;
		String identifyingNumber="7877872020";
		Keyword keyword = Keyword.REQUEST;
		String data = "01`";
//		Keyword keyword = Keyword.GPS;
//		String data = "NONE";

		
		NativeLinkingServerClient client = new NativeLinkingServerClient(host, port);
		ConnectionMessageBuilder builder = new ConnectionMessageBuilder();
		builder.setType(connectionType).setIdentifyingNumber(identifyingNumber).setKeyword(keyword).setData(data);
		client.connect(builder.finalizeObject());
		ConnectionMessage message = client.writeAndGetResponse();
		
		System.out.println("Received:\n"+message);
		
		client.close();
		
	}



}
