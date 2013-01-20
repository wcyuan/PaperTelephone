package com.conanyuan.papertelephone;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.ListFragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

public class MainActivity extends FragmentActivity {
	private MyAdapter mAdapter;
	private ViewPager mPager;
	private ArrayList<IGame> mLocalGames;
	private ArrayList<IGame> mNetworkGames;
	private ArrayList<IGame> mCompletedGames;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_layout);

		mLocalGames = new ArrayList<IGame>();
		mNetworkGames = new ArrayList<IGame>();
		mCompletedGames = new ArrayList<IGame>();

		List<ArrayList<IGame>> gameList = new ArrayList<ArrayList<IGame>>();
		gameList.add(mLocalGames);
		gameList.add(mNetworkGames);
		gameList.add(mCompletedGames);
		List<String> names = new ArrayList<String>();
		names.add(Page.LOCAL_GAMES.getName());
		names.add(Page.NETWORK_GAMES.getName());
		names.add(Page.COMPLETED_GAMES.getName());
		mAdapter = new MyAdapter(getSupportFragmentManager(), gameList, names);

		mPager = (ViewPager) findViewById(R.id.pager);
		mPager.setAdapter(mAdapter);
	}
	
	/* (non-Javadoc)
	 * @see android.support.v4.app.FragmentActivity#onPause()
	 */
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	}

	public static enum Page {
		LOCAL_GAMES("Local Games"), NETWORK_GAMES("Network Games"), COMPLETED_GAMES(
				"Completed Games");

		private final String name;

		Page(String name) {
			this.name = name;
		}

		public String getName() {
			return name;
		}

		private static final int size = Page.values().length;

		public static int size() {
			return size;
		}
	}

	public static class MyAdapter extends FragmentPagerAdapter {
		private List<ArrayList<IGame>> mGameLists;
		private List<String> mNames;

		public MyAdapter(FragmentManager fragmentManager,
				List<ArrayList<IGame>> gameLists, List<String> names) {
			super(fragmentManager);
			mGameLists = gameLists;
			mNames = names;
		}

		@Override
		public int getCount() {
			return Page.size();
		}

		@Override
		public Fragment getItem(int position) {
			return ArrayListFragment.newInstance(mGameLists.get(position));
		}

		@Override
		public CharSequence getPageTitle(int position) {
			return mNames.get(position);
		}
	}

	public static class ArrayListFragment extends ListFragment {
		static final String GAME_MESSAGE = "com.conanyuan.papertelephone.GAME";
		private ArrayList<IGame> mGames;

		/**
		 * Create a new instance of ArrayListFragment, providing "games" as an
		 * argument.
		 */
		static ArrayListFragment newInstance(ArrayList<IGame> games) {
			ArrayListFragment f = new ArrayListFragment();

			Bundle args = new Bundle();
			args.putParcelableArrayList("games", games);
			f.setArguments(args);

			return f;
		}

		/**
		 * When creating, retrieve this instance's number from its arguments.
		 */
		@Override
		public void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			if (getArguments() != null) {
				mGames = getArguments().getParcelableArrayList("games");
			}
		}

		/**
		 * The Fragment's UI is just a simple text view showing its instance
		 * number.
		 */
		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View v = inflater.inflate(R.layout.fragment_pager_list, container,
					false);

			// Watch for requests to add a new game.
			Button button = (Button) v.findViewById(R.id.new_game);
			button.setOnClickListener(new OnClickListener() {
				@SuppressWarnings("unchecked")
				public void onClick(View v) {
					mGames.add(new PhraseGame());
					// Have to cast to an ArrayAdapter in order to call
					// notifyDataSetChanged.
					// And if we don't call notifyDataSetChanged, then the view
					// won't know the array changed until we move to a different
					// tab and move back. We know we will get an ArrayAdapter
					// because that's what we set it to in onActivityCreated.
					((ArrayAdapter<IGame>) getListAdapter())
							.notifyDataSetChanged();
				}
			});

			return v;
		}

		@Override
		public void onActivityCreated(Bundle savedInstanceState) {
			super.onActivityCreated(savedInstanceState);
			setListAdapter(new ArrayAdapter<IGame>(getActivity(),
					android.R.layout.simple_list_item_1, mGames));
		}

		@Override
		public void onListItemClick(ListView l, View v, int position, long id) {
			Log.i("FragmentList", "Item clicked: " + id);
			Intent intent = new Intent(getActivity(), GameActivity.class);
		    intent.putExtra(GAME_MESSAGE, mGames.get(position));
		    startActivityForResult(intent, position);
		}

		/* (non-Javadoc)
		 * @see android.support.v4.app.Fragment#onActivityResult(int, int, android.content.Intent)
		 */
		@Override
		public void onActivityResult(int requestCode, int resultCode,
				Intent data) {
			mGames.get(requestCode).addTurn((ITurn)data.getParcelableExtra(GAME_MESSAGE));
		}
	}
}
