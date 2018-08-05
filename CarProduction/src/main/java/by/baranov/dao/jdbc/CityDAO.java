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

import by.baranov.dao.ICity;
import by.baranov.entities.City;

public class CityDAO extends AbstractDAO implements ICity {

	private static Logger logger = LogManager.getLogger();

	private final static String GET_CITY_BY_ID = "SELECT * FROM CITIES WHERE ID IN (?)";
	private final static String GET_ALL_CITY = "SELECT ID, NAME FROM CITIES";
	private final static String CREATE_CITY = "INSERT INTO CITIES (ID, NAME) VALUES(?, ?)";
	private final static String UPDATE_CITY = "UPDATE CITIES SET NAME=? WHERE ID IN (?)";
	private final static String DELETE_CITY = "DELETE FROM CITIES WHERE ID IN (?)";

	@Override
	public List<City> getAll() {
		List<City> listCity = new ArrayList<>();

		PreparedStatement ps = null;
		ResultSet rs = null;
		Connection connection = null;
		City cityAll = new City();

		try {
			connection = getConnectionPool().getConnection();
			ps = connection.prepareStatement(GET_ALL_CITY);
			rs = ps.executeQuery();
			while (rs.next()) {

				cityAll = setDataCityTable(rs);
				listCity.add(cityAll);
			}
			logger.log(Level.INFO, listCity.toString());

		} catch (SQLException e) {
			logger.log(Level.ERROR, "Error getting all city: " + e);
		} finally {
			closePreparedStatement(ps);
			closeResultSet(rs);
			getConnectionPool().returnConnection(connection);
		}
		return listCity;
	}

	private City setDataCityTable(ResultSet rs) {
		City cityAll = new City();

		try {
			cityAll.setId(rs.getLong("id"));
			cityAll.setName(rs.getString("name"));
		} catch (SQLException e) {
			logger.log(Level.ERROR, "Error getting data from cityTable" + e);
		}
		return cityAll;
	}

	@Override
	public City getById(Long id) {
		City city = new City();

		PreparedStatement ps = null;
		ResultSet rs = null;
		Connection connection = null;

		try {
			connection = getConnectionPool().getConnection();
			ps = connection.prepareStatement(GET_CITY_BY_ID);
			ps.setLong(1, id);
			rs = ps.executeQuery();
			rs.next();

			city = setDataCityTable(rs);

			logger.log(Level.INFO, "City [Id=" + city.getId() + ", name=" + city.getName() + "]");

		} catch (SQLException e) {
			logger.log(Level.ERROR, "Error getting a city by Id" + e);
		} finally {
			closePreparedStatement(ps);
			closeResultSet(rs);
			getConnectionPool().returnConnection(connection);
		}
		return city;
	}

	@Override
	public void create(City city) {
		PreparedStatement ps = null;
		Connection connection = null;

		try {
			connection = getConnectionPool().getConnection();
			connection.setAutoCommit(false);
			ps = connection.prepareStatement(CREATE_CITY);

			ps.setLong(1, city.getId());
			ps.setString(2, city.getName());

			ps.executeUpdate();
			connection.commit();
			logger.log(Level.INFO, "Creating city is successful");
			logger.log(Level.INFO, city.toString());
		} catch (SQLException e) {
			logger.log(Level.ERROR, "Error creating a city" + e);
			try {
				connection.rollback();
			} catch (SQLException e1) {
				logger.log(Level.ERROR, "Attempt to cancel query failed : " + e1);	
			}
		} finally {
			closePreparedStatement(ps);
			getConnectionPool().returnConnection(connection);
		}

	}

	@Override
	public void update(City city) {
		PreparedStatement ps = null;
		Connection connection = null;
		try {
			connection = getConnectionPool().getConnection();
			connection.setAutoCommit(false);
			ps = connection.prepareStatement(UPDATE_CITY);

			ps.setLong(2, city.getId());
			ps.setString(1, city.getName());

			ps.executeUpdate();
			connection.commit();

			logger.log(Level.INFO, "Updating city is successful");
			logger.log(Level.INFO, city.toString());
		} catch (SQLException e) {
			logger.log(Level.ERROR, "Error updating a city" + e);
			try {
				connection.rollback();
			} catch (SQLException e1) {
				logger.log(Level.ERROR, "Attempt to cancel query failed : " + e1);	
			}
		} finally {
			closePreparedStatement(ps);
			getConnectionPool().returnConnection(connection);
		}
	}

	@Override
	public void delete(City city) {
		PreparedStatement ps = null;
		Connection connection = null;
		try {
			connection = getConnectionPool().getConnection();
			connection.setAutoCommit(false);
			ps = connection.prepareStatement(DELETE_CITY);

			ps.setLong(1, city.getId());
			ps.executeUpdate();
			connection.commit();

			logger.log(Level.INFO, "Delete city is successful");
		} catch (SQLException e) {
			logger.log(Level.ERROR, "Error deleting a city" + e);
			try {
				connection.rollback();
			} catch (SQLException e1) {
				logger.log(Level.ERROR, "Attempt to cancel query failed : " + e1);	
			}
		} finally {
			closePreparedStatement(ps);
			getConnectionPool().returnConnection(connection);
		}

	}
}
