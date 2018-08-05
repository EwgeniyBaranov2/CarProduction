package by.baranov.entities;

import java.time.LocalDate;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonRootName;

import by.baranov.runner.LocalDateXmlAdapter;

@XmlRootElement
@XmlType (propOrder={"model", "dateOfManufacture", "motor", "transmission", "body", "customer", "color", "headlights"})
@JsonRootName("car")
@JsonPropertyOrder({"model", "dateOfManufacture", "motor", "transmission", "body", "customer", "color", "headlights"})
public class Car extends AbstractEntity{
	
	@JsonProperty ("model-name")
	private String model;
	private LocalDate dateOfManufacture;
	private Motor motor;
	private Transmission transmission;
	private Body body;
	private Customer customer;
	private Color color;
	private List<Headlight> headlights;
	
	public Car() {		
	}
	
	public Car(Long id, String model, LocalDate dateOfManufacture, Motor motor, Transmission transmission, 
			Body body, Customer customer, Color color, List<Headlight> headlights) {
		super(id);
		this.model = model;
		this.dateOfManufacture = dateOfManufacture;
		this.motor = motor;
		this.transmission = transmission;
		this.body = body;
		this.customer = customer;
		this.color = color;
		this.headlights = headlights; 
	}

	public String getModel() {
		return model;
	}
	@XmlElement(name = "model")
	public void setModel(String model) {
		this.model = model;
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

	public Motor getMotor() {
		return motor;
	}

	@XmlElement(name = "motor")
	public void setMotor(Motor motor) {
		this.motor = motor;
	}

	public Transmission getTransmission() {
		return transmission;
	}
	@XmlElement(name = "transmission")
	public void setTransmission(Transmission transmission) {
		this.transmission = transmission;
	}

	public Body getBody() {
		return body;
	}
	@XmlElement(name = "body")
	public void setBody(Body body) {
		this.body = body;
	}

	public Customer getCustomer() {
		return customer;
	}
	@XmlElement(name = "customer")
	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	public Color getColor() {
		return color;
	}
	@XmlElement(name = "color")
	public void setColor(Color color) {
		this.color = color;
	}
	
	public List<Headlight> getHeadlights() {
		return headlights;
	}
	@XmlElementWrapper(name = "headlights")
	@XmlElement(name = "headlight")	
	public void setHeadlights(List<Headlight> headlights) {
		this.headlights = headlights;
	}

	@Override
	public String toString() {
		return "Car [id="+getId()+", model=" + model + ", date_of_manufacture=" + dateOfManufacture + ", motor=" + motor
				+ ", transmission=" + transmission + ", body=" + body + ", headlights="+ headlights+ ", customer=" + customer
				+ ", color=" + color + "]";
	}

	

	
	
}
