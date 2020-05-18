package demo.repositories.implementations;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import demo.model.Note;
import demo.repositories.database.DBConnection;
import demo.repositories.services.NotesRepositoryService;

public class NoteRepository implements NotesRepositoryService{

	@Override
	public boolean create(Note note) {
		String insertString = "INSERT INTO Notes(subject, content, date) VALUES(?,?,NOW())";
		int count = 0;
		boolean isCreated = false;
		try {
			Connection connection = DBConnection.getInstance().getConnection();
			connection.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);

			PreparedStatement statement = connection.prepareStatement(insertString);

			statement.setString(1, note.getSubject());
			statement.setString(2, note.getContent()); 
			
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
	public List<Note> getAll() {
		String query = "SELECT * FROM Notes";
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

	@Override
	public Note getById(Integer id) {
		Optional<Note> optNote = getAll().stream().filter(n -> n.getId() == id).findFirst();
		return optNote.isPresent() ? optNote.get() : null;
	}

	@Override
	public boolean update(Note note) {
		String updateString = "UPDATE Notes SET subject = ?, content = ? date = NOW()"
							+" WHERE id = ?";
		int count = 0;
		boolean isUpdated = false;

		if (note.getId() > 0) { //
			try {
				Connection connection = DBConnection.getInstance().getConnection();
				PreparedStatement statement = connection.prepareStatement(updateString);

				statement.setString(1, note.getSubject());
				statement.setString(2, note.getContent());
				statement.setInt(3, note.getId());
				
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
	public boolean delete(Note note) {
		String deleteString = "DELETE FROM Notes WHERE id = ?";
		int count = 0;
		boolean isDeleted = false;
		try {
			Connection connection = DBConnection.getInstance().getConnection();
			connection.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
			
			PreparedStatement statement = connection.prepareStatement(deleteString);
			
			statement.setInt(1, note.getId());

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
