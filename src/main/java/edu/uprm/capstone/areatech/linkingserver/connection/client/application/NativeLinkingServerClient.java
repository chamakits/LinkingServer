package edu.uprm.capstone.areatech.linkingserver.connection.client.application;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sun.corba.se.impl.orbutil.threadpool.TimeoutException;

import edu.uprm.capstone.areatech.linkingserver.connection.ConnectionMessage;
import edu.uprm.capstone.areatech.linkingserver.connection.ConnectionMessageBuilder;
import edu.uprm.capstone.areatech.linkingserver.connection.ConnectionType;
import edu.uprm.capstone.areatech.linkingserver.connection.Keyword;
import edu.uprm.capstone.areatech.linkingserver.connection.MessageResponderHandler;

public class NativeLinkingServerClient 
{
	private static final Logger LOGGER = LoggerFactory.getLogger(NativeLinkingServerClient.class);


	private final String host;
	private final int port;

	private Socket client;

	private BufferedReader reader;
	private PrintWriter writer;

	private final int TIME_OUT=MessageResponderHandler.THREAD_TIME_OUT;  
	private ConnectionMessage messageToSend;
	private ConnectionMessage errorMessage=null;
	private ConnectionMessageBuilder messageBuilder=null;

	public NativeLinkingServerClient(String host, int port)
	{
		this.host = host;
		this.port = port;
	}

	public void connect(ConnectionMessage connectionMessage)
	{
		messageBuilder =new ConnectionMessageBuilder();
		this.messageToSend = connectionMessage;
		messageBuilder.setType(connectionMessage.getType());
		messageBuilder.setIdentifyingNumber(connectionMessage.getIdentifyingNumber());
		messageBuilder.setKeyword(Keyword.ERRONEOUS);
		try 
		{
			client = new Socket(host, port);
			client.setSoTimeout(TIME_OUT);

			this.reader = 
				new BufferedReader(new InputStreamReader(client.getInputStream()));

			this.writer = 
				new PrintWriter(client.getOutputStream(),true);

		} 
		catch (UnknownHostException e) 
		{
			messageBuilder.setData("CON;Connection error.Host not found.");
			this.errorMessage = messageBuilder.finalizeObject();
		} 
		catch (IOException e) 
		{
			messageBuilder.setData("HST;Problem connecting to host.");
			this.errorMessage = messageBuilder.finalizeObject();
		}
	}

	public ConnectionMessage writeAndGetResponse()
	{
		if(errorMessage!=null)
		{
			return errorMessage;
		}
		String sending = messageToSend.toString();
		LOGGER.info("Sending:"+sending);
		writer.write(sending);
		writer.flush();


		char[] buff = new char[2];
		int totalRead = 0;
		int read=0;

		String message ="";
		while(totalRead<buff.length)
		{
			try 
			{
				read = reader.read(buff);
			} 
			catch (IOException e) 
			{
				
				this.messageBuilder.setData("CON;Error reading from connection.");
				return this.messageBuilder.finalizeObject();
				
			}
			for(int i =0; i < read ; ++i)
			{
				message+=buff[i];
				buff[i]='\0';
			}
			totalRead+=read;
		}

		int sizeOfData = (message.charAt(0)-'0')*10;
		sizeOfData+=(message.charAt(1)-'0');

		buff=new char[
		              ConnectionMessage.TYPE_SIZE+
		              ConnectionMessage.IDENTIFYING_NUMBER_SIZE+
		              ConnectionMessage.KEYWORD_SIZE+
		              ConnectionMessage.DIVIDERS_SIZE+
		              sizeOfData];
		totalRead=0;
		while(totalRead<buff.length)
		{
			try 
			{
				read = reader.read(buff);
			} 
			catch (IOException e) 
			{
				this.messageBuilder.setData("CON;Error reading from connection.");
				return this.messageBuilder.finalizeObject();
			}
			for(int i =0; i < read ; ++i)
			{
				message+=buff[i];
				buff[i]='\0';
			}
			totalRead+=read;
		}


		LOGGER.info("Received:"+message);

		return ConnectionMessage.connectionMessageFromString(message);
	}

	public void close() throws IOException
	{
		reader.close();
		writer.close();
		client.close();
	}

}
