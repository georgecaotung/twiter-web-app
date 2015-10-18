package vn.picore.playtwitter.service;

import java.util.UUID;

import vn.picore.playtwitter.model.Token;

public class TokenService {

	private TokenService() {
	}

	public UUID validate(String token) {
		return Token.validate(token);
	}
	
	public void create(Token token) {
		Token.create(token);
	}
	
	public void delete(String token) {
		Token.delete(token);
	}

	private static TokenService singleton = new TokenService();

	public static TokenService getInstance() {
		return singleton;
	}
}