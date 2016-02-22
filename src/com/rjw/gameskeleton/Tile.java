package com.rjw.gameskeleton;

import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

public class Tile extends AnimatedSprite{

	//TODODONE: perhaps make an "editor" version of this with the extra stuff like name, image string, etc.?
	private int _altitude;
	//private boolean _visible;
	private BufferedImage _animationImage;
	private int _row;
	private int _col;
	private int _tilemapX;
	private int _tilemapY;
	private String _name;
	
	/**
	 * This constructor is the one used for making Tile's based on an external animation. 
	 * The _tilemapX and _tilemapY should be set to zero, since that is technically correct
	 * if the image being loaded is only the size of one tile (namely at position 0,0).
	 * @param x - x position
	 * @param y - y position
	 * @param w - width (typically OtherStuff.TILE_SIZE)
	 * @param h - height (typically OtherStuff.TILE_SIZE)
	 * @param animation - the external animation for the tile
	 * @param row - the row (e.g. if in a map structure)
	 * @param col - the col (e.g. if in a map structure)
	 * @param name - the name of the tile. Recommended to be the filename of the source image
	 */
	public Tile(int x, int y, int w, int h, Animation animation, int row, int col, String name, boolean isVisible) {
		super(x, y, w, h, animation, isVisible);
		
		_row = row;
		_col = col;
		_name = name;
		_tilemapX = 0;
		_tilemapY = 0;
		
	}//constructor
	
	// TODO:
	// need to make a Tile that can be created from a master tileset (big grid image map containing 
	// many tiles or equal size) referenced by a filename and a set of coordinates indicating where 
	// the top left corner of the desired tile is

	/**
	 * This constructor allows for the creation of a tile based on a tilemap and tile coords
	 * @param tilemapFilename - the file name of the tile map image
	 * @param tilemapX - the X coord of the desired tile within the map
	 * @param tilemapY - the Y coord of the desired tile within the map
	 * NOTE: prints an error to stdout if something goes wrong
	 * NOTE: OtherStuff.TILE_SIZE is the assumed size of each tile
	 * NOTE: naming convention for tilemap tiles is "[tilemapFilename]_[tilemapX]_[tilemapY]"
	 * TODO: change above naming and just output  tilemapX and tilemapY to xml and level I/O
	 */
	/*
	public Tile(int x, int y, int w, int h, int row, int col, String tilemapFilename, int tilemapX, int tilemapY){
		
		// call super with new animation
		super(x, y, w, h, new Animation());
		
		_row = row;
		_col = col;
		//_name = tilemapFilename + "_" + tilemapX + "_" + tilemapY;
		_name = tilemapFilename;
		_tilemapX = tilemapX;
		_tilemapY = tilemapY;
		
		// now for the fun part: make a new animation out of the tilemap and the tilemapX/Y
		try{
			_animationImage = ImageIO.read(new File(OtherStuff.SPRITE_PATH_PREFIX + tilemapFilename));
			super.getAnimation().addFrame(_animationImage.getSubimage(tilemapX*OtherStuff.TILE_SIZE, tilemapY*OtherStuff.TILE_SIZE, OtherStuff.TILE_SIZE, OtherStuff.TILE_SIZE), 0);	
		}catch(Exception e){
			GameSkeleton.printDebugMessage("Tile: Error creating Tile @("+tilemapX+","+tilemapY+") from tilemap image '"+tilemapFilename+"': " + e.getMessage());
		}
		
		
	}//constructor
	*/
	
	//TODO: experiment with creating tile solely from reference to tilemape image passed
	public Tile(int x, int y, int w, int h, int row, int col, String tilemapFilename, BufferedImage masterImage, int tilemapX, int tilemapY, boolean isVisible){
		
		// call super with new animation
		super(x, y, w, h, new Animation(), isVisible);
		
		_row = row;
		_col = col;
		//_name = tilemapFilename + "_" + tilemapX + "_" + tilemapY;
		_name = tilemapFilename;
		_tilemapX = tilemapX;
		_tilemapY = tilemapY;
		
		// now for the fun part: make a new animation out of the tilemap and the tilemapX/Y
		try{
			super.getAnimation().addFrame(masterImage.getSubimage(tilemapX*OtherStuff.TILE_SIZE, tilemapY*OtherStuff.TILE_SIZE, OtherStuff.TILE_SIZE, OtherStuff.TILE_SIZE), 0);	
		}catch(Exception e){
			GameSkeleton.printDebugMessage("Tile: Error creating Tile from Master Image @("+tilemapX+","+tilemapY+") from tilemap image '"+tilemapFilename+"': " + e.getMessage());
		}
		
		
	}//constructor
	
	/**
	 * This constructor creates a copy of the passed tile
	 * @param tileToCopy
	 */
	public Tile(Tile tileToCopy){
		
		super(tileToCopy.getX(), tileToCopy.getY(), tileToCopy.getW(), tileToCopy.getH(), tileToCopy.getAnimation(), tileToCopy.isVisible());
		
		_row = tileToCopy.getRow();
		_col = tileToCopy.getCol();
		_name = tileToCopy.getName();
		_tilemapX = tileToCopy.getTilemapX();
		_tilemapY = tileToCopy.getTilemapY();
		
	}//copyConstructor
	
	/**
	 * This function compares Tiles for equality by checking for 
	 * compareTo equality on the Strings, and tilemapX, and 
	 * tilemapY equality.
	 * @param compareToTile - the tile to compare "this" to
	 */
	public boolean equals(Tile compareToTile){

		//TODO: consider removing the inline return since the function is no longer sleek due to the null check
		if(compareToTile != null){
			return ( ( this._name.compareTo(compareToTile.getName()) == 0 && this.getTilemapX() == compareToTile.getTilemapX() && this.getTilemapY() == compareToTile.getTilemapY() ) ? true:false );
		}else{
			return false;
		}
				
	}//equals
	
	public int getRow(){ return _row; }
	public int getCol(){ return _col; }
	public int getTilemapX(){ return _tilemapX; }
	public int getTilemapY(){ return _tilemapY; }
	public String getName(){ return _name; }

}//Tile
