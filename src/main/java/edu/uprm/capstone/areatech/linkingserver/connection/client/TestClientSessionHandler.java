package edu.uprm.capstone.areatech.linkingserver.connection.client;

import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IoSession;

public class TestClientSessionHandler extends IoHandlerAdapter
{
	
	@Override
	public void messageReceived(IoSession session, Object message) throws Exception
	{
		// TODO Auto-generated method stub
		super.messageReceived(session, message);
	}

}
