package by.baranov.connectionpool;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class UserThread extends Thread{
	private ConnectionPool connections;
	private static Logger logger = LogManager.getLogger();	
	
	public UserThread(ConnectionPool connections) {
		super();
		this.connections = connections;
	}

	public void run() {
		logger.log(Level.INFO, "UserThread is created");
		connections.getConnection();		
	}
}
