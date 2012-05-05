package com.rjw.gameskeleton;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.io.File;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.rjw.gameskeleton.OtherStuff;
import com.rjw.sfx.SoundManagerSingleton;

/**
 * 
 * @author rwalsh
 *
 */
public class GameSkeleton extends JPanel {

	// Keyboard Input
	KeyboardInput keyboardInput = new KeyboardInput(); 
	
	// Screen image and buffer
	private BufferedImage _fOffscreenImage;
	private Graphics _fOffscreenGraphics;

	//Game timing
	private long _startTime;
    private long _currTime;
    private long _elapsedTime;
    // frame rate stuff
    private Timer _frameRateLimiter;
    private static final int FRAMES_PER_SECOND = 30;
    private int frameNumber = 0;
    
	// Our avatar
	private Avatar _avatar;
	// And related HUD sprites
	private Sprite _healthIcon;
	private Sprite _livesIcon;
	
	//our Level and layer management
	private int _currentLevelLayer; 
	
    // our sequence of levels
    private int _currentLevelIndex;
    private Level[] _gameLevels;
    private Level _currentLevel;
   	
	// menu stuff
	private boolean _menuActive;
	private Timer _generalKeyTimer;	//to slow key rates for things like menu selection/execution
	private MainGameMenu _mainMenu;
	
	// font based image test
	private GameFont _gameFont;
	
	// game screens
	private GameScreen _mainGameScreen; 
	
	// TODO: test particle emitter
	private ParticleEmitter _emitterTest;
	
	// HUD
	Sprite _hudBackground;
	
	// flag to draw debug data
	private boolean _drawDebugData;

	// Constants //
	public static final String MUSIC_LEVEL_COMPLETE = "sound_music_level_complete.wav";
	
    /**
     * This is our constructor where all the game items get initialized (screen, levels, sounds, etc.)
     */
	public GameSkeleton() {

		//init our key listener, focus, and graphics
		setFocusable(true);
		addKeyListener(keyboardInput);
		_fOffscreenImage = new BufferedImage(OtherStuff.SCREEN_WIDTH, OtherStuff.SCREEN_HEIGHT, 1);
		_fOffscreenGraphics = _fOffscreenImage.getGraphics();

		// load the level sequence
		loadLevelSequence(OtherStuff.GAME_LEVEL_SEQUENCE_FILE);
		
		_currentLevelIndex = 0;
		
		// assign current Level to the first one in the sequence
		_currentLevel = _gameLevels[_currentLevelIndex];
		
		//load level from file
		_currentLevelLayer = OtherStuff.LAYER_ACTION;
		
		// move level to the right starting position (bottom of level flush with bottom of screen)
		_currentLevel.moveXY( 0, -((_currentLevel.getMapAtIndex(_currentLevelLayer).getRowCount() * OtherStuff.EDITOR_TILE_SIZE) - (OtherStuff.SCREEN_HEIGHT)) );
		
		// init our avatar
		_avatar = new Avatar(OtherStuff.AVATAR_START_X,OtherStuff.AVATAR_START_Y,OtherStuff.AVATAR_Y_SPEED,OtherStuff.AVATAR_X_SPEED, 9, 100);
		// and other HUD sprites
		_healthIcon = new Sprite(OtherStuff.SPRITE_HEALTH_ICON);
		_livesIcon = new Sprite(OtherStuff.SPRITE_LIVES_ICON);
		
		// init our frame rate limiter
		_frameRateLimiter = new Timer(OtherStuff.FRAME_RATE_TIMER_IN_MS);
		
		// and  sounds
		initSounds();
		
		// Menu stuff - start with menu active so the game doesnt start until player "says go"
		_menuActive = true;
		_generalKeyTimer = new Timer(OtherStuff.GAME_KEY_TIMEOUT);
		//TODO: use menu dimensions
		_mainMenu = new MainGameMenu(0,0);
		
		_gameFont = new GameFont();

		// GAME INTRO SCREEN //
		_mainGameScreen = new GameScreen();
		BufferedImage frameImage;
		
		// HUD //
		_hudBackground = new Sprite(OtherStuff.SPRITE_HUD_BACKGROUND);
		
		//create the animation
		try{	
			frameImage = ImageIO.read(new File(OtherStuff.SPRITE_PATH_PREFIX + "introScreen2.png"));
			
			// Add frames for our fly straight animation
			_mainGameScreen.getMainAnimatedSprite().getAnimation().addFrame(frameImage.getSubimage(0, 0, 640, 480), 600);
			_mainGameScreen.getMainAnimatedSprite().getAnimation().addFrame(frameImage.getSubimage(640, 0, 640, 480), 600);
			_mainGameScreen.getMainAnimatedSprite().getAnimation().addFrame(frameImage.getSubimage(1280, 0, 640, 480), 600);
			_mainGameScreen.getMainAnimatedSprite().getAnimation().addFrame(frameImage.getSubimage(1920, 0, 640, 480), 600);
			_mainGameScreen.getMainAnimatedSprite().getAnimation().addFrame(frameImage.getSubimage(2560, 0, 640, 480), 600);
			_mainGameScreen.getMainAnimatedSprite().getAnimation().addFrame(frameImage.getSubimage(3200, 0, 640, 480), 600);
			_mainGameScreen.getMainAnimatedSprite().getAnimation().addFrame(frameImage.getSubimage(3840, 0, 640, 480), 600);
			_mainGameScreen.getMainAnimatedSprite().getAnimation().addFrame(frameImage.getSubimage(4480, 0, 640, 480), 600);
			_mainGameScreen.getMainAnimatedSprite().getAnimation().addFrame(frameImage.getSubimage(5120, 0, 640, 480), 600);
			
			_mainGameScreen.getMainAnimatedSprite().getAnimation().setLoopCount(1);
			
		}catch(Exception e){
			GameSkeleton.printDebugMessage("Error loading frames for Game Intro Screen ("+e.getMessage()+")");
		}

		//_mainGameScreen.loadSound(OtherStuff.SFX_PATH_PREFIX + "sound_music_level_complete.wav");
		//_mainGameScreen.playSound();
		
		// END INTRO SCREEN //
		
		//TODO: test emitter
		_emitterTest = new ParticleEmitter(50, 100, 100, 100);
		
		_drawDebugData = true;
		
	}//Constructor

