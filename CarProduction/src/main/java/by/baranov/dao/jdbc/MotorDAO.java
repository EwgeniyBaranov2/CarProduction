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

import by.baranov.dao.IMotor;
import by.baranov.entities.Motor;

public class MotorDAO extends AbstractDAO implements IMotor{
	
	private static Logger logger = LogManager.getLogger();
	
	private final static String GET_MOTOR_BY_ID = "SELECT * FROM MOTORS WHERE ID IN (?)";
	private final static String GET_ALL_MOTOR = "SELECT * FROM MOTORS";
	private final static String CREATE_MOTOR = "INSERT INTO MOTORS (ID, FORCE, DATE_OF_MANUFACTURE, FACTORIES_ID) VALUES(?, ?, ?, ?)";			
	private final static String UPDATE_MOTOR = "UPDATE MOTORS SET FORCE=?, DATE_OF_MANUFACTURE=?, FACTORIES_ID=? WHERE ID IN (?)";
	private final static String DELETE_MOTOR = "DELETE FROM MOTORS WHERE ID IN (?)";
	
	@Override
	public List<Motor> getAll() {
		List<Motor> listMotor = new ArrayList<>();
		
		PreparedStatement ps = null;
		ResultSet rs = null;
		Connection connection = null;
		Motor motorAll = new Motor();
		try {
			
			connection = getConnectionPool().getConnection();
			ps = connection.prepareStatement(GET_ALL_MOTOR);
			rs = ps.executeQuery();
			while (rs.next()) {
				motorAll = setDataMotorTable(rs);
				listMotor.add(motorAll);			
			}						
			logger.log(Level.INFO, listMotor.toString());				
				
		} catch (SQLException e) {
			logger.log(Level.ERROR, "Error getting all motor: " + e);
		} finally {
			closePreparedStatement(ps);
			closeResultSet(rs);
			getConnectionPool().returnConnection(connection);
		}
		return listMotor;
	}

	private Motor setDataMotorTable(ResultSet rs) {
		Motor motorAll = new Motor();
		FactoryDAO factoryDAO = new FactoryDAO();
		
		try {
			motorAll.setId(rs.getLong("id"));
			motorAll.setForce(rs.getInt("force"));
			motorAll.setDateOfManufacture(rs.getDate("date_of_manufacture").toLocalDate());
			motorAll.setFactory(factoryDAO.getById(rs.getLong("factories_id")));	
		} catch (SQLException e) {
			logger.log(Level.ERROR, "Error getting data from motorTable" + e);
		}
		return motorAll;
	}
	
	@Override
	public Motor getById(Long id) {
		Motor motor = new Motor();
			
		PreparedStatement ps = null;
		ResultSet rs = null;
		Connection connection = null;
		
		try {
			connection = getConnectionPool().getConnection();

			ps = connection.prepareStatement(GET_MOTOR_BY_ID);
			ps.setLong(1, id);
			rs = ps.executeQuery();
			rs.next();

			motor = setDataMotorTable(rs);		

			logger.log(Level.INFO, "Motor[Id=" + motor.getId() + ", force=" + motor.getForce() + ", date_of_manufacture=" + motor.getDateOfManufacture() +  
					", factory=" + motor.getFactory() +"]");
		} catch (SQLException e) {
			logger.log(Level.ERROR, "Error getting a motor by Id" + e);
		} finally {
			closePreparedStatement(ps);
			closeResultSet(rs);
			getConnectionPool().returnConnection(connection);
		}
		return motor;
	}

	@Override
	public void create(Motor motor) {
		PreparedStatement ps = null;
		Connection connection = null;
		
		try{
			connection = getConnectionPool().getConnection();
			connection.setAutoCommit(false);
			ps = connection.prepareStatement(CREATE_MOTOR);
									
			ps.setLong(1, motor.getId());			
			ps.setInt(2, motor.getForce());
			ps.setObject(3, motor.getDateOfManufacture());
			ps.setLong(4, motor.getFactory().getId());
			ps.executeUpdate();
			connection.commit();
			
			logger.log(Level.INFO, "Creating motor is successful");
			logger.log(Level.INFO, motor.toString());
		}catch(SQLException e) {
			logger.log(Level.ERROR, "Error creating a motor : " + e);
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
	public void update(Motor motor) {
		PreparedStatement ps = null;
		Connection connection = null;
		try{
			connection = getConnectionPool().getConnection();
			connection.setAutoCommit(false);
			ps = connection.prepareStatement(UPDATE_MOTOR);
			
			ps.setLong(4, motor.getId());			
			ps.setInt(1, motor.getForce());
			ps.setObject(2, motor.getDateOfManufacture());
			ps.setLong(3, motor.getFactory().getId());
			ps.executeUpdate();
			connection.commit();
			
			logger.log(Level.INFO, "Updating motor is successful");
			logger.log(Level.INFO, motor.toString());
		}catch(SQLException e) {
			logger.log(Level.ERROR, "Error updating a motor" + e);
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
	public void delete(Motor motor) {
		PreparedStatement ps = null;
		Connection connection = null;
		try{
			connection = getConnectionPool().getConnection();
			connection.setAutoCommit(false);
			ps = connection.prepareStatement(DELETE_MOTOR);
			
			ps.setLong(1, motor.getId());
			ps.executeUpdate();
			connection.commit();
			
			logger.log(Level.INFO, "Delete motor is successful");			
		}catch(SQLException e) {
			logger.log(Level.ERROR, "Error deleting a motor" + e);
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

