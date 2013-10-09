package edu.uprm.capstone.areatech.linkingserver.sandbox;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import edu.uprm.capstone.areatech.linkingserver.db.DBManager;

public class DBManagerPlaying 
{
	
	public static void main(String[] args) throws SQLException 
	{
		
		Connection connection = DBManager.getConnection();
		PreparedStatement statement = connection.prepareStatement("SELECT * FROM profile_view");
		
		
		ResultSet resultSet = statement.executeQuery();
		resultSet.next();
		System.out.println(resultSet.getString("unit_telephone_number"));
		
		connection.close();
		
		
	}
	

}
