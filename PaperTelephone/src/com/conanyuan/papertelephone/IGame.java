package com.conanyuan.papertelephone;

import java.io.File;
import java.io.IOException;

import org.apache.http.impl.cookie.DateParseException;

import android.os.Parcelable;

public interface IGame extends Parcelable {

	void setNextTurnView(GameActivity a);

	void toDisk(File dir) throws IOException;

	void fromDisk(File dir) throws IOException, DateParseException;

	void addTurn(ITurn turn);

	int nTurns();

	int getLayoutView();

	int getGameId();
	
	String getRootdir();
}