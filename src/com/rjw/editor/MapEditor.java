package com.rjw.editor;

import java.awt.Graphics;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

import com.rjw.gameskeleton.ExitListener;
import com.rjw.gameskeleton.KeyboardInput;
import com.rjw.gameskeleton.Level;
import com.rjw.gameskeleton.MouseInput;
import com.rjw.gameskeleton.OtherStuff;
import com.rjw.gameskeleton.Sprite;
import com.rjw.gameskeleton.Thing;
import com.rjw.gameskeleton.ThingManager;
import com.rjw.gameskeleton.Tile;
import com.rjw.gameskeleton.Timer;
import com.rjw.gameskeleton.Animation;
import com.rjw.gameskeleton.GameSkeleton;
//TODO: replace int with short/long for more platform independence for future languages
public class MapEditor extends JPanel /*implements MouseListener*/ {
	
    //BlankArea blankArea;
	private KeyboardInput _keyboardInput = new KeyboardInput(); // _keyboardInput polling
	private MouseInput _mouseInput = new MouseInput();
	private BufferedImage _fOffscreenImage;
	private Graphics _fOffscreenGraphics;
	private String _eventDetails;
	
	// our tool palette and related stuff
	private ToolPalette _toolPalette;
	//private TilePalette _tilePalette;
	//private ThingPalette _thingPalette;
	private Timer _toolPaletteSwitchTimer;
	//TODODONE: experimental palettes
	private TileSetPalette _tilesetPalette;
	private ThingSetPalette _thingsetPalette;
	// for suspending our tool palette so we can make
	// mouse clicks do non-tool/map related things like
	// select tiles and things from overlayed palettes
	private boolean _toolPaletteActive;
	
	//timing stuff
	private Timer _generalKeyTimer;	//to slow key rates for things like save/load/etc.
	private Timer _autoSaveTimer;	//TODO: autosave
	private long _startTime;
    private long _currTime;
    private long _elapsedTime;
	
	// our level and things
    private Level _levelWithLayers;
    private int _currentLevelLayer;
    private int _thingCount;
    
	//sprites etc.
	private Tile _blankTile;
	private BufferedImage _tempTileImage;
	private Animation _blankTileAnimation;
	private Tile _currentTile;
	private Tile _previousTile;
	private Thing _currentThing;
	private Thing _previousThing;
	private Sprite _layerManagerSprite;
	private boolean _showGridLines;
	
	// file menu (save, load, etc.)
	private FileMenu _fileMenu;
	private boolean _showHelpHints;
	private Sprite _helpHintsSprite;
	
	private EditorOptionsDialog _editorOptionsDialog;
	
	
	// main method
    public static void main(String[] args) {
        
    	MapEditor app = new MapEditor();
    	//WindowUtilities.openInJFrame(app, (OtherStuff.EDITOR_CAMERA_WIDTH * OtherStuff.TILE_WIDTH), (OtherStuff.EDITOR_CAMERA_HEIGHT * OtherStuff.TILE_WIDTH));
    	//WindowUtilities.openInJFrame(app, OtherStuff.EDITOR_SCREEN_W, OtherStuff.EDITOR_SCREEN_H + OtherStuff.JAVA_JFRAME_WINDOW_OFFSET);
		
    	
    	//TODO: make this class extend JFrame so Dialogs work properly 
    	JFrame frame = new JFrame("Title");
        frame.setBackground(Color.white);
        frame.setSize(OtherStuff.EDITOR_SCREEN_W, OtherStuff.EDITOR_SCREEN_H + OtherStuff.JAVA_JFRAME_WINDOW_OFFSET);
        frame.setContentPane(app);
        frame.addWindowListener(new ExitListener());
        frame.setVisible(true);
    	
    	
    	app.run();
		System.exit(0);
        
    }//main

    //Constructor
    public MapEditor() {
    	
		//our key listener
		setFocusable(true);
		addKeyListener(_keyboardInput);
		//our mouse AAAAAAAAAAND mouse motion listeners
		addMouseListener(_mouseInput);
		addMouseMotionListener(_mouseInput);
		
		//init graphical elements and such
		_fOffscreenImage = new BufferedImage(OtherStuff.EDITOR_SCREEN_W, OtherStuff.EDITOR_SCREEN_H,1);
		_fOffscreenGraphics = _fOffscreenImage.getGraphics();
		_eventDetails = "none yet.";
        
		try{
			
			//Keep track of which layer we're on. Make the default for the editor be the Action layer
			_currentLevelLayer = OtherStuff.LAYER_ACTION;
			
			_levelWithLayers = new Level("sample_level.xml");
			if(!_levelWithLayers.loadLevelWithLayers("sample_level.xml")){
				throw new Exception("Error loading level");
			}
			_levelWithLayers.moveXY( 0, -((_levelWithLayers.getMapAtIndex(_currentLevelLayer).getRowCount() * OtherStuff.EDITOR_TILE_SIZE) - OtherStuff.EDITOR_SCREEN_H) );
			
	        //init our tool palette and set the default tool selection to be the 1x1 brush
	        _toolPalette = new ToolPalette();
	        _toolPalette.setToolSelection(ToolPalette.TOOL_BRUSH1x1);
	        _toolPaletteSwitchTimer = new Timer(ToolPalette.TOOL_SWITCH_TIMEOUT);
			updateCursor();
			
			//TODODONE: init our experimental tileset and thingset palettes
			_tilesetPalette = new TileSetPalette(OtherStuff.EDITOR_SCREEN_W, OtherStuff.EDITOR_SCREEN_H);
			_thingsetPalette = new ThingSetPalette(OtherStuff.EDITOR_SCREEN_W, OtherStuff.EDITOR_SCREEN_H);
			
			//TODODONE: init our _currentTile, _previousTile, and _currentThing
			_currentTile = _tilesetPalette.getCurrentPaletteItem().getTileFromSet(0, 0);
			_previousTile = _currentTile;
			_currentThing = _thingsetPalette.getCurrentPaletteItem().getThingFromSet(0, 0);
			
			//by default, make our toolPalette active
			_toolPaletteActive = true;
			
			_layerManagerSprite = new Sprite(OtherStuff.SPRITE_TOOL_LAYER_MANAGER);
			
			//init our general key timer
			_generalKeyTimer = new Timer(OtherStuff.EDITOR_KEY_TIMEOUT);
			
			_showGridLines = true;

			//_loadedTiles = new Hashtable<String, short[][]>();
			
		}catch(Exception e){
			GameSkeleton.printDebugMessage("MapEditor: Error creating MapEditor! " + e.getMessage());
			GameSkeleton.printDebugMessage("(Possible Causes: Missing Things/Thing/Map/Row objects in XML files)");
			System.exit(1);
		}
        
		//TODO: file menu
		_fileMenu = new FileMenu(0,568);
		
		//TODO: help hints
		_showHelpHints = false;
		_helpHintsSprite = new Sprite("tool_help_hints.png");
		
		_editorOptionsDialog = new EditorOptionsDialog(this);
		_editorOptionsDialog.refreshLevelOptionValues(_levelWithLayers);
		
    }//constructor
    
    /*
     * Basically, we're running our editor like it's the actual game, but not with
     * any of the gameplay logic or movement. So this is our main "tick" loop
     */
    public void run(){
    	
    	//get the wall time when we started
		_startTime = System.currentTimeMillis();
	    _currTime = _startTime;
    	
    	while(true){
    		
			//update our timing info
			_elapsedTime = System.currentTimeMillis() - _currTime;
			_currTime += _elapsedTime;
    		
    		//update our timers
			_toolPalette.updatePaletteItemSwitchTimer(_elapsedTime);
			//_tilePalette.updatePaletteItemSwitchTimer(_elapsedTime);
			//_thingPalette.updatePaletteItemSwitchTimer(_elapsedTime);
			_generalKeyTimer.update(_elapsedTime);
    		
    		//process keyboard input
    		_keyboardInput.poll();
    		processKeyboardInput();
    		
    		//process mouse input
    		_mouseInput.poll();
    		processMouseInput();
    		
    		// update Thing and Map Tile animations
    		updateAnimations(_elapsedTime);
    		
    		//render//
    		render();
    		
			// Let the OS wait a bit
    		//TODO: put in frame rate regulator
			try{
				Thread.sleep(10);
			}catch(InterruptedException e){}
    		
    	}//main loop
    	
    }//run
    
