package vn.picore.playtwitter.model;

import java.util.UUID;

import org.jongo.MongoCollection;
import org.jongo.MongoCursor;

import uk.co.panaxiom.playjongo.PlayJongo;

public class Token {

	private UUID _id;
	private String token;
	private UUID userid;

	public static MongoCollection token() {
		return PlayJongo.getCollection("token");
	}

	public Token() {
		this._id = UUID.randomUUID();
	}

	public UUID getId() {
		return _id;
	}

	public void setId(UUID _id) {
		this._id = _id;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public UUID getUserid() {
		return userid;
	}

	public void setUserid(UUID userid) {
		this.userid = userid;
	}

	public static void create(Token token) {
		Token.token().save(token);
	}
	
	public static void delete(String token) {
		Token.token().remove("{token: #}", token);
	}

	public static UUID validate(String token) {
		MongoCursor<Token> cursor = Token.token().find("{token: '" + token + "'}").as(Token.class);
		return cursor.count() > 0 ? cursor.next().getUserid() : null;
	}
}
