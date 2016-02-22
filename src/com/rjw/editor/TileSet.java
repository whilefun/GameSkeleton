package com.rjw.editor;

import java.awt.image.BufferedImage;

import java.io.File;

import javax.imageio.ImageIO;

import com.rjw.gameskeleton.Animation;
import com.rjw.gameskeleton.OtherStuff;
import com.rjw.gameskeleton.Tile;
import com.rjw.gameskeleton.GameSkeleton;

/**
 * This class is basic and houses a big tilemap image and some
 * pointers to keep track of the size and dimensions of the grid
 * of tiles. 
 * @author rwalsh
 * Note: Recommended size of the tileset image is 224px x 320px (or 7 tiles x 10 tiles) 
 */
public class TileSet {

	private BufferedImage _tilemapImage;
	private String _tilemapImageFilename;
	private int _tilemapWidthInTiles;
	private int _tilemapHeightInTiles;
	private int _tilemapImageWidth;
	private int _tilemapImageHeight;
	
	public TileSet(String tilemapFilename, int width, int height){
		
		// open our tilemap
		try{
			_tilemapImage = ImageIO.read(new File(OtherStuff.SPRITE_PATH_PREFIX + tilemapFilename));	
		}catch(Exception e){
			GameSkeleton.printDebugMessage("TileSet: Error creating TileSet from file '"+tilemapFilename+"': " + e.getMessage());
		}
		
		_tilemapImageFilename = tilemapFilename;
		_tilemapWidthInTiles = width;
		_tilemapHeightInTiles = height;
		_tilemapImageWidth = _tilemapImage.getWidth();
		_tilemapImageHeight = _tilemapImage.getHeight();
		
	}//constructor
	
	/**
	 * This function is a little fugly, and just returns a new tile based on this
	 * set's tilemap image and the x/y positions given. If 2,2 are given, this 
	 * returns the tile located at position 2,2 of the tilemap grid
	 * @param xPos
	 * @param yPos
	 * @return
	 */
	public Tile getTileFromSet(int xPos, int yPos){ 
		
		//return new Tile(0,0,OtherStuff.TILE_SIZE,OtherStuff.TILE_SIZE,0,0,_tilemapImageFilename,xPos,yPos);

		//TODO: experimenting with copy from master image
		return new Tile(0,0,OtherStuff.TILE_SIZE,OtherStuff.TILE_SIZE,0,0,_tilemapImageFilename, _tilemapImage, xPos,yPos, false);
		
	}//getTileFromSet
	
	public BufferedImage getImage(){ return _tilemapImage; }
	public int getImageWidth(){ return _tilemapImageWidth; }
	public int getImageHeight(){ return _tilemapImageHeight; }
	public int getTilemapWidthInTiles(){ return _tilemapWidthInTiles; }
	public int getTilemapHeightInTiles(){ return _tilemapHeightInTiles; }
	public String getName(){ return _tilemapImageFilename; }
	
	
}//TileSet
