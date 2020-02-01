


import org.slf4j.LoggerFactory;

import com.corundumstudio.socketio.Configuration;
import com.corundumstudio.socketio.SocketIOServer;

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
			config.setOrigin("http://localhost:3000");
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
	
	}
	
}
