/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mb.data;

import com.badlogic.gdx.math.Vector2;
import com.mb.objects.MapLozeta;

/**
 *
 * @author carlos2
 */
public class PropertiesMapData {
    private String AssetPath;
    private int WidthLozetas;
    private int HeightLozetas;
    private int Width;
    private int Height;
    private Vector2 TORREA;
    private Vector2 TORREB;
    private MapLozeta MapLozetas [][];
    
    public void setAssetPath(String path){
        AssetPath = path;
    }
    
    public void setWidthLozetas(int widthlozetas){
        WidthLozetas = widthlozetas;
    }
    
    public void setHeightLozetas(int heightlozetas){
        HeightLozetas = heightlozetas;
    }
    public void setWidth(int width){
        Width = width;
    }
    public void setHeight(int height){
        Height = height;
    }
    
    public void setTorreA(Vector2 torrea){
        TORREA = torrea;
    }
    
    public void setTorreB(Vector2 torreb){
        TORREB = torreb;
    }
    
    public void setMapLozetas(MapLozeta [][] maplozetas){
        MapLozetas = maplozetas;
    }
    
    public String getAssetPath(){
        return AssetPath;
    }
    
    public int getWidthLozetas(){
        return WidthLozetas;
    }
    
    public int getHeightLozetas(){
        return HeightLozetas;
    }
    
    public int getWidth(){
        return Width;
    }
    
    public int getHeight(){
        return Height;
    }
    
    public Vector2 getTorreA(){
        return TORREA;
    }
    
    public Vector2 getTorreB(){
        return TORREB;
    }
    
    public MapLozeta [][] getMapLozetas(){
        return MapLozetas;
    }
}
