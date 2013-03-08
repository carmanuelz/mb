package com.mb.screens;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenManager;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.Touchpad;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton.ImageButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Touchpad.TouchpadStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Json;
import com.mb.data.DataStartGame;
import com.mb.data.ObjectData;
import com.mb.objects.Object;
import com.mb.objects.Nodo;
import com.mb.utils.SpriteAccessor;
import com.mb.utils.Funciones;

public class StartGameScreen extends AbstractScreen{

	private OrthographicCamera camera;
	private SpriteBatch batch;
	AssetManager assetManager;
	
	float[] mod = new float[4];
	
	private float CamMaxX;
	private float CamMaxY;
	private float CamMinX;
	private float CamMinY;
	
	private TextureRegion contorno;
	private TextureRegion buttonTexture;
	
	public TextureAtlas textures;
	public TextureAtlas joypadtextures;
	
	public Sprite MapBack;
	
	private Sprite areaTarget;
	private Sprite Torre;
	
	private float porcentx;
	private float porcenty;
	
	private Object Objeto;

	private Sprite DraggedFicha;
	private boolean dragged = false;
	private boolean atdragged = false;
	private boolean touchTarget = true;
	
	private Stage stage;
	private Stage cardStage;
	
	private Slider slider;
	private Touchpad joystick2;
	private ImageButton Deck;
	private ImageButton Card;
	private ImageButton CardMagic;
	
	public final TweenManager tweenManager = new TweenManager();
	
	public Funciones funciones;
	
	public Nodo[][] Nodos;
	
	private Vector2 TempPosicionMat;
	
	public int indexFicha = 0;
	
	/*>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>*/
	
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
	
	/*<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<*/
	
	public int ESTADO = 0;
	public int HABILIDAD = 0;
	public Vector2 NODOSELECTED = new Vector2(); 
	public int SIZESELECTED;
	
	private List<Vector2> AlcanceMP;
	private List<Vector2> AlcanceHabilidad;
	private List<Vector2> listTarget;
	
	public float Width = 0;
	public float Height = 0;
	public float factorW = 0;
	public float factorH = 0;
	
	public float losetaW;
	public float losetaH;
	
	/*<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<*/
	private DataStartGame Data;
	private ObjectData ObjectData;
	public StartGameScreen startgamescreen = this;
	/*<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<*/
	
	public StartGameScreen(MainScreen game, DataStartGame data) {		
		super(game);
		// TODO Auto-generated constructor stub
		game.mNativeFunctions.getConnection();
		Data = data;
	}
	@Override
	public void render(float delta) {
		// TODO Auto-generated method stub
		Gdx.gl.glClearColor(0f, 0f, 0f, 1);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		
		if((camera.position.x+porcentx)>CamMaxX || (camera.position.x+porcentx)<CamMinX)
        	porcentx = 0;
        if((camera.position.y+porcenty)>CamMaxY || (camera.position.y+porcenty)<CamMinY)
        	porcenty = 0;
        
		camera.translate(porcentx, porcenty, 0);
		mod[2] -= porcentx;
		mod[3] -= porcenty;
		
		tweenManager.update(Gdx.graphics.getDeltaTime());
		InputHandler();
		
		camera.update();
		
		batch.setProjectionMatrix(camera.combined);
		batch.begin();
		
		MapBack.draw(batch);		

		if(NODOSELECTED!=null){
			Nodos[(int)NODOSELECTED.y][(int)NODOSELECTED.x].ficha.RangoMovDraw(batch);
		}
		if(atdragged&&touchTarget)
			AreaDraw();
		
		for(int i = 37;i>=0;i--)
			for (int j = 0; j<=37; j++){
				if(Nodos[j][i].ficha!=null)
					Nodos[j][i].ficha.FichaDraw(batch);
			}
		if(dragged)
			DraggedFicha.draw(batch);
		Torre.draw(batch);
		
		batch.end();
		
		stage.act(Gdx.graphics.getDeltaTime());
        stage.draw();
        
        cardStage.act(Gdx.graphics.getDeltaTime());
        cardStage.draw();
	}