    /*
     * This function does all our map and related rendering
     */
    public void render(){
    	
    	// blank out background
    	_fOffscreenGraphics.setColor(Color.BLACK);
    	_fOffscreenGraphics.fillRect(0,0, OtherStuff.EDITOR_SCREEN_W, OtherStuff.EDITOR_SCREEN_H);
    	
    	_fOffscreenGraphics.setColor(Color.GREEN);
    	
    	// LAYERS OF MAP
    	for(int layerCounter = 0; layerCounter < _levelWithLayers.getNumberOfLayers(); layerCounter++){
    	
    		//if the layer is visible, draw all it's components
    		if(_levelWithLayers.getLayerAtIndex(layerCounter).isVisible()){
    		
				// CURRENT MAP //
				for(int x = 0; x < _levelWithLayers.getMapAtIndex(layerCounter).getRowCount(); x++){
					
					for(int y = 0; y < _levelWithLayers.getMapAtIndex(layerCounter).getColCount(); y++){
						
						//Renders tiles 40% faster. Bam.
						if(
								(_levelWithLayers.getMapAtIndex(layerCounter).getMap()[x][y] != null) &&
								(_levelWithLayers.getMapAtIndex(layerCounter).getMap()[x][y].getX() >= 0) &&
	    						(_levelWithLayers.getMapAtIndex(layerCounter).getMap()[x][y].getX() < OtherStuff.EDITOR_SCREEN_W) &&
	    						(_levelWithLayers.getMapAtIndex(layerCounter).getMap()[x][y].getY() >= -OtherStuff.TILE_SIZE) &&
	    						(_levelWithLayers.getMapAtIndex(layerCounter).getMap()[x][y].getY() < OtherStuff.EDITOR_SCREEN_H) 
						){
							_fOffscreenGraphics.drawImage( _levelWithLayers.getMapAtIndex(layerCounter).getMap()[x][y].getImage(),_levelWithLayers.getMapAtIndex(layerCounter).getMap()[x][y].getX(),_levelWithLayers.getMapAtIndex(layerCounter).getMap()[x][y].getY(),null);
						}
						
						
					}//for
					
				}//forMapTiles
	
		        // THINGS //
				Thing tempThing;
				for(int thingIndex = 0; thingIndex < _levelWithLayers.getThingsAtIndex(layerCounter).length; thingIndex++){
					
					tempThing = ((Thing) _levelWithLayers.getThingsAtIndex(layerCounter)[thingIndex]);
					_fOffscreenGraphics.drawImage(tempThing.getMainImage(),tempThing.getX(), tempThing.getY(),null);
					//DEBUG: also draw the x,y or each thing below it
					_fOffscreenGraphics.setColor(Color.GREEN);
					_fOffscreenGraphics.drawString("["+tempThing.getX()+","+tempThing.getY()+"]", tempThing.getX(),tempThing.getY());
					
				}//forThings
				
    		}//ifLayerVisible
			
    	}//forLayers
		
    	// GRID LINES //
    	if(_showGridLines){
    		
    		// draw overall box
    		_fOffscreenGraphics.drawRect(_levelWithLayers.getMapAtIndex(0).getX(), _levelWithLayers.getMapAtIndex(0).getY(), _levelWithLayers.getMapAtIndex(0).getColCount()*OtherStuff.TILE_SIZE, _levelWithLayers.getMapAtIndex(0).getRowCount()*OtherStuff.TILE_SIZE);

    		//then draw lines for every row and column
    		for(int colCount = 0; colCount < _levelWithLayers.getMapAtIndex(0).getColCount(); colCount++)
    			_fOffscreenGraphics.drawLine(_levelWithLayers.getMapAtIndex(0).getX() + (colCount*OtherStuff.TILE_SIZE), _levelWithLayers.getMapAtIndex(0).getY(),_levelWithLayers.getMapAtIndex(0).getX() + (colCount*OtherStuff.TILE_SIZE), _levelWithLayers.getMapAtIndex(0).getY() + (_levelWithLayers.getMapAtIndex(0).getRowCount()*OtherStuff.TILE_SIZE));
    		
    		for(int rowCount = 0; rowCount < _levelWithLayers.getMapAtIndex(0).getRowCount(); rowCount++)
    			_fOffscreenGraphics.drawLine(_levelWithLayers.getMapAtIndex(0).getX(), _levelWithLayers.getMapAtIndex(0).getY() + (rowCount*OtherStuff.TILE_SIZE),_levelWithLayers.getMapAtIndex(0).getX() + ( _levelWithLayers.getMapAtIndex(0).getColCount()*OtherStuff.TILE_SIZE), _levelWithLayers.getMapAtIndex(0).getY() + (rowCount*OtherStuff.TILE_SIZE));
    		
    		
    	}//ifGridLines
    	
    	// map coords
    	_fOffscreenGraphics.drawString("_map Pos("+_levelWithLayers.getMapAtIndex(_currentLevelLayer).getX()+","+_levelWithLayers.getMapAtIndex(_currentLevelLayer).getY()+")", OtherStuff.EDITOR_SCREEN_W - 200, OtherStuff.EDITOR_SCREEN_H - 100);
    	
    	
    	// DRAW THE CURRENT TOOL, if applicable //
    	// TOOL_BRUSH1x1 //
    	if(_toolPalette.getSelectedToolIndex() == ToolPalette.TOOL_BRUSH1x1){
    		_fOffscreenGraphics.drawImage(_currentTile.getImage(),
											_mouseInput.getPosition().x - OtherStuff.EDITOR_TILE_SIZE/2, 
											_mouseInput.getPosition().y - OtherStuff.EDITOR_TILE_SIZE/2, 
											null);
    	}//TOOL_BRUSH1x1
    	// TOOL_BRUSH2x2 //
    	else if(_toolPalette.getSelectedToolIndex() == ToolPalette.TOOL_BRUSH2x2){
    		_fOffscreenGraphics.drawImage(_currentTile.getImage(),
					_mouseInput.getPosition().x - OtherStuff.EDITOR_TILE_SIZE/2, 
					_mouseInput.getPosition().y - OtherStuff.EDITOR_TILE_SIZE/2, 
					null);
    		
    		_fOffscreenGraphics.drawImage(_currentTile.getImage(),
					_mouseInput.getPosition().x + OtherStuff.EDITOR_TILE_SIZE - OtherStuff.EDITOR_TILE_SIZE/2, 
					_mouseInput.getPosition().y - OtherStuff.EDITOR_TILE_SIZE/2, 
					null);
    		
    		_fOffscreenGraphics.drawImage(_currentTile.getImage(),
					_mouseInput.getPosition().x - OtherStuff.EDITOR_TILE_SIZE/2, 
					_mouseInput.getPosition().y + OtherStuff.EDITOR_TILE_SIZE - OtherStuff.EDITOR_TILE_SIZE/2, 
					null);
    		
    		_fOffscreenGraphics.drawImage(_currentTile.getImage(),
					_mouseInput.getPosition().x + OtherStuff.EDITOR_TILE_SIZE - OtherStuff.EDITOR_TILE_SIZE/2, 
					_mouseInput.getPosition().y + OtherStuff.EDITOR_TILE_SIZE - OtherStuff.EDITOR_TILE_SIZE/2, 
					null);
    	}//TOOL_BRUSH2x2
    	
    	
    	// Draw ToolPalette //
    	//TODO: don't draw suspended tool icons (polkaroo)
    	for(int toolIndex = 0; toolIndex < _toolPalette.getSprites().size(); toolIndex++){
    		_fOffscreenGraphics.drawImage(_toolPalette.getSprites().get(toolIndex).getImage(), _toolPalette.getSprites().get(toolIndex).getX(), _toolPalette.getSprites().get(toolIndex).getY(), null);
    	}
    	// draw overlay for currently selected tool
    	_fOffscreenGraphics.drawImage(_toolPalette.getOverlayImage().getImage(), _toolPalette.getOverlayImage().getX(), _toolPalette.getOverlayImage().getY(), null);

    	//
    	//TODO: Draw the active one of thing/tile-setpalette on top, depending on which is active
    	///
    	// Draw TilesetPalette //
    	// TODO: draw overlayed grid?
    	_fOffscreenGraphics.drawImage(_tilesetPalette.getPaletteImage().getImage(), _tilesetPalette.getX(),_tilesetPalette.getY(), null);
    	// draw the current tileset image
    	_fOffscreenGraphics.drawImage(_tilesetPalette.getCurrentPaletteItem().getImage(), _tilesetPalette.getX() + TileSetPalette.TILESET_PALETTE_TILE_GRID_X_OFFSET, _tilesetPalette.getY() + + TileSetPalette.TILESET_PALETTE_TILE_GRID_Y_OFFSET, null);
    	//TODO: draw name of active tile set in name spot
    	
    	// Draw ThingsetPalette //
    	_fOffscreenGraphics.drawImage(_thingsetPalette.getPaletteImage().getImage(), _thingsetPalette.getX(),_thingsetPalette.getY(), null);
    	// draw the current thingset image
    	_fOffscreenGraphics.drawImage(_thingsetPalette.getCurrentPaletteItem().getImage(), _thingsetPalette.getX() + ThingSetPalette.THINGSET_PALETTE_THING_GRID_X_OFFSET, _thingsetPalette.getY() + + ThingSetPalette.THINGSET_PALETTE_THING_GRID_Y_OFFSET, null);
    	//TODO: draw name of active thing set in name spot
    	
    	
    	//DEBUG: draw currently selected tool number
    	_fOffscreenGraphics.drawString("TOOL#: " + _toolPalette.getSelectedToolIndex(),100,100);
    	
    	// Draw mouse coords //
    	//TODO: make this optional
    	_fOffscreenGraphics.drawString("["+_mouseInput.getPosition().x+","+_mouseInput.getPosition().y+"]", 
							    			_mouseInput.getPosition().x, 
							    			_mouseInput.getPosition().y);
    	

    	// LAYER MANAGER //
    	_fOffscreenGraphics.drawImage(_layerManagerSprite.getImage(), OtherStuff.EDITOR_LAYER_MANAGER_X,OtherStuff.EDITOR_LAYER_MANAGER_Y, null);
    	// draw the selection box
    	_fOffscreenGraphics.setColor(Color.CYAN);
    	_fOffscreenGraphics.drawRect(OtherStuff.EDITOR_LAYER_MANAGER_LAYERS_X_OFFSET + (_currentLevelLayer*(OtherStuff.EDITOR_LAYER_MANAGER_BOX_GAP + OtherStuff.EDITOR_LAYER_MANAGER_BOX_W)), OtherStuff.EDITOR_LAYER_MANAGER_LAYERS_SELECTION_INDICATOR_Y_OFFSET, OtherStuff.EDITOR_LAYER_MANAGER_BOX_W, OtherStuff.EDITOR_LAYER_MANAGER_SELECTION_INDICATOR_BOX_H);
    	// draw the visibility indicators
    	for(int layerCounter = 0; layerCounter < _levelWithLayers.getNumberOfLayers(); layerCounter++){
    		if(_levelWithLayers.getLayerAtIndex(layerCounter).isVisible()){
    			_fOffscreenGraphics.setColor(Color.GREEN);
    		}else{
    			_fOffscreenGraphics.setColor(Color.RED);
    		}
    		_fOffscreenGraphics.fillRect(OtherStuff.EDITOR_LAYER_MANAGER_LAYERS_X_OFFSET + (layerCounter*(OtherStuff.EDITOR_LAYER_MANAGER_BOX_GAP + OtherStuff.EDITOR_LAYER_MANAGER_BOX_W)), OtherStuff.EDITOR_LAYER_MANAGER_LAYERS_VIS_BOX_Y_OFFSET, OtherStuff.EDITOR_LAYER_MANAGER_BOX_W, OtherStuff.EDITOR_LAYER_MANAGER_VIS_BOX_H);
    	}//forLayers
    	
    	
    	
    	//TODO: menu stuff
		// if our menu is active, draw the menu background, then all the items
		
		// for all menu items, draw the item images
		for(int menuIndex = 0; menuIndex < _fileMenu.getMenuItems().length; menuIndex++){
			
			// if this one is the selected one, draw the selected image. otherwise, draw the regular image
			if(menuIndex == _fileMenu.getCurrentMenuItem()){
				_fOffscreenGraphics.drawImage(_fileMenu.getMenuItems()[menuIndex].getSelectedItemImage().getAnimation().getImage(), _fileMenu.getX() + FileMenu.MENU_ITEM_WIDTH*menuIndex, _fileMenu.getY(), null);
			}else{
				_fOffscreenGraphics.drawImage(_fileMenu.getMenuItems()[menuIndex].getRegularItemImage().getAnimation().getImage(), _fileMenu.getX() + FileMenu.MENU_ITEM_WIDTH*menuIndex, _fileMenu.getY(), null);
			}
			
		}//for
		
		//DEBUG
		_fOffscreenGraphics.drawString(_fileMenu.getCurrentMenuItem()+"", 100, 100 );
    	
		
		
		//TODO: Help Hints
		_fOffscreenGraphics.drawString("Press F1 for Help", 4, 16);
		if(_showHelpHints){
			_fOffscreenGraphics.drawImage(_helpHintsSprite.getImage(), 0, 0, null);
		}
		
		
    	// aaaaand blit.
    	paint(getGraphics());
    	
    }//render
    
