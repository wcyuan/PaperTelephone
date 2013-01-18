package com.conanyuan.papertelephone;

import java.io.File;
import java.io.IOException;

import android.app.Activity;
import android.os.Parcelable;

public interface IGame extends Parcelable {
	
	public abstract void setNextTurnView(Activity a);

	public void toDisk(File dir) throws IOException;

	public void fromDisk(File dir) throws IOException;

	public void addTurn(ITurn turn);
}