package com.mb.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.assets.AssetManager;
import com.mb.utils.NativeFunctions;

/**
 * @author Mats Svensson
 */
public class MainScreen extends Game {

    /**
     * Holds all our assets
     */
	
	public NativeFunctions mNativeFunctions;
	public MainScreen(NativeFunctions nativeFunctions){
		mNativeFunctions = nativeFunctions;
	    try {
			mNativeFunctions.cliente();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			System.out.println("no se conecto");
			e.printStackTrace();
		}
		mNativeFunctions.DownloadDB();
		mNativeFunctions.getConnection();
	}
    public AssetManager manager = new AssetManager();

    @Override
    public void create() {
        setScreen(new LoadingScreen(this));
    }
}