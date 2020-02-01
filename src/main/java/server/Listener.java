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
				lg.info("Attending Event Register Shelters Requested.");
				try {
				DBAPI api = new DBAPI();
				api.registerShelter(response);
			}catch(Exception ex){
				client.sendEvent("failure", "Registration Failure");
			}
			}
		});
		
		server.addEventListener("registerRelief", JSONObject.class, new DataListener<JSONObject>(){
			public void onData(SocketIOClient client, JSONObject response, AckRequest ackRequest) {
				lg.info("Attending Event Register Relief Requested.");
				try {
					DBAPI api = new DBAPI();
					api.registerRelief(response);
				}catch(Exception ex){
					client.sendEvent("failure", "Registration Failure");
				}
				}
			});
		
		server.addEventListener("shelterNeccAmt", JSONObject.class, new DataListener<JSONObject>(){
			public void onData(SocketIOClient client, JSONObject response, AckRequest ackRequest) {
				lg.info("Attending Event Shelter Neccessity Amount Requested.");
				try {
					DBAPI api = new DBAPI();
					api.shelterNeccAmt(response);
				}catch(Exception ex){
					client.sendEvent("failure", "Response Failure");
				}
				}
			});
		
		server.addEventListener("shelterReqSupp", JSONObject.class, new DataListener<JSONObject>(){
			public void onData(SocketIOClient client, JSONObject response, AckRequest ackRequest) {
				lg.info("Attending Event Shelter Shelter Request Supply Requested.");
				//APIcall
			}
		});
		
		server.addEventListener("reliefInventory", JSONObject.class, new DataListener<JSONObject>(){
			public void onData(SocketIOClient client, JSONObject response, AckRequest ackRequest) {
				lg.info("Attending Event Relief Inventory Requested.");
				//APIcall
			}
		});
		
		server.addEventListener("reliefUpdateInventory", JSONObject.class, new DataListener<JSONObject>(){
			public void onData(SocketIOClient client, JSONObject response, AckRequest ackRequest) {
				lg.info("Attending Event Relief Update Inventory Requested.");
				//APIcall
			}
		});
		
		server.addEventListener("shelterInfo", JSONObject.class, new DataListener<JSONObject>(){
			public void onData(SocketIOClient client, JSONObject response, AckRequest ackRequest) {
				lg.info("Attending Event Shelter Info Requested.");
				//APIcall
			}
		});
		
		server.addEventListener("reliefNeccUrg", JSONObject.class, new DataListener<JSONObject>(){
			public void onData(SocketIOClient client, JSONObject response, AckRequest ackRequest) {
				lg.info("Attending Event Relief Neccessity Urgency Requested.");
				//APIcall
			}
		});
		
		server.addEventListener("supplyCharter", JSONObject.class, new DataListener<JSONObject>(){
			public void onData(SocketIOClient client, JSONObject response, AckRequest ackRequest) {
				lg.info("Attending Event Supply Charter Requested.");
				//APIcall
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
