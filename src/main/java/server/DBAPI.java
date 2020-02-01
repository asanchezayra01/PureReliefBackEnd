package server;

import java.sql.*;

import org.json.JSONArray;
import org.json.JSONObject;

import utils.JSONTranslator;


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
	
	public String getAllRequests() throws Exception
	{
		try
		{
			JSONArray list = new JSONArray();
		
			final String query = "CALL pure_relief.get_all_requests()";
		
			CallableStatement procedure = connect.prepareCall(query);
		
			procedure.execute();
			
			ResultSet set = procedure.getResultSet();
			
			list = JSONTranslator.resultSetToJSONArray(set);
			
			return list.toString();
		
		}
		catch(Exception ex)
		{
			throw new Exception("Failed to get all requests.");
		}
	}
	
	public String createReliefInventory(int user_id, int type, int shelter_id, int urgency, int amount ) throws Exception
	{
		try
		{
			final String query = "CALL pure_relief.create_relief_inventory(?,?,?,?,?)";
			
			CallableStatement procedure = connect.prepareCall(query);
			
			procedure.setInt("user_id",user_id);
			
			procedure.setInt("type", type);
			
			procedure.setInt("urgency", urgency);
			
			procedure.setInt("shelter",shelter_id);
			
			procedure.setInt("amount", amount);
		
			procedure.execute();
			
			ResultSet set = procedure.getResultSet();
			
			return "SUCCESS";
		}
		catch(Exception ex)
		{
			throw new Exception("Failed to create inventory for the relief");
		}
	}
	
	public String createShelterNecessity(int user_id, int type, int urgency, int amount) throws Exception
	{
		try
		{
			String out = "SUCCESS";
			
			final String query = "CALL pure_relief.create_shelter_necessity(?,?,?,?)";
			
			CallableStatement procedure = connect.prepareCall(query);
			
			procedure.setInt("user_id",user_id);
			
			procedure.setInt("type", type);
			
			procedure.setInt("urgency", urgency);
			
			procedure.setInt("amount", amount);
			
			procedure.execute();
			
			return out;
		}
		catch(Exception ex)
		{
			throw new Exception("Failed to create necessities for shelters.");
		}
	}
	
	public String donateToShelter(int shelter, int relief, int amountDonated ) throws Exception
	{
		try
		{
			String out = "SUCCESS";
			
			final String query = "CALL pure_relief.donate_to_shelter(?,?,?)";

			CallableStatement procedure = connect.prepareCall(query);
			
			procedure.setInt("shelter_id", shelter);
			
			procedure.setInt("relief_id", relief);
			
			procedure.setInt("amount_donated", amountDonated);
			
			procedure.execute();
			
			return out;
		}
		catch(Exception ex)
		{
			throw new Exception("Failed to donate to the shelter that you chose.");
		}
	}
	
	public String getAllShelters() throws Exception
	{
		try
		{
			final String query = "CALL pure_relief.get_all_shelters()";
			
			CallableStatement procedure = connect.prepareCall(query);
			
			procedure.execute();
			
			return JSONTranslator.resultSetToJSONArray(procedure.getResultSet()).toString();
		}
		catch(Exception ex)
		{
			throw new Exception("Failed to get all the shelters.")
		}
	}
	
	public String getShelterById(int shelter) throws Exception
	{
		try
		{
			final String query = "CALL pure_relief.get_shelter_by_id(?)";
			
			CallableStatement procedure = connect.prepareCall(query);
			
			procedure.setInt("shelter", shelter);
			
			procedure.execute();
			
			JSONObject json = JSONTranslator.resultSetToJSONObject(procedure.getResultSet());
			
			return json.toString();
		}
		catch(Exception ex)
		{
			throw new Exception("Failed to get shelter by ID.");
		}
	}
	
	public String getUserId (String name) throws Exception
	{
		try
		{
			final String query = "CALL pure_relief.get_user_id(?)";
			
			CallableStatement procedure = connect.prepareCall(query);
			
			procedure.setString("username", name);
			
			int userID = 0;
			
			procedure.registerOutParameter("userid", userID);
			
			if ( userID < 0 )
			{
				throw new Exception("No user was found.");
			}
			
			return userID + "";
		}
		catch(Exception ex)
		{
			throw new Exception("Failed to get the user id. More details: " + ex.getMessage());
		}
	}
	
	public String loginValidation(String user_name, String password) throws Exception
	{
		try
		{
			
			final String query = "CALL pure_relief.login_validation(?,?,?,?,?)";
			
			CallableStatement procedure = connect.prepareCall(query);
			
			procedure.setString("username", user_name);
			
			procedure.setString("password", password);
			
			boolean DOES_NOT_EXIST = false;
					
			boolean LOGGED_IN = false;
			
			return "";
			
		}
		catch(Exception ex)
		{
			throw new Exception("Invalid credentials were provided.");
		}
	}

}
