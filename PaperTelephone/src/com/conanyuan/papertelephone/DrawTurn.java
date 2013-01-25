package com.conanyuan.papertelephone;

import java.io.FileOutputStream;
import java.io.IOException;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;
import android.widget.ImageView;

public class DrawTurn extends TurnImpl {
	private Bitmap mBitmap;

	public DrawTurn() {
		super();
	}

	public DrawTurn(Bitmap bm) {
		this();
		mBitmap = bm;
	}

	public DrawTurn(int gameId, int nth) {
		super(gameId, nth);
	}

	public DrawTurn(Bitmap bm, int gameId, int nth) {
		this(gameId, nth);
		mBitmap = bm;
	}

	/* -------- BEGIN Parcelable interface -------------- */

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		super.writeToParcel(dest, flags);
		try {
			contentToFile();
		} catch (IOException e) {
			e.printStackTrace();
		}
		//mBitmap.writeToParcel(dest, flags);
	}

	protected DrawTurn(Parcel in) {
		super(in);
		try {
			contentFromFile();
		} catch (IOException e) {
			e.printStackTrace();
		}
		//mBitmap = Bitmap.CREATOR.createFromParcel(in);
		
	}

	public static final Parcelable.Creator<DrawTurn> CREATOR =
		new Parcelable.Creator<DrawTurn>() {  
		    
		public DrawTurn createFromParcel(Parcel in) {  
			return new DrawTurn(in);  
		}
	   
		public DrawTurn[] newArray(int size) {
			return new DrawTurn[size];  
		}
	}; 

	/* -------- END Parcelable interface -------------- */

	@Override
	public void setReadView(Activity a, int viewId) {
		ImageView prev = (ImageView) a.findViewById(viewId);
		prev.setImageBitmap(mBitmap);
	}

	@Override
	public void setEditView(GameActivity a, IGame g, int contentId, int doneId) {
		// NOP: rely on the activity
	}

	@Override
	protected void contentToFile() throws IOException {
		String fn = contentFilename();
		Log.i("DrawTurn", "writing bitmap to " + fn);
		FileOutputStream out = new FileOutputStream(fn);
		mBitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
		out.close();
	}

	@Override
	protected boolean contentFromFile() throws IOException {
		String fn = contentFilename();
		Log.i("DrawTurn", "reading bitmap from " + fn);
		mBitmap = BitmapFactory.decodeFile(fn);
		return true;
	}
}
