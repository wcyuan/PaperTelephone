package com.conanyuan.papertelephone;

import java.io.File;
import java.io.IOException;
import java.util.Date;

import android.os.Parcelable;

public interface IGame extends Parcelable {

	void setNextTurnView(GameActivity a);

	void toDisk() throws IOException;

	boolean fromDisk(File dir) throws IOException;

	boolean addTurn(ITurn turn);

	int nTurns();

	ITurn getTurn(int i);

	int getLayoutView();

	int getGameId();

	String getRootdir();

	Date firstTurnTimestamp();

	void markCompleted();

	boolean isCompleted();
}
