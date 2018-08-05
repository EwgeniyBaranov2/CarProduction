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

import by.baranov.dao.IHouse;
import by.baranov.entities.House;

public class HouseDAO extends AbstractDAO implements IHouse{

	private static Logger logger = LogManager.getLogger();
	
	private final static String GET_HOUSE_BY_ID = "SELECT * FROM HOUSES WHERE ID IN (?)";
	private final static String GET_ALL_HOUSE = "SELECT ID, NUMBER_OF_HOUSE FROM HOUSES";
	private final static String CREATE_HOUSE = "INSERT INTO HOUSES (ID, NUMBER_OF_HOUSE) VALUES(?, ?)";
	private final static String UPDATE_HOUSE = "UPDATE HOUSES SET NUMBER_OF_HOUSE=? WHERE ID IN (?)";
	private final static String DELETE_HOUSE = "DELETE FROM HOUSES WHERE ID IN (?)";
	
	@Override
	public List<House> getAll() {
		List<House> listHouse = new ArrayList<>();
		
		PreparedStatement ps = null;
		ResultSet rs = null;
		Connection connection = null;
		House houseAll = new House();
		try {
			
			connection = getConnectionPool().getConnection();
			ps = connection.prepareStatement(GET_ALL_HOUSE);
			rs = ps.executeQuery();
			while (rs.next()) {
				houseAll = setDataHouseTable(rs);
				listHouse.add(houseAll);			
			}						
			logger.log(Level.INFO, listHouse.toString());				
				
		} catch (SQLException e) {
			logger.log(Level.ERROR, "Error getting all house: " + e);
		} finally {
			closePreparedStatement(ps);
			closeResultSet(rs);
			getConnectionPool().returnConnection(connection);
		}
		return listHouse;
	}

	private House setDataHouseTable(ResultSet rs) {
		House houseAll = new House();

		try {
			houseAll.setId(rs.getLong("id"));
			houseAll.setNumberOfHouse(rs.getString("number_of_house"));	
		} catch (SQLException e) {
			logger.log(Level.ERROR, "Error getting data from houseTable" + e);
		}
		return houseAll;
	}
	
	@Override
	public House getById(Long id) {
		House house = new House();
		
		PreparedStatement ps = null;
		ResultSet rs = null;
		Connection connection = null;
		
		try {
			connection = getConnectionPool().getConnection();

			ps = connection.prepareStatement(GET_HOUSE_BY_ID);
			ps.setLong(1, id);
			rs = ps.executeQuery();
			rs.next();

			house = setDataHouseTable(rs);
			
			logger.log(Level.INFO, "House [Id=" + house.getId() + ", number_of_house=" + house.getNumberOfHouse() +"]");

		} catch (SQLException e) {
			logger.log(Level.ERROR, "Error getting an house by Id" + e);
		} finally {
			closePreparedStatement(ps);
			closeResultSet(rs);
			getConnectionPool().returnConnection(connection);
		}
		return house;
	}

	@Override
	public void create(House house) {
		PreparedStatement ps = null;
		Connection connection = null;
		
		try{
			connection = getConnectionPool().getConnection();
			connection.setAutoCommit(false);
			ps = connection.prepareStatement(CREATE_HOUSE);
									
			ps.setLong(1, house.getId());
			ps.setString(2, house.getNumberOfHouse());
			ps.executeUpdate();
			connection.commit();
			
			logger.log(Level.INFO, "Creating house is successful");
			logger.log(Level.INFO, house.toString());
		}catch(SQLException e) {
			logger.log(Level.ERROR, "Error creating an house" + e);
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
	public void update(House house) {
		PreparedStatement ps = null;
		Connection connection = null;
		try{
			connection = getConnectionPool().getConnection();
			connection.setAutoCommit(false);
			ps = connection.prepareStatement(UPDATE_HOUSE);
			
			ps.setLong(2, house.getId());	
			ps.setString(1, house.getNumberOfHouse());
			ps.executeUpdate();
			connection.commit();
			
			logger.log(Level.INFO, "Updating house is successful");
			logger.log(Level.INFO, house.toString());
		}catch(SQLException e) {
			logger.log(Level.ERROR, "Error updating an house" + e);
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
	public void delete(House house) {
		PreparedStatement ps = null;
		Connection connection = null;
		try{
			connection = getConnectionPool().getConnection();
			connection.setAutoCommit(false);
			ps = connection.prepareStatement(DELETE_HOUSE);
			
			ps.setLong(1, house.getId());
			ps.executeUpdate();
			connection.commit();
			
			logger.log(Level.INFO, "Delete house is successful");			
		}catch(SQLException e) {
			logger.log(Level.ERROR, "Error deleting an house" + e);
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
