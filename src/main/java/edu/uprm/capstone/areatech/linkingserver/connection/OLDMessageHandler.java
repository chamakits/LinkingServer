package edu.uprm.capstone.areatech.linkingserver.connection;

import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IoSession;

public class OLDMessageHandler extends IoHandlerAdapter
{
	
	@Override
	public void sessionCreated(IoSession session) throws Exception
	{
		// TODO Auto-generated method stub
		super.sessionCreated(session);
	}
	@Override
	public void messageReceived(IoSession session, Object message) throws Exception
	{
		ConnectionMessage connectionMessage = (ConnectionMessage)message;
		
		ConnectionMessageBuilder builder = ConnectionMessageBuilder.createNewClientBuilder();
		builder.setType(ConnectionType.SERVER).setIdentifyingNumber("7871213435").setKeyword("ACK").setData("");
		connectionMessage = builder.finalizeObject();
		
//		System.out.println("DEBUG:SERVERrECEIVEDdATA:"+connectionMessage.getData());
		
		session.write(connectionMessage);
	}
	
	@Override
	public void exceptionCaught(IoSession session, Throwable cause) throws Exception
	{
		super.exceptionCaught(session, cause);
		
		//TODO this isn't appropriate handling of error.
		session.close(false);
		System.exit(1);
	}

}
