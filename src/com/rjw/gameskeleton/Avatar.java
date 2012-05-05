package com.rjw.gameskeleton;

import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;
import com.rjw.sfx.SoundManagerSingleton;

// TODO: add animation polish for increasing and decreasing velocity (perhaps longer afterburner or something)
// TODO: polish feel of controls vs. speed. Right now it feels like i'm mashing the arrows and it's not going fast enough


/**
 * Our main avatar (player)
 */
public class Avatar extends Thing{

	private int _vx;
	private int _vy;
	private int _health;
	private int _lives;
	private BufferedImage _frameTile;
	private BufferedImage _effectsTile;
	//private BufferedImage _healthBarTile; 
	private int _mainAnimationState = 0;
	private int _effectAnimationState = 0;
	private int _shadowAnimationState = 0;
	private int _gameState = 0;
	private boolean _canChangeVelocity;
	
	//health bar animation
	private HealthBar _healthBar;
	
	private Weapon[] _weapons;
	private int _currentWeapon;

	public static final int WEAPON_MISSILES = 0;
	public static final int WEAPON_BULLETS = 1;
	public static final int WEAPON_COUNT = 2;
	
	//Timers
	Timer _hitTimer;
	Timer _dyingTimer;
	Timer _weaponTimer;
	Timer _weaponSwitchTimer;
	Timer _velocityChangeTimer;
	
	//DEBUG
	int _shotsFired = 0;	
	
	// animation states - will reuse these for the shadows as well
	static final int ANIM_FLY_STRAIGHT = 0;
	static final int ANIM_ROLL_RIGHT = 1;
	static final int ANIM_ROLL_LEFT = 2;
	
	//effect animation states
	static final int ANIM_EFFECT_NONE = 0;
	static final int ANIM_EFFECT_DAMAGE = 1;
	static final int ANIM_EFFECT_DYING = 2;
	
	// game states
	static final int GAME_NORMAL = 0;
	static final int GAME_HIT = 1;
	static final int GAME_DYING = 2;
	static final int GAME_DEAD = 3;
	
	// game state timers (in ms)
	static final int GAME_HIT_TIMER = 2000;
	static final int GAME_DYING_TIMER = 1500;
	static final int GAME_WEAPON_SWITCH_TIMER = 1000;
	static final int  AVATAR_WEAPON_CHANGE_TIMER = 1500;

	// sound stuff //
	//TODO: make weapon sounds come from weapon
	static final String SOUND_HURT = "sound_sfx_avatar_hit_01.wav";
	static final String SOUND_DIE = "sound_sfx_avatar_death.wav";
	static final String SOUND_FIRE_BULLET = "sound_sfx_shortBulletFireTest.wav";
	static final String SOUND_FIRE_MISSILE = "sound_sfx_weapon_missile_fire.wav";
	static final String SOUND_FIRE_50MM_BURST = "50mmTest4.wav";//"sound_sfx_avatar_50mm_burst.ogg";
	static final int SOUND_ENGINE = 2;
	static final int SOUND_ENGINE2 = 3;
	static final int SOUND_ENGINE3 = 4;
	static final int SOUND_ENGINE4 = 5;
	static final String SOUND_HEAL = "sound_sfx_avatar_heal.wav";
	static final int SOUND_RELOAD = 7;
	static final int SOUND_COUNT = 8;
	static final String SOUND_SLOW_DOWN = "sound_sfx_avatar_slowdown.wav";
	static final String SOUND_SPEED_UP = "sound_sfx_avatar_speedup.wav";

	static final int AVATAR_W = 32;
	static final int AVATAR_H = 32;
	
