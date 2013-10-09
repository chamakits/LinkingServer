package edu.uprm.capstone.areatech.linkingserver.connection.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class SimpleTestClient
{

	public static void main(String[] args) throws UnknownHostException, IOException, InterruptedException
	{

		Socket client = new Socket("localhost",6189);

		BufferedReader reader = new BufferedReader
		(new InputStreamReader(client.getInputStream()));

		PrintWriter writer = 
			new PrintWriter(client.getOutputStream(),true);

		String str = "04:A:7877877024:RQT:NONE\0";
		System.out.println("Will write:"+str);
		writer.write(str);
		writer.flush();
		System.out.println("Wrote write:"+str);
		char[] buff = new char[str.length()];
		int totalRead = 0;
		int read=0;
		
		String message ="";
		while(totalRead<str.length())
		{
			Thread.sleep(1000);
			read = reader.read(buff);
			for(int i =0; i < read ; ++i)
			{
				System.out.println("Read so far:"+message);
				message+=buff[i];
				buff[i]='\0';
			}
			totalRead+=read;
			System.out.println("Read:"+totalRead);
		}
		System.out.println("RECEIVED="+message);
		client.close();
		reader.close();
		writer.close();

	}

}