	//paint what we rendered in a double-buffered kinda way
	public void paint(Graphics g){
		
		if (_fOffscreenImage!=null){
			g.drawImage(_fOffscreenImage, 0, 0, this);
		}else{
			GameSkeleton.printDebugMessage("fOffScreenImage is null");
		}
		
	}//paint
    
	/**
	 * Processes key presses from keyboard
	 */
	protected void processKeyboardInput(){
		
		
		//TODO: FIle Menu Stuff
		// previous menu item
    	if( _keyboardInput.keyDown( KeyEvent.VK_LEFT) ) {
			
    		if(_generalKeyTimer.isDone()){
    			_fileMenu.getPreviousMenuItem();
    			_generalKeyTimer.reset();
    		}
    		
		}
    	// next menu item
    	if( _keyboardInput.keyDown( KeyEvent.VK_RIGHT) ) {
    		
    		if(_generalKeyTimer.isDone()){
    			_fileMenu.getNextMenuItem();
    			_generalKeyTimer.reset();
    		}
    		
		}
    	// execute menu item
    	if( _keyboardInput.keyDownOnce( KeyEvent.VK_ENTER ) ) {

    		if(_generalKeyTimer.isDone()){
    			
    			
    			switch(_fileMenu.getCurrentMenuItem()){
    			
    				case FileMenu.MENU_ITEM_SAVE:
    					saveLevel();
    					break;
    				case FileMenu.MENU_ITEM_SAVE_AS:
    					saveLevelAs();
    					break;
    				case FileMenu.MENU_ITEM_LOAD:
    					loadLevel();
    					break;
    				case FileMenu.MENU_ITEM_OPTIONS:
    					showOptions();
    					break;
    				default:
    					break;
    					
    			}//switch
    			
    			_generalKeyTimer.reset();
    			
    		}//if
    		
		}//ifExecuteMenu
    	
		//TODO: Help Menu/Hints
    	if( _keyboardInput.keyDown( KeyEvent.VK_F1) ) {
    		
    		if(_generalKeyTimer.isDone()){
    			_showHelpHints = !_showHelpHints;
    			_generalKeyTimer.reset();
    		}

		}//help
		
		
		//move map "up"
    	if( _keyboardInput.keyDown( KeyEvent.VK_W ) ) {
    		moveMapAndThings(0, OtherStuff.EDITOR_CAMERA_Y_SPEED);
		}
		//move map "down"
    	if( _keyboardInput.keyDown( KeyEvent.VK_S ) ) {
    		moveMapAndThings(0, -OtherStuff.EDITOR_CAMERA_Y_SPEED);
		}
		//move map "left"
    	if( _keyboardInput.keyDown( KeyEvent.VK_A ) ) {
    		moveMapAndThings(OtherStuff.EDITOR_CAMERA_X_SPEED, 0);
		}
		//move map "right"
    	if( _keyboardInput.keyDown( KeyEvent.VK_D ) ) {
    		moveMapAndThings(-OtherStuff.EDITOR_CAMERA_X_SPEED, 0);
		}
		
    	
    	//exit?
    	if( _keyboardInput.keyDown( KeyEvent.VK_ESCAPE) ){
    		System.exit(0);
    	}
		
    	// TOOL PALETTE SWITCHING //
    	//TODO: don't call updateCursor() everywhere :|
    	if( _keyboardInput.keyDown( KeyEvent.VK_1 ) ) {
    		_toolPalette.setToolSelection(ToolPalette.TOOL_BRUSH1x1);
    		updateCursor();
		}
    	if( _keyboardInput.keyDown( KeyEvent.VK_2 ) ) {
    		_toolPalette.setToolSelection(ToolPalette.TOOL_BRUSH2x2);
    		updateCursor();
		}
    	if( _keyboardInput.keyDown( KeyEvent.VK_4 ) ) {
    		_toolPalette.setToolSelection(ToolPalette.TOOL_BRUSH4x4);
    		updateCursor();
		}
    	if( _keyboardInput.keyDown( KeyEvent.VK_X) ) {
    		_toolPalette.setToolSelection(ToolPalette.TOOL_ERASER);
    		updateCursor();
		}
    	if( _keyboardInput.keyDown( KeyEvent.VK_R) ) {
    		_toolPalette.setToolSelection(ToolPalette.TOOL_REPLACER);
    		updateCursor();
		}
    	if( _keyboardInput.keyDown( KeyEvent.VK_F) ) {
    		_toolPalette.setToolSelection(ToolPalette.TOOL_FILL);
    		updateCursor();
		}
    	if( _keyboardInput.keyDown( KeyEvent.VK_P) ) {
    		_toolPalette.setToolSelection(ToolPalette.TOOL_PICKER);
    		updateCursor();
		}
    	if( _keyboardInput.keyDown( KeyEvent.VK_T) ) {
    		_toolPalette.setToolSelection(ToolPalette.TOOL_THING_TOOL);
    		updateCursor();
		}
    	
    	// TILE SET PALETTE SWITCHING/MOVING //
    	if( _keyboardInput.keyDown( KeyEvent.VK_Y) ) {
    		
    		if(_tilesetPalette.canMoveOut()){
    			_tilesetPalette.moveX(-TileSetPalette.TILESET_PALETTE_STEP_SIZE_X);
    			_toolPalette.toggleSuspension(ToolPalette.TOOL_SUSPENDED_SHOW_TILESETPALETTE);
    			updateCursor();
    		}
		}
    	if( _keyboardInput.keyUp( KeyEvent.VK_Y) ) {
    	
    		// TODO: since we update the cursor the whole time it's
    		// moving in, the cursor flickers during the animation
    		if(_tilesetPalette.canMoveIn()){
    			_tilesetPalette.moveX(TileSetPalette.TILESET_PALETTE_STEP_SIZE_X);
    			//_toolPalette.toggleSuspension();
    			//updateCursor();
    		}
    	}
    	//TODO: fix this mess? :( now the cursor is randomly sticking as 
    	if( _keyboardInput.keyUpOnce( KeyEvent.VK_Y) ) {
    		_toolPalette.toggleSuspension(ToolPalette.TOOL_SUSPENDED_SHOW_TILESETPALETTE);	
    		updateCursor();
    	}
    	
    	// THING SET PALETTE SWITCHING/MOVING //
    	if( _keyboardInput.keyDown( KeyEvent.VK_U) ) {
    		
    		if(_thingsetPalette.canMoveOut()){
    			_thingsetPalette.moveX(-ThingSetPalette.THINGSET_PALETTE_STEP_SIZE_X);
    			_toolPalette.toggleSuspension(ToolPalette.TOOL_SUSPENDED_SHOW_THINGSETPALETTE);
    			updateCursor();
    		}
		}
    	if( _keyboardInput.keyUp( KeyEvent.VK_U) ) {
    	
    		if(_thingsetPalette.canMoveIn()){
    			_thingsetPalette.moveX(ThingSetPalette.THINGSET_PALETTE_STEP_SIZE_X);
    		}
    	}
    	//TODO: fix this mess! :( now the cursor is randomly sticking as 
    	if( _keyboardInput.keyUpOnce( KeyEvent.VK_U) ) {
    		_toolPalette.toggleSuspension(ToolPalette.TOOL_SUSPENDED_SHOW_THINGSETPALETTE);	
    		updateCursor();
    	}
    	
    	// LAYER SWITCHING - INCREASE//
    	if( _keyboardInput.keyDown(KeyEvent.VK_0) ){
    		
    		if(_generalKeyTimer.isDone()){
    			_currentLevelLayer = (_currentLevelLayer == (_levelWithLayers.getNumberOfLayers()-1)?0:_currentLevelLayer+1);
    			_generalKeyTimer.reset();
    		}
    		
    	}
    	// LAYER SWITCHING - DECREASE//
    	if( _keyboardInput.keyDown(KeyEvent.VK_9) ){
    		
    		if(_generalKeyTimer.isDone()){
    			_currentLevelLayer = (_currentLevelLayer == 0?(_levelWithLayers.getNumberOfLayers()-1):_currentLevelLayer-1);
    			_generalKeyTimer.reset();
    		}
    		
    	}
    	// CURRENT LAYER VISIBILITY //
    	if( _keyboardInput.keyDown(KeyEvent.VK_V)){
    		
    		if(_generalKeyTimer.isDone()){
    			_levelWithLayers.getLayerAtIndex(_currentLevelLayer).setVisible(!(_levelWithLayers.getLayerAtIndex(_currentLevelLayer).isVisible()));
    			_generalKeyTimer.reset();
    		}
    		
    	}
    	// HIDE/UNHIDE OTHER LAYERS //
    	if( _keyboardInput.keyDown(KeyEvent.VK_H)){
    		
    		if(_generalKeyTimer.isDone()){
    			
    			for(int layerCounter = 0; layerCounter < _levelWithLayers.getNumberOfLayers(); layerCounter++){
    				if(layerCounter != _currentLevelLayer)
    					_levelWithLayers.getLayerAtIndex(layerCounter).setVisible(!_levelWithLayers.getLayerAtIndex(layerCounter).isVisible());
    			}
    			
    			_generalKeyTimer.reset();
    		}
    		
    	}
    	// CREATE NEW LEVEL //
    	if( _keyboardInput.keyDown(KeyEvent.VK_DELETE)){
    		
    		if(_generalKeyTimer.isDone()){
    			//TODO: prompt for "are you sure" here...
    			_levelWithLayers.clearAndMakeNew();
    			_levelWithLayers.moveXY( 0, -((_levelWithLayers.getMapAtIndex(_currentLevelLayer).getRowCount() * OtherStuff.EDITOR_TILE_SIZE) - OtherStuff.EDITOR_SCREEN_H) );
    			_generalKeyTimer.reset();
    		}
    		
    	}
    	///TODO: Show/Hide Map Grid
    	if( _keyboardInput.keyDown(KeyEvent.VK_G)){
    		
    		if(_generalKeyTimer.isDone()){
    			//show/hide grid here
    			_generalKeyTimer.reset();
    		}
    		
    	}
    	
/*
    	///TODO: Dialog tester
    	if( _keyboardInput.keyDownOnce(KeyEvent.VK_M)){
    		
    		if(_generalKeyTimer.isDone()){

    			SimpleDialog d = new SimpleDialog(this, "Ruh Roh", "This is a message.");
    			d.setLocationRelativeTo(this);
    			d.setVisible(true);

    			_generalKeyTimer.reset();
    		}
    		
    	}

    	///TODO: Dialog tester2
    	if( _keyboardInput.keyDownOnce(KeyEvent.VK_N)){
    		
    		if(_generalKeyTimer.isDone()){


    			Object[] options = {"Yes, please",
		                "No, thanks",
		                "No eggs, no ham!"};
				int n = JOptionPane.showOptionDialog(this,
				            "Would you like some green eggs to go "
				            + "with that ham?",
				            "A Silly Question",
				            JOptionPane.YES_NO_CANCEL_OPTION,
				            JOptionPane.QUESTION_MESSAGE,
				            null,
				            options,
				            options[2]);
				if (n == JOptionPane.YES_OPTION) {
					GameSkeleton.printDebugMessage("Here you go: green eggs and ham!");
				} else if (n == JOptionPane.NO_OPTION) {
					GameSkeleton.printDebugMessage("OK, just the ham, then.");
				} else if (n == JOptionPane.CANCEL_OPTION) {
					GameSkeleton.printDebugMessage("Well, I'm certainly not going to eat them!");
				} else {
					GameSkeleton.printDebugMessage("Please tell me what you want!");
				}

    			
    			_generalKeyTimer.reset();
    		}
    		
    	}
    	
    	///TODO: Dialog tester3
    	if( _keyboardInput.keyDownOnce(KeyEvent.VK_B)){
    		
    		if(_generalKeyTimer.isDone()){
    	
    			//String s = TextInputDialog.showTextInputDialog(frame, "Save as...", _levelWithLayers.getFilename());
    			//TextInputDialog t = new TextInputDialog(frame, "Save as...", _levelWithLayers.getFilename());
    			//t.setLocationRelativeTo(frame);
    			//t.setVisible(true);
    			
    			
    			  String s = (String)JOptionPane.showInputDialog(
                          this,
                          "Complete the sentence:\n"
                          + "\"Green eggs and...\"",
                          "Customized Dialog",
                          JOptionPane.PLAIN_MESSAGE,
                          null,
                          null,
                          "ham");

			      //If a string was returned, say so.
			      if ((s != null) && (s.length() > 0)) {
			          GameSkeleton.printDebugMessage("Green eggs and... " + s + "!");
			          return;
			      }
    			
    			_generalKeyTimer.reset();
    		}
    		
    	}
    	*/
    	
	}//process_keyboardInput
    