	@Override
	public void show() {
		// TODO Auto-generated method stub
		Width = Gdx.graphics.getWidth();
		Height = Gdx.graphics.getHeight();
		
		factorW = Width/1280;
		factorH = Height/800;
		
		losetaW = 100*factorH;
		losetaH = 70*factorH;
		
		CamMaxX = (2000*factorH-Width)/2;
		CamMinX = -(2000*factorH-Width)/2;
		
		CamMaxY = (1260*factorH-Height)/2-losetaW;
		CamMinY = -(1260*factorH-Height)/2-losetaH;
		
		camera = new OrthographicCamera(Width,Height);
		batch = new SpriteBatch();
		
		Data.setIDMap(1);
		Data.setGold(150);
		Data.setTime(90);
		
		Json json = new Json();
		System.out.println(json.toJson(Data));
		
		funciones = new Funciones(factorH);
		
		ObjectData = game.mNativeFunctions.getHeroData(3);
		//System.out.println(fichadata.nombre+" "+fichadata.descripcion);
		
		textures = new TextureAtlas(Gdx.files.internal("data/texturas.pack"));
		joypadtextures = new TextureAtlas(Gdx.files.internal("data/joypad.pack"));
		
		MapBack = new Sprite(new TextureRegion(new Texture(Gdx.files.internal("data/terreno2.png")),0,0,2000,1260));
		MapBack.setSize(MapBack.getWidth()*factorH, MapBack.getHeight()*factorH);
		
		areaTarget = textures.createSprite("loseta");
		areaTarget.setSize(100*factorH,70*factorH);
		areaTarget.setColor(1, 1, 1, 0.25f);
		
		
		Torre = textures.createSprite("torre");
		Torre.setSize(300*factorH,300*factorH);
		Vector2 posTorre = funciones.SeleccionarPos(12,9);
		Torre.setPosition(posTorre.x-losetaW/2-50*factorH, posTorre.y);
		
		
		Skin skin = new Skin(Gdx.files.internal("data/controlui.json"));
		
		slider = new Slider(-25, 50, 1, false, skin);
		slider.setSize(100, 20);
		slider.setPosition(20, 50);
		slider.setValue(0);
		
		
		agregarActors();
		AgregarListener();
		
		cardStage = new Stage();
		stage = new Stage();
		stage.addActor(joystick2);
		stage.addActor(Deck);
		stage.addActor(Card);
		stage.addActor(CardMagic);
		stage.addActor(slider);
		
		Tween.setWaypointsLimit(10);
		Tween.registerAccessor(Sprite.class, new SpriteAccessor());
		
		InputMultiplexer multiplexer = new InputMultiplexer();
		
		multiplexer.addProcessor(cardStage);
		multiplexer.addProcessor(stage);
		multiplexer.addProcessor(inputProcessor);
		
		Gdx.input.setInputProcessor(multiplexer);
		
		Nodos = new Nodo[38][38];
		for(int j=0;j<38;j++)
			for(int i=0;i<38;i++){
				Nodo nodo = new Nodo(i,j);
				Nodos[j][i] = nodo;
			}		
		
		NODOSELECTED  = null;
		AlcanceMP = new LinkedList<Vector2>();
		AlcanceHabilidad = new LinkedList<Vector2>();
		listTarget = new LinkedList<Vector2>();
		
		Vector2 posMapaBack = funciones.SeleccionarPos(0,18);
		
		MapBack.setPosition(posMapaBack.x, posMapaBack.y);
		
	}
	
	private void agregarActors(){
		contorno = joypadtextures.findRegion("contorno800");
		buttonTexture = joypadtextures.findRegion("boton800");
		
		TouchpadStyle style = new TouchpadStyle(new TextureRegionDrawable(contorno),new TextureRegionDrawable(buttonTexture));
		joystick2 = new Touchpad(1,style );
		joystick2.setPosition(20, 150);
		
		TextureRegion textureDeckUp = new TextureRegion(new Texture(Gdx.files.internal("data/deck_up.png")),0,0,64,38);
		TextureRegion textureDeckDown = new TextureRegion(new Texture(Gdx.files.internal("data/deck_down.png")),0,0,64,38);
		
		ImageButtonStyle styleButtonDeck= new ImageButtonStyle();
		styleButtonDeck.up=new TextureRegionDrawable(textureDeckUp);
		styleButtonDeck.down=new TextureRegionDrawable(textureDeckDown);
		Deck=new ImageButton(styleButtonDeck);
		Deck.setPosition(3, 434);
		
		TextureRegion textureCardUp = new TextureRegion(new Texture(Gdx.files.internal("data/card_up.png")),0,0,42,60);
		TextureRegion textureCardDown = new TextureRegion(new Texture(Gdx.files.internal("data/card_down.png")),0,0,42,60);
		
		ImageButtonStyle styleButtonCard= new ImageButtonStyle();
		styleButtonCard.up=new TextureRegionDrawable(textureCardUp);
		styleButtonCard.down=new TextureRegionDrawable(textureCardDown);
		
		Card=new ImageButton(styleButtonCard);
		Card.setPosition(100, 420);
		
		TextureRegion textureCardMagicUp = new TextureRegion(new Texture(Gdx.files.internal("data/card_magic_up.png")),0,0,42,60);
		TextureRegion textureCardMagicDown = new TextureRegion(new Texture(Gdx.files.internal("data/card_magic_down.png")),0,0,42,60);
		
		ImageButtonStyle styleButtonMagicCard= new ImageButtonStyle();
		styleButtonMagicCard.up=new TextureRegionDrawable(textureCardMagicUp);
		styleButtonMagicCard.down=new TextureRegionDrawable(textureCardMagicDown);
		
		CardMagic=new ImageButton(styleButtonMagicCard);
		CardMagic.setPosition(150, 420);
	}
	
