package edu.uprm.capstone.areatech.linkingserver.connection.client.application;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sun.corba.se.impl.orbutil.threadpool.TimeoutException;

import edu.uprm.capstone.areatech.linkingserver.connection.ConnectionMessage;
import edu.uprm.capstone.areatech.linkingserver.connection.ConnectionMessageBuilder;
import edu.uprm.capstone.areatech.linkingserver.connection.ConnectionType;
import edu.uprm.capstone.areatech.linkingserver.connection.Keyword;

public class TestNativeClient 
{
	private static final Logger LOGGER = LoggerFactory.getLogger(TestNativeClient.class);
	
	public static void main(String[] args) throws IOException, TimeoutException 
	{
		String host="localhost";
		int port = 6189;

		ConnectionType connectionType = ConnectionType.APPLICATION;
		String identifyingNumber="7877872020";
		Keyword keyword = Keyword.REQUEST;
		String data = "NONE";

		int checkArg=0;
		if(args.length>(checkArg))
		{
			host=args[checkArg];
		}

		if(args.length>(++checkArg))
		{
			port = Integer.valueOf(args[checkArg], 10);
		}
		if(args.length>(++checkArg))
		{
			connectionType = ConnectionType.determineClientType(args[checkArg]);
		}
		if(args.length>(++checkArg))
		{
			identifyingNumber = args[checkArg];
		}
		if(args.length>(++checkArg))
		{
			keyword = Keyword.determineKeyword(args[checkArg]);
		}
		if(args.length>(++checkArg))
		{
			data = args[checkArg];
		}
		
		NativeLinkingServerClient client = new NativeLinkingServerClient(host, port);
		ConnectionMessageBuilder builder = new ConnectionMessageBuilder();
		builder.setType(connectionType).setIdentifyingNumber(identifyingNumber).setKeyword(keyword).setData(data);
		client.connect(builder.finalizeObject());
		ConnectionMessage message = client.writeAndGetResponse();
		
		System.out.println("Received:\n"+message);
		
		client.close();
		
	}

}
