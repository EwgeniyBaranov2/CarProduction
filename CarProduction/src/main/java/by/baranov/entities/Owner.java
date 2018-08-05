package by.baranov.entities;

import java.time.LocalDate;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import com.fasterxml.jackson.annotation.JsonIgnore;

@XmlRootElement
public class Owner extends AbstractPerson{
	@XmlTransient
	@JsonIgnore
	private List<Factory> factories;
	
	public Owner() {		
	}
	public Owner(Long id, String name, Address address, LocalDate dateOfBirth, List<Factory> factories) {
		super(id, name, address, dateOfBirth);
		this.factories = factories;
	}
	
	@XmlElementWrapper(name = "factories")
	@XmlElement(name = "factory")
	public List<Factory> getFactories() {
		return factories;
	}
	
	public void setFactories(List<Factory> factories) {
		this.factories = factories;
	}
	@Override
	public String toString() {
		return "Owner [id=" + getId() + ", name=" + getName() + ", date_of_birth=" + getDate() + " "
				+ "address= "+ getAddress()+", factories="+ factories +"]";
	}

}
