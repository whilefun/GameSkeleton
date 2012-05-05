package com.rjw.editor;

import java.awt.image.BufferedImage;
import java.util.Vector;


import com.rjw.gameskeleton.Animation;
import com.rjw.gameskeleton.OtherStuff;
import com.rjw.gameskeleton.Sprite;
import com.rjw.gameskeleton.Thing;
import com.rjw.gameskeleton.Tile;

/**
 * This is the experimental "auto-hiding" palette set, but
 * for Things instead of Tiles. Booyah.
 * @author rwalsh
 */
public class ThingSetPalette extends Palette{

	//TODO: check comments to make sure they make sense for THINGS instead of TILES (since this was copy/paste)
	
	// offsets and such
	public final static long THING_SWITCH_TIMEOUT = 200;
	public final static int THINGSET_PALETTE_X_OFFSET = 48;
	public final static int THINGSET_PALETTE_Y_OFFSET = 0;
	// the offsets between this palette's top left corner and the 
	// top left corner of our grid of tiles (Where clicks will matter
	// for selecting tiles)
	public final static int THINGSET_PALETTE_THING_GRID_X_OFFSET = 33;
	public final static int THINGSET_PALETTE_THING_GRID_Y_OFFSET = 2;
	//our max movement and step sizes
	public final static int THINGSET_PALETTE_MAX_MOVE_X = OtherStuff.TILEMAP_DEFAULT_WIDTH*OtherStuff.TILE_SIZE + THINGSET_PALETTE_THING_GRID_X_OFFSET;
	public final static int THINGSET_PALETTE_STEP_SIZE_X = 20;
	//our prev/next arrows, and label stuff
	public final static int THINGSET_PALETTE_PREV_ARROW_X_OFFSET = 34;
	public final static int THINGSET_PALETTE_NEXT_ARROW_X_OFFSET = 220;
	public final static int THINGSET_PALETTE_ARROW_WIDTH = 38;
	public final static int THINGSET_PALETTE_ARROW_HEIGHT = 26;

	private Vector<ThingSet> _thingsets;
	private int _currentThingsetIndex;
	private int _xCoord;
	private int _yCoord;
	
	
	public ThingSetPalette(int screenW, int screenH) {
		
		//TODO: is timeout needed?
		super(screenW, screenH, THING_SWITCH_TIMEOUT);
		_thingsets = new Vector<ThingSet>();
		_xCoord = this.getPaletteImage().getX();
		_yCoord = this.getPaletteImage().getY();
		initPaletteSet();
		updateIcon();
		
	}

	// to help with animation
	//public void moveX(int xDelta){ this.getPaletteImage().setX(this.getPaletteImage().getX() + xDelta); }
	public void moveX(int xDelta){ _xCoord += xDelta; }
	public int getX(){ return _xCoord; }
	public int getY(){ return _yCoord; }
	public boolean canMoveOut(){ return _xCoord >= (OtherStuff.EDITOR_SCREEN_W - THINGSET_PALETTE_MAX_MOVE_X); }
	public boolean canMoveIn(){ return _xCoord < (OtherStuff.EDITOR_SCREEN_W - THINGSET_PALETTE_X_OFFSET); }

	/**
	 * This function returns a new tile based on where the user clicked on 
	 * the screen
	 * @param absClickedX - the absolute SCREEN X coord the user clicked
	 * @param absClickedY - the absolute SCREEN Y coord the user clicked
	 * @return - the tile from the current tileset at the position clicked
	 */
	public Thing getThingFromCurrentThingSet(int absClickedX, int absClickedY){
		
		Thing tempThing = null;
		
		// if the click was inside the bounds of our thingsetpalette
		
		//debug
		//GameSkeleton.printDebugMessage("checking for tile @ (" + absClickedX + "," + absClickedY + ") with ");
		//GameSkeleton.printDebugMessage("x="+_xCoord+", y="+_yCoord+", imageW="+_thingsets.get(_currentThingsetIndex).getImageWidth()+", imageY=" + _thingsets.get(_currentThingsetIndex).getImageHeight());
		
		
		// if x > left side of tileset tile grid AND x < right side of tileset tile grid
		// AND
		// if y > top side of tileset tilegrid AND y < bottom of tileset tile grid
		
		if( (absClickedX > _xCoord + THINGSET_PALETTE_THING_GRID_X_OFFSET) 
				&& (absClickedX < _xCoord + THINGSET_PALETTE_THING_GRID_X_OFFSET + _thingsets.get(_currentThingsetIndex).getImageWidth()) 
				&& (absClickedY > _yCoord + THINGSET_PALETTE_THING_GRID_Y_OFFSET) 
				&& ( absClickedY < _yCoord + THINGSET_PALETTE_THING_GRID_Y_OFFSET + _thingsets.get(_currentThingsetIndex).getImageHeight()) 
				){
			
			//get the tile from the CURRENT tile set that is at the coords clicked
			// but first have to calc/pass the coords relative to the top left corner
			// of the tileset tile grid
			//GameSkeleton.printDebugMessage("ThingSetPalette: Clicked in grid at ["+ (absClickedX - (_xCoord + THINGSET_PALETTE_THING_GRID_X_OFFSET))/OtherStuff.TILE_SIZE +","+ (absClickedY - (_yCoord + THINGSET_PALETTE_THING_GRID_Y_OFFSET))/OtherStuff.TILE_SIZE+"]");
			return _thingsets.get(_currentThingsetIndex).getThingFromSet((absClickedX - (_xCoord + THINGSET_PALETTE_THING_GRID_X_OFFSET))/OtherStuff.TILE_SIZE, (absClickedY - (_yCoord + THINGSET_PALETTE_THING_GRID_Y_OFFSET))/OtherStuff.TILE_SIZE);
			
		}
		
		return tempThing;
		
	}//getThingFromCurrentThingSet

	
	/**
	 * This function returns whether or not we clicked on the "previous"
	 * tileset arrow located at the bottom of the thingsetpalette
	 * @param x - the x screen coord clicked that we're checking against
	 * @param y - the y screen coord clicked that we're checking against 
	 * @return - true if clicked, false otherwise
	 * Note: previous arrow located at rect Top Left(), Width() Height()
	 */
	public boolean clickedPreviousThingset(int x, int y){
		
		return ((x > _xCoord + THINGSET_PALETTE_PREV_ARROW_X_OFFSET)
				&& (x < (_xCoord + (THINGSET_PALETTE_ARROW_WIDTH + THINGSET_PALETTE_PREV_ARROW_X_OFFSET))) 
				&& (y > (_yCoord + (this.getPaletteImage().getH() - THINGSET_PALETTE_ARROW_HEIGHT))) 
				&& (y < (_yCoord + this.getPaletteImage().getH())));
		
	}//clickedPreviousThingset
	
