package com.mb.data;

import com.badlogic.gdx.math.Vector2;

public class MapData {
	public String nombre;
	public String descripcion;
	public Vector2 posA;
	public Vector2 posB;
	
	public void setPosA(int x, int y){
		posA = new Vector2(x,y);
	}
	
	public void setPosB(int x, int y){
		posB = new Vector2(x,y);
	}

}
