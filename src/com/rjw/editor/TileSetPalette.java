package com.rjw.editor;

import java.awt.image.BufferedImage;
import java.util.Vector;


import com.rjw.gameskeleton.Animation;
import com.rjw.gameskeleton.OtherStuff;
import com.rjw.gameskeleton.Sprite;
import com.rjw.gameskeleton.Tile;

/**
 * This is the experimental "auto-hiding" palette set
 * @author rwalsh
 *
 */
public class TileSetPalette extends Palette{

	// offsets and such
	public final static long TILE_SWITCH_TIMEOUT = 200;
	public final static int TILESET_PALETTE_X_OFFSET = 48;
	public final static int TILESET_PALETTE_Y_OFFSET = 0;
	// the offsets between this palette's top left corner and the 
	// top left corner of our grid of tiles (Where clicks will matter
	// for selecting tiles)
	public final static int TILESET_PALETTE_TILE_GRID_X_OFFSET = 33;
	public final static int TILESET_PALETTE_TILE_GRID_Y_OFFSET = 2;
	//our max movement and step sizes
	public final static int TILESET_PALETTE_MAX_MOVE_X = OtherStuff.TILEMAP_DEFAULT_WIDTH*OtherStuff.TILE_SIZE + TILESET_PALETTE_TILE_GRID_X_OFFSET;
	public final static int TILESET_PALETTE_STEP_SIZE_X = 20;
	//our prev/next arrows, and label stuff
	public final static int TILESET_PALETTE_PREV_ARROW_X_OFFSET = 34;
	public final static int TILESET_PALETTE_NEXT_ARROW_X_OFFSET = 220;
	public final static int TILESET_PALETTE_ARROW_WIDTH = 38;
	public final static int TILESET_PALETTE_ARROW_HEIGHT = 26;
	
	