	/**
	 * Our Avatar
	 * @param x - x coord
	 * @param y - y coord
	 * @param vy - velocity in y
	 * @param vx - velocity in x
	 * @param lives - number of lives
	 * @param health - health
	 */
	public Avatar(int x, int y, int vy, int vx, int lives, int health){
		
		//call our thing constructor
		super(ThingManager.THING_ID_AVATAR, x, y, AVATAR_W, AVATAR_H, new Animation(), new Animation(), new Animation());
		
		//and some custom avatar stuff
		_vy = vy;
		_vx = vx;
		_lives = lives;
		_health = health;
		_mainAnimationState = ANIM_FLY_STRAIGHT;
		_effectAnimationState = ANIM_EFFECT_NONE;
		_gameState = GAME_NORMAL;
		
		_canChangeVelocity = true;
		
		// init weapons
		_weapons = new Weapon[WEAPON_COUNT];
		_weapons[0] = new Weapon("Missiles", /*"sound_sfx_weapon_missile_fire.wav",*/ 1250, 1, 50, 50, ThingManager.THING_ID_PROJECTILE, OtherStuff.SPRITE_MISSILE_ICON);
		_weapons[1] = new Weapon("50 mm", /*"sound_sfx_weapon_bullet_50mm_fire",*/ 200, 1, 500, 500, ThingManager.THING_ID_BULLET_50MM, OtherStuff.SPRITE_BULLET_50MM_ICON);
		_currentWeapon = 1;
		
		//TODO: make this nicer?
		_hitTimer = new Timer(GAME_HIT_TIMER);
		_dyingTimer = new Timer(GAME_DYING_TIMER);
		_weaponTimer = new Timer(_weapons[_currentWeapon].getFireDelay());
		_weaponSwitchTimer = new Timer(GAME_WEAPON_SWITCH_TIMER);
		// This timer is a workaround because we don't want velocity change sounds to overlap and echo :\
		_velocityChangeTimer = new Timer(AVATAR_WEAPON_CHANGE_TIMER);
		
		//add some "collidability"
		this.setCollisionsMatter(true);
		
		//try to load our animation sprite tile images
		try{	
			_frameTile = ImageIO.read(new File(OtherStuff.SPRITE_PATH_PREFIX + OtherStuff.SPRITE_PLANE_TILE_TEST));
			_effectsTile = ImageIO.read(new File(OtherStuff.SPRITE_PATH_PREFIX + OtherStuff.SPRITE_EFFECTS_001));
		}catch(Exception e){
			GameSkeleton.printDebugMessage("error loading frames for avatar ("+e.getMessage()+")");
		}
		
		/////////////////////
		// MAIN ANIMATIONS //
		/////////////////////
		// Add frames for our fly straight animation
		super.addAnimFrame(_frameTile.getSubimage(2*OtherStuff.AVATAR_SIZE, 0, OtherStuff.AVATAR_SIZE, OtherStuff.AVATAR_SIZE), 300);
		super.addAnimFrame(_frameTile.getSubimage(2*OtherStuff.AVATAR_SIZE, OtherStuff.AVATAR_SIZE, OtherStuff.AVATAR_SIZE, OtherStuff.AVATAR_SIZE), 300);
		
		// then add a new animation for roll right, index to it, and loop it once only
		super.addAnimation(new Animation());
		super.setCurrentAnimation(ANIM_ROLL_RIGHT);
		super.setAnimationLoopCount(1);
		//then add our two roll right frames
		super.addAnimFrame(_frameTile.getSubimage(3*OtherStuff.AVATAR_SIZE, 0, OtherStuff.AVATAR_SIZE, OtherStuff.AVATAR_SIZE), 300);
		super.addAnimFrame(_frameTile.getSubimage(3*OtherStuff.AVATAR_SIZE, OtherStuff.AVATAR_SIZE, OtherStuff.AVATAR_SIZE, OtherStuff.AVATAR_SIZE), 300);
		super.addAnimFrame(_frameTile.getSubimage(4*OtherStuff.AVATAR_SIZE, 0, OtherStuff.AVATAR_SIZE, OtherStuff.AVATAR_SIZE), 300);
		super.addAnimFrame(_frameTile.getSubimage(4*OtherStuff.AVATAR_SIZE, OtherStuff.AVATAR_SIZE, OtherStuff.AVATAR_SIZE, OtherStuff.AVATAR_SIZE), 300);
		
		// and add a new animation for roll left, index to it, and loop it once only
		super.addAnimation(new Animation());
		super.setCurrentAnimation(ANIM_ROLL_LEFT);
		super.setAnimationLoopCount(1);
		//then add our two roll right frames
		super.addAnimFrame(_frameTile.getSubimage(OtherStuff.AVATAR_SIZE, 0, OtherStuff.AVATAR_SIZE, OtherStuff.AVATAR_SIZE), 300);
		super.addAnimFrame(_frameTile.getSubimage(OtherStuff.AVATAR_SIZE, OtherStuff.AVATAR_SIZE, OtherStuff.AVATAR_SIZE, OtherStuff.AVATAR_SIZE), 300);
		super.addAnimFrame(_frameTile.getSubimage(0, 0, OtherStuff.AVATAR_SIZE, OtherStuff.AVATAR_SIZE), 300);
		super.addAnimFrame(_frameTile.getSubimage(0, OtherStuff.AVATAR_SIZE, OtherStuff.AVATAR_SIZE, OtherStuff.AVATAR_SIZE), 300);

		///////////////////////
		// EFFECT ANIMATIONS //
		///////////////////////
		// Add frames for our default effect animation (i.e. NOTHING!)
		super.addEffectAnimFrame(_effectsTile.getSubimage(0,0,OtherStuff.AVATAR_SIZE,OtherStuff.AVATAR_SIZE), 0);
		
		// index to our damage effect, and create it as fireballs
		super.addEffectAnimation(new Animation());
		super.setCurrentEffectAnimation(ANIM_EFFECT_DAMAGE);
		super.addEffectAnimFrame(_effectsTile.getSubimage(OtherStuff.AVATAR_SIZE,0,OtherStuff.AVATAR_SIZE,OtherStuff.AVATAR_SIZE),250);
		super.addEffectAnimFrame(_effectsTile.getSubimage(2*OtherStuff.AVATAR_SIZE,0,OtherStuff.AVATAR_SIZE,OtherStuff.AVATAR_SIZE),250);
		
		//index to our dying effect, and create it as big fireballs
		super.addEffectAnimation(new Animation());
		super.setCurrentEffectAnimation(ANIM_EFFECT_DYING);
		super.addEffectAnimFrame(_effectsTile.getSubimage(3*OtherStuff.AVATAR_SIZE,0,OtherStuff.AVATAR_SIZE,OtherStuff.AVATAR_SIZE),250);
		super.addEffectAnimFrame(_effectsTile.getSubimage(4*OtherStuff.AVATAR_SIZE,0,OtherStuff.AVATAR_SIZE,OtherStuff.AVATAR_SIZE),250);
		super.addEffectAnimFrame(_effectsTile.getSubimage(5*OtherStuff.AVATAR_SIZE,0,OtherStuff.AVATAR_SIZE,OtherStuff.AVATAR_SIZE),250);
		super.addEffectAnimFrame(_effectsTile.getSubimage(6*OtherStuff.AVATAR_SIZE,0,OtherStuff.AVATAR_SIZE,OtherStuff.AVATAR_SIZE),250);
		
		/////////////////////////////
		// SHADOW EFFECT ANIMATION //
		/////////////////////////////
		// Add frames for our fly straight animation
		super.addShadowAnimFrame(_frameTile.getSubimage(2*OtherStuff.AVATAR_SIZE, 2*OtherStuff.AVATAR_SIZE, OtherStuff.AVATAR_SIZE, OtherStuff.AVATAR_SIZE), 300);
		//super.addShadowAnimFrame(_frameTile.getSubimage(2*OtherStuff.AVATAR_SIZE, OtherStuff.AVATAR_SIZE, OtherStuff.AVATAR_SIZE, OtherStuff.AVATAR_SIZE), 300);
		
		// then add a new animation for roll right, index to it, and loop it once only
		super.addShadowAnimation(new Animation());
		super.setCurrentShadowAnimation(ANIM_ROLL_RIGHT);
		super.setShadowAnimationLoopCount(1);
		//then add our two roll right frames
		super.addShadowAnimFrame(_frameTile.getSubimage(3*OtherStuff.AVATAR_SIZE, 2*OtherStuff.AVATAR_SIZE, OtherStuff.AVATAR_SIZE, OtherStuff.AVATAR_SIZE), 300);
		//super.addShadowAnimFrame(_frameTile.getSubimage(3*OtherStuff.AVATAR_SIZE, OtherStuff.AVATAR_SIZE, OtherStuff.AVATAR_SIZE, OtherStuff.AVATAR_SIZE), 300);
		super.addShadowAnimFrame(_frameTile.getSubimage(4*OtherStuff.AVATAR_SIZE, 2*OtherStuff.AVATAR_SIZE, OtherStuff.AVATAR_SIZE, OtherStuff.AVATAR_SIZE), 300);
		//super.addShadowAnimFrame(_frameTile.getSubimage(4*OtherStuff.AVATAR_SIZE, OtherStuff.AVATAR_SIZE, OtherStuff.AVATAR_SIZE, OtherStuff.AVATAR_SIZE), 300);
		
		// and add a new animation for roll left, index to it, and loop it once only
		super.addShadowAnimation(new Animation());
		super.setCurrentShadowAnimation(ANIM_ROLL_LEFT);
		super.setShadowAnimationLoopCount(1);
		//then add our two roll right frames
		super.addShadowAnimFrame(_frameTile.getSubimage(OtherStuff.AVATAR_SIZE, 2*OtherStuff.AVATAR_SIZE, OtherStuff.AVATAR_SIZE, OtherStuff.AVATAR_SIZE), 300);
		//super.addShadowAnimFrame(_frameTile.getSubimage(OtherStuff.AVATAR_SIZE, OtherStuff.AVATAR_SIZE, OtherStuff.AVATAR_SIZE, OtherStuff.AVATAR_SIZE), 300);
		super.addShadowAnimFrame(_frameTile.getSubimage(0, 2*OtherStuff.AVATAR_SIZE, OtherStuff.AVATAR_SIZE, OtherStuff.AVATAR_SIZE), 300);
		//super.addShadowAnimFrame(_frameTile.getSubimage(0, OtherStuff.AVATAR_SIZE, OtherStuff.AVATAR_SIZE, OtherStuff.AVATAR_SIZE), 300);


		
		// and our healthBar
		_healthBar = new HealthBar();
		
		////////////
		//MAKE SURE YOU SET TO THE DESIRED DEFAULT ANIMATION INDICES AND AVATAR STATE!
		_gameState = GAME_NORMAL;
		// main 
		_mainAnimationState = ANIM_FLY_STRAIGHT;
		super.setCurrentAnimation(ANIM_FLY_STRAIGHT);
		super.startAnimation();
		// effect
		_effectAnimationState = ANIM_EFFECT_NONE;
		super.setCurrentEffectAnimation(ANIM_EFFECT_NONE);
		super.startEffectAnimation();
		// shadow
		_shadowAnimationState = ANIM_FLY_STRAIGHT;
		super.setCurrentShadowAnimation(ANIM_FLY_STRAIGHT);
		super.startShadowAnimation();
		
		
	}//constructor
	
