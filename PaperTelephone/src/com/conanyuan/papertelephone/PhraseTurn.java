package com.conanyuan.papertelephone;

import android.os.Parcel;
import android.os.Parcelable;

public class PhraseTurn extends TurnImpl {
	private String mPhrase;

	public PhraseTurn() {
		super();
	}

	@Override
	public int getViewId() {
		return R.layout.phrase_turn;
	}

	@Override
	public int getEditId() {
		return R.layout.phrase_turn;
	}

	public String getPhrase() {
		return mPhrase;
	}
	
	public void setPhrase(CharSequence str) {
		mPhrase = str.toString();
	}

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
}
