package edu.uprm.capstone.areatech.linkingserver.connection.protocol;

import java.util.StringTokenizer;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.CumulativeProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolDecoderOutput;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.uprm.capstone.areatech.linkingserver.connection.ConnectionMessage;
import edu.uprm.capstone.areatech.linkingserver.connection.ConnectionMessageBuilder;
import edu.uprm.capstone.areatech.linkingserver.connection.ConnectionType;
import edu.uprm.capstone.areatech.linkingserver.connection.Keyword;
import edu.uprm.capstone.areatech.linkingserver.connection.log.EventLogParser;
import edu.uprm.capstone.areatech.linkingserver.utilities.Converter;

public class MessageDecoder extends CumulativeProtocolDecoder 
{
	final static Logger LOGGER = LoggerFactory.getLogger(MessageDecoder.class);
	

//	private int MAX_MESSAGE_SIZE=140;
	@Override
	protected boolean doDecode(IoSession session, IoBuffer buffer,ProtocolDecoderOutput out) throws Exception
	{

		
		if(buffer.remaining()<ConnectionMessage.DATA_DIGIT_AMOUNT)
		{
			return false;
		}
		else
		{
			//Mark buffer in case once the buffer is read it is determined that enough data hasn't been retrieved.
			buffer.mark();
			byte[] dataSize = new byte[ConnectionMessage.DATA_DIGIT_AMOUNT];
			buffer.get(dataSize, 0, ConnectionMessage.DATA_DIGIT_AMOUNT);
			//TODO this is giving negative results.
			int desiredBytes = 
				(Converter.byteArrayIntStringToInt(dataSize)+ConnectionMessage.MINIMUM_MESSAGE_SIZE)
				-ConnectionMessage.DATA_DIGIT_AMOUNT;
			if(desiredBytes<=0)
			{
				return false;
			}
			
			String messageSoFar = new String(buffer.array());
			LOGGER.debug("CurrentlyRecieved="+messageSoFar);
			if(buffer.remaining() < desiredBytes)
			{				
				buffer.reset();
				return false;
			}

			else
			{
				byte[] receivedBytes = new byte[desiredBytes];
				buffer.get(receivedBytes);
				/*
				 * The string, as is, contains a ':' at the beginning which needs
				 * to be removed, so the StringTokenizer can properly get the data it needs.
				 * Because of this, substring(1) is used.
				*/
				String clientMessage = new String(receivedBytes).substring(1);
//				System.out.println("DEBUG:MESSAGE="+clientMessage);
				String tempString="";

				/**
				 * This works assuming the standard communication is formatted as such:
				 * "TYPE(1):IDENTIFYING_NUMBER(10):KEYWORD(3):DATA"
				 * for example, an application requesting information would be:
				 * "00:A:7873335555:RQT:"
				 * or sending data would be:
				 * "04:A:7873335555:UPT:SOME"
				 */
				StringTokenizer tokenizer = new StringTokenizer(clientMessage,":");
				ConnectionMessageBuilder builder= ConnectionMessageBuilder.createNewConnectionMessageBuilder();

				tempString=tokenizer.nextToken();
				builder.setType(ConnectionType.determineClientType(tempString));

				tempString=tokenizer.nextToken();
				builder.setIdentifyingNumber(tempString);

				tempString=tokenizer.nextToken();
				Keyword keyword=Keyword.determineKeyword(tempString);
				builder.setKeyword(keyword);

				//This could bring trouble.  It is nonallowed character now.
				tempString=tokenizer.nextToken();
				builder.setData(tempString.replaceAll("`", ""));

				ConnectionMessage clientConnection = builder.finalizeObject();
				out.write(clientConnection);

				return true;
			}

		}
	}
}