	/**
	 * Processes mouse events (mainly tool execution)
	 */
    public void processMouseInput(){
    	
    	//TODO: make click and hold only for "brush" things like painting and erasing tiles
    	// for other stuff, like switching tilesets or placing Things, make it one click only
    	
    	// MOUSE BUTTON 1 - Click or Click + Hold //
    	if( /*_mouseInput.buttonDownOnce( MouseEvent.BUTTON1 ) ||*/ _mouseInput.buttonDown( MouseEvent.BUTTON1 )) {
    		
			int x = _mouseInput.getPosition().x;
			int y = _mouseInput.getPosition().y;
    		
    		// if our mouse click is inside the map area (between left and right, and between top and bottom)
    		if(_mouseInput.getPosition().x > _levelWithLayers.getMapAtIndex(_currentLevelLayer).getX() //left
    			&& _mouseInput.getPosition().x < (_levelWithLayers.getMapAtIndex(_currentLevelLayer).getX() + _levelWithLayers.getMapAtIndex(_currentLevelLayer).getColCount()*OtherStuff.EDITOR_TILE_SIZE) //right
    			&& _mouseInput.getPosition().y > _levelWithLayers.getMapAtIndex(_currentLevelLayer).getY() //top
    			&& _mouseInput.getPosition().y < (_levelWithLayers.getMapAtIndex(_currentLevelLayer).getY() + _levelWithLayers.getMapAtIndex(_currentLevelLayer).getRowCount()*OtherStuff.EDITOR_TILE_SIZE) //bottom
    			&& !(_toolPalette.isSuspended())
    		){
    			
    			// calculate and store the tile index and mouse coords that we just clicked
    			int tileClickedRow = (Math.abs(_mouseInput.getPosition().y - _levelWithLayers.getMapAtIndex(_currentLevelLayer).getY())) / OtherStuff.EDITOR_TILE_SIZE;
    			int tileClickedColumn = (Math.abs(_mouseInput.getPosition().x - _levelWithLayers.getMapAtIndex(_currentLevelLayer).getX())) / OtherStuff.EDITOR_TILE_SIZE;
    			//int x = _mouseInput.getPosition().x;
    			//int y = _mouseInput.getPosition().y;
    			
    			// Then paint the tile we clicked with the current tile, or whatever the tool's behaviour should be
    			
        		// TOOL_BRUSH1x1 //
        		if(_toolPalette.getSelectedToolIndex() == ToolPalette.TOOL_BRUSH1x1){

	    			//_levelWithLayers.getMapAtIndex(_currentLevelLayer).getMap().get(tileClickedRow).get(tileClickedColumn).setAnimation(_currentTileAnimation);
        			//_levelWithLayers.getMapAtIndex(_currentLevelLayer).getMap().get(tileClickedRow).get(tileClickedColumn).setAnimation(_currentTile.getAnimation());
        			_levelWithLayers.getMapAtIndex(_currentLevelLayer).replaceTile(tileClickedRow, tileClickedColumn, _currentTile);
    			
        		}//TOOL_BRUSH1x1
        		
        		// TOOL_BRUSH2x2 //
        		else if(_toolPalette.getSelectedToolIndex() == ToolPalette.TOOL_BRUSH2x2){
        			
        			_levelWithLayers.getMapAtIndex(_currentLevelLayer).replaceTile(tileClickedRow, tileClickedColumn, _currentTile);

        			//and paint the other tiles if they are inside the map
        			if((tileClickedRow + 1) < _levelWithLayers.getMapAtIndex(_currentLevelLayer).getRowCount() ){
        				_levelWithLayers.getMapAtIndex(_currentLevelLayer).replaceTile(tileClickedRow + 1, tileClickedColumn, _currentTile);
	    			}
	    			// and the tile below (if inside the map)
	    			if((tileClickedColumn + 1) < _levelWithLayers.getMapAtIndex(_currentLevelLayer).getColCount() ){
	    				_levelWithLayers.getMapAtIndex(_currentLevelLayer).replaceTile(tileClickedRow, tileClickedColumn + 1, _currentTile);
	    			}
	    			// and the tile below and to the right (if inside the map)
	    			//TODO: pretty this up because the logic could be simpler for the final tile (?)
	    			if((tileClickedRow + 1) < _levelWithLayers.getMapAtIndex(_currentLevelLayer).getRowCount() && (tileClickedColumn + 1) < _levelWithLayers.getMapAtIndex(_currentLevelLayer).getColCount() ){
	    				_levelWithLayers.getMapAtIndex(_currentLevelLayer).replaceTile(tileClickedRow + 1, tileClickedColumn + 1, _currentTile);
	    			}
        			
        		}//TOOL_BRUSH2x2
        		
        		// TOOL_ERASER //
        		else if(_toolPalette.getSelectedToolIndex() == ToolPalette.TOOL_ERASER){

        			//_levelWithLayers.getMapAtIndex(_currentLevelLayer).replaceTile(tileClickedRow, tileClickedColumn, _blankTile);
        			_levelWithLayers.getMapAtIndex(_currentLevelLayer).getMap()[tileClickedRow][tileClickedColumn] = null;
        			
        		}//TOOL_ERASER
        		
        		// TOOL_FILL //
        		else if(_toolPalette.getSelectedToolIndex() == ToolPalette.TOOL_FILL){
        			
        			// store the existing animation for our find/replace/fill logic
        			
        			//TODO: experimental null tile check
        			//if()
        			//_previousTile = _levelWithLayers.getMapAtIndex(_currentLevelLayer).getMap().get(tileClickedRow).get(tileClickedColumn);
        			_previousTile = _levelWithLayers.getMapAtIndex(_currentLevelLayer).getMap()[tileClickedRow][tileClickedColumn];
        			fillMap(tileClickedRow,tileClickedColumn);
        			
        		}//TOOL_FILL
        		
        		// TOOL_REPLACER //
        		else if(_toolPalette.getSelectedToolIndex() == ToolPalette.TOOL_REPLACER){
        			
        			// store the existing animation for our find/replace logic
        			//_previousTile = _levelWithLayers.getMapAtIndex(_currentLevelLayer).getMap().get(tileClickedRow).get(tileClickedColumn);
        			_previousTile = _levelWithLayers.getMapAtIndex(_currentLevelLayer).getMap()[tileClickedRow][tileClickedColumn];
        			findReplaceTiles();
        			
        		}//TOOL_REPLACER
        		
        		// TOOL_PICKER //
        		else if(_toolPalette.getSelectedToolIndex() == ToolPalette.TOOL_PICKER){
        			
        			// TODO: does it matter that the tile could be not in our tile palette? 
        			// Hmm...maybe that's actually a good feature to have when multiple
        			// tile sets are in the palette
        			
        			// set our current tile to be the one that our map has in the clicked location
        			//_currentTile = _levelWithLayers.getMapAtIndex(_currentLevelLayer).getMap().get(tileClickedRow).get(tileClickedColumn);
        			
        			//check for null tile selected
        			if(_levelWithLayers.getMapAtIndex(_currentLevelLayer).getMap()[tileClickedRow][tileClickedColumn] != null){
        				_currentTile = _levelWithLayers.getMapAtIndex(_currentLevelLayer).getMap()[tileClickedRow][tileClickedColumn];
        				_toolPalette.switchBackToPreviousTool(); //TODO: BLAH
        				updateCursor();
        			}
        			
        		}//TOOL_REPLACER
        		
        		
        		/*
        		
        		// TOOL_THING_TOOL //
        		else if(_toolPalette.getSelectedToolIndex() == ToolPalette.TOOL_THING_TOOL){

        			//TODO: get this to make a unique copy for each
        			//add an instance of the current thing to our editor things
        			//_levelWithLayers.getThingsAtIndex(_currentLevelLayer).add(_thingPalette.getCurrentPaletteItem());
        			addNewThing(x, y);
        			
        			
        		}//TOOL_THING_TOOL
        		
    			*/
    		
    		}//ifWithinRangeOfMap
    		
    		// Else, we clicked outside the map (but that is ok for tool/tile/thing selection, etc.
    		else{
    			
    			/*
        		// TOOL SUSPENDED SHOW TILESETPALETTE //
        		if(_toolPalette.getSelectedToolIndex() == ToolPalette.TOOL_SUSPENDED_SHOW_TILESETPALETTE){
        			
        			// if we clicked our left or right arrows on the tilesetPalette
        			if(_tilesetPalette.clickedPreviousTileset(x,y)){
        				_tilesetPalette.prevTileset();
        			}else if(_tilesetPalette.clickedNextTileset(x,y)){
        				_tilesetPalette.nextTileset();
        			}else{
        			
	        			// store currentTile into previousTile
	        			_previousTile = _currentTile;
	        			//get the clicked one from the tileset
	        			_currentTile = _tilesetPalette.getTileFromCurrentTileSet(x, y);
	        			//and check if it's null. if it is, revert by putting current back to previous
	        			//note: it can be null if there was an error getting it from the tileset
	        			if(_currentTile == null){
	        				_currentTile = _previousTile;
	        			}
	        			
        			}//else
        			
        		}//TOOL_SUSPENDED_SHOW_TILESETPALETTE

        		// TOOL SUSPENDED SHOW THINGSETPALETTE //
        		if(_toolPalette.getSelectedToolIndex() == ToolPalette.TOOL_SUSPENDED_SHOW_THINGSETPALETTE){
        			
        			//GameSkeleton.printDebugMessage("MapEditor: Thing Palette Active and Clicked");
        			
        			// if we clicked our left or right arrows on the tilesetPalette
        			if(_thingsetPalette.clickedPreviousThingset(x,y)){
        				_thingsetPalette.prevThingset();
        			}else if(_thingsetPalette.clickedNextThingset(x,y)){
        				_thingsetPalette.nextThingset();
        			}else{
        			
	        			// store currentThing into previousThing
	        			_previousThing = _currentThing;
        				
	        			//get the clicked one from the thingset
	        			_currentThing = _thingsetPalette.getThingFromCurrentThingSet(x, y);
	        			//and check if it's null. if it is, revert by putting current back to previous
	        			//note: it can be null if there was an error getting it from the thingset
	        			if(_currentThing == null){
	        				_currentThing = _previousThing;
	        				GameSkeleton.printDebugMessage("MapEditor: Thing is null");
	        			}
	        			
	        			else
	        				GameSkeleton.printDebugMessage("MapEditor: Thing clicked is " + _currentThing.getClass());
	        			
        			}//else
        			
        		}//TOOL_SUSPENDED_SHOW_THINGSETPALETTE

    			 */

    		}//else
  	
    	}//MOUSE_1_CLICK_AND_HOLD
    	
    	// Mouse 1 - click once only
    	if( _mouseInput.buttonDownOnce( MouseEvent.BUTTON1 ) ) {
    		
    		int x = _mouseInput.getPosition().x;
			int y = _mouseInput.getPosition().y;
    		
    		// if our mouse click is inside the map area (between left and right, and between top and bottom)
    		if(_mouseInput.getPosition().x > _levelWithLayers.getMapAtIndex(_currentLevelLayer).getX() //left
    			&& _mouseInput.getPosition().x < (_levelWithLayers.getMapAtIndex(_currentLevelLayer).getX() + _levelWithLayers.getMapAtIndex(_currentLevelLayer).getColCount()*OtherStuff.EDITOR_TILE_SIZE) //right
    			&& _mouseInput.getPosition().y > _levelWithLayers.getMapAtIndex(_currentLevelLayer).getY() //top
    			&& _mouseInput.getPosition().y < (_levelWithLayers.getMapAtIndex(_currentLevelLayer).getY() + _levelWithLayers.getMapAtIndex(_currentLevelLayer).getRowCount()*OtherStuff.EDITOR_TILE_SIZE) //bottom
    			&& !(_toolPalette.isSuspended())
    		){
    			
    			// calculate and store the tile index and mouse coords that we just clicked
    			int tileClickedRow = (Math.abs(_mouseInput.getPosition().y - _levelWithLayers.getMapAtIndex(_currentLevelLayer).getY())) / OtherStuff.EDITOR_TILE_SIZE;
    			int tileClickedColumn = (Math.abs(_mouseInput.getPosition().x - _levelWithLayers.getMapAtIndex(_currentLevelLayer).getX())) / OtherStuff.EDITOR_TILE_SIZE;
    			//int x = _mouseInput.getPosition().x;
    			//int y = _mouseInput.getPosition().y;
    		
    			// TOOL_THING_TOOL //
        		if(_toolPalette.getSelectedToolIndex() == ToolPalette.TOOL_THING_TOOL){

        			//TODO: get this to make a unique copy for each
        			//add an instance of the current thing to our editor things
        			//_levelWithLayers.getThingsAtIndex(_currentLevelLayer).add(_thingPalette.getCurrentPaletteItem());
        			addNewThing(x, y);
        			
        			/*
        			_levelWithLayers.getThingsAtIndex(_currentLevelLayer).add(new Projectile(0,0,0,0,0,0));
        			//_levelWithLayers.getThingsAtIndex(_currentLevelLayer).setElementAt(_thingPalette.getCurrentPaletteItem(), _levelWithLayers.getThingsAtIndex(_currentLevelLayer).size()-1);
        			
        			_levelWithLayers.getThingsAtIndex(_currentLevelLayer).get(_levelWithLayers.getThingsAtIndex(_currentLevelLayer).size()-1).setX(x);
        			_levelWithLayers.getThingsAtIndex(_currentLevelLayer).get(_levelWithLayers.getThingsAtIndex(_currentLevelLayer).size()-1).setY(y);
        			*/
        			
        		}//TOOL_THING_TOOL
        		
    		}else{
    			
    			// TOOL SUSPENDED SHOW TILESETPALETTE //
        		if(_toolPalette.getSelectedToolIndex() == ToolPalette.TOOL_SUSPENDED_SHOW_TILESETPALETTE){
        			
        			// if we clicked our left or right arrows on the tilesetPalette
        			if(_tilesetPalette.clickedPreviousTileset(x,y)){
        				_tilesetPalette.prevTileset();
        			}else if(_tilesetPalette.clickedNextTileset(x,y)){
        				_tilesetPalette.nextTileset();
        			}else{
        			
	        			// store currentTile into previousTile
	        			_previousTile = _currentTile;
	        			//get the clicked one from the tileset
	        			_currentTile = _tilesetPalette.getTileFromCurrentTileSet(x, y);
	        			//and check if it's null. if it is, revert by putting current back to previous
	        			//note: it can be null if there was an error getting it from the tileset
	        			if(_currentTile == null){
	        				_currentTile = _previousTile;
	        			}
	        			
        			}//else
        			
        		}//TOOL_SUSPENDED_SHOW_TILESETPALETTE

        		// TOOL SUSPENDED SHOW THINGSETPALETTE //
        		if(_toolPalette.getSelectedToolIndex() == ToolPalette.TOOL_SUSPENDED_SHOW_THINGSETPALETTE){
        			
        			//GameSkeleton.printDebugMessage("MapEditor: Thing Palette Active and Clicked");
        			
        			// if we clicked our left or right arrows on the tilesetPalette
        			if(_thingsetPalette.clickedPreviousThingset(x,y)){
        				_thingsetPalette.prevThingset();
        			}else if(_thingsetPalette.clickedNextThingset(x,y)){
        				_thingsetPalette.nextThingset();
        			}else{
        			
	        			// store currentThing into previousThing
	        			_previousThing = _currentThing;
        				
	        			//get the clicked one from the thingset
	        			_currentThing = _thingsetPalette.getThingFromCurrentThingSet(x, y);

	        			//and check if it's null. if it is, revert by putting current back to previous
	        			//note: it can be null if there was an error getting it from the thingset
	        			if(_currentThing == null){
	        				_currentThing = _previousThing;
	        				//GameSkeleton.printDebugMessage("MapEditor: Thing is null");
	        			}
	        			
	        			//else
	        			//	GameSkeleton.printDebugMessage("MapEditor: Thing clicked is " + _currentThing.getClass());
	        			
        			}//else
        			
        		}//TOOL_SUSPENDED_SHOW_THINGSETPALETTE
    			
    		}//elseOutOfRangeOfMap
    		
    	}//MOUSE_IF_CLICK_ONCE
    	
    	if( _mouseInput.buttonDown( MouseEvent.BUTTON1 ) ) {
    		//GameSkeleton.printDebugMessage("MOUSE 1 still down");
    	}
    	if( _mouseInput.buttonDownOnce( MouseEvent.BUTTON3 ) ) {
    		
    		int x = _mouseInput.getPosition().x;
			int y = _mouseInput.getPosition().y;
    		
    		// if our mouse click is inside the map area (between left and right, and between top and bottom)
    		if(_mouseInput.getPosition().x > _levelWithLayers.getMapAtIndex(_currentLevelLayer).getX() //left
    			&& _mouseInput.getPosition().x < (_levelWithLayers.getMapAtIndex(_currentLevelLayer).getX() + _levelWithLayers.getMapAtIndex(_currentLevelLayer).getColCount()*OtherStuff.EDITOR_TILE_SIZE) //right
    			&& _mouseInput.getPosition().y > _levelWithLayers.getMapAtIndex(_currentLevelLayer).getY() //top
    			&& _mouseInput.getPosition().y < (_levelWithLayers.getMapAtIndex(_currentLevelLayer).getY() + _levelWithLayers.getMapAtIndex(_currentLevelLayer).getRowCount()*OtherStuff.EDITOR_TILE_SIZE) //bottom
    			&& !(_toolPalette.isSuspended())
    		){
    		
    			// TOOL_ERASER //
        		if(_toolPalette.getSelectedToolIndex() == ToolPalette.TOOL_ERASER){

        			_levelWithLayers.eraseThingAtCoords(x,y, _currentLevelLayer, OtherStuff.THING_ERASER_COORD_RANGE);
        			
        		}//TOOL_ERASER

    		}//ifInMap
    			
    	}//IF_MOUSE_3_DOWN_ONCE
    	
    }//processMouseInput

