package com.conanyuan.papertelephone;

import android.app.Activity;
import android.os.Parcelable;

public interface IGame extends Parcelable {
	
	public abstract void setNextTurnView(Activity a);

}