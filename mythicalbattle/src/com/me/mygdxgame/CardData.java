package com.me.mygdxgame;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class CardData {
	private ResultSet resultSet = null;
	static Connection con;
	private Statement statement = null;
	public CardData(Connection conection){
		con = conection;
	}
	
	public ObjectData getHeroData(int id){
		ObjectData fichaData = new ObjectData();
		   try {
		     statement = con.createStatement();
		     resultSet = statement.executeQuery("SELECT * FROM hero where idhero ="+id);
		     while (resultSet.next())
		     {
		    	 fichaData.nombre = resultSet.getString("nombre");
		    	 fichaData.descripcion = resultSet.getString("descripcion");
		     }
		    }
		    catch (SQLException ex) {
		       System.out.println(ex);
		    }
		return fichaData;
	}
	
	public ObjectData getRelicData(int id){
		ObjectData fichaData = new ObjectData();
		return fichaData;
	}
	
	public ObjectData getTowerData(int id){
		ObjectData fichaData = new ObjectData();
		  
		return fichaData;
	}
	
	public SpellData getSpellData(int id){
		SpellData fichaData = new SpellData();
		  
		return fichaData;
	}
	
	public EquipData getEquipData(int id){
		EquipData fichaData = new EquipData();
		  
		return fichaData;
	}
	
}