	/**
	 * This function does exactly what you think - loads the GameSkeleton's sounds
	 */
	private void initSounds(){
       
	}//initSounds
	
	/**
	 * The main game "clockwork"
	 */
	public void run() {
		
		// get our current "wall time"
		_startTime = System.currentTimeMillis();
	    _currTime = _startTime;

		// show our intro screen
		drawScreen(_mainGameScreen);
	    
	    // main game loop
		while( true ) {
			
			try {
			
				// Poll the keyboardInput
				keyboardInput.poll();
				
				// Should we exit?
				//if( keyboardInput.keyDownOnce( KeyEvent.VK_ESCAPE ) )
				//	break;
				
				// Menu Active
				if(_menuActive){
					
					processMenuInput();
					
				}else{
				
					///////////////////
					//process our level
					
					// update collision and animations to do all layers? or just for collisions
					// within layers? For example, we don't want the avatar colliding with a vehicle
					// on the ground if the avatar is flying at altitude
					
					// check for collisions
					//TODO: this is a bit hacky...needs to work with all layers, perhaps not just action layer
					//Thing.collisionDetection(_avatar, _levelWithLayers.getThingsAtIndex(_currentLevelLayer));
					Thing.collisionDetection(_avatar, _currentLevel.getThingsAtIndex(_currentLevelLayer));
					
					// Update sprite animations
					updateAnimations(_elapsedTime);
					
					
					
					//////////////////////////////////////////////////
					//////////////////////////////////////////////////
					//////////////////////////////////////////////////
					
					//TODO: optimize this
					
					// FOR NON-LOOPING LEVELS //
					if(_gameLevels[_currentLevelIndex].getLoopState() == Level.LEVEL_LOOP_FALSE){
						
						//GameSkeleton.printDebugMessage("level is NOT looping: "+_gameLevels[_currentLevelIndex].getLoopState()+" != "+Level.LEVEL_LOOP_FALSE+"");

						
						//For all layers, update our locations of backgrounds, map, etc.
						for(int layerCounter = 0; layerCounter < _currentLevel.getNumberOfLayers(); layerCounter++){
						
							//_levelWithLayers.getMapAtIndex(layerCounter).updatePosition(0, OtherStuff.CAMERA_Y_SPEED);
							_currentLevel.getMapAtIndex(layerCounter).updatePosition(0, OtherStuff.CAMERA_Y_SPEED);
							
							// for all our things in this layer, update their position as well.
							for(int thingIndex = 0; thingIndex < _currentLevel.getThingsAtIndex(layerCounter).length; thingIndex++){
								
								// if the thing is not the first thing in our first action layer things (i.e. our AVATAR), update it
								//if(layerCounter != OtherStuff.LAYER_ACTION && thingIndex != 0)
								//_levelWithLayers.getThingsAtIndex(layerCounter)[thingIndex].updatePosition(0, OtherStuff.CAMERA_Y_SPEED);
								_currentLevel.getThingsAtIndex(layerCounter)[thingIndex].updatePosition(0, OtherStuff.CAMERA_Y_SPEED);
								
							}//forThings
							
						}//forLayers
						
						
						
					}//ifLoopingLevel
					
					// FOR LOOPING LEVELS //
					else{
						
						//For all layers//
						for(int layerCounter = 0; layerCounter < _currentLevel.getNumberOfLayers(); layerCounter++){

							// TILE UPDATES //
							
							// 1 - still need to move map in this layer
							_currentLevel.getMapAtIndex(layerCounter).updatePosition(0, OtherStuff.CAMERA_Y_SPEED);

							// for all the columns in our current level's current layer's map
							for(int col = 0; col < _currentLevel.getMapAtIndex(layerCounter).getColCount(); col++){
							
								//GameSkeleton.printDebugMessage("row loop index is: " + _currentLevel.getMapAtIndex(layerCounter).getLoopRowIndex());
								
								// if the current tile is not null, AND it's Y position is below the bottom of the map, update it's Y coord to 
								// be at the top of the map!
								if((_currentLevel.getMapAtIndex(layerCounter).getMap()[_currentLevel.getMapAtIndex(layerCounter).getLoopRowIndex()][col] != null) &&
									(_currentLevel.getMapAtIndex(layerCounter).getMap()[_currentLevel.getMapAtIndex(layerCounter).getLoopRowIndex()][col].getY() > OtherStuff.SCREEN_HEIGHT)
								){
									
									//TODO: set the Y value to be the Y value of the PREVIOUS row (or first, if we 're last) - TILESIZE
									if((_currentLevel.getMapAtIndex(layerCounter).getLoopRowIndex() + 1) < _currentLevel.getMapAtIndex(layerCounter).getRowCount()){
										
										_currentLevel.getMapAtIndex(layerCounter).getMap()[_currentLevel.getMapAtIndex(layerCounter).getLoopRowIndex()][col].setY(
												(_currentLevel.getMapAtIndex(layerCounter).getMap()[_currentLevel.getMapAtIndex(layerCounter).getLoopRowIndex() + 1][col].getY()) - OtherStuff.TILE_SIZE
												);
										
									}else{
										
										_currentLevel.getMapAtIndex(layerCounter).getMap()[_currentLevel.getMapAtIndex(layerCounter).getLoopRowIndex()][col].setY(
												(_currentLevel.getMapAtIndex(layerCounter).getMap()[0][col].getY()) - OtherStuff.TILE_SIZE
												);
										
									}
									
								}//if

								
							}//forColumns
							
							// after we update all tiles in columns for current level's current 
							// layer's map's current row , update the loop index of that map
							_currentLevel.getMapAtIndex(layerCounter).updateLoopRowIndex();

							
							
							// THING UPDATES - IN LOOPING LEVELS, DON'T UPDATE THING POSITIONS ?? Hmm//
							
							// for all our things in this layer, update their position as well.
							for(int thingIndex = 0; thingIndex < _currentLevel.getThingsAtIndex(layerCounter).length; thingIndex++){
								
								_currentLevel.getThingsAtIndex(layerCounter)[thingIndex].updatePosition(0, OtherStuff.CAMERA_Y_SPEED);
								
							}//forThings
							
							
						}//forLayers
						
						
					}//elseNonLoopingLevel			
					
			
			
					//////////////////////////////////////////////////
					//////////////////////////////////////////////////
					//////////////////////////////////////////////////
					
					// process all our Things
					processThings(_elapsedTime);
					
	                // Clean up stuff that's gone (e.g. life of 0 and in
	                // destroyable state, eg explosion graphic), quit if we were destroyed
					cleanUpThings();
					
					// move our things
					processInput();
					
					// check for the end of our level
					//TODO: should this go here?
					checkForEndOfLevel();
					
					
				}//elseMenuInactive
				
				//render our things
				render();

				
				// and check our frame rate and delay if required
				while(!_frameRateLimiter.isDone()){
					_elapsedTime = System.currentTimeMillis() - _currTime;
					_currTime += _elapsedTime;
					_frameRateLimiter.update(_elapsedTime);
				}
				
				// after our frame timer expires, reset it and update it
				_frameRateLimiter.reset();
				_frameRateLimiter.update(_elapsedTime);
				
				//update our general key timer
				_generalKeyTimer.update(_elapsedTime);

				// lastly, clean up things that are no longer alive
				//cleanUpThings();
				
			//TODO: get rid of this?
			} finally {
				// Release resources
				//if( graphics != null ) 
				//	graphics.dispose();
				//if( g2d != null ) 
				//	g2d.dispose();
			}
			
		}//while
		
	}//run

	
	/**
	 * This function renders our graphics before they are blitted to the screen
	 */
	protected void render(){
		
		//kill our background with all BACKGROUND_COLOUR
		// TODO: get rid of this rect fill once our map "ends" etc.
		_fOffscreenGraphics.setColor(OtherStuff.BACKGROUND_COLOR);
		_fOffscreenGraphics.fillRect(0, 0, OtherStuff.SCREEN_WIDTH, OtherStuff.SCREEN_HEIGHT);
		
		//For all layers//
		for(int layerCounter = 0; layerCounter < _currentLevel.getNumberOfLayers(); layerCounter++){
			
			// Draw our map's tiles
			for(int row = 0; row < _currentLevel.getMapAtIndex(layerCounter).getRowCount(); row++){
				
				for(int column = 0; column < _currentLevel.getMapAtIndex(layerCounter).getColCount(); column++){

					// If our tile is not null, and is in the viewable area, draw it. Note: we're 
					// leaving a buffer of 1 TILE_SIZE above the top of the screen to eliminate flickering
					//TODO: Optimize by storing the map for the layer instead of calling getMap() 5 times. We're 
					// trying to save clock cycles here to get the screen draw rate up. However, this way is still about
					// 40% faster than the old way. yay.
					if(
    						
    						(_currentLevel.getMapAtIndex(layerCounter).getMap()[row][column] != null) &&
							(_currentLevel.getMapAtIndex(layerCounter).getMap()[row][column].getX() >= 0) &&
    						(_currentLevel.getMapAtIndex(layerCounter).getMap()[row][column].getX() < (OtherStuff.SCREEN_WIDTH)) &&
    						(_currentLevel.getMapAtIndex(layerCounter).getMap()[row][column].getY() >= -OtherStuff.TILE_SIZE) &&
    						(_currentLevel.getMapAtIndex(layerCounter).getMap()[row][column].getY() < (OtherStuff.SCREEN_HEIGHT)) 
    						
					){
						_fOffscreenGraphics.drawImage( _currentLevel.getMapAtIndex(layerCounter).getMap()[row][column].getImage(), _currentLevel.getMapAtIndex(layerCounter).getMap()[row][column].getX(), _currentLevel.getMapAtIndex(layerCounter).getMap()[row][column].getY(),null);
						
						if(_drawDebugData){
							_fOffscreenGraphics.drawString("" + _currentLevel.getMapAtIndex(layerCounter).getMap()[row][column].getY() + "", _currentLevel.getMapAtIndex(layerCounter).getMap()[row][column].getX()+(OtherStuff.TILE_SIZE/2), _currentLevel.getMapAtIndex(layerCounter).getMap()[row][column].getY()+(OtherStuff.TILE_SIZE/2));
						}
						
					}//if
					
				}
				
			}//for
		
			
			//TODO: draw avatar shadow (and other shadows?) here so they fall on the 
			// world, but not on the things in the world
			if(layerCounter == OtherStuff.LAYER_ACTION){
				//_fOffscreenGraphics.setColor(Color.BLACK);
				//_fOffscreenGraphics.fillRect(_avatar.getX()-10, _avatar.getY()-10, 24, 24);
				_fOffscreenGraphics.drawImage(_avatar.getShadowImage(),_avatar.getX()+_avatar.getShadowDrawOffsetX(), _avatar.getY()+_avatar.getShadowDrawOffsetY(), null);
			}
			
			
	        // render all our things that are on screen 
			//TODO: move this to global to save on 4 bytes each frame?
			Thing tempThing;
			
			for(int x = 0; x < _currentLevel.getThingsAtIndex(layerCounter).length; x++){
				
				tempThing = ((Thing) _currentLevel.getThingsAtIndex(layerCounter)[x]);
				
				if(
						(tempThing.getX() >= 0) &&
						(tempThing.getX() < (OtherStuff.SCREEN_WIDTH)) &&
						(tempThing.getY() >= -OtherStuff.TILE_SIZE) &&
						(tempThing.getY() < (OtherStuff.SCREEN_HEIGHT))
						
				){
					
					_fOffscreenGraphics.drawImage(tempThing.getMainImage(),tempThing.getX(), tempThing.getY(),null);
					// and draw the effect animation if this thing has one
					if(tempThing.hasEffectAnimation()){
						_fOffscreenGraphics.drawImage(tempThing.getEffectImage(),tempThing.getX(), tempThing.getY(), null);
					}else{
						
					}
					//DEBUG: also draw the x,y or each thing below it
					if(_drawDebugData){
						_fOffscreenGraphics.setColor(Color.GREEN);
						_fOffscreenGraphics.drawString("["+tempThing.getX()+","+tempThing.getY()+"]-FX?" + tempThing.hasEffectAnimation(), tempThing.getX(),tempThing.getY());
						_fOffscreenGraphics.drawRect(tempThing.getX(), tempThing.getY(), tempThing.getW(), tempThing.getH());
					}
				}
				
			}//forThings
			
			// if the layer we just rendered was the action layer, then also render our avatar, cuz that's where he lives
			if(layerCounter == OtherStuff.LAYER_ACTION){
				
				// Draw _avatar and its effects with bounding box
				_fOffscreenGraphics.drawImage(_avatar.getMainImage(),_avatar.getX(), _avatar.getY(), null);
				_fOffscreenGraphics.drawImage(_avatar.getEffectImage(),_avatar.getX(), _avatar.getY(), null);
				
				if(_drawDebugData){
					_fOffscreenGraphics.setColor(Color.GREEN);
					_fOffscreenGraphics.drawRect(_avatar.getX(), _avatar.getY(), _avatar.getW(), _avatar.getH());
				}
				
			}//ifActionLayer

		}//forLayers
		
		// HUD //
		_fOffscreenGraphics.setColor(Color.BLACK);
		_fOffscreenGraphics.drawImage(_hudBackground.getImage(), OtherStuff.HUD_X, OtherStuff.HUD_Y, null);
		// health
		_fOffscreenGraphics.drawImage(_avatar.getHealthBar().getMainImage(), OtherStuff.AVATAR_HEALTH_BAR_X, OtherStuff.AVATAR_HEALTH_BAR_Y, null);
		_fOffscreenGraphics.drawString(_avatar.getHealth()+"", OtherStuff.AVATAR_HEALTH_BAR_X, OtherStuff.AVATAR_HEALTH_BAR_Y);
		_fOffscreenGraphics.drawImage(_gameFont.createImageFromString(_avatar.getHealth()+""), OtherStuff.AVATAR_HEALTH_BAR_X, OtherStuff.AVATAR_HEALTH_BAR_Y, null);
		
		// weapons/ammo
		_fOffscreenGraphics.drawImage(_avatar.getCurrentWeapon().getIcon().getImage(), OtherStuff.WEAPON_ICON_X, OtherStuff.WEAPON_ICON_Y, null);
		_fOffscreenGraphics.drawString(_avatar.getCurrentWeapon().getAmmoLeft()+"", OtherStuff.WEAPON_ICON_X, OtherStuff.WEAPON_ICON_Y);
		//TODO: lives?
		
		//TODO: test emitter
		_fOffscreenGraphics.drawImage(_emitterTest.drawParticles(), _emitterTest.getOriginX(), _emitterTest.getOriginY(), null);
		
		
		//draw map y coord if debugging
		if(_drawDebugData){
			_fOffscreenGraphics.setColor(Color.BLACK);
			_fOffscreenGraphics.drawString("Map y is " + _currentLevel.getMapAtIndex(0).getY(), 10, 25);
		}
		
		// MENUS - Do these last so they draw on top if active //
		// if our menu is active, draw the menu background, then all the items
		if(_menuActive){
			
			_fOffscreenGraphics.drawImage(_mainMenu.getMenuBackground().getImage(), _mainMenu.getX(), _mainMenu.getY(), null);
			
			// for all menu items, draw the item images
			for(int menuIndex = 0; menuIndex < _mainMenu.getMenuItems().length; menuIndex++){
				
				// if this one is the selected one, draw the selected image. otherwise, draw the regular image
				if(menuIndex == _mainMenu.getCurrentMenuItem()){
					_fOffscreenGraphics.drawImage(_mainMenu.getMenuItems()[menuIndex].getSelectedItemImage().getAnimation().getImage(), _mainMenu.getX() + (_mainMenu.getW()/2) - (MainGameMenu.MENU_ITEM_WIDTH/2), (_mainMenu.getY() + (_mainMenu.getH()/2) - ((MainGameMenu.MENU_ITEM_HEIGHT*MainGameMenu.MAIN_MENU_ITEM_COUNT)/2)) + MainGameMenu.MENU_ITEM_HEIGHT*menuIndex, null);
				}else{
					_fOffscreenGraphics.drawImage(_mainMenu.getMenuItems()[menuIndex].getRegularItemImage().getAnimation().getImage(), _mainMenu.getX() + (_mainMenu.getW()/2) - (MainGameMenu.MENU_ITEM_WIDTH/2), (_mainMenu.getY() + (_mainMenu.getH()/2) - ((MainGameMenu.MENU_ITEM_HEIGHT*MainGameMenu.MAIN_MENU_ITEM_COUNT)/2)) + MainGameMenu.MENU_ITEM_HEIGHT*menuIndex, null);
				}
				
			}//for
			
			
			// Draw release version info
			_fOffscreenGraphics.setColor(Color.white);
			_fOffscreenGraphics.drawString(OtherStuff.RELEASE_NUMBER, OtherStuff.SCREEN_WIDTH - OtherStuff.RELEASE_NUMBER.length()*8, 10);
			
			//DEBUG
			if(_drawDebugData){
				_fOffscreenGraphics.drawString(_mainMenu.getCurrentMenuItem()+"", 100, 100 );
			}

			
		}//ifMenuActive

		
		// then blit this mutha to our screen
		paint(getGraphics());
		
	}//render
	
