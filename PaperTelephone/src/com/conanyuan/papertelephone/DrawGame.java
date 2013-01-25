package com.conanyuan.papertelephone;

import android.os.Parcel;
import android.os.Parcelable;
import android.view.View;
import android.widget.TextView;

public class DrawGame extends GameImpl {

	public DrawGame(int id, String dirname) {
		super(id, dirname);
	}

	@Override
	protected ITurn getNewTurn() {
		if (nTurns() % 2 == 0) {
			return new PhraseTurn();
		} else {
			return new DrawTurn();
		}
	}

	@Override
	public void setNextTurnView(final GameActivity a) {
		super.setNextTurnView(a);
		if (nTurns() % 2 == 1) {
			// Hack.  Because, to add a new drawing, we are using a
			// phrase_turn layout, then adding a drawing canvas.
			// So we have to get rid of the new phrase box.
			a.findViewById(getEditViewId()).setVisibility(View.GONE);
			TextView next_label = (TextView)a.findViewById(R.id.next_turn_label);
			next_label.setText("Draw:");
			a.findViewById(getDoneId()).setVisibility(View.GONE);
		}
	}

	@Override
	public int getLayoutView() {
		if (nTurns() % 2 == 0) {
			return R.layout.draw_game_phrase;
		} else {
			// sort of a hack...
			return R.layout.phrase_turn;
		}
	}

	@Override
	protected int getReadViewId() {
		if (nTurns() % 2 == 0) {
			return R.id.prev_drawing;
		} else {
			return R.id.prev_phrase;
		}
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

	public DrawGame(Parcel in) {
		super(in);
	}

	public static final Parcelable.Creator<DrawGame> CREATOR =
		new Parcelable.Creator<DrawGame>() {  
		    
		public DrawGame createFromParcel(Parcel in) {  
			return new DrawGame(in);  
		}
	   
		public DrawGame[] newArray(int size) {
			return new DrawGame[size];  
		}
	};

	/* -------- END Parcelable interface -------------- */
}
