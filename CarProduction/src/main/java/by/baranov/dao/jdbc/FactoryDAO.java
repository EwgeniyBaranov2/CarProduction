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

import by.baranov.dao.IFactory;
import by.baranov.entities.Factory;

public class FactoryDAO extends AbstractDAO implements IFactory{
	
private static Logger logger = LogManager.getLogger();
	
	private final static String GET_FACTORY_BY_ID = "SELECT * FROM FACTORIES WHERE ID IN (?)";
	private final static String GET_ALL_FACTORY = "SELECT ID, NAME, ADRESSES_ID FROM FACTORIES";
	private final static String CREATE_FACTORY = "INSERT INTO FACTORIES (ID, NAME, ADRESSES_ID) VALUES(?, ?, ?)";
	private final static String UPDATE_FACTORY = "UPDATE FACTORIES SET NAME=?, ADRESSES_ID=? WHERE ID IN (?)";
	private final static String DELETE_FACTORY = "DELETE FROM FACTORIES WHERE ID IN (?)";
	private final static String GET_FACTORY_BY_CITY_NAME = "SELECT * FROM FACTORIES "
			+ "LEFT JOIN ADRESSES ON FACTORIES.ADRESSES_ID = ADRESSES.ID "
			+ "LEFT JOIN CITIES ON ADRESSES.CITIES_ID = CITIES.ID WHERE CITIES.NAME LIKE ?";
	private final static String GET_FACTORIES_BY_OWNER_ID = "SELECT * FROM FACTORIES "
			+ "LEFT JOIN FACTORIES_HAS_OWNERS ON FACTORIES.ID = FACTORIES_HAS_OWNERS.FACTORIES_ID "
			+ "LEFT JOIN OWNERS ON  FACTORIES_HAS_OWNERS.OWNERS_ID = OWNERS.ID WHERE OWNERS.ID IN (?)";
	
	@Override
	public List<Factory> getFactoriesByOwnerId(Long ownerId) {
		List<Factory> listFactoriesOwner = new ArrayList<>();
		
		PreparedStatement ps = null;
		ResultSet rs = null;
		Connection connection = null;
		Factory factoryOwner = new Factory();
		
		try {			
			connection = getConnectionPool().getConnection();
			ps = connection.prepareStatement(GET_FACTORIES_BY_OWNER_ID);
			ps.setLong(1, ownerId);
			rs = ps.executeQuery();
			while (rs.next()) {				
				factoryOwner = setDataFactoryTable(rs);				
				listFactoriesOwner.add(factoryOwner);			
			}						
			logger.log(Level.INFO, listFactoriesOwner.toString());				
				
		} catch (SQLException e) {
			logger.log(Level.ERROR, "Error getting all factories of the owner : " + e);
		} finally {
			closePreparedStatement(ps);
			closeResultSet(rs);
			getConnectionPool().returnConnection(connection);
		}
		return listFactoriesOwner;
	}
	
	@Override
	public List<Factory> getFactoryByCityName(String cityName) {
		List<Factory> listFactory = new ArrayList<>();
		
		PreparedStatement ps = null;
		ResultSet rs = null;
		Connection connection = null;
		Factory factoryCity = new Factory();
		
		try {			
			connection = getConnectionPool().getConnection();
			ps = connection.prepareStatement(GET_FACTORY_BY_CITY_NAME);
			ps.setString(1, cityName);
			rs = ps.executeQuery();
			while (rs.next()) {				
				factoryCity = setDataFactoryTable(rs);				
				listFactory.add(factoryCity);			
			}						
			logger.log(Level.INFO, listFactory.toString());				
				
		} catch (SQLException e) {
			logger.log(Level.ERROR, "Error getting factories by name of city : " + e);
		} finally {
			closePreparedStatement(ps);
			closeResultSet(rs);
			getConnectionPool().returnConnection(connection);
		}
		return listFactory;
	}
	
