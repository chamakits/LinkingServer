package edu.uprm.capstone.areatech.linkingserver.connection.client;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

public class SimpleHTTPTestClient
{
	
	public static void main(String[] args) throws MalformedURLException, IOException
	{
		String url = "http://localhost:5555";
		String charset = "US-ASCII";
		String param1 = "value1";
		String param2 = "value2";
		
		String query = String.format("param1=%s&param2=%s", 
		     URLEncoder.encode(param1, charset), 
		     URLEncoder.encode(param2, charset));
		
		URLConnection connection = new URL(url + "?" + query).openConnection();
		connection.setDoOutput(true); // Triggers POST.
		connection.setRequestProperty("Accept-Charset", charset);
		connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=" + charset);
		connection.setRequestProperty("Lol-Property","Some value random ahi.");
		OutputStream output = null;
		try {
			
		     output = connection.getOutputStream();
		     System.out.println("DEBUG:WRITING:"+query);
		     output.write(query.getBytes(charset));
		} finally {
		     if (output != null) try { output.close(); } catch (IOException logOrIgnore) 
		     {
		    	 logOrIgnore.printStackTrace();
		     }
		}
		InputStream response = connection.getInputStream();
		System.out.println(response.toString());

	}

}