	//paint what we rendered in a double-buffered kinda way
	public void paint(Graphics g){
		
		if (_fOffscreenImage!=null){
			g.drawImage(_fOffscreenImage, 0, 0, this);
		}else{
			GameSkeleton.printDebugMessage("GameSkeleton: _fOffscreenImage is null in paint()");
		}
		
	}//paint

	/**
	 * This function processes our input when the menu is inactive
	 */
	protected void processInput(){
		
		// Toggle menu off and on
		if( keyboardInput.keyDown( KeyEvent.VK_ESCAPE ) ) {
		
			if(_generalKeyTimer.isDone()){
				_menuActive = !_menuActive;
    			_generalKeyTimer.reset();
    		}
		
		}
		
		///////////////////
		// If moving down
		if( keyboardInput.keyDown( KeyEvent.VK_DOWN ) ) {
		
			_avatar.decreaseForwardVelocity();
			_avatar.setY(_avatar.getY() + _avatar.getVY());
			
			// Check collision with botton
			if( _avatar.getY() > (OtherStuff.AVATAR_MAX_Y_COORD) - 1 )
				_avatar.setY((OtherStuff.AVATAR_MAX_Y_COORD) - 1);
		
		}
		///////////////////
		// If moving up
		if( keyboardInput.keyDown( KeyEvent.VK_UP ) ) {
			
			_avatar.increaseForwardVelocity();
			_avatar.setY(_avatar.getY() - _avatar.getVY());
		
			// Check collision with top
			if( _avatar.getY() < 0 )
				_avatar.setY(0);
		
		}
		
		// if we let go of the up arrow, enable another velocity change
		if( keyboardInput.keyUpOnce(KeyEvent.VK_UP)){
			_avatar.enableVelocityChange();
		}
		
		// if we let go of the down arrow, enable another velocity change
		if( keyboardInput.keyUpOnce(KeyEvent.VK_DOWN)){
			_avatar.enableVelocityChange();
		}
		
		////////////////////
		// If moving right //
		if( keyboardInput.keyDown(KeyEvent.VK_RIGHT) || keyboardInput.keyDownOnce(KeyEvent.VK_RIGHT) ) {
			
			//TODO: make keyDown events play full animations. For some reason they are not. LAME! Current
			// workaround is to ask if keyDown OR keyDOwnOnce...not sure if that will cause problems
			
			//if the avatar is not already rolling right, roll them right
			if(_avatar.getMainAnimationState() != Avatar.ANIM_ROLL_RIGHT){
				_avatar.rollRight();
			}

			_avatar.setX(_avatar.getX() + _avatar.getVX());
			
			// Check collision with right. If we collide, move us away, and set state to not rolling right anymore
			if( _avatar.getX() + _avatar.getW() > (OtherStuff.SCREEN_WIDTH) - 1 ){
				_avatar.setX((OtherStuff.SCREEN_WIDTH) - _avatar.getW() - 1);
				//TODO: not sure if I like this. If you're close to the wall when you start banking, it looks choppy
				// Maybe something like "finish bank animation, then bank back to straight..."
				_avatar.flyStraight();
			}
			
		}
		if( keyboardInput.keyUpOnce(KeyEvent.VK_RIGHT) ) {
			_avatar.flyStraight();
		}
		
		////////////////////
		// If moving left //
		if( keyboardInput.keyDown(KeyEvent.VK_LEFT) || keyboardInput.keyDownOnce(KeyEvent.VK_LEFT) ) {
			
			//TODO: make keyDown events play full animations. For some reason they are not. LAME! Current
			// workaround is to ask if keyDown OR keyDOwnOnce...not sure if that will cause problems
			
			//if the avatar is not already rolling right, roll them right
			if(_avatar.getMainAnimationState() != Avatar.ANIM_ROLL_LEFT){
				_avatar.rollLeft();
			}

			_avatar.setX(_avatar.getX() - _avatar.getVX());
			
			// Check collision with right. If we collide, move us away, and set state to not rolling right anymore
			if( _avatar.getX() < 0 ){
				_avatar.setX(0);
				//TODO: not sure if I like this. If you're close to the wall when you start banking, it looks choppy
				// Maybe something like "finish bank animation, then bank back to straight..."
				_avatar.flyStraight();
			}
			
		}
		if( keyboardInput.keyUpOnce(KeyEvent.VK_LEFT) ) {
			_avatar.flyStraight();
		}

		/////////////////////////////////////
		// If firing (space for now) //
		if( keyboardInput.keyDown(KeyEvent.VK_SPACE) || keyboardInput.keyDownOnce(KeyEvent.VK_SPACE) ){
			// note: there is no target for avatar, the coords are just required by the function
			//_avatar.fire(_levelWithLayers.getLayerAtIndex(_currentLevelLayer), 0, 0);
			_avatar.fire(_currentLevel.getLayerAtIndex(_currentLevelLayer), 0, 0);
		}
		
		// If getting next weapon
		if( keyboardInput.keyDown(KeyEvent.VK_Z) || keyboardInput.keyDownOnce(KeyEvent.VK_Z) ){
			_avatar.nextWeapon();
		}
		
		// Toggle _drawDebugData flag
		if( keyboardInput.keyDown(KeyEvent.VK_D) || keyboardInput.keyDownOnce(KeyEvent.VK_D) ){
			_drawDebugData = !_drawDebugData;
		}
		
	}//processInput
	
