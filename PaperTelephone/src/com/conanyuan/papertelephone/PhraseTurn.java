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
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

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
	public void setEditView(final GameActivity a, final IGame g, int contentId, int doneId) {
		final EditText next = (EditText) a.findViewById(contentId);
		Button done = (Button) a.findViewById(doneId);
		final PhraseTurn turn = this;
		done.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
		    	mPhrase = next.getText().toString();
		    	g.addTurn(turn);
		    	a.returnTurn(turn);
			}
		});
	}

	/* (non-Javadoc)
	 * @see com.conanyuan.papertelephone.ITurn#toFile(java.lang.String)
	 */
	@Override
	public void contentToFile() throws IOException {
		FileOutputStream fos = new FileOutputStream(contentFilename(true));
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
		FileInputStream fis = new FileInputStream(contentFilename(false));
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
