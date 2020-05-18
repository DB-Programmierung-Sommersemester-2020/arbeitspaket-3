package demo.repositories.implementations;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import demo.model.User;
import demo.repositories.database.DBConnection;
import demo.repositories.services.UserRepositoryService;

public class UserRepository implements UserRepositoryService {

	private static UserRepository instance = null;

	private UserRepository() {

	}

	public static UserRepository getInstance() {
		if (instance == null) {
			instance = new UserRepository();
		}
		return instance;
	}

	@Override
	public boolean create(User user) {

		// Ueberpruefung nach doppelte Eintraege erfolgt in UserManager
		// Deswegen hier wird auf die Ueberpruefung verzichtet...
		// ID is autoincrement
		String insertString = "INSERT INTO Users(username, password, email) VALUES(?,?,?)";
		int count = 0;
		boolean isCreated = false;
		try {
			Connection connection = DBConnection.getInstance().getConnection();
			connection.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);

			PreparedStatement statement = connection.prepareStatement(insertString);

			statement.setString(1, user.getUsername());
			statement.setString(2, user.getPassword()); // TODO: adjust visibility/security.
			statement.setString(3, user.getEmail());
			count = statement.executeUpdate();

			statement.close();
			connection.close();

		} catch (SQLException e) {
			e.printStackTrace();
		}

		isCreated = (count > 0) ? true : false;

		return isCreated;
	}

	@Override
	public List<User> getAll() {
		String query = "SELECT * FROM Users";
		List<User> users = new ArrayList<User>();
		try {
			Connection connection = DBConnection.getInstance().getConnection();
			connection.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);

			ResultSet resultSet = connection.createStatement().executeQuery(query);
			while (resultSet.next()) {
				User user = new User(resultSet.getInt("id"), resultSet.getString("username"),
						resultSet.getString("password"), resultSet.getString("email"));
				users.add(user);

				resultSet.close();
				connection.close();
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return users;
	}

	@Override
	public User getById(Integer id) {
		Optional<User> optUser = getAll().stream().filter(u -> u.getId() == id).findFirst();
		return optUser.isPresent() ? optUser.get() : null;
	}

	@Override
	public boolean update(User user) {
		String updateString = "UPDATE Users SET username = ?, password = ? email = ? WHERE id = ?";
		int count = 0;
		boolean isUpdated = false;

		if (user.getId() > 0) { //
			try {
				Connection connection = DBConnection.getInstance().getConnection();
				PreparedStatement statement = connection.prepareStatement(updateString);

				statement.setString(1, user.getUsername());
				statement.setString(2, user.getPassword()); // TODO: anpassen gemaes Aufgabe
				statement.setString(3, user.getEmail());
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
	public boolean delete(User user) {
		String deleteString = "DELETE FROM Users WHERE id = ? AND username = ? AND email = ?";
		int count = 0;
		boolean isDeleted = false;
		try {
			Connection connection = DBConnection.getInstance().getConnection();
			PreparedStatement statement = connection.prepareStatement(deleteString);
			statement.setInt(1, user.getId());
			statement.setString(2, user.getUsername());
			statement.setString(3, user.getEmail());

			count = statement.executeUpdate();
			statement.close();
			connection.close();

		} catch (SQLException e) {
			e.printStackTrace();
		}
		isDeleted = (count > 0) ? true : false;
		return isDeleted;
	}

}
