package edu.uprm.capstone.areatech.linkingserver.connection.client;

import org.apache.mina.core.RuntimeIoException;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.transport.socket.nio.NioSocketConnector;

import edu.uprm.capstone.areatech.linkingserver.connection.ConnectionMessageBuilder;
import edu.uprm.capstone.areatech.linkingserver.connection.ConnectionType;
import edu.uprm.capstone.areatech.linkingserver.connection.protocol.MessageCodecFactory;

public class TestClient extends IoHandlerAdapter
{
	
	
	public static void main(String[] args) throws InterruptedException
	{
		
//		final String HOST="localhost";
//		final int PORT=9123;
		
		NioSocketConnector connector = new NioSocketConnector();
		
		ConnectionMessageBuilder builder = ConnectionMessageBuilder.createNewClientBuilder();
		builder.setType(ConnectionType.SERVER).setIdentifyingNumber("7871213435").setKeyword("ACK").setData("");
//		ConnectionMessage connectionMessage = builder.finalizeObject();
		
		connector.getFilterChain().addLast("client_message", new ProtocolCodecFilter(new MessageCodecFactory()));
		connector.setHandler(new IoHandlerAdapter());
//		IoSession session;
		for (;;) {
	        try {
	            
//	            session = connector.getSession();
	            break;
	        } catch (RuntimeIoException e) {
	            System.err.println("Failed to connect.");
	            e.printStackTrace();
	            Thread.sleep(5000);
	        }
	    }
//		session.write(connectionMessage);
		
		
	}

}
