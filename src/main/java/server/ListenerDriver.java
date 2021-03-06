package server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.json.*;


public class ListenerDriver {

	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub

		JSONObject json = new JSONObject();
		json.put("username", "shelterA");
		json.put("password", "password");
		json.put("image", "");
		
		System.out.println(json);
		
		Logger lg = LoggerFactory.getLogger(Listener.class);
		lg.info("Starting the PureRelief Server.");
	
		try {
			Listener server = Listener.instance("10.136.189.34", 48900);	
			server.ListenForEvents();
		} catch(Exception ex) {
			lg.error("Error:", ex);
			return;
		}
		
		
	}

}
