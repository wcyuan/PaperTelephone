package com.conanyuan.papertelephone;

import java.util.ArrayList;
import java.util.List;

import android.os.Parcel;

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

	protected GameImpl(Parcel in) {
		mTurns = new ArrayList<ITurn>();
		in.readList(mTurns, getClass().getClassLoader());
	}

	public void addTurn() {
		mTurns.add(getNextTurn());
	}

	protected abstract ITurn getNextTurn();

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeList(mTurns);
	}

	@Override
	public String toString() {
		int nturns = mTurns.size();

		if (nturns == 0) {
			return "No Turns";
		} else {
			return nturns + " turns.  Last turn at: "
					+ mTurns.get(nturns - 1).getTimestamp();
		}
	}
}
