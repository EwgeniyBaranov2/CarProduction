package by.baranov.entities;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class House extends AbstractEntity{
	
	private String numberOfHouse;
	public House() {		
	}
	
	public House(Long id, String numberOfHouse) {
		super(id);	
		this.numberOfHouse = numberOfHouse;
	}
	
	public String getNumberOfHouse() {
		return numberOfHouse;
	}
	@XmlElement(name = "numberOfHouse")
	public void setNumberOfHouse(String numberOfHouse) {
		this.numberOfHouse = numberOfHouse;
	}

	@Override
	public String toString() {
		return "House [id=" + getId() + ", numberOfHouse=" + numberOfHouse + "]";
	}
	

}
