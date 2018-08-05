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

import by.baranov.dao.IAddress;
import by.baranov.entities.Address;

public class AddressDAO extends AbstractDAO implements IAddress {

	private static Logger logger = LogManager.getLogger();

	private final static String GET_ADDRESS_BY_ID = "SELECT * FROM ADRESSES WHERE ID=?";
	private final static String GET_ALL_ADDRESS_ID = "SELECT ID, CITIES_ID, STREET_ID, HOUSE_ID FROM ADRESSES";
	private final static String CREATE_ADDRESS_ID = "INSERT INTO ADRESSES (ID, CITIES_ID, STREET_ID, HOUSE_ID) VALUES(?, ?, ?, ?)";
	private final static String UPDATE_ADDRESS_ID = "UPDATE ADRESSES SET CITIES_ID=?, STREET_ID=?, HOUSE_ID=? WHERE ID=?";
	private final static String DELETE_ADDRESS_ID = "DELETE FROM ADRESSES WHERE ID=?";

	@Override
	public List<Address> getAll() {

		List<Address> listAddress = new ArrayList<>();		
				
		Connection connection = null;
		PreparedStatement ps = null;
		ResultSet rs = null;		
		Address addressAll = new Address();
		
		try {

			connection = getConnectionPool().getConnection();
			ps = connection.prepareStatement(GET_ALL_ADDRESS_ID);
			rs = ps.executeQuery();
			while (rs.next()) {
				addressAll = setDataAddressTable(rs);
				listAddress.add(addressAll);
			}
			logger.log(Level.INFO, listAddress.toString());

		} catch (SQLException e) {
			logger.log(Level.ERROR, "Error getting all address: " + e);
		} finally {
			closePreparedStatement(ps);
			closeResultSet(rs);
			getConnectionPool().returnConnection(connection);
		}
		return listAddress;
	}

	private Address setDataAddressTable(ResultSet rs) {
		Address address = new Address();
		CityDAO cityDAO = new CityDAO();
		StreetDAO streetDAO = new StreetDAO();
		HouseDAO houseDAO = new HouseDAO();

		try {
			address.setId(rs.getLong("id"));
			address.setCity(cityDAO.getById(rs.getLong("cities_id")));
			address.setStreet(streetDAO.getById(rs.getLong("street_id")));
			address.setHouse(houseDAO.getById(rs.getLong("house_id")));
		} catch (SQLException e) {
			logger.log(Level.ERROR, "Error getting data from addressTable" + e);
		}
		return address;
	}

	@Override
	public Address getById(Long id) {

		PreparedStatement ps = null;
		ResultSet rs = null;
		Connection connection = null;
		Address address = new Address();

		try {
			connection = getConnectionPool().getConnection();
			ps = connection.prepareStatement(GET_ADDRESS_BY_ID);
			ps.setLong(1, id);
			rs = ps.executeQuery();
			rs.next();

			address = setDataAddressTable(rs);
			logger.log(Level.INFO, "Address[addressId=" + address.getId() + ", city=" + address.getCity() + ", street="
					+ address.getStreet() + ", house=" + address.getHouse() + "]");
		} catch (SQLException e) {
			logger.log(Level.ERROR, "Error getting an address by Id" + e);
		} finally {
			closePreparedStatement(ps);
			closeResultSet(rs);
			getConnectionPool().returnConnection(connection);
		}
		return address;
	}

	@Override
	public void create(Address address) {
		PreparedStatement ps = null;
		Connection connection = null;

		try {
			connection = getConnectionPool().getConnection();
			connection.setAutoCommit(false);
			ps = connection.prepareStatement(CREATE_ADDRESS_ID);
			
			ps.setLong(1, address.getId());
			ps.setLong(2, address.getCity().getId());
			ps.setLong(3, address.getStreet().getId());
			ps.setLong(4, address.getHouse().getId());
			ps.executeUpdate();
			connection.commit();
			
			logger.log(Level.INFO, "Creating addressId is successful");
			logger.log(Level.INFO, address.toString());			
		} catch (SQLException e) {
			logger.log(Level.ERROR, "Error creating an addressId : " + e);
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
	public void update(Address address) {
		PreparedStatement ps = null;
		Connection connection = null;
		try {
			connection = getConnectionPool().getConnection();
			connection.setAutoCommit(false);
			ps = connection.prepareStatement(UPDATE_ADDRESS_ID);
			
			ps.setLong(4, address.getId());
			ps.setLong(1, address.getCity().getId());
			ps.setLong(2, address.getStreet().getId());
			ps.setLong(3, address.getHouse().getId());
			ps.executeUpdate();
			connection.commit();
			
			logger.log(Level.INFO, "Updating addressId is successful");
			logger.log(Level.INFO, address.toString());
		} catch (SQLException e) {
			logger.log(Level.ERROR, "Error updating an addressId" + e);
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
	public void delete(Address address) {
		PreparedStatement ps = null;
		Connection connection = null;
		try {
			connection = getConnectionPool().getConnection();
			connection.setAutoCommit(false);
			ps = connection.prepareStatement(DELETE_ADDRESS_ID);

			ps.setLong(1, address.getId());
			ps.executeUpdate();
			connection.commit();
			
			logger.log(Level.INFO, "Delete addressId is successful");
		} catch (SQLException e) {
			logger.log(Level.ERROR, "Error deleting an addressId" + e);
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
