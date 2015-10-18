package vn.picore.playtwitter.msgprocessor;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.jongo.MongoCursor;

import vn.picore.playtwitter.model.Message;

public class SweeterUtils {
	public static String generateRandom(int length) {
		Random random = new Random();
		char[] digits = new char[length];
		digits[0] = (char) (random.nextInt(9) + '1');
		for (int i = 1; i < length; i++) {
			digits[i] = (char) (random.nextInt(10) + '0');
		}
		return new String(digits);
	}
	
}
