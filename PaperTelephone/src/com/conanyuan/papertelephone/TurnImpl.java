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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.impl.cookie.DateParseException;
import org.apache.http.impl.cookie.DateUtils;

import android.os.Parcel;

public abstract class TurnImpl implements ITurn {

	private User mUser;
	private final Date mTimestamp;
	private final int mGameId;
	private final int mNth;
	private final String mRootdir;

	protected TurnImpl(int gameId, int nth, String rootdir) {
		mTimestamp = new Date();
		mGameId = gameId;
		mNth = nth;
		mRootdir = rootdir;
	}

	protected TurnImpl(IGame game) {
		this(game.getGameId(), game.nTurns(), game.getRootdir());
	}

	public static class TurnParseException extends Exception {
		public TurnParseException(String string) {
			super(string);
		}

		public TurnParseException() {
			super();
		}

		private static final long serialVersionUID = -5372762648389317288L;
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

	@Override
	public int getGameId() {
		return mGameId;
	}

	@Override
	public int getNth() {
		return mNth;
	}
	/* -------- BEGIN to/from file -------------- */

	private String getdir(boolean shouldCreate) {
		String dir = mRootdir + "/" + GameImpl.gameDir(mGameId) + "/Turn-" + mNth;
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
			bufferedWriter.write(mRootdir);
			bufferedWriter.newLine();
		} finally {
			bufferedWriter.close();
		}
	}

	protected abstract void contentToFile() throws IOException;

	protected abstract boolean contentFromFile() throws IOException;

	/**
	 * Wish we could use JSON, but the JsonReader/Writer aren't available until API 11
	 * 
	 * (non-Javadoc)
	 * @see com.conanyuan.papertelephone.ITurn#fromFile(java.lang.String)
	 */
	protected TurnImpl(File dir) throws TurnParseException, IOException, DateParseException {
		// Validate the directory name
		if (!dir.isDirectory()) {
			throw new TurnParseException("Not a directory: " + dir);
		}
		Pattern p = Pattern.compile("^Turn-(\\d+)$");
		Matcher m = p.matcher(dir.getName());
		int gameId = -1;
		while (m.find()) {
			gameId = Integer.parseInt(m.group());
			break;
		}
		if (gameId < 0) {
			throw new TurnParseException("Invalid directory name: " + dir);
		}

		// Validate the expected files
		File metadataFile = new File(dir + "/Data");
		File contentFile = new File(dir + "/Content");
		for (File file : dir.listFiles()) {
			if (file.getName() == "Data") {
				metadataFile = file;
				if (!metadataFile.canRead() || !metadataFile.isFile()) {
					throw new TurnParseException("Can't read metadata: " + metadataFile);
				}
			} else if (file.getName() == "Content") {
				contentFile = file;
				if (!contentFile.canRead() || !contentFile.isFile()) {
					throw new TurnParseException("Can't read content: " + contentFile);
				}
			} else {
				// Unknown file -- delete it
				GameImpl.deleteFileRecursively(file);
			}
		}

		// Read the data from the directory
		FileInputStream fis = new FileInputStream(metadataFile);
		InputStreamReader inputStreamReader = new InputStreamReader(fis);
		BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
		try {
			String line;
			if (null == (line = bufferedReader.readLine())) {
				throw new TurnParseException("Empty file: " + metadataFile);
			}
			if (line == "USER") {
				mUser = User.find(bufferedReader.readLine());
			}
			if (null == (line = bufferedReader.readLine())) {
				throw new TurnParseException("Not enough lines: " + metadataFile);
			}
			if (line == "TIMESTAMP") {
				mTimestamp = DateUtils.parseDate(bufferedReader.readLine());
			} else {
				mTimestamp = new Date();
			}
			if (null == (line = bufferedReader.readLine())) {
				throw new TurnParseException("Not enough lines: " + metadataFile);
			}
			mGameId = Integer.parseInt(line);
			if (mGameId != gameId) {
				throw new TurnParseException("Game id from directory name (" + gameId +
						") doesn't match game id from file (" + mGameId + ")");
			}
			if (null == (line = bufferedReader.readLine())) {
				throw new TurnParseException("Not enough lines: " + metadataFile);
			}
			mNth = Integer.parseInt(line);
			if (null == (line = bufferedReader.readLine())) {
				throw new TurnParseException("Not enough lines: " + metadataFile);
			}
			mRootdir = line;
		} finally {
			bufferedReader.close();
		}
		contentFromFile();
	}

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
		mRootdir = in.readString();
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
		dest.writeString(mRootdir);
	}

	/* -------- END Parcelable interface -------------- */
}
