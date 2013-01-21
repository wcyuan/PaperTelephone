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

	/* -------- BEGIN Parcelable interface -------------- */

	protected TurnImpl(Parcel in) {
		mTimestamp = (Date) in.readSerializable();
		String name = in.readString();
		String email = in.readString();
		if (!name.equals("") && !email.equals("")) {
			mUser = User.find(name, email);
		}
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeSerializable(mTimestamp);
		if (mUser != null) {
			dest.writeString(mUser.getName());
			dest.writeString(mUser.getEmail());
		} else {
			dest.writeString("");
			dest.writeString("");
		}
	}

	/* -------- END Parcelable interface -------------- */
}
