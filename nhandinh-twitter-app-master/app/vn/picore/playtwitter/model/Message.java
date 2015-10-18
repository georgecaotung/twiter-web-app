package vn.picore.playtwitter.model;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.regex.Pattern;

import org.jongo.MongoCollection;
import org.jongo.MongoCursor;

import uk.co.panaxiom.playjongo.PlayJongo;

public class Message {

	private UUID _id;
	private String message;
	private UUID creator;
	private Date createddate;

	private User user;

	public Message() {
		this._id = UUID.randomUUID();
	}

	public static MongoCollection messages() {
		return PlayJongo.getCollection("messages");
	}

	public UUID getId() {
		return _id;
	}

	public void setId(UUID id) {
		this._id = id;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public UUID getCreator() {
		return creator;
	}

	public void setCreator(UUID creator) {
		this.creator = creator;
	}

	public Date getCreateddate() {
		return createddate;
	}

	public void setCreateddate(Date createddate) {
		this.createddate = createddate;
	}

	public User getUser() {
		return user;
	}

	private Message loadUser() {
		this.user = User.getById(this.getCreator());
		return this;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public static void create(Message msg) {
		Message.messages().save(msg);
	}

	public static List<Message> loadIn(List<UUID> inArr) {

		MongoCursor<Message> msgs = Message.messages().find("{_id: {$in: #}}", inArr).sort("{createddate: -1}")
				.as(Message.class);
		return Message.normalize(msgs);
	}
	
	public static List<Message> getByUser(UUID id, Integer from, Integer perpage, Long timestamp) {
		MongoCursor<Message> msgs = null;
		if (timestamp != null) {
			Date date = new Date(timestamp);
			msgs = Message.messages().find("{creator: #, createddate: {$gt: #}}", id, date).sort("{createddate: -1}")
					.skip(from).limit(perpage).as(Message.class);
		} else {
			msgs = Message.messages().find("{creator: #}", id).sort("{createddate: -1}").skip(from).limit(perpage)
					.as(Message.class);
		}
		return Message.normalize(msgs);
	}

	public static List<Message> getAll() {
		MongoCursor<Message> msgs = Message.messages().find().sort("{createddate: -1}").as(Message.class);
		return Message.normalize(msgs);
	}

	public static List<Message> getLast(String last) {
		Date date = new Date(Long.valueOf(last));
		MongoCursor<Message> msgs = Message.messages().find("{createddate: { $gt: #}}", date).sort("{createddate: -1}")
				.as(Message.class);
		return Message.normalize(msgs);
	}

	private static List<Message> normalize(MongoCursor<Message> msgs) {
		List<Message> lm = new ArrayList<Message>();
		Message msg;
		while (msgs.hasNext()) {
			msg = msgs.next();
			msg.loadUser();
			lm.add(msg);
		}
		return lm;
	}
	public static MongoCursor<Message> findByMessage(String message, Integer from, Integer perpage) {
		return Message.messages().find("{message: #}", Pattern.compile(message)).sort("{createddate: -1}").skip(from)
				.limit(perpage).as(Message.class);
	}

}
