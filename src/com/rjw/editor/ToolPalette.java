package com.rjw.editor;

import java.awt.Cursor;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.util.Vector;

import com.rjw.gameskeleton.OtherStuff;
import com.rjw.gameskeleton.Sprite;
import com.rjw.gameskeleton.Timer;


/**
 * This is our tool palette that is shown in our editor
 * @author rwalsh
 *
 */
public class ToolPalette {

	public static final int TOOL_LABEL = 0;
	public static final int TOOL_BRUSH1x1 = 1;
	public static final int TOOL_BRUSH2x2 = 2;
	public static final int TOOL_BRUSH4x4 = 3;
	public static final int TOOL_ERASER= 4;
	public static final int TOOL_REPLACER = 5;
	public static final int TOOL_FILL = 6;
	public static final int TOOL_PICKER = 7;
	public static final int TOOL_THING_TOOL = 8;
	public static final int TOOL_SUSPENDED_SHOW_TILESETPALETTE = 9;
	public static final int TOOL_SUSPENDED_SHOW_THINGSETPALETTE = 10;
	
	public static final int TOOL_SPRITE_SIZE = 32;
	public static final int TOOL_SWITCH_TIMEOUT = 200;
	
	//our tool sprites and cursors
	Vector<Sprite> _tools;
	Vector<Cursor> _toolCursors;
	private Sprite _toolPaletteOverlay;
	private int _currentToolIndex = 0;
	// previous index will allow to switch back after using picker or other "one time execution" tools
	private int _previousToolIndex = 0;
	private Timer _toolSwitchTimer;
	
	public ToolPalette(){

		Point tempPoint = new Point(0,0);
		Toolkit tk = Toolkit.getDefaultToolkit();
		Image tempImage;
		
		_tools = new Vector<Sprite>();
		_toolCursors = new Vector<Cursor>();

		// LABEL
		_tools.add(new Sprite(OtherStuff.SPRITE_TOOL_PALETTE_LABEL, 10, 20 + (TOOL_LABEL * TOOL_SPRITE_SIZE)));
		tempImage = tk.createImage(OtherStuff.SPRITE_PATH_PREFIX + OtherStuff.SPRITE_TOOL_CURSOR_BLANK);
		_toolCursors.add(tk.createCustomCursor(tempImage, tempPoint, "BLANK"));
		
		// BRUSH1x1
		_tools.add(new Sprite(OtherStuff.SPRITE_TOOL_PALETTE_BRUSH1x1, 10, 20 + (TOOL_BRUSH1x1 * TOOL_SPRITE_SIZE)));
		tempImage = tk.createImage(OtherStuff.SPRITE_PATH_PREFIX + OtherStuff.SPRITE_TOOL_CURSOR_BLANK);
		_toolCursors.add(tk.createCustomCursor(tempImage, tempPoint, "BRUSH1x1"));
		
		// BRUSH2x2
		_tools.add(new Sprite(OtherStuff.SPRITE_TOOL_PALETTE_BRUSH2x2, 10, 20 + (TOOL_BRUSH2x2 * TOOL_SPRITE_SIZE)));
		tempImage = tk.createImage(OtherStuff.SPRITE_PATH_PREFIX + OtherStuff.SPRITE_TOOL_CURSOR_BLANK);
		_toolCursors.add(tk.createCustomCursor(tempImage, tempPoint, "BRUSH2x2"));
		
		// BRUSH4x4
		_tools.add(new Sprite(OtherStuff.SPRITE_TOOL_PALETTE_BRUSH4x4, 10, 20 + (TOOL_BRUSH4x4 * TOOL_SPRITE_SIZE)));
		tempImage = tk.createImage(OtherStuff.SPRITE_PATH_PREFIX + OtherStuff.SPRITE_TOOL_CURSOR_BLANK);
		_toolCursors.add(tk.createCustomCursor(tempImage, tempPoint, "BRUSH4x4"));
		
		// ERASER
		_tools.add(new Sprite(OtherStuff.SPRITE_TOOL_PALETTE_ERASER, 10, 20 + (TOOL_ERASER * TOOL_SPRITE_SIZE)));
		tempImage = tk.createImage(OtherStuff.SPRITE_PATH_PREFIX + OtherStuff.SPRITE_TOOL_CURSOR_ERASER);
		_toolCursors.add(tk.createCustomCursor(tempImage, tempPoint, "ERASER"));
		
		// REPLACER
		_tools.add(new Sprite(OtherStuff.SPRITE_TOOL_PALETTE_REPLACER, 10, 20 + (TOOL_REPLACER * TOOL_SPRITE_SIZE)));
		tempImage = tk.createImage(OtherStuff.SPRITE_PATH_PREFIX + OtherStuff.SPRITE_TOOL_CURSOR_REPLACER);
		_toolCursors.add(tk.createCustomCursor(tempImage, tempPoint, "REPLACER"));
		
		// FILL
		_tools.add(new Sprite(OtherStuff.SPRITE_TOOL_PALETTE_FILL, 10, 20 + (TOOL_FILL * TOOL_SPRITE_SIZE)));
		tempImage = tk.createImage(OtherStuff.SPRITE_PATH_PREFIX + OtherStuff.SPRITE_TOOL_CURSOR_FILL);
		_toolCursors.add(tk.createCustomCursor(tempImage, tempPoint, "FILL"));
		
		//PICKER
		_tools.add(new Sprite(OtherStuff.SPRITE_TOOL_PALETTE_PICKER, 10, 20 + (TOOL_PICKER* TOOL_SPRITE_SIZE)));
		tempImage = tk.createImage(OtherStuff.SPRITE_PATH_PREFIX + OtherStuff.SPRITE_TOOL_CURSOR_PICKER);
		_toolCursors.add(tk.createCustomCursor(tempImage, tempPoint, "PICKER"));
		
		//THING_TOOL
		_tools.add(new Sprite(OtherStuff.SPRITE_TOOL_PALETTE_THING_TOOL, 10, 20 + (TOOL_THING_TOOL* TOOL_SPRITE_SIZE)));
		tempImage = tk.createImage(OtherStuff.SPRITE_PATH_PREFIX + OtherStuff.SPRITE_TOOL_CURSOR_THING_TOOL);
		_toolCursors.add(tk.createCustomCursor(tempImage, tempPoint, "THING_TOOL"));
		
		//TILESET PALETTE (SUSPENDED) TOOL
		_tools.add(new Sprite(OtherStuff.SPRITE_TOOL_PALETTE_SUSPENDED_TOOL, 10, 20 + (TOOL_SUSPENDED_SHOW_TILESETPALETTE * TOOL_SPRITE_SIZE)));
		tempImage = tk.createImage(OtherStuff.SPRITE_PATH_PREFIX + OtherStuff.SPRITE_TOOL_CURSOR_SUSPENDED);
		_toolCursors.add(tk.createCustomCursor(tempImage, tempPoint, "TILESET_PALETTE"));

		//THINGSET PALETTE (SUSPENDED) TOOL
		_tools.add(new Sprite(OtherStuff.SPRITE_TOOL_PALETTE_SUSPENDED_TOOL, 10, 20 + (TOOL_SUSPENDED_SHOW_THINGSETPALETTE * TOOL_SPRITE_SIZE)));
		tempImage = tk.createImage(OtherStuff.SPRITE_PATH_PREFIX + OtherStuff.SPRITE_TOOL_CURSOR_SUSPENDED);
		_toolCursors.add(tk.createCustomCursor(tempImage, tempPoint, "THINGSET_PALETTE"));
		
		// and make our overlay default to TOOL_BRUSH1x1
		_toolPaletteOverlay = new Sprite(OtherStuff.SPRITE_TOOL_OVERLAY, 10, 20 + (TOOL_BRUSH1x1 * TOOL_SPRITE_SIZE));
		_currentToolIndex = TOOL_BRUSH1x1;
		_previousToolIndex = TOOL_BRUSH1x1;
		_toolSwitchTimer = new Timer(TOOL_SWITCH_TIMEOUT);
		
	}//constructor