	private void AgregarListener(){
		
		InputListener cardListener = new InputListener() {
	    	public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
	            return true;
	    }
	    	public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
	    }
	};
		
		joystick2.addListener(new ChangeListener() {
	         @Override
	         public void changed (ChangeEvent event, Actor actor) {
	            Touchpad touchpad = (Touchpad) actor;
	            porcentx = (int)(touchpad.getKnobPercentX()*6);
	            porcenty = (int)(touchpad.getKnobPercentY()*6);
	         }
	         
	      });
		
		slider.addListener(new ChangeListener() {
	         @Override
	         public void changed (ChangeEvent event, Actor actor) {
	        	 camera.zoom = 1+slider.getValue()/100;
	        	 CamMaxX = (2000*factorH-Width*camera.zoom)/2;
	        	 CamMinX = -(2000*factorH-Width*camera.zoom)/2;
	     		
	        	 CamMaxY = (1260*factorH-Height*camera.zoom)/2-35*factorH;
	        	 CamMinY = -(1260*factorH-Height*camera.zoom)/2-35*factorH;
	        	 
	        	 float x = 0;
	     	   	float y = 0;
	     	
	     	   	if((camera.position.x+1)>CamMaxX)
	     	   		x=(CamMaxX-1)-camera.position.x;
	     	   	if((camera.position.x-1)<CamMinX)
	     	   		x=(CamMinX+1)-camera.position.x;
	     	    if((camera.position.y+1)>CamMaxY)
	     	    	y=(CamMaxY-1)-camera.position.y;
	     	   	if((camera.position.y-1)<CamMinY)
	     	   		y=(CamMinY+1)-camera.position.y;
	     	   	
	     	   	camera.translate(x, y);
	     	   	
	     	   mod[2] -= x;
	     	   mod[3] -= y;
	         }
	         
	      });
		
		
		Deck.addListener(new InputListener() {
	    	public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
	    		ObjectData hero = game.mNativeFunctions.getHeroData(1);
	    		System.out.println(hero.nombre);
	    		return true;
	    }});
		
		Card.addListener(new InputListener() {
	    	public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
	    		Objeto = new Object(startgamescreen,ObjectData);
	    		DraggedFicha = new Sprite(Objeto.getSprite());
	    		DraggedFicha.setColor(1, 1, 1, 0.7f);
	    		enableDragged();
	    		SIZESELECTED = ObjectData.size;
	    		if(NODOSELECTED!=null)
	    			Nodos[(int)NODOSELECTED.y][(int)NODOSELECTED.x].ficha.unselected();
	    		cardStage.clear();
				ESTADO = UNSELECTED;
				NODOSELECTED = null;
	            return true;
	    }
	    	public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
	    		if(SIZESELECTED==2){
	    			if(	!Nodos[(int)TempPosicionMat.y][(int)TempPosicionMat.x].ocupado && 
	    				!Nodos[(int)TempPosicionMat.y][(int)TempPosicionMat.x+1].ocupado &&
	    				!Nodos[(int)TempPosicionMat.y+1][(int)TempPosicionMat.x].ocupado &&
	    				!Nodos[(int)TempPosicionMat.y+1][(int)TempPosicionMat.x+1].ocupado ){
	    				
	    				Objeto.setPositionFicha(TempPosicionMat.x,TempPosicionMat.y);
		            	Nodos[(int)TempPosicionMat.y][(int)TempPosicionMat.x].ficha=Objeto;
		            	Nodos[(int)TempPosicionMat.y][(int)TempPosicionMat.x].size = Objeto.size;
		            	Nodos[(int)TempPosicionMat.y][(int)TempPosicionMat.x].ficha.IdFicha = indexFicha;
		            	
		            	Nodos[(int)TempPosicionMat.y][(int)TempPosicionMat.x].ocupado = true;
		            	Nodos[(int)TempPosicionMat.y][(int)TempPosicionMat.x+1].ocupado = true;
		            	Nodos[(int)TempPosicionMat.y+1][(int)TempPosicionMat.x].ocupado = true;
		            	Nodos[(int)TempPosicionMat.y+1][(int)TempPosicionMat.x+1].ocupado = true;
		            	
		            	Nodos[(int)TempPosicionMat.y][(int)TempPosicionMat.x+1].Reflect = true;
		            	Nodos[(int)TempPosicionMat.y+1][(int)TempPosicionMat.x].Reflect = true;
		            	Nodos[(int)TempPosicionMat.y+1][(int)TempPosicionMat.x+1].Reflect = true;
		            	
		            	Nodos[(int)TempPosicionMat.y][(int)TempPosicionMat.x+1].nodoReflect = TempPosicionMat;
		            	Nodos[(int)TempPosicionMat.y+1][(int)TempPosicionMat.x].nodoReflect = TempPosicionMat;
		            	Nodos[(int)TempPosicionMat.y+1][(int)TempPosicionMat.x+1].nodoReflect =TempPosicionMat;
		            	indexFicha+=1;
	    			}
	    		}
	    		else{
	    			if(!Nodos[(int)TempPosicionMat.y][(int)TempPosicionMat.x].ocupado){
		            	Object tem = new Object(startgamescreen, ObjectData);
		            	tem.setPositionFicha(TempPosicionMat.x,TempPosicionMat.y);
		            	Nodos[(int)TempPosicionMat.y][(int)TempPosicionMat.x].ficha=tem;
		            	Nodos[(int)TempPosicionMat.y][(int)TempPosicionMat.x].ficha.IdFicha = indexFicha;
		            	Nodos[(int)TempPosicionMat.y][(int)TempPosicionMat.x].ocupado = true;
		            	indexFicha+=1;
		            }
	    		}
	            desableDragged();
	    }
	});
		
		joystick2.addListener(cardListener);		
		Deck.addListener(cardListener);
		Card.addListener(cardListener);
	}

	@Override
	public void dispose() {
		batch.dispose();
	}

	
	private void enableDragged(){
		dragged = true;
	}
	
	private void desableDragged(){
		dragged = false;
	}
	
	private void InputHandler(){
		if(Gdx.input.isTouched()){
			Vector2 pos = funciones.convertirPuntero(Gdx.input.getX(), Gdx.input.getY(),camera,mod);
			Vector2 posMat = funciones.CalcularPosicionMat(pos.x, pos.y);
			if(dragged){
				Vector2 posFicha = funciones.SeleccionarPos(posMat.x, posMat.y);
				TempPosicionMat = new Vector2(posMat);
				DraggedFicha.setPosition(posFicha.x-Objeto.offsetW, posFicha.y+Objeto.offsetH);
			}
			if(atdragged){
				funciones.Untarget(listTarget,HABILIDAD,Nodos);
				touchTarget=verificarArea(posMat);
				if(touchTarget){
					funciones.targetTouch(posMat,listTarget,AlcanceMP,AlcanceHabilidad,HABILIDAD,Nodos,NODOSELECTED);
				}
			}
		}
		if(Gdx.input.isKeyPressed(Input.Keys.A)) {
            camera.zoom += 0.02;
		}
		if(Gdx.input.isKeyPressed(Input.Keys.S)) {
            camera.zoom -= 0.02;
		}
	}
	
	public void AreaDraw(){
		Iterator<Vector2> iter = AlcanceMP.iterator();
			while(iter.hasNext()){
				Vector2 posicion = iter.next();
				areaTarget.setPosition(posicion.x, posicion.y);
				areaTarget.draw(batch);			
		}
	}
	
	
	
	/*Con esta funcion se verifica que el puntero este sobre una pocicion en la matriz donde es posible seleccionar
	 *un area, si el puntero se encuentra fuera de rango entonces se retornala falso*/
	private boolean verificarArea(Vector2 posMat){
		Iterator<Vector2> iter = Nodos[(int)NODOSELECTED.y][(int)NODOSELECTED.x].ficha.AlcanceHabilidad.iterator();
		while(iter.hasNext()){
			Vector2 posicion = iter.next();
			if(posMat.x==posicion.x&&posMat.y==posicion.y)
				return true;
		}
		return false;
	}
	
	@Override
	public void resize(int width, int height) {
		mod[0] = width/2;
		mod[1] = height/2;
	}
	/*El imputo procesor maneja los eventos de entrada sobre el campo de juego, esto no incluye los actores
	 * como botones o controles*/
	private final InputProcessor inputProcessor = new InputAdapter() {
		@Override
		public boolean touchDown(int x, int y, int pointer, int button) {
			/*Se manejan 3 estados de juego:
			 * el primero UNSELECTED: que indica que ninguna ficha esta seleccionada
			 * el segundo SELECTED: que indica que se ha seleccionado una ficha, en este 
			 * estado tambien es posible mover la ficha tantos espacios como tenga disponibles
			 * el tercero READY: que indica que la ficha esta lista para ejecutar una habilidad*/
			
			/*Antes de interactuar con las fichas y el campo se verifica que la lista de fichas en
			 *el campo no este vacia*/
			if(indexFicha==0)
				return true;
			
			/*Se convierte el puntero a las coordenadas de el juego*/
			Vector2 puntero = funciones.convertirPuntero(x, y, camera, mod);
			
			/*Se recupera un puntero con la ubicacion en la matris del juego*/
			Vector2 indexTemp = funciones.CalcularPosicionMat(puntero.x, puntero.y);
			
			/*Se procede a ejecutar los eventes deacuerdo al estado actual del juego*/
			switch (ESTADO){
			/*Si el estado es UNSELECTED se verificara si la posicion seleccionada esta vacia o pertenece a una ficha,
			 * si pertenece a una ficha se le asignara  la variable de estado del juego y luego se agregan los actoren
			 * a la ficha seleccionada*/
			case UNSELECTED	:
					if(Nodos[(int)indexTemp.y][(int)indexTemp.x].ocupado){
						if(Nodos[(int)indexTemp.y][(int)indexTemp.x].size==2){
							NODOSELECTED =  new Vector2(indexTemp);
							ESTADO = SELECTED;
							Nodos[(int)NODOSELECTED.y][(int)NODOSELECTED.x].ficha.selected();
							Nodos[(int)NODOSELECTED.y][(int)NODOSELECTED.x].ficha.addButton(cardStage);
							SIZESELECTED = Nodos[(int)NODOSELECTED.y][(int)NODOSELECTED.x].size;
						}else if(Nodos[(int)indexTemp.y][(int)indexTemp.x].Reflect){
								Vector2 reflectpos = Nodos[(int)indexTemp.y][(int)indexTemp.x].nodoReflect;
								NODOSELECTED =  new Vector2(reflectpos);
								ESTADO = SELECTED;
								Nodos[(int)NODOSELECTED.y][(int)NODOSELECTED.x].ficha.selected();
								Nodos[(int)NODOSELECTED.y][(int)NODOSELECTED.x].ficha.addButton(cardStage);
								SIZESELECTED = Nodos[(int)NODOSELECTED.y][(int)NODOSELECTED.x].size;
							}else{
								NODOSELECTED =  new Vector2(indexTemp);
								ESTADO = SELECTED;
								Nodos[(int)NODOSELECTED.y][(int)NODOSELECTED.x].ficha.selected();
								Nodos[(int)NODOSELECTED.y][(int)NODOSELECTED.x].ficha.addButton(cardStage);
								SIZESELECTED = Nodos[(int)NODOSELECTED.y][(int)NODOSELECTED.x].size;
							}
					}
					break;
			/*Si la ficha esta seleccionada, es posible que se este seleccionando a un espacio vacio, si la pocicion
			 * se encuentra dentro de el rango de mobilidad de la ficha, se puede ejecutar el movimiento, si se encuentra
			 * fuera del rando la ficha pasaria a un estado UNSELECTED, por lo que se verifica si la posicion seleccionada es tiene valor -1
			 * si no ocurre eso es posible que se este seleccionando otra ficha, por lo que se procede a seleccionar
			 * la ultima ficha seleccionada que estaria en la variable de estado y se limpia el Stage de los actores de la ficha
			 * luego se procede a asignar el nuevo valor a la variable de estado y se agregan los actores que pertenecen a la
			 * nueva ficha seleccionada */
			case SELECTED	:
					if(!Nodos[(int)indexTemp.y][(int)indexTemp.x].ocupado){
						if(Nodos[(int)NODOSELECTED.y][(int)NODOSELECTED.x].ficha.ifPasos(indexTemp)){
							if(SIZESELECTED == 2){
								indexTemp = funciones.getReflect(indexTemp, Nodos);
								List<Vector2> pasos = funciones.getPasos(indexTemp, Nodos);
								
								Nodo nodoAUX = Nodos[(int)NODOSELECTED.y][(int)NODOSELECTED.x];
								
								Nodos[(int)NODOSELECTED.y][(int)NODOSELECTED.x] = new Nodo((int)NODOSELECTED.x,(int)NODOSELECTED.y);
								Nodos[(int)NODOSELECTED.y][(int)NODOSELECTED.x+1] = new Nodo((int)NODOSELECTED.x+1,(int)NODOSELECTED.y);
								Nodos[(int)NODOSELECTED.y+1][(int)NODOSELECTED.x] = new Nodo((int)NODOSELECTED.x,(int)NODOSELECTED.y+1);
								Nodos[(int)NODOSELECTED.y+1][(int)NODOSELECTED.x+1] = new Nodo((int)NODOSELECTED.x+1,(int)NODOSELECTED.y+1);
								
								Nodos[(int)indexTemp.y][(int)indexTemp.x] = nodoAUX;
								
				            	Nodos[(int)indexTemp.y][(int)indexTemp.x+1].ocupado = true;
				            	Nodos[(int)indexTemp.y+1][(int)indexTemp.x].ocupado = true;
				            	Nodos[(int)indexTemp.y+1][(int)indexTemp.x+1].ocupado = true;
				            	
				            	Nodos[(int)indexTemp.y][(int)indexTemp.x+1].Reflect = true;
				            	Nodos[(int)indexTemp.y+1][(int)indexTemp.x].Reflect = true;
				            	Nodos[(int)indexTemp.y+1][(int)indexTemp.x+1].Reflect = true;
				            	
				            	Nodos[(int)indexTemp.y][(int)indexTemp.x+1].nodoReflect = indexTemp;
				            	Nodos[(int)indexTemp.y+1][(int)indexTemp.x].nodoReflect = indexTemp;
				            	Nodos[(int)indexTemp.y+1][(int)indexTemp.x+1].nodoReflect = indexTemp;
				            	
				            	NODOSELECTED = indexTemp;
				            									
								Nodos[(int)NODOSELECTED.y][(int)NODOSELECTED.x].ficha.MoveFicha(pasos, indexTemp);
								
								Nodos[(int)NODOSELECTED.y][(int)NODOSELECTED.x].setPosicion((int)indexTemp.x,(int) indexTemp.y);
								Nodos[(int)NODOSELECTED.y][(int)NODOSELECTED.x].ficha.selected();
							}
							
							else{
								List<Vector2> pasos = funciones.getPasos(indexTemp, Nodos);
								
								Nodos[(int)indexTemp.y][(int)indexTemp.x] = Nodos[(int)NODOSELECTED.y][(int)NODOSELECTED.x];
								Nodos[(int)NODOSELECTED.y][(int)NODOSELECTED.x] = new Nodo((int)NODOSELECTED.x,(int)NODOSELECTED.y);
								NODOSELECTED = indexTemp;
								Nodos[(int)NODOSELECTED.y][(int)NODOSELECTED.x].ficha.MoveFicha(pasos, indexTemp);
								Nodos[(int)NODOSELECTED.y][(int)NODOSELECTED.x].x = (int)NODOSELECTED.x;
								Nodos[(int)NODOSELECTED.y][(int)NODOSELECTED.x].y = (int)NODOSELECTED.y;
							}
						}
						else{ 
							cardStage.clear();
							ESTADO = UNSELECTED;
							NODOSELECTED = null;
						}
					}
					else 
						if(Nodos[(int)indexTemp.y][(int)indexTemp.x].Reflect){
							Nodos[(int)NODOSELECTED.y][(int)NODOSELECTED.x].ficha.unselected();
							cardStage.clear();
							Vector2 reflectpos = Nodos[(int)indexTemp.y][(int)indexTemp.x].nodoReflect;
							NODOSELECTED =  new Vector2(reflectpos);
							ESTADO = SELECTED;
							Nodos[(int)NODOSELECTED.y][(int)NODOSELECTED.x].ficha.selected();
							Nodos[(int)NODOSELECTED.y][(int)NODOSELECTED.x].ficha.addButton(cardStage);
							SIZESELECTED = Nodos[(int)NODOSELECTED.y][(int)NODOSELECTED.x].size;
							}
						else{
							Nodos[(int)NODOSELECTED.y][(int)NODOSELECTED.x].ficha.unselected();
							cardStage.clear();
							NODOSELECTED = new Vector2(indexTemp);
							ESTADO = SELECTED;
							Nodos[(int)NODOSELECTED.y][(int)NODOSELECTED.x].ficha.selected();
							Nodos[(int)NODOSELECTED.y][(int)NODOSELECTED.x].ficha.addButton(cardStage);
							SIZESELECTED = Nodos[(int)NODOSELECTED.y][(int)NODOSELECTED.x].size;
							}
					break;
			/*Si se ha seleccionado uno de los botones de habilidad de la ficha entra a un estado de READY que puede perderse al
			 * seleccionar un espacion en blanco con valor -1, se lo contrario se porcede a ejecutar la habilidad deacuerdo al tipo
			 * de habilidad*/
			case READY		:
					SelecetHabilidad(indexTemp);
					break;
			}
			return true;
		}
		
		public boolean touchUp(int x, int y, int pointer, int button) {
			if(atdragged){
				atdragged=false;
				Nodos[(int)NODOSELECTED.y][(int)NODOSELECTED.x].ficha.usarHabilidad(listTarget);
				funciones.Untarget(listTarget,HABILIDAD,Nodos);
			}
			return true;
		}
		
	};
	
	public Object getFichaById(int id){
		for(int i = 0 ; i< 38; i++)
			for(int j = 0; j<38; j++){
				if(Nodos[j][i].ficha.IdFicha == id)
					return Nodos[j][i].ficha;
			}
		return null;
	}
	
	public void SelecetHabilidad(Vector2 indexTemp){
		switch (HABILIDAD){
		/*Targeteo simple amistoso*/
			case STFTYPE	:
				Nodos[(int)NODOSELECTED.y][(int)NODOSELECTED.x].ficha.usarHabilidad(indexTemp);
				break;
				/*Targeteo simple para enemigo*/
			case STETYPE	:
				Nodos[(int)NODOSELECTED.y][(int)NODOSELECTED.x].ficha.usarHabilidad(indexTemp);
				break;
				/*Targeteo de area amistoso*/
			case ATFTYPE	:
				atdragged=true;			
				break;
				/*Targeteo de area para enemigos*/
			case ATETYPE	:
				Nodos[(int)NODOSELECTED.y][(int)NODOSELECTED.x].ficha.usarHabilidad(indexTemp);
				break;
				/*Targeteo de area local amistoso*/
			case ALFTYPE	:
				Nodos[(int)NODOSELECTED.y][(int)NODOSELECTED.x].ficha.usarHabilidad(indexTemp);
				break;
				/*Targeteo de area local para enemigos*/
			case ALETYPE	:
				Nodos[(int)NODOSELECTED.y][(int)NODOSELECTED.x].ficha.usarHabilidad(indexTemp);
				break;
				/*Auto lanzar habilidad*/
			case ATYPE	:
				Nodos[(int)NODOSELECTED.y][(int)NODOSELECTED.x].ficha.usarHabilidad(indexTemp);
				break;
		}
	}
	
	public String getSize() {
	      String input = "";

	      try {
	         URL url = new URL(
	               "http://190.118.213.102/mb/filesize.php");
	         URLConnection connection = url.openConnection();
	         connection.setConnectTimeout(3000);
	         connection.connect();
	         BufferedReader br = new BufferedReader(new InputStreamReader(
	               connection.getInputStream()));
	         String line;
	         while ((line = br.readLine()) != null) {
	            input += line;
	         }
	         br.close();
	      } catch (IOException e) {
	         e.printStackTrace();
	         input = "";
	      } 
	      return input;
	   }
	@Override
	public void hide() {
		// TODO Auto-generated method stub
		
	}
	
}