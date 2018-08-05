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

import by.baranov.dao.ITransmission;
import by.baranov.entities.Transmission;

public class TransmissionDAO extends AbstractDAO implements ITransmission{
	
	private static Logger logger = LogManager.getLogger();
	
	private final static String GET_TRANSMISSION_BY_ID = "SELECT * FROM TRANSMISSIONS WHERE ID IN (?)";
	private final static String GET_ALL_TRANSMISSION = "SELECT ID, NAME, DATE_OF_MANUFACTURE, FACTORIES_ID FROM TRANSMISSIONS";
	private final static String CREATE_TRANSMISSION = "INSERT INTO TRANSMISSIONS (ID, NAME, DATE_OF_MANUFACTURE, FACTORIES_ID) VALUES(?, ?, ?, ?)";
	private final static String UPDATE_TRANSMISSION = "UPDATE TRANSMISSIONS SET NAME=?, DATE_OF_MANUFACTURE=?, FACTORIES_ID=? WHERE ID IN (?)";
	private final static String DELETE_TRANSMISSION = "DELETE FROM TRANSMISSIONS WHERE ID IN (?)";
	
	@Override
	public List<Transmission> getAll() {
		List<Transmission> listTransmission = new ArrayList<>();
		
		PreparedStatement st = null;
		ResultSet rs = null;
		Connection connection = null;
		Transmission transmissionAll = new Transmission();
		
		try {			
			connection = getConnectionPool().getConnection();
			st = connection.prepareStatement(GET_ALL_TRANSMISSION);
			rs = st.executeQuery();
			while (rs.next()) {
				transmissionAll = setDataTransmissionTable(rs);
				listTransmission.add(transmissionAll);			
			}						
			logger.log(Level.INFO, listTransmission.toString());				
				
		} catch (SQLException e) {
			logger.log(Level.ERROR, "Error getting all transmission: " + e);
		} finally {
			closePreparedStatement(st);
			closeResultSet(rs);
			getConnectionPool().returnConnection(connection);
		}
		return listTransmission;
	}

	private Transmission setDataTransmissionTable(ResultSet rs) {
		Transmission transmissionAll = new Transmission();
		FactoryDAO factoryDAO = new FactoryDAO();
		
		try {
			transmissionAll.setId(rs.getLong("id"));
			transmissionAll.setName(rs.getString("name"));
			transmissionAll.setDateOfManufacture(rs.getDate("date_of_manufacture").toLocalDate());
			transmissionAll.setFactory(factoryDAO.getById(rs.getLong("factories_id")));	
		} catch (SQLException e) {
			logger.log(Level.ERROR, "Error getting data from transmissionTable" + e);
		}
		return transmissionAll;
	}
	
	@Override
	public Transmission getById(Long id) {
		Transmission transmission = new Transmission();
		
		PreparedStatement ps = null;
		ResultSet rs = null;
		Connection connection = null;
		
		try {
			connection = getConnectionPool().getConnection();

			ps = connection.prepareStatement(GET_TRANSMISSION_BY_ID);
			ps.setLong(1, id);
			rs = ps.executeQuery();
			rs.next();

			transmission = setDataTransmissionTable(rs);			

			logger.log(Level.INFO, "Transmission[Id=" + transmission.getId() + ", name=" + transmission.getName() + 
					", date_of_manufacture=" + transmission.getDateOfManufacture() + ", factory=" + transmission.getFactory() +"]");
		} catch (SQLException e) {
			logger.log(Level.ERROR, "Error getting a transmission by Id" + e);
		} finally {
			closePreparedStatement(ps);
			closeResultSet(rs);
			getConnectionPool().returnConnection(connection);
		}
		return transmission;
	}

	@Override
	public void create(Transmission transmission) {
		PreparedStatement ps = null;
		Connection connection = null;
		
		try{
			connection = getConnectionPool().getConnection();
			connection.setAutoCommit(false);
			ps = connection.prepareStatement(CREATE_TRANSMISSION);
									
			ps.setLong(1, transmission.getId());			
			ps.setString(2, transmission.getName());
			ps.setObject(3, transmission.getDateOfManufacture());
			ps.setLong(4, transmission.getFactory().getId());
			ps.executeUpdate();
			connection.commit();
			
			logger.log(Level.INFO, "Creating transmission is successful");
			logger.log(Level.INFO, transmission.toString());
		}catch(SQLException e) {
			logger.log(Level.ERROR, "Error creating a transmission" + e);
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
	public void update(Transmission transmission) {
		PreparedStatement ps = null;
		Connection connection = null;
		try{
			connection = getConnectionPool().getConnection();
			connection.setAutoCommit(false);
			ps = connection.prepareStatement(UPDATE_TRANSMISSION);
			
			ps.setLong(4, transmission.getId());			
			ps.setString(1, transmission.getName());
			ps.setObject(2, transmission.getDateOfManufacture());
			ps.setLong(3, transmission.getFactory().getId());	
			ps.executeUpdate();
			connection.commit();
			
			logger.log(Level.INFO, "Updating transmission is successful");
			logger.log(Level.INFO, transmission.toString());
		}catch(SQLException e) {
			logger.log(Level.ERROR, "Error updating a transmission" + e);
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
	public void delete(Transmission transmission) {
		PreparedStatement ps = null;
		Connection connection = null;
		try{
			connection = getConnectionPool().getConnection();
			connection.setAutoCommit(false);
			ps = connection.prepareStatement(DELETE_TRANSMISSION);
			
			ps.setLong(1, transmission.getId());
			ps.executeUpdate();
			connection.commit();
			
			logger.log(Level.INFO, "Delete transmission is successful");			
		}catch(SQLException e) {
			logger.log(Level.ERROR, "Error deleting a transmission" + e);
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

