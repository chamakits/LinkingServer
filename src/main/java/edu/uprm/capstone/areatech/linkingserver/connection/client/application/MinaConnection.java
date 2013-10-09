package edu.uprm.capstone.areatech.linkingserver.connection.client.application;

import java.net.InetSocketAddress;
import java.util.ArrayList;

import org.apache.mina.core.RuntimeIoException;
import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.logging.LoggingFilter;
import org.apache.mina.transport.socket.nio.NioSocketConnector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.uprm.capstone.areatech.linkingserver.connection.ConnectionMessage;
import edu.uprm.capstone.areatech.linkingserver.connection.ConnectionType;
import edu.uprm.capstone.areatech.linkingserver.connection.protocol.MessageCodecFactory;

public class MinaConnection extends IoHandlerAdapter
{
	
	private final ConnectionType connectionType;
	private final String identifyingNumber;
	private InetSocketAddress address;
	private NioSocketConnector connector;
	private int connectionTimeout;
	private final int RETRY_CONNECTION;
	private ConnectionMessage responseMessage;
	private static final Logger LOGGER = LoggerFactory.getLogger(MinaConnection.class);
	
	private ArrayList<ConnectionMessage> messages = new ArrayList<ConnectionMessage>(); 
	
	private IoSession session = null;

	private MinaConnection()
	{
		throw new AssertionError("Do not use this empty constructor.");
	}
	
	public MinaConnection(String host, int port, ConnectionType connectionType,String identifyingNumber)
	{
		this(host,port, 5,connectionType,identifyingNumber,10*1000);	
	}
	
	public MinaConnection(String host, int port, int retryConnectionAmount, ConnectionType connectionType, String identifyingNumber, int connectionTimeout)
	{		
		this.address = new InetSocketAddress(host, port);
		this.connectionType= connectionType;
		this.connectionTimeout=connectionTimeout;
		this.connector = new NioSocketConnector();
		this.RETRY_CONNECTION=retryConnectionAmount;
		this.identifyingNumber=identifyingNumber;
		
		connector.setConnectTimeoutMillis(connectionTimeout);
		connector.getFilterChain().addLast("codec",new ProtocolCodecFilter( new MessageCodecFactory()));
		connector.getFilterChain().addLast("logger", new LoggingFilter());
		connector.setHandler(this);
	}
	
	public void connect()
	{
		
		for(int retries = 0; retries < RETRY_CONNECTION; ++retries)
		{
			try
			{
				ConnectFuture future = connector.connect( this.address);
				LOGGER.debug("Future obtained.");
				future.awaitUninterruptibly(connectionTimeout);
				LOGGER.debug("Back from message.");
				session = future.getSession();
				break;
			}
			catch(RuntimeIoException e)
			{
				try 
				{
					LOGGER.info("Retrying connection.");
					Thread.sleep(2000);
					
				}
				catch (InterruptedException e1) 
				{
					e1.printStackTrace();
				}
			}
			LOGGER.info("Reconnecting.");
		}
	}
	
	public void disconnect()
	{
		if(session != null)
		{
			session.close(true).awaitUninterruptibly(connectionTimeout);
			session=null;
		}
	}
	
	@Override
	public void messageReceived(IoSession session, Object uMessage)
	{
		
		ConnectionMessage message = (ConnectionMessage)uMessage;
		this.responseMessage = message;
	}
	
	@Override
    public void messageSent(IoSession session, Object message) throws Exception 
    {
        ConnectionMessage sending = (ConnectionMessage)message;
        session.write(message);
    }
	
	
	public ConnectionMessage getResponse()
	{
		
		return this.responseMessage;
	}
	
	public boolean responseReady()
	{
		return this.responseMessage!=null;
	}
	

}
