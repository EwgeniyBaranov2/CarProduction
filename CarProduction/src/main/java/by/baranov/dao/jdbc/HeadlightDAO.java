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

import by.baranov.dao.IHeadlight;
import by.baranov.entities.Headlight;

public class HeadlightDAO extends AbstractDAO implements IHeadlight{
	
	private static Logger logger = LogManager.getLogger();
	
	private final static String GET_HEADLIGHT_BY_ID = "SELECT * FROM HEADLIGHTS WHERE ID IN (?)";
	private final static String GET_ALL_HEADLIGHT = "SELECT ID, CARS_ID, NAME, DATE_OF_MANUFACTURE, FACTORIES_ID FROM HEADLIGHTS";
	private final static String CREATE_HEADLIGHT = "INSERT INTO HEADLIGHTS (ID, CARS_ID, NAME, DATE_OF_MANUFACTURE, FACTORIES_ID) VALUES(?, ?, ?, ?, ?)";
	private final static String UPDATE_HEADLIGHT = "UPDATE HEADLIGHTS SET CARS_ID=?, NAME=?, DATE_OF_MANUFACTURE=?, FACTORIES_ID=? WHERE ID IN (?)";
	private final static String DELETE_HEADLIGHT = "DELETE FROM HEADLIGHTS WHERE ID IN (?)";
	private final static String GET_HEADLIGHT_BY_CAR_ID = "SELECT * FROM HEADLIGHTS LEFT JOIN CARS ON HEADLIGHTS.CARS_ID = CARS.ID WHERE HEADLIGHTS.CARS_ID IN (?)";
	
	
	@Override
	public List<Headlight> getHeadlightsByCarId(Long carsId){
		List<Headlight> listHeadlights = new ArrayList<>();
		PreparedStatement ps = null;
		ResultSet rs = null;
		Connection connection = null;
		Headlight headlightAll = new Headlight();
		
		try {			
			connection = getConnectionPool().getConnection();
			ps = connection.prepareStatement(GET_HEADLIGHT_BY_CAR_ID);
			ps.setLong(1, carsId);
			rs = ps.executeQuery();
			while (rs.next()) {
				headlightAll = setDataHeadlightTable(rs);
				listHeadlights.add(headlightAll);			
			}						
			logger.log(Level.INFO, listHeadlights.toString());					
		} catch (SQLException e) {
			logger.log(Level.ERROR, "Error getting all headlight: " + e);
		} finally {
			closePreparedStatement(ps);
			closeResultSet(rs);
			getConnectionPool().returnConnection(connection);
		}
		return listHeadlights;
		
	}
	@Override
	public List<Headlight> getAll() {
		List<Headlight> listHeadlight = new ArrayList<>();
		
		PreparedStatement ps = null;
		ResultSet rs = null;
		Connection connection = null;
		Headlight headlightAll = new Headlight();
		try {
			
			connection = getConnectionPool().getConnection();
			ps = connection.prepareStatement(GET_ALL_HEADLIGHT);
			rs = ps.executeQuery();
			while (rs.next()) {
				headlightAll = setDataHeadlightTable(rs);
				listHeadlight.add(headlightAll);			
			}						
			logger.log(Level.INFO, listHeadlight.toString());				
				
		} catch (SQLException e) {
			logger.log(Level.ERROR, "Error getting all headlight: " + e);
		} finally {
			closePreparedStatement(ps);
			closeResultSet(rs);
			getConnectionPool().returnConnection(connection);
		}
		return listHeadlight;
	}

	private Headlight setDataHeadlightTable(ResultSet rs) {
		Headlight headlightAll = new Headlight();
		CarDAO carDAO = new CarDAO();
		FactoryDAO factoryDAO = new FactoryDAO();

		try {
			headlightAll.setId(rs.getLong("id"));
			headlightAll.setCar(carDAO.getById(rs.getLong("cars_id")));
			headlightAll.setName(rs.getString("name"));
			headlightAll.setDateOfManufacture(rs.getDate("date_of_manufacture").toLocalDate());
			headlightAll.setFactory(factoryDAO.getById(rs.getLong("factories_id")));	
		} catch (SQLException e) {
			logger.log(Level.ERROR, "Error getting data from headlightTable" + e);
		}
		return headlightAll;
	}
	
	@Override
	public Headlight getById(Long id) {
		Headlight headlight = new Headlight();
				
		PreparedStatement ps = null;
		ResultSet rs = null;
		Connection connection = null;
		
		try {
			connection = getConnectionPool().getConnection();

			ps = connection.prepareStatement(GET_HEADLIGHT_BY_ID);
			ps.setLong(1, id);
			rs = ps.executeQuery();
			rs.next();

			headlight = setDataHeadlightTable(rs);				

			logger.log(Level.INFO, "Headlight [Id=" + headlight.getId() + 
					",  name=" + headlight.getName() + 
					", date_of_manufacture=" + headlight.getDateOfManufacture() +  
					", factory=" + headlight.getFactory() +"]");

		} catch (SQLException e) {
			logger.log(Level.ERROR, "Error getting an headlight by Id" + e);
		} finally {
			closePreparedStatement(ps);
			closeResultSet(rs);
			getConnectionPool().returnConnection(connection);
		}
		return headlight;
	}

	@Override
	public void create(Headlight headlight) {
		PreparedStatement ps = null;
		Connection connection = null;
		
		try{
			connection = getConnectionPool().getConnection();
			connection.setAutoCommit(false);
			ps = connection.prepareStatement(CREATE_HEADLIGHT);
									
			ps.setLong(1, headlight.getId());
			ps.setLong(2, headlight.getCar().getId());
			ps.setString(3, headlight.getName());
			ps.setObject(4, headlight.getDateOfManufacture());
			ps.setLong(5, headlight.getFactory().getId());			
			ps.executeUpdate();
			connection.commit();
			
			logger.log(Level.INFO, "Creating headlight is successful");
			logger.log(Level.INFO, headlight.toString());
		}catch(SQLException e) {
			logger.log(Level.ERROR, "Error creating an headlight" + e);
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
	public void update(Headlight headlight) {
		PreparedStatement ps = null;
		Connection connection = null;
		try{
			connection = getConnectionPool().getConnection();
			connection.setAutoCommit(false);
			ps = connection.prepareStatement(UPDATE_HEADLIGHT);
			
			ps.setLong(5, headlight.getId());
			ps.setLong(1, headlight.getCar().getId());
			ps.setString(2, headlight.getName());
			ps.setObject(3, headlight.getDateOfManufacture());
			ps.setLong(4, headlight.getFactory().getId());	
			ps.executeUpdate();
			connection.commit();
			
			logger.log(Level.INFO, "Updating headlight is successful");
			logger.log(Level.INFO, headlight.toString());
		}catch(SQLException e) {
			logger.log(Level.ERROR, "Error updating an headlight" + e);
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
	public void delete(Headlight headlight) {
		PreparedStatement ps = null;
		Connection connection = null;
		try{
			connection = getConnectionPool().getConnection();
			connection.setAutoCommit(false);
			ps = connection.prepareStatement(DELETE_HEADLIGHT);
			
			ps.setLong(1, headlight.getId());
			ps.executeUpdate();
			connection.commit();
			
			logger.log(Level.INFO, "Delete headlight is successful");			
		}catch(SQLException e) {
			logger.log(Level.ERROR, "Error deleting an headlight" + e);
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
