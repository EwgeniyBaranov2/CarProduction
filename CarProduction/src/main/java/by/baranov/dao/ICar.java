package by.baranov.dao;

import java.util.List;

import by.baranov.entities.Car;

public interface ICar extends IAbstractEntity<Long, Car>{
		List<Car> getCarByCityNameOfManufactureMotor(String cityName);
}
