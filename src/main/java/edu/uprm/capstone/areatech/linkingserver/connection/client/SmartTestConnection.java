package edu.uprm.capstone.areatech.linkingserver.connection.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class SmartTestConnection 
{
	
	public static void main(String[] args) throws UnknownHostException, IOException 
	{
		final String str= args[0];
		final String host = args[1];
		int port = Integer.valueOf(args[2]);
		
		System.out.println("DEBUG:SENDING="+str+"\nDEBUG:TO="+host+"\nDEBUG:AT="+port);
		
		Socket client = new Socket(host,port);
		
		BufferedReader reader = new BufferedReader
		(new InputStreamReader(client.getInputStream()));

		PrintWriter writer = 
			new PrintWriter(client.getOutputStream(),true);

//		String sampleServerMessage= "04:S:7877877024:RQT:NONE\0";
		
//		int lengthReceived = sampleServerMessage.length();
		
		writer.write(str);
		writer.flush();
		char[] buff = new char[str.length()];
		int totalRead = 0;
		int read=0;
		
		String message ="";
		while(totalRead<str.length())
		{
//			Thread.sleep(3000);
			read = reader.read(buff);
			for(int i =0; i < read ; ++i)
			{
				message+=buff[i];
				buff[i]='\0';
			}
			totalRead+=read;
		}
		System.out.println("RECEIVED="+message);
		client.close();
		reader.close();
		writer.close();
		
		
		
	}

}
