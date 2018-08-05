package by.baranov.dao.jdbc;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.commons.dbutils.DbUtils;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import by.baranov.connectionpool.ConnectionPool;

public abstract class AbstractDAO {

	private static Logger logger = LogManager.getLogger();
	private ConnectionPool connectionPool = ConnectionPool.getConnectionPool();
	
	public void closePreparedStatement(PreparedStatement statement) {
		try {
			DbUtils.close(statement);
			logger.log(Level.INFO, "PreparedStatement is closed");
		} catch (SQLException e) {
			logger.log(Level.ERROR, "Error closing PreparedStatement: " + e);
		}
	}
		
	public void closeResultSet(ResultSet resultSet) {
		try {
			DbUtils.close(resultSet);
			logger.log(Level.INFO, "ResultSet is closed");
		} catch (SQLException e) {
			logger.log(Level.ERROR, "Error closing ResultSet: ", e);
		}
	}

	public ConnectionPool getConnectionPool() {
		return connectionPool;
	}

//	public void setConnectionPool(ConnectionPool connectionPool) {
//		this.connectionPool = connectionPool;
//	}

}
