package server;



import org.slf4j.LoggerFactory;

import com.corundumstudio.socketio.AckRequest;
import com.corundumstudio.socketio.Configuration;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.listener.ConnectListener;
import com.corundumstudio.socketio.listener.DataListener;
import com.corundumstudio.socketio.listener.DisconnectListener;

import org.json.JSONObject;
import org.slf4j.Logger;




public class Listener {
	
	private Configuration config;
	private SocketIOServer server;

	private Logger lg;
	
	private Listener(String hostName, int portNumber) throws Exception
	{ 
		try
		{
			config = new Configuration();
			config.setHostname(hostName);
			config.setPort(portNumber);
			
			server =  new SocketIOServer(config);
			lg = LoggerFactory.getLogger(Listener.class);
		}
		catch(Exception ex)
		{
			lg.error("There was an issue establishing the server.\nMore details: ", ex);
		}
		
	};
	
	static private Listener _instance = null;
	
	static public Listener instance(String hostName, int portNumber) throws Exception
	 {
		 try
		 {
			 if (null == _instance) {
				 _instance = new Listener(hostName, portNumber);
			 }
			 return _instance;
		 }
		 catch(Exception ex)
		 {
			 throw new Exception("Failed to retrieve the instance of the SOS server.\nMore details: " + ex.getMessage());
		 }
	 }
	
