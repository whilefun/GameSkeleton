package com.rjw.gameskeleton;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Vector;
import javax.imageio.ImageIO;

// TODODONE: make painting faster (?) by only blitting what part of the map is visible, rather than looping through all the tiles and blitting each one
// TODO: make map (optionally) loopable so when we get to the end the map loops back to the start (thinking bonus/survival levels)
public class Map {

	//so we have static, readable indices into our viewable pizel area array 
	public static int VIEWABLE_TOP_LEFT_X = 0;
	public static int VIEWABLE_TOP_LEFT_Y = 1;
	public static int VIEWABLE_BOTTOM_RIGHT_X = 2;
	public static int VIEWABLE_BOTTOM_RIGHT_Y = 3;
	
	
	private int _cols;
	private int _rows;
	private int _x;
	private int _y;
	
	private int _loopRowIndex;
	
	// TODO: keep track of our max x and y pixel (screen) coords with 2 x,y points so we can optimize drawing
	// The array will contain [topLeftX, topleftY, bottomRightX, bottomRightY]
	private short[] _viewablePixelArea;
	
	//array based map to improve speed (I hope!). Will eventually ditch the Vector based one.
	private Tile[][] _arrayMap;
	
	/**
	 * @param w is the width in tiles
	 * @param h is the width in tiles
	 * @param x is the x pixel coordinate of the top left corner of the map
	 * @param y is the y pixel coordinate of the top left corner of the map
	 * WARNING: Assumes square tiles, ALL of the same size
	 * WARNING: Width <=> Columns, Height <=> Rows
	 * NOTE: Map is draw from bottom to top, left to right? TODO: confirm this.
	 */
    public Map(int rows, int cols /*, int x, int y*/){	
    	
    	_cols = cols;
    	_rows = rows;
    	_arrayMap = new Tile[rows][cols];
    	Animation tempAnimation = new Animation();
    	BufferedImage tempTileImage;

    	// WARNING: this default value is also used in getAndUpdateLoopRowIndex() below
    	_loopRowIndex = _rows - 1;
    	
    	// by default, set our viewable area to be really big
    	_viewablePixelArea = new short[4];
    	this.setViewablePixelArea((short)0, (short)0, (short)9999, (short)9999);
    	
    	
    	try{
    		
    		//tempTileImage = ImageIO.read(new File(OtherStuff.SPRITE_PATH_PREFIX + OtherStuff.SPRITE_BLANK_TILE));
    		tempTileImage = ImageIO.read(new File(OtherStuff.SPRITE_PATH_PREFIX + OtherStuff.SPRITE_32_32_BLANK));
    		tempAnimation.addFrame(tempTileImage, 5000);
        	
        	//init our map with dummy data to test it
    		//TODO: replace this with file reading
    		
    		// for every row that we want to make
    		for(int row = 0; row < _rows; row++){
    			
    			//add a new vector for that row
    			//_map.add(new Vector<Tile>());
    			
    			// then for every column we want to make,
    			// add a new tile to the new row
    			for(int column = 0; column < _cols; column++){
    				
    				// add the tile to the slot at (row,column), by adding to the row column number of tiles
    				// with screen coords (column*tilewidth, row*tileheight)
    				//_map.get(row).add( new Tile(column*tempTileImage.getWidth(), row*tempTileImage.getHeight(), tempTileImage.getWidth(),tempTileImage.getHeight(),tempAnimation,column, row, OtherStuff.SPRITE_32_32_BLANK) );
    				
    				//TODO: experimental "null" tile to save on RAM
    				//_map.get(row).add(null);
    				
    				//_arrayMap[row][column] = new Tile(column*tempTileImage.getWidth(), row*tempTileImage.getHeight(), tempTileImage.getWidth(),tempTileImage.getHeight(),tempAnimation,column, row, OtherStuff.SPRITE_32_32_BLANK);
    				_arrayMap[row][column] = null;
    				
    			}//for
    			
    		}//for
        	
        	//
        	// Map (x,y), denoted as m is always here:
        	//		 0 1 2 3 W
        	// Map:	+---------+
        	//		|m| | | | |H
        	//		 - - - - - 
        	//		| | | | | |1
        	//		 - - - - - 
        	//		| | | | | |0
        	//		+---------+
        	//
        	// Which is the top left corner of the left most, top most tile. Namely,
        	// the first tile we added in the first row
        	//
        	//TODODONE: FIX THIS SHIT SO I CAN GET THE TILE I CLIKC IN THE EDITOR!!!
    		resetXY();
        	
    	}catch(Exception e){
    		GameSkeleton.printDebugMessage("Map - Error creating map: " + e.getMessage());
    	}
    	
    }//Constructor

