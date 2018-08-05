package by.baranov.services;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import by.baranov.dao.jdbc.AddressDAO;
import by.baranov.dao.jdbc.FactoryDAO;
import by.baranov.dao.jdbc.OwnerDAO;
import by.baranov.entities.Owner;

public class OwnerService {

	private final static String FORMAT_DATE = "dd/MM/yyyy";
	Owner owner;
	List<Owner> owners;
	
	FactoryDAO factoryDAO;	
	AddressDAO addressDAO;
	OwnerDAO ownerDAO;
	
	public OwnerService() {
		this.factoryDAO = new FactoryDAO();
		this.ownerDAO = new OwnerDAO();
		this.addressDAO = new AddressDAO();
		this.owner = new Owner();
	}
	public List<Owner> getOwnersByCityName(String cityName) {
		owners = ownerDAO.getOwnersByCityName(cityName);
		for(Owner owner : owners) {
			owner.setFactories(factoryDAO.getFactoriesByOwnerId(owner.getId()));
		}
		return owners;
	}
	
	public List<Owner> getAllOwner(){
		owners = ownerDAO.getAll();
		for(Owner owner : owners) {
			owner.setFactories(factoryDAO.getFactoriesByOwnerId(owner.getId()));;
		}
		return owners;
	}
	
	public Owner getOwnerById(Long id) {
		owner = ownerDAO.getById(id);
		owner.setFactories(factoryDAO.getFactoriesByOwnerId(owner.getId()));
		return owner;
	}
	
	public void createNewOwner (Long id, String name, String date, Long address_id) {
		owner.setId(id);
		owner.setName(name);
				
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern(FORMAT_DATE);
		LocalDate localDate = LocalDate.parse(date, formatter);
		owner.setDate(localDate);
		
		owner.setAddress(addressDAO.getById(address_id));
		owner.setFactories(factoryDAO.getFactoriesByOwnerId(owner.getId()));
		ownerDAO.create(owner);	
	}
	
	public void updateOwner (Long id, String name, String date, Long address_id) {

		owner.setId(id);
		owner.setName(name);
		
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern(FORMAT_DATE);
		LocalDate localDate = LocalDate.parse(date, formatter);
		owner.setDate(localDate);
		
		owner.setAddress(addressDAO.getById(address_id));
		owner.setFactories(factoryDAO.getFactoriesByOwnerId(owner.getId()));
		ownerDAO.update(owner);
	}
	
	public void deleteOwner (Long id) {
		owner.setId(id);
		ownerDAO.delete(owner);
	}
	
}
