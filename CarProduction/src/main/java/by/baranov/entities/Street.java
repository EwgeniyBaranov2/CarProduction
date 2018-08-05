package by.baranov.entities;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Street extends AbstractEntity{
	
	private String name;
	
	public Street() {
	}
	public Street(Long id, String name) {
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
		return "Street [id=" + getId()+ ", name=" + name + "]";
	}
	
}
