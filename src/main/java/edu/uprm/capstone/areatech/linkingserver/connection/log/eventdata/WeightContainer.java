package edu.uprm.capstone.areatech.linkingserver.connection.log.eventdata;

public class WeightContainer
{
	
	private double weightLeftArmrest;
	private double weightRightArmrest;
	private double weightSeat;
	
	WeightContainer()
	{
		this.weightLeftArmrest=this.weightRightArmrest=this.weightSeat=0;
	}
	
	WeightContainer(double weightLeftArmrest, double weightRightArmrest,
			double weightSeat)
	{
		super();
		this.weightLeftArmrest = weightLeftArmrest;
		this.weightRightArmrest = weightRightArmrest;
		this.weightSeat = weightSeat;
	}




	void setWeightLeftArmrest(double weightLeftArmrest)
	{
		this.weightLeftArmrest = weightLeftArmrest;
	}

	void setWeightRightArmrest(double weightRightArmrest)
	{
		this.weightRightArmrest = weightRightArmrest;
	}

	void setWeightSeat(double weightSeat)
	{
		this.weightSeat = weightSeat;
	}
	
	double getWeightLeftArmrest()
	{
		return weightLeftArmrest;
	}
	
	double getWeightRightArmrest()
	{
		return weightRightArmrest;
	}
	
	double getWeightSeat()
	{
		return weightSeat;
	}

	@Override
	public String toString()
	{
		return "WeightContainer [weightLeftArmrest=" + weightLeftArmrest + ", weightRightArmrest=" + weightRightArmrest + ", weightSeat=" + weightSeat + "]";
	}

	
	

}