	//getters
	public int getVX(){ return _vx; }
	public int getVY(){ return _vy;	}
	public int getLives(){ return _lives; }
	public int getHealth() { return _health; }
	public int getMainAnimationState() { return _mainAnimationState; }
	public int getEffectAnimationState() { return _effectAnimationState; }
	public int getShadowAnimationState() { return _shadowAnimationState; }
	public int getGameState() { return _gameState; }
	public String getCurrentWeaponName(){ return _weapons[_currentWeapon].getName(); }
	public int getAmmoLeftOnCurrentWeapon(){ return _weapons[_currentWeapon].getAmmoLeft(); }
	public HealthBar getHealthBar(){ return _healthBar;	}
	public Weapon getCurrentWeapon(){ return _weapons[_currentWeapon]; }
	
	//setters
	public void setLives(int newLives){ _lives = newLives; }
	public void setHealth(int newHealth) { _health = newHealth; }
	public void setState(int newState) { _mainAnimationState = newState; }
	
	/**
	 * Decrease forward velocity by specified increment, or to preset minimum 
	 * value. Also plays deceleration sound.
	 * TODO: add animation change?
	 */
	public void decreaseForwardVelocity(){
		
		_vy = Math.max(OtherStuff.AVATAR_MIN_Y_SPEED, _vy - OtherStuff.AVATAR_X_SPEED_CHANGE_INCREMENT);
		
		if(_velocityChangeTimer.isDone() && _canChangeVelocity){
			_velocityChangeTimer.reset();
			_canChangeVelocity = false;
			//SoundManagerSingleton.getSingletonObject().stopSound(SOUND_SLOW_DOWN);
			SoundManagerSingleton.getSingletonObject().playSoundIfNotPlaying(SOUND_SLOW_DOWN);
			//SoundManagerSingleton.getSingletonObject().playSound(SOUND_SLOW_DOWN);
		}
		
	}//decreaseForwardVelocity
	
