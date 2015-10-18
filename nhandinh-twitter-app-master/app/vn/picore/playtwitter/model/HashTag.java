package vn.picore.playtwitter.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.regex.Pattern;

import org.jongo.MongoCollection;
import org.jongo.MongoCursor;

import uk.co.panaxiom.playjongo.PlayJongo;

public class HashTag {

	private UUID _id;
	private UUID messageid;
	private String tag;
	private UUID userid;
	private Date createddate;

	public HashTag() {
		this._id = UUID.randomUUID();
	}

	public static MongoCollection hashtags() {
		return PlayJongo.getCollection("hashtags");
	}

	public UUID getId() {
		return _id;
	}

	public UUID getUserid() {
		return userid;
	}

	public void setUserid(UUID userid) {
		this.userid = userid;
	}

	public void setId(UUID id) {
		this._id = id;
	}

	public UUID getMessageId() {
		return messageid;
	}

	public void setMessage(UUID message) {
		this.messageid = message;
	}

	public String getTag() {
		return tag;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}

	public Date getCreateddate() {
		return createddate;
	}

	public void setCreateddate(Date createddate) {
		this.createddate = createddate;
	}

	public static void create(HashTag tag) {
		HashTag.hashtags().save(tag);
	}

	public static void save(HashTag tag) {
		HashTag.hashtags().save(tag);
	}

	public static MongoCursor<HashTag> search(String query) {
		MongoCursor<HashTag> cursor = HashTag.hashtags().find("{tag: #}", query).as(HashTag.class);
		return cursor;
	}

	public static MongoCursor<HashTag> getByTagName(String tagname, Integer from, Integer perpage, Long timestamp) {
		if (timestamp != null) {
			Date date = new Date(timestamp + 1000);
			return HashTag.hashtags().find("{createddate: {$gt: #}, tag: #}", date, tagname).sort("{createddate: -1}")
					.skip(from).limit(perpage).as(HashTag.class);

		} else {
			return HashTag.hashtags().find("{tag: #}", tagname).sort("{createddate: -1}").skip(from).limit(perpage)
					.as(HashTag.class);
		}
	}

	public static MongoCursor<HashTag> findByTagName(String tagname, Integer from, Integer perpage) {
		return HashTag.hashtags().find("{tag: #}", Pattern.compile(tagname)).sort("{createddate: -1}").skip(from)
				.limit(perpage).as(HashTag.class);
	}

}