	/**
	 * This function processes our input when the menu is active
	 */
	protected void processMenuInput(){
		
		//TODO: move generalKeyTimer checks to the main loop before calling this function?
		
		// Toggle menu off and on
		if( keyboardInput.keyDown( KeyEvent.VK_ESCAPE ) ) {
		
			if(_generalKeyTimer.isDone()){
				_menuActive = !_menuActive;
    			_generalKeyTimer.reset();
    		}
		
		}
		
		// select next menu item
		if( keyboardInput.keyDown( KeyEvent.VK_DOWN ) ) {
		
			if(_generalKeyTimer.isDone()){
    			_mainMenu.getNextMenuItem();
    			_generalKeyTimer.reset();
    		}
		
		}
		// select previous menu item
		if( keyboardInput.keyDown( KeyEvent.VK_UP ) ) {
			
			if(_generalKeyTimer.isDone()){
				_mainMenu.getPreviousMenuItem();
    			_generalKeyTimer.reset();
    		}
		
		}
		
		// Execute menu item functionality
		if( keyboardInput.keyDown( KeyEvent.VK_ENTER ) ) {
			
			if(_generalKeyTimer.isDone()){
				
				
				// if quit game //
				if(_mainMenu.getCurrentMenuItem() == MainGameMenu.MENU_ITEM_QUIT_GAME){
					//GameSkeleton.printDebugMessage("Quitting Game. Goodbye!");
					System.exit(0);
				}
				
				
    			_generalKeyTimer.reset();
    		}
		
		}
		
	}//processMenuInput
	