	/**
	 * This function returns whether or not we clicked on the "next"
	 * tileset arrow located at the bottom of the thingsetpalette
	 * @param x - the x screen coord clicked that we're checking against
	 * @param y - the y screen coord clicked that we're checking against 
	 * @return - true if clicked, false otherwise
	 */
	public boolean clickedNextThingset(int x, int y){

		//TODO:
		//return truth of:
		// x > left side of palette + next arrow offset
		// x < left side of palette + next arrow offset + arrow size
		// y > top side of palette + (height of palette - arrow height)
		// y < top side of palette + height of palette
		return ((x > _xCoord + THINGSET_PALETTE_NEXT_ARROW_X_OFFSET)
				&& (x < (_xCoord + (THINGSET_PALETTE_ARROW_WIDTH + THINGSET_PALETTE_NEXT_ARROW_X_OFFSET))) 
				&& (y > (_yCoord + (this.getPaletteImage().getH() - THINGSET_PALETTE_ARROW_HEIGHT))) 
				&& (y < (_yCoord + this.getPaletteImage().getH())));
		
	}//clickedNextThingset
	
	/**
	 * this function gets the previous tileset
	 * or the first tileset if we can wrap
	 * around the sets (eg. set 0 - 1 = set N, where N is the max set index)
	 */
	public void prevThingset(){
		
		//TODO: implement wrapping properly according to _canWrap var?
		
		// if index - 1 will be less than zero,
		// make it the max index
		if(_currentThingsetIndex - 1 < 0){
			_currentThingsetIndex = (_thingsets.size() - 1);
		}else{
			_currentThingsetIndex--;
		}
		
	}//prevThingset
	
	/**
	 * this function gets the previous tileset
	 * or the first tileset if we can wrap
	 * around the sets (eg. set 0 - 1 = set N, where N is the max set index)
	 */
	public void nextThingset(){
		
		//TODO: implement wrapping properly according to _canWrap var?
		
		// if index+1 will be more than last index,
		// make it the min index (0)
		if(_currentThingsetIndex + 1 > _thingsets.size() - 1){
			_currentThingsetIndex = 0;
		}else{
			_currentThingsetIndex++;
		}
		
	}//nextThingset
	
	// OTHER INHERITED SHIT //
	@Override
	protected void initPaletteSet() {
	
		//TODO: add all our thing sets here
		_thingsets.add(new ThingSet(OtherStuff.THINGSETFILENAME_001));
		_thingsets.add(new ThingSet(OtherStuff.THINGSETFILENAME_002));
		
	}//initPaletteSet
	
	@Override
	public ThingSet getCurrentPaletteItem() {

		return _thingsets.get(_currentThingsetIndex);
		
	}//getCurrentPaletteItem

	@Override
	protected void initPalette(int screenW, int screenH) {
		
		this.setPaletteImage(new Sprite(OtherStuff.SPRITE_TOOL_THINGSET_PALETTE));
		this.getPaletteImage().setX(screenW - THINGSET_PALETTE_X_OFFSET);
		this.getPaletteImage().setY(THINGSET_PALETTE_Y_OFFSET);
		
	}



	@Override
	protected void switchPaletteItem(int index) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void updateIcon() {
		// TODO Auto-generated method stub
		
	}


	
	
	
}//ThingSetPalette 
