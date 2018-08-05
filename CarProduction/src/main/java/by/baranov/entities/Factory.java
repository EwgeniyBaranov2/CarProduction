package by.baranov.entities;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import com.fasterxml.jackson.annotation.JsonProperty;

@XmlRootElement
@XmlType (propOrder={"name", "address", "owners"})
public class Factory extends AbstractEntity{
	
	private String name;	
	private Address address;	
	private List<Owner> owners;
	
	public Factory() {		
	}
	
	public Factory(Long id, String name, Address address, List<Owner> owners) {
		super(id);
		this.name = name;
		this.address = address;
		this.owners = owners;
	}
	@XmlElement(name = "address")	
	public Address getAddress() {
		return address;
	}
	
	public void setAddress(Address address) {
		this.address = address;
	}
	@XmlElementWrapper(name = "owners")
	@XmlElement(name = "owner")
	@JsonProperty ("owners")
	
	public List<Owner> getOwners() {
		return owners;
	}
	
	public void setOwners(List<Owner> owners) {
		this.owners = owners;
	}
	@XmlElement(name = "name")
	public String getName() {
		return name;
	}	
	public void setName(String name) {
		this.name = name;
	}
	@Override
	public String toString() {
		return "Factory [id="+ getId()+ ", name=" + name + ", address=" + address + ", owners="+ owners + "]";
	}

	
}
