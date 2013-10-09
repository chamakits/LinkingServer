package edu.uprm.capstone.areatech.linkingserver.sandbox;

import org.apache.commons.lang.ArrayUtils;

import utils.UnitConverter;

import edu.uprm.capstone.areatech.linkingserver.connection.log.EventLogParser;
import edu.uprm.capstone.areatech.linkingserver.connection.log.eventdata.EventLog;
import edu.uprm.capstone.areatech.linkingserver.connection.log.eventdata.EventLogBuilder;
import edu.uprm.capstone.areatech.linkingserver.utilities.Converter;

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
		
//		byte bitsSetByte = Converter.bitsToByte(0,1,2,3,4,5,6,7);
//		System.out.println(Converter.bitString(bitsSetByte));
//		
//		byte[] bytes= new byte[2];
//		bytes[1]=Converter.bitsToByte(0,1,2,3);
//		bytes[0]=0;
//		
//		long converted=Converter.unsignedByteArrayToLong(bytes);
//		
//		
//		System.out.println(Converter.unsignedByteToLong(bytes[1]));
//		System.out.println(Converter.unsignedByteToLong(bytes[0]));
//		System.out.println(converted);
//		System.out.println(Converter.bitString((int)converted));
/*		
		byte[] bytes = new byte[22];
		int counter =0;
		bytes[counter++] = Converter.bitsToByte(0,1);
		bytes[counter++] = Converter.bitsToByte(0);
		bytes[counter++] = Converter.bitsToByte(0,4);
		bytes[counter++] = Converter.bitsToByte(3);
		//4 bytes done
		
		bytes[counter++] = Converter.bitsToByte(6);
		bytes[counter++] = Converter.bitsToByte(0,6);
		bytes[counter++] = Converter.bitsToByte(0);
		bytes[counter++] = Converter.bitsToByte(0,2,7);
		//8 bytes done		
		
		bytes[counter++] = Converter.bitsToByte(0,1,3);
		bytes[counter++] = 0;
		bytes[counter++] = 0;
		bytes[counter++] = Converter.bitsToByte(1);
		//12 bytes done
		
		bytes[counter++] = Converter.bitsToByte(2,3,5);
		bytes[counter++] = Converter.bitsToByte(0);
		bytes[counter++] = Converter.bitsToByte(0,1,4);
		bytes[counter++] = Converter.bitsToByte(0);
		//16 bytes done
		
		bytes[counter++] = Converter.bitsToByte(1,3,4,5,6);
		bytes[counter++] = 0;
		bytes[counter++] = Converter.bitsToByte(0,1,4,6);
		bytes[counter++] = 0;
		//20 bytes done
		
		bytes[counter++] = 0;
		bytes[counter++] = Converter.bitsToByte(1);
		//22 bytes done
		
		for(int i = 0; i < bytes.length; ++i)
		{
			if(i%4==0 && i!=0)
			{
				System.out.println();
			}
			System.out.print(Converter.bitString(bytes[i])+" ");
		}
		
		byte[] accelerationSubarray = ArrayUtils.subarray(bytes, 15, 21);
		
		EventLogBuilder eventLogBuilderAcceleration = new EventLogBuilder();
		
		EventLogParser.parseAccelerationIntoEventLogBuilder(accelerationSubarray, eventLogBuilderAcceleration);
		
		EventLog eventLogAcceleration = eventLogBuilderAcceleration.finalizeObject();
		
		System.out.println("Acceleration:");
		System.out.println("x: "+eventLogAcceleration.getAccelerationX());
		System.out.println("y: "+eventLogAcceleration.getAccelerationY());
		System.out.println("z: "+eventLogAcceleration.getAccelerationZ());
		
		
		byte[] weightSubarray = ArrayUtils.subarray(bytes, 9, 15);
		
		EventLogBuilder eventLogBuilderWeight= new EventLogBuilder();
		
		EventLogParser.parseWeightIntoEventLogBuilder(weightSubarray, eventLogBuilderWeight);
		
		EventLog eventLogWeight = eventLogBuilderWeight.finalizeObject();
		
		System.out.println("Weight:");
		System.out.println("Seat weight:"+eventLogWeight.getWeightSeat());
		System.out.println("Right weight:"+eventLogWeight.getWeightRightArmrest());
		System.out.println("Left weight:"+eventLogWeight.getWeightLeftArmrest());
		
		
		System.out.println(UnitConverter.voltageToWeight(1.793548387096774));
		System.out.println(UnitConverter.voltageToWeight(0.8870967741935484));
		
		System.out.println(UnitConverter.weightToVoltage(190));
*/		
		
		byte aByte = Converter.bitsToByte(0,1,2,3,7);
		byte reverse = Converter.reverseByte(aByte);
		
		System.out.println(Converter.bitString(aByte));
		System.out.println(Converter.bitString(reverse));
		

	}

}
