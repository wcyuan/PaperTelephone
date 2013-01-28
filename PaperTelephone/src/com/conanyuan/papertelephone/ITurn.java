package com.conanyuan.papertelephone;

import java.io.IOException;
import java.util.Date;

import android.app.Activity;
import android.os.Parcelable;

public interface ITurn extends Parcelable {

	Date getTimestamp();

	void setReadView(final Activity a, int viewId);

	void setEditView(final GameActivity a, int contentId, int doneId);

	void toFile() throws IOException;

	int getGameId();

	int getNth();
}
