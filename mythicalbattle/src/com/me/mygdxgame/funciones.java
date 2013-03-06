package com.me.mygdxgame;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;

public class funciones {
	private float factorH = 0;
	private float X0;
	private float Y0;
	private float fichaW;
	private float fichaH;
	private float dimH;
	private float dimW;
	
	public static final int UNSELECTED = 0;
	public static final int SELECTED = 1;
	public static final int READY = 2;
	
	public static final int STFTYPE = 1;
	public static final int STETYPE = 2;
	public static final int ATFTYPE = 3;
	public static final int ATETYPE = 4;
	public static final int ALFTYPE = 5;
	public static final int ALETYPE = 6;
	public static final int ATYPE = 7;
	
	public funciones(float factH){
		
		dimW = 19*100;
		dimH = 19*70;
		
		factorH = factH;
		Y0 = dimH*factorH;
		X0 = -dimW*factorH;
		fichaW = 100*factorH;
		fichaH = 70*factorH;
	}
	
	public Vector2 CalcularPosicionMat(float x, float y){
		Vector2 aux = new Vector2(0,0);
		aux.x =(int)((((Y0+y)/0.7)+x)/fichaW);
		aux.y =(int)((0.7*(X0-x)+y)/fichaH);
		if(aux.y>0)
			aux.y=0;
		if(aux.y<-37)
			aux.y=-37;
		if(aux.x>37)
			aux.x=37;
		if(aux.x<0)
			aux.x=0;
		Vector2 posicion = new Vector2();
		posicion.x = Math.abs(aux.x);
		posicion.y = Math.abs(aux.y);
		return posicion;
	}
	
	public Vector2 convertirPuntero (float x, float y,  OrthographicCamera camera, float[] mod){
		Vector2 puntero = new Vector2(((x-mod[0])*camera.zoom)-mod[2],-((y-mod[1])*camera.zoom)-mod[3]);
		return puntero;
	}
	public Vector2 SeleccionarPos(float x, float y){
		Vector2 posicion = new Vector2(0,0);
		posicion.x = (-dimW+(x+y)*50)*factorH;
		posicion.y = (-35+x*35-y*35)*factorH;
		return posicion;
	}
	
	public void CalcularAlcance(Vector2 posicion,int SIZE, int rango1, int rango2, List<Vector2> List1, List<Vector2> List2, Nodo[][] nodos){
		List1.clear();
		List2.clear();
		cleanNodos(nodos);
		int x = (int)posicion.x;
		int y = (int)posicion.y;
		Vector2 vector2=new Vector2();
		Vector2 vector1=new Vector2();
		if(SIZE == 1){
			for (int i=y-rango1;i< y+rango1+1; i++)
				for(int j=x-rango1;j<x+rango1+1;j++){
					int valor = Math.abs(i-y)+Math.abs(j-x);
					if(valor<=rango1 && valor>=rango2){
						vector2 = new Vector2(j,i);
						vector1 = SeleccionarPos(j,i);
						List1.add(vector1);
						List2.add(vector2);
					}
				}
		}
		else{
			rango2 = rango2+2;
			for (int i=y-rango1;i< y+rango1+1; i++)
				for(int j=x-rango1;j<x+rango1+1;j++){
					int valor = Math.abs(i-y)+Math.abs(j-x);
					if(valor<=rango1 && valor>=rango2){
						if(!nodos[i][j].closed){
							vector2 = new Vector2(j,i);
							vector1 = SeleccionarPos(j,i);
							List1.add(vector1);
							List2.add(vector2);
							nodos[i][j].closed = true;
						}
						if(!nodos[i][j+1].closed){
							vector2 = new Vector2(j+1,i);
							vector1 = SeleccionarPos(j+1,i);
							List1.add(vector1);
							List2.add(vector2);
							nodos[i][j+1].closed = true;
						}
						if(!nodos[i+1][j].closed){
							vector2 = new Vector2(j,i+1);
							vector1 = SeleccionarPos(j,i+1);
							List1.add(vector1);
							List2.add(vector2);
							nodos[i+1][j].closed = true;
						}
						if(!nodos[i+1][j+1].closed){
							vector2 = new Vector2(j+1,i+1);
							vector1 = SeleccionarPos(j+1,i+1);
							List1.add(vector1);
							List2.add(vector2);
							nodos[i+1][j+1].closed = true;
						}
					}
				}
		}

	}
	
