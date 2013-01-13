package com.conanyuan.papertelephone;

import java.util.Date;

import android.os.Parcel;

public abstract class TurnImpl implements ITurn {
	private User mUser;
	private Date mTimestamp;

	protected TurnImpl() {
		super();
		mTimestamp = new Date();
	}

	protected TurnImpl(Parcel in) {
		mTimestamp = (Date) in.readSerializable();
		String name = in.readString();
		String email = in.readString();
		mUser = User.find(name, email);
	}

	public User getUser() {
		return mUser;
	}

	public void setUser(User user) {
		mUser = user;
	}

	@Override
	public Date getTimestamp() {
		return mTimestamp;
	}

	public void setTimestamp(Date timestamp) {
		mTimestamp = timestamp;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeSerializable(mTimestamp);
		dest.writeString(mUser.getName());
		dest.writeString(mUser.getEmail());
	}
}
