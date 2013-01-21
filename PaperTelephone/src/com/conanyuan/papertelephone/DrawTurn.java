package com.conanyuan.papertelephone;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Parcel;
import android.os.Parcelable;

public class DrawTurn extends TurnImpl {
	private Bitmap mBitmap;

	/* -------- BEGIN Parcelable interface -------------- */

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		super.writeToParcel(dest, flags);
		mBitmap.writeToParcel(dest, flags);
	}

	protected DrawTurn(Parcel in) {
		super(in);
		mBitmap = Bitmap.CREATOR.createFromParcel(in);
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
		// TODO Auto-generated method stub

	}

	@Override
	public void setEditView(GameActivity a, IGame g, int viewId) {
		// TODO Auto-generated method stub

	}

	@Override
	public void toFile(File file) throws IOException {
		FileOutputStream out = new FileOutputStream(file);
		mBitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
	}

	@Override
	public void fromFile(File file) throws IOException {
		mBitmap = BitmapFactory.decodeFile(file.getPath());
	}
}
