package by.baranov.services;

import java.util.List;

import by.baranov.dao.jdbc.BodyDAO;
import by.baranov.dao.jdbc.FactoryDAO;
import by.baranov.dao.jdbc.OwnerDAO;
import by.baranov.entities.Body;

public class BodyService {
	private BodyDAO bodyDAO;
	private FactoryDAO factoryDAO;
	private OwnerDAO ownerDAO;
	
	private List<Body> bodies;
	private Body body;
	
	public BodyService() {	
		this.bodyDAO = new BodyDAO();
		this.factoryDAO = new FactoryDAO();
		this.ownerDAO = new OwnerDAO();
		this.body = new Body();
		
	}
	
	public List<Body> getAllBody() {
		bodies = bodyDAO.getAll();
		for (Body body : bodies) {
			body.getFactory().setOwners(ownerDAO.getOwnersByFactoryId(body.getFactory().getId()));
		}
		return bodies;
	}
	
	public Body getBodyById(Long id) {				
		
		body = bodyDAO.getById(id);
		body.getFactory().setOwners(ownerDAO.getOwnersByFactoryId(body.getFactory().getId()));
				
		return body;
	}

	public void createNewBody (Long id, String material, Float capacity, Long factoriesId) {
		
		body.setId(id);  
		body.setMaterial(material);
		body.setCapacity(capacity);
		body.setFactory(factoryDAO.getById(factoriesId));
		body.getFactory().setOwners(ownerDAO.getOwnersByFactoryId(body.getFactory().getId()));
		bodyDAO.create(body);
	}
	
	public void updateBody (Long id, String material, Float capacity, Long factoriesId) {

		body.setId(id);
		body.setMaterial(material);
		body.setCapacity(capacity);
		body.setFactory(factoryDAO.getById(factoriesId));
		body.getFactory().setOwners(ownerDAO.getOwnersByFactoryId(body.getFactory().getId()));		
		bodyDAO.update(body);
	}
	
	public void deleteBody (Long id) {
		body.setId(id);
		bodyDAO.delete(body);
	}
}
