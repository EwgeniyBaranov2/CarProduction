package by.baranov.connectionpool;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class ConnectionPool {
	private Lock lock = new ReentrantLock();
	
	private static final ConnectionPool CONNECTION_POOL = new ConnectionPool();
	private static Logger logger = LogManager.getLogger();

	private int poolSize;
	private BlockingQueue<Connection> availableConnections; 

	private String url;
	private String name;
	private String password;

	private ConnectionPool() {
//		try {
//			DriverManager.registerDriver(new com.mysql.cj.jdbc.Driver());
//		} catch (SQLException e) {
//		logger.log(Level.ERROR, "Error of ConnectionPool : " + e);
//		}
		
		ResourceBundle rb = ResourceBundle.getBundle("connection");
			try {
				Class.forName(rb.getString("DRIVER_JDBC"));
			} catch (ClassNotFoundException e) {
				logger.log(Level.ERROR, "Error of ConnectionPool : " + e);
			}
	}
	
	public void initialize(){
	availableConnections = new ArrayBlockingQueue<Connection>(getPoolSize());
	}
	
	public static ConnectionPool getConnectionPool() {
		return CONNECTION_POOL;
	}

	private int getPoolSize() {
		return poolSize;
	}
	public void setPoolSize(int poolSize) {
		this.poolSize = poolSize;
	}

	private String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}

	private String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

	private String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}

	private Connection createNewConnectionForPool() {
		Connection connection = null;
		try {
			connection = DriverManager.getConnection(getUrl(), getName(), getPassword());
			logger.log(Level.INFO, "New connection is created. Size : " + availableConnections.size());			
		} catch (SQLException e) {
			logger.log(Level.ERROR, "Can't create new connection : " + e);
		}
		return connection;
	}

	public Connection getConnection() {		
		Connection newConnection = null;
		lock.lock();
		if (availableConnections.isEmpty() && poolSize != 0) {			
			newConnection = createNewConnectionForPool();
			poolSize--;
			lock.unlock();
		} else if (availableConnections.size() != 0) {
			try {
				newConnection = availableConnections.take();	
				logger.log(Level.INFO, "The connection is taken from ConnectionPool without "
						+ "creating the new connection.Size : " + availableConnections.size());
			} catch (InterruptedException e) {
				logger.log(Level.ERROR, "Error of putting connection in the ConnectionPool "
						+ "(number of used connections less than number of created connections) : " + e);
			}finally{
				lock.unlock();
			}			
		} else { 
			try {
				logger.log(Level.ERROR,	"All connectoins from the ConnectionPool are busy. "
						+ "Wait, when connection will be available ...Size : " + availableConnections.size());				
				newConnection = availableConnections.take();
				logger.log(Level.INFO, "The ConnectionPool is available. New connection is available in the ConnectionPool. "
						+ "It was taken from the CoonectionPool");				
			} catch (InterruptedException e) {
				logger.log(Level.ERROR,
						"Error of putting connection in the ConnectionPool (all connections are busy) : " + e);
			}finally{
				lock.unlock();
			}		
		}
		return newConnection;
	}

	public void returnConnection(Connection connection) {
		try {			
			availableConnections.put(connection);
			logger.log(Level.INFO, "The connection was returned in the ConnectionPool. Size : " + availableConnections.size());
		} catch (InterruptedException e) {
			logger.log(Level.ERROR, "Cann't return the connection in the ConnectionPool : " + e);
			e.printStackTrace();
		}
	}
	
	public void  cleaningConnectionPool() {		
		while(availableConnections.remainingCapacity()!=0) {			
			Connection connection = createNewConnectionForPool();
			returnConnection(connection);					
		}
	}
	
	public void setParametrsConnectionPoolFromProperties() {
		ResourceBundle rb = ResourceBundle.getBundle("connection");
		
		setUrl(rb.getString("URL"));
		setName(rb.getString("USER_LOGIN"));
		setPassword(rb.getString("USER_PASSWORD"));
		setPoolSize(Integer.parseInt(rb.getString("POOL_SIZE")));
		initialize();
	}
	
}
