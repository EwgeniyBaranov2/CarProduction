package by.baranov.entities;

import java.time.LocalDate;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import com.fasterxml.jackson.annotation.JsonFormat;

import by.baranov.runner.LocalDateXmlAdapter;

@XmlRootElement
@XmlType (propOrder={"force", "dateOfManufacture"})
public class Motor extends AbstractElementOfCar {
	
	private Integer force;	
	private LocalDate dateOfManufacture; 

	public Motor() {
	}

	public Motor(Long id, LocalDate dateOfManufacture, Factory factory, Integer force) {
		super(id, factory);
		this.dateOfManufacture = dateOfManufacture;
		this.force = force;
	}

	public Integer getForce() {
		return force;
	}
	@XmlElement(name = "force")
	public void setForce(Integer force) {
		this.force = force;
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
		return "Motor [id=" + getId() + ", force=" + force + ", date_of_manufacture=" + dateOfManufacture + ", "
				+ "factory=" + getFactory() + "]";
	}

}
