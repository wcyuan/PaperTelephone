package com.conanyuan.papertelephone;

public class User {
	private String mName;
	private String mEmail;
	private int mId;

	public User(String name, String email, int id) {
		super();
		mName = name;
		mEmail = email;
		mId = id;
	}

	public String getName() {
		return mName;
	}

	public void setName(String name) {
		mName = name;
	}

	public String getEmail() {
		return mEmail;
	}

	public void setEmail(String email) {
		mEmail = email;
	}

	public int getId() {
		return mId;
	}

	public void setmId(int id) {
		mId = id;
	}
}
