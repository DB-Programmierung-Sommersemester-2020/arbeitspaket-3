package demo.repositories.implementations;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import demo.model.Password;
import demo.model.User;
import demo.repositories.database.DBConnection;
import demo.repositories.services.PasswordRepositoryService;

public class PasswordRepository implements PasswordRepositoryService {

	private static PasswordRepository instance = null;
	
	private PasswordRepository() {
		
	}
	
	public static PasswordRepository getInstance() {
		return (instance == null) ? new PasswordRepository() : instance;
	}
	
	@Override
	public boolean create(Password password) {
		String insertString = "INSERT INTO Passwords VALUES(?,?,?)";
		boolean passwordExitsts = (this.getAll().stream().filter(pwd->pwd.getId() == password.getId()).count()>0);
		boolean isCreated = false;
		int count = 0;
		if (!passwordExitsts) {
			try {
				Connection connection = DBConnection.getInstance().getConnection();
				PreparedStatement statement = connection.prepareStatement(insertString);
				
				statement.setInt(1, password.getId());
				statement.setBytes(2, password.getSalt());
				statement.setBytes(3, password.getPasswordHash());
				isCreated = (count > 0) ? true : false;
				count = statement.executeUpdate();
				
				statement.close();
				connection.close();
				
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		return isCreated;
	}
	
	public boolean create(Password password, User user) {
		
		String insertString = "INSERT INTO Passwords VALUES(?,?,?)";
		boolean passwordExitsts = (this.getAll().stream().filter(pwd->pwd.getId() == password.getId()).count()>0);
		boolean isCreated = false;
		int count = 0;
		if (!passwordExitsts) {
			try {
				Connection connection = DBConnection.getInstance().getConnection();
				PreparedStatement statement = connection.prepareStatement(insertString);
				
				statement.setInt(1, user.getId());
				statement.setBytes(2, password.getSalt());
				statement.setBytes(3, password.getPasswordHash());
				isCreated = (count > 0) ? true : false;
				count = statement.executeUpdate();
				
				statement.close();
				connection.close();
				
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		return isCreated;
	}

	@Override
	public List<Password> getAll() {
		String query = "SELECT * FROM Passwords";
		List<Password> passwords = new ArrayList<Password>();
		try {
			Connection connection = DBConnection.getInstance().getConnection();
			connection.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);

			ResultSet resultSet = connection.createStatement().executeQuery(query);
			while (resultSet.next()) {
				Password pwd = new Password(resultSet.getInt("id"), resultSet.getBytes("salt"),
						resultSet.getBytes("pwdhash"));
				passwords.add(pwd);

				resultSet.close();
				connection.close();
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return passwords;
	}

	@Override
	public Password getById(Integer id) {
		Optional<Password> optPwd = getAll().stream().filter(pwd->pwd.getId()==id).findFirst();
		return optPwd.isPresent() ? optPwd.get() : null;
	}

	@Override
	public boolean update(Password password) {
		String updateString = "UPDATE Passwords SET salt = ? pwdhash = ? WHERE id = ?";
		int count = 0;
		boolean isUpdated = false;

		if (password.getId() > 0) { 
			try {
				Connection connection = DBConnection.getInstance().getConnection();
				connection.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
				PreparedStatement statement = connection.prepareStatement(updateString);

				statement.setBytes(1, password.getSalt());
				statement.setBytes(2, password.getPasswordHash());
				statement.setInt(3, password.getId());
				
				count = statement.executeUpdate();

				statement.close();
				connection.close();

			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		isUpdated = (count > 0) ? true : false;
		return isUpdated;
	}

	@Override
	public boolean delete(Password password) {
		return delete(password.getId());
	}
	
	public boolean delete(int id) {
		String deleteString = "DELETE FROM Passwords WHERE id = ?";
		int count = 0;
		boolean isDeleted = false;
		try {
			Connection connection = DBConnection.getInstance().getConnection();
			connection.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
			
			PreparedStatement statement = connection.prepareStatement(deleteString);
			
			statement.setInt(1, id);

			count = statement.executeUpdate();
			statement.close();
			connection.close();

		} catch (SQLException e) {
			e.printStackTrace();
		}
		isDeleted = (count > 0) ? true : false;
		return isDeleted;
	}

	@Override
	public byte[] getPasswordHashByUserId(int id) { //UserId => PasswordId	
		return getById(id).getPasswordHash();
	}

	@Override
	public byte[] getSaltByUserId(int id) {
		return getById(id).getPasswordHash();
	}

	

}