	protected void updateAnimations(long elapsedTime){
		
		//for now just update our animated sprite
		//_animatedSpriteThing.updateAnimation(elapsedTime);
		
		//and update our avatar too
		_avatar.updateAnimations(elapsedTime);
		//and update the avatar's health bar
		_avatar.getHealthBar().updateAnimations(elapsedTime);
		
		//TODO: test emitter
		_emitterTest.updateParticles(elapsedTime);
		
		
		// For all Layers //
		for(int layerCounter = 0; layerCounter < _currentLevel.getNumberOfLayers(); layerCounter++){
		
			// update our map sprites
			for(int row = 0; row < _currentLevel.getMapAtIndex(layerCounter).getRowCount(); row++){
				
				for(int column = 0; column < _currentLevel.getMapAtIndex(layerCounter).getColCount(); column++){
					
					//if our animated sprite is visible, update it
					if(_currentLevel.getMapAtIndex(layerCounter).getMap()[row][column] != null)
						_currentLevel.getMapAtIndex(layerCounter).getMap()[row][column].updateAnimation(elapsedTime);
					
				}//for
				
			}//for
			
			//update our world things
			for(int i = 0; i < _currentLevel.getThingsAtIndex(layerCounter).length; i++){
	
				_currentLevel.getThingsAtIndex(layerCounter)[i].updateAnimations(elapsedTime);
				
			}//for
		
		}//forAllLayers
		
	}//updateAnimations
	