	private Vector<TileSet> _tilesets;
	private int _currentTilesetIndex;
	private int _xCoord;
	private int _yCoord;
	
	
	public TileSetPalette(int screenW, int screenH) {
		
		//TODO: is timeout needed?
		super(screenW, screenH, TILE_SWITCH_TIMEOUT);
		_tilesets = new Vector<TileSet>();
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
	public boolean canMoveOut(){ return _xCoord >= (OtherStuff.EDITOR_SCREEN_W - TILESET_PALETTE_MAX_MOVE_X); }
	public boolean canMoveIn(){ return _xCoord < (OtherStuff.EDITOR_SCREEN_W - TILESET_PALETTE_X_OFFSET); }

	/**
	 * This function returns a new tile based on where the user clicked on 
	 * the screen
	 * @param absClickedX - the absolute SCREEN X coord the user clicked
	 * @param absClickedY - the absolute SCREEN Y coord the user clicked
	 * @return - the tile from the current tileset at the position clicked
	 */
	public Tile getTileFromCurrentTileSet(int absClickedX, int absClickedY){
		
		Tile tempTile = null;
		
		// if the click was inside the bounds of our tilesetpalette
		
		//debug
		//GameSkeleton.printDebugMessage("checking for tile @ (" + absClickedX + "," + absClickedY + ") with ");
		//GameSkeleton.printDebugMessage("x="+_xCoord+", y="+_yCoord+", imageW="+_tilesets.get(_currentTilesetIndex).getImageWidth()+", imageY=" + _tilesets.get(_currentTilesetIndex).getImageHeight());
		
		
		// if x > left side of tileset tile grid AND x < right side of tileset tile grid
		// AND
		// if y > top side of tileset tilegrid AND y < bottom of tileset tile grid
		
		if( (absClickedX > _xCoord + TILESET_PALETTE_TILE_GRID_X_OFFSET) 
				&& (absClickedX < _xCoord + TILESET_PALETTE_TILE_GRID_X_OFFSET + _tilesets.get(_currentTilesetIndex).getImageWidth()) 
				&& (absClickedY > _yCoord + TILESET_PALETTE_TILE_GRID_Y_OFFSET) 
				&& ( absClickedY < _yCoord + TILESET_PALETTE_TILE_GRID_Y_OFFSET + _tilesets.get(_currentTilesetIndex).getImageHeight()) 
				){
			
			//get the tile from the CURRENT tile set that is at the coords clicked
			// but first have to calc/pass the coords relative to the top left corner
			// of the tileset tile grid
			//GameSkeleton.printDebugMessage("clicked in grid at ["+ (absClickedX - (_xCoord + TILESET_PALETTE_TILE_GRID_X_OFFSET))/OtherStuff.TILE_SIZE +","+ (absClickedY - (_yCoord + TILESET_PALETTE_TILE_GRID_Y_OFFSET))/OtherStuff.TILE_SIZE+"]");
			return _tilesets.get(_currentTilesetIndex).getTileFromSet((absClickedX - (_xCoord + TILESET_PALETTE_TILE_GRID_X_OFFSET))/OtherStuff.TILE_SIZE, (absClickedY - (_yCoord + TILESET_PALETTE_TILE_GRID_Y_OFFSET))/OtherStuff.TILE_SIZE);
			
		}
		
		return tempTile;
		
	}//getTileFromCurrentTileSet

	
	/**
	 * This function returns whether or not we clicked on the "previous"
	 * tileset arrow located at the bottom of the tilesetpalette
	 * @param x - the x screen coord clicked that we're checking against
	 * @param y - the y screen coord clicked that we're checking against 
	 * @return - true if clicked, false otherwise
	 * Note: previous arrow located at rect Top Left(), Width() Height()
	 */
	public boolean clickedPreviousTileset(int x, int y){
		
		return ((x > _xCoord + TILESET_PALETTE_PREV_ARROW_X_OFFSET)
				&& (x < (_xCoord + (TILESET_PALETTE_ARROW_WIDTH + TILESET_PALETTE_PREV_ARROW_X_OFFSET))) 
				&& (y > (_yCoord + (this.getPaletteImage().getH() - TILESET_PALETTE_ARROW_HEIGHT))) 
				&& (y < (_yCoord + this.getPaletteImage().getH())));
		
	}//clickedPreviousTileset
	
	/**
	 * This function returns whether or not we clicked on the "next"
	 * tileset arrow located at the bottom of the tilesetpalette
	 * @param x - the x screen coord clicked that we're checking against
	 * @param y - the y screen coord clicked that we're checking against 
	 * @return - true if clicked, false otherwise
	 */
	public boolean clickedNextTileset(int x, int y){

		//TODO:
		//return truth of:
		// x > left side of palette + next arrow offset
		// x < left side of palette + next arrow offset + arrow size
		// y > top side of palette + (height of palette - arrow height)
		// y < top side of palette + height of palette
		return ((x > _xCoord + TILESET_PALETTE_NEXT_ARROW_X_OFFSET)
				&& (x < (_xCoord + (TILESET_PALETTE_ARROW_WIDTH + TILESET_PALETTE_NEXT_ARROW_X_OFFSET))) 
				&& (y > (_yCoord + (this.getPaletteImage().getH() - TILESET_PALETTE_ARROW_HEIGHT))) 
				&& (y < (_yCoord + this.getPaletteImage().getH())));
		
	}//clickedNextTileset
	
	/**
	 * this function gets the previous tileset
	 * or the first tileset if we can wrap
	 * around the sets (eg. set 0 - 1 = set N, where N is the max set index)
	 */
	public void prevTileset(){
		
		//TODO: implement wrapping properly according to _canWrap var?
		
		// if index - 1 will be less than zero,
		// make it the max index
		if(_currentTilesetIndex - 1 < 0){
			_currentTilesetIndex = (_tilesets.size() - 1);
		}else{
			_currentTilesetIndex--;
		}
		
	}//prevTileset
	
	/**
	 * this function gets the previous tileset
	 * or the first tileset if we can wrap
	 * around the sets (eg. set 0 - 1 = set N, where N is the max set index)
	 */
	public void nextTileset(){
		
		//TODO: implement wrapping properly according to _canWrap var?
		
		// if index+1 will be more than last index,
		// make it the min index (0)
		if(_currentTilesetIndex + 1 > _tilesets.size() - 1){
			_currentTilesetIndex = 0;
		}else{
			_currentTilesetIndex++;
		}
		
	}//prevTileset
	
	// OTHER INHERITED SHIT //
	@Override
	protected void initPaletteSet() {
	
		//TODO: add all our tilemaps here
		_tilesets.add(new TileSet(OtherStuff.SPRITE_TILEMAP_TEST_02, OtherStuff.TILEMAP_DEFAULT_WIDTH, OtherStuff.TILEMAP_DEFAULT_HEIGHT));
		_tilesets.add(new TileSet(OtherStuff.SPRITE_TILEMAP_BLANK, OtherStuff.TILEMAP_DEFAULT_WIDTH, OtherStuff.TILEMAP_DEFAULT_HEIGHT));
		_tilesets.add(new TileSet(OtherStuff.SPRITE_TILEMAP_TEST_01, OtherStuff.TILEMAP_DEFAULT_WIDTH, OtherStuff.TILEMAP_DEFAULT_HEIGHT));
		_tilesets.add(new TileSet(OtherStuff.SPRITE_TILEMAP_TEST_03, OtherStuff.TILEMAP_DEFAULT_WIDTH, OtherStuff.TILEMAP_DEFAULT_HEIGHT));
		_tilesets.add(new TileSet(OtherStuff.SPRITE_TILEMAP_TEST_04, OtherStuff.TILEMAP_DEFAULT_WIDTH, OtherStuff.TILEMAP_DEFAULT_HEIGHT));
		_tilesets.add(new TileSet(OtherStuff.SPRITE_TILEMAP_TEST_05, OtherStuff.TILEMAP_DEFAULT_WIDTH, OtherStuff.TILEMAP_DEFAULT_HEIGHT));
		
	}//initPaletteSet
	
	@Override
	public TileSet getCurrentPaletteItem() {

		return _tilesets.get(_currentTilesetIndex);
		
	}//getCurrentPaletteItem

	@Override
	protected void initPalette(int screenW, int screenH) {
		
		this.setPaletteImage(new Sprite(OtherStuff.SPRITE_TOOL_TILESET_PALETTE));
		this.getPaletteImage().setX(screenW - TILESET_PALETTE_X_OFFSET);
		this.getPaletteImage().setY(TILESET_PALETTE_Y_OFFSET);
		
	}



	@Override
	protected void switchPaletteItem(int index) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void updateIcon() {
		// TODO Auto-generated method stub
		
	}


	
	
	
}//TileSetPalette 
