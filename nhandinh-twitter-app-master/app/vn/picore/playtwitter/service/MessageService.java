package vn.picore.playtwitter.service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.jongo.MongoCursor;

import vn.picore.playtwitter.model.Message;
import vn.picore.playtwitter.model.User;

public class MessageService {

	private ArrayList<Message> msgList;
	int counter = 1;

	private MessageService() {
		this.msgList = new ArrayList<Message>();
	}

	public void create(Message msg) {
		Message.create(msg);
	}

	public List<Message> getAll() {
		return Message.getAll();
	}
	
	public List<Message> getLast(String last) {
		return Message.getLast(last);
	}
	
	public List<Message> loadIn(List<UUID> inArr) {
		return Message.loadIn(inArr);
	}

	public List<Message> getByUsername(String username, Integer from, Integer perpage, Long timestamp) {
		User user = User.getByUsername(username);
		if (user == null)
			return null;
		return Message.getByUser(user.getId(), from, perpage, timestamp);
	}

	private static MessageService singleton = new MessageService();

	public static MessageService getInstance() {
		return singleton;
	}
	public MongoCursor<Message> findByMessage(String message, Integer from, Integer perpage) {
		return Message.findByMessage(message, from, perpage);
	}
}