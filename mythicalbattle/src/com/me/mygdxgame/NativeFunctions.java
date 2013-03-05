package com.me.mygdxgame;

public interface NativeFunctions {
	public void cliente();
	public void getConnection();
	public FichaData getFichaData(int id);
	public void DownloadDB(int size);	
	public float getPercent();
}
