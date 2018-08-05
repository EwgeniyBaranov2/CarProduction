package by.baranov.runner;

import java.io.File;
import java.io.FileNotFoundException;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import by.baranov.connectionpool.ConnectionPool;
import by.baranov.entities.Cars;
import by.baranov.services.CarService;

public class RunnerJAXB {
	private static Logger logger = LogManager.getLogger();
	private final static String PATH_FILE = "dataBase.xml";
	
	public static void main(String[] args) throws FileNotFoundException {

		ConnectionPool connectionPool = ConnectionPool.getConnectionPool();	
		connectionPool.setParametrsConnectionPoolFromProperties();		
			
		File file = new File(PATH_FILE);
		CarService carService = new CarService();
		Cars cars = new Cars();
	
		try {
/*			
			JAXBContext context = JAXBContext.newInstance(Car.class);
			Marshaller marshaller = context.createMarshaller();			
			marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);	
			
			marshaller.marshal(carService.getCarById(3), file);	
			
			logger.log(Level.INFO, "Mapping data from database to XML file is successfull");	
*/		
			JAXBContext context = JAXBContext.newInstance(Cars.class);
			Marshaller marshaller = context.createMarshaller();			
			marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);	
			
			cars.setCars(carService.getAllCar());
			marshaller.marshal(cars, file);
	
			logger.log(Level.INFO, "Mapping data from database to XML file is successfull");			
		} catch (JAXBException e) {
			logger.log(Level.ERROR, "Error writting data to file using JAXB: " + e);
		}		
		
		try {
/*			
			JAXBContext context = JAXBContext.newInstance(Car.class);
			Unmarshaller unmarshaller = context.createUnmarshaller();
			Car car = (Car) unmarshaller.unmarshal(file);
			logger.log(Level.INFO, "Mapping data from XML file to java object is successfull \n" + car);
*/
			
			JAXBContext context = JAXBContext.newInstance(Cars.class);
			Unmarshaller unmarshaller = context.createUnmarshaller();
			
			Cars carsNew = (Cars) unmarshaller.unmarshal(file);
			logger.log(Level.INFO, "Mapping data from XML file to java object is successfull \n" + carsNew);
			
		} catch (JAXBException e) {
			logger.log(Level.ERROR, "Error reading data from file using JAXB: " + e);
		}
	}
}
