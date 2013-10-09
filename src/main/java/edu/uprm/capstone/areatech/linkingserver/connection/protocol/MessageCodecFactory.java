package edu.uprm.capstone.areatech.linkingserver.connection.protocol;

import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFactory;
import org.apache.mina.filter.codec.ProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolEncoder;

public class MessageCodecFactory implements ProtocolCodecFactory
{
	private final ProtocolEncoder encoder;
	private final ProtocolDecoder decoder;
	
	public MessageCodecFactory()
	{
		this.encoder=new MessageEncoder();
		this.decoder= new MessageDecoder();
	}

	@Override
	public ProtocolDecoder getDecoder(IoSession arg0) throws Exception
	{
		return this.decoder;
	}

	@Override
	public ProtocolEncoder getEncoder(IoSession arg0) throws Exception
	{
		return this.encoder;
	}

}
