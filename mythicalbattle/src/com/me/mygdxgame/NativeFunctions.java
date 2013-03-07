package com.me.mygdxgame;

public interface NativeFunctions {
	
	public void cliente();
	public void getConnection();
	public void DownloadDB(int size);	
	public float getPercent();
	
	public ObjectData getHeroData(int id);
	public ObjectData getRelicData(int id);
	public ObjectData getTowerData(int id);
	
	public EquipData getEquipData(int id);
	public SpellData getSpellData(int id);
}
