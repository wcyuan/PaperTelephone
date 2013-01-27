package com.conanyuan.papertelephone;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
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

	private String getdir(boolean shouldCreate) {
		String dir = mDirname + "/" + GameImpl.gameDir(mGameId) + "/Turn-" + mNth;
		if (shouldCreate) {
			(new File(dir)).mkdirs();
		}
		return dir;
	}

	protected String metadataFilename(boolean shouldCreate) {
		return getdir(shouldCreate) + "/Data";
	}

	protected String contentFilename(boolean shouldCreate) {
		return getdir(shouldCreate) + "/Content";
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
	 * 
	 * TODO:
	 * given the name of a turn directory:
	 * make sure the directory has the right form of name, otherwise return false
	 * Make sure a metadata file and a content file
	 *   otherwise, return false
	 * delete any other files
	 * try to read both metadata and content
	 */
	@Override
	public boolean fromFile(File dir) throws IOException,
			DateParseException {
		return metadataFromFile(dir + "/Data") && contentFromFile();
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
		FileOutputStream fos = new FileOutputStream(metadataFilename(true));
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
			String line;
			if (null == (line = bufferedReader.readLine())) {
				return false;
			}
			if (line == "USER") {
				mUser = User.find(bufferedReader.readLine());
			}
			if (null == (line = bufferedReader.readLine())) {
				return false;
			}
			if (line == "TIMESTAMP") {
				mTimestamp = DateUtils.parseDate(bufferedReader.readLine());
			}
			if (null == (line = bufferedReader.readLine())) {
				return false;
			}
			mGameId = Integer.parseInt(line);
			if (null == (line = bufferedReader.readLine())) {
				return false;
			}
			mNth = Integer.parseInt(line);
			if (null == (line = bufferedReader.readLine())) {
				return false;
			}
			mDirname = line;
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
