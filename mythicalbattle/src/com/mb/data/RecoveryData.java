package com.mb.data;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class RecoveryData {
	private ResultSet resultSet = null;
	static Connection con;
	private Statement statement = null;
	public RecoveryData(Connection conection){
		con = conection;
	}
	
	public ObjectData getHeroData(int id){
		ObjectData hero = new ObjectData();
		   try {
		     statement = con.createStatement();
		     resultSet = statement.executeQuery("SELECT * FROM hero where idhero ="+id);
		     while (resultSet.next())
		     {
		    	 hero.nombre = resultSet.getString("nombre");
		    	 hero.descripcion = resultSet.getString("descripcion");
		    	 hero.size = Integer.parseInt(resultSet.getString("size"));
		    	 hero.Type = 1;
		     }
		    }
		    catch (SQLException ex) {
		       System.out.println(ex);
		    }
		return hero;
	}
	
	public ObjectData getRelicData(int id){
		ObjectData fichaData = new ObjectData();
		return fichaData;
	}
	
	public ObjectData getTowerData(int id){
		ObjectData torre = new ObjectData();
		try {
			statement = con.createStatement();
		    resultSet = statement.executeQuery("SELECT * FROM torre where idtorre ="+id);
		    while (resultSet.next()){
		    	torre.nombre = resultSet.getString("nombre");
		    	torre.descripcion = resultSet.getString("descripcion");
		    	torre.size = 2;
		    	torre.Type = 3;
		    	}
		    }
		    catch (SQLException ex) {
		       System.out.println(ex);
		    }
		return torre;
	}
	
	public SpellData getSpellData(int id){
		SpellData fichaData = new SpellData();
		  
		return fichaData;
	}
	
	public EquipData getEquipData(int id){
		EquipData fichaData = new EquipData();
		  
		return fichaData;
	}
	
	public CardData getCardData(int id){
		CardData fichaData = new CardData();
		  
		return fichaData;
	}
	
	public DescriptionMapData getMapData(int id){
		DescriptionMapData Mapa = new DescriptionMapData();
		try {
			statement = con.createStatement();
		    resultSet = statement.executeQuery("SELECT * FROM map where idmap ="+id);
		    while (resultSet.next())
		    {
		    	Mapa.nombre = resultSet.getString("nombre");
		    	Mapa.descripcion = resultSet.getString("descripcion");
		    	Mapa.data = resultSet.getString("data");
		    }
		    }
		    catch (SQLException ex) {
		       System.out.println(ex);
		    }
		  
		return Mapa;
	}
}
