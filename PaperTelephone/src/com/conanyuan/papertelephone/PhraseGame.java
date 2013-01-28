package com.conanyuan.papertelephone;

import android.os.Parcel;
import android.os.Parcelable;

public class PhraseGame extends GameImpl {

	public PhraseGame(int id, String rootdir) {
		super(id, rootdir);
	}

	@Override
	protected Class<? extends ITurn> getNextTurnClass() {
		return PhraseTurn.class;
	}

	@Override
	public int getLayoutView() {
		return R.layout.phrase_turn;
	}

	@Override
	protected int getReadViewId() {
		return R.id.prev_phrase;
	}

	@Override
	protected int getEditViewId() {
		return R.id.next_phrase;
	}

	@Override
	protected int getDoneId() {
		return R.id.button_done;
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
