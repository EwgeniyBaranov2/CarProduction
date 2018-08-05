package by.baranov.services;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import by.baranov.dao.jdbc.FactoryDAO;
import by.baranov.dao.jdbc.OwnerDAO;
import by.baranov.dao.jdbc.TransmissionDAO;
import by.baranov.entities.Transmission;

public class TransmissionService {

	private final static String FORMAT_DATE = "dd/MM/yyyy";
	private TransmissionDAO transmissionDAO;
	private FactoryDAO factoryDAO;
	private OwnerDAO ownerDAO;
	
	private List<Transmission> transmissions;
	private Transmission transmission;
	
	public TransmissionService() {	
		this.transmissionDAO = new TransmissionDAO();
		this.factoryDAO = new FactoryDAO();
		this.ownerDAO = new OwnerDAO();
		this.transmission = new Transmission();
		
	}
	
	public List<Transmission> getAllTransmission() {
		transmissions = transmissionDAO.getAll();
		for (Transmission transmission : transmissions) {
			transmission.getFactory().setOwners(ownerDAO.getOwnersByFactoryId(transmission.getFactory().getId()));
		}
		return transmissions;
	}
	
	public Transmission getTransmissionById(Long id) {				
		
		transmission = transmissionDAO.getById(id);
		transmission.getFactory().setOwners(ownerDAO.getOwnersByFactoryId(transmission.getFactory().getId()));
				
		return transmission;
	}

	public void createNewTransmission (Long id, String name, String date_of_manufacture, Long factories_id) {
		
		transmission.setId(id);  
		transmission.setName(name);
		
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern(FORMAT_DATE);
		LocalDate localDate = LocalDate.parse(date_of_manufacture, formatter);
		transmission.setDateOfManufacture(localDate);
		
		transmission.setFactory(factoryDAO.getById(factories_id));
		transmission.getFactory().setOwners(ownerDAO.getOwnersByFactoryId(transmission.getFactory().getId()));
		transmissionDAO.create(transmission);
	}
	
	public void updateTransmission (Long id, String name, String date_of_manufacture, Long factories_id) {

		transmission.setId(id);
		transmission.setName(name);		
		
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern(FORMAT_DATE);
		LocalDate localDate = LocalDate.parse(date_of_manufacture, formatter);
		transmission.setDateOfManufacture(localDate);
		
		transmission.setFactory(factoryDAO.getById(factories_id));
		transmission.getFactory().setOwners(ownerDAO.getOwnersByFactoryId(transmission.getFactory().getId()));
		transmissionDAO.update(transmission);
	}
	
	public void deleteTransmission (Long id) {
		transmission.setId(id);
		transmissionDAO.delete(transmission);
	}
}