	@Override
	public List<Factory> getAll() {
		List<Factory> listFactory = new ArrayList<>();
		
		PreparedStatement ps = null;
		ResultSet rs = null;
		Connection connection = null;
		Factory factoryAll = new Factory();
		try {
			
			connection = getConnectionPool().getConnection();
			ps = connection.prepareStatement(GET_ALL_FACTORY);
			rs = ps.executeQuery();
			while (rs.next()) {
				
				factoryAll = setDataFactoryTable(rs);
				listFactory.add(factoryAll);			
			}						
			logger.log(Level.INFO, listFactory.toString());				
				
		} catch (SQLException e) {
			logger.log(Level.ERROR, "Error getting all factory: " + e);
		} finally {
			closePreparedStatement(ps);
			closeResultSet(rs);
			getConnectionPool().returnConnection(connection);
		}
		return listFactory;
	}

	private Factory setDataFactoryTable(ResultSet rs) {
		Factory factoryAll = new Factory();
		AddressDAO addressDAO = new AddressDAO();

		try {
			factoryAll.setId(rs.getLong("id"));
			factoryAll.setName(rs.getString("name"));
			factoryAll.setAddress(addressDAO.getById(rs.getLong("adresses_id")));	
		} catch (SQLException e) {
			logger.log(Level.ERROR, "Error getting data from factoryTable" + e);
		}
		return factoryAll;
	}
	
	@Override
	public Factory getById(Long id) {
		Factory factory = new Factory();
				
		PreparedStatement ps = null;
		ResultSet rs = null;
		Connection connection = null;
		
		try {
			connection = getConnectionPool().getConnection();

			ps = connection.prepareStatement(GET_FACTORY_BY_ID);
			ps.setLong(1, id);
			rs = ps.executeQuery();
			rs.next();

			factory = setDataFactoryTable(rs);
												
			logger.log(Level.INFO, "Factory [Id=" + factory.getId() + ", name=" + factory.getName() +
					", adresses_id=" + factory.getAddress() +"]");

		} catch (SQLException e) {
			logger.log(Level.ERROR, "Error getting a factory by Id" + e);
		} finally {
			closePreparedStatement(ps);
			closeResultSet(rs);
			getConnectionPool().returnConnection(connection);
		}
		return factory;
	}

	@Override
	public void create(Factory factory) {
		PreparedStatement ps = null;
		Connection connection = null;
		
		try{
			connection = getConnectionPool().getConnection();
			connection.setAutoCommit(false);
			ps = connection.prepareStatement(CREATE_FACTORY);
									
			ps.setLong(1, factory.getId());
			ps.setString(2, factory.getName());		
			ps.setLong(3, factory.getAddress().getId());									
			ps.executeUpdate();
			connection.commit();
			
			logger.log(Level.INFO, "Creating factory is successful");
			logger.log(Level.INFO, factory.toString());
		}catch(SQLException e) {
			logger.log(Level.ERROR, "Error creating a factory" + e);
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
	public void update(Factory factory) {
		PreparedStatement ps = null;
		Connection connection = null;
		try{
			connection = getConnectionPool().getConnection();
			connection.setAutoCommit(false);
			ps = connection.prepareStatement(UPDATE_FACTORY);
			
			ps.setLong(3, factory.getId());	
			ps.setString(1, factory.getName());			
			ps.setLong(2, factory.getAddress().getId());
			ps.executeUpdate();
			connection.commit();
			
			logger.log(Level.INFO, "Updating factory is successful");
			logger.log(Level.INFO, factory.toString());
		}catch(SQLException e) {
			logger.log(Level.ERROR, "Error updating a factory" + e);
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
	public void delete(Factory factory) {
		PreparedStatement ps = null;
		Connection connection = null;
		try{
			connection = getConnectionPool().getConnection();
			connection.setAutoCommit(false);
			ps = connection.prepareStatement(DELETE_FACTORY);
			
			ps.setLong(1, factory.getId());
			ps.executeUpdate();
			connection.commit();
			
			logger.log(Level.INFO, "Delete factory is successful");			
		}catch(SQLException e) {
			logger.log(Level.ERROR, "Error deleting a factory" + e);
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