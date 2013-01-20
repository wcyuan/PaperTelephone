package com.conanyuan.papertelephone;

import java.io.File;
import java.io.IOException;

import android.os.Parcelable;

public interface IGame extends Parcelable {
	
	public void setNextTurnView(GameActivity a);

	public void toDisk(File dir) throws IOException;

	public void fromDisk(File dir) throws IOException;

	public void addTurn(ITurn turn);
}