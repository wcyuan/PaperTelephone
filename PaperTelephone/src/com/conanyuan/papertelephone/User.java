package com.conanyuan.papertelephone;

import java.util.HashMap;
import java.util.Map;

public class User {
	private String mName;
	private String mEmail;
	private static Map<String, User> Users = new HashMap<String, User>();

	public static User find(String string) {
		int sep = string.indexOf("\n");
		int len = string.length();
		String name;
		String email;
		if (sep < 0) {
			name = string;
			email = "";
		} else {
			name = string.substring(0, sep);
			if (len >= sep) {
				email = string.substring(sep+1);
			} else {
				email = "";
			}
		}
		return find(name, email);
	}

	public static User find(String name, String email) {
		if (Users.containsKey(email)) {
			return Users.get(email);
		} else {
			User user = new User(name, email);
			Users.put(email, user);
			return user;
		}
	}

	private User(String name, String email) {
		super();
		mName = name;
		mEmail = email;
	}

	public String getName() {
		return mName;
	}

	public String getEmail() {
		return mEmail;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return mName + "\n" + mEmail + "\n";
	}
}