    /*
     * This function replaces all instances of the tile we clicked with the 
     * current tile we have selected on the tile palette
     * @param tileClickedRow - the row we clicked
     * @param tileClickedColumn - the column we clicked
     */
    private void findReplaceTiles() {
		    	
		for(int row = 0; row < _levelWithLayers.getMapAtIndex(_currentLevelLayer).getRowCount(); row++){
			
			for(int col = 0; col < _levelWithLayers.getMapAtIndex(_currentLevelLayer).getColCount(); col++){

				//TODO: experimental null check
				
				// If the tile we clicked (and store into previousTile before calling the function) was 
				// null, then replace the tile
				if(_previousTile == null){
					_levelWithLayers.getMapAtIndex(_currentLevelLayer).replaceTile(row, col, _currentTile);
				}
				
				// If the tile is NOT null and _previousTile is the same as _currentTile, replace it
				else if(_levelWithLayers.getMapAtIndex(_currentLevelLayer).getMap()[row][col] != null && _levelWithLayers.getMapAtIndex(_currentLevelLayer).getMap()[row][col].equals(_previousTile)){
					_levelWithLayers.getMapAtIndex(_currentLevelLayer).replaceTile(row, col, _currentTile);
				}
				
			}
			
		}//forMapTiles
    	
	}//findReplaceTiles

