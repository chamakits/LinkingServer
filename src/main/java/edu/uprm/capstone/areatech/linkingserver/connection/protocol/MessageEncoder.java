package edu.uprm.capstone.areatech.linkingserver.connection.protocol;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolEncoder;
import org.apache.mina.filter.codec.ProtocolEncoderOutput;

import edu.uprm.capstone.areatech.linkingserver.connection.ConnectionMessage;

/**
 * This will only be used by the client application.
 * @author s802052585
 *
 */
public class MessageEncoder implements ProtocolEncoder {

	@Override
	public void dispose(IoSession arg0) throws Exception 
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void encode(IoSession session, Object message, ProtocolEncoderOutput out)
			throws Exception 
	{
		ConnectionMessage clientConnection = (ConnectionMessage)message;

		String messageToSend = clientConnection.toString()+'\0';
		IoBuffer buffer = IoBuffer.allocate(messageToSend.length(),false);
		buffer.put(messageToSend.getBytes());
		buffer.flip();
		
		out.write(buffer);
	}


}
