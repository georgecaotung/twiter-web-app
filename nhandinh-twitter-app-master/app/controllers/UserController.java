package controllers;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.codec.digest.DigestUtils;
import org.jongo.MongoCursor;

import com.fasterxml.jackson.databind.JsonNode;

import play.libs.Json;
import play.mvc.BodyParser;
import play.mvc.Controller;
import play.mvc.Result;
import vn.picore.playtwitter.dto.ResponseData;
import vn.picore.playtwitter.model.Token;
import vn.picore.playtwitter.model.User;
import vn.picore.playtwitter.msgprocessor.MessageProcessor;
import vn.picore.playtwitter.msgprocessor.SweeterUtils;
import vn.picore.playtwitter.service.TokenService;
import vn.picore.playtwitter.service.UserService;

public class UserController extends Controller {

	@BodyParser.Of(BodyParser.FormUrlEncoded.class)
	public Result register() {
		Map<String, String[]> map = request().body().asFormUrlEncoded();
		User user = new User();
		String email = map.get("email")[0];
		user.setEmail(email);
		user.setCreateddate(new Date());
		user.setPassword(DigestUtils.sha1Hex(map.get("password")[0]));
		MongoCursor<User> lu = UserService.getInstance().findByEmail(email);
		ResponseData res = null;
		if (lu.count() > 0) {
			res = new ResponseData<String>(1, "", "ACCOUNT_EXIST");
		} else {
			user.setUsername(MessageProcessor.generateUsername(email));
			UserService.getInstance().create(user);
			res = new ResponseData<String>(0, user.getUsername(), "SUCCESS");
		}
		JsonNode jn = Json.toJson(res);
		response().setHeader("Access-Control-Allow-Origin", "*");
		return ok(jn);
	}

	@BodyParser.Of(BodyParser.FormUrlEncoded.class)
	public Result login() {
		Map<String, String[]> map = request().body().asFormUrlEncoded();
		String email = map.get("email")[0];
		String password = DigestUtils.sha1Hex(map.get("password")[0]);
		UUID id = UserService.getInstance().login(email, password);

		ResponseData res = null;
		if (id != null) {
			String token = SweeterUtils.generateRandom(24);
			Token tk = new Token();
			tk.setUserid(id);
			tk.setToken(token);
			TokenService.getInstance().create(tk);

			MongoCursor<User> users = UserService.getInstance().findByEmail(email);
			User user = users.next();
			HashMap<String, Object> hashMap = new HashMap<String, Object>();
			hashMap.put("token", token);
			hashMap.put("user", user);

			session("userid", id.toString());

			res = new ResponseData<HashMap>(0, hashMap, "SUCCESS");
		} else {
			res = new ResponseData<String>(1, null, "NOT_AUTHORIZED");
		}
		JsonNode jn = Json.toJson(res);
		response().setHeader("Access-Control-Allow-Origin", "*");
		return ok(jn);
	}

	@BodyParser.Of(BodyParser.FormUrlEncoded.class)
	public Result logout() {
		Map<String, String[]> map = request().body().asFormUrlEncoded();
		String[] token = map.get("token");
		if (token == null) {
			session().remove("userid");
		} else {
			TokenService.getInstance().delete(token[0]);
		}
		ResponseData<String> res = new ResponseData<String>(0, null, "SUCCESS");
		JsonNode jn = Json.toJson(res);
		response().setHeader("Access-Control-Allow-Origin", "*");
		return ok(jn);
	}

}
