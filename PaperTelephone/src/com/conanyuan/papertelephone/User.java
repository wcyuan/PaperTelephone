package com.conanyuan.papertelephone;

import java.util.HashMap;
import java.util.Map;

public class User {
	private String mName;
	private String mEmail;
	private static Map<String, User> Users = new HashMap<String, User>();

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
}
