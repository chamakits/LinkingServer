package edu.uprm.capstone.areatech.linkingserver.sandbox;

import edu.uprm.capstone.areatech.linkingserver.utilities.Converter;
import edu.uprm.capstone.areatech.linkingserver.utilities.NumberPadding;

public class ConverterPlaying
{

	public static void main(String[] args)
	{
		/*String number = NumberPadding.zeroPaddedNumber(8795, 6, true);
		System.out.println(number);

		Integer integer = Integer.valueOf(number);
		System.out.println(integer);

		int converted = Converter.byteArrayIntStringToInt(number.getBytes());
		System.out.println(converted);

		System.out.println(Converter.bitString(1));

		byte abyte = Byte.MAX_VALUE;
		System.out.println(abyte+":"+Converter.bitString(abyte)+":"+Integer.toBinaryString(abyte));

		abyte=Byte.MIN_VALUE;
		System.out.println(abyte+":"+Converter.bitString(abyte)+":"+Integer.toBinaryString(abyte));

		abyte=0;
//		int detectNeg = 1<<7;
//		int asInt=0;
		System.out.println(abyte+":"+Converter.bitString(abyte)+":"+Integer.toBinaryString(abyte));
		for(abyte=1; abyte!=0; ++abyte)
		{
//			asInt = abyte;
			System.out.println(abyte+":"+Converter.bitString(abyte)+":"+Integer.toBinaryString(abyte));
//			System.out.println(abyte+"=="+asInt);
		}

		int value = 1;
		value<<=7;
//		byte abyte1 = (byte) value;

		int bit16Number = 40907;

		byte lowerByte = (byte)bit16Number;
		int lowerByteAsInt = lowerByte;

		byte higherByte = (byte) (bit16Number>>8);
		int higherByteAsInt = higherByte;
		System.out.println("HigherAsInt="+higherByteAsInt+":"+Converter.bitString(higherByteAsInt));
		//		higherByteAsInt=(higherByteAsInt<<(32-8)>>(32-8));
		higherByteAsInt=higherByteAsInt<<(32-8);
		System.out.println("HigherAsInt="+higherByteAsInt+":"+Converter.bitString(higherByteAsInt));
		higherByteAsInt=higherByteAsInt>>(32-8);
		System.out.println("HigherAsInt="+higherByteAsInt+":"+Converter.bitString(higherByteAsInt));

		System.out.println("Int="+bit16Number+":"+Converter.bitString(bit16Number));

		System.out.println("Higher="+higherByte+":"+Converter.bitString(higherByte));
		System.out.println("HigherAsInt="+higherByteAsInt+":"+Converter.bitString(higherByteAsInt));

		System.out.println("Lower="+lowerByte+":"+Converter.bitString(lowerByte));
		System.out.println("LowerAsInt="+lowerByteAsInt+":"+Converter.bitString(lowerByteAsInt));


		int rebuiltNumber = 0;
		rebuiltNumber|=higherByte;

		//		System.out.println(rebuiltNumber+"=?="+bit16Number);

		rebuiltNumber<<=8;
		//		System.out.println(rebuiltNumber+"=?="+bit16Number);

		rebuiltNumber+=lowerByteAsInt;
		System.out.println("Rebuilt="+rebuiltNumber+":"+Converter.bitString(rebuiltNumber));
		System.out.println("Origina="+bit16Number+":"+Converter.bitString(bit16Number));
		//		System.out.println(rebuiltNumber+"=?="+bit16Number);

		//		System.out.println("int="+Converter.bitString(value)+"\nbyte="+Converter.bitString(abyte));
		//		System.out.println("Value:int="+value+",byte="+abyte1);

		System.out.println(Byte.MIN_VALUE);
		System.out.println(-Byte.MIN_VALUE);
		System.out.println(Byte.MIN_VALUE+(-Byte.MIN_VALUE));

		byte lowestByte = Byte.MIN_VALUE;
		int lowestByteAsInt = (int)Byte.MIN_VALUE;
		System.out.println(lowestByte);
		System.out.println(lowestByteAsInt);
		
		System.out.println(Converter.unsignedByteToLong(Byte.MAX_VALUE));
		
		byte testByte = -22;
		System.out.println(testByte);
		System.out.println(testByte & Byte.MAX_VALUE);
		
//		System.out.println(Converter.unsignedByteToIntNEW(Byte.MIN_VALUE));
//		System.out.println(Converter.unsignedByteToIntNEW((byte) -1));
		
		byte[] byteArray1 = new byte[1];
		byteArray1[0] = -1;
		System.out.println("byteArray1="+Converter.unsignedByteArrayToLong(byteArray1));
		
		byte[] byteArray2 = new byte[2];
		byteArray2[0] = -1;
		byteArray2[1] = -1;
		System.out.println("byteArray2="+Converter.unsignedByteArrayToLong(byteArray2));

		byte[] byteArray3 = new byte[3];
		byteArray3[0] = -1;
		byteArray3[1] = -1;
		byteArray3[2] = -1;
		System.out.println("byteArray3="+Converter.unsignedByteArrayToLong(byteArray3));
		*/
		
		byte bitsSetByte = Converter.bitsToByte(0,1,2,3,4,5,6,7);
		System.out.println(Converter.bitString(bitsSetByte));

	}

}
