package demo.repositories.services;

import java.util.List;

public interface CRUDRepository<T, K> {
	
	boolean create(T t);
	List<T> getAll();
	T getById(K k);
	boolean update(T t);
	boolean delete(T t);
	
}
