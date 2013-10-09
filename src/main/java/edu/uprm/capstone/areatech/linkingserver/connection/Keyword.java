package edu.uprm.capstone.areatech.linkingserver.connection;

public enum Keyword 
{
	
	REQUEST("RQT"), UPLOADED("UPL"), ACKNOWLEDGE("ACK"), GPS("GPS"), DIAGNOSTIC("DIA"), SUCCESS("SCC"), ERRONEOUS("ERR");
	
	String keyword;
	String definition;
	
	private Keyword(String keyword) 
	{
		this.keyword = keyword;
	}
	
	public static Keyword determineKeyword(String key)
	{
		if(key.length()>3 && key.length()>0)
		{
			return Keyword.ERRONEOUS;
		}
		else
		{
			for(Keyword t :Keyword.values())
			{
				if(t.getKeyword().equals(key))
				{
					return t;
				}
			}

		}
		return Keyword.ERRONEOUS;
	}

	public String getKeyword() {
		return this.keyword;
	}
	
	public String toString()
	{
		return this.keyword;
	}
	
	
	
	

}
