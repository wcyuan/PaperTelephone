package com.conanyuan.papertelephone;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import android.app.Activity;
import android.os.Parcel;
import android.os.Parcelable;
import android.view.KeyEvent;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

public class PhraseTurn extends TurnImpl {
	private String mPhrase;

	public PhraseTurn() {
		super();
	}

	public PhraseTurn(int gameId, int nth) {
		super(gameId, nth);
	}

	@Override
	public void setReadView(final Activity a, int viewId) {
		TextView prev = (TextView) a.findViewById(viewId);
		prev.setText(mPhrase);
	}

	@Override
	public void setEditView(final GameActivity a, final IGame g, int viewId) {
		EditText next = (EditText) a.findViewById(viewId);
		final PhraseTurn turn = this;
		next.setOnEditorActionListener(new OnEditorActionListener() {
			@Override
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
		    	mPhrase = v.getText().toString();
		    	g.addTurn(turn);
		    	a.returnTurn(turn);
		    	return true;
			}
		});
	}

	/* (non-Javadoc)
	 * @see com.conanyuan.papertelephone.ITurn#toFile(java.lang.String)
	 */
	@Override
	public void contentToFile() throws IOException {
		FileOutputStream fos = new FileOutputStream(contentFilename());
		OutputStreamWriter outputStreamWriter = new OutputStreamWriter(fos);
		BufferedWriter bufferedWriter = new BufferedWriter(outputStreamWriter);
		try {
			bufferedWriter.write(mPhrase);
		} finally {
			bufferedWriter.close();
		}
	}

	/* (non-Javadoc)
	 * @see com.conanyuan.papertelephone.ITurn#fromFile(java.lang.String)
	 */
	@Override
	public boolean contentFromFile() throws IOException {
		FileInputStream fis = new FileInputStream(contentFilename());
		InputStreamReader inputStreamReader = new InputStreamReader(fis);
		BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
		try {
			StringBuilder sb = new StringBuilder();
			String line;
			while ((line = bufferedReader.readLine()) != null) {
				sb.append(line).append("\n");
			}
			mPhrase = sb.toString();
		} finally {
			bufferedReader.close();
		}
		return true;
	}

	/* -------- BEGIN Parcelable interface -------------- */

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		super.writeToParcel(dest, flags);
		dest.writeString(mPhrase);
	}

	protected PhraseTurn(Parcel in) {
		super(in);
		mPhrase = in.readString();
	}

	public static final Parcelable.Creator<PhraseTurn> CREATOR =
		new Parcelable.Creator<PhraseTurn>() {

		public PhraseTurn createFromParcel(Parcel in) {  
			return new PhraseTurn(in);  
		}

		public PhraseTurn[] newArray(int size) {
			return new PhraseTurn[size];  
		}
	};

	/* -------- END Parcelable interface -------------- */
}
