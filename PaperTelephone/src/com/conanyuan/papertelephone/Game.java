package com.conanyuan.papertelephone;

import java.util.ArrayList;
import java.util.List;

/**
 * The Game object.
 * 
 * @author Yuan
 */
public class Game {
	private String mName;
	private int mId;
	private List<Turn> mTurns;

	public Game(String name, int id) {
		mName = name;
		mId = id;
		mTurns = new ArrayList<Turn>();
	}

	public String getName() {
		return mName;
	}

	public void setName(String name) {
		mName = name;
	}

	public int getId() {
		return mId;
	}

	public void setId(int id) {
		mId = id;
	}

	public void addTurn(Turn turn) {
		mTurns.add(turn);
	}

	public List<Turn> getTurns() {
		return mTurns;
	}
}
