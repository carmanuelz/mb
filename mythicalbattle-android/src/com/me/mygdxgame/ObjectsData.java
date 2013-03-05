package com.me.mygdxgame;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class ObjectsData {
	private ResultSet resultSet = null;
	static Connection con;
	private Statement statement = null;
	public ObjectsData(Connection conection){
		con = conection;
	}
	
	public FichaData getFichaData(int id){
		FichaData fichaData = new FichaData();
		   try {
		     statement = con.createStatement();
		     resultSet = statement.executeQuery("SELECT * FROM ficha where idFicha ="+id);
		     while (resultSet.next())
		     {
		    	 fichaData.nombre = resultSet.getString("nombre");
		    	 fichaData.descripcion = resultSet.getString("descripcion");
		    	 fichaData.HP = Integer.parseInt(resultSet.getString("HP"));
		    	 fichaData.IEP = Integer.parseInt(resultSet.getString("IEP"));
		     }
		    }
		    catch (SQLException ex) {
		       System.out.println(ex);
		    }
		return fichaData;
	}
	
}