	public void CalcularAlcance(Vector2 posicion,int rango1, List<Vector2> List1, Nodo[][] nodos){
		List1.clear();
		cleanNodos(nodos);
		if(nodos[(int)posicion.y][(int)posicion.x].size==1)
			findingPath1(posicion, rango1, List1, nodos);
		else 
			findingPath2(posicion, rango1, List1, nodos);
	}
	
	/*>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
	 * PATHFINDING
	 */
	
	private void findingPath1(Vector2 posicion,int rango1, List<Vector2> List1, Nodo[][] nodos){
		List<Vector2> Abierta = new LinkedList<Vector2>();
		Vector2 end = new Vector2(posicion.x-rango1, posicion.y);
		nodos[(int)posicion.y][(int)posicion.x].opened = true;
		nodos[(int)posicion.y][(int)posicion.x].parent = null;
		Abierta.add(posicion);
		while(Abierta.size()>0){
			Vector2 nodo = new Vector2(Abierta.get(0));
			List1.add(SeleccionarPos(nodo.x,nodo.y));
			nodos[(int)Abierta.get(0).y][(int)Abierta.get(0).x].closed = true;
			Abierta.remove(0);
			if(nodo.x==end.x && nodo.y==end.y)
				break;
			
			List<Vector2> vecinos = getVecinos(nodo, nodos);
			for(int i=0;i<vecinos.size();i++){
				Vector2 vecino = new Vector2(vecinos.get(i));
				if(nodos[(int)vecino.y][(int)vecino.x].closed || nodos[(int)vecino.y][(int)vecino.x].opened)
					continue;
				
				nodos[(int)vecino.y][(int)vecino.x].opened = true;
				nodos[(int)vecino.y][(int)vecino.x].parent= nodos[(int)nodo.y][(int)nodo.x];
				if(countPasos(nodos[(int)vecino.y][(int)vecino.x])<= rango1)
					Abierta.add(vecino);
			}
		}
	}
	private void findingPath2(Vector2 posicion,int rango1, List<Vector2> List1, Nodo[][] nodos){
		List<Vector2> List2 = new LinkedList<Vector2>();
		nodos[(int)posicion.y][(int)posicion.x].ReflectThis = true;
		nodos[(int)posicion.y+1][(int)posicion.x].ReflectThis = true;
		nodos[(int)posicion.y][(int)posicion.x+1].ReflectThis = true;
		nodos[(int)posicion.y+1][(int)posicion.x+1].ReflectThis = true;
		
		List<Vector2> Abierta = new LinkedList<Vector2>();
		
		Vector2 end = new Vector2(posicion.x-rango1, posicion.y);
		
		nodos[(int)posicion.y][(int)posicion.x].opened = true;
		nodos[(int)posicion.y][(int)posicion.x].parent = null;
		
		Abierta.add(posicion);
		
		while(Abierta.size()>0){
			Vector2 nodo = new Vector2(Abierta.get(0));
			List2.add(nodo);
			nodos[(int)Abierta.get(0).y][(int)Abierta.get(0).x].closed = true;
			nodos[(int)Abierta.get(0).y][(int)Abierta.get(0).x].guia = true;
			Abierta.remove(0);
			if(nodo.x==end.x && nodo.y==end.y)
				break;
			
			List<Vector2> vecinos = getVecinos2(nodo, nodos);
			for(int i=0;i<vecinos.size();i++){
				Vector2 vecino = new Vector2(vecinos.get(i));
				if(nodos[(int)vecino.y][(int)vecino.x].closed || nodos[(int)vecino.y][(int)vecino.x].opened)
					continue;
				
				nodos[(int)vecino.y][(int)vecino.x].opened = true;				
				nodos[(int)vecino.y][(int)vecino.x].parent= nodos[(int)nodo.y][(int)nodo.x];
				if(countPasos(nodos[(int)vecino.y][(int)vecino.x])<= rango1)
					Abierta.add(vecino);
			}
		}
		addReflect(List2,nodos);
		getFinalList(List1,List2,nodos);
	}
	
