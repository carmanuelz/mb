package com.mb.main;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.mb.data.CardData;
import com.mb.data.DescriptionMapData;
import com.mb.data.EquipData;
import com.mb.data.ObjectData;
import com.mb.data.RecoveryData;
import com.mb.data.SpellData;
import com.mb.net.Cliente;
import com.mb.net.ObjectEchoClientHandler;
import com.mb.screens.MainScreen;
import com.mb.utils.NativeFunctions;

public class MainActivity extends AndroidApplication implements NativeFunctions {

	private static String file_url = "https://dl.dropboxusercontent.com/u/79250909/mythbattle.sqlite";	
	String url = "jdbc:sqldroid:/data/data/com.mb.main/files/mythbattle.sqlite";
	private Connection connection;
	public RecoveryData RecoveryData;

    public int filesize = 0;
    public int percent = 0;

	Cliente cliente;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        AndroidApplicationConfiguration cfg = new AndroidApplicationConfiguration();
        cfg.useGL20 = true;        
        initialize(new MainScreen(this), cfg);
    }
    
    @Override
	public void cliente(){   
    	EventLoopGroup group = new NioEventLoopGroup();
        try {
            Bootstrap b = new Bootstrap();
            b.group(group)
             .channel(NioSocketChannel.class)
             .handler(new ChannelInitializer<SocketChannel>() {
                @Override
                public void initChannel(SocketChannel ch) throws Exception {
                    ch.pipeline().addLast(
                            new ObjectEncoder(),
                            new ObjectDecoder(ClassResolvers.cacheDisabled(getClass().getClassLoader())),
                            new ObjectEchoClientHandler());
                }
             });

            // Start the connection attempt.
            try {
				b.connect("192.168.1.13", 8080).sync().channel();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        } finally {
            group.shutdownGracefully();
        }	
    }

@Override
public Connection getConnection() {
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
	 return connection;
}



	public void DownloadDB(){
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

		public String getSize() {
		      String input = "";

		      try {
		         URL httpurl = new URL(
		               "http://lazonanegativa.com//mb/filesize.php");
		         URLConnection httpconn = httpurl.openConnection();
		         httpconn.setConnectTimeout(3000);
		         httpconn.connect();
		         BufferedReader br = new BufferedReader(new InputStreamReader(
		        		 httpconn.getInputStream()));
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

		/**
		 * Downloading file in background thread
		 * */
		@Override
		protected String doInBackground(String... f_url) {
			filesize = 524288;//Integer.parseInt(getSize());
			System.out.println(filesize);
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
		connection = getConnection();
		RecoveryData = new RecoveryData(connection);
		ObjectData hero = RecoveryData.getHeroData(id);
		try {
			connection.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return hero;
	}
	@Override
	public ObjectData getRelicData(int id) {
		// TODO Auto-generated method stub
		connection = getConnection();
		RecoveryData = new RecoveryData(connection);
		ObjectData relic = RecoveryData.getRelicData(id);
		try {
			connection.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return relic;
	}
	@Override
	public ObjectData getTowerData(int id) {
		// TODO Auto-generated method stub
		connection = getConnection();
		RecoveryData = new RecoveryData(connection);
		ObjectData tower = RecoveryData.getTowerData(id);
		try {
			connection.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return tower;
	}
	@Override
	public EquipData getEquipData(int id) {
		// TODO Auto-generated method stub
		connection = getConnection();
		RecoveryData = new RecoveryData(connection);
		EquipData equip = RecoveryData.getEquipData(id);
		try {
			connection.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return equip;
	}
	@Override
	public SpellData getSpellData(int id) {
		// TODO Auto-generated method stub
		connection = getConnection();
		RecoveryData = new RecoveryData(connection);
		SpellData spell = RecoveryData.getSpellData(id);
		try {
			connection.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return spell;
	}
	@Override
	public CardData getCardData(int id) {
		// TODO Auto-generated method stub
		connection = getConnection();
		RecoveryData = new RecoveryData(connection);
		CardData card = RecoveryData.getCardData(id);
		try {
			connection.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return card;
	}
	@Override
	public DescriptionMapData getMapData(int id) {
		// TODO Auto-generated method stub
		connection = getConnection();
		RecoveryData = new RecoveryData(connection);
		DescriptionMapData map = RecoveryData.getMapData(id);
		try {
			connection.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return map;
	}
}