	// increase by specified increment, or to maximum vy value
	/**
	 * Increase forward velocity by specified increment, or to preset maximum 
	 * value. Also plays deceleration sound.
	 * TODO: add animation change?
	 */
	public void increaseForwardVelocity(){
		
		_vy = Math.min(OtherStuff.AVATAR_MAX_Y_SPEED, _vy + OtherStuff.AVATAR_X_SPEED_CHANGE_INCREMENT);
		
		if(_velocityChangeTimer.isDone() && _canChangeVelocity){
			_velocityChangeTimer.reset();
			_canChangeVelocity = false;
			//SoundManagerSingleton.getSingletonObject().stopSound(SOUND_SPEED_UP);
			SoundManagerSingleton.getSingletonObject().playSoundIfNotPlaying(SOUND_SPEED_UP);
			//SoundManagerSingleton.getSingletonObject().isSoundPlaying(SOUND_SPEED_UP);
			//SoundManagerSingleton.getSingletonObject().playSound(SOUND_SPEED_UP);
			//TODO: make the sound only play if the previous instance has stopped playing
		}
		
	}//increaseForwardVelocity

	/**
	 * This unlocks avatar velocity change again. Call this when the player
	 * lets go of the accelerator (e.g. up arrow)
	 */
	public void enableVelocityChange(){
		
		_canChangeVelocity = true;
		
	}//enableVelocityChange
	
	
	// damage method
	public void damage(int damageDone){

		// 0 DAMAGE //
		if(damageDone == 0){
			//nothing for now
		}
		// + DAMAGE //
		else if(damageDone > 0){
		
			//if we're not already in the hit state, damage us and put in hit state
			if(_gameState == GAME_NORMAL){
			
				// decrement health by damage done
				_health -= damageDone;
				//set our healthbar state
				_healthBar.setHealthBarStateFromHealth(_health);
				
				// If health is greater than 0, change to GAME_HIT state
				// and draw damage animation
				if (_health > 0){
					
					//TODO: make damage states for flying straight, and rolling left and right - required??
					_effectAnimationState = ANIM_EFFECT_DAMAGE;
					super.setCurrentEffectAnimation(ANIM_EFFECT_DAMAGE);
					super.setEffectAnimationLoopCount(3);
					super.startEffectAnimation();
					_gameState = GAME_HIT;
					//Reset hit timer to start counting now that we're hit
					_hitTimer.reset();

					// play our sound!
					SoundManagerSingleton.getSingletonObject().playSound(SOUND_HURT);
					
				}
				
			}//GAMESTATE_NORMAL

		}
		// - DAMAGE //
		else if(damageDone < 0){
			
		}
	
	}//damage
	
