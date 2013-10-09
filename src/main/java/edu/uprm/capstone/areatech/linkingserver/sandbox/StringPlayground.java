package edu.uprm.capstone.areatech.linkingserver.sandbox;

public class StringPlayground 
{
	
	public static void main(String[] args) 
	{
		String str ="abcde:12345:xyz"; 
		String messageString = str;
		
		System.out.println(messageString.substring(0, messageString.indexOf(':')));
		
		String messageString2=
			messageString.substring(messageString.indexOf(':')).substring(1,messageString.indexOf(':')+1);
		System.out.println(messageString2);
		
		String messageString3 = 
			messageString.substring(messageString.indexOf(':')+1).substring(messageString.indexOf(':')+1);
		System.out.println(messageString3);
		

		
	}

}
