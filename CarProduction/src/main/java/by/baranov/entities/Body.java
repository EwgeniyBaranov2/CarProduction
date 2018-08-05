package by.baranov.entities;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement
@XmlType (propOrder={"material","capacity"})
public class Body extends AbstractElementOfCar{
	
	private String material;	
	private Float capacity;	
		
	public Body() {		
	}
	
	public Body(Long id, String material, Float capacity, Factory factory) {
		super(id, factory);
		this.material = material;
		this.capacity = capacity;		
	}
	@XmlElement(name = "material")
	public String getMaterial() {
		return material;
	}
	
	public void setMaterial(String material) {
		this.material = material;
	}
	@XmlElement(name = "capacity")
	public float getCapacity() {
		return capacity;
	}
	
	public void setCapacity(Float capacity) {
		this.capacity = capacity;
	}

	@Override
	public String toString() {
		return "Body [id="+getId()+", material=" + material + ", capacity=" + capacity + ", factory=" + getFactory() + "]";
	}
	
	
	
	
	
}
