package com.mb.main;

import java.net.InetSocketAddress;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.concurrent.Executors;

import org.jboss.netty.bootstrap.ClientBootstrap;
import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.channel.socket.nio.NioClientSocketChannelFactory;
import org.jboss.netty.handler.codec.serialization.ClassResolvers;
import org.jboss.netty.handler.codec.serialization.ObjectDecoder;
import org.jboss.netty.handler.codec.serialization.ObjectEncoder;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

import com.mb.data.DescriptionMapData;
import com.mb.data.RecoveryData;
import com.mb.data.EquipData;
import com.mb.data.ObjectData;
import com.mb.data.SpellData;
import com.mb.data.CardData; 
import com.mb.net.ObjectEchoClientHandler;
import com.mb.screens.MainScreen;
import com.mb.utils.NativeFunctions;

public class Main implements NativeFunctions {

	private String url = "jdbc:sqlite:mbbd/mythbattle.sqlite";
	private Connection connection = null;
	public RecoveryData RecoveryData;
	public int percent = 100;

	public static void main(String[] args) {
		LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
		cfg.title = "mythicalbattle";
		cfg.useGL20 = false;
		cfg.width = 800;
		cfg.height = 480;
		Main game = new Main();		
		new LwjglApplication(new MainScreen(game), cfg);
	}
	@Override
	public void cliente(){
    	
    	ClientBootstrap bootstrap = new ClientBootstrap(
                new NioClientSocketChannelFactory(
                        Executors.newCachedThreadPool(),
                        Executors.newCachedThreadPool()));

        // Set up the pipeline factory.
        bootstrap.setPipelineFactory(new ChannelPipelineFactory() {
            public ChannelPipeline getPipeline() throws Exception {
                return Channels.pipeline(
                        new ObjectEncoder(),
                        new ObjectDecoder(ClassResolvers.cacheDisabled(getClass().getClassLoader())),
                        new ObjectEchoClientHandler());
            }
        });

        // Start the connection attempt.
        bootstrap.connect(new InetSocketAddress("localhost", 8080));
    	
    }
	@Override
	public Connection getConnection() {
	        try {
	                Class.forName("org.sqlite.JDBC");
	                connection = DriverManager.getConnection(url);
	        } catch (ClassNotFoundException e) {
	                e.printStackTrace();
	        } catch (SQLException e) {
	                e.printStackTrace();
	        }
	        return connection;
	}
	
	@Override
	public void DownloadDB() {
	}
	@Override
	public float getPercent() {
		if(percent>0)
			return percent;
		return 0;
	}
	@Override
	public ObjectData getHeroData(int id) {
		connection = getConnection();
		RecoveryData = new RecoveryData(connection);
		ObjectData hero = RecoveryData.getHeroData(id);
		try {
			connection.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return hero;
	}
	@Override
	public ObjectData getRelicData(int id) {
		connection = getConnection();
		RecoveryData = new RecoveryData(connection);
		ObjectData relic = RecoveryData.getRelicData(id);
		try {
			connection.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return relic;
	}
	@Override
	public ObjectData getTowerData(int id) {
		connection = getConnection();
		RecoveryData = new RecoveryData(connection);
		ObjectData tower = RecoveryData.getTowerData(id);
		try {
			connection.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return tower;
	}
	@Override
	public EquipData getEquipData(int id) {
		connection = getConnection();
		RecoveryData = new RecoveryData(connection);
		EquipData equip = RecoveryData.getEquipData(id);
		try {
			connection.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return equip;
	}
	@Override
	public SpellData getSpellData(int id) {
		connection = getConnection();
		RecoveryData = new RecoveryData(connection);
		SpellData spell = RecoveryData.getSpellData(id);
		try {
			connection.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return spell;
	}
	@Override
	public CardData getCardData(int id) {
		connection = getConnection();
		RecoveryData = new RecoveryData(connection);
		CardData card = RecoveryData.getCardData(id);
		try {
			connection.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return card;
	}
	@Override
	public DescriptionMapData getMapData(int id) {
		connection = getConnection();
		RecoveryData = new RecoveryData(connection);
		DescriptionMapData map = RecoveryData.getMapData(id);
		try {
			connection.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return map;
	}
	
}