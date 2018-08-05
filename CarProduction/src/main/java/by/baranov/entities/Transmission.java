package by.baranov.entities;

import java.time.LocalDate;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import com.fasterxml.jackson.annotation.JsonFormat;

import by.baranov.runner.LocalDateXmlAdapter;

@XmlRootElement
@XmlType (propOrder={"name", "dateOfManufacture"})
public class Transmission extends AbstractElementOfCar{
	
	private String name;	
	private LocalDate dateOfManufacture; 
			
	public Transmission() {
	}

	public Transmission(Long id, String name, LocalDate dateOfManufacture, Factory factory) {
		super(id, factory);
		this.name = name;
		this.dateOfManufacture = dateOfManufacture;
	}
	
	public String getName() {
		return name;
	}
	@XmlElement(name = "name")
	public void setName(String name) {
		this.name = name;
	}
	@XmlElement(name = "dateOfManufacture", type = String.class)
	@XmlJavaTypeAdapter(LocalDateXmlAdapter.class)
	@JsonFormat(pattern="dd/MM/yyyy")  
	public LocalDate getDateOfManufacture() {
		return dateOfManufacture;
	}

	public void setDateOfManufacture(LocalDate dateOfManufacture) {
		this.dateOfManufacture = dateOfManufacture;
	}

	@Override
	public String toString() {
		return "Transmission [id=" + getId() + ", name=" + name + ", date_of_manufacture=" + dateOfManufacture + ", factory="
				+ getFactory() + "]";
	}

	
}
