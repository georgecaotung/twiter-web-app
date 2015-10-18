package vn.picore.playtwitter.model;

import java.util.Date;
import java.util.UUID;
import java.util.regex.Pattern;

import org.jongo.MongoCollection;
import org.jongo.MongoCursor;

import uk.co.panaxiom.playjongo.PlayJongo;

public class User {

	private UUID _id;
	private String email;
	private String username;
	private String password;
	private Date createddate;

	public static MongoCollection users() {
		return PlayJongo.getCollection("users");
	}

	public User() {
		this._id = UUID.randomUUID();
	}

	public UUID getId() {
		return _id;
	}

	public void setId(UUID _id) {
		this._id = _id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Date getCreateddate() {
		return createddate;
	}

	public void setCreateddate(Date createddate) {
		this.createddate = createddate;
	}

	public static User getByUsername(String username) {
		MongoCursor<User> users = User.users().find("{username: #}", username).as(User.class);
		return users.count() > 0 ? users.next() : null;
	}

	public static User getById(UUID id) {
		MongoCursor<User> users = User.users().find("{_id: #}", id).as(User.class);
		return users.count() > 0 ? users.next() : null;
	}

	public static void create(User user) {
		User.users().save(user);
	}

	public static MongoCursor<User> search(String query) {
		MongoCursor<User> cursor = User.users().find("{username: #}", Pattern.compile(query)).skip(0).limit(5)
				.as(User.class);
		return cursor;
	}

	public static MongoCursor<User> getByEmail(String email) {
		MongoCursor<User> cursor = User.users().find("{email: #}", email).as(User.class);
		return cursor;
	}

	public static MongoCursor<User> findByEmailAndPassword(String email, String password) {
		MongoCursor<User> cursor = User.users().find("{email: #, password: #}", email, password).as(User.class);
		return cursor;
	}

}
