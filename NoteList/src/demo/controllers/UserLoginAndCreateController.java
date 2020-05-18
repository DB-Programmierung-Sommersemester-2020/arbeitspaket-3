package demo.controllers;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import demo.model.User;
import demo.repositories.implementations.UserRepository;

public class UserLoginAndCreateController {
	private static UserLoginAndCreateController instance = new UserLoginAndCreateController();
	private Map<String, User> users = new HashMap<>();
	private int userIdCounter = 1;

	private UserLoginAndCreateController() {
		super();
		fillUsersMap();
	}

	public static UserLoginAndCreateController getInstance() {
		return UserLoginAndCreateController.instance;
	}
	
	private void fillUsersMap() {
		if (!UserRepository.getInstance().getAll().isEmpty())
			UserRepository.getInstance().getAll().forEach(user -> users.put(user.getUsername(), user));
	}

	public Optional<User> lookupUser(String username) {
		return this.users.values().stream().filter(user -> user.getUsername().equals(username)).findFirst();
	}

	public User register(String username, String password, String email) {
		if (this.lookupUser(username).isPresent()) {
			RuntimeException exce = new RuntimeException("User " + username + " exists");
			throw exce;
		} else {
			User user = new User(userIdCounter, username, password, email);
			this.users.put(username, user);
			UserRepository.getInstance().create(user);
			userIdCounter++;
			return user;
		}
	}
}
