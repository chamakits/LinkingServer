package edu.uprm.capstone.areatech.linkingserver.connection;

import edu.uprm.capstone.areatech.linkingserver.utilities.NumberPadding;

public class ConnectionMessage
{	
	private String identifyingNumber;
	private String keyword;
	private String data;
	private ConnectionType type;
	
	public static final int DATA_DIGIT_AMOUNT= 2;
	public static final int TYPE_SIZE=1;
	public static final int IDENTIFYING_NUMBER_SIZE=10;
	public static final int KEYWORD_SIZE=3;
	//This must be changed if there is anything else needed for the protocol.
	public static final int DIVIDERS_SIZE=4;
	
	public static final int MINIMUM_MESSAGE_SIZE=
		DATA_DIGIT_AMOUNT+TYPE_SIZE+IDENTIFYING_NUMBER_SIZE+KEYWORD_SIZE+DIVIDERS_SIZE;

	ConnectionMessage(String identifyingNumber,
			String keyword, String data, ConnectionType type)
	{
		super();
		this.identifyingNumber = identifyingNumber;
		this.keyword = keyword;
		this.data = data;
		this.type = type;
	}
	
	public String getDataSize()
	{
		return NumberPadding.zeroPaddedNumber(this.data.length(), DATA_DIGIT_AMOUNT, true);
	}

	public String getIdentifyingNumber() {
		return this.identifyingNumber;
	}

	public String getKeyword() {
		return this.keyword;
	}

	public String getData() {
		return this.data;
	}
	
	public ConnectionType getType()
	{
		return this.type;
	}
	
	public String toString()
	{
		return 	
				this.getDataSize()+":"+
				this.type.getFlag()+":"+
				this.identifyingNumber+":"+
				this.keyword+":"+
				this.data;
	}

}
