package by.baranov.services;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import by.baranov.dao.jdbc.BodyDAO;
import by.baranov.dao.jdbc.CarDAO;
import by.baranov.dao.jdbc.ColorDAO;
import by.baranov.dao.jdbc.CustomerDAO;
import by.baranov.dao.jdbc.HeadlightDAO;
import by.baranov.dao.jdbc.MotorDAO;
import by.baranov.dao.jdbc.OwnerDAO;
import by.baranov.dao.jdbc.TransmissionDAO;
import by.baranov.entities.Car;
import by.baranov.entities.Headlight;

public class CarService {	
	
	private final static String FORMAT_DATE = "dd/MM/yyyy";
	private CarDAO carDAO;
	private MotorDAO motorDAO;
	private BodyDAO bodyDAO;
	private TransmissionDAO transmissionDAO;
	private CustomerDAO customerDAO;
	private ColorDAO colorDAO; 
	private HeadlightDAO headlightDAO;
	private OwnerDAO ownerDAO;
	
	private List<Car> cars;
	private Car car;	
	
	public CarService() {
		this.carDAO = new CarDAO();	
		this.motorDAO = new MotorDAO();
		this.bodyDAO = new BodyDAO();
		this.transmissionDAO = new TransmissionDAO();
		this.customerDAO = new CustomerDAO();
		this.colorDAO = new ColorDAO();
		this.headlightDAO = new HeadlightDAO();		
		this.ownerDAO = new OwnerDAO();
		this.car = new Car();
	}
	
	public List<Car> getCarByCityMotor(String cityName) {
		cars = carDAO.getCarByCityNameOfManufactureMotor(cityName);
		cars.stream().forEach(car -> car.setHeadlights(headlightDAO.getHeadlightsByCarId(car.getId())));
		cars.stream().forEach(car -> car.getTransmission().getFactory().setOwners(ownerDAO.getOwnersByFactoryId(car.getTransmission().getFactory().getId())));
		cars.stream().forEach(car -> car.getBody().getFactory().setOwners(ownerDAO.getOwnersByFactoryId(car.getBody().getFactory().getId())));
		cars.stream().forEach(car -> car.getMotor().getFactory().setOwners(ownerDAO.getOwnersByFactoryId(car.getMotor().getFactory().getId())));
		
		cars.stream().forEach(car -> car.getHeadlights().stream().forEach(headlight -> headlight.getFactory().setOwners(ownerDAO.getOwnersByFactoryId(headlight.getFactory().getId()))));
		
//		for(Car car : cars) {
//			car.setHeadlights(headlightDAO.getHeadlightsByCarId(car.getId()));
//			car.getTransmission().getFactory().setOwners(ownerDAO.getOwnersByFactoryId(car.getTransmission().getFactory().getId()));
//			car.getBody().getFactory().setOwners(ownerDAO.getOwnersByFactoryId(car.getBody().getFactory().getId()));
//			car.getMotor().getFactory().setOwners(ownerDAO.getOwnersByFactoryId(car.getMotor().getFactory().getId()));
//			
//			for(Headlight headlight : car.getHeadlights()) {
//				headlight.getFactory().setOwners(ownerDAO.getOwnersByFactoryId(headlight.getFactory().getId()));
//			}
//			
//		}			
		return cars;
	}
	
	public List<Car> getAllCar() {
		cars = carDAO.getAll();
		
		cars.stream().forEach(car -> car.setHeadlights(headlightDAO.getHeadlightsByCarId(car.getId())));
		cars.stream().forEach(car -> car.getTransmission().getFactory().setOwners(ownerDAO.getOwnersByFactoryId(car.getTransmission().getFactory().getId())));
		cars.stream().forEach(car -> car.getBody().getFactory().setOwners(ownerDAO.getOwnersByFactoryId(car.getBody().getFactory().getId())));
		cars.stream().forEach(car -> car.getMotor().getFactory().setOwners(ownerDAO.getOwnersByFactoryId(car.getMotor().getFactory().getId())));
		
		cars.stream().forEach(car -> car.getHeadlights().stream().forEach(headlight -> headlight.getFactory().setOwners(ownerDAO.getOwnersByFactoryId(headlight.getFactory().getId()))));
		
//		for(Car car : cars) {
//			car.setHeadlights(headlightDAO.getHeadlightsByCarId(car.getId()));			
//			car.getTransmission().getFactory().setOwners(ownerDAO.getOwnersByFactoryId(car.getTransmission().getFactory().getId()));
//			car.getBody().getFactory().setOwners(ownerDAO.getOwnersByFactoryId(car.getBody().getFactory().getId()));
//			car.getMotor().getFactory().setOwners(ownerDAO.getOwnersByFactoryId(car.getMotor().getFactory().getId()));
//			
//			for(Headlight headlight : car.getHeadlights()) {
//				headlight.getFactory().setOwners(ownerDAO.getOwnersByFactoryId(headlight.getFactory().getId()));
//			}
//		}
		return cars;
	}
	
