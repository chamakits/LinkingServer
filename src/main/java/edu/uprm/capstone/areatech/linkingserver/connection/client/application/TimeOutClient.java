package edu.uprm.capstone.areatech.linkingserver.connection.client.application;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;


public class TimeOutClient 
{
	
	public static void main(String[] args) throws UnknownHostException, IOException, InterruptedException 
	{
		

		String host="localhost";
		int port = 6189;

//		ConnectionType connectionType = ConnectionType.APPLICATION;
//		String identifyingNumber="7877872020";
//		Keyword keyword = Keyword.REQUEST;
//		String data = "NONE";

		int checkArg=0;
		if(args.length>(checkArg))
		{
			host=args[checkArg];
		}

		if(args.length>(++checkArg))
		{
			port = Integer.valueOf(args[checkArg], 10);
		}
		
		Socket client = new Socket(host, port);		
		
		Thread.sleep(999999999);
		
		
	}

}
