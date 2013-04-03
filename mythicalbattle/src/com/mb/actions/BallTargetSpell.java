package com.mb.actions;

import aurelienribon.tweenengine.BaseTween;
import aurelienribon.tweenengine.Timeline;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenCallback;
import aurelienribon.tweenengine.TweenManager;
import aurelienribon.tweenengine.TweenPaths;
import aurelienribon.tweenengine.equations.Linear;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.ParticleEmitter;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.mb.objects.Objeto;
import com.mb.utils.SpriteAccessor;

public class BallTargetSpell {
	private Sprite Particle;
	private ParticleEffect effect;
	private ParticleEmitter emitter;
	//private Vector2 TempPos;
	//private Vector2 ActualPos;
	private Vector2 Origen;
	private Vector2 Destino;
	private TweenManager tweenManager;
	private Objeto objetivo;
	private double Time;
	private boolean ready = false;
	
	TweenCallback callback = new TweenCallback() {
		@Override public void onEvent(int type, BaseTween source) {
			switch (type) {
				case START:
					emitter.start();break;
				case COMPLETE:
					emitter.setMaxParticleCount(500);
					emitter.getAngle().setRelative(true);
					emitter.getVelocity().setHigh(200, 200);
					Particle.setColor(1,0,0,0f);
					objetivo.AnimHabilidad();
					break;
			}
		}
	};
	TweenCallback callback2 = new TweenCallback() {
		@Override public void onEvent(int type, BaseTween source) {
			switch (type) {
				case START:break;
				case COMPLETE:break;
			}
		}
	};
	
	public BallTargetSpell(TweenManager tween){		
		effect = new ParticleEffect();
		effect.load(Gdx.files.internal("data/test2.p"), Gdx.files.internal("data"));
		tweenManager = tween;
	}
	
	public void init(Vector2 origen, Objeto objeto){
		Array<ParticleEmitter> emitters = new Array<ParticleEmitter>(effect.getEmitters());
		emitter = emitters.get(0);
		effect.getEmitters().clear();
		effect.getEmitters().add(emitter);
		Origen = new Vector2(origen);
		objetivo = objeto;
		Destino = objetivo.getPositionCoorder();
		Particle = new Sprite(new TextureRegion(new Texture(Gdx.files.internal("data/fireball.png")),0,0,32,32));
		Particle.setColor(1,0,0,7f);
		Particle.setSize(24, 24);
		Particle.setPosition(origen.x+50,origen.y+35);
		Origen.sub(Destino);
		Time = Math.abs((Math.sqrt(Math.pow(Origen.x, 2)+Math.pow(Origen.y, 2)))/250)+0.1;
		Timeline.createSequence()
		.push(Tween.to(Particle,SpriteAccessor.POS_XY, (float)Time-0.3f).target(Destino.x,Destino.y+35).ease(Linear.INOUT)
				.setCallback(callback).setCallbackTriggers(TweenCallback.START | TweenCallback.COMPLETE))
		.push(Tween.to(Particle,SpriteAccessor.POS_XY, 0.3f).target(Destino.x,Destino.y+35).ease(Linear.INOUT)
				.setCallback(callback2).setCallbackTriggers(TweenCallback.START | TweenCallback.COMPLETE))
		.start(tweenManager);
		
		System.out.println(Time);
		emitter.setMaxParticleCount(200);
		emitter.getDuration().setLow((float)Time*1000,1000);
		emitter.getAngle().setRelative(false);
		emitter.getVelocity().setHigh(30, 300);
		emitter.getAngle().setHigh(Origen.angle()+15,Origen.angle()-15);
		ready = true;
	}
	public void render(SpriteBatch batch,float delta){
		if(ready){
			Particle.draw(batch);
			effect.setPosition(Particle.getX()+16, Particle.getY()+10);
			effect.draw(batch, delta);
		}
	}

}