	/*
     * This function fills our map "paint bucket style" based on where we clicked
     * and the textures in the tiles of our map.
     */
    private void fillMap(int tileClickedRow, int tileClickedColumn) {
    	
    	//TODO: fill doesnt work on large maps, as recursive call creates stack overflow.
    	
    	/*
    	 * 
    	 * BEFORE calling this when the mouse is clicked, remember the source 
    	 * texture as the texture of the tile we clicked
    	 * 
    	 * ---
    	 * 
    	 * change the texture of the current tile to our destination texture 
    	 * 
    	 * if the tile to my right is the same as the source texture
    	 *    make a recursive call to fillMap at the new x/y
    	 *    
    	 * if the tile to my left is the same as the source texture
    	 *    make a recursive call to fillMap at the new x/y
    	 *    
    	 * if the tile to my top is the same as the source texture
    	 *    make a recursive call to fillMap at the new x/y
    	 *    
    	 * if the tile to my bottom is the same as the source texture
    	 *    make a recursive call to fillMap at the new x/y
    	 *    
    	 */

    	// slight optimization, if the the existing and replacement are the
    	// same tile, then just return and save a lot of recursive "FALSE" 
    	// evaluations
    	//TODO: experimental null tile
    	//if(_levelWithLayers.getMapAtIndex(_currentLevelLayer).getMap().get(tileClickedRow).get(tileClickedColumn) != null && (tileClickedRow >= 0 && tileClickedRow < _levelWithLayers.getMapAtIndex(_currentLevelLayer).getRows() && tileClickedColumn >= 0 && tileClickedColumn < _levelWithLayers.getMapAtIndex(_currentLevelLayer).getCols()) && _levelWithLayers.getMapAtIndex(_currentLevelLayer).getMap().get(tileClickedRow).get(tileClickedColumn).equals(_currentTile)){
    	if((_levelWithLayers.getMapAtIndex(_currentLevelLayer).getMap()[tileClickedRow][tileClickedColumn] != null) && (tileClickedRow >= 0 && tileClickedRow < _levelWithLayers.getMapAtIndex(_currentLevelLayer).getRowCount() && tileClickedColumn >= 0 && tileClickedColumn < _levelWithLayers.getMapAtIndex(_currentLevelLayer).getColCount()) && _levelWithLayers.getMapAtIndex(_currentLevelLayer).getMap()[tileClickedRow][tileClickedColumn].equals(_currentTile)){    		
    		return;
    	}
    	
		// change THIS tile to the new tile (_currentTile)
    	_levelWithLayers.getMapAtIndex(_currentLevelLayer).replaceTile(tileClickedRow, tileClickedColumn, _currentTile);
		
		// now check the tiles around the one we clicked and fill them recursively
		
		//right, if the tile to the right is in bounds/exists
		if((tileClickedColumn + 1) < _levelWithLayers.getMapAtIndex(_currentLevelLayer).getColCount()){
			
			//TODO: experimental null check
			//if(_levelWithLayers.getMapAtIndex(_currentLevelLayer).getMap().get(tileClickedRow).get(tileClickedColumn + 1) != null && _levelWithLayers.getMapAtIndex(_currentLevelLayer).getMap().get(tileClickedRow).get(tileClickedColumn + 1).equals(_previousTile)){
			if((_levelWithLayers.getMapAtIndex(_currentLevelLayer).getMap()[tileClickedRow][tileClickedColumn + 1] == null) || _levelWithLayers.getMapAtIndex(_currentLevelLayer).getMap()[tileClickedRow][tileClickedColumn + 1].equals(_previousTile)){
				fillMap(tileClickedRow, tileClickedColumn + 1);
			}
			
		}//if

		//left, if the tile to the left is in bounds/exists
		if((tileClickedColumn - 1) >= 0){
			
			//TODO: experimental null check
			//if(_levelWithLayers.getMapAtIndex(_currentLevelLayer).getMap().get(tileClickedRow).get(tileClickedColumn - 1) != null && _levelWithLayers.getMapAtIndex(_currentLevelLayer).getMap().get(tileClickedRow).get(tileClickedColumn - 1).equals(_previousTile)){
			if((_levelWithLayers.getMapAtIndex(_currentLevelLayer).getMap()[tileClickedRow][tileClickedColumn - 1] == null) || _levelWithLayers.getMapAtIndex(_currentLevelLayer).getMap()[tileClickedRow][tileClickedColumn - 1].equals(_previousTile)){
				fillMap(tileClickedRow, tileClickedColumn - 1);
			}
			
		}//if

		//top, if the tile to the top is in bounds/exists
		if((tileClickedRow - 1) >= 0){
			
			//TODO: experimental null check
			//if(_levelWithLayers.getMapAtIndex(_currentLevelLayer).getMap().get(tileClickedRow - 1).get(tileClickedColumn) != null && _levelWithLayers.getMapAtIndex(_currentLevelLayer).getMap().get(tileClickedRow - 1).get(tileClickedColumn).equals(_previousTile)){
			if((_levelWithLayers.getMapAtIndex(_currentLevelLayer).getMap()[tileClickedRow - 1][tileClickedColumn] == null) || _levelWithLayers.getMapAtIndex(_currentLevelLayer).getMap()[tileClickedRow - 1][tileClickedColumn].equals(_previousTile)){
				fillMap(tileClickedRow - 1 , tileClickedColumn);
			}
			
		}//if
		
		//bottom, if the tile to the bottom is in bounds/exists
		if((tileClickedRow + 1) < _levelWithLayers.getMapAtIndex(_currentLevelLayer).getRowCount()){
			
			//TODO: experimental null check
			//if(_levelWithLayers.getMapAtIndex(_currentLevelLayer).getMap().get(tileClickedRow + 1).get(tileClickedColumn)!= null && _levelWithLayers.getMapAtIndex(_currentLevelLayer).getMap().get(tileClickedRow + 1).get(tileClickedColumn).equals(_previousTile)){
			if((_levelWithLayers.getMapAtIndex(_currentLevelLayer).getMap()[tileClickedRow + 1][tileClickedColumn] == null) || _levelWithLayers.getMapAtIndex(_currentLevelLayer).getMap()[tileClickedRow + 1][tileClickedColumn].equals(_previousTile)){
				fillMap(tileClickedRow + 1 , tileClickedColumn);
			}
			
		}//if

	}//fillMap

