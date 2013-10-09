package edu.uprm.capstone.areatech.linkingserver.sandbox;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.Scanner;

import sun.net.ftp.FtpClient;

public class JavaFTPPlaying 
{

	public static void main(String[] args) throws IOException 
	{
		
//		FtpClient client = new 
		
		URL url = new URL("ftp://diameter_device:diapass@areatech.org/test.txt;type=i");
		URLConnection urlc = url.openConnection();
		InputStream is = urlc.getInputStream(); // To download
//		OutputStream os = urlc.getOutputStream(); // To upload
		
		Scanner scanner = new Scanner(is);
		
		while(scanner.hasNext())
			System.out.println(scanner.nextLine());


	}

}
