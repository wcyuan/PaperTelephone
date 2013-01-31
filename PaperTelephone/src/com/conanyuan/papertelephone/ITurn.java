package com.conanyuan.papertelephone;

import java.io.IOException;
import java.util.Date;

import android.app.Activity;
import android.os.Parcelable;
import android.view.View;

public interface ITurn extends Parcelable {

	Date getTimestamp();

	void setReadView(final Activity a, View view);

	void setEditView(final GameActivity a, View contentView, View doneView);

	void toFile() throws IOException;

	int getGameId();

	int getNth();
}