	public void heal(int healAmount){
		_health += healAmount;
		_healthBar.setHealthBarStateFromHealth(_health);
		//sm2.playSoundAtIndex(SOUND_HEAL);
		//_soundSystem.quickPlay(true, "sound_sfx_avatar_heal.wav", false, 0, 0, 0, SoundSystemConfig.ATTENUATION_NONE, 0.0f);
		//sm2.playSound("sound_sfx_avatar_heal.wav");
		SoundManagerSingleton.getSingletonObject().playSound(SOUND_HEAL);
	}

	public void reload(int ammoAmount, int weaponIndex){
		//TODO: should check bounds here
		_weapons[weaponIndex].increaseAmmoBy(ammoAmount);
		//sm2.playSoundAtIndex(SOUND_HEAL);
		//_soundSystem.quickPlay(true, "sound_sfx_avatar_heal.wav", false, 0, 0, 0, SoundSystemConfig.ATTENUATION_NONE, 0.0f);
		//sm2.playSound("sound_sfx_avatar_heal.wav");
		SoundManagerSingleton.getSingletonObject().playSound(SOUND_HEAL);
	}
	
	public boolean isDead(){
		return (_health <= 0);
	}
	
	/**
	 * The logical stuff that happens every frame
	 */
	public void process(long elapsedTime){
		
		//TODO: put movement handling stuff here?

		
		//update all our timers
		//TODO: make a cleaner "STATE" class with timers built in?
		_hitTimer.update(elapsedTime);
		_dyingTimer.update(elapsedTime);
		_weaponTimer.update(elapsedTime);
		_weaponSwitchTimer.update(elapsedTime);
		_velocityChangeTimer.update(elapsedTime);

		// If our health is out, check our state
		if (_health <= 0){
		
			// If avatar is normal, set to dying state and animation
			if (_gameState == GAME_NORMAL){

				_effectAnimationState = ANIM_EFFECT_DYING;
				super.setCurrentEffectAnimation(ANIM_EFFECT_DYING);
				super.setEffectAnimationLoopCount(100);
				super.startEffectAnimation();

				// set avatar game state to dying and reset the dying timer
				_gameState = GAME_DYING;
				_dyingTimer.reset();
				
				//sm2.playSoundAtIndex(0);
				//_soundSystem.quickPlay(true, "sound_sfx_avatar_death.wav", false, 0, 0, 0, SoundSystemConfig.ATTENUATION_NONE, 0.0f);
				//sm2.playSound("sound_sfx_avatar_death.wav");
				SoundManagerSingleton.getSingletonObject().playSound(SOUND_DIE);

				
			}
			// if we are in dying state
			else if(_gameState == GAME_DYING){
				
				//if we're done dying (lol), then reset our avatar and take one life
				if(_dyingTimer.isDone()){
					
					_lives--;
					_health = OtherStuff.AVATAR_DEFAULT_HEALTH;
					_gameState = GAME_NORMAL;
					
					_effectAnimationState = ANIM_EFFECT_NONE;
					super.setCurrentEffectAnimation(ANIM_EFFECT_NONE);
					super.setEffectAnimationLoopCount(1);
					super.startEffectAnimation();
					
					this.ressurect();
					
				}
			}
		
		}//ifNoHealth
		
		// If avatar is not in a normal state (say he is being damaged or is dying
		if (_gameState != GAME_NORMAL){
			
			// If the avatar has been in a GAME_HIT state for the required time
			// then set the state back to normal
			if ((_hitTimer.isDone()) && (_gameState != GAME_DYING)){
				
				_gameState = GAME_NORMAL;
				
				// set animation back to normal
				_effectAnimationState = ANIM_EFFECT_NONE;
				super.setCurrentEffectAnimation(ANIM_EFFECT_NONE);
				super.setEffectAnimationLoopCount(1);
				super.startEffectAnimation();
	
			}
			
		}//ifAbnormalState
		
	}//process
	
