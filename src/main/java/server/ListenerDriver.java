package server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;





public class ListenerDriver {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		Logger lg = LoggerFactory.getLogger(Listener.class);
		lg.info("Starting the SOS Server.");
	
		try {
			Listener server = Listener.instance("10.136.127.220", 1234);	
			server.ListenForEvents();
		} catch(Exception ex) {
			lg.error("Error:", ex);
			return;
		}
		
		
	}

}
