package com.rjw.gameskeleton;

import java.awt.image.BufferedImage;

/**
 * A class for the items in our menu
 * @author rwalsh
 *
 */
public class GameMenuItem {

	// two images - one selected one regular (unselected). Think HTML mouseovers! hah.
	private AnimatedSprite _regularItemImage;
	private AnimatedSprite _selectedItemImage;
	private int _menuItemNumber;
	private String _menuItemName;
	
	public GameMenuItem(AnimatedSprite regular, AnimatedSprite selected, int menuItemNumber, String menuItemName){
		
		_regularItemImage = regular;
		_selectedItemImage = selected;
		_menuItemNumber = menuItemNumber;
		_menuItemName = menuItemName;		
		
	}//constructor

	//getters and setters
	public AnimatedSprite getRegularItemImage() {
		return _regularItemImage;
	}

	public void setRegularItemImage(AnimatedSprite regularItemImage) {
		_regularItemImage = regularItemImage;
	}

	public AnimatedSprite getSelectedItemImage() {
		return _selectedItemImage;
	}

	public void setSelectedItemImage(AnimatedSprite selectedItemImage) {
		_selectedItemImage = selectedItemImage;
	}

	public int getMenuItemNumber() {
		return _menuItemNumber;
	}

	public void setMenuItemNumber(int menuItemNumber) {
		_menuItemNumber = menuItemNumber;
	}

	public String getMenuItemName() {
		return _menuItemName;
	}

	public void setMenuItemName(String menuItemName) {
		_menuItemName = menuItemName;
	}
	
	
	
	
}//GameMenuItem
