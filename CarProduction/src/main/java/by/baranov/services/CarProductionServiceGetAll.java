package by.baranov.services;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import by.baranov.dao.jdbc.CityDAO;
import by.baranov.dao.jdbc.ColorDAO;
import by.baranov.dao.jdbc.HouseDAO;
import by.baranov.dao.jdbc.StreetDAO;


public class CarProductionServiceGetAll {
	
	private static Logger logger = LogManager.getLogger();
	
	private BodyService body;
	private CarService car;
	private CityDAO city;
	private ColorDAO color;
	private CustomerService customer;
	private FactoryService factory;
	private HeadlightService headlight;
	private HouseDAO house;
	private MotorService motor;
	private OwnerService owner;
	private StreetDAO streetDAO;
	private TransmissionService transmission;
		
	public CarProductionServiceGetAll() {
		
		this.body = new BodyService();
		this.car = new CarService();
		this.city = new CityDAO();
		this.color = new ColorDAO();
		this.customer = new CustomerService();
		this.factory = new FactoryService();
		this.headlight = new HeadlightService();
		this.house = new HouseDAO();
		this.motor = new MotorService();
		this.owner = new OwnerService();
		this.streetDAO = new StreetDAO();
		this.transmission = new TransmissionService();
	}
	
	public void getAll() {
		logger.log(Level.INFO, "CarPoduction = " + "\n" + car.getAllCar() + "\n" + motor.getAllMotor() + "\n" +	transmission.getAllTransmission() 
				+ "\n" + body.getAllBody() + "\n" +	headlight.getAllHeadlight()+ "\n" + color.getAll()+ 
				"\n" +	customer.getAllCustomer() + "\n" +	factory.getAllFactory() + "\n" + owner.getAllOwner()+ 
				"\n" + city.getAll() +	"\n" +	streetDAO.getAll() + "\n" + house.getAll());
		
		
	}
	
}