	// basically calls process() on all our things
	protected void processThings(long elapsedTime){
		
		Thing tempThing;
		
		// process our avatar
		_avatar.process(elapsedTime);
				
		// then the rest of our things
		
		// For All Layers //
		for(int layerCounter = 0; layerCounter < _currentLevel.getNumberOfLayers(); layerCounter++){
		
			for(int thingIndex =0; thingIndex < _currentLevel.getThingsAtIndex(layerCounter).length; thingIndex++){
			
				tempThing = _currentLevel.getThingsAtIndex(layerCounter)[thingIndex];
				tempThing.process(_elapsedTime);
			
				//TODO: this is hacky but might be the only way for it to work
				if(tempThing.readyToFire()){
					tempThing.fire(_currentLevel.getLayerAtIndex(layerCounter), _avatar.getX(), _avatar.getY());
				}
			
			}
		
		}//forAllLayers
		
	}//processThings
	
	/**
	 * This function deletes any thing that is not alive, or otherwise off 
	 * screen or no longer usable.
	 * WARNING: This function makes assumptions that we're using the JVM
	 * and will rely on garbage collection. When implementing in another
	 * language, replace the null handle stuff with destructors or other 
	 * functionality that will recover the memory. 
	 */
	protected void cleanUpThings(){
		
		// For All Layers //
		//for(int layerCounter = 0; layerCounter < _levelWithLayers.getNumberOfLayers(); layerCounter++){
		for(int layerCounter = 0; layerCounter < _currentLevel.getNumberOfLayers(); layerCounter++){
		
			//for(int thingIndex = 0; thingIndex < _levelWithLayers.getThingsAtIndex(layerCounter).length; thingIndex++){
			for(int thingIndex = 0; thingIndex < _currentLevel.getThingsAtIndex(layerCounter).length; thingIndex++){
				
				// if the Thing is not alive, or it's Y index is past the bottom of the screen, replace
				// its index in the things array with null so that the JVM can recover the memory
				if(_currentLevel.getThingsAtIndex(layerCounter)[thingIndex].getLifeState() == Thing.LIFE_STATE_DEAD_AND_GONE){
					_currentLevel.getThingsAtIndex(layerCounter)[thingIndex] = null;
					_currentLevel.getLayerAtIndex(layerCounter).removeThingAtIndex(thingIndex);
				}//if
				
			}//for
		
		}//forAllLayers
		
	}//cleanUpThings
	