	public void ListenForEvents() 
	{
		
		server.addEventListener("registerShelter", JSONObject.class, new DataListener<JSONObject>(){
			public void onData(SocketIOClient client, JSONObject response, AckRequest ackRequest) {
				lg.info("Register Shelters Requested.");
				try {
				DBAPI api = new DBAPI();
				client.sendEvent("doneRegisteringShelter", api.registerShelter(response));
			}catch(Exception ex){
				lg.info(ex.getMessage());
				client.sendEvent("failure", "Registration Failure");
			}
			}
		});
		
		server.addEventListener("registerRelief", JSONObject.class, new DataListener<JSONObject>(){
			public void onData(SocketIOClient client, JSONObject response, AckRequest ackRequest) {
				lg.info("Register Relief Requested.");
				try {
					DBAPI api = new DBAPI();
					client.sendEvent("doneRegisteringRelief", api.registerRelief(response));
				}catch(Exception ex){
					lg.info(ex.getMessage());
					client.sendEvent("failure", "Registration Failure");
				}
				}
			});
		
		server.addEventListener("login", JSONObject.class, new DataListener<JSONObject>() {
			public void onData(SocketIOClient client, JSONObject response, AckRequest ackRequest) {
				lg.info("Login Requested.");
				
				try {
					
					DBAPI api = new DBAPI();
					
					client.sendEvent("login", api.loginValidation(response));
					
				}catch(Exception ex){
					
					lg.info(ex.getMessage());
					
					client.sendEvent("failure", "Response Failure");
					
				}
				}
		});
		
		server.addEventListener("logout", JSONObject.class, new DataListener<JSONObject>() {
			public void onData(SocketIOClient client, JSONObject response, AckRequest ackRequest) {
				lg.info("Login Requested.");
				
				try {
					
					DBAPI api = new DBAPI();
					
					client.sendEvent("logout", api.logout(response));
					
				}catch(Exception ex){
					
					lg.info(ex.getMessage());
					
					client.sendEvent("failure", "Response Failure");
					
				}
				}
		});
		
		server.addEventListener("shelterNeccAmt", JSONObject.class, new DataListener<JSONObject>(){
			public void onData(SocketIOClient client, JSONObject response, AckRequest ackRequest) {
				lg.info("Shelter Neccessity Amount Requested.");
				
				try {
					
					DBAPI api = new DBAPI();
					
					client.sendEvent("shelterNecessityDone", api.createShelterNecessity(response));
					
				}catch(Exception ex){
					
					lg.info(ex.getMessage());
					
					client.sendEvent("failure", "Response Failure");
					
				}
				}
			});
		
		server.addEventListener("shelterReqSupp", JSONObject.class, new DataListener<JSONObject>(){
			public void onData(SocketIOClient client, JSONObject response, AckRequest ackRequest) {
				lg.info("Shelter Request Supply Requested.");
				
				try
				{
					DBAPI api = new DBAPI();
					
					client.sendEvent("eventShelterRequest", api.requestMoreNecessities(response) );
				}
				catch(Exception ex)
				{
					lg.info(ex.getMessage());
					
					client.sendEvent("failure", ex.getMessage());
				}
				
			}
		});
		
		server.addEventListener("reliefInventory", JSONObject.class, new DataListener<JSONObject>(){
			public void onData(SocketIOClient client, JSONObject response, AckRequest ackRequest) {
				lg.info("Relief Inventory Requested.");
				
				try
				{
					DBAPI api = new DBAPI();
					
					
					client.sendEvent("eventReliefInventory", api.createReliefInventory(response));
				}
				catch (Exception ex)
				{
					lg.info(ex.getMessage());
					
					client.sendEvent("failure", ex.getMessage());
				}
			}
		});
		
		server.addEventListener("reliefUpdateInventory", JSONObject.class, new DataListener<JSONObject>(){
			public void onData(SocketIOClient client, JSONObject response, AckRequest ackRequest) {
				lg.info("Relief Update Inventory Requested.");
				
				try
				{
					DBAPI api = new DBAPI();
					
					client.sendEvent("reliefUpdateInventory", api.updateInventoryDetails(response));
				}
				catch(Exception ex)
				{
					lg.info(ex.getMessage());
					
					client.sendEvent("failure", ex.getMessage());
				}
			}
		});
		
		server.addEventListener("shelterInfo", JSONObject.class, new DataListener<JSONObject>(){
			public void onData(SocketIOClient client, JSONObject response, AckRequest ackRequest) {
				lg.info("Shelter Info Requested.");
				
				try
				{
					DBAPI api = new DBAPI();
					
					client.sendEvent("shelterInfo", api.getShelterById(response));
				}
				catch(Exception ex)
				{
					lg.info(ex.getMessage());
					
					client.sendEvent("failure", ex.getMessage());
				}
			}
		});
		
		server.addEventListener("reliefNeccUrg", JSONObject.class, new DataListener<JSONObject>(){
			public void onData(SocketIOClient client, JSONObject response, AckRequest ackRequest) {
				lg.info("Attending Event Relief Neccessity Urgency Requested.");
				
				try {
					DBAPI api = new DBAPI();
					
					client.sendEvent("releifNeccUrg", api.getAllRequests());
				}
				catch(Exception ex)
				{
					lg.info(ex.getMessage());
					
					client.sendEvent("failure", ex.getMessage());
				}
				
			}
		});
		
		server.addEventListener("supplyShelter", JSONObject.class, new DataListener<JSONObject>(){
			public void onData(SocketIOClient client, JSONObject response, AckRequest ackRequest) {
				lg.info("Supply Shelter Requested.");
				
				try
				{
					DBAPI api = new DBAPI();
					
					client.sendEvent("supply", api.requestMoreNecessities(response));
				}
				catch(Exception ex)
				{
					lg.info(ex.getMessage());
					
					client.sendEvent("failure", ex.getMessage());
					
					
				}
			}
		});
		
		
		
		server.addConnectListener(new ConnectListener() { 
					public void onConnect(SocketIOClient client) {
					lg.info("I have recieved a connection.");
					System.out.println(client.getRemoteAddress());
					
				//APIcall
			} } );
		
		server.addDisconnectListener(new DisconnectListener () {
					public void onDisconnect(SocketIOClient client) {
						lg.info("I am disconnecting");
						System.out.println(client.getRemoteAddress());
					}
				});
		
		server.start();
		
		try {
			synchronized(server) {
				server.wait(Integer.MAX_VALUE);
			}
		}
		catch(InterruptedException e) {
			e.printStackTrace();
		}
		server.stop();
	
	}
	
}
