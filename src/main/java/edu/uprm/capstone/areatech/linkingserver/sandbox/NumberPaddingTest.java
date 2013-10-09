package edu.uprm.capstone.areatech.linkingserver.sandbox;

import edu.uprm.capstone.areatech.linkingserver.utilities.NumberPadding;

public class NumberPaddingTest
{
	
	public static void main(String[] args)
	{
		String number = NumberPadding.zeroPaddedNumber(4, 2, true);
		System.out.println(number);
		Integer integer = Integer.valueOf(number);
		System.out.println(integer);
	}

}
