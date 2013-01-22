package com.conanyuan.papertelephone;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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

	protected GameImpl() {
		mTurns = new ArrayList<ITurn>();
	}

	@Override
	public void addTurn(ITurn turn) {
		mTurns.add(turn);
	}

	abstract protected ITurn getNewTurn();

	abstract protected int getReadViewId();

	abstract protected int getEditViewId();

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
		getNewTurn().setEditView(a, this, getEditViewId());
	}

	/* (non-Javadoc)
	 * @see com.conanyuan.papertelephone.IGame#toFile(java.io.File)
	 */
	@Override
	public void toDisk(File dir) throws IOException {
		dir.mkdirs();
		for (File file : dir.listFiles()) {
			file.delete();
		}
		for (int ii = 0; ii < mTurns.size(); ii++) {
			mTurns.get(ii).toFile(new File(dir, "Turn-" + ii));
		}
	}

	/* (non-Javadoc)
	 * @see com.conanyuan.papertelephone.IGame#fromFile(java.io.File)
	 */
	@Override
	public void fromDisk(File dir) throws IOException {
		File[] files = dir.listFiles();
		Arrays.sort(files);
		for (File file : files) {
			ITurn turn = getNewTurn();
			turn.fromFile(file);
			mTurns.add(turn);
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
		mTurns = new ArrayList<ITurn>();
		in.readList(mTurns, getClass().getClassLoader());
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeList(mTurns);
	}

	/* -------- END Parcelable interface -------------- */
}
