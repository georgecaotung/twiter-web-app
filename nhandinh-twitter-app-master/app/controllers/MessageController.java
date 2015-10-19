package controllers;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.jongo.MongoCursor;

import com.fasterxml.jackson.databind.JsonNode;

import play.libs.Json;
import play.mvc.BodyParser;
import play.mvc.Controller;
import play.mvc.Result;
import vn.picore.playtwitter.dto.ResponseData;
import vn.picore.playtwitter.model.HashTag;
import vn.picore.playtwitter.model.HashTagItem;
import vn.picore.playtwitter.model.Message;
import vn.picore.playtwitter.model.User;
import vn.picore.playtwitter.msgprocessor.MessageProcessor;
import vn.picore.playtwitter.service.HashTagService;
import vn.picore.playtwitter.service.MessageService;
import vn.picore.playtwitter.service.TokenService;
import vn.picore.playtwitter.service.UserService;

public class MessageController extends Controller {

	@BodyParser.Of(BodyParser.FormUrlEncoded.class)
	public Result postMessage() {
		Map<String, String[]> map = request().body().asFormUrlEncoded();
		String token = map.get("token")[0];
		UUID id = TokenService.getInstance().validate(token);
		User user = UserService.getInstance().getById(id);

		ResponseData res = null;
		if (id == null) {
			res = new ResponseData<String>(1, "", "NOT_AUTHORIZED");
		} else {

			Message msg = new Message();
			String message = map.get("message")[0];
			msg.setMessage(message);
			msg.setCreateddate(new Date());
			msg.setCreator(id);
			MessageService.getInstance().create(msg);

			List<String> tags = MessageProcessor.getTag(message);
			tags.add(user.getUsername());
			String tagName;
			for (int i = 0; i < tags.size(); i++) {
				tagName = tags.get(i);
				if (!HashTagService.getInstance().checkTagExist(tagName)) {
					HashTagItem item = new HashTagItem();
					item.setTag(tagName.toLowerCase());
					HashTagService.getInstance().createItem(item);
				}
				HashTag tag = new HashTag();
				tag.setCreateddate(new Date());
				tag.setMessage(msg.getId());
				tag.setTag(tagName.toLowerCase());
				tag.setUserid(id);
				HashTagService.getInstance().create(tag);
			}
			res = new ResponseData<String>(0, msg.getId().toString(), "SUCCESS");
		}
		JsonNode jn = Json.toJson(res);
		response().setHeader("Access-Control-Allow-Origin", "*");
		return ok(jn);
	}

	public Result timeline() {
		Map<String, String[]> map = request().body().asFormUrlEncoded();
		String last = null;
		if (map != null) {
			String[] lasts = map.get("last");
			if (lasts != null) {
				last = lasts[0];
			}
		}
		List<Message> list = new ArrayList<Message>();
		if (last != null) {
			list = MessageService.getInstance().getLast(last);
		} else {
			list = MessageService.getInstance().getAll();
		}
		ResponseData<List> res = new ResponseData<List>(0, list, "SUCCESS");
		JsonNode jn = Json.toJson(res);
		response().setHeader("Access-Control-Allow-Origin", "*");
		return ok(jn);
	}

	public Result byUser(String username) {
		Map<String, String[]> map = request().body().asFormUrlEncoded();
		Long last = null;
		if (map != null) {
			String[] lasts = map.get("last");
			if (lasts != null) {
				last = Long.valueOf(lasts[0]);
			}
		}
		List<Message> list = new ArrayList<Message>();
		list = MessageService.getInstance().getByUsername(username, 0, 12, last);
		ResponseData<List> res = new ResponseData<List>(0, list, "SUCCESS");
		JsonNode jn = Json.toJson(res);
		response().setHeader("Access-Control-Allow-Origin", "*");
		return ok(jn);
	}

	public Result byUserWithPage(String username, Integer page) {
		page = page < 0 ? 0 : page;
		List<Message> list = MessageService.getInstance().getByUsername(username, page * 12, 12, null);
		ResponseData<List> res = new ResponseData<List>(0, list, "SUCCESS");
		JsonNode jn = Json.toJson(res);
		response().setHeader("Access-Control-Allow-Origin", "*");
		return ok(jn);
	}