	/**
	 * This function loads our level sequence so our game can progress. It loads
	 * from a file so that we don't need to edit the game to make new level 
	 * packs...because that would be really dumb.
	 * @param filename - the name of the level sequence file to load
	 */
	public boolean loadLevelSequence(String filename){
		
		int levelIndex = 0;
		boolean successful = true;
		File f = new File(filename);
		
		if(f.canRead()){
		
			try{
				
				DocumentBuilderFactory dbfac = DocumentBuilderFactory.newInstance();
				DocumentBuilder docBuilder = dbfac.newDocumentBuilder();
				Document doc = docBuilder.parse(f);
				
				//LevelSequence//
				Node levelSequence = doc.getFirstChild();
				NamedNodeMap levelAttributes = levelSequence.getAttributes();
				NodeList levelSequenceChildren = levelSequence.getChildNodes();
				
				// initiate our game levels array, using xml level sequence data
				//GameSkeleton.printDebugMessage("Trying to load " + Integer.parseInt(levelAttributes.getNamedItem("numberOfLevels").getNodeValue()) + " levels...");
				_gameLevels = new Level[Integer.parseInt(levelAttributes.getNamedItem("numberOfLevels").getNodeValue())];
				
				//GameSkeleton.printDebugMessage("game levels is of size " + _gameLevels.length);
				
				//for all children of the level sequence, check them out
				for(int childCount = 0; childCount < levelSequenceChildren.getLength(); childCount++){
					
					Node tempNode = levelSequenceChildren.item(childCount);

					// Level //
					if(tempNode.getNodeName() == "Level"){
						
						//GameSkeleton.printDebugMessage("Child is Level, level index is " + levelIndex);
						
						NamedNodeMap levelAttr = tempNode.getAttributes();

						//GameSkeleton.printDebugMessage("GameSkeleton: leveIIndex is " + levelIndex);
						
						_gameLevels[levelIndex] = new Level(levelAttr.getNamedItem("filename").getNodeValue());
						_gameLevels[levelIndex].loadLevelWithLayers(levelAttr.getNamedItem("filename").getNodeValue());
						
						//GameSkeleton.printDebugMessage("Child #" + levelIndex + ": filename = '" + levelAttr.getNamedItem("filename").getNodeValue() + "'");
						
						levelIndex++;
						
					}else{
						//GameSkeleton.printDebugMessage("MapEditor:loadLevelSequence() - Error: Encountered non-Level node");
					}

				}//forLevelNodes				
			
			}catch ( Exception e ){
				GameSkeleton.printDebugMessage( "GameSkeleton: Error parsing xml file for Level Sequence!: class("+e.getClass()+"), message:" + e.getMessage() );
			}
		
		}else{
			successful = false;
		}
		
		return successful;
		
	}//loadLevelSequence
	
	/**
	 * This function checks to see if our current level is over
	 * and switches to the next level if it is 
	 */
	public void checkForEndOfLevel(){
		
		// if our current level is at the end, end the level
		// and get the next one
		if( (_gameLevels[_currentLevelIndex].getLoopState() == Level.LEVEL_LOOP_FALSE) && (_gameLevels[_currentLevelIndex].getLevelEndYCoord() < _currentLevel.getLayerAtIndex(0).getMap().getY())){
			
			//GameSkeleton.printDebugMessage("GameSkeleton.checkForEndOfLevel(): current level (#"+_currentLevelIndex+") end coord("+_gameLevels[_currentLevelIndex].getLevelEndYCoord()+") < current map Y coord ("+_currentLevel.getLayerAtIndex(0).getMap().getY()+")");
			
			GameSkeleton.printDebugMessage("Level " + _currentLevelIndex + " Complete!");
			_currentLevelIndex++;
			
			//TODO: stop playing game music during level transition
			//_soundManager.stopSoundAtIndex(0);
			
			// Test transition screen
			testScreen();
			
			if(_currentLevelIndex > _gameLevels.length-1){
				GameSkeleton.printDebugMessage("GameSkeleton: No more levels. Exiting...");
				System.exit(-1);
			}else{
				_currentLevel = _gameLevels[_currentLevelIndex];
				_currentLevel.moveXY( 0, -((_currentLevel.getMapAtIndex(_currentLevelLayer).getRowCount() * OtherStuff.EDITOR_TILE_SIZE) - (OtherStuff.SCREEN_HEIGHT)) );
			}
			
		}else{
			
			// For looping levels...probably does nothing
			
			
		}//else

	}//checkForEndOfLevel
	
