package com.mb.utils;

import java.sql.Connection;

import com.mb.data.*;

public interface NativeFunctions {
	
	public void cliente();
	public Connection getConnection();
	public void DownloadDB();	
	public float getPercent();
	
	public ObjectData getHeroData(int id);
	public ObjectData getRelicData(int id);
	public ObjectData getTowerData(int id);	
	public EquipData getEquipData(int id);
	public SpellData getSpellData(int id);
	
	public CardData getCardData(int id);
	
	public DescriptionMapData getMapData(int id);
}
