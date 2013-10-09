package edu.uprm.capstone.areatech.linkingserver.sandbox;

import java.util.StringTokenizer;

import edu.uprm.capstone.areatech.linkingserver.connection.log.EventLogParser;
import edu.uprm.capstone.areatech.linkingserver.connection.log.eventdata.EventLog;

public class EventLogParserSandbox
{
	
	public static void main(String[] args)
	{
		
		String testHexString = "03 01 11 08 40 41 01 02 00 00 02 2C 01 13 01 7A 00 53 00 00 01";
		StringTokenizer tokenizer = new StringTokenizer(testHexString," ");
		String currentString ="";
		
		System.out.println("Log hex string:\n"+testHexString);
		
		byte[] inputBytes = new byte[tokenizer.countTokens()];
		
		
		int index=0;
		
		while(tokenizer.hasMoreTokens())
		{
			currentString = tokenizer.nextToken();
			inputBytes[index++]|=Integer.valueOf(currentString, 16);
		}
		
		for(byte currentByte:inputBytes)
		{
//			System.out.println("current:"+currentByte+", bits:"+Converter.bitString(currentByte));
		}
		
		EventLog log = EventLogParser.parseLog(inputBytes);
		
		System.out.println(log.toString());
		
	}

}
