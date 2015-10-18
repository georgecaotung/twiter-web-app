package vn.picore.playtwitter.service;

import java.util.ArrayList;
import java.util.UUID;

import org.bson.types.ObjectId;
import org.jongo.MongoCursor;

import vn.picore.playtwitter.model.HashTag;
import vn.picore.playtwitter.model.HashTagItem;
import vn.picore.playtwitter.model.User;

public class HashTagService {

	private ArrayList<HashTag> tagList;

	private HashTagService() {
		this.tagList = new ArrayList<HashTag>();
	}

	public MongoCursor<User> findByEmail(String email) {
		MongoCursor<User> cursor = User.getByEmail(email);
		return cursor;
	}

	public void create(HashTag tag) {
		HashTag.create(tag);
	}

	public void save(HashTag tag) {
		HashTag.save(tag);
	}

	public MongoCursor<HashTag> getByTagname(String tagname, Integer from, Integer perpage, Long timestamp) {
		return HashTag.getByTagName(tagname, from, perpage, timestamp);
	}
	
	public MongoCursor<HashTag> findByTagname(String tagname, Integer from, Integer perpage) {
		return HashTag.findByTagName(tagname, from, perpage);
	}

	public MongoCursor<HashTagItem> searchTag(String tag) {
		return HashTagItem.search(tag);
	}

	public Boolean checkTagExist(String tag) {
		MongoCursor<HashTagItem> list = HashTagItem.getByTag(tag);
		return list.count() > 0 ? true : false;
	}
	/*
	 * public MongoCursor<HashTag> getByUser(UUID userid) { return
	 * HashTag.getByUser(userid); }
	 */

	public UUID login(String email, String password) {
		MongoCursor<User> lu = User.findByEmailAndPassword(email, password);
		if (lu.count() > 0) {
			return lu.next().getId();
		}
		return null;
	}

	public void createItem(HashTagItem item) {
		HashTagItem.create(item);
	}

	private static HashTagService singleton = new HashTagService();

	public static HashTagService getInstance() {
		return singleton;
	}
}