	public void ressurect(){
		
		super.setX(OtherStuff.AVATAR_START_X);
		super.setY(OtherStuff.AVATAR_START_Y);
		setHealth(OtherStuff.AVATAR_DEFAULT_HEALTH);
		_healthBar.setHealthBarStateFromHealth(_health);
		
	}
	
	//TODO: add weapon system for more than one weapon, current weapon, fire rate, ammo left, etc.
	public void fire(LevelLayer layer, int targetX, int targetY) {

		// TODO: fire from actual weapon hardpoint. For the test, just fire from the x,y
		
		if(_weaponTimer.isDone()){
			
			//TODO: do I need to care that I'm coding in the speed difference between avatar and projectile?

			// If we have ammo and are not dying
			if(_weapons[_currentWeapon].getAmmoLeft() > 0 && !isDeadOrDying()){
				
				switch(_currentWeapon){
				
					case WEAPON_MISSILES: // SPREAD MODE TEST
						layer.addNewThing(new Projectile(this.getX(),this.getY()-33,this.getVX(), -(this.getVY()+3), 1, 0));
						layer.getThings()[layer.getThings().length-1].setLifeState(Thing.LIFE_STATE_LIVED);
						//off a little to the right (slope of 2 to 1)
						layer.addNewThing(new Projectile(this.getX(),this.getY()-33,this.getVX(), -(this.getVY()+2), 2, 1));
						layer.getThings()[layer.getThings().length-1].setLifeState(Thing.LIFE_STATE_LIVED);
						//off a little more to the right
						layer.addNewThing(new Projectile(this.getX(),this.getY()-33,this.getVX(), -(this.getVY()+1), 1, 1));
						layer.getThings()[layer.getThings().length-1].setLifeState(Thing.LIFE_STATE_LIVED);
						//of a little to the left (slope of 2 to 1, with negative x velocity)
						layer.addNewThing(new Projectile(this.getX(),this.getY()-33,-this.getVX(), -(this.getVY()+2), 2, 1));
						layer.getThings()[layer.getThings().length-1].setLifeState(Thing.LIFE_STATE_LIVED);
						//of a little more to the left
						layer.addNewThing(new Projectile(this.getX(),this.getY()-33,-this.getVX(), -(this.getVY()+1), 1, 1));
						layer.getThings()[layer.getThings().length-1].setLifeState(Thing.LIFE_STATE_LIVED);
						_weapons[_currentWeapon].decreasedAmmoBy(5);
						SoundManagerSingleton.getSingletonObject().playSound(SOUND_FIRE_MISSILE);
						break;
					case WEAPON_BULLETS:
						layer.addNewThing(new Bullet_50mm(this.getX() + 2, this.getY() + 8, 0,-(this.getVY()+3), 0, 0));
						layer.getThings()[layer.getThings().length-1].setLifeState(Thing.LIFE_STATE_LIVED);
						layer.addNewThing(new Bullet_50mm(this.getX() + 30, this.getY() + 8, 0,-(this.getVY()+3), 0, 0));
						layer.getThings()[layer.getThings().length-1].setLifeState(Thing.LIFE_STATE_LIVED);
						_weapons[_currentWeapon].decreasedAmmoBy(2);
						//SoundManagerSingleton.getSingletonObject().playSound(SOUND_FIRE_BULLET);
						SoundManagerSingleton.getSingletonObject().playSound(SOUND_FIRE_50MM_BURST);
						break;
					default:
						//TODO: dead man's click :) - or perhaps some kind of auto-switch to other weapon, or something
						break;

				}//switch
				
			}else{
				
				//TODO: dead man's click
				
			}//else
			
			//after we fire, reset our weapon timer so we can just fire a million bullets
			_weaponTimer.reset();
			_shotsFired++;
			
			//TODO: play weapon specific sounds	
			//SoundManagerSingleton.getSingletonObject().playSound(SOUND_FIRE);

		}		
	
	}//fire
	
