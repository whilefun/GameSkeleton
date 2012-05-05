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
import com.rjw.gameskeleton.Avatar;
import com.rjw.gameskeleton.OfficeBuilding001;
import com.rjw.gameskeleton.OtherStuff;
import com.rjw.gameskeleton.Projectile;
import com.rjw.gameskeleton.Sprite;
import com.rjw.gameskeleton.TallStructure;
import com.rjw.gameskeleton.Thing;
import com.rjw.gameskeleton.Tile;
import com.rjw.gameskeleton.Timer;

/**
 * This is our palette of things. It's very close to the tile palette,
 * but it is for world things instead of just tiles.
 * The thing palette image has the following key coords:
 * 2,2: top left of 32x32 pixel for current tile's icon
 * 65,5: top left of a text region for tile's name
 * 65,19: top left of a text region for tile set's name
 */
public class ThingPalette extends Palette{

	// offsets and such
	public final static int THING_PALETTE_ICON_OFFSET = 2;
	public final static int THING_PALETTE_TEXT_X_OFFSET = 65;
	public final static int THING_PALETTE_TEXT_Y_OFFSET_TILE = 14;
	public final static int THING_PALETTE_TEXT_Y_OFFSET_SET = 28;
	public final static long THING_SWITCH_TIMEOUT = 200;
	
	//private Vector<EditorThing> _things;
	private Vector<Thing> _things;
	private Animation _thingAnimation;
	private BufferedImage _thingsImage;

	
	/**
	 * 
	 * @param screenW is the width of the screen we're putting the palette on
	 * @param screenH is the height of the screen we're putting the palette on
	 */
	public ThingPalette(int screenW, int screenH){
		
		super(screenW, screenH, THING_SWITCH_TIMEOUT);
		_things = new Vector<Thing>();
		//_things = new Vector<EditorThing>();
		initPaletteSet();
		updateIcon();

	}//ThingPalette

	// sets our tile icon to a scaled version of the current tile
	protected void updateIcon(){
		
		// set the thing's icon to be the first animation frame
		this.setPaletteItemIcon(new BufferedImage(OtherStuff.EDITOR_TILE_ICON_SIZE,OtherStuff.EDITOR_TILE_ICON_SIZE, BufferedImage.TYPE_INT_RGB));
		Graphics2D  g2D = this.getPaletteItemIcon().createGraphics();
		AffineTransform xform = AffineTransform.getScaleInstance((double)((double)OtherStuff.EDITOR_TILE_ICON_SIZE/(double)OtherStuff.EDITOR_TILE_SIZE),(double)((double)OtherStuff.EDITOR_TILE_ICON_SIZE/(double)OtherStuff.EDITOR_TILE_SIZE));
		g2D.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
		g2D.drawImage(this.getCurrentPaletteItem().getMainImage(), xform, null);
		g2D.dispose();
		
		//this.setPaletteItemIcon(_things.get(this.getCurrentIndex()).getIcon());
		
	}//updateThingIcon
	
	/**
	 * sets current thing index to index provided, and prints an error if it's out of bounds
	 * and only sets the Thing if the timer has expired
	 */
	public void switchPaletteItem(int index){
		
			// if higher, and we can wrap, set to beginning
			if(index >= _things.size()){
				if(this.canWrap()){
					this.setCurrentIndex(0);
				}
			}
			// if lower and we can wrap, set to end
			else if(index < 0){
				if(this.canWrap()){
					this.setCurrentIndex(_things.size()-1);
				}
			}
			//otherwise, just set to index requested
			else{
				this.setCurrentIndex(index);
			}
			
			// yes, there is a chance this is not necessary...but it looks better to write it once
			updateIcon();
		
	}//switchPaletteItem

	/**
	 * Returns the current Thing in our Thing Palette
	 */
	public Thing getCurrentPaletteItem(){ return _things.get(this.getCurrentIndex()); }
	
	
	/**
	 * This function is called from the constructor and needs to perform
	 * the following: 
	 * -init palette image, and set to correct x/y coords
	 * @param screenH - the height of the parent application's screen
	 * @param screenW - the width of the parent application's screen
	 */
	protected void initPalette(int screenW, int screenH){
		
		this.setPaletteImage(new Sprite(OtherStuff.SPRITE_TOOL_THING_PALETTE));
		this.getPaletteImage().setX(screenW/4 - this.getPaletteImage().getW()/2);
		this.getPaletteImage().setY(screenH - this.getPaletteImage().getH());
	
	}//initPalette
	
	/**
	 * made this a separate function because it's messy and throw away, and 
	 * will eventually be based on external files probably
	 */
	protected void initPaletteSet(){
		
		_things.add(new Avatar(0,0,0,0,0,0));
		_things.add(new Projectile(0,0,0,0,0,0));
		_things.add(new OfficeBuilding001(0,0));
		_things.add(new PlayerStart(0,0));
		
	}//initPaletteSet

}//ThingPalette
