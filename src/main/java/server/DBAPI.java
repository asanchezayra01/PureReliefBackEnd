package server;

import java.sql.*;

import org.json.JSONObject;


public class DBAPI {
	
	private Connection connect;
	
	final private String password = "";
	
	public DBAPI() throws Exception
	{
		try
		{
			Class.forName("com.mysql.cj.jdbc.Driver");
			
			// Change this to a const.
			connect = DriverManager
					.getConnection("jdbc:mysql://10.136.104.49:3306/?user=server&password=" + password);
		}
		catch(Exception ex)
		{
			throw new Exception("Failed to connect to database.\nMore details: " + ex.getMessage());
		}
	}
	
	
	public String registerShelter(JSONObject json) throws Exception
	{
		String out = "";
		
		try
		{
		final String query = "CALL pure_relief.register_shelter(?,?)";
		
		CallableStatement procedure = connect.prepareCall(query);
		
		procedure.setString(1, json.getString("username"));
		procedure.setString(2, json.getString("password"));
		
		procedure.execute();
		
		return out= "SUCCESS";
		}
		catch(Exception ex)
		{
			throw ex;
		}
	}
	
	

}
