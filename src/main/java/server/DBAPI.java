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
	
	public String createReliefInventory(JSONObject json) throws Exception
	{
		try
		{
			final String query = "CALL pure_relief.create_relief_inventory(?,?,?,?,?)";
			
			CallableStatement procedure = connect.prepareCall(query);
			
			procedure.setInt("user_id",json.getInt("userId");
			
			procedure.setInt("type", json.getInt("type"));
			
			procedure.setInt("urgency", json.getInt("urgency"));
			
			procedure.setInt("shelter", json.getInt("shelter"));
			
			procedure.setInt("amount", json.getInt("amount"));
		
			procedure.execute();
			
			ResultSet set = procedure.getResultSet();
			
			return "SUCCESS";
		}
		catch(Exception ex)
		{
			throw new Exception("Failed to create inventory for the relief");
		}
	}
	
	public String createShelterNecessity(JSONObject json) throws Exception
	{
		try
		{
			String out = "SUCCESS";
			
			final String query = "CALL pure_relief.create_shelter_necessity(?,?,?,?)";
			
			CallableStatement procedure = connect.prepareCall(query);
			
			procedure.setInt("user_id",json.getInt("user_id"));
			
			procedure.setInt("type", json.getInt("type"));
			
			procedure.setInt("urgency", json.getInt("urgency"));
			
			procedure.setInt("amount", json.getInt("amount"));
			
			procedure.execute();
			
			return out;
		}
		catch(Exception ex)
		{
			throw new Exception("Failed to create necessities for shelters.");
		}
	}
	
	public String donateToShelter( JSONObject json) throws Exception
	{
		try
		{
			String out = "SUCCESS";
			
			final String query = "CALL pure_relief.donate_to_shelter(?,?,?)";

			CallableStatement procedure = connect.prepareCall(query);
			
			procedure.setInt("shelter_id", json.getInt("shelter"));
			
			procedure.setInt("relief_id", json.getInt("relief"));
			
			procedure.setInt("amount_donated", json.getInt("amountDonated"));
			
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
			throw new Exception("Failed to get all the shelters.");
		}
	}
	
	public String getShelterById(JSONObject json) throws Exception
	{
		try
		{
			final String query = "CALL pure_relief.get_shelter_by_id(?)";
			
			CallableStatement procedure = connect.prepareCall(query);
			
			procedure.setInt("shelter", json.getInt("shelter"));
			
			procedure.execute();
			
			JSONObject jsonOut = JSONTranslator.resultSetToJSONObject(procedure.getResultSet());
			
			return jsonOut.toString();
		}
		catch(Exception ex)
		{
			throw new Exception("Failed to get shelter by ID.");
		}
	}
	
	public String getUserId (JSONObject json) throws Exception
	{
		try
		{
			final String query = "CALL pure_relief.get_user_id(?)";
			
			CallableStatement procedure = connect.prepareCall(query);
			
			procedure.setString("username", json.getString(name));
			
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
	
	public String loginValidation(JSONObject json) throws Exception
	{
		try
		{
			
			final String query = "CALL pure_relief.login_validation(?,?,?,?,?)";
			
			CallableStatement procedure = connect.prepareCall(query);
			
			procedure.setString("user_name", json.getString("user_name"));
			
			procedure.setString("password", json.getString("password"));
			
			boolean DOES_NOT_EXIST = false;
					
			boolean LOGGED_IN = false;
			
			int user_id = Integer.parseInt(getUserId(json));
			
			procedure.registerOutParameter("response", 0 );
			
			procedure.registerOutParameter("logged_in", 0);
			
			procedure.registerOutParameter("user_id", user_id);
			
			procedure.execute();
			
			if (DOES_NOT_EXIST)
			{
				throw new Exception("The user does not exist!")
			}
			
			if (LOGGED_IN)
			{
				throw new Exception("The user is already logged into the server");
			}
			
			return json.toString();
			
		}
		catch(Exception ex)
		{
			throw new Exception("Invalid credentials were provided, " + ex.getMessage());
		}
	}
	
	public String logout(JSONObject json) throws Exception
	{
		try
		{
			final String query = "CALL pure_relief.logout(?)";
			
			CallableStatement procedure = connect.prepareCall(query);
			
			procedure.setString("username", json.getString("user_name"));
			
			procedure.execute();
			
			return "SUCCESS";
		}
		catch(Exception ex)
		{
			throw new Exception("Failure occurred when try to logout");
		}
	}
	
	public String registerRelief(JSONObject json) throws Exception
	{
		try
		{
			String name = json.getString("name");
			String password = json.getString("password");
			String location = json.getString("location");

			final String query = "CALL pure_relief.register_relief(?)";
			
			CallableStatement procedure = connect.prepareCall(query);
			
			procedure.setString("name", name);
			procedure.setString("pasword", password);
			procedure.setString("location", location);
			
			procedure.execute();
			
			return "SUCCESS";
		}
		catch(Exception ex)
		{
			throw new Exception("An error occurred while attempting to register your own devce");
		}
	}
	
	public String registerShelter(JSONObject json) throws Exception 
	{
		try
		{
			String name = json.getString("name");
			String password = json.getString("password");
			String location = json.getString("location");
			
			final String query = "CALL pure_relief.register_shelter(?)";
			
			CallableStatement procedure = connect.prepareCall(query);
			
			procedure.setString("name", name);
			procedure.setString("pasword", password);
			procedure.setString("location", location);
			
			procedure.execute();
			
			return "SUCCESS";
		}
		catch(Exception ex)
		{
			throw new Exception("An error occurred while attempting to register");
		}
	}
	
	public String requestMoreNecessities(JSONObject json) throws Exception
	{
		
		try
		{
			int shelterID = json.getInt("shelter");
			
			int necessity_type = json.getInt("type");
			
			int amount = json.getInt("amount");
			
			int urgency = json.getInt("urgency");
			
			boolean success = true;
			
			final String query = "CALL pure_relief.register_more_necessities(?,?,?,out boolean)";
			
			CallableStatement procedure = connect.prepareCall(query);
			
			procedure.setInt("shelter", shelterID);
			procedure.setInt("necessity_type", necessity_type);
			procedure.setInt("amount", amount);
			procedure.setInt("urgency", urgency);
			procedure.setBoolean("success", success);
			
			
			procedure.execute();
			
			return "SUCCESS";
			
		}
		catch(Exception ex)
		{
			throw new Exception("An error occurred while attempting to request more necessities.");
		}
		
	}
	
	public String updateInventoryDetails(JSONObject json) throws Exception
	{
		try
		{
			int type = json.getInt("type");
			
			int amount = json.getInt("amount");
			
			int userID = json.getInt("user_id");
			
			int urgency = json.getInt("urgency");
			
			boolean success = true;
			
			final String query = "CALL pure_relief.update_inventory_details(?,?,?,?,?)";
			
			CallableStatement procedure = connect.prepareCall(query);
			
			procedure.setInt("type", type);
			procedure.setInt("amount", amount);
			procedure.setInt("userID", userID);
			procedure.setInt("urgency", urgency);
			procedure.setBoolean("4", success);
			
			procedure.execute();
			
			if(!success)
			{
				throw new Exception("Failed to update inventory details");
			}
			
			return "SUCCESS";
			
		}
		catch(Exception ex)
		{
			throw new Exception("Failed to update inventory details");
		}
		
	}
	
	public String updateNecessityDetails(JSONObject json) throws Exception
	{
		try
		{
			int type = json.getInt("type");
			int amount = json.getInt("amount");
			int userID = json.getInt("userId");
			int urgency = json.getInt("urgency");
			boolean success = true;
			
			final String query = "CALL pure_relief.update_necessity_details(?,?,?,?)";
			
			CallableStatement procedure = connect.prepareCall(query);
			
			procedure.setInt("type", type);
			procedure.setInt("amount", amount);
			procedure.setInt("userID", userID);
			procedure.setInt("urgency", urgency);
			procedure.setBoolean("4", success);
			
			procedure.execute();
			
			if(!success)
			{
				throw new Exception("break");
			}
			
			return "SUCCESS";
			
		}
		catch(Exception ex)
		{
			throw new Exception("Could not update neccessity details.");
		}
		
	}
	
	public String update_request_donation(JSONObject json) throws Exception
	{
		try
		{
			int amountDonated = json.getInt("donated");
			int shelter = json.getInt("shelter");
			int type = json.getInt("type");
			
			final String query = "CALL pure_relief.update_request_donation(?,?,?)";
			
			CallableStatement procedure = connect.prepareCall(query);
			procedure.setInt("amountDonated", donated);
			procedure.setInt("shelter_id", shelter);
			procedure.setInt("type",type);
			
			procedure.execute();
			
			return "SUCCESS";
		}
		catch(Exception ex)
		{
			throw new Exception("Something failed while trying to update donations for a request.");
		}
		
	}
	
	

}
