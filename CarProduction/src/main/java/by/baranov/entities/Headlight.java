package by.baranov.entities;

import java.time.LocalDate;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import com.fasterxml.jackson.annotation.JsonFormat;

import by.baranov.runner.LocalDateXmlAdapter;

@XmlRootElement
@XmlType (propOrder={"name", "dateOfManufacture", "car"})
public class Headlight extends AbstractElementOfCar {
	
	private String name;	
	private LocalDate dateOfManufacture; 	
	private Car car;

	public Headlight() {
	}

	public Headlight(Long id, String name, LocalDate dateOfManufacture, Factory factory, Car car) {
		super(id, factory);
		this.name = name;
		this.dateOfManufacture = dateOfManufacture;
		this.car = car;
	}

	public Car getCar() {
		return car;
	}
	@XmlElement(name = "car")
	public void setCar(Car car) {
		this.car = car;
	}

	public String getName() {
		return name;
	}
	@XmlElement(name = "name")
	public void setName(String name) {
		this.name = name;
	}

	public LocalDate getDateOfManufacture() {
		return dateOfManufacture;
	}
	@XmlElement(name = "dateOfManufacture", type = String.class)
	@XmlJavaTypeAdapter(LocalDateXmlAdapter.class)
	@JsonFormat(pattern="dd/MM/yyyy")
	public void setDateOfManufacture(LocalDate dateOfManufacture) {
		this.dateOfManufacture = dateOfManufacture;
	}

	@Override
	public String toString() {
		return "Headlight [id=" + getId() + ", name=" + name + ", date_of_manufacture=" + dateOfManufacture
				+ ", factory=" + getFactory() + " ]";
	}

}
