package edu.uprm.capstone.areatech.linkingserver.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

import utils.PropertiesLoader;

public class DBManager 
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
		try
		{
			Class.forName("com.mysql.jdbc.Driver").newInstance();
		} 
		catch (ClassNotFoundException e) 
		{
			throw new RuntimeException("Unable to load database driver.\n" + e.getStackTrace());
		} 
		catch (InstantiationException e) 
		{
			throw new RuntimeException("Unable to load database driver.\n" + e.getStackTrace());
		}
		catch (IllegalAccessException e) 
		{
			throw new RuntimeException("Unable to load database driver.\n" + e.getStackTrace());
		}
		
	}
	
	public static Connection getConnection() 
	{
		Properties connectionParameters = new Properties();
		connectionParameters.setProperty(USER, dbProperties.getProperty(DEFAULT_USERNAME));
		connectionParameters.setProperty(PASSWORD, dbProperties.getProperty(DEFAULT_PASSWORD));
		return getConnection(connectionParameters);
	}
	
	private static Connection getConnection(Properties connectionParameters) 
	{
		try {
			Connection con = DriverManager.getConnection(dbProperties.getProperty(JDBC_URL), connectionParameters);
			return con;
		} catch(SQLException e) {
			throw new IllegalArgumentException("Unable to create connection to database for specified connection parameters:  " + connectionParameters);
		}
	}
	
	
	

}
