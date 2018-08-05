package by.baranov.services;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import by.baranov.dao.jdbc.CarDAO;
import by.baranov.dao.jdbc.FactoryDAO;
import by.baranov.dao.jdbc.HeadlightDAO;
import by.baranov.dao.jdbc.OwnerDAO;
import by.baranov.entities.Headlight;


public class HeadlightService {

	private final static String FORMAT_DATE = "dd/MM/yyyy";
	private CarDAO carDAO;
	private HeadlightDAO headlightDAO;
	private FactoryDAO factoryDAO;
	private OwnerDAO ownerDAO;
	
	private List<Headlight> headlights;
	private Headlight headlight;
	
	public HeadlightService() {
		this.carDAO = new CarDAO();	
		this.headlightDAO = new HeadlightDAO();
		this.factoryDAO = new FactoryDAO();
		this.ownerDAO = new OwnerDAO();
		this.headlight = new Headlight();
	}
	
	public List<Headlight> getAllHeadlight() {
		headlights = headlightDAO.getAll();
		for (Headlight headlight : headlights) {
			headlight.getFactory().setOwners(ownerDAO.getOwnersByFactoryId(headlight.getFactory().getId()));
		}
		return headlights;
	}
	
	public Headlight getHeadlightById(Long id) {				
		headlight = headlightDAO.getById(id);
		headlight.getFactory().setOwners(ownerDAO.getOwnersByFactoryId(headlight.getFactory().getId()));
		
		return headlight;
	}

	public void createNewHeadlight (Long id, Long cars_id, String name, String date_of_manufacture, Long factories_id) {
		
		headlight.setId(id);  
		headlight.setCar(carDAO.getById(cars_id));
		headlight.setName(name);
		
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern(FORMAT_DATE);
		LocalDate localDate = LocalDate.parse(date_of_manufacture, formatter);
		headlight.setDateOfManufacture(localDate);
		
		headlight.setFactory(factoryDAO.getById(factories_id));
		headlight.getFactory().setOwners(ownerDAO.getOwnersByFactoryId(headlight.getFactory().getId()));
		headlightDAO.create(headlight);
	}
	
	public void updateHeadlight (Long id, Long cars_id, String name, String date_of_manufacture, Long factories_id) {

		headlight.setId(id);
		headlight.setCar(carDAO.getById(cars_id));
		headlight.setName(name);		
		
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern(FORMAT_DATE);
		LocalDate localDate = LocalDate.parse(date_of_manufacture, formatter);
		headlight.setDateOfManufacture(localDate);
		
		headlight.setFactory(factoryDAO.getById(factories_id));
		headlight.getFactory().setOwners(ownerDAO.getOwnersByFactoryId(headlight.getFactory().getId()));
		headlightDAO.update(headlight);
	}
	
	public void deleteHeadlight (Long id) {
		headlight.setId(id);
		headlightDAO.delete(headlight);
	}
	
}
