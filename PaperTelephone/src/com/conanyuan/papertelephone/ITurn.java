package com.conanyuan.papertelephone;

import java.util.Date;

import android.os.Parcelable;

public interface ITurn extends Parcelable {

	public abstract Date getTimestamp();

	public abstract int getViewId();

	public abstract int getEditId();

}