	private List<Vector2> getVecinos(Vector2 posicion, Nodo[][] nodos){
		List<Vector2> vecinos = new LinkedList<Vector2>();
		if(isWalkableAt(posicion.x,posicion.y-1)&& nodos[(int)posicion.y-1][(int)posicion.x].isWalkable())
			vecinos.add(new Vector2(posicion.x,posicion.y-1));
		
		if(isWalkableAt(posicion.x+1,posicion.y)&& nodos[(int)posicion.y][(int)posicion.x+1].isWalkable())
			vecinos.add(new Vector2(posicion.x+1,posicion.y));
		
		if(isWalkableAt(posicion.x,posicion.y+1)&& nodos[(int)posicion.y+1][(int)posicion.x].isWalkable())
			vecinos.add(new Vector2(posicion.x,posicion.y+1));
		
		if(isWalkableAt(posicion.x-1,posicion.y)&& nodos[(int)posicion.y][(int)posicion.x-1].isWalkable())
			vecinos.add(new Vector2(posicion.x-1,posicion.y));
		return vecinos;
	}
	
	private List<Vector2> getVecinos2(Vector2 posicion, Nodo[][] nodos){
		List<Vector2> vecinos = new LinkedList<Vector2>();
		if(	isWalkableAt2(posicion.x,posicion.y-1, nodos))
			vecinos.add(new Vector2(posicion.x,posicion.y-1));
		
		if(	isWalkableAt2(posicion.x+1,posicion.y, nodos))
			vecinos.add(new Vector2(posicion.x+1,posicion.y));
		
		if(	isWalkableAt2(posicion.x,posicion.y+1,nodos))
			vecinos.add(new Vector2(posicion.x,posicion.y+1));
		
		if(	isWalkableAt2(posicion.x-1,posicion.y,nodos))
			vecinos.add(new Vector2(posicion.x-1,posicion.y));
		
		return vecinos;
	}
	
	private boolean isWalkableAt(float x,float y){
		return (x>=0 && x<38 )&&(y>=0 && y<38 ) ;
	}
	
	private boolean isWalkableAt2(float x,float y,Nodo[][] nodos){
		if((x>=0 && x<38 )&&(y>=0 && y<38 )&&(x+1>=0 && x+1<38 )&&(y+1>=0 && y+1<38 ))
			if(	(nodos[(int)y][(int)x].isWalkable() || nodos[(int)y][(int)x].ReflectThis) &&
				(nodos[(int)y][(int)x+1].isWalkable() || nodos[(int)y][(int)x+1].ReflectThis) &&
				(nodos[(int)y+1][(int)x].isWalkable() || nodos[(int)y+1][(int)x].ReflectThis) &&
				(nodos[(int)y+1][(int)x+1].isWalkable() || nodos[(int)y+1][(int)x+1].ReflectThis))
 				return true;
		return false ;
	}
	
	public void cleanNodos(Nodo[][] nodos){
		for (int i=0;i< 38; i++)
			for(int j=0;j<38;j++){
				nodos[i][j].clean();
			}
	}
	
	public int countPasos(Nodo nodo){
		int pasos = 0;
		Nodo nodito = nodo;
		while (nodito.parent!=null){
			nodito = nodito.parent;
			pasos++;			
		}
		return pasos;
	}
	
	public List<Vector2> getPasos(Vector2 destino,Nodo[][] nodos){
		List<Vector2> pasos = new LinkedList<Vector2>();
		Nodo nodo = nodos[(int)destino.y][(int)destino.x];
		while(nodo.parent!=null){
			pasos.add(SeleccionarPos(nodo.x,nodo.y));
			nodo = nodo.parent;
		}
		return pasos;
	}
	
