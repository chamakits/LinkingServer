package edu.uprm.capstone.areatech.linkingserver.sandbox;

import java.util.Properties;

import utils.PropertiesLoader;

public class PropertiesPlaying 
{

	private static final Properties dbProperties;
	private static final String propertiesLocation = "src/main/resources/database.properties";

	//Properties required in the database properties file
	private static final String JDBC_URL = "jdbc_url";
	private static final String TECHNICIAN_USERNAME = "technician_username";
	private static final String TECHNICIAN_PASSWORD = "technician_password";
	private static final String ADMIN_USERNAME = "admin_username";
	private static final String ADMIN_PASSWORD = "admin_password";
	private static final String DEFAULT_USERNAME = "default_username";
	private static final String DEFAULT_PASSWORD = "default_password";

	//Connection properties name
	private static final String USER = "user";
	private static final String PASSWORD = "password";
	
	static
	{
		dbProperties = PropertiesLoader.loadProperties("src/main/resources/database.properties");
	}
	
	public static void main(String[] args) 
	{
		
		Properties connectionParameters = new Properties();
		System.out.println(dbProperties.getProperty(DEFAULT_USERNAME));
		System.out.println(dbProperties.getProperty(DEFAULT_PASSWORD));
//		connectionParameters.setProperty(USER, );
//		connectionParameters.setProperty(PASSWORD, );
//		return getConnection(connectionParameters);
		
	}

}
