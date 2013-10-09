package edu.uprm.capstone.areatech.linkingserver.utilities;

public class NumberPadding 
{
	private static final int DECIMAL_BASE= 10;
	private static final String ZERO_PAD="0000"+"0000"+"0000"+"0000";
	
	public static String zeroPaddedNumber(int number, int digits, boolean forceDigits)
	{
		int digitCount=1;
		int tempNumber=number;
		while(tempNumber > DECIMAL_BASE)
		{
			tempNumber=tempNumber/DECIMAL_BASE;
			++digitCount;
		}
		
		if(forceDigits && digitCount>digits)
		{
			throw new IllegalArgumentException("Sent number has more digits than the desired digit amount.");
		}
		
		String pad = ZERO_PAD.substring(0,digits-digitCount);
		
		return pad+number;
		
	}

}
