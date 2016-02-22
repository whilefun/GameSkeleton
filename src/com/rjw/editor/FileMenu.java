package com.rjw.editor;

import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

import com.rjw.gameskeleton.AnimatedSprite;
import com.rjw.gameskeleton.Animation;
import com.rjw.gameskeleton.GameMenu;
import com.rjw.gameskeleton.GameMenuItem;
import com.rjw.gameskeleton.OtherStuff;
import com.rjw.gameskeleton.Sprite;
import com.rjw.gameskeleton.GameSkeleton;
/**
 * Our editor's File Menu for loading, saving, etc.
 * @author rwalsh
 *
 */
public class FileMenu extends GameMenu{
	
	
	public static final int MENU_ITEM_WIDTH = 32;
	public static final int MENU_ITEM_HEIGHT = 32;
	
	public static final int FILE_MENU_ITEM_COUNT = 4;
	public static final int MENU_ITEM_SAVE = 0;
	public static final int MENU_ITEM_SAVE_AS = 1;
	public static final int MENU_ITEM_LOAD = 2;
	public static final int MENU_ITEM_OPTIONS = 3;
	
	private BufferedImage _frameTile;
	
	/**
	 * A basic editor file menu
	 * @param x
	 * @param y
	 */
	public FileMenu(int x, int y){
		
		super(x, y, MENU_ITEM_WIDTH*FILE_MENU_ITEM_COUNT, MENU_ITEM_HEIGHT, FILE_MENU_ITEM_COUNT, null, null);
		
		// and also disable sound for this menu
		this.disableSound();
		
	}//constructor
	
	@Override
	/**
	 * @warning - This menu does not have a background image, so don't try to draw it!
	 */
	public void initMenuItemsAndBackground(){
		
		//try to load our animation sprite tile images
		try{	
			_frameTile = ImageIO.read(new File(OtherStuff.SPRITE_PATH_PREFIX + OtherStuff.SPRITE_TOOL_FILE_MENU));
		}catch(Exception e){
			GameSkeleton.printDebugMessage("error loading frames for main game menu ("+e.getMessage()+")");
		}
		
		//save
		_menuItems[0] = new GameMenuItem(new AnimatedSprite(_x, _y, 0, 0, new Animation(), true), new AnimatedSprite(_x, _y, 0, 0, new Animation(), true), 0, "Test Item");
		_menuItems[0].getRegularItemImage().getAnimation().addFrame(_frameTile.getSubimage(MENU_ITEM_WIDTH*0, 0, MENU_ITEM_WIDTH, MENU_ITEM_HEIGHT), 300);
		_menuItems[0].getSelectedItemImage().getAnimation().addFrame(_frameTile.getSubimage(MENU_ITEM_WIDTH*0, MENU_ITEM_HEIGHT, MENU_ITEM_WIDTH, MENU_ITEM_HEIGHT), 300);
		
		//saveas
		_menuItems[1] = new GameMenuItem(new AnimatedSprite(_x, _y, 0, 0, new Animation(), true), new AnimatedSprite(_x, _y, 0, 0, new Animation(), true), 1, "Test Item2");
		_menuItems[1].getRegularItemImage().getAnimation().addFrame(_frameTile.getSubimage(MENU_ITEM_WIDTH*1, 0, MENU_ITEM_WIDTH, MENU_ITEM_HEIGHT), 300);
		_menuItems[1].getSelectedItemImage().getAnimation().addFrame(_frameTile.getSubimage(MENU_ITEM_WIDTH*1, MENU_ITEM_HEIGHT, MENU_ITEM_WIDTH, MENU_ITEM_HEIGHT), 300);		
		
		//load
		_menuItems[2] = new GameMenuItem(new AnimatedSprite(_x, _y, 0, 0, new Animation(), true), new AnimatedSprite(_x, _y, 0, 0, new Animation(), true), 2, "Test Item3");
		_menuItems[2].getRegularItemImage().getAnimation().addFrame(_frameTile.getSubimage(MENU_ITEM_WIDTH*2, 0, MENU_ITEM_WIDTH, MENU_ITEM_HEIGHT), 300);
		_menuItems[2].getSelectedItemImage().getAnimation().addFrame(_frameTile.getSubimage(MENU_ITEM_WIDTH*2, MENU_ITEM_HEIGHT, MENU_ITEM_WIDTH, MENU_ITEM_HEIGHT), 300);
		
		// options
		_menuItems[3] = new GameMenuItem(new AnimatedSprite(_x, _y, 0, 0, new Animation(), true), new AnimatedSprite(_x, _y, 0, 0, new Animation(), true), 3, "Test Item4");
		_menuItems[3].getRegularItemImage().getAnimation().addFrame(_frameTile.getSubimage(MENU_ITEM_WIDTH*3, 0, MENU_ITEM_WIDTH, MENU_ITEM_HEIGHT), 300);
		_menuItems[3].getSelectedItemImage().getAnimation().addFrame(_frameTile.getSubimage(MENU_ITEM_WIDTH*3, MENU_ITEM_HEIGHT, MENU_ITEM_WIDTH, MENU_ITEM_HEIGHT), 300);
		
		//TODO: yes, this is inefficient since the menu graphic is solid...oh well.
		//_menuBackground = new Sprite("tool_file_menu_background.png");
		
		
		//GameSkeleton.printDebugMessage("FileMenu: There are " + _menuItems.length + " menu items.");
		
	}//initMenuItems
	
	
	
	
	
	
	
}//FileMenu
