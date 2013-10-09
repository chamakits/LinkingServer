package edu.uprm.capstone.areatech.linkingserver.test;

import static org.junit.Assert.*;

import org.junit.Test;

import edu.uprm.capstone.areatech.linkingserver.utilities.Converter;

public class ConverterTest
{

	@Test
	public void testByteArrayToInt()
	{
		byte[] bytes = new byte[2];
		bytes[0]=Byte.MIN_VALUE;
		bytes[1]=Byte.MIN_VALUE;
		System.out.println("DEBUG:TESTbYTEaRRAYtOiNT:BYTEStOsTRING="+bytes.toString());
		Converter.byteArrayToInt(bytes, 8);
	       		
		//fail("Not yet implemented");
	}

}