	/**
	 * A small test to see if we can add state-like weapon behaviour. In this case, once I
	 * stop holding the fire button, I want to kill all my gun sound threads
	 */
	public void ceaseFire(){
		
		
	}//ceaseFire
	
	//motions and animations and stuff...testing
	public void rollRight(){
		
		if(!isDeadOrDying()){
		
			_mainAnimationState = ANIM_ROLL_RIGHT;
			super.setCurrentAnimation(ANIM_ROLL_RIGHT);
			super.setAnimationLoopCount(1);
			super.startAnimation();
			
			_shadowAnimationState = ANIM_ROLL_RIGHT;
			super.setCurrentShadowAnimation(ANIM_ROLL_RIGHT);
			super.setShadowAnimationLoopCount(1);
			super.startShadowAnimation();
		
		}
		
	}//rollRight

	//motions and animations and stuff...testing
	public void rollLeft(){
		
		if(!isDeadOrDying()){
		
			_mainAnimationState = ANIM_ROLL_LEFT;
			super.setCurrentAnimation(ANIM_ROLL_LEFT);
			super.setAnimationLoopCount(1);
			super.startAnimation();
			
			_shadowAnimationState = ANIM_ROLL_LEFT;
			super.setCurrentShadowAnimation(ANIM_ROLL_LEFT);
			super.setShadowAnimationLoopCount(1);
			super.startShadowAnimation();
		
		}
		
	}//rollLeft
	