    public /*Vector<Vector<Tile>>*/ Tile[][] getMap(){
    	//return _map;
    	return _arrayMap;
    }
    
    public int getRowCount(){ return _rows; }
    public int getColCount(){ return _cols; }
    public int getX(){ return _x; }
    public int getY(){ return _y; }
    //TODO: why is this needed? Won't getRows() do the trick?
    //public int getMapRows(){ return _map.size(); }
    public void setX(int x){ _x = x; }
    public void setY(int y){ _y = y; }
    public void changeYBy(int yDelta){ _y += yDelta; }
    //TODO: this is kind of sloppy because it means rows and cols can 
    //be set to not reflect actual data structure size
    public void setRowValue(int rows){ _rows = rows; }
    public void setColValue(int cols){ _cols = cols; }
    
    
    /**
     * sets our viewable pixel area by 4 points
     * @param x1
     * @param y1
     * @param x2
     * @param y2
     */
    public void setViewablePixelArea(short x1, short y1, short x2, short y2){
    	
    	_viewablePixelArea[VIEWABLE_TOP_LEFT_X] = x1;
    	_viewablePixelArea[VIEWABLE_TOP_LEFT_Y] = y1;
    	_viewablePixelArea[VIEWABLE_BOTTOM_RIGHT_X] = x2;
    	_viewablePixelArea[VIEWABLE_BOTTOM_RIGHT_Y] = y2;
    	
    }//setViewablePixelArea
    
    //updates map to move with camera's speed
	public void updatePosition(int worldMovementX, int worldMovementY) {
		
		// Move all tiles "camera y speed" down
    	for(int row = 0; row < _rows; row++){
    		
    		for(int column = 0; column < _cols; column++){
    			
    			//TODO: make this cleaner by adding a getter for the camera speed?
    			//_map.get(row).get(column).setY(_map.get(row).get(column).getY() + (OtherStuff.CAMERA_Y_SPEED) );
    			
    			//TODO: "destroy" the tiles if the map is not being looped
    			//_map.get(row).get(column).setVisible( !( (_map.get(row).get(column).getY() > (OtherStuff.CAMERA_HEIGHT * OtherStuff.TILE_HEIGHT) ) 
    											//|| ((_map.get(row).get(column).getY() + _map.get(row).get(column).getH()) < 0) ) );
    			
    			if(_arrayMap[row][column] != null){
    				
    				_arrayMap[row][column].changeYBy(worldMovementY);
    				//_arrayMap[row][column].setVisible(!( (_arrayMap[row][column].getY() > (OtherStuff.CAMERA_HEIGHT * OtherStuff.TILE_HEIGHT) - 50 ) 
    													//|| ((_arrayMap[row][column].getY() + _arrayMap[row][column].getH()) < 0)) );

    				
    				/*
    				if(
    						(_arrayMap[row][column].getX() >= _viewablePixelArea[VIEWABLE_TOP_LEFT_X]) &&
    						(_arrayMap[row][column].getX() < _viewablePixelArea[VIEWABLE_BOTTOM_RIGHT_X]) &&
    						(_arrayMap[row][column].getY() > _viewablePixelArea[VIEWABLE_TOP_LEFT_Y]) &&
    						(_arrayMap[row][column].getY() < _viewablePixelArea[VIEWABLE_BOTTOM_RIGHT_Y])    						
    				){
    					_arrayMap[row][column].setVisible(true);
    				}else{
    					_arrayMap[row][column].setVisible(false);
    				}
    				*/
    				
    				
    			}
    			
    		}//for
    		
    	}//for
    	
    	//finally update our overall y coord
    	_y += OtherStuff.CAMERA_Y_SPEED;

	}//update
	
