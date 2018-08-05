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

import by.baranov.dao.IOwner;
import by.baranov.entities.Owner;

public class OwnerDAO extends AbstractDAO implements IOwner{
	
private static Logger logger = LogManager.getLogger();
	
	private final static String GET_OWNER_BY_ID = "SELECT * FROM OWNERS WHERE ID IN (?)";
	private final static String GET_ALL_OWNER = "SELECT ID, NAME, DATE_OF_BIRTH, ADRESSES_ID FROM OWNERS";
	private final static String CREATE_OWNER = "INSERT INTO OWNERS (ID, NAME, DATE_OF_BIRTH, ADRESSES_ID) VALUES(?, ?, ?, ?)";
	private final static String UPDATE_OWNER = "UPDATE OWNERS SET NAME=?, DATE_OF_BIRTH=?, ADRESSES_ID=? WHERE ID IN (?)";
	private final static String DELETE_OWNER = "DELETE FROM OWNERS WHERE ID IN (?)";
	private final static String GET_OWNERS_BY_FACTORY_ID = "SELECT * FROM OWNERS "
			+ "LEFT JOIN FACTORIES_HAS_OWNERS ON OWNERS.ID = FACTORIES_HAS_OWNERS.OWNERS_ID "
			+ "LEFT JOIN FACTORIES ON  FACTORIES_HAS_OWNERS.FACTORIES_ID = FACTORIES.ID WHERE FACTORIES.ID IN (?)";
	private final static String GET_OWNERS_BY_CITY_NAME = "SELECT OWNERS.ID, OWNERS.NAME, OWNERS.DATE_OF_BIRTH, OWNERS.ADRESSES_ID"
			+ " FROM OWNERS LEFT JOIN ADRESSES ON OWNERS.ADRESSES_ID = ADRESSES.ID"
			+ "	LEFT JOIN CITIES ON ADRESSES.CITIES_ID = CITIES.ID"
			+ "	WHERE CITIES.NAME LIKE ?";
	
	@Override
	public List<Owner> getOwnersByCityName(String cityName){
		List<Owner> listOwnersCity = new ArrayList<>();
		
		PreparedStatement ps = null;
		ResultSet rs = null;
		Connection connection = null;
		Owner ownerCity = new Owner();
		
		try {
			
			connection = getConnectionPool().getConnection();
			ps = connection.prepareStatement(GET_OWNERS_BY_CITY_NAME);
			ps.setString(1, cityName);
			rs = ps.executeQuery();
			while (rs.next()) {
				
				ownerCity = setDataOwnerTable(rs);
				listOwnersCity.add(ownerCity);			
			}						
			logger.log(Level.INFO, listOwnersCity.toString());				
				
		} catch (SQLException e) {
			logger.log(Level.ERROR, "Error getting all owners of the city: " + e);
		} finally {
			closePreparedStatement(ps);
			closeResultSet(rs);
			getConnectionPool().returnConnection(connection);
		}
		return listOwnersCity;
	}
	
	@Override
	public List<Owner> getOwnersByFactoryId(Long factoryId){
		List<Owner> listOwnersFactory = new ArrayList<>();
		
		PreparedStatement ps = null;
		ResultSet rs = null;
		Connection connection = null;
		Owner ownerFactory = new Owner();
		
		try {
			
			connection = getConnectionPool().getConnection();
			ps = connection.prepareStatement(GET_OWNERS_BY_FACTORY_ID);
			ps.setLong(1, factoryId);
			rs = ps.executeQuery();
			while (rs.next()) {
				
				ownerFactory = setDataOwnerTable(rs);
				listOwnersFactory.add(ownerFactory);			
			}						
			logger.log(Level.INFO, listOwnersFactory.toString());				
				
		} catch (SQLException e) {
			logger.log(Level.ERROR, "Error getting all owners of the factory: " + e);
		} finally {
			closePreparedStatement(ps);
			closeResultSet(rs);
			getConnectionPool().returnConnection(connection);
		}
		return listOwnersFactory;
		
	}
	
