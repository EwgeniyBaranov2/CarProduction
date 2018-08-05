package by.baranov.dao.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import by.baranov.dao.IStreet;
import by.baranov.entities.Street;

public class StreetDAO extends AbstractDAO implements IStreet{

	private static Logger logger = LogManager.getLogger();
	
	private final static String GET_STREET_BY_ID = "SELECT * FROM STREETS WHERE ID IN (?)";
	private final static String GET_ALL_STREET = "SELECT ID, NAME FROM STREETS";
	private final static String CREATE_STREET = "INSERT INTO STREETS (ID, NAME) VALUES(?, ?)";
	private final static String UPDATE_STREET = "UPDATE STREETS SET NAME=? WHERE ID IN (?)";
	private final static String DELETE_STREET = "DELETE FROM STREETS WHERE ID IN (?)";
	
	@Override
	public List<Street> getAll() {
		List<Street> listStreet = new ArrayList<>();
		
		PreparedStatement ps = null;
		ResultSet rs = null;
		Connection connection = null;
		Street streetAll = new Street();
		try {
			
			connection = getConnectionPool().getConnection();
			ps = connection.prepareStatement(GET_ALL_STREET);
			rs = ps.executeQuery();
			while (rs.next()) {
				
				streetAll = setDataStreetTable(rs);
				listStreet.add(streetAll);			
			}						
			logger.log(Level.INFO, listStreet.toString());				
				
		} catch (SQLException e) {
			logger.log(Level.ERROR, "Error getting all street: " + e);
		} finally {
			closePreparedStatement(ps);
			closeResultSet(rs);
			getConnectionPool().returnConnection(connection);
		}
		return listStreet;
	}

	private Street setDataStreetTable(ResultSet rs) {
		Street streetAll = new Street();
		
		try {
			streetAll.setId(rs.getLong("id"));
			streetAll.setName(rs.getString("name"));
		} catch (SQLException e) {
			logger.log(Level.ERROR, "Error getting data from streetTable" + e);
		}
		return streetAll;
	}
	
	@Override
	public Street getById(Long id) {
		Street street = new Street();
		
		PreparedStatement ps = null;
		ResultSet rs = null;
		Connection connection = null;
		
		try {
			connection = getConnectionPool().getConnection();

			ps = connection.prepareStatement(GET_STREET_BY_ID);
			ps.setLong(1, id);
			rs = ps.executeQuery();
			rs.next();

			street = setDataStreetTable(rs);
			
			logger.log(Level.INFO, "Street [Id=" + street.getId() + ", name=" + street.getName() +"]");

		} catch (SQLException e) {
			logger.log(Level.ERROR, "Error getting a street by Id" + e);
		} finally {
			closePreparedStatement(ps);
			closeResultSet(rs);
			getConnectionPool().returnConnection(connection);
		}
		return street;
	}

	@Override
	public void create(Street street) {
		PreparedStatement ps = null;
		Connection connection = null;
		
		try{
			connection = getConnectionPool().getConnection();
			connection.setAutoCommit(false);
			ps = connection.prepareStatement(CREATE_STREET);
									
			ps.setLong(1, street.getId());
			ps.setString(2, street.getName());	
			ps.executeUpdate();
			connection.commit();
			
			logger.log(Level.INFO, "Creating street is successful");
			logger.log(Level.INFO, street.toString());
		}catch(SQLException e) {
			logger.log(Level.ERROR, "Error creating a street" + e);
			try {
				connection.rollback();
			} catch (SQLException e1) {
				logger.log(Level.ERROR, "Attempt to cancel query failed : " + e1);	
			}
		}finally {
			closePreparedStatement(ps);
			getConnectionPool().returnConnection(connection);
		}
		
	}

	@Override
	public void update(Street street) {
		PreparedStatement ps = null;
		Connection connection = null;
		try{
			connection = getConnectionPool().getConnection();
			connection.setAutoCommit(false);
			ps = connection.prepareStatement(UPDATE_STREET);
			
			ps.setLong(2, street.getId());	
			ps.setString(1, street.getName());
			ps.executeUpdate();
			connection.commit();
			
			logger.log(Level.INFO, "Updating street is successful");
			logger.log(Level.INFO, street.toString());
		}catch(SQLException e) {
			logger.log(Level.ERROR, "Error updating a street" + e);
			try {
				connection.rollback();
			} catch (SQLException e1) {
				logger.log(Level.ERROR, "Attempt to cancel query failed : " + e1);	
			}
		}finally {
			closePreparedStatement(ps);
			getConnectionPool().returnConnection(connection);
		}		
	}

	@Override
	public void delete(Street street) {
		PreparedStatement ps = null;
		Connection connection = null;
		try{
			connection = getConnectionPool().getConnection();
			connection.setAutoCommit(false);
			ps = connection.prepareStatement(DELETE_STREET);
			
			ps.setLong(1, street.getId());
			ps.executeUpdate();
			connection.commit();
			
			logger.log(Level.INFO, "Delete street is successful");			
		}catch(SQLException e) {
			logger.log(Level.ERROR, "Error deleting a street" + e);
			try {
				connection.rollback();
			} catch (SQLException e1) {
				logger.log(Level.ERROR, "Attempt to cancel query failed : " + e1);	
			}
		}finally {
			closePreparedStatement(ps);
			getConnectionPool().returnConnection(connection);
		}
		
	}
}

