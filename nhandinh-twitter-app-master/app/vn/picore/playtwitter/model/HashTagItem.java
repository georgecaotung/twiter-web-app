package vn.picore.playtwitter.model;

import java.util.UUID;
import java.util.regex.Pattern;

import org.jongo.MongoCollection;
import org.jongo.MongoCursor;

import uk.co.panaxiom.playjongo.PlayJongo;

public class HashTagItem {

	private UUID _id;
	private String tag;

	public HashTagItem() {
		this._id = UUID.randomUUID();
	}

	public static MongoCollection hashtagitem() {
		return PlayJongo.getCollection("hashtagitem");
	}

	public UUID getId() {
		return _id;
	}

	public void setId(UUID id) {
		this._id = id;
	}

	public String getTag() {
		return tag;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}

	public static void create(HashTagItem tag) {
		HashTagItem.hashtagitem().save(tag);
	}

	public static MongoCursor<HashTagItem> getByTag(String tag) {
		return HashTagItem.hashtagitem().find("{tag: #}", tag).as(HashTagItem.class);
	}

	public static MongoCursor<HashTagItem> search(String query) {
		MongoCursor<HashTagItem> cursor = HashTagItem.hashtagitem().find("{tag: #}", Pattern.compile(query))
				.skip(0).limit(5).as(HashTagItem.class);
		return cursor;
	}

}
