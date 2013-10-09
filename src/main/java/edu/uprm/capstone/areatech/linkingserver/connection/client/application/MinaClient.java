package edu.uprm.capstone.areatech.linkingserver.connection.client.application;

import org.apache.mina.transport.socket.nio.NioSocketConnector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.uprm.capstone.areatech.linkingserver.connection.ConnectionType;
import edu.uprm.capstone.areatech.linkingserver.connection.Keyword;

public class MinaClient 
{
	
	private static final long CONNECT_TIMEOUT = 0;
	private static final boolean USE_CUSTOM_CODEC = false;
	
	private static final Logger LOGGER = LoggerFactory.getLogger(MinaSimpleClient.class);

	public static void main(String[] args) throws Throwable {
		NioSocketConnector connector = new NioSocketConnector();

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
		
		MinaConnection connection = new MinaConnection("localhost", 6189, 5, connectionType, identifyingNumber, 10*1000);
		connection.connect();
//		connection.sendMessage(keyword, data);
		connection.disconnect();
		System.out.println(connection.getResponse().toString());
	}

}
