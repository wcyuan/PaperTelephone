package com.conanyuan.papertelephone;

import android.os.Parcel;
import android.os.Parcelable;

public class PhraseGame extends GameImpl {

	public PhraseGame(int id, String dirname) {
		super(id, dirname);
	}

	protected ITurn getNewTurn() {
		return new PhraseTurn();
	}

	public int getLayoutView() {
		return R.layout.phrase_turn;
	}

	protected int getReadViewId() {
		return R.id.prev_phrase;
	}

	protected int getEditViewId() {
		return R.id.next_phrase;
	}

	/* -------- BEGIN Parcelable interface -------------- */

	public PhraseGame(Parcel in) {
		super(in);
	}

	public static final Parcelable.Creator<PhraseGame> CREATOR =
		new Parcelable.Creator<PhraseGame>() {  
		    
		public PhraseGame createFromParcel(Parcel in) {  
			return new PhraseGame(in);  
		}
	   
		public PhraseGame[] newArray(int size) {
			return new PhraseGame[size];  
		}
	};

	/* -------- END Parcelable interface -------------- */
}
