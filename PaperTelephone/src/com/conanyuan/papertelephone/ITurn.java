package com.conanyuan.papertelephone;

import java.io.IOException;
import java.util.Date;

import android.app.Activity;
import android.os.Parcelable;

public interface ITurn extends Parcelable {

	public abstract Date getTimestamp();

	public abstract void setReadView(final Activity a, int viewId);

	public abstract void setEditView(final GameActivity a, IGame g, int contentId, int doneId);

	public void toFile() throws IOException;

	public void delete() throws IOException;
}
