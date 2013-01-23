package com.conanyuan.papertelephone;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.Date;

import org.apache.http.impl.cookie.DateParseException;
import org.apache.http.impl.cookie.DateUtils;

import android.os.Parcel;

public abstract class TurnImpl implements ITurn {

	private User mUser;
	private Date mTimestamp;
	private int mGameId;
	private int mNth;
	private String mDirname;

	protected TurnImpl() {
		super();
		mTimestamp = new Date();
	}

	protected TurnImpl(int gameId, int nth) {
		this();
		mGameId = gameId;
		mNth = nth;
	}

	@Override
	public void setGameInfo(int gameId, int nth, String dirname) {
		mGameId = gameId;
		mNth = nth;
		mDirname = dirname;
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

	/* -------- BEGIN to/from file -------------- */

	protected String metadataFilename() {
		return mDirname + "/Turn-" + mGameId + "." + mNth;
	}

	protected String contentFilename() {
		return mDirname + "/TurnContent-" + mGameId + "." + mNth;
	}

	/* (non-Javadoc)
	 * @see com.conanyuan.papertelephone.ITurn#toFile()
	 */
	@Override
	public void toFile() throws IOException {
		metadataToFile();
		contentToFile();
	}

	/* (non-Javadoc)
	 * @see com.conanyuan.papertelephone.ITurn#fromFile(java.lang.String)
	 */
	@Override
	public boolean fromFile(String filename) throws IOException,
			DateParseException {
		return metadataFromFile(filename) && contentFromFile();
	}

	/* (non-Javadoc)
	 * @see com.conanyuan.papertelephone.ITurn#delete()
	 */
	@Override
	public void delete() throws IOException {
		// TODO Auto-generated method stub
	}

	/* (non-Javadoc)
	 * @see com.conanyuan.papertelephone.ITurn#toFile(java.lang.String)
	 *
	 * Wish we could use JSON, but the JsonReader/Writer aren't available until API 11
	 */
	private void metadataToFile() throws IOException {
		FileOutputStream fos = new FileOutputStream(metadataFilename());
		OutputStreamWriter outputStreamWriter = new OutputStreamWriter(fos);
		BufferedWriter bufferedWriter = new BufferedWriter(outputStreamWriter);
		try {
			if (mUser == null) {
				bufferedWriter.write("NO USER\n");
				bufferedWriter.newLine();
			} else {
				bufferedWriter.write("USER");
				bufferedWriter.newLine();
				bufferedWriter.write(mUser.toString());
				bufferedWriter.newLine();
			}
			if (mTimestamp == null) {
				bufferedWriter.write("NO TIMESTAMP");
				bufferedWriter.newLine();
			} else {
				bufferedWriter.write("TIMESTAMP");
				bufferedWriter.newLine();
				bufferedWriter.write(mTimestamp.toString());
				bufferedWriter.newLine();
			}
			bufferedWriter.write(Integer.toString(mGameId));
			bufferedWriter.newLine();
			bufferedWriter.write(Integer.toString(mNth));
			bufferedWriter.newLine();
			bufferedWriter.write(mDirname);
			bufferedWriter.newLine();
		} finally {
			bufferedWriter.close();
		}
	}

	/* (non-Javadoc)
	 * @see com.conanyuan.papertelephone.ITurn#fromFile(java.lang.String)
	 * 
	 * Wish we could use JSON, but the JsonReader/Writer aren't available until API 11
	 */
	private boolean metadataFromFile(String filename) throws IOException, DateParseException {
		FileInputStream fis = new FileInputStream(filename);
		InputStreamReader inputStreamReader = new InputStreamReader(fis);
		BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
		try {
			if (bufferedReader.readLine() == "USER") {
				mUser = User.find(bufferedReader.readLine());
			}
			if (bufferedReader.readLine() == "TIMESTAMP") {
				mTimestamp = DateUtils.parseDate(bufferedReader.readLine());
			}
			mGameId = Integer.parseInt(bufferedReader.readLine());
			mNth = Integer.parseInt(bufferedReader.readLine());
			mDirname = bufferedReader.readLine();
		} finally {
			bufferedReader.close();
		}
		return true;
	}

	protected abstract void contentToFile() throws IOException;

	protected abstract boolean contentFromFile() throws IOException;

	/* -------- END to/from file -------------- */

	/* -------- BEGIN Parcelable interface -------------- */

	protected TurnImpl(Parcel in) {
		mTimestamp = (Date) in.readSerializable();
		String name = in.readString();
		String email = in.readString();
		if (!name.equals("") && !email.equals("")) {
			mUser = User.find(name, email);
		}
		mGameId = in.readInt();
		mNth = in.readInt();
		mDirname = in.readString();
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
		dest.writeInt(mGameId);
		dest.writeInt(mNth);
		dest.writeString(mDirname);
	}

	/* -------- END Parcelable interface -------------- */
}