	public Sprite getOverlayImage(){	return _toolPaletteOverlay; }
	public Vector<Sprite> getSprites(){ return _tools; }
	public int getSelectedToolIndex() { return _currentToolIndex; }
	public Cursor getCurrentCursor() { return _toolCursors.get(_currentToolIndex); }
	public void switchBackToPreviousTool(){ setToolSelection(_previousToolIndex); }
	//TODO: clean this up someday so it's not a big OR statement
	public boolean isSuspended(){ return ((_currentToolIndex == TOOL_SUSPENDED_SHOW_TILESETPALETTE) || (_currentToolIndex == TOOL_SUSPENDED_SHOW_THINGSETPALETTE)); }
	
	/**
	 * Turns tool suspension on/off, switching to the specified 
	 * tool during the suspension
	 * @param suspendToThisTool - the tool that we want to switch too while the others are suspended
	 */
	public void toggleSuspension(int suspendToThisTool){
	 
		//TODO: multiple tools that are suspended break this. combine "active" fix for which palette is on top 
		// in the editor with which tool is active when other tools are suspended
		
		// if the tool palette is already suspended, switch
		// back to the last tool used. Otherwise, remember the 
		// current tool that is selected, and suspend
		if(this.isSuspended()){
			switchBackToPreviousTool();
		}else{
			_previousToolIndex = _currentToolIndex;
			//_currentToolIndex = TOOL_SUSPENDED_SHOW_TILESETPALETTE;
			_currentToolIndex = suspendToThisTool;
		}
		
	}//toggleSuspended
	
	//simple function to update the tool switch timer
	public void updatePaletteItemSwitchTimer(long elapsedTime){ _toolSwitchTimer.update(elapsedTime); }
	
	/**
	 * this function sets our index to the selected tool, and moves the overlay to that new tool
	 * @param newSelection - the index of the tool we want to select
	 */
	public void setToolSelection(int newSelection) {
		
		//only switch if the timer is done
		if(_toolSwitchTimer.isDone()){
			
			_toolSwitchTimer.reset();
			_previousToolIndex = _currentToolIndex;
			_currentToolIndex = newSelection;
			
			// if our new tool selection is NOT the 
			// suspended tool, update the selection image
			if(newSelection != TOOL_SUSPENDED_SHOW_TILESETPALETTE){
				_toolPaletteOverlay.setY(20 + (_currentToolIndex * TOOL_SPRITE_SIZE));
			}
		
		}//if
		
	}//setToolSelection
	
}//ToolPalette
