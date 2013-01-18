package com.conanyuan.papertelephone;

import android.app.Activity;
import android.content.Intent;
import android.os.Parcel;
import android.os.Parcelable;
import android.view.KeyEvent;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

public class PhraseGame extends GameImpl {

	public PhraseGame() {
		super();
	}

	public PhraseGame(Parcel in) {
		super(in);
	}

	protected ITurn getNewTurn() {
		return new PhraseTurn();
	}
	
	@Override
	public void setNextTurnView(final Activity a) {
		a.setContentView(R.layout.phrase_turn);
		ITurn lastTurn = getPrevTurn();
		if (lastTurn != null) {
			TextView prev = (TextView) a.findViewById(R.id.prev_phrase);
			prev.setText(((PhraseTurn)lastTurn).getPhrase());
		}
		EditText next = (EditText) a.findViewById(R.id.next_phrase);
		next.setOnEditorActionListener(new OnEditorActionListener() {
			@Override
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
		    	PhraseTurn nextTurn = new PhraseTurn();
		    	nextTurn.setPhrase(v.getText());
		    	addTurn(nextTurn);
		    	Intent intent = new Intent();
		    	intent.putExtra(MainActivity.ArrayListFragment.GAME_MESSAGE, nextTurn);
		    	a.setResult(Activity.RESULT_OK, intent);
		    	a.finish();
		    	return true;
			}
		});
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
}
