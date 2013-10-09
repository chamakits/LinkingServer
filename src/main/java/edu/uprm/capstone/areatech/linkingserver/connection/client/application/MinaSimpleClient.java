package edu.uprm.capstone.areatech.linkingserver.connection.client.application;

import java.net.InetSocketAddress;

import org.apache.mina.core.RuntimeIoException;
import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.logging.LoggingFilter;
import org.apache.mina.transport.socket.nio.NioSocketConnector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.uprm.capstone.areatech.linkingserver.connection.ConnectionType;
import edu.uprm.capstone.areatech.linkingserver.connection.Keyword;
import edu.uprm.capstone.areatech.linkingserver.connection.protocol.MessageCodecFactory;

public class MinaSimpleClient 
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





		// Configure the service.
		connector.setConnectTimeoutMillis(CONNECT_TIMEOUT);

		connector.getFilterChain().addLast("codec",
				new ProtocolCodecFilter(new MessageCodecFactory()));

		connector.getFilterChain().addLast("logger", new LoggingFilter());
		connector.setHandler(new LinkingClientHandler(connectionType,identifyingNumber,keyword, data));

		IoSession session = null;

		try 
		{
			ConnectFuture future = connector.connect(new InetSocketAddress(host, port));
			LOGGER.debug("Future obtained.");
			future.awaitUninterruptibly();
//			future.
			LOGGER.debug("EndedFunction.");
			session = future.getSession();
			LOGGER.debug("Session obtained.");
		} 
		catch (RuntimeIoException e) 
		{
			System.err.println("Failed to connect.");
			e.printStackTrace();
			Thread.sleep(5000);
		}


		// wait until the summation is done
//		session.getCloseFuture().awaitUninterruptibly();
//		LOGGER.debug("Closing Future.");
		connector.dispose();
		LOGGER.info("Connector Disposed.");
	}


}
