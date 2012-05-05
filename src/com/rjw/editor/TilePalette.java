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
import com.rjw.gameskeleton.Tile;
import com.rjw.gameskeleton.Timer;
import com.rjw.gameskeleton.GameSkeleton;

/*
 * The tile palette iamge has the following key coords:
 * 2,2: top left of 32x32 pixel for current tile's icon
 * 65,5: top left of a text region for tile's name
 * 65,19" top left of a text region for tile set's name
 */
public class TilePalette extends Palette{

	// offsets and such
	public final static int TILE_PALETTE_ICON_OFFSET = 2;
	public final static int TILE_PALETTE_TEXT_X_OFFSET = 65;
	public final static int TILE_PALETTE_TEXT_Y_OFFSET_TILE = 14;
	public final static int TILE_PALETTE_TEXT_Y_OFFSET_SET = 28;
	public final static long TILE_SWITCH_TIMEOUT = 200;
	
	//private int _currentTileIndex;
	//private BufferedImage _currentTileIcon;
	//private Sprite _tilePaletteImage;
	private Vector<Tile> _tiles;
	private BufferedImage _tempTileImage;
	private Animation _blankTileAnimation;
	//private boolean _canWrap;
	
	/**
	 * Constructor for our Tile Palette
	 * @param screenW is the width of the screen we're putting the palette on
	 * @param screenH is the height of the screen we're putting the palette on
	 */
	public TilePalette(int screenW, int screenH){
		
		super(screenW, screenH, TILE_SWITCH_TIMEOUT);
		_tiles = new Vector<Tile>();
		initPaletteSet();
		updateIcon();

	}//TilePalette

	// inits our tile palette
	protected void initPalette(int screenW, int screenH){
		
		this.setPaletteImage(new Sprite(OtherStuff.SPRITE_TOOL_TILE_PALETTE));
		this.getPaletteImage().setX(screenW/2 - this.getPaletteImage().getW()/2);
		this.getPaletteImage().setY(screenH - this.getPaletteImage().getH());
		
	}//initPalette
	
	// sets our tile icon to a scaled version of the current tile
	protected void updateIcon(){
		
		this.setPaletteItemIcon(new BufferedImage(OtherStuff.EDITOR_TILE_ICON_SIZE,OtherStuff.EDITOR_TILE_ICON_SIZE, BufferedImage.TYPE_INT_RGB));
		Graphics2D  g2D = this.getPaletteItemIcon().createGraphics();
		AffineTransform xform = AffineTransform.getScaleInstance((double)((double)OtherStuff.EDITOR_TILE_ICON_SIZE/(double)OtherStuff.EDITOR_TILE_SIZE),(double)((double)OtherStuff.EDITOR_TILE_ICON_SIZE/(double)OtherStuff.EDITOR_TILE_SIZE));
		g2D.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
		g2D.drawImage(_tiles.get(this.getCurrentIndex()).getImage(), xform, null);
		g2D.dispose();
		
	}//updateIcon

	/*
	 * sets current tile index to index provided, and prints an error if it's out of bounds
	 */
	public void switchPaletteItem(int index){

		// if higher, and we can wrap, set to beginning
		if(index >= _tiles.size()){
			if(this.canWrap()){
				this.setCurrentIndex(0);
			}
		}

		// if lower and we can wrap, set to end
		else if(index < 0){
			if(this.canWrap()){
				this.setCurrentIndex(_tiles.size()-1);
			}
		}

		//otherwise, just set to index requested
		else{
			this.setCurrentIndex(index);
		}
		
		// yes, there is a chance this is not necessary...but it looks better to write it once
		updateIcon();
		
	}//setCurrentTile
	
	public Tile getCurrentPaletteItem(){ return _tiles.get(this.getCurrentIndex()); }

	/*
	 * made this a separate function because it's messy and throw away, and 
	 * will eventually be based on external files probably
	 */
	protected void initPaletteSet(){
		
		// try to make our tile set from a "whack o' tiles"
		/*
		addTileToSet(OtherStuff.SPRITE_BLANK_TILE);
		addTileToSet(OtherStuff.SPRITE_WATER_FRAME_001);
		addTileToSet(OtherStuff.SPRITE_WATER_FRAME_002);
		addTileToSet(OtherStuff.SPRITE_WATER_FRAME_003);
		addTileToSet(OtherStuff.SPRITE_RED_STUFF);
		addTileToSet(OtherStuff.SPRITE_GREEN_STUFF);
		addTileToSet(OtherStuff.SPRITE_BLUE_STUFF);
		
		addTileGridToSet(OtherStuff.SPRITE_GRID_TEST_4x4,4,4);
		*/
		
		// testing 32x32
		addTileToSet(OtherStuff.SPRITE_32_32_01);
		addTileToSet(OtherStuff.SPRITE_32_32_02);
		addTileToSet(OtherStuff.SPRITE_32_32_BLANK);
		
	}//initTileSet
	
	private void addTileToSet(String name){
		
		try{
			_tempTileImage = ImageIO.read(new File(OtherStuff.SPRITE_PATH_PREFIX + name));
			_blankTileAnimation = new Animation();
			_blankTileAnimation.addFrame(_tempTileImage, 10);
		}catch(Exception e){
			GameSkeleton.printDebugMessage("TilePalette - Error initializing TileSet in addTileToSet() " + e.getMessage());
		}
		
		// add our current tile
		_tiles.add(new Tile(this.getPaletteImage().getX()+ 3,this.getPaletteImage().getY() + 3,_tempTileImage.getWidth(),_tempTileImage.getHeight(),_blankTileAnimation, -1, -1, name, false));
		
	}//addTile
	
	
	private void addTileGridToSet(String name, int tilesWide, int tilesHigh){
		
		try{
			
			_tempTileImage = ImageIO.read(new File(OtherStuff.SPRITE_PATH_PREFIX + name));
			
			//GameSkeleton.printDebugMessage(_tempTileImage.getWidth() + "," + _tempTileImage.getHeight());
			
			// add all the tiles in this grid
			for(int w = 0; w < tilesWide; w++){
				
				for(int h = 0; h < tilesHigh; h++){
					
					_blankTileAnimation = new Animation();
					_blankTileAnimation.addFrame(_tempTileImage.getSubimage(w*OtherStuff.EDITOR_TILE_SIZE, h*OtherStuff.EDITOR_TILE_SIZE, OtherStuff.EDITOR_TILE_SIZE, OtherStuff.EDITOR_TILE_SIZE), 10);
					_tiles.add(new Tile(this.getPaletteImage().getX()+ 3,this.getPaletteImage().getY() + 3,OtherStuff.EDITOR_TILE_SIZE,OtherStuff.EDITOR_TILE_SIZE,_blankTileAnimation, -1, -1, name, false));
				
				}//for
				
			}//for
			
		}catch(Exception e){
			GameSkeleton.printDebugMessage("TilePalette - Error initializing TileSet in addTileGridToSet(): " + e.getMessage());
		}

	}//addTile
	
}//TileSet
