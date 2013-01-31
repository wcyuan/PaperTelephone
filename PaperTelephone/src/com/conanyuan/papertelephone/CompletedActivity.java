package com.conanyuan.papertelephone;

import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
//import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class CompletedActivity extends GameActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_completed);
        ListView lv = (ListView) findViewById(R.id.completed_list);
        lv.setAdapter(new MyListAdapter(this));
    }

    private class MyListAdapter extends BaseAdapter {
    	//private LayoutInflater mInflater;
    	private Activity mActivity;

    	public MyListAdapter(Context context) {
    		//mInflater = LayoutInflater.from(context);
    		mActivity = (Activity)context;
    	}

		@Override
		public int getCount() {
			return getGame().nTurns();
		}

		@Override
		public Object getItem(int position) {
			return getGame().getTurn(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ITurn turn = getGame().getTurn(position);
			View view;
			if (position % 2 == 0) {
				// phrase turn
				view = new TextView(mActivity);
			} else {
				// draw turn
				view = new ImageView(mActivity);
			}
			view.setLayoutParams(new ListView.LayoutParams(
	        		ListView.LayoutParams.FILL_PARENT,
	        		ListView.LayoutParams.WRAP_CONTENT));
			turn.setReadView(mActivity, view);
			return view;
		}

    }
}
