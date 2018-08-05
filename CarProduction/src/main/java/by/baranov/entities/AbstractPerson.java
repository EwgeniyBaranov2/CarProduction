package by.baranov.entities;

import java.time.LocalDate;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import com.fasterxml.jackson.annotation.JsonFormat;

import by.baranov.runner.LocalDateXmlAdapter;

@XmlRootElement
@XmlType (propOrder={"name", "address", "date"})
public abstract class AbstractPerson extends AbstractEntity{	
	
	private String name;
	private Address address;
	private LocalDate date;
	
	public AbstractPerson() {	
	}

	public AbstractPerson(Long id, String name, Address address, LocalDate date) {
		super(id);
		this.name = name;
		this.address = address;
		this.date = date;
	}

	public Address getAddress() {
		return address;
	}
	@XmlElement(name = "address")
	public void setAddress(Address address) {
		this.address = address;
	}

	public LocalDate getDate() {
		return date;
	}
	@XmlElement(name = "dateOfPurchase", type = String.class)
	@XmlJavaTypeAdapter(LocalDateXmlAdapter.class)
	@JsonFormat(pattern="dd/MM/yyyy")	
	public void setDate(LocalDate date) {
		this.date = date;
	}

	public String getName() {
		return name;
	}
	@XmlElement(name = "name")
	public void setName(String name) {
		this.name = name;
	}	

	
}