	public void flyStraight(){
		
		//then get index to our fly straight animation
		_mainAnimationState = ANIM_FLY_STRAIGHT;
		super.setCurrentAnimation(ANIM_FLY_STRAIGHT);
		super.startAnimation();
		
		_shadowAnimationState = ANIM_FLY_STRAIGHT;
		super.setCurrentShadowAnimation(ANIM_FLY_STRAIGHT);
		super.startShadowAnimation();
		
	}//flyStraight

	/**
	 * This method switches to our next weapon in the weapon array
	 * and resets the weapon timer to be that of the newly selected
	 * weapon
	 */
	public void nextWeapon(){
		
		// Cleverly (??) reuse the weapon timer to align switching from a weapon similar to firing it.
		// This means that the clumsy BFG-type weapon will take a while to switch from, and perhaps
		// deter the player from making it their go to firefight weapon, etc.
		if(_weaponSwitchTimer.isDone()){
		
			//update our index and wrap to zero if we are over the end of the array
			_currentWeapon = ( ((_currentWeapon + 1) < _weapons.length) ? _currentWeapon + 1 : 0 );
			
			// then reset our weapon timer for the new weapon
			_weaponTimer = new Timer(_weapons[_currentWeapon].getFireDelay());
			_weaponTimer.reset();
			
			_weaponSwitchTimer.reset();
			
		}//if
		
	}//nextWeapon
	
	public void initSounds(){
		
		SoundManagerSingleton.getSingletonObject().loadSound(SOUND_HURT);
		SoundManagerSingleton.getSingletonObject().loadSound(SOUND_DIE); 
		SoundManagerSingleton.getSingletonObject().loadSound(SOUND_FIRE_BULLET); 
		SoundManagerSingleton.getSingletonObject().loadSound(SOUND_FIRE_MISSILE);
		SoundManagerSingleton.getSingletonObject().loadSound(SOUND_FIRE_50MM_BURST);
		//SoundManagerSingleton.getSingletonObject().loadSound(SOUND_SPEED_UP, SOUND_SPEED_UP);
		SoundManagerSingleton.getSingletonObject().loadSound(SOUND_SPEED_UP);
		SoundManagerSingleton.getSingletonObject().loadSound(SOUND_SLOW_DOWN);
		
	}
	
	private boolean isDeadOrDying(){
		return (_gameState == GAME_DEAD || _gameState == GAME_DYING);
	}
	
}//Avatar
