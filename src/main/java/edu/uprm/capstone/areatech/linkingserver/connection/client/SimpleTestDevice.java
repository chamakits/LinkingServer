package edu.uprm.capstone.areatech.linkingserver.connection.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class SimpleTestDevice
{
	
	public static void main(String[] args) throws UnknownHostException, IOException
	{
		Socket client = new Socket("localhost",6189);

		BufferedReader reader = new BufferedReader
		(new InputStreamReader(client.getInputStream()));

		PrintWriter writer = 
			new PrintWriter(client.getOutputStream(),true);

		String str = "04:D:7877877024:RQT:NONE\0";
		writer.write(str);
		writer.flush();
		char[] buff = new char[str.length()];
		int totalRead = 0;
		int read=0;
		
		String message ="";
		while(totalRead<19)
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
