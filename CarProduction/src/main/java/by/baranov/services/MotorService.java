package by.baranov.services;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import by.baranov.dao.jdbc.FactoryDAO;
import by.baranov.dao.jdbc.MotorDAO;
import by.baranov.dao.jdbc.OwnerDAO;
import by.baranov.entities.Motor;

public class MotorService {

	private final static String FORMAT_DATE = "dd/MM/yyyy";
	private MotorDAO motorDAO;
	private FactoryDAO factoryDAO;
	private OwnerDAO ownerDAO;
	
	private List<Motor> motors;
	private Motor motor;
	
	public MotorService() {	
		this.motorDAO = new MotorDAO();
		this.factoryDAO = new FactoryDAO();
		this.ownerDAO = new OwnerDAO();
		this.motor = new Motor();
	}
	
	public List<Motor> getAllMotor() {
		motors = motorDAO.getAll();
		for (Motor motor : motors) {
			motor.getFactory().setOwners(ownerDAO.getOwnersByFactoryId(motor.getFactory().getId()));
		}
		return motors;
	}
	
	public Motor getMotorById(Long id) {				
		
		motor = motorDAO.getById(id);
		motor.getFactory().setOwners(ownerDAO.getOwnersByFactoryId(motor.getFactory().getId()));
				
		return motor;
	}

	public void createNewMotor (Long id, int force, String date_of_manufacture, Long factories_id) {
		
		motor.setId(id);  
		motor.setForce(force);
		
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern(FORMAT_DATE);
		LocalDate localDate = LocalDate.parse(date_of_manufacture, formatter);
		motor.setDateOfManufacture(localDate);
		
		motor.setFactory(factoryDAO.getById(factories_id));
		motor.getFactory().setOwners(ownerDAO.getOwnersByFactoryId(motor.getFactory().getId()));
		motorDAO.create(motor);
	}
	
	public void updateMotor (Long id, int force, String date_of_manufacture, Long factories_id) {

		motor.setId(id);  
		motor.setForce(force);
		
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern(FORMAT_DATE);
		LocalDate localDate = LocalDate.parse(date_of_manufacture, formatter);
		motor.setDateOfManufacture(localDate);
		
		motor.setFactory(factoryDAO.getById(factories_id));
		motor.getFactory().setOwners(ownerDAO.getOwnersByFactoryId(motor.getFactory().getId()));	
		motorDAO.update(motor);
	}
	
	public void deleteMotor (Long id) {
		motor.setId(id);
		motorDAO.delete(motor);
	}
}