	@Override
	public List<Owner> getAll() {
		List<Owner> listOwner = new ArrayList<>();
		
		PreparedStatement ps = null;
		ResultSet rs = null;
		Connection connection = null;
		Owner ownerAll = new Owner();
		
		try {
			
			connection = getConnectionPool().getConnection();
			ps = connection.prepareStatement(GET_ALL_OWNER);
			rs = ps.executeQuery();
			while (rs.next()) {
				
				ownerAll = setDataOwnerTable(rs);
				listOwner.add(ownerAll);			
			}						
			logger.log(Level.INFO, listOwner.toString());				
				
		} catch (SQLException e) {
			logger.log(Level.ERROR, "Error getting all owner: " + e);
		} finally {
			closePreparedStatement(ps);
			closeResultSet(rs);
			getConnectionPool().returnConnection(connection);
		}
		return listOwner;
	}

	private Owner setDataOwnerTable(ResultSet rs) {
		Owner ownerAll = new Owner();
		AddressDAO addressDAO = new AddressDAO();
		
		try {
			ownerAll.setId(rs.getLong("id"));
			ownerAll.setName(rs.getString("name"));
			ownerAll.setDate(rs.getDate("date_of_birth").toLocalDate());
			ownerAll.setAddress(addressDAO.getById(rs.getLong("adresses_id")));
		} catch (SQLException e) {
			logger.log(Level.ERROR, "Error getting data from ownerTable" + e);
		}
		return ownerAll;
	}
	
	@Override
	public Owner getById(Long id) {
		Owner owner = new Owner();
				
		PreparedStatement ps = null;
		ResultSet rs = null;
		Connection connection = null;
		
		try {
			connection = getConnectionPool().getConnection();

			ps = connection.prepareStatement(GET_OWNER_BY_ID);
			ps.setLong(1, id);
			rs = ps.executeQuery();
			rs.next();

			owner = setDataOwnerTable(rs);
			
			logger.log(Level.INFO, "Owner [Id=" + owner.getId() + ", name=" + owner.getName() +
					", date_of_birth=" + owner.getDate() +", adresses_id=" + owner.getAddress().getId() +"]");
		} catch (SQLException e) {
			logger.log(Level.ERROR, "Error getting an owner by Id" + e);
		} finally {
			closePreparedStatement(ps);
			closeResultSet(rs);
			getConnectionPool().returnConnection(connection);
		}
		return owner;
	}

	@Override
	public void create(Owner owner) {
		PreparedStatement ps = null;
		Connection connection = null;
		
		try{
			connection = getConnectionPool().getConnection();
			connection.setAutoCommit(false);
			ps = connection.prepareStatement(CREATE_OWNER);
									
			ps.setLong(1, owner.getId());
			ps.setString(2, owner.getName());
			ps.setObject(3, owner.getDate());
			ps.setLong(4, owner.getAddress().getId());
			ps.executeUpdate();
			connection.commit();
			
			logger.log(Level.INFO, "Creating owner is successful");
			logger.log(Level.INFO, owner.toString());
		}catch(SQLException e) {
			logger.log(Level.ERROR, "Error creating an owner" + e);
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
	public void update(Owner owner) {
		PreparedStatement ps = null;
		Connection connection = null;
		try{
			connection = getConnectionPool().getConnection();
			connection.setAutoCommit(false);
			ps = connection.prepareStatement(UPDATE_OWNER);
			
			ps.setLong(4, owner.getId());	
			ps.setString(1, owner.getName());
			ps.setObject(2, owner.getDate());
			ps.setLong(3, owner.getAddress().getId());
			ps.executeUpdate();
			connection.commit();
			
			logger.log(Level.INFO, "Updating owner is successful");
			logger.log(Level.INFO, owner.toString());
		}catch(SQLException e) {
			logger.log(Level.ERROR, "Error updating an owner" + e);
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
	public void delete(Owner owner) {
		PreparedStatement ps = null;
		Connection connection = null;
		try{
			connection = getConnectionPool().getConnection();
			connection.setAutoCommit(false);
			
			ps = connection.prepareStatement(DELETE_OWNER);
			
			ps.setLong(1, owner.getId());
			ps.executeUpdate();
			connection.commit();
			
			logger.log(Level.INFO, "Delete owner is successful");			
		}catch(SQLException e) {
			logger.log(Level.ERROR, "Error deleting an owner" + e);
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