	public void testScreen(){
		
		boolean screenActive = true;
		
		BadGuyTest testGuy = new BadGuyTest(100, 100, 0, 0);
		Sprite testSprite = new Sprite("sprite_screen_level_complete.png");
		
		SoundManagerSingleton.getSingletonObject().loadSound(MUSIC_LEVEL_COMPLETE);
		SoundManagerSingleton.getSingletonObject().playSound(MUSIC_LEVEL_COMPLETE);
		
		BufferedImage testFontImage = _gameFont.createImageFromString("SPRITE FONT ENGINE!");
		
		while(screenActive){
			
			//GameSkeleton.printDebugMessage("screen active...");
				
			// Poll the keyboardInput
			keyboardInput.poll();
			
			// Update sprite animations


			
			// process all our Things

			
			
			// process input (e.g. space to exit screen
			
			// Toggle menu off and on
			if( keyboardInput.keyDown( KeyEvent.VK_SPACE ) ) {
			
				if(_generalKeyTimer.isDone()){
					screenActive = false;
					//soundTest.stopSoundAtIndex(0);
					SoundManagerSingleton.getSingletonObject().stopSound("sound_music_level_complete.wav");
					keyboardInput.flushKeyPresses();
	    			_generalKeyTimer.reset();
	    		}
			
			}
			
			//render our stuff
			_fOffscreenGraphics.setColor(OtherStuff.BACKGROUND_COLOR);
			_fOffscreenGraphics.fillRect(0, 0, OtherStuff.SCREEN_WIDTH, OtherStuff.SCREEN_HEIGHT);
			_fOffscreenGraphics.drawImage(testSprite.getImage(),10,10, null);
			_fOffscreenGraphics.setColor(Color.GREEN);
			_fOffscreenGraphics.drawString("Level " + (_currentLevelIndex + 1) + "complete!",100,10);
			
			_fOffscreenGraphics.drawImage(testFontImage,0,0, null);
			
			paint(getGraphics());
			
			
			// and check our frame rate and delay if required
			while(!_frameRateLimiter.isDone()){
				_elapsedTime = System.currentTimeMillis() - _currTime;
				_currTime += _elapsedTime;
				_frameRateLimiter.update(_elapsedTime);
			}
			
			// after our frame timer expires, reset it and update it
			_frameRateLimiter.reset();
			_frameRateLimiter.update(_elapsedTime);
			
			//update our general key timer
			_generalKeyTimer.update(_elapsedTime);

			
		}//while
		
	}//testScreen
	
	
	/**
	 * draw's a game screen until ENTER is pressed
	 */
	public void drawScreen(GameScreen gameScreen){
		
		boolean screenActive = true;

		BufferedImage testFontImage = _gameFont.createImageFromString("SPRITE FONT ENGINE!");		
				
		// play screen sound
		//gameScreen.playSound();
		gameScreen.playSound(MUSIC_LEVEL_COMPLETE);
		
		// and do the screen loop
		while(screenActive){
			
			//GameSkeleton.printDebugMessage("screen active...");
				
			// Poll the keyboardInput
			keyboardInput.poll();
			
			// Update sprite animations
			gameScreen.updateAnimation(_elapsedTime);
		
			// process all our Things
			
			// process input (e.g. space to exit screen
			
			// Toggle menu off and on
			if( keyboardInput.keyDown( KeyEvent.VK_SPACE ) ) {
			
				if(_generalKeyTimer.isDone()){
					screenActive = false;
					gameScreen.stopSound(MUSIC_LEVEL_COMPLETE);
					keyboardInput.flushKeyPresses();
	    			_generalKeyTimer.reset();
	    		}
			
			}
			
			//render our stuff
			_fOffscreenGraphics.setColor(OtherStuff.BACKGROUND_COLOR);
			_fOffscreenGraphics.fillRect(0, 0, OtherStuff.SCREEN_WIDTH, OtherStuff.SCREEN_HEIGHT);
			_fOffscreenGraphics.drawImage(gameScreen.getMainAnimatedSprite().getImage(), 0, 0, null);
			_fOffscreenGraphics.setColor(Color.GREEN);
			_fOffscreenGraphics.drawString("Strings Are Great!",10,10);
			
			_fOffscreenGraphics.drawImage(testFontImage,0,0, null);
			
			paint(getGraphics());
			
			
			// and check our frame rate and delay if required
			while(!_frameRateLimiter.isDone()){
				_elapsedTime = System.currentTimeMillis() - _currTime;
				_currTime += _elapsedTime;
				_frameRateLimiter.update(_elapsedTime);
			}
			
			// after our frame timer expires, reset it and update it
			_frameRateLimiter.reset();
			_frameRateLimiter.update(_elapsedTime);
			
			//update our general key timer
			_generalKeyTimer.update(_elapsedTime);
			
		}//while
		
	}//testScreen
	
	/**
	 * This is a simple abstracted debug message printer. It prints
	 * the string msg and adds a newline at the end
	 * @param msg - the String to print
	 */
	public static void printDebugMessage(String msg){
		System.out.println(msg);
	}//printDebugMessage

	
	public static void main( String[] args ) {
		
		GameSkeleton app = new GameSkeleton();
		//WindowUtilities.openInJFrame(app, (OtherStuff.SCREEN_WIDTH), (OtherStuff.SCREEN_HEIGHT) + OtherStuff.JAVA_JFRAME_WINDOW_OFFSET);
		
    	JFrame frame = new JFrame("Game Skeleton");
    	frame.setBackground(Color.white);
    	app.setBackground(Color.gray);
    	frame.setSize((OtherStuff.SCREEN_WIDTH), (OtherStuff.SCREEN_HEIGHT) + OtherStuff.JAVA_JFRAME_WINDOW_OFFSET);
    	frame.setContentPane(app);
    	frame.addWindowListener(new ExitListener());
    	frame.setVisible(true);
		
		app.run();
		System.exit( 0 );
		
	}//main

}//GameSkeletonClass

