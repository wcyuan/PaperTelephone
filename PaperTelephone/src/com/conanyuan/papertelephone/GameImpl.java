package com.conanyuan.papertelephone;

import java.util.ArrayList;
import java.util.List;

//import android.app.Activity;
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

	public void addTurn(ITurn turn) {
		mTurns.add(turn);
	}

	protected abstract ITurn getNextTurn();

	/*
	@Override
	public void setNextTurnView(Activity a) {
		// Every turn should have a View for showing the content and editing the
		// content. For example, a PhraseTurn shows content with a TextView and
		// edits it with a EditText.
		//
		// The Game should just find the two views, and show them in a linear
		// layout, along with text that shows which number turn we're on.
		// Need to set the layout params
		//getNextTurn().setView(a);
		int nturns = mTurns.size();
		if (nturns > 0) {
			mTurns.get(nturns-1).getViewId();
		}
		getNextTurn().getEditId();
		a.setContentView(R.layout.phrase_turn);
	}
	 */

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
			return "New Game";
		} else {
			return nturns + " turns.  Last turn at: "
					+ mTurns.get(nturns - 1).getTimestamp();
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
}
