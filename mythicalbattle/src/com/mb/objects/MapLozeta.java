package com.mb.objects;

import java.util.LinkedList;
import java.util.List;

import com.mb.actions.Aura;

public class MapLozeta {
    public int propiedad;
    public boolean modpropiedad;
    public List<Aura> ListAura;
    public int  Time = 999;

    public MapLozeta(){
        propiedad= 0;
        modpropiedad = true;
        ListAura = new LinkedList<Aura>();
    }
    
    public void setPropiedadFinal(int pro){
        propiedad = pro;
        modpropiedad = false;
    }
    
    public void setPropiedad(int pro){
        propiedad = pro;
    }
    
    public int getPropiedad(){
        return propiedad;
    }
    
    public void remove(){
        propiedad = 0;
        modpropiedad = true;
    }

}
