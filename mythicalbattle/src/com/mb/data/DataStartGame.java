package com.mb.data;

public class DataStartGame {
	private int IDMAP;
	private int TIME;
	private int GOLD;
	
	public void setIDMap(int id){
		IDMAP = id;
	}
	
	public void setTime(int time){
		TIME = time;
	}
	
	public void setGold(int gold){
		GOLD = gold;
	}
	
	public int getIDMap(){
		return IDMAP;
	}
	
	public int gedTime(){
		return TIME;
	}
	
	public int getGold(){
		return GOLD;
	}

}
