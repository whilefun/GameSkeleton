package com.rjw.gameskeleton;

import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;

/**
 * Our main game menu
 * @author rwalsh
 *
 */
public class MainGameMenu extends GameMenu{

	public static final int MAIN_MENU_ITEM_COUNT = 3;
	public static final int MENU_ITEM_WIDTH = 224;
	public static final int MENU_ITEM_HEIGHT = 32;
	
	public static final int MENU_ITEM_TBD1 = 0;
	public static final int MENU_ITEM_TBD2 = 1;
	public static final int MENU_ITEM_QUIT_GAME = 2;
	
	public static final String MENU_SOUND_MOVE = "sound_sfx_main_menu_move.wav";
	public static final String MENU_SOUND_SELECT = "sound_sfx_main_menu_select.wav";
	
	private BufferedImage _frameTile;
	
	/**
	 * 
	 * @param x
	 * @param y
	 */
	public MainGameMenu(int x, int y){
		//super(x, y, MAIN_MENU_ITEM_COUNT, OtherStuff.SFX_PATH_PREFIX + MENU_SOUND_MOVE, null);
		super(x, y, OtherStuff.SCREEN_WIDTH, OtherStuff.SCREEN_HEIGHT, MAIN_MENU_ITEM_COUNT, MENU_SOUND_MOVE, MENU_SOUND_SELECT);
	}
	
	@Override
	public void initMenuItemsAndBackground(){
		
		//try to load our animation sprite tile images
		try{	
			_frameTile = ImageIO.read(new File(OtherStuff.SPRITE_PATH_PREFIX + OtherStuff.SPRITE_MAIN_GAME_MENU));
		}catch(Exception e){
			GameSkeleton.printDebugMessage("error loading frames for main game menu ("+e.getMessage()+")");
		}
		
		_menuItems[0] = new GameMenuItem(new AnimatedSprite(_x, _y + MENU_ITEM_HEIGHT*0, 0, 0, new Animation(), true), new AnimatedSprite(_x, _y, 0, 0, new Animation(), true), 0, "Test Item");
		_menuItems[0].getRegularItemImage().getAnimation().addFrame(_frameTile.getSubimage(0, MENU_ITEM_HEIGHT*0, MENU_ITEM_WIDTH, MENU_ITEM_HEIGHT), 300);
		_menuItems[0].getSelectedItemImage().getAnimation().addFrame(_frameTile.getSubimage(0, MENU_ITEM_HEIGHT*1, MENU_ITEM_WIDTH, MENU_ITEM_HEIGHT), 300);
		
		_menuItems[1] = new GameMenuItem(new AnimatedSprite(_x, _y + MENU_ITEM_HEIGHT*1, 0, 0, new Animation(), true), new AnimatedSprite(_x, _y, 0, 0, new Animation(), true), 1, "Test Item2");
		_menuItems[1].getRegularItemImage().getAnimation().addFrame(_frameTile.getSubimage(0, MENU_ITEM_HEIGHT*2, MENU_ITEM_WIDTH, MENU_ITEM_HEIGHT), 300);
		_menuItems[1].getSelectedItemImage().getAnimation().addFrame(_frameTile.getSubimage(0, MENU_ITEM_HEIGHT*3, MENU_ITEM_WIDTH, MENU_ITEM_HEIGHT), 300);
		
		_menuItems[2] = new GameMenuItem(new AnimatedSprite(_x, _y + MENU_ITEM_HEIGHT*2, 0, 0, new Animation(), true), new AnimatedSprite(_x, _y, 0, 0, new Animation(), true), 2, "Test Item3");
		_menuItems[2].getRegularItemImage().getAnimation().addFrame(_frameTile.getSubimage(0, MENU_ITEM_HEIGHT*4, MENU_ITEM_WIDTH, MENU_ITEM_HEIGHT), 300);
		_menuItems[2].getSelectedItemImage().getAnimation().addFrame(_frameTile.getSubimage(0, MENU_ITEM_HEIGHT*5, MENU_ITEM_WIDTH, MENU_ITEM_HEIGHT), 300);
		
		_menuBackground = new Sprite(OtherStuff.SPRITE_MAIN_GAME_MENU_BACKGROUND);
		
		
		//GameSkeleton.printDebugMessage("MainGameMenu: There are " + _menuItems.length + " menu items.");
		
	}//initMenuItems

	
	
}//MainGameMenu
