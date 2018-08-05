package by.baranov.services;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import by.baranov.dao.jdbc.AddressDAO;
import by.baranov.dao.jdbc.CustomerDAO;
import by.baranov.entities.Customer;

public class CustomerService {
	
	private final static String FORMAT_DATE = "dd/MM/yyyy";
	private CustomerDAO customerDAO;
	private AddressDAO addressDAO;
	private List<Customer> customers;
	private Customer customer;
		
	public CustomerService() {
		this.customerDAO = new CustomerDAO();
		this.addressDAO = new AddressDAO();
		this.customer = new Customer();
	}
	public List<Customer> getCustomersByCityName(String name) {
		customers = customerDAO.getCustomerByCityName(name);
		return customers;
	}
	
	public List<Customer> getAllCustomer(){
		customers = customerDAO.getAll();
		return customers;
	}
	
	public Customer getCustomerById(Long id) {
		customer = customerDAO.getById(id);
		return customer;
	}

	public void createNewCustomer (Long id, String name, String date, Long addressId) {
		customer.setId(id);
		customer.setName(name);
		
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern(FORMAT_DATE);
		LocalDate localDate = LocalDate.parse(date, formatter);
				
		customer.setDate(localDate);
		customer.setAddress(addressDAO.getById(addressId));		
		customerDAO.create(customer);	
	}
	
	public void updateCustomer (Long id, String name, String date, Long address_id) {

		customer.setId(id);
		customer.setName(name);		
		
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern(FORMAT_DATE);
		LocalDate localDate = LocalDate.parse(date, formatter);
			
		customer.setDate(localDate);
		customer.setAddress(addressDAO.getById(address_id));
		customerDAO.update(customer);
	}
	
	public void deleteCustomer (Long id) {
		customer.setId(id);
		customerDAO.delete(customer);
	}	
}
