package edu.uprm.capstone.areatech.linkingserver.connection.protocol;

import java.util.StringTokenizer;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.CumulativeProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolDecoderOutput;

import edu.uprm.capstone.areatech.linkingserver.connection.ConnectionMessage;
import edu.uprm.capstone.areatech.linkingserver.connection.ConnectionMessageBuilder;
import edu.uprm.capstone.areatech.linkingserver.connection.ConnectionType;
import edu.uprm.capstone.areatech.linkingserver.utilities.Converter;

public class MessageDecoder extends CumulativeProtocolDecoder {

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
			int desiredBytes = 
				(Converter.byteArrayIntStringToInt(dataSize)+ConnectionMessage.MINIMUM_MESSAGE_SIZE)
				-ConnectionMessage.DATA_DIGIT_AMOUNT;
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
				ConnectionMessageBuilder builder= ConnectionMessageBuilder.createNewClientBuilder();

				//TODO skip the temp string.
				tempString=tokenizer.nextToken();
				builder.setType(determineClientType(tempString));

				tempString=tokenizer.nextToken();
				builder.setIdentifyingNumber(tempString);

				tempString=tokenizer.nextToken();
				builder.setKeyword(tempString);

				tempString=tokenizer.nextToken();
				builder.setData(tempString);

				ConnectionMessage clientConnection = builder.finalizeObject();
				out.write(clientConnection);

				return true;
			}

		}
	}

	private ConnectionType determineClientType(String token)
	{
//		ConnectionType type;
		if(token.length()>1 && token.length()>0)
		{
			return ConnectionType.ERRONEOUS;
		}
		else
		{
			char flagChar = token.toUpperCase().charAt(0);
			for(ConnectionType t :ConnectionType.values())
			{
				if(t.getFlag()== flagChar)
				{
					return t;
				}
			}

		}
		return ConnectionType.ERRONEOUS;
	}
}
