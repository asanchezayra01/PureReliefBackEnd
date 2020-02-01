package server;



import org.slf4j.LoggerFactory;

import com.corundumstudio.socketio.AckRequest;
import com.corundumstudio.socketio.Configuration;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.listener.DataListener;

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
			config.setOrigin("https://10.136.127.220:"+portNumber);
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
		
		server.addEventListener("registerShelter", String.class, new DataListener<String>(){
			public void onData(SocketIOClient client, String response, AckRequest ackRequest) {
				lg.info("Attending Event Requested.");
			}
		});
		
		server.addEventListener("shelterNeccAmt", String.class, new DataListener<String>(){
			public void onData(SocketIOClient client, String response, AckRequest ackRequest) {
				lg.info("Attending Event Requested.");
				//APIcall
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
