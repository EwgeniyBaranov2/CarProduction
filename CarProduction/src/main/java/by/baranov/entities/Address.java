package by.baranov.entities;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement
@XmlType (propOrder={"city","street", "house"})
public class Address extends AbstractEntity{
	
	private City city;	
	private Street street;	
	private House house;
	
	public Address() {		
	}

	public Address(Long id, City city, Street street, House house) {
		super(id);
		this.city = city;
		this.street = street;
		this.house = house;
	}
	@XmlElement(name = "city")
	public City getCity() {
		return city;
	}
	
	public void setCity(City city) {
		this.city = city;
	}
	@XmlElement(name = "street")
	public Street getStreet() {
		return street;
	}
	
	public void setStreet(Street street) {
		this.street = street;
	}
	@XmlElement(name = "house")
	public House getHouse() {
		return house;
	}

	public void setHouse(House house) {
		this.house = house;
	}

	@Override
	public String toString() {
		return "Address [Id= "+getId()+ ", cityId=" + city + ", street=" + street + ", house=" + house + "]";
	}

	

	

}
