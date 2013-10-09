package edu.uprm.capstone.areatech.linkingserver.connection.log.eventdata;

public class AccelerationVector
{
	
	private double accelerationX=0;
	private double accelerationY=0;
	private double accelerationZ=0;
		
	AccelerationVector()
	{
		
	}
	
	AccelerationVector(double accelerationX, double accelerationY,
			double accelerationZ)
	{
		super();
		this.accelerationX = accelerationX;
		this.accelerationY = accelerationY;
		this.accelerationZ = accelerationZ;
	}
	double getAccelerationX()
	{
		return accelerationX;
	}
	double getAccelerationY()
	{
		return accelerationY;
	}
	double getAccelerationZ()
	{
		return accelerationZ;
	}

	void setAccelerationX(double accelerationX)
	{
		this.accelerationX = accelerationX;
	}

	void setAccelerationY(double accelerationY)
	{
		this.accelerationY = accelerationY;
	}

	void setAccelerationZ(double accelerationZ)
	{
		this.accelerationZ = accelerationZ;
	}

	@Override
	public String toString()
	{
		return "AccelerationVector [accelerationX=" + accelerationX + ", accelerationY=" + accelerationY + ", accelerationZ=" + accelerationZ + "]";
	}
	
	
	
	

}