	private void addReflect(List<Vector2> List2, Nodo[][] nodos){
		int size = List2.size();
		for(int i=1;i<size;i++){
			Vector2 v = new Vector2(List2.get(i));
			
			if(!nodos[(int)v.y][(int)v.x+1].closed){
				List2.add(new Vector2(v.x+1,v.y));
				nodos[(int)v.y][(int)v.x+1].closed = true;
			}
			if(!nodos[(int)v.y+1][(int)v.x].closed){
				List2.add(new Vector2(v.x,v.y+1));
				nodos[(int)v.y+1][(int)v.x].closed = true;
			}
			if(!nodos[(int)v.y+1][(int)v.x+1].closed){
				List2.add(new Vector2(v.x+1,v.y+1));
				nodos[(int)v.y+1][(int)v.x+1].closed = true;
			}
		}
	}
	
	public Vector2 getReflect(Vector2 indexTemp, Nodo[][] nodos){
		Vector2 vfinal = new Vector2(indexTemp);
		int pasos = 99;
		int temp = 0;
		if(nodos[(int)indexTemp.y][(int)indexTemp.x].guia){
			temp = countPasos(nodos[(int)indexTemp.y][(int)indexTemp.x]);		
			if(temp<pasos  ){
				pasos = temp;
				vfinal = new Vector2(indexTemp);
			}
		}
		if(nodos[(int)indexTemp.y][(int)indexTemp.x-1].guia){
			temp = countPasos(nodos[(int)indexTemp.y][(int)indexTemp.x-1]);
			if(temp<pasos){
				pasos = temp;
				vfinal = new Vector2(indexTemp.x-1,indexTemp.y);
			}
		}
		if(nodos[(int)indexTemp.y-1][(int)indexTemp.x].guia){
			temp = countPasos(nodos[(int)indexTemp.y-1][(int)indexTemp.x]);
			if(temp<pasos){
				pasos = temp;
				vfinal = new Vector2(indexTemp.x,indexTemp.y-1);
			}
		}
		
		if(nodos[(int)indexTemp.y-1][(int)indexTemp.x-1].guia){
			temp = countPasos(nodos[(int)indexTemp.y-1][(int)indexTemp.x-1]);
			if(temp<pasos){
				pasos = temp;
				vfinal = new Vector2(indexTemp.x-1,indexTemp.y-1);
			}
		}
			return vfinal;
	}
	
	public void getFinalList(List<Vector2> List1,List<Vector2> List2, Nodo[][] nodos){
		Iterator<Vector2> iter = List2.iterator();
		while(iter.hasNext()){
			Vector2 v = iter.next();
			if(nodos[(int)v.y][(int)v.x].ReflectThis)
				continue;
			List1.add(SeleccionarPos(v.x,v.y));
		}
	}
	
