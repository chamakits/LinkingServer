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

import edu.uprm.capstone.areatech.linkingserver.connection.MessageResponderHandler;
import edu.uprm.capstone.areatech.linkingserver.connection.protocol.MessageCodecFactory;


public class ClientManager 
{
	
	public static void main(String[] args) throws IOException 
	{
		
		int PORT = 6189;
		
		if(args.length>0)
		{
			PORT = Integer.valueOf(args[0]);
		}
		
		IoAcceptor acceptor = new NioSocketAcceptor();
		
		DefaultIoFilterChainBuilder chain = acceptor.getFilterChain();
		chain.addLast("logger", new LoggingFilter());
		chain.addLast
		("text_protocol", new ProtocolCodecFilter(new MessageCodecFactory()));
		
//		acceptor.setHandler(new MessageHandler());
		acceptor.setHandler(new MessageResponderHandler());
		
		
		IoSessionConfig ioConfig = acceptor.getSessionConfig();
		ioConfig.setReadBufferSize(17);
		ioConfig.setIdleTime(IdleStatus.BOTH_IDLE, 10);
		
		acceptor.bind(new InetSocketAddress(PORT));
		
	}
	

}
