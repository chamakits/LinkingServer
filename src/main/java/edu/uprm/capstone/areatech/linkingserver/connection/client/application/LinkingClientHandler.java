package edu.uprm.capstone.areatech.linkingserver.connection.client.application;

import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IoSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.uprm.capstone.areatech.linkingserver.connection.ConnectionMessage;
import edu.uprm.capstone.areatech.linkingserver.connection.ConnectionMessageBuilder;
import edu.uprm.capstone.areatech.linkingserver.connection.ConnectionType;
import edu.uprm.capstone.areatech.linkingserver.connection.Keyword;

public class LinkingClientHandler extends IoHandlerAdapter 
{

	private final static Logger LOGGER = LoggerFactory.getLogger(LinkingClientHandler.class);


	private boolean finished;

	private final ConnectionMessageBuilder messageReceivedBuilder;
	
//	private String identifyingNumber;
//	private String data;
//	private Keyword keyword;

//	private ConnectionType connectionType;

	public LinkingClientHandler( ConnectionType connectionType, String identifyingNumber,Keyword keyword, String data) 
	{
//		this.identifyingNumber=identifyingNumber;
//		this.data=data;
//		this.keyword=keyword;
//		this.connectionType = connectionType;
		messageReceivedBuilder = new ConnectionMessageBuilder();
		messageReceivedBuilder.setData(data).setIdentifyingNumber(identifyingNumber).setKeyword(keyword).setType(connectionType);
	}

	@Override
	public void sessionOpened(IoSession session) 
	{
//		// send summation requests
//		for (int i = 0; i < values.length; i++) {
//			AddMessage m = new AddMessage();
//			m.setSequence(i);
//			m.setValue(values[i]);
//			session.write(m);
//		}
		ConnectionMessage writingMessage = (ConnectionMessage) messageReceivedBuilder.finalizeObject();
		LOGGER.info("Writing:"+writingMessage);
		session.write(writingMessage);
	}

	@Override
	public void messageReceived(IoSession session, Object message) {
		// server only sends ResultMessage. otherwise, we will have to identify
		// its type using instanceof operator.
		ConnectionMessage receivedMessage = (ConnectionMessage) message;
		LOGGER.info("Received:"+message.toString());
	}

	@Override
	public void exceptionCaught(IoSession session, Throwable cause) {
		session.close(true);
	}



}
