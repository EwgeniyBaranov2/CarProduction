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

import by.baranov.dao.ICar;
import by.baranov.entities.Car;

public class CarDAO extends AbstractDAO implements ICar{
	
	private static Logger logger = LogManager.getLogger();
	
	private final static String GET_CAR_BY_ID = "SELECT * FROM CARS WHERE ID IN (?)";
	private final static String GET_ALL_CAR = "SELECT ID, MODEL, DATE_OF_MANUFACTURE, MOTORS_ID, BODIES_ID, TRANSMISSIONS_ID, CUSTOMERS_ID, COLORS_ID FROM CARS";
	private final static String CREATE_CAR = "INSERT INTO CARS (ID, MODEL, DATE_OF_MANUFACTURE, MOTORS_ID, BODIES_ID, TRANSMISSIONS_ID, CUSTOMERS_ID, COLORS_ID) VALUES(?, ?, ?, ?, ?, ?, ?, ?)";
	private final static String UPDATE_CAR = "UPDATE CARS SET MODEL=?, DATE_OF_MANUFACTURE=?, MOTORS_ID=?, BODIES_ID=?, TRANSMISSIONS_ID=?, CUSTOMERS_ID=?, COLORS_ID=? WHERE ID IN (?)";
	private final static String DELETE_CAR = "DELETE FROM CARS WHERE ID IN (?)";
	
	private final static String GET_CARS_BY_CITY_NAME_OF_MANUFACTURE_MOTOR = "SELECT * FROM CARS LEFT JOIN MOTORS ON CARS.MOTORS_ID = MOTORS.ID "
			+ "LEFT JOIN FACTORIES ON MOTORS.FACTORIES_ID = FACTORIES.ID "
			+ "LEFT JOIN ADRESSES ON FACTORIES.ADRESSES_ID = ADRESSES.ID "
			+ "LEFT JOIN CITIES ON ADRESSES.CITIES_ID = CITIES.ID WHERE CITIES.NAME LIKE ?";	
	
	@Override
	public List<Car> getCarByCityNameOfManufactureMotor(String cityName) {
		List<Car> listCar = new ArrayList<>();
		
		PreparedStatement ps = null;
		ResultSet rs = null;
		Connection connection = null;
		Car carCityMotor = new Car();
		
		try {			
			connection = getConnectionPool().getConnection();
			ps = connection.prepareStatement(GET_CARS_BY_CITY_NAME_OF_MANUFACTURE_MOTOR);
			ps.setString(1, cityName);
			rs = ps.executeQuery();
			while (rs.next()) {				
				carCityMotor = setDataCarTable(rs);				
				listCar.add(carCityMotor);			
			}						
			logger.log(Level.INFO, listCar.toString());				
				
		} catch (SQLException e) {
			logger.log(Level.ERROR, "Error getting cars by name of city, where the motor is made: " + e);
		} finally {
			closePreparedStatement(ps);
			closeResultSet(rs);
			getConnectionPool().returnConnection(connection);
		}
		return listCar;
	}
	
	@Override
	public List<Car> getAll() {
		List<Car> listCar = new ArrayList<>();
		
		PreparedStatement ps = null;
		ResultSet rs = null;
		Connection connection = null;
		Car carAll = new Car();
		try {
			
			connection = getConnectionPool().getConnection();
			ps = connection.prepareStatement(GET_ALL_CAR);
			rs = ps.executeQuery();
			while (rs.next()) {				
				carAll = setDataCarTable(rs);				
				listCar.add(carAll);			
			}						
			logger.log(Level.INFO, listCar.toString());				
				
		} catch (SQLException e) {
			logger.log(Level.ERROR, "Error getting all car: " + e);
		} finally {
			closePreparedStatement(ps);
			closeResultSet(rs);
			getConnectionPool().returnConnection(connection);
		}
		return listCar;
	}

