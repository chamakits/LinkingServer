package edu.uprm.capstone.areatech.linkingserver.utilities;

public class LocalDebugPrinter
{
	
	public static void println(String message)
	{
		System.out.println("DEBUG:"+message);
	}
	
	public static void println(String message, String tag)
	{
		println(tag+":"+message);
	}
	
	public static void println(String message, String tag, Class aclass)
	{
		println(aclass.getName()+":"+message, tag);
	}
	
	public static void println(String message, Class aclass)
	{
		println(aclass.getName()+":"+message);
	}
}