	public Result byTag(String tagname) {
		Map<String, String[]> map = request().body().asFormUrlEncoded();
		Long last = null;
		if (map != null) {
			String[] lasts = map.get("last");
			if (lasts != null) {
				last = Long.valueOf(lasts[0]);
			}
		}
		tagname = tagname.toLowerCase();
		MongoCursor<HashTag> list = HashTagService.getInstance().getByTagname(tagname, 0, 12, last);
		List<Message> lm = new ArrayList<Message>();
		List<UUID> idArr = new ArrayList<UUID>();
		while (list.hasNext()) {
			HashTag tag = list.next();
			idArr.add(tag.getMessageId());
		}
		lm = MessageService.getInstance().loadIn(idArr);
		ResponseData<List> res = new ResponseData<List>(0, lm, "SUCCESS");
		JsonNode jn = Json.toJson(res);
		response().setHeader("Access-Control-Allow-Origin", "*");
		return ok(jn);
	}

	public Result byTagWithPage(String tagname, Integer page) {
		page = page < 0 ? 0 : page;
		tagname = tagname.toLowerCase();
		MongoCursor<HashTag> list = HashTagService.getInstance().getByTagname(tagname, page * 12, 12, null);
		List<Message> lm = new ArrayList<Message>();
		List<UUID> idArr = new ArrayList<UUID>();
		for (int i = 0; i < list.count(); i++) {
			idArr.add(list.next().getMessageId());
		}
		lm = MessageService.getInstance().loadIn(idArr);
		ResponseData<List> res = new ResponseData<List>(0, lm, "SUCCESS");
		JsonNode jn = Json.toJson(res);
		response().setHeader("Access-Control-Allow-Origin", "*");
		return ok(jn);
	}

	public Result search(String query) {
		/*
		 * HashMap<String, Object> retData = new HashMap<String, Object>();
		 * retData.put("users", UserService.getInstance().search(query));
		 * retData.put("tags", HashTagService.getInstance().searchTag(query));
		 * 
		 * ResponseData<HashMap> res = new ResponseData<HashMap>(0, retData,
		 * "SUCCESS");
		 */
		query = query.toLowerCase();
		MongoCursor<HashTag> list = HashTagService.getInstance().findByTagname(query, 0, 12);
		List<UUID> idArr = new ArrayList<UUID>();
		while (list.hasNext()) {
			idArr.add(list.next().getMessageId());
		}
		List<Message> lm = new ArrayList<Message>();

		lm = MessageService.getInstance().loadIn(idArr);
		ResponseData<List> res = new ResponseData<List>(0, lm, "SUCCESS");
		JsonNode jn = Json.toJson(res);
		response().setHeader("Access-Control-Allow-Origin", "*");
		return ok(jn);
	}
	
	public Result searchmessage(String query) {
		query = query.toLowerCase();
		MongoCursor<Message> listMessage=  MessageService.getInstance().findByMessage(query,0,30);
		List<UUID> idArr = new ArrayList<UUID>();
		while (listMessage.hasNext()) {
			idArr.add(listMessage.next().getId());
		}
		List<Message> lm = new ArrayList<Message>();

		lm = MessageService.getInstance().loadIn(idArr);
		ResponseData<List> res = new ResponseData<List>(0, lm, "SUCCESS");
		JsonNode jn = Json.toJson(res);
		response().setHeader("Access-Control-Allow-Origin", "*");
		return ok(jn);
	}

	public Result searchuser(String query) {
		query = query.toLowerCase();
		MongoCursor<User> listUser=  UserService.getInstance().search(query);
		List<UUID> idArr = new ArrayList<UUID>();
		while (listUser.hasNext()) {
			idArr.add(listUser.next().getId());
		}
		List<Message> lm = new ArrayList<Message>();

		lm = MessageService.getInstance().loadInUser(idArr,0,30);
		ResponseData<List> res = new ResponseData<List>(0, lm, "SUCCESS");
		JsonNode jn = Json.toJson(res);
		response().setHeader("Access-Control-Allow-Origin", "*");
		return ok(jn);
	}

	public Result searchBox(String query) {
		query = query.toLowerCase();
		HashMap<String, Object> retData = new HashMap<String, Object>();
		retData.put("users", UserService.getInstance().search(query));
		retData.put("tags", HashTagService.getInstance().searchTag(query));
		ResponseData<HashMap> res = new ResponseData<HashMap>(0, retData, "SUCCESS");
		JsonNode jn = Json.toJson(res);
		response().setHeader("Access-Control-Allow-Origin", "*");
		return ok(jn);
	}

}
