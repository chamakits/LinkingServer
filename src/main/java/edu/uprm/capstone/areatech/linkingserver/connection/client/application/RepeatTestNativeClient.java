package edu.uprm.capstone.areatech.linkingserver.connection.client.application;

import java.io.IOException;

import com.sun.corba.se.impl.orbutil.threadpool.TimeoutException;

import edu.uprm.capstone.areatech.linkingserver.connection.MessageResponderHandler;

public class RepeatTestNativeClient 
{
	public static void main(String[] args) throws IOException, TimeoutException, InterruptedException 
	{
		for(;;)
		{
			TestNativeClient.main(args);
			Thread.sleep(MessageResponderHandler.THREAD_TIME_OUT/2);
		}
	}

}
