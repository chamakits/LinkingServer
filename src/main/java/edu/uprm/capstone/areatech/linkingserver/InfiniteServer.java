package edu.uprm.capstone.areatech.linkingserver;

import java.io.IOException;

public class InfiniteServer 
{
	
	public static void main(String[] args) throws IOException 
	{
		for(;;)
		{
			ClientManager.main(args);
		}
		
		
	}

}
