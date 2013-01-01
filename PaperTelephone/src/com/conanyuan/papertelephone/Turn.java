package com.conanyuan.papertelephone;

import java.util.Date;

public class Turn {
	private User mUser;
	private Date mTimestamp;

	public Turn(User user, Date timestamp) {
		super();
		mUser = user;
		mTimestamp = timestamp;
	}

	public User getUser() {
		return mUser;
	}

	public void setUser(User user) {
		mUser = user;
	}

	public Date getTimestamp() {
		return mTimestamp;
	}

	public void setTimestamp(Date timestamp) {
		mTimestamp = timestamp;
	}
}
