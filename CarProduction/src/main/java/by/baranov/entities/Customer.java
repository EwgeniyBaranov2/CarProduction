package by.baranov.entities;

import java.time.LocalDate;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Customer extends AbstractPerson{	
	
	public Customer() {		
	}

	public Customer(Long id, String name, Address address, LocalDate dateOfPurchase) {
		super(id, name, address, dateOfPurchase);	
	}

	@Override
	public String toString() {
		return "Customer [id="+getId()+", name=" + getName() + ", date_of_purchase=" + getDate() + ", "
				+ "Address=" + getAddress()+ "]";
	}
	

}
