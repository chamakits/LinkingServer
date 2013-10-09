package utils;


public class UnitConverter {
	/**
	 *Method that returns the numbers of cycles the counter need to pass between 
	 *interrupts to measure the provided mph given a specified wheel diameter
	 * @param mph the mph to be converted.
	 * @param wheelDiameterInches the diameter of the wheel in inches. 
	 * @return the number of cycles (AT unit)
	 */
	public static final int mphToATu(double mph, double wheelDiameterInches) {
		double atu = (1/mph)*ATuFactor(wheelDiameterInches);
		return (int) atu;
	}
	
	/**
	 * Method that converts from ATu to MPH. The result has up to 2 decimal figures.
	 * @param atu reading from the tachometer's counter
	 * @param wheelDiameterInches
	 * @return
	 */
	public static final double ATuToMPH(double atu, double wheelDiameterInches) {
		double mph = (1/atu) * ATuFactor(wheelDiameterInches);
		return mph;
	}
	
	private static final double ATuFactor(double wheelDiameterInches) {
		return (3600.0/63360.0)*(wheelDiameterInches*Math.PI/5.0)*(1000000.0/32.0);
	}
	
//	public static final double weightToVoltage(double voltage) {
//		double x = voltage;
//		double force = (2.345/227.84*((x-226.8)*(x-158.8))) + (2.29/-217.6*(x-230.0)*(x-158.8)) + (1.93/4841.6*(x-230.0)*(x-226.8));
//		return force;
//	}
	
	 public static String bitString(byte value)
	  {
	    String str = "";

	    long bitHolder = 1L;
	    for (int i = 0; i < 8; ++i)
	    {
	      if ((value & bitHolder) > 0L)
	        str = "1" + str;
	      else
	        str = "0" + str;
	      bitHolder <<= 1;
	    }
	    return str;
	  }
	 
	 public static final double weightToVoltage(double weight) {
		 //calculation of the weight to voltage conversion.  
		 double voltage = 3.3 - 3.3 * Math.exp(-0.005391158 * weight);
		 return voltage;
	 }
	 
	 public static final double voltageToWeight(double voltage) {
		 double weight= Math.log(((voltage - 3.3)/-3.3)) / (-0.005391158);
		 return weight;
	 }
	 
	 public static int voltageToADC (double voltage) {
		 int adc =  (int) Math.round(adcConstant() * voltage);
		 return adc;
	 }
	 
	 public static double adcToVoltage(int adc) {
		 double voltage = ((double)adc)/adcConstant();
		 return voltage;
	 }
	 
	 private static final double adcConstant() {
		 return Math.round(((Math.pow(2, 10))/3.3));
	 }

}
