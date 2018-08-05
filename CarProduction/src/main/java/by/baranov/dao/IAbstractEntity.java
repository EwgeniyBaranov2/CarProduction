package by.baranov.dao;

import java.util.List;

import by.baranov.entities.AbstractEntity;

public interface IAbstractEntity <K, T extends AbstractEntity> {

	List<T> getAll();
	T getById(K id);	

	void create(T entity);

	void update(T entity);

	void delete (T entity);
	
}