	/**
	 * Resets map's overall X and Y values to be the same as the 
	 * position of the internal map structures top left Tile's
	 * top left corner. This aids in map editing and other
	 * related actions.
	 */
	public void resetXY(){
		
		// check rows and cols to make sure we can reset our x and y values
		if(_rows > 0 && _cols > 0){
        	
			//_x = _map.get(0).get(0).getX();
        	//_y = _map.get(0).get(0).getY();
        	
			//TODO: null tile check
			if(_arrayMap[0][0] != null){
				_x = _arrayMap[0][0].getX();
				_y = _arrayMap[0][0].getY();
			}else{
				_x = _y = 0;
			}
        	
		}
		
	}//resetXY

	/**
	 * Moves our map by X and Y passed values
	 * @param xDelta - change in x
	 * @param yDelta - change in y
	 */
	public void moveXY(int xDelta, int yDelta) {
		
		//GameSkeleton.printDebugMessage("Map - moveXY(): This is what is breaking our post-null-Tile editor tile placement (since coords are not being updated until tile is in place");
		
		// Move all tiles in X and Y
    	for(int row = 0; row < _rows; row++){
    		
    		for(int column = 0; column < _cols; column++){

    			//TODO: null tile check
    			if(_arrayMap[row][column] != null){
	    			//_map.get(row).get(column).setX(_map.get(row).get(column).getX() + xDelta);
	    			//_map.get(row).get(column).setY(_map.get(row).get(column).getY() + yDelta);
	    			
	    			_arrayMap[row][column].changeXBy(xDelta);
	    			_arrayMap[row][column].changeYBy(yDelta);
	    			
    			}

    		}//for
    		
    	}//for
    	
    	//finally update our overall map x and y coords
    	_x += xDelta;
    	_y += yDelta;
		
	}//moveXY
	
	/**
	 * This function will replace the current tile in the passed x,y with the passed tile
	 * @param row/col - map location where replaced tile should go
	 * @param replacementTile - the new tile that should replace the old one
	 */
	public void replaceTile(int row, int col, Tile replacementTile){
		
		//if row/col are in bounds, then set the tile accordingly
		//TODO: experimental null tile check - if it's null, just put the replacementTile in the slot
		//if(_map.get(row).get(col) == null){
			//TODO: post-null check experiment, this is fucked up. it's not getting the right coords and stuff.
		//	_map.get(row).setElementAt(new Tile(col*OtherStuff.TILE_SIZE, row*OtherStuff.TILE_SIZE, OtherStuff.TILE_SIZE,OtherStuff.TILE_SIZE,row, col, replacementTile.getName(), replacementTile.getTilemapX(), replacementTile.getTilemapY()), col);
		//}else if(row < _rows && row >= 0 && col < _cols && col >= 0){
		//	_map.get(row).setElementAt(new Tile(_map.get(row).get(col).getX(), _map.get(row).get(col).getY(), _map.get(row).get(col).getW(), _map.get(row).get(col).getH(), _map.get(row).get(col).getRow(), _map.get(row).get(col).getCol(), replacementTile.getName(), replacementTile.getTilemapX(), replacementTile.getTilemapY()),col);
		//}
		
		//GameSkeleton.printDebugMessage("replacing tile @ " + row + "," + col + "with coords ("+(int)(this.getX()+(col*OtherStuff.TILE_SIZE))+","+(int)(this.getY()+(row*OtherStuff.TILE_SIZE))+")");
		
		_arrayMap[row][col] = new Tile(replacementTile);
		//TODO: set x and y of the replacementTile (now the tile in the map
		_arrayMap[row][col].setX(this.getX()+(col*OtherStuff.TILE_SIZE));
		_arrayMap[row][col].setY(this.getY()+(row*OtherStuff.TILE_SIZE));
		
		
	}//replaceTile

	/**
	 * This function returns the current map loop row index (the row where we last looped 
	 * around the tiles)
	 * @return the current loop row index
	 */
	public synchronized int getLoopRowIndex() {

		return _loopRowIndex;
		
	}//getAndIncrementLoopRowIndex
	
	/**
	 * This function updates the loop row index (the row where we last looped 
	 * around the tiles). If the index is lower than zero, then it is 
	 * automatically reset for the next call
	 */
	public synchronized void updateLoopRowIndex() {
		
		// check for wrap around. If wrapped, reset to default value.
		if((_loopRowIndex - 1) >= 0){
			_loopRowIndex--;
		}else{
			_loopRowIndex = _rows - 1;
		}
		
	}//getAndIncrementLoopRowIndex
	
}//Map
