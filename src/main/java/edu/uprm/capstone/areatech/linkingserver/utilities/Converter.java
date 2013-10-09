package edu.uprm.capstone.areatech.linkingserver.utilities;

public class Converter
{
	
	private static final int BASE_TEN=10;
	private static final int NEGATIVE_BIT_FLAG = 1<<7;
	
	public static byte bitsToByte(int... bitSetPositions)
	{
		byte total=0;
		for(int index: bitSetPositions)
		{
			total|=(1<<index);
		}
		return total;
		
	}
	
	//TODO look into how to do this in a generic manner.
	public static String bitString(byte value)
	{
		String str = "";
	
		long bitHolder= 1;
		for(int i = 0 ; i < Byte.SIZE; ++i)
		{
			if((value & bitHolder) >0)
				str="1"+str;
			else
				str="0"+str;
			bitHolder<<=1;
		}
		return str;
	}
	
	public static String bitString(short value)
	{
		String str = "";
		long bitHolder= 1;
		for(int i = 0 ; i < Short.SIZE; ++i)
		{
			if((value & bitHolder) >0)
				str="1"+str;
			else
				str="0"+str;
			bitHolder<<=1;
		}
		return str;
	}
	
	public static String bitString(int value)
	{
		String str = "";
		long bitHolder= 1;
		for(int i = 0 ; i < Integer.SIZE; ++i)
		{
			if((value & bitHolder) >0)
				str="1"+str;
			else
				str="0"+str;
			bitHolder<<=1;
		}
		return str;
	}
	
	public static String bitString(long value)
	{
		String str = "";
		long bitHolder= 1;
		for(int i = 0 ; i < Long.SIZE; ++i)
		{
			if((value & bitHolder) >0)
				str="1"+str;
			else
				str="0"+str;
			bitHolder<<=1;
		}
		return str;
	}
	
//	public static long unsignedByteToInt(byte inputValue)
//	{
//		byte value =inputValue;
//		
//		long total=0;
//		boolean isNegative = (value&NEGATIVE_BIT_FLAG) >0;
//		if(isNegative)
//		{
//			value+=-Byte.MIN_VALUE;
//			total=value; 
//			total-=-Byte.MIN_VALUE;
//		}
//		else
//		{
//			total=value;
//		}
//		return total;
//	}
	
	public static long unsignedByteToLong(byte value)
	{
		boolean isNegative = (value&NEGATIVE_BIT_FLAG) >0;
		long total = (byte) (value & Byte.MAX_VALUE);
		
		if(isNegative)
		{
			total+=-Byte.MIN_VALUE;
		}
		
		return total;
		
	}
	
	//Methods assumes how the bytes are organized.
	//TODO verify this assumption.
	public static long unsignedByteArrayToLong(byte[] byteArray)
	{
		long total = 0;
//		for(byte currentByte : byteArray)
		for(int i = byteArray.length-1 ; i >=0 ; --i)
		{
			total<<=Byte.SIZE;
			total+=unsignedByteToLong(byteArray[i]);
		}
		return total;
	}
	
	
	public static int byteArrayIntStringToInt(byte[] bytesOfString)
	{
		int totalValue=0;
		int tens=10;
				
		for(int i = bytesOfString.length-1; i>=0;--i)
		{
			System.out.println();
			int currentNum = (bytesOfString[i]- '0')*(tens/BASE_TEN);
			tens*=BASE_TEN;
			totalValue += currentNum;
		}
		return totalValue;
	}
	
	public static long byteArrayToInt(byte[] bytes, int bitsPerByte)
	{
		long total = 0;
		long current=0;
		int currentDigitPosition=0;
		for(int counter = bytes.length-1; counter >= 0; --counter, ++currentDigitPosition)
		{
			current=bytes[counter];
			System.out.println("DEBUG:PRE:CURRENT="+current);
			current=current<<(bitsPerByte+currentDigitPosition);
			System.out.println("DEBUG:POST:CURRENT="+current);
			total+=current;
			System.out.println("DEBUG:BYTEaRRAYtOiNT:BYTE="+bytes[counter]+
					",TOTAL="+total);
		}
		return total;
	}

}
