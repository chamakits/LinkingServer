package edu.uprm.capstone.areatech.linkingserver.connection;

public enum ConnectionType 
{
	DEVICE('D'), APPLICATION('A'),ERRONEOUS('E'),SERVER('S');
	
	private char flag;
	

	ConnectionType(char flag)
	{
		this.flag=flag;
	}

	public char getFlag()
	{
		return flag;
	}
	
}