	/*
     * Changes our cursor to something other than the default
     */
    private void updateCursor() {
    	
    	setCursor(_toolPalette.getCurrentCursor());
    	
    }//changeCursor
    
    /**
     * Keeps our map alive and ticking by drawing 
     * appropriate animation frames, etc.
     */
	protected void updateAnimations(long elapsedTime){
		
		//for all layers
		for(int layerCounter = 0; layerCounter < _levelWithLayers.getNumberOfLayers(); layerCounter++){
		
			//update our map tile animations
			for(int row = 0; row < _levelWithLayers.getMapAtIndex(_currentLevelLayer).getRowCount(); row++){
				for(int column = 0; column < _levelWithLayers.getMapAtIndex(_currentLevelLayer).getColCount(); column++){
					
					//TODO: null tile check
					if(_levelWithLayers.getMapAtIndex(_currentLevelLayer).getMap()[row][column] != null)
						_levelWithLayers.getMapAtIndex(_currentLevelLayer).getMap()[row][column].updateAnimation(elapsedTime);
					
				}
			}
			
			//update our world things
			for(int i = 0; i < _levelWithLayers.getThingsAtIndex(_currentLevelLayer).length; i++){
				_levelWithLayers.getThingsAtIndex(_currentLevelLayer)[i].updateAnimations(elapsedTime);
			}
			
		}//forLayers
		
	}//updateAnimations

