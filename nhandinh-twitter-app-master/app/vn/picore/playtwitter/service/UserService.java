package vn.picore.playtwitter.service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.jongo.MongoCursor;

import vn.picore.playtwitter.model.User;

public class UserService {

	private ArrayList<User> userList;
	int counter = 1;

	private UserService() {
		this.userList = new ArrayList<User>();
	}

	public User getById(UUID id) {
		return User.getById(id);
	}

	public MongoCursor<User> findByEmail(String email) {
		MongoCursor<User> cursor = User.getByEmail(email);
		return cursor;
	}

	public User getByUsername(String username) {
		return User.getByUsername(username);
	}

	public void create(User user) {
		User.create(user);
	}

	public MongoCursor<User> search(String query) {
		return User.search(query);
	}

	public UUID login(String email, String password) {
		MongoCursor<User> lu = User.findByEmailAndPassword(email, password);
		if (lu.count() > 0) {
			return lu.next().getId();
		}
		return null;
	}

	private static UserService singleton = new UserService();

	public static UserService getInstance() {
		return singleton;
	}
}