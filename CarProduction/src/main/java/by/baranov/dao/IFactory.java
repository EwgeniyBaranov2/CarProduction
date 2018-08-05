package by.baranov.dao;

import java.util.List;

import by.baranov.entities.Factory;

public interface IFactory extends IAbstractEntity<Long, Factory>{
	
	List<Factory> getFactoryByCityName(String nameCity);
	List<Factory> getFactoriesByOwnerId(Long id);
	
}
