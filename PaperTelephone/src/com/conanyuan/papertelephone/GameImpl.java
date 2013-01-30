package com.conanyuan.papertelephone;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.conanyuan.papertelephone.TurnImpl.TurnParseException;

//import android.app.Activity;
import android.os.Parcel;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

/**
 * The Game object.
 * 
 * @author Yuan
 */
public abstract class GameImpl implements IGame {
	private final List<ITurn> mTurns;
	private final int mGameId;
	private final String mRootdir;
	private boolean isCompleted = false;

	protected GameImpl(int gameId, String dirname) {
		mTurns = new ArrayList<ITurn>();
		mGameId = gameId;
		mRootdir = dirname;
	}

	/* Gettors for Turn */

	public static String gameDir(int id) {
		return "Game-" + id;
	}

	public static int parseGameDir(String dir) {
		Pattern p = Pattern.compile("^Game-(\\d+)$");
		Matcher m = p.matcher(dir);
		int gameId = -1;
		while (m.find()) {
			gameId = Integer.parseInt(m.group(1));
			break;
		}
		return gameId;
	}

	@Override
	public int nTurns() {
		return mTurns.size();
	}

	/* (non-Javadoc)
	 * @see com.conanyuan.papertelephone.IGame#getTurn(int)
	 */
	@Override
	public ITurn getTurn(int i) {
		return mTurns.get(i);
	}

	/* (non-Javadoc)
	 * @see com.conanyuan.papertelephone.IGame#markCompleted()
	 */
	@Override
	public void markCompleted() {
		isCompleted = true;
	}

	/* (non-Javadoc)
	 * @see com.conanyuan.papertelephone.IGame#isCompleted()
	 */
	@Override
	public boolean isCompleted() {
		return isCompleted;
	}

	@Override
	public int getGameId() {
		return mGameId;
	}

	@Override
	public String getRootdir() {
		return mRootdir;
	}

	@Override
	public boolean addTurn(ITurn turn) {
		if (turn.getGameId() != mGameId) {
			Log.e("add turn", "Turn has wrong game id " + turn.getGameId() + " instead of " + mGameId);
			return false;
		}
		if (turn.getNth() != mTurns.size()) {
			Log.e("add turn", "Turn has wrong game id " + turn.getNth() + " instead of " + mTurns.size());
			return false;
		}
		if (!getNextTurnClass().isInstance(turn)) {
			Log.e("add turn", "Turn has wrong class " + turn.getClass() + " instead of " + getNextTurnClass());
			return false;
		}
		mTurns.add(turn);
		return true;
	}

	/* Gettors for GameActivity */

	abstract protected Class<? extends ITurn> getNextTurnClass();

	protected ITurn getNewTurn() {
		try {
			return getNextTurnClass().getConstructor(IGame.class).newInstance(this);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		}
		return new PhraseTurn(this);
	}

	protected ITurn getNewTurn(File dir) throws TurnParseException,
			IOException, ParseException
	{
		try {
			return getNextTurnClass().getConstructor(File.class).newInstance(dir);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		}
		return new PhraseTurn(dir);
	}

	abstract protected int getReadViewId();

	abstract protected int getEditViewId();

	abstract protected int getDoneId();

	@Override
	public void setNextTurnView(final GameActivity a) {
		a.setContentView(getLayoutView());
		ITurn lastTurn = getPrevTurn();
		if (lastTurn == null) {
			a.findViewById(R.id.prev_turn_label).setVisibility(View.GONE);
			a.findViewById(getReadViewId()).setVisibility(View.GONE);
			TextView next_label = (TextView) a
					.findViewById(R.id.next_turn_label);
			next_label.setText("First Turn:");
		} else {
			lastTurn.setReadView(a, getReadViewId());
		}
		getNewTurn().setEditView(a, getEditViewId(), getDoneId());
	}

