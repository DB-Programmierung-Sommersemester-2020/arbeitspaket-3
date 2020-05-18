package demo.controllers;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import demo.model.Note;
import demo.model.User;
import demo.repositories.database.DBConnection;
import demo.repositories.services.NotesRepositoryService;
import demo.repositories.services.UserRepositoryService;

public class UserController {

	private UserRepositoryService userRepositoryService;
	private NotesRepositoryService notesRepositoryService;

	public UserController(UserRepositoryService userRepositoryService, NotesRepositoryService notesRepositoryService) {
		super();
		this.userRepositoryService = userRepositoryService;
		this.notesRepositoryService = notesRepositoryService;
	}

	public boolean saveNoteOfUser(User user, Note note) {
		notesRepositoryService.create(note);
		String updateString = "UPDATE Notes SET userId = ? WHERE id = ?";
		boolean isSaved = false;
		int count = 0;
		try {
			Connection connection = DBConnection.getInstance().getConnection();
			PreparedStatement statement = connection.prepareStatement(updateString);

			statement.setInt(1, user.getId());
			statement.setInt(2, note.getId());

			count = statement.executeUpdate();
			isSaved = (count > 0) ? true : false;

			statement.close();
			connection.close();

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return isSaved;
	}

	public boolean removeAllNotesOfUser(User user) {
		String deleteString = "DELETE FROM Notes WHERE userId = ?";
		
		int count = 0;
		boolean isDeleted = false;
		try {
			Connection connection = DBConnection.getInstance().getConnection();
			connection.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
			
			PreparedStatement statement = connection.prepareStatement(deleteString);
			
			statement.setInt(1, user.getId());

			count = statement.executeUpdate();
			
			statement.close();
			connection.close();

		} catch (SQLException e) {
			e.printStackTrace();
		}
		isDeleted = (count > 0) ? true : false;
		return isDeleted;
	}
	
	public List<Note> getAllNotesOfUser(User user) {
		String query = "SELECT * FROM Notes WHERE userId = " + user.getId();
		List<Note> notes = new ArrayList<Note>();
		try {
			Connection connection = DBConnection.getInstance().getConnection();
			connection.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);

			ResultSet resultSet = connection.createStatement().executeQuery(query);
			while (resultSet.next()) {
				Note note = new Note(resultSet.getInt("id"), resultSet.getString("subject"),
						resultSet.getString("content")); //TODO: getDate()...
				notes.add(note);

				resultSet.close();
				connection.close();
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return notes;
	}
}
