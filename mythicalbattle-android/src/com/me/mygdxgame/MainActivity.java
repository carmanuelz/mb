package com.me.mygdxgame;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.URL;
import java.net.URLConnection;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.concurrent.Executors;

import org.jboss.netty.bootstrap.ClientBootstrap;
import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.channel.socket.nio.NioClientSocketChannelFactory;
import org.jboss.netty.handler.codec.serialization.ClassResolvers;
import org.jboss.netty.handler.codec.serialization.ObjectDecoder;
import org.jboss.netty.handler.codec.serialization.ObjectEncoder;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;

public class MainActivity extends AndroidApplication implements NativeFunctions {
		
	private static String file_url = "https://dl.dropbox.com/u/79250909/mythbattle.sqlite";	
	String url = "jdbc:sqldroid:/data/data/com.me.mygdxgame/files/mythbattle.sqlite";
	private Connection connection;
	public CardData CardData;
	
    public int filesize = 0;
    public int percent = 0;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        AndroidApplicationConfiguration cfg = new AndroidApplicationConfiguration();
        cfg.useGL20 = false;        
        initialize(new mainscreen(this), cfg);
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
        bootstrap.connect(new InetSocketAddress("192.168.0.13", 8080));
    	//System.out.println("hola");
    	
    }

@Override
public void getConnection() {
	 try {
         Class.forName("org.sqldroid.SQLDroidDriver").newInstance();
         connection = DriverManager.getConnection(url);
         System.out.println("Conecccion Satisfactoria");
     } catch (java.sql.SQLException e) {
         // TODO Auto-generated catch block
         e.printStackTrace();
     } catch (ClassNotFoundException e) {
         // TODO Auto-generated catch block
         e.printStackTrace();
     } catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	 CardData = new CardData(connection); 
}



public void DownloadDB(int size){
	filesize = size;
	new DownloadFileFromURL().execute(file_url);
}

	class DownloadFileFromURL extends AsyncTask<String, String, String> {
		/**
		 * Before starting background thread
		 * Show Progress Bar Dialog
		 * */
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}
	
		/**
		 * Downloading file in background thread
		 * */
		@Override
		protected String doInBackground(String... f_url) {
			int count;
	        try {
	            URL url = new URL(f_url[0]);
	            URLConnection conection = url.openConnection();
	            conection.connect();
	            // getting file length
	            int lenghtOfFile = filesize;
	
	            // input stream to read file - with 8k buffer
	            InputStream input = new BufferedInputStream(url.openStream(), 8192);
	       
	            // Output stream to write file
	            OutputStream output = openFileOutput("mythbattle.sqlite", Context.MODE_PRIVATE);
	            byte data[] = new byte[1024];
	
	            long total = 0;
	
	            while ((count = input.read(data)) != -1) {
	                total += count;
	                // publishing the progress....
	                // After this onProgressUpdate will be called
	                percent=(int)((total*100)/lenghtOfFile);
	                
	                // writing data to file
	                output.write(data, 0, count);
	            }
	
	            // flushing output
	            output.flush();
	            
	            // closing streams
	            output.close();
	            input.close();
	            
	        } catch (Exception e) {
	        	Log.e("Error: ", e.getMessage());
	        }
	        
	        return null;
		}
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