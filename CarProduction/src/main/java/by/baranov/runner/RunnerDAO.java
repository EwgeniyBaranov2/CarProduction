package by.baranov.runner;

import java.util.ResourceBundle;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import by.baranov.connectionpool.ConnectionPool;

import by.baranov.services.BodyService;
import by.baranov.services.CarProductionServiceGetAll;
import by.baranov.services.CarService;
import by.baranov.services.CustomerService;
import by.baranov.services.FactoryService;
import by.baranov.services.HeadlightService;
import by.baranov.services.MotorService;
import by.baranov.services.OwnerService;
import by.baranov.services.TransmissionService;

public class RunnerDAO {
	private static Logger logger = LogManager.getLogger();
		
	public static void main(String[] args) {
		
		ConnectionPool connectionPool = ConnectionPool.getConnectionPool();	
		connectionPool.setParametrsConnectionPoolFromProperties();

//*******		
//		
//		CarProductionServiceGetAll carProduction = new CarProductionServiceGetAll();
//		carProduction.getAll();		
//		
////*******
//		
		CarService carService = new CarService();
//		logger.log(Level.INFO, carService.getCarByCityMotor("Minsk"));
//		logger.log(Level.INFO, carService.getCarById(5L));		
//		logger.log(Level.INFO, carService.getAllCar());
//		carService.createNewCar(15L, "BMW", "16/08/2016",3L, 4L, 1L, 2L, 3L);
//		carService.updateCar(15L, "Audi",  "21/04/2014", 1L, 1L, 1L, 1L, 1L);
//		carService.deleteCar(15L);
//		
////*******	
//		
//		HeadlightService headlightService = new HeadlightService();
//		logger.log(Level.INFO, headlightService.getHeadlightById(5L));
//		logger.log(Level.INFO, headlightService.getAllHeadlight());
//		headlightService.createNewHeadlight(15L, 5L, "Type10","21/08/1990", 7L);
//		headlightService.updateHeadlight(15L, 2L, "Type12", "13/12/1989", 1L);
//		headlightService.deleteHeadlight(15L);
//		
////*******
//		
//		TransmissionService transmissionService = new TransmissionService();
//		logger.log(Level.INFO, transmissionService.getTransmissionById(5L));
//		logger.log(Level.INFO, transmissionService.getAllTransmission());
//		transmissionService.createNewTransmission(15L, "manual","21/08/1990", 7L);
//		transmissionService.updateTransmission(15L, "automatic", "13/12/1989", 5L);
//		transmissionService.deleteTransmission(15L);
//		
////*******	
//		
//		BodyService bodyService = new BodyService();
//		logger.log(Level.INFO, bodyService.getBodyById(5L));
//		logger.log(Level.INFO, bodyService.getAllBody());
//		bodyService.createNewBody(15L, "Steel", 1250.45f, 7L);
//		bodyService.updateBody(15L, "Steel", 1000f, 9L);
//		bodyService.deleteBody(15L);
//		
////*******
//		
//		MotorService motorService = new MotorService();
//		logger.log(Level.INFO, motorService.getMotorById(5L));
//		logger.log(Level.INFO, motorService.getAllMotor());
//		motorService.createNewMotor(15L, 145, "06/06/2016", 3L);
//		motorService.updateMotor(15L, 115, "01/02/2015", 4L);
//		motorService.deleteMotor(15L);
//		
////*******
//		
//		FactoryService factoryService = new FactoryService();
//		logger.log(Level.INFO, factoryService.getFactoryByCityName("Minsk"));
//		logger.log(Level.INFO, factoryService.getFactoryById(5L));
//		logger.log(Level.INFO, factoryService.getAllFactory());
//		factoryService.createNewFactory(15L, "Philips", 9L);
//		factoryService.updateCustomer(15L, "Dell", 6L);
//		factoryService.deleteCustomer(15L);
//		
////*******	
//		
//		OwnerService ownerService = new OwnerService();
//		logger.log(Level.INFO, ownerService.getOwnersByCityName("Minsk"));
//		logger.log(Level.INFO, ownerService.getOwnerById(5L));
//		logger.log(Level.INFO, ownerService.getAllOwner());
//		ownerService.createNewOwner(15L, "Golubev Leonid","21/08/1990", 7L);
//		ownerService.updateOwner(15L, "Ivanov Vasiliy", "13/12/1989", 8L);
//		ownerService.deleteOwner(15L);
//	
////*******
//
//		CustomerService customerService = new CustomerService();
//		logger.log(Level.INFO, customerService.getCustomersByCityName("Minsk"));
//		logger.log(Level.INFO, customerService.getCustomerById(5L));
//		logger.log(Level.INFO, customerService.getAllCustomer());
//		customerService.createNewCustomer(15L, "Korotkevich Ivan","21/08/1990", 7L);
//		customerService.updateCustomer(15L, "Ivanov Vasiliy", "13/12/1989", 8L);
//		customerService.deleteCustomer(15L);
//		
////*******				

	}

}