	/*>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
	 *  FIN PATHFINDING
	 */
	
	
	/*>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
	 *  TARGING
	 */
	/*Se seleccionan a las fichas que entran dentro de el area de la habilidad, para eso se llama a funciones.CalcularRangoHab()
	 * que agrega a la lista de las pociciones de las fichas que son seleccionadas por la habilidad(AlcanceHabilidad) y la lista
	 * de las lozetas que son seleccionadas (AlcanceMP)
	 * Se procede a recorrer toda la listas de la fichas que se encuentran dentro de el rango, este rango esta definido con un 
	 * radio de RH3 que depende de a habilidad
	 * Se verifica en la matris de fichas que las pociciones de la lista no sea la misma ficha o un espacio vacio(-1)
	 * y luego se la targetea como amigo o como enemigo, dependiendo de la habilidad*/
	public void targetTouch(Vector2 posMat,List<Vector2> listTarget,List<Vector2> AlcanceMP, List<Vector2> AlcanceHabilidad, int HABILIDAD, Nodo[][] nodos, Vector2 NODOSELECTED ){
		int RH3 = nodos[(int)NODOSELECTED.y][(int)NODOSELECTED.x].ficha.RH3;
		CalcularAlcance(posMat,1,RH3,0,AlcanceMP,AlcanceHabilidad,nodos);
		Iterator<Vector2> iter = AlcanceHabilidad.iterator();
		while(iter.hasNext()){
			Vector2 posicion = iter.next();
			if(nodos[(int)posicion.y][(int)posicion.x].ocupado){
				if(nodos[(int)posicion.y][(int)posicion.x].Reflect)
					posicion = nodos[(int)posicion.y][(int)posicion.x].nodoReflect;
				if(!nodos[(int)posicion.y][(int)posicion.x].target &&(posicion.x!=NODOSELECTED.x || posicion.y!=NODOSELECTED.y)){
					listTarget.add(posicion);
					nodos[(int)posicion.y][(int)posicion.x].target = true;
					if(HABILIDAD == ATFTYPE)
						nodos[(int)posicion.y][(int)posicion.x].ficha.targetF();
					else
						nodos[(int)posicion.y][(int)posicion.x].ficha.target();
				}
				if(nodos[(int)posicion.y][(int)posicion.x].size==2){
					nodos[(int)posicion.y][(int)posicion.x+1].target = true;
					nodos[(int)posicion.y+1][(int)posicion.x].target = true;
					nodos[(int)posicion.y+1][(int)posicion.x+1].target = true;
				}
			}
		}
	}
	
	/*Se deselecciona a todos las fichas que se encuentren targeteadas por el area seleccionada, estas
	 * son las dichas que se agregaron a la lista lisTarget*/

	public void Untarget(List<Vector2> listTarget, int HABILIDAD, Nodo[][] nodos){
		Iterator<Vector2> iter = listTarget.iterator();
		while(iter.hasNext()){
			Vector2 index = iter.next();
			if(HABILIDAD == ATFTYPE)
				nodos[(int)index.y][(int)index.x].ficha.UntargetF();
			else
				nodos[(int)index.y][(int)index.x].ficha.Untarget();
		}
		listTarget.clear();
	}
	
	/*Se seleccionan las unidades en un area determinada definida por 2 rangos RH1 y Rh2*/
	public void Target(Vector2 posicionMat, int size,List<Vector2> listTarget, List<Vector2> AlcanceRango,List<Vector2> AlcanceHabilidad,int HABILIDAD,Nodo[][] nodos ){
		int RH1 = nodos[(int)posicionMat.y][(int)posicionMat.x].ficha.RH1;
		int RH2 = nodos[(int)posicionMat.y][(int)posicionMat.x].ficha.RH2;
		CalcularAlcance(posicionMat,size,RH1,RH2,AlcanceRango,AlcanceHabilidad,nodos);
		Iterator<Vector2> iter = AlcanceHabilidad.iterator();
		while(iter.hasNext()){
			Vector2 posicion = iter.next();
			if(nodos[(int)posicion.y][(int)posicion.x].ocupado){
				if(nodos[(int)posicion.y][(int)posicion.x].Reflect)
					posicion = nodos[(int)posicion.y][(int)posicion.x].nodoReflect;
				if(!nodos[(int)posicion.y][(int)posicion.x].target &&(posicion.x!=posicionMat.x || posicion.y!=posicionMat.y)){
					listTarget.add(posicion);
					nodos[(int)posicion.y][(int)posicion.x].target = true;
					if(HABILIDAD == ATFTYPE)
						nodos[(int)posicion.y][(int)posicion.x].ficha.targetF();
					else
						nodos[(int)posicion.y][(int)posicion.x].ficha.target();
				}
				if(nodos[(int)posicion.y][(int)posicion.x].size==2){
					nodos[(int)posicion.y][(int)posicion.x+1].target = true;
					nodos[(int)posicion.y+1][(int)posicion.x].target = true;
					nodos[(int)posicion.y+1][(int)posicion.x+1].target = true;
				}
			}
		}
	}

}
