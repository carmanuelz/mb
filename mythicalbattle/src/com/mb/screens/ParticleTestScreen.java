package com.mb.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.ParticleEmitter;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.mb.actions.BallTargetSpell;
import com.mb.utils.Funciones;
import com.mb.utils.SpriteAccessor;

import aurelienribon.tweenengine.BaseTween;
import aurelienribon.tweenengine.Timeline;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenCallback;
import aurelienribon.tweenengine.TweenManager;
import aurelienribon.tweenengine.TweenPaths;
import aurelienribon.tweenengine.equations.Linear;
import aurelienribon.tweenengine.equations.Quad;

public class ParticleTestScreen extends AbstractScreen{
	public final TweenManager tweenManager = new TweenManager();
	private OrthographicCamera camera;
	private SpriteBatch batch;
	BallTargetSpell ballspell;
	Funciones funciones = new Funciones(1, 38);
	boolean isrendereffect = false;

	public ParticleTestScreen(MainScreen game) {
		super(game);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void render(float delta) {
		// TODO Auto-generated method stub
		Gdx.gl.glClearColor(0f, 0f, 0f, 1);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		batch.setProjectionMatrix(camera.combined);
		tweenManager.update(delta);
		batch.begin();
		if(isrendereffect)
			ballspell.render(batch, delta);
		batch.end();
		
	}

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void show() {
		// TODO Auto-generated method stub
		int Width = Gdx.graphics.getWidth();
		int Height = Gdx.graphics.getHeight();
		
		camera = new OrthographicCamera(Width,Height);
		batch = new SpriteBatch();
		
		ballspell = new BallTargetSpell(tweenManager);
		
		Tween.setWaypointsLimit(10);
		Tween.registerAccessor(Sprite.class, new SpriteAccessor());
		Gdx.input.setInputProcessor(inputProcessor);
	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub
		
	}
	private final InputProcessor inputProcessor = new InputAdapter() {
		@Override
		public boolean touchDown(int x, int y, int pointer, int button) {
			return true;
		}
	};

}
