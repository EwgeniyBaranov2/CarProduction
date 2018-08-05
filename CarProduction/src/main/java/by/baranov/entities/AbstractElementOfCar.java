package by.baranov.entities;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public abstract class AbstractElementOfCar extends AbstractEntity{
	
	private Factory factory;	
	
	public AbstractElementOfCar() {
	}

	public AbstractElementOfCar(Long id, Factory factory) { 
		super(id);		
		this.factory = factory;		
	}

	public Factory getFactory() {
		return factory;
	}
	@XmlElement(name = "factory")
	public void setFactory(Factory factory) {
		this.factory = factory;
	}	
}
