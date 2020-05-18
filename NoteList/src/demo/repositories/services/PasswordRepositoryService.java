package demo.repositories.services;

import demo.model.Password;

public interface PasswordRepositoryService extends CRUDRepository<Password, Integer>{
	byte[] getPasswordHashByUserId(int id);
	byte[] getSaltByUserId(int id);
}
