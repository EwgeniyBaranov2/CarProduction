package by.baranov.dao;

import java.util.List;

import by.baranov.entities.Owner;

public interface IOwner extends IAbstractEntity<Long, Owner>{
	List<Owner> getOwnersByFactoryId(Long id);
	List<Owner> getOwnersByCityName(String cityName);
}
