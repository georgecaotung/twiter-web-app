package vn.picore.playtwitter.msgprocessor;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import vn.picore.playtwitter.service.UserService;

public class MessageProcessor {

	private static Pattern hashTagPattern = Pattern.compile("\\S*#(?:\\[[^\\]]+\\]|\\S+)");

	public static List<String> getTag(String msg) {
		List<String> list = new ArrayList<String>();
		Matcher matcher = hashTagPattern.matcher(msg);
		while (matcher.find()) {
			list.add(matcher.group().substring(1));
		}
		return list;
	}

	public static String generateUsername(String email) {
		int pos = email.indexOf("@");
		String username = email.substring(0, pos);
		Integer counter = 0;
		while (UserService.getInstance().getByUsername(username) != null) {
			counter++;
			username = username + counter;
		}
		return username;

	}
}