    /**
     * This function moves all the Things by X and Y as passed
     * @param xDelta - the difference in x that is desired by the move
     * @param yDelta - the difference in y that is desired by the move
     */
	public void moveMapAndThings(int xDelta, int yDelta) {
		
		//for all layers, move map and things
		for(int layerCounter = 0; layerCounter < _levelWithLayers.getNumberOfLayers(); layerCounter++){
		
			_levelWithLayers.getMapAtIndex(layerCounter).moveXY(xDelta, yDelta);
			
			for(int i = 0; i < _levelWithLayers.getThingsAtIndex(layerCounter).length; i++){
				_levelWithLayers.getThingsAtIndex(layerCounter)[i].setX(_levelWithLayers.getThingsAtIndex(layerCounter)[i].getX() + xDelta);
				_levelWithLayers.getThingsAtIndex(layerCounter)[i].setY(_levelWithLayers.getThingsAtIndex(layerCounter)[i].getY() + yDelta);
			}
			
		}//forLayers
		
		
		//_levelWithLayers.moveXY(xDelta, yDelta);
    	
	}//moveThings
	
	/**
	 * This is a really lazy/bad way for me to add things to my editor's current level. It's
	 * a separate function because it's really messy and I don't want it tangled in my tool
	 * event handler code.
	 * This function basically adds a new instance of the current thing selected in our 
	 * ThingPalette to our editor's _things vector.
	 * TODO: find a better way to do this
	 * @param x - the x coord of the new thing
	 * @param y - the y coord of the new thing
	 */
	public void addNewThing( int x,	int y) {
		
		//TODO: testing  new ThingManager function
		_levelWithLayers.getLayerAtIndex(_currentLevelLayer).addNewThing((Thing)ThingManager.createThingWithID(_currentThing.getID(), x, y));
	
	}//addNewThing

	/**
	 * This function saves our map to file
	 */
	public void saveLevel(){
		
		if(_levelWithLayers.writeLevelWithLayers()){
			GameSkeleton.printDebugMessage("Level Saved.");
		}else{
			GameSkeleton.printDebugMessage("Ruh Roh! Something went wrong when saving the level!");
		}
		
	}//saveLevel
	
	/**
	 * This function prompts user for map file name to save as, populating the
	 * current name by default
	 */
	public void saveLevelAs(){
		
		  String s = (String)JOptionPane.showInputDialog(
                  this,
                  "Save Level As:\n",
                  "Save Level As...",
                  JOptionPane.PLAIN_MESSAGE,
                  null,
                  null,
                  _levelWithLayers.getFilename());

		  //If a string was returned, say so.
		  if ((s != null) && (s.length() > 0)) {
			  
			  GameSkeleton.printDebugMessage("MapEditor: Changing level name to: " + s + "!");
			  _levelWithLayers.changeFileNameTo(s);
			  
			  if(_levelWithLayers.writeLevelWithLayers()){
				  GameSkeleton.printDebugMessage("Level Saved.");
			  }else{
				  GameSkeleton.printDebugMessage("Ruh Roh! Something went wrong when saving the level!");
			  }

			  return;
			  
		  }//if

	}//saveLevelAs
	
	/**
	 * This function loads our map to file
	 */
	public void loadLevel(){
		
		/*
		String[] choices = { "A", "B", "C", "D", "E", "F" };
		String levelChoice = (String)JOptionPane.showInputDialog(
				this,
				"Open Level...",
				"Open which level?",
				JOptionPane.QUESTION_MESSAGE,
				null,
				choices, 		// Array of choices
				choices[0]); 	// Initial choice

		if ((levelChoice != null) && (levelChoice.length() > 0)) {
			GameSkeleton.printDebugMessage(levelChoice);	
		}
		*/
		

		JFileChooser fileopen = new JFileChooser();
		FileFilter filter = new FileNameExtensionFilter("level xml files", "xml");
		fileopen.addChoosableFileFilter(filter);
		
		/*
		GameSkeleton.printDebugMessage("MapEditor: current dir is:'"+fileopen.getCurrentDirectory()+"'");
		String userDir = System.getProperty("user.dir");
		GameSkeleton.printDebugMessage("MapEditor: app was started in:'" + userDir + "'");
		*/

		File appOpenedInDir = new File(System.getProperty("user.dir"));
		fileopen.setCurrentDirectory(appOpenedInDir);

		try{
		
			int fileOpenReturnValue = fileopen.showDialog(this,"Open level for editing");
			
			switch(fileOpenReturnValue){
			
				case JFileChooser.APPROVE_OPTION:
					File file = fileopen.getSelectedFile();
					_levelWithLayers = new Level(file.getName());
					if(_levelWithLayers.loadLevelWithLayers(file.getName())){
    					_levelWithLayers.moveXY( 0, -((_levelWithLayers.getMapAtIndex(_currentLevelLayer).getRowCount() * OtherStuff.EDITOR_TILE_SIZE) - OtherStuff.EDITOR_SCREEN_H) );
						//GameSkeleton.printDebugMessage("Level Loaded.");
					}else{
						GameSkeleton.printDebugMessage("Ruh Roh! Something went wrong when loading the level!");
					}
				case JFileChooser.CANCEL_OPTION:
					//GameSkeleton.printDebugMessage("Cancel or the close-dialog icon was clicked");
					break;
				case JFileChooser.ERROR_OPTION:
					GameSkeleton.printDebugMessage("MapEditor - Error: Got an Error condition from the file open dialog...");
					break;
			
			}//switch
			
		}catch(Exception e){
			GameSkeleton.printDebugMessage("MapEditor:loadLevel() - Error: " + e.getMessage());
		}

		
		/*
		// "reset" our level to prevent ghosting if the level is saved then loaded repeatedly
		_levelWithLayers = new Level(_levelWithLayers.getFilename());
		
		if(_levelWithLayers.loadLevelWithLayers(_levelWithLayers.getFilename())){
			GameSkeleton.printDebugMessage("Level Loaded.");
		}else{
			GameSkeleton.printDebugMessage("Ruh Roh! Something went wrong when loading the level!");
		}
		*/
		
	}//loadLevel	
	
	/**
	 * 
	 */
	public void showOptions(){

		_editorOptionsDialog.refreshLevelOptionValues(_levelWithLayers);
		_editorOptionsDialog.setVisible(true);
		
	}//showOptions
	
	/**
	 * A simple action listener function to handle our options dialog
	 */
	public class MapEditorActionListener implements ActionListener{

		// some command constants
		public static final String MAP_EDITOR_ACTION_CANCEL_OPTIONS = "Cancel";
		public static final String MAP_EDITOR_ACTION_APPLY_OPTIONS = "Apply";
		
		@Override
		public void actionPerformed(ActionEvent arg0) {

			GameSkeleton.printDebugMessage("event fired: " + arg0.getActionCommand());
			String actionPerformed = arg0.getActionCommand();
			
			
			if(actionPerformed == MAP_EDITOR_ACTION_APPLY_OPTIONS){
				
				GameSkeleton.printDebugMessage(arg0.getSource().toString());
				_levelWithLayers.setLevelName(_editorOptionsDialog.getLevelNameValue());
				
				//TODO: update this for "forever", "once", and "false". For not just do false and forever
				if(_editorOptionsDialog.getLevelLoopValue())
					_levelWithLayers.setLoopState(Level.LEVEL_LOOP_FOREVER);
				else{
					_levelWithLayers.setLoopState(Level.LEVEL_LOOP_FALSE);
				}
			
				_editorOptionsDialog.setVisible(false);
				
			}else if(actionPerformed == MAP_EDITOR_ACTION_CANCEL_OPTIONS){
				
				GameSkeleton.printDebugMessage(arg0.getSource().toString());
				
				_editorOptionsDialog.setVisible(false);

			
			}else{
			
				GameSkeleton.printDebugMessage("MapEditorActionListener:: Unknown Action '"+actionPerformed+"'");
			
			}//else
				
			

		}//actionPerformed

		
		
	}//MapEditorActionListener
	
	
	
	
	
	
}//MapEditor