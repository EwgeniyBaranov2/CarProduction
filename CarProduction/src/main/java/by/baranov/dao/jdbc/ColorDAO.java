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

import by.baranov.dao.IColor;
import by.baranov.entities.Color;

public class ColorDAO extends AbstractDAO implements IColor{
	
private static Logger logger = LogManager.getLogger();
	
	private final static String GET_COLOR_BY_ID = "SELECT * FROM COLORS WHERE ID IN (?)";
	private final static String GET_ALL_COLOR = "SELECT ID, NAME FROM COLORS";
	private final static String CREATE_COLOR = "INSERT INTO COLORS (ID, NAME) VALUES(?, ?)";
	private final static String UPDATE_COLOR = "UPDATE COLORS SET NAME=? WHERE ID=?";
	private final static String DELETE_COLOR = "DELETE FROM COLORS WHERE ID=?";
	
	@Override
	public List<Color> getAll() {
		List<Color> listColor = new ArrayList<>();
		
		PreparedStatement ps = null;
		ResultSet rs = null;
		Connection connection = null;
		Color colorAll = new Color();
		try {
			
			connection = getConnectionPool().getConnection();
			ps = connection.prepareStatement(GET_ALL_COLOR);
			rs = ps.executeQuery();
			while (rs.next()) {
				colorAll = setDataColorTable(rs);
				listColor.add(colorAll);			
			}						
			logger.log(Level.INFO, listColor.toString());				
				
		} catch (SQLException e) {
			logger.log(Level.ERROR, "Error getting all color: " + e);
		} finally {
			closePreparedStatement(ps);
			closeResultSet(rs);
			getConnectionPool().returnConnection(connection);
		}
		return listColor;
	}

	private Color setDataColorTable(ResultSet rs) {
		Color colorAll = new Color();

		try {
			colorAll.setId(rs.getLong("id"));
			colorAll.setName(rs.getString("name"));
		} catch (SQLException e) {
			logger.log(Level.ERROR, "Error getting data from colorTable" + e);
		}
		return colorAll;
	}
	
	@Override
	public Color getById(Long id) {
		Color color = new Color();
		
		PreparedStatement ps = null;
		ResultSet rs = null;
		Connection connection = null;
		
		try {
			connection = getConnectionPool().getConnection();

			ps = connection.prepareStatement(GET_COLOR_BY_ID);
			ps.setLong(1, id);
			rs = ps.executeQuery();
			rs.next();

			color = setDataColorTable(rs);
			
			logger.log(Level.INFO, "Color [Id=" + color.getId() + ", name=" + color.getName() +"]");

		} catch (SQLException e) {
			logger.log(Level.ERROR, "Error getting a color by Id" + e);
		} finally {
			closePreparedStatement(ps);
			closeResultSet(rs);
			getConnectionPool().returnConnection(connection);
		}
		return color;
	}

	@Override
	public void create(Color color) {
		PreparedStatement ps = null;
		Connection connection = null;
		
		try{
			connection = getConnectionPool().getConnection();
			connection.setAutoCommit(false);
			ps = connection.prepareStatement(CREATE_COLOR);
									
			ps.setLong(1, color.getId());
			ps.setString(2, color.getName());	
			ps.executeUpdate();
			connection.commit();
			
			logger.log(Level.INFO, "Creating color is successful");
			logger.log(Level.INFO, color.toString());
		}catch(SQLException e) {
			logger.log(Level.ERROR, "Error creating a color" + e);
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
	public void update(Color color) {
		PreparedStatement ps = null;
		Connection connection = null;
		try{
			connection = getConnectionPool().getConnection();
			connection.setAutoCommit(false);
			ps = connection.prepareStatement(UPDATE_COLOR);
			
			ps.setLong(2, color.getId());	
			ps.setString(1, color.getName());
			ps.executeUpdate();
			connection.commit();
			
			logger.log(Level.INFO, "Updating color is successful");
			logger.log(Level.INFO, color.toString());
		}catch(SQLException e) {
			logger.log(Level.ERROR, "Error updating a color" + e);
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
	public void delete(Color color) {
		PreparedStatement ps = null;
		Connection connection = null;
		try{
			connection = getConnectionPool().getConnection();
			connection.setAutoCommit(false);
			ps = connection.prepareStatement(DELETE_COLOR);
			
			ps.setLong(1, color.getId());
			ps.executeUpdate();
			connection.commit();
			
			logger.log(Level.INFO, "Delete color is successful");			
		}catch(SQLException e) {
			logger.log(Level.ERROR, "Error deleting a color" + e);
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
