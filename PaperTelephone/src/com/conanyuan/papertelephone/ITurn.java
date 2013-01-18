package com.conanyuan.papertelephone;

import java.io.File;
import java.io.IOException;
import java.util.Date;

import android.os.Parcelable;

public interface ITurn extends Parcelable {

	public abstract Date getTimestamp();

	public abstract int getViewId();

	public abstract int getEditId();

	public void toFile(File file) throws IOException;

	public void fromFile(File file) throws IOException;
}