	public Car getCarById(Long id) {				
		car = carDAO.getById(id);
		car.setHeadlights(headlightDAO.getHeadlightsByCarId(id));
		car.getTransmission().getFactory().setOwners(ownerDAO.getOwnersByFactoryId(car.getTransmission().getFactory().getId()));
		car.getBody().getFactory().setOwners(ownerDAO.getOwnersByFactoryId(car.getBody().getFactory().getId()));
		car.getMotor().getFactory().setOwners(ownerDAO.getOwnersByFactoryId(car.getMotor().getFactory().getId()));
		
		car.getHeadlights().stream().forEach(headlight -> headlight.getFactory().setOwners(ownerDAO.getOwnersByFactoryId(headlight.getFactory().getId())));
//		for(Headlight headlight : car.getHeadlights()) {
//			headlight.getFactory().setOwners(ownerDAO.getOwnersByFactoryId(headlight.getFactory().getId()));
//		}
		return car;
	}

	public void createNewCar (Long id, String model, String date, Long motorId, 
								Long bodyId, Long transmissionId, Long customerId, Long colorId) {
		
		car.setId(id);  
		car.setModel(model);
		
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern(FORMAT_DATE);
		LocalDate localDate = LocalDate.parse(date, formatter);
	
		car.setDateOfManufacture(localDate);
		car.setMotor(motorDAO.getById(motorId));
		car.setBody(bodyDAO.getById(bodyId));
		car.setTransmission(transmissionDAO.getById(transmissionId));
		car.setCustomer(customerDAO.getById(customerId));
		car.setColor(colorDAO.getById(colorId));
		car.setHeadlights(headlightDAO.getHeadlightsByCarId(car.getId()));
		car.getTransmission().getFactory().setOwners(ownerDAO.getOwnersByFactoryId(car.getTransmission().getFactory().getId()));
		car.getBody().getFactory().setOwners(ownerDAO.getOwnersByFactoryId(car.getBody().getFactory().getId()));
		car.getMotor().getFactory().setOwners(ownerDAO.getOwnersByFactoryId(car.getMotor().getFactory().getId()));
		
		car.getHeadlights().stream().forEach(headlight -> headlight.getFactory().setOwners(ownerDAO.getOwnersByFactoryId(headlight.getFactory().getId())));
		
//		for(Headlight headlight : car.getHeadlights()) {
//			headlight.getFactory().setOwners(ownerDAO.getOwnersByFactoryId(headlight.getFactory().getId()));
//		}
		carDAO.create(car);
	}
	
	public void updateCar (Long id, String model, String date, Long motorId, 
							Long bodyId, Long transmissionId, Long customerId, Long colorId) {

		car.setId(id);
		car.setModel(model);		
		
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern(FORMAT_DATE);
		LocalDate localDate = LocalDate.parse(date, formatter);
		
		car.setDateOfManufacture(localDate);
		car.setMotor(motorDAO.getById(motorId));
		car.setBody(bodyDAO.getById(bodyId));
		car.setTransmission(transmissionDAO.getById(transmissionId));
		car.setCustomer(customerDAO.getById(customerId));
		car.setColor(colorDAO.getById(colorId));
		car.setHeadlights(headlightDAO.getHeadlightsByCarId(car.getId()));
		car.getTransmission().getFactory().setOwners(ownerDAO.getOwnersByFactoryId(car.getTransmission().getFactory().getId()));
		car.getBody().getFactory().setOwners(ownerDAO.getOwnersByFactoryId(car.getBody().getFactory().getId()));
		car.getMotor().getFactory().setOwners(ownerDAO.getOwnersByFactoryId(car.getMotor().getFactory().getId()));
		
		car.getHeadlights().stream().forEach(headlight -> headlight.getFactory().setOwners(ownerDAO.getOwnersByFactoryId(headlight.getFactory().getId())));

//		for(Headlight headlight : car.getHeadlights()) {
//			headlight.getFactory().setOwners(ownerDAO.getOwnersByFactoryId(headlight.getFactory().getId()));
//		}
		carDAO.update(car);
	}
	
	public void deleteCar (Long id) {
		car.setId(id);
		carDAO.delete(car);
	}
}


