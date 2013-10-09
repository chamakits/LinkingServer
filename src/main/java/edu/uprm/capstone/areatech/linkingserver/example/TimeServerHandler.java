package edu.uprm.capstone.areatech.linkingserver.example;
import java.util.Date;

import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;


public class TimeServerHandler extends IoHandlerAdapter 
{

	@Override
	public void exceptionCaught(IoSession session, Throwable cause)
	throws Exception 
	{
		cause.printStackTrace();
	}

	@Override
	public void messageReceived(IoSession session, Object message)
	throws Exception 
	{
		String messageString = message.toString();
		if(messageString.equalsIgnoreCase("quit"))
		{
			session.close(true);
		}
		else
		{
			session.write((new Date()).toString());
			System.out.println("Message written...");
		}

		return;
	}

	@Override
	public void sessionIdle(IoSession session, IdleStatus status)
	throws Exception 
	{
		System.out.println("IDLE:"+session.getIdleCount(status));

	}



}
