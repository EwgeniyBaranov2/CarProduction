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

import by.baranov.dao.IBody;
import by.baranov.entities.Body;

public class BodyDAO extends AbstractDAO implements IBody{
	
	private static Logger logger = LogManager.getLogger();
	
	private final static String GET_BODY_BY_ID = "SELECT * FROM BODIES WHERE ID IN (?)";
	private final static String GET_ALL_BODY = "SELECT ID, MATERIAL, CAPACITY, FACTORIES_ID FROM BODIES";
	private final static String CREATE_BODY = "INSERT INTO BODIES (ID, MATERIAL, CAPACITY, FACTORIES_ID) VALUES(?, ?, ?, ?)";
	private final static String UPDATE_BODY = "UPDATE BODIES SET MATERIAL=?, CAPACITY=?, FACTORIES_ID=? WHERE ID IN (?)";
	private final static String DELETE_BODY = "DELETE FROM BODIES WHERE ID IN (?)";
	
	@Override
	public List<Body> getAll() {
		List<Body> listBody = new ArrayList<>();
		
		PreparedStatement ps = null;
		ResultSet rs = null;
		Connection connection = null;
		Body bodyAll = new Body();
		try {
			
			connection = getConnectionPool().getConnection();
			ps = connection.prepareStatement(GET_ALL_BODY);
			rs = ps.executeQuery();
			while (rs.next()) {				
				bodyAll = setDataBodyTable(rs);
				listBody.add(bodyAll);			
			}						
			logger.log(Level.INFO, listBody.toString());				
				
		} catch (SQLException e) {
			logger.log(Level.ERROR, "Error getting all body: " + e);
		} finally {
			closePreparedStatement(ps);
			closeResultSet(rs);
			getConnectionPool().returnConnection(connection);
		}
		return listBody;
	}

	private Body setDataBodyTable(ResultSet rs) {
		Body bodyAll = new Body();
		FactoryDAO factoryDAO = new FactoryDAO();

		try {
			bodyAll.setId(rs.getLong("id"));
			bodyAll.setMaterial(rs.getString("material"));
			bodyAll.setCapacity(rs.getFloat("capacity"));
			bodyAll.setFactory(factoryDAO.getById(rs.getLong("factories_id")));
		} catch (SQLException e) {
			logger.log(Level.ERROR, "Error getting data from bodyTable" + e);
		}
		return bodyAll;
	}
	
	@Override
	public Body getById(Long id) {
					
		PreparedStatement ps = null;
		ResultSet rs = null;
		Connection connection = null;
		Body body = new Body();
		
		try {
			connection = getConnectionPool().getConnection();

			ps = connection.prepareStatement(GET_BODY_BY_ID);
			ps.setLong(1, id);
			rs = ps.executeQuery();
			rs.next();
			body = setDataBodyTable(rs);
			
			logger.log(Level.INFO, "Body[Id=" + body.getId() + ", material=" + body.getMaterial()
					+ ", capacity=" + body.getCapacity() + ", factory=" + body.getFactory() + "]");

		} catch (SQLException e) {
			logger.log(Level.ERROR, "Error getting a body by Id" + e);
		} finally {
			closePreparedStatement(ps);
			closeResultSet(rs);
			getConnectionPool().returnConnection(connection);
		}
		return body;
	}

	@Override
	public void create(Body body) {
		PreparedStatement ps = null;
		Connection connection = null;
		
		try{
			connection = getConnectionPool().getConnection();
			connection.setAutoCommit(false);
			ps = connection.prepareStatement(CREATE_BODY);
									
			ps.setLong(1, body.getId());
			ps.setString(2, body.getMaterial());
			ps.setFloat(3, body.getCapacity());
			ps.setLong(4, body.getFactory().getId());
			
			ps.executeUpdate();
			connection.commit();
			logger.log(Level.INFO, "Creating body is successful");
			logger.log(Level.INFO, body.toString());
		}catch(SQLException e) {
			logger.log(Level.ERROR, "Error creating a body" + e);
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
	public void update(Body body) {
		PreparedStatement ps = null;
		Connection connection = null;
		try{
			connection = getConnectionPool().getConnection();
			connection.setAutoCommit(false);
			ps = connection.prepareStatement(UPDATE_BODY);
			
			ps.setLong(4, body.getId());	
			ps.setString(1, body.getMaterial());
			ps.setFloat(2, body.getCapacity());
			ps.setLong(3, body.getFactory().getId());			
			ps.executeUpdate();
			connection.commit();
			
			logger.log(Level.INFO, "Updating body is successful");
			logger.log(Level.INFO, body.toString());
		}catch(SQLException e) {
			logger.log(Level.ERROR, "Error updating a body" + e);
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
	public void delete(Body body) {
		PreparedStatement ps = null;
		Connection connection = null;
		try{
			connection = getConnectionPool().getConnection();
			connection.setAutoCommit(false);
			ps = connection.prepareStatement(DELETE_BODY);
			
			ps.setLong(1, body.getId());
			ps.executeUpdate();
			connection.commit();
			
			logger.log(Level.INFO, "Delete body is successful");			
		}catch(SQLException e) {
			logger.log(Level.ERROR, "Error deleting a body" + e);
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
