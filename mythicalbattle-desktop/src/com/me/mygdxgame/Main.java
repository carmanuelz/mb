package com.me.mygdxgame;

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

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

public class Main implements NativeFunctions {
	
	private String url = "jdbc:sqlite:../mythicalbattle-android/assets/dx/mythbattle.sqlite";
	private Connection connection = null;
	public CardData CardData;
	
	public int percent = 100;
	
	public static void main(String[] args) {
		LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
		cfg.title = "mythicalbattle";
		cfg.useGL20 = false;
		cfg.width = 800;
		cfg.height = 480;
		Main game = new Main();		
		new LwjglApplication(new mainscreen(game), cfg);
	}
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
public void getConnection() {
        try {
                Class.forName("org.sqlite.JDBC");
                connection = DriverManager.getConnection(url);
        } catch (ClassNotFoundException e) {
                e.printStackTrace();
        } catch (SQLException e) {
                e.printStackTrace();
        }
        CardData = new CardData(connection);
}

@Override
public void DownloadDB(int size) {
	// TODO Auto-generated method stub
	
}
@Override
public float getPercent() {
	// TODO Auto-generated method stub
	if(percent>0)
		return percent;
	return 0;
}
@Override
public ObjectData getHeroData(int id) {
	// TODO Auto-generated method stub
	return CardData.getHeroData(id);
}
@Override
public ObjectData getRelicData(int id) {
	// TODO Auto-generated method stub
	return CardData.getRelicData(id);
}
@Override
public ObjectData getTowerData(int id) {
	// TODO Auto-generated method stub
	return CardData.getTowerData(id);
}
@Override
public EquipData getEquipData(int id) {
	// TODO Auto-generated method stub
	return CardData.getEquipData(id);
}
@Override
public SpellData getSpellData(int id) {
	// TODO Auto-generated method stub
	return CardData.getSpellData(id);
}

}
