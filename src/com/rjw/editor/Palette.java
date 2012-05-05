package com.rjw.editor;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Vector;

import javax.imageio.ImageIO;

import com.rjw.gameskeleton.Animation;
import com.rjw.gameskeleton.OtherStuff;
import com.rjw.gameskeleton.Sprite;
import com.rjw.gameskeleton.Thing;
import com.rjw.gameskeleton.Timer;

/**
 * Abstract Palette class, since our 3 palettes have a lot of common code
 */
public abstract class Palette {
	
	private int _currentIndex;
	private BufferedImage _currentItemIcon;
	private Sprite _paletteImage;
	private boolean _canWrap;
	private Timer _paletteSwitchTimer;
	
	/*
	 * @param screenW is the width of the screen we're putting the palette on
	 * @param screenH is the height of the screen we're putting the palette on
	 */
	public Palette(int screenW, int screenH, long timeout){
		
		initPalette(screenW, screenH);
		_currentIndex = 0;
		//updateIcon();
		_canWrap = true;
		_paletteSwitchTimer = new Timer(timeout);

	}//ThingPalette

	/**
	 * This function is called from the constructor and needs to perform
	 * the following: 
	 * -init palette image, and set to correct x/y coords
	 * @param screenH - the height of the parent application's screen
	 * @param screenW - the width of the parent application's screen
	 */
	protected abstract void initPalette(int screenW, int screenH);
	
	/**
	 * Sets the palette icon to a scaled version of the current palette item
	 */
	protected abstract void updateIcon();

	/**
	 * simple function to update the tool switch timer
	 * @param elapsedTime -  the long ms that have elapsed since last update
	 */
	public void updatePaletteItemSwitchTimer(long elapsedTime){ _paletteSwitchTimer.update(elapsedTime); }
	
	/**
	 * This function sets the current palette item to the passed 
	 * index, only if the timer is done. It then calls switchPaletteItem
	 * to "physically" switch around the selected item and associated 
	 * objects (e.g. graphics, sounds, etc.) 
	 * @param index
	 */
	public void setCurrentPaletteItem(int index){

		if(_paletteSwitchTimer.isDone()){
			switchPaletteItem(index);
			_paletteSwitchTimer.reset();
		}
		
	}//setCurrentPaletteItem
	
	/**
	 * This function actually switches the currently selected item
	 */
	protected abstract void switchPaletteItem(int index);
	
	/**
	 * made this a separate function because it's messy and throw away, and 
	 * will eventually be based on external files probably
	 */
	protected abstract void initPaletteSet();
	
	/**
	 * This function returns the currently selected item in the palette
	 * @return - the current item in the palette
	 */
	public abstract Object getCurrentPaletteItem();
	
	// Other Functions
	public void toggleWrapping(){ _canWrap = !_canWrap;}
	public void setCurrentIndex(int index){ _currentIndex = index; }
	public void setPaletteImage(Sprite paletteSprite){ _paletteImage = paletteSprite; }
	public void setPaletteItemIcon(BufferedImage icon){ _currentItemIcon = icon; }
	
	public Sprite getPaletteImage(){ return _paletteImage; }
	public BufferedImage getPaletteItemIcon(){ return _currentItemIcon; }
	public int getCurrentIndex(){ return _currentIndex; }
	public boolean canWrap(){ return _canWrap; }
	public Timer getTimer(){ return _paletteSwitchTimer; }


	
}//Palette
