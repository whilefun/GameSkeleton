package com.rjw.gameskeleton;

//import com.rjw.sfx.SoundManager_Deprecated;
import com.rjw.sfx.SoundManagerSingleton;

/**
 * This is our generic GameMenu class
 * @author rwalsh
 *
 */
public abstract class GameMenu{

	protected GameMenuItem[] _menuItems;
	protected Sprite _menuBackground;
	protected int _currentMenuItem;
	protected int _x;
	protected int _y;
	protected int _w;
	protected int _h;
	//private SoundManager _menuSoundManager;
	private String _menuMoveSoundFilename;
	private String _menuSelectSoundFilename;
	private boolean _soundsEnabled; 

	/**
	 * This function creates a generic game menu from the parameters specified
	 * @param x - x value for top left corner of the menu
	 * @param y - y value for top left corner of the menu
	 * @param w - the width of the menu
	 * @param h - the height of the menu
	 * @param menuItemCount - the number of items to have in the menu
	 * @param menuMoveSoundFilename - the sound to make when moving between menu items. If null, no sound is added.
	 * @param menuSelectSoundFilename - the sound to make when selecting a menu item. If null, no sound is added
	 */
	public GameMenu(int x, int y, int w, int h, int menuItemCount, String menuMoveSoundFilename, String menuSelectSoundFilename){

		// other crap
		_x = x;
		_y = y;
		_w = w;
		_h = h;
		_menuItems = new GameMenuItem[menuItemCount];
		_currentMenuItem = 0;
		_soundsEnabled = true;
		
		// sounds
		//_menuSoundManager = new SoundManager();
		if(menuMoveSoundFilename != null){
			//_menuSoundManager.loadSound(menuMoveSoundFilename);
			_menuMoveSoundFilename = menuMoveSoundFilename;
		}
		if(menuSelectSoundFilename != null){
			//_menuSoundManager.loadSound(menuSelectSoundFilename);
			_menuSelectSoundFilename = menuSelectSoundFilename;
		}

		// menu items
		initMenuItemsAndBackground();
		
	}//constructor
	
	public void getPreviousMenuItem(){
		if(_soundsEnabled){
			//_menuSoundManager.playSound(_menuMoveSoundFilename);
			SoundManagerSingleton.getSingletonObject().playSound(_menuMoveSoundFilename);
		}
		_currentMenuItem = ((_currentMenuItem == 0)?(_menuItems.length-1) : (_currentMenuItem - 1));
	}
	
	public void getNextMenuItem(){
		if(_soundsEnabled){
			//_menuSoundManager.playSound(_menuMoveSoundFilename);
			SoundManagerSingleton.getSingletonObject().playSound(_menuMoveSoundFilename);
		}
		_currentMenuItem = ((_currentMenuItem == _menuItems.length-1)? (0) : (_currentMenuItem + 1));
	}
	
	public GameMenuItem[] getMenuItems() {
		return _menuItems;
	}

	public int getCurrentMenuItem() {
		return _currentMenuItem;
	}

	public int getX() {
		return _x;
	}

	public int getY() {
		return _y;
	}
	
	public int getW() {
		return _w;
	}
	
	public int getH() {
		return _h;
	}

	public Sprite getMenuBackground() {
		return _menuBackground;
	}

	public boolean isSoundEnabled(){ return _soundsEnabled; }
	public void disableSound(){ _soundsEnabled = false; }
	public void enableSound(){ _soundsEnabled = true; }
	
	/**
	 * This function is abstract and meant to initialize all the items
	 * in the menuItems array (namely GameMenuItems), as well as the 
	 * game menu background sprite menuBackground.
	 */
	public abstract void initMenuItemsAndBackground();
	
}//GameMenu
