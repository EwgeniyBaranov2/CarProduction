package by.baranov.services;

import java.util.List;

import by.baranov.dao.jdbc.AddressDAO;
import by.baranov.dao.jdbc.FactoryDAO;
import by.baranov.dao.jdbc.OwnerDAO;
import by.baranov.entities.Factory;

public class FactoryService {
	Factory factory;
	List<Factory> factories;
	
	FactoryDAO factoryDAO;	
	AddressDAO addressDAO;
	OwnerDAO ownerDAO;
	
	public FactoryService() {
		this.factoryDAO = new FactoryDAO();
		this.ownerDAO = new OwnerDAO();
		this.addressDAO = new AddressDAO();	
		this.factory = new Factory();
	}
	public List<Factory> getFactoryByCityName(String cityName) {
		factories = factoryDAO.getFactoryByCityName(cityName);
		for(Factory factory : factories) {
			factory.setOwners(ownerDAO.getOwnersByFactoryId(factory.getId()));
		}
		return factories;
	}
	
	public List<Factory> getAllFactory(){
		factories = factoryDAO.getAll();
		for(Factory factory : factories) {
			factory.setOwners(ownerDAO.getOwnersByFactoryId(factory.getId()));
		}
		return factories;
	}
	
	public Factory getFactoryById(Long id) {
		factory = factoryDAO.getById(id);
		factory.setOwners(ownerDAO.getOwnersByFactoryId(factory.getId()));
		return factory;
	}
	
	public void createNewFactory (Long id, String name, Long address_id) {
		factory.setId(id);
		factory.setName(name);				
		factory.setAddress(addressDAO.getById(address_id));	
		factory.setOwners(ownerDAO.getOwnersByFactoryId(factory.getId()));
		factoryDAO.create(factory);	
	}
	
	public void updateCustomer (Long id, String name, Long address_id) {

		factory.setId(id);
		factory.setName(name);
		factory.setAddress(addressDAO.getById(address_id));
		factory.setOwners(ownerDAO.getOwnersByFactoryId(factory.getId()));
		factoryDAO.update(factory);
	}
	
	public void deleteCustomer (Long id) {
		factory.setId(id);
		factoryDAO.delete(factory);
	}

}
