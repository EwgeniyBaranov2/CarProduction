package by.baranov.entities;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement

public class Cars{
	private List<Car> cars;
	
	public Cars() {
		
	}
	public Cars(List<Car> cars) {
		
		this.cars = cars;
	}

	public List<Car> getCars() {
		return cars;
	}
	@XmlElementWrapper(name = "cars")
	@XmlElement(name = "car")
	public void setCars(List<Car> cars) {
		this.cars = cars;
	}
	@Override
	public String toString() {
		return "Cars [cars=" + cars + "]";
	}
	
	

}
