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
	
	public static ConnectionType determineClientType(String token)
	{
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
