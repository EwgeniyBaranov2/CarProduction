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

import by.baranov.dao.ICustomer;
import by.baranov.entities.Customer;

public class CustomerDAO extends AbstractDAO implements ICustomer{
	
private static Logger logger = LogManager.getLogger();
	
	private final static String GET_CUSTOMER_BY_ID = "SELECT * FROM CUSTOMERS WHERE ID IN (?)";
	private final static String GET_ALL_CUSTOMER = "SELECT ID, NAME, DATE, ADRESSES_ID FROM CUSTOMERS";
	private final static String CREATE_CUSTOMER = "INSERT INTO CUSTOMERS (ID, NAME, DATE, ADRESSES_ID) VALUES(?, ?, ?, ?)";
	private final static String UPDATE_CUSTOMER = "UPDATE CUSTOMERS SET NAME=?, DATE=?, ADRESSES_ID=? WHERE ID IN (?)";
	private final static String DELETE_CUSTOMER = "DELETE FROM CUSTOMERS WHERE ID IN (?)";
	private final static String GET_CUSTOMER_BY_CITY_NAME = "SELECT CUSTOMERS.ID, CUSTOMERS.NAME, CUSTOMERS.DATE, CUSTOMERS.ADRESSES_ID"
															+ " FROM CUSTOMERS LEFT JOIN ADRESSES ON CUSTOMERS.ADRESSES_ID = ADRESSES.ID"
															+ "	LEFT JOIN CITIES ON ADRESSES.CITIES_ID = CITIES.ID"
															+ "	WHERE CITIES.NAME LIKE ?";
	@Override
	public List <Customer> getCustomerByCityName (String cityName){
		List<Customer> customerCity = new ArrayList<>();
		PreparedStatement ps = null;
		ResultSet rs = null;
		Connection connection = null;
		Customer ñustomer = new Customer();
		
		try {			
			connection = getConnectionPool().getConnection();
			ps = connection.prepareStatement(GET_CUSTOMER_BY_CITY_NAME);
			ps.setString(1, cityName);
			rs = ps.executeQuery();
			while (rs.next()) {
				ñustomer = setDataCustomerTable(rs);
				customerCity.add(ñustomer);			
			}						
			logger.log(Level.INFO, customerCity.toString());	
		} catch (SQLException e) {
			logger.log(Level.ERROR, "Error getting ñustomer by name of city: " + e);
		} finally {
			closePreparedStatement(ps);
			closeResultSet(rs);
			getConnectionPool().returnConnection(connection);
		}		
		return customerCity;
	}
	
	@Override
	public List<Customer> getAll() {
		List<Customer> listCustomer = new ArrayList<>();
		
		PreparedStatement ps = null;
		ResultSet rs = null;
		Connection connection = null;
		Customer ñustomerAll = new Customer();
		try {
			
			connection = getConnectionPool().getConnection();
			ps = connection.prepareStatement(GET_ALL_CUSTOMER);
			rs = ps.executeQuery();
			while (rs.next()) {
				ñustomerAll = setDataCustomerTable(rs);
				listCustomer.add(ñustomerAll);			
			}						
			logger.log(Level.INFO, listCustomer.toString());				
				
		} catch (SQLException e) {
			logger.log(Level.ERROR, "Error getting all ñustomer: " + e);
		} finally {
			closePreparedStatement(ps);
			closeResultSet(rs);
			getConnectionPool().returnConnection(connection);
		}
		return listCustomer;
	}

	private Customer setDataCustomerTable(ResultSet rs) {
		Customer customerAll = new Customer();
		AddressDAO addressDAO = new AddressDAO();

		try {
			customerAll.setId(rs.getLong("id"));
			customerAll.setName(rs.getString("name"));
			customerAll.setDate(rs.getDate("date").toLocalDate());
			customerAll.setAddress(addressDAO.getById(rs.getLong("adresses_id")));
		} catch (SQLException e) {
			logger.log(Level.ERROR, "Error getting data from customerTable" + e);
		}
		return customerAll;
	}
	
	@Override
	public Customer getById(Long id) {
		Customer customer = new Customer();
				
		PreparedStatement ps = null;
		ResultSet rs = null;
		Connection connection = null;
		
		try {
			connection = getConnectionPool().getConnection();

			ps = connection.prepareStatement(GET_CUSTOMER_BY_ID);
			ps.setLong(1, id);
			rs = ps.executeQuery();
			rs.next();

			customer = setDataCustomerTable(rs);
			
			logger.log(Level.INFO, "Customer [Id=" + customer.getId() + ", name=" + customer.getName() +
					", date=" + customer.getDate() +", adresses=" + customer.getAddress() +"]");

		} catch (SQLException e) {
			logger.log(Level.ERROR, "Error getting a customer by Id" + e);
		} finally {
			closePreparedStatement(ps);
			closeResultSet(rs);
			getConnectionPool().returnConnection(connection);
		}
		return customer;
	}

	@Override
	public void create(Customer customer) {
		PreparedStatement ps = null;
		Connection connection = null;
		
		try{
			connection = getConnectionPool().getConnection();
			connection.setAutoCommit(false);
			ps = connection.prepareStatement(CREATE_CUSTOMER);
									
			ps.setLong(1, customer.getId());
			ps.setString(2, customer.getName());
			ps.setObject(3, customer.getDate());
			ps.setLong(4, customer.getAddress().getId());
			ps.executeUpdate();
			connection.commit();
			
			logger.log(Level.INFO, "Creating customer is successful");
			logger.log(Level.INFO, customer.toString());
		}catch(SQLException e) {
			logger.log(Level.ERROR, "Error creating an customer" + e);
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
	public void update(Customer customer) {
		PreparedStatement ps = null;
		Connection connection = null;
		try{
			connection = getConnectionPool().getConnection();
			connection.setAutoCommit(false);
			ps = connection.prepareStatement(UPDATE_CUSTOMER);
			
			ps.setLong(4, customer.getId());	
			ps.setString(1, customer.getName());
			ps.setObject(2, customer.getDate());
			ps.setLong(3, customer.getAddress().getId());						
			ps.executeUpdate();
			connection.commit();
			
			logger.log(Level.INFO, "Updating customer is successful");
			logger.log(Level.INFO, customer.toString());
		}catch(SQLException e) {
			logger.log(Level.ERROR, "Error updating a customer" + e);
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
	public void delete(Customer customer) {
		PreparedStatement ps = null;
		Connection connection = null;
		try{
			connection = getConnectionPool().getConnection();
			connection.setAutoCommit(false);
			ps = connection.prepareStatement(DELETE_CUSTOMER);
			
			ps.setLong(1, customer.getId());
			ps.executeUpdate();
			connection.commit();
			
			logger.log(Level.INFO, "Delete customer is successful");			
		}catch(SQLException e) {
			logger.log(Level.ERROR, "Error deleting a customer" + e);
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
