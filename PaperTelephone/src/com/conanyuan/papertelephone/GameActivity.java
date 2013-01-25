package com.conanyuan.papertelephone;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;

public class GameActivity extends Activity {

	private IGame mGame;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Intent intent = getIntent();
		mGame = intent
				.getParcelableExtra(MainActivity.LocalGameListFragment.GAME_MESSAGE);
		mGame.setNextTurnView(this);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		//getMenuInflater().inflate(R.menu.activity_game, menu);
		return true;
	}

	public void returnTurn(ITurn turn) {
    	Intent intent = new Intent();
    	intent.putExtra(MainActivity.LocalGameListFragment.GAME_MESSAGE, turn);
    	setResult(Activity.RESULT_OK, intent);
    	finish();
	}

	protected int nTurns() {
		return mGame.nTurns();
	}

	protected IGame getGame() {
		return mGame;
	}

	/*
	 * @Override public boolean onOptionsItemSelected(MenuItem item) { switch
	 * (item.getItemId()) { case android.R.id.home:
	 * NavUtils.navigateUpFromSameTask(this); return true; } return
	 * super.onOptionsItemSelected(item); }
	 */
}
