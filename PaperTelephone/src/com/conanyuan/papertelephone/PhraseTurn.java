package com.conanyuan.papertelephone;

import android.os.Parcel;
import android.os.Parcelable;

public class PhraseTurn extends TurnImpl {
	private String mPhrase;

	public PhraseTurn() {
		super();
	}

	public String getPhrase() {
		return mPhrase;
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