	/**
	 * Recursively delete a file or directory
	 */
	public static void deleteFileRecursively(File f) throws IOException {
		if (f.isDirectory()) {
			for (File c : f.listFiles())
				deleteFileRecursively(c);
		}
		Log.i("GameImpl recursive delete", "Deleting file " + f);
		if (!f.delete()) {
			throw new FileNotFoundException("Failed to delete file: " + f);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.conanyuan.papertelephone.IGame#toFile(java.io.File)
	 */
	@Override
	public void toDisk() throws IOException {
		File dir = new File(mRootdir + "/" + gameDir(mGameId));
		if (dir.exists() && !dir.isDirectory()) {
			Log.w("GameImpl.toDisk", "Not a directory, deleting " + dir);
			deleteFileRecursively(dir);
		}
		dir.mkdirs();
		for (int ii = 0; ii < mTurns.size(); ii++) {
			mTurns.get(ii).toFile();
		}
		if (isCompleted) {
			File complete = new File(dir + "/complete");
			complete.createNewFile();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.conanyuan.papertelephone.IGame#fromDisk(java.io.File)
	 * 
	 * Given a directory, parse the name of the directory to get the game id 
	 * if the name doesn't match, return false
	 * (directory is not valid, caller should remove this directory)
	 * loop through all contents of the directory, sorted foreach file
	 *   try to create a turn
	 *   if we can't create a turn from it, (Turn.fromFile returns false)
	 *     delete the directory for that turn
	 *   if we can create a turn, validate it:
	 *     make sure the turn has the right number make sure the turn has the right
	 *     type otherwise, delete the turn directory if no turns, return false
	 *     (parent will delete the game)
	 */
	@Override
	public boolean fromDisk(File dir) throws IOException {
		if (!dir.exists()) {
			Log.w("GameImpl.fromDisk", "Doesn't exist: " + dir);
			return false;
		}
		if (!dir.isDirectory()) {
			Log.w("GameImpl.fromDisk", "Deleting, not a directory: " + dir);
			deleteFileRecursively(dir);
			return false;
		}
		if (mGameId != parseGameDir(dir.getName())) {
			Log.w("GameImpl.fromDisk", "Deleting " + dir + " doesn't match id " + mGameId);
			deleteFileRecursively(dir);
			return false;
		}
		File[] files = dir.listFiles();
		Arrays.sort(files);
		for (File file : files) {
			if (file.getName().equals("complete")) {
				isCompleted = true;
				continue;
			}
			ITurn turn;
			try {
				turn = getNewTurn(file);
				if (!addTurn(turn)) {
					Log.w("GameImpl.fromDisk", "Failed to add turn, deleting " + file);
					deleteFileRecursively(file);
				}
			} catch (TurnParseException e) {
				e.printStackTrace();
				// Not a valid turn, delete it.
				Log.w("GameImpl.fromDisk", "Invalid turn, deleting " + file);
				deleteFileRecursively(file);
			} catch (IOException e) {
				e.printStackTrace();
				Log.w("GameImpl.fromDisk", "Can't read turn, deleting " + file);
				deleteFileRecursively(file);
			} catch (ParseException e) {
				e.printStackTrace();
				Log.w("GameImpl.fromDisk", "Invalid date in turn, deleting " + file);
				deleteFileRecursively(file);
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
				Log.w("GameImpl.fromDisk", "Invalid format in turn, deleting " + file);
				deleteFileRecursively(file);
			}
		}
		if (mTurns.size() == 0) {
			// Can't delete zero-turned games: when we return to the 
			// MainActivity with our first turn, the game file will have zero
			// turns but we don't want to delete it...

			//Log.w("GameImpl.fromDisk", "No turns, deleting " + file);
			//deleteFileRecursively(dir);
			//return false;
		}
		return true;
	}

	@Override
	public String toString() {
		int nturns = mTurns.size();

		if (nturns == 0) {
			return "New Game";
		} else {
			return nturns + " turns.  Last turn at: "
					+ mTurns.get(nturns - 1).getTimestamp();
		}
	}

	@Override
	public Date firstTurnTimestamp() {
		if (mTurns.size() == 0) {
			return new Date();
		} else {
			return mTurns.get(0).getTimestamp();
		}
	}

	protected ITurn getPrevTurn() {
		int nturns = mTurns.size();

		if (nturns == 0) {
			return null;
		} else {
			return mTurns.get(nturns - 1);
		}
	}

	/* -------- BEGIN Parcelable interface -------------- */

	protected GameImpl(Parcel in) {
		mGameId = in.readInt();
		mRootdir = in.readString();
		mTurns = new ArrayList<ITurn>();
		in.readList(mTurns, getClass().getClassLoader());
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(mGameId);
		dest.writeString(mRootdir);
		dest.writeList(mTurns);
	}

	/* -------- END Parcelable interface -------------- */

	public static class ByTimestamp implements Comparator<IGame> {
		@Override
		public int compare(IGame object1, IGame object2) {
			return object1.firstTurnTimestamp().compareTo(object2.firstTurnTimestamp());
		}
	}
}
