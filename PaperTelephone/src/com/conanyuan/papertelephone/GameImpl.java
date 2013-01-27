package com.conanyuan.papertelephone;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.http.impl.cookie.DateParseException;

//import android.app.Activity;
import android.os.Parcel;
import android.view.View;
import android.widget.TextView;

/**
 * The Game object.
 * 
 * @author Yuan
 */
public abstract class GameImpl implements IGame {
	private List<ITurn> mTurns;
	private int mGameId;
	private String mDirname;

	protected GameImpl(int gameId, String dirname) {
		mTurns = new ArrayList<ITurn>();
		mGameId = gameId;
		mDirname = dirname;
	}

	@Override
	public void addTurn(ITurn turn) {
		turn.setGameInfo(mGameId, mTurns.size(), mDirname);
		mTurns.add(turn);
	}

	abstract protected ITurn getNewTurn();

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
			TextView next_label = (TextView)a.findViewById(R.id.next_turn_label);
			next_label.setText("First Turn:");
		} else {
			lastTurn.setReadView(a, getReadViewId());
		}
		getNewTurn().setEditView(a, this, getEditViewId(), getDoneId());
	}

	public static String gameDir(int id) {
		return "Game-" + id;
	}

	/* (non-Javadoc)
	 * @see com.conanyuan.papertelephone.IGame#toFile(java.io.File)
	 * TODO:
	 * only write turns to disk that aren't already there
	 * don't delete files before writing
	 * don't need to be given the file to write to
	 */
	@Override
	public void toDisk(File dir) throws IOException {
		dir.mkdirs();
		for (File file : dir.listFiles()) {
			file.delete();
		}
		for (int ii = 0; ii < mTurns.size(); ii++) {
			mTurns.get(ii).toFile();
		}
	}

	/* (non-Javadoc)
	 * @see com.conanyuan.papertelephone.IGame#fromFile(java.io.File)
	 * 
	 * TODO:
	 * given a directory:
	 * parse the name of the directory to get the game id
	 * if the name doesn't match, return false
	 *   (directory is not valid, caller should remove this directory)
	 * loop through all contents of the directory, sorted
	 * foreach file, try to create a turn
	 *   if we can't create a turn from it, (Turn.fromFile returns false)
	 *     delete the directory for that turn
	 *   if we can create a turn, validate it:
	 *     make sure the turn has the right number
	 *     make sure the turn has the right type
	 *     otherwise, delete the turn directory
	 * if no turns, return false (parent will delete the game)
	 */
	@Override
	public void fromDisk(File dir) throws IOException, DateParseException {
		
		File[] files = dir.listFiles();
		Arrays.sort(files);
		for (File file : files) {
			ITurn turn = getNewTurn();
			if (turn.fromFile(file)) {
				mTurns.add(turn);
			}
		}
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
	public int nTurns() {
		return mTurns.size();
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
		mDirname = in.readString();
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
		dest.writeString(mDirname);
		dest.writeList(mTurns);
	}

	/* -------- END Parcelable interface -------------- */
}
