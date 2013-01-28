package com.conanyuan.papertelephone;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.text.ParseException;

import android.app.Activity;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class PhraseTurn extends TurnImpl {
	private String mPhrase;

	public PhraseTurn(IGame game) {
		super(game);
	}

	public PhraseTurn(File dir) throws TurnParseException, IOException, ParseException {
		super(dir);
	}

	@Override
	public void setReadView(final Activity a, int viewId) {
		TextView prev = (TextView) a.findViewById(viewId);
		prev.setText(mPhrase);
	}

	@Override
	public void setEditView(final GameActivity a, int contentId, int doneId) {
		final EditText next = (EditText) a.findViewById(contentId);
		Button done = (Button) a.findViewById(doneId);
		final PhraseTurn turn = this;
		done.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
		    	mPhrase = next.getText().toString();
		    	a.returnTurn(turn);
			}
		});
	}

	/* (non-Javadoc)
	 * @see com.conanyuan.papertelephone.ITurn#toFile(java.lang.String)
	 */
	@Override
	public void contentToFile() throws IOException {
		String fn = contentFilename(true);
		Log.i("DrawTurn", "writing phrase to " + fn);
		FileOutputStream fos = new FileOutputStream(fn);
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
		String fn = contentFilename(false);
		Log.i("DrawTurn", "reading phrase from " + fn);
		FileInputStream fis = new FileInputStream(fn);
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
