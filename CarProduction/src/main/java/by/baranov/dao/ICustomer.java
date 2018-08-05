package by.baranov.dao;

import java.util.List;

import by.baranov.entities.Customer;

public interface ICustomer extends IAbstractEntity<Long, Customer>{
	
	List <Customer> getCustomerByCityName (String cityName);
	
}
