package by.baranov.dao;

import java.util.List;

import by.baranov.entities.Headlight;

public interface IHeadlight extends IAbstractEntity<Long, Headlight>{
	List<Headlight> getHeadlightsByCarId(Long id);
}
