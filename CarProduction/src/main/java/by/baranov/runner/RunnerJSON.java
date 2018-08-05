package by.baranov.runner;

import java.io.File;
import java.io.IOException;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import by.baranov.connectionpool.ConnectionPool;
import by.baranov.entities.Cars;
import by.baranov.services.CarService;

public class RunnerJSON {
	private static Logger logger = LogManager.getLogger();
	private final static String PATH_FILE = "dataBase.json";
	
	public static void main(String[] args) {
		
		ConnectionPool connectionPool = ConnectionPool.getConnectionPool();	
		connectionPool.setParametrsConnectionPoolFromProperties();			
				
		CarService carService= new CarService();	
		ObjectMapper objectMapper = new ObjectMapper();	
		File file = new File(PATH_FILE);	
		Cars cars = new Cars();

		try {			
			objectMapper.registerModule(new JavaTimeModule());

			objectMapper.configure(SerializationFeature.INDENT_OUTPUT, true);
			
			cars.setCars(carService.getAllCar());			
			objectMapper.writeValue(file, cars);
			
			logger.log(Level.INFO, "Mapping data from database to JSON file is successfull");			
		} catch (JsonGenerationException e) {
			logger.log(Level.ERROR, "Error mapping to JSON file: " + e);
		} catch (JsonMappingException e) {
			logger.log(Level.ERROR, "Error mapping to JSON file: " + e);
		} catch (IOException e) {
			logger.log(Level.ERROR, "Error I/O to JSON file: " + e);
		}
		
			
		try {
			Cars carsNew = objectMapper.readValue(file, Cars.class);
			
			logger.log(Level.INFO, "Mapping data from JSON file to java object is successfull \n" + carsNew);
		} catch (JsonParseException e) {
			logger.log(Level.ERROR, "Error parsing from JSON file: " + e);
		} catch (JsonMappingException e) {
			logger.log(Level.ERROR, "Error mapping from JSON file: " + e);
		} catch (IOException e) {
			logger.log(Level.ERROR, "Error I/O from JSON file: " + e);
		}
		
	}
}
