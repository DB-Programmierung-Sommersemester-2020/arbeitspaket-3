package demo.controllers;

import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

import demo.model.Password;
import demo.model.User;
import demo.repositories.implementations.PasswordRepository;
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

	private static byte[] generateSalt() {
		try {
			SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
			byte[] salt = new byte[8];
			random.nextBytes(salt);
			return salt;
		} catch (NoSuchAlgorithmException exce) {
			exce.printStackTrace();
			return "42".getBytes();
		}
	}

	private static byte[] generatePassword(String password, byte[] salt) {
		try {
			SecretKeyFactory f = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
			KeySpec ks = new PBEKeySpec(password.toCharArray(), salt, 10000, 160);
			SecretKey s = f.generateSecret(ks);
			Key k = new SecretKeySpec(s.getEncoded(), "AES");
			return k.getEncoded();
		} catch (InvalidKeySpecException | NoSuchAlgorithmException exce) {
			exce.printStackTrace();
			return password.getBytes();
		}
	}

	public boolean checkPassword(User user, String passwd) {
		byte[] pwd = PasswordRepository.getInstance().getPasswordHashByUserId(user.getId());
		byte[] salt = PasswordRepository.getInstance().getSaltByUserId(user.getId());
		byte[] pwdToTest = generatePassword(passwd, salt);
		boolean compare = true;
		if (pwd.length != pwdToTest.length)
			return false;
		for (int i = 0; i < pwd.length; i++) {
			compare &= (pwd[i] == pwdToTest[i]);
		}
		return compare;
	}

	public Optional<User> lookupUser(String username) {
		return this.users.values().stream().filter(user -> user.getUsername().equals(username)).findFirst();
	}

	public User register(String username, String password, String email) {
		if (this.lookupUser(username).isPresent()) {
			RuntimeException exce = new RuntimeException("User " + username + " exists");
			throw exce;
		} else {
			User user = new User(userIdCounter, username, "dummyPassword :)", email); //permanent don't wont to touch User - Model
			this.users.put(username, user);
			byte[] salt = generateSalt();
			byte[] pwdHash = generatePassword(password, salt);
			
			//Userpwd will be saved in separate table => Passwords, by Users => currently will be saved "dummy password"...
			PasswordRepository.getInstance().create(new Password(user.getId(),salt, pwdHash));
			UserRepository.getInstance().create(user);
			userIdCounter++;
			return user;
		}
	}
}
