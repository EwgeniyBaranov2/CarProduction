package by.baranov.entities;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class City extends AbstractEntity{
	
	private String name;
	
	public City() {		
	}
	
	public City(Long id, String name) {
		super(id);
		this.name = name;
	}	
	
	public String getName() {
		return name;
	}
	@XmlElement(name = "name")
	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return "City [id=" +getId()+ ", name=" + name + "]";
	}

}
