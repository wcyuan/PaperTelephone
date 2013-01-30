package com.conanyuan.papertelephone;

import java.util.ArrayList;

import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.support.v4.app.NavUtils;

public class CompletedActivity extends GameActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_completed);
        ListView lv = (ListView) findViewById(R.id.completed_list);
        lv.setAdapter(new MyListAdapter(this));
    }

    private class MyListAdapter extends BaseAdapter {
    	private LayoutInflater mInflater;

    	public MyListAdapter(Context context) {
    		mInflater = LayoutInflater.from(context);
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
			// TODO Auto-generated method stub
			return null;
		}

    }
}
