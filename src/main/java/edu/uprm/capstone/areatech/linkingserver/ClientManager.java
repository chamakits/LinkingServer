package edu.uprm.capstone.areatech.linkingserver;
import java.io.IOException;

import java.net.InetSocketAddress;

import org.apache.mina.core.filterchain.DefaultIoFilterChainBuilder;
import org.apache.mina.core.service.IoAcceptor;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSessionConfig;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.logging.LoggingFilter;
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

import edu.uprm.capstone.areatech.linkingserver.connection.MessageResponderHandler;
import edu.uprm.capstone.areatech.linkingserver.connection.protocol.MessageCodecFactory;


public class ClientManager 
{
	final static Logger LOGGER = LoggerFactory.getLogger(ClientManager.class);
	public static boolean infiniteDebug=false;
	public static int IDLE_TIME=10;	
	
	public static void main(String[] args) throws IOException 
	{
		int PORT = 6189;
		int debugIfOne=0;
		
		
		if(args.length>0)
		{
			PORT = Integer.valueOf(args[0],10);
		}
		if(args.length>1)
		{
			debugIfOne=Integer.valueOf(args[1],10);
			if(debugIfOne==1)
				ClientManager.infiniteDebug=true;
		}
		
		IoAcceptor acceptor = new NioSocketAcceptor();
		
		LOGGER.info("Starting Linking Server on port:"+PORT);
		LOGGER.debug("Debugging on.");
		LOGGER.info(ClientManager.class.getCanonicalName());
		
		DefaultIoFilterChainBuilder chain = acceptor.getFilterChain();
		chain.addLast("logger", new LoggingFilter());
		chain.addLast
		("text_protocol", new ProtocolCodecFilter(new MessageCodecFactory()));
		
//		acceptor.setHandler(new MessageHandler());
		acceptor.setHandler(new MessageResponderHandler());
		
		
		IoSessionConfig ioConfig = acceptor.getSessionConfig();
		ioConfig.setReadBufferSize(17);
		ioConfig.setIdleTime(IdleStatus.BOTH_IDLE, IDLE_TIME);
		ioConfig.setReaderIdleTime(MessageResponderHandler.THREAD_TIME_OUT);
		ioConfig.setWriterIdleTime(MessageResponderHandler.THREAD_TIME_OUT);
		
		acceptor.bind(new InetSocketAddress(PORT));
		
	}
	

}
