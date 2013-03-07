package com.mb.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.assets.AssetManager;
import com.mb.utils.NativeFunctions;

/**
 * @author Mats Svensson
 */
public class mainscreen extends Game {

    /**
     * Holds all our assets
     */
	
	public NativeFunctions mNativeFunctions;
	public mainscreen(NativeFunctions nativeFunctions){
		mNativeFunctions = nativeFunctions;
	    //mNativeFunctions.cliente();
		//mNativeFunctions.getConnection();
		mNativeFunctions.DownloadDB(360449);
	}
    public AssetManager manager = new AssetManager();

    @Override
    public void create() {
        setScreen(new LoadingScreen(this));
    }
}