	private Car setDataCarTable(ResultSet rs) {
		Car carAll = new Car();
		MotorDAO motorDAO = new MotorDAO();
		BodyDAO bodyDAO = new BodyDAO();
		TransmissionDAO transmissionDAO = new TransmissionDAO();
		CustomerDAO customerDAO = new CustomerDAO();
		ColorDAO colorDAO = new ColorDAO();
		
		try {
			carAll.setId(rs.getLong("id"));
			carAll.setModel(rs.getString("model"));
			carAll.setDateOfManufacture(rs.getDate("date_of_manufacture").toLocalDate());
			carAll.setMotor(motorDAO.getById(rs.getLong("motors_id")));
			carAll.setBody(bodyDAO.getById(rs.getLong("bodies_id")));
			carAll.setTransmission(transmissionDAO.getById(rs.getLong("transmissions_id")));
			carAll.setCustomer(customerDAO.getById(rs.getLong("customers_id")));
			carAll.setColor(colorDAO.getById(rs.getLong("colors_id")));
			
		} catch (SQLException e) {
			logger.log(Level.ERROR, "Error getting data from carTable : " + e);
		}
		return carAll;
	}
	
	@Override
	public Car getById(Long id) {
		Car car = new Car();
		
		PreparedStatement ps = null;
		ResultSet rs = null;
		Connection connection = null;
		
		try {
			connection = getConnectionPool().getConnection();

			ps = connection.prepareStatement(GET_CAR_BY_ID);
			ps.setLong(1, id);
			rs = ps.executeQuery();
			rs.next();

			car = setDataCarTable(rs);	

			logger.log(Level.INFO, "Car[Id=" + car.getId() + ", date_of_manufacture=" + car.getDateOfManufacture()
					+ ", motor=" + car.getMotor() + ", body=" + car.getBody() + ", transmission=" + car.getTransmission() 
					+ ", headlights=" + car.getHeadlights() + ", customer=" + car.getCustomer() +", color=" + car.getColor() +"]");

		} catch (SQLException e) {
			logger.log(Level.ERROR, "Error getting a car by Id" + e);
		} finally {
			closePreparedStatement(ps);
			closeResultSet(rs);
			getConnectionPool().returnConnection(connection);
		}
		return car;
	}

	@Override
	public void create(Car car) {
		PreparedStatement ps = null;
		Connection connection = null;
		
		try{
			connection = getConnectionPool().getConnection();
			connection.setAutoCommit(false);
			ps = connection.prepareStatement(CREATE_CAR);
									
			ps.setLong(1, car.getId());
			ps.setString(2, car.getModel());
			ps.setObject(3, car.getDateOfManufacture());
			ps.setLong(4, car.getMotor().getId());
			ps.setLong(5, car.getBody().getId());
			ps.setLong(6, car.getTransmission().getId());
			ps.setLong(7, car.getCustomer().getId());
			ps.setLong(8, car.getColor().getId());
			ps.executeUpdate();
			connection.commit();
			
			logger.log(Level.INFO, "Creating car is successful");
			logger.log(Level.INFO, car.toString());
		}catch(SQLException e) {
			logger.log(Level.ERROR, "Error creating a car" + e);
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
	public void update(Car car) {
		PreparedStatement ps = null;
		Connection connection = null;
		try{
			connection = getConnectionPool().getConnection();
			connection.setAutoCommit(false);
			ps = connection.prepareStatement(UPDATE_CAR);
			
			ps.setLong(8, car.getId());	
			ps.setString(1, car.getModel());
			ps.setObject(2, car.getDateOfManufacture());
			ps.setLong(3, car.getMotor().getId());
			ps.setLong(4, car.getBody().getId());
			ps.setLong(5, car.getTransmission().getId());
			ps.setLong(6, car.getCustomer().getId());
			ps.setLong(7, car.getColor().getId());
			ps.executeUpdate();
			connection.commit();
			
			logger.log(Level.INFO, "Updating car is successful");
			logger.log(Level.INFO, car.toString());
		}catch(SQLException e) {
			logger.log(Level.ERROR, "Error updating a car" + e);
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
	public void delete(Car car) {
		PreparedStatement ps = null;
		Connection connection = null;
		try{
			connection = getConnectionPool().getConnection();
			connection.setAutoCommit(false);
			ps = connection.prepareStatement(DELETE_CAR);
			
			ps.setLong(1, car.getId());
			ps.executeUpdate();
			connection.commit();
			
			logger.log(Level.INFO, "Delete car is successful");			
		}catch(SQLException e) {
			logger.log(Level.ERROR, "Error deleting a car" + e);
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
