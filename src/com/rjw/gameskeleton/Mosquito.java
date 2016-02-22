package com.rjw.gameskeleton;

import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;

//import com.rjw.sfx.SoundManager_Deprecated;
import com.rjw.sfx.SoundManagerSingleton;

/**
 * Our Mosquito enemy. very fast and small, but very weak and doesnt do much damage. These should swarm in the dozens.
 * @author rwalsh
 *
 */
public class Mosquito extends Thing{

	private int _vx = 0;
	private int _vy = 0;
	private int _health;
	private int _lives;
	private BufferedImage _frameTile;
	private BufferedImage _effectsTile;
	private int _mainAnimationState = 0;
	private int _effectAnimationState = 0;
	private int _gameState = 0;
	
	// our hardpoint to fire small orb from
	private int _hardPoint_1_OffsetX = 1;
	private int _hardPoint_1_OffsetY = 8;
	private int _hardPoint_2_OffsetX = 6;
	private int _hardPoint_2_OffsetY = 8;
	
	//Timers
	Timer _hitTimer;
	Timer _dyingTimer;
	Timer _weaponTimer;
	
	//store our default movement speeds
	public static final int VELOCITY_Y = 1;
	public static final int VELOCITY_X = 0;
	public static final int WEAPON_TIMER = 500;
	public static final int DYING_TIMER = 1500;
	public static final int HIT_TIMER = 500;
	public static final int ORB_SPEED = 2;
	static final int MOSQUITO_SIZE = 8;
	static final int MOSQUITO_HEALTH = 25;

	// animation states
	static final int ANIM_FLY_STRAIGHT = 0;
	//static final int ANIM_ROLL_RIGHT = 1;
	//static final int ANIM_ROLL_LEFT = 2;
	
	//effect animation states
	static final int ANIM_EFFECT_NONE = 0;
	static final int ANIM_EFFECT_DAMAGE = 1;
	static final int ANIM_EFFECT_DYING = 2;
	
	// game states
	static final int GAME_NORMAL = 0;
	static final int GAME_HIT = 1;
	static final int GAME_DYING = 2;
	static final int GAME_DEAD = 3;
	
	
	//TODO: make weapon sounds come from weapon
	static final String SOUND_FIRE = "sound_sfx_badguy_fire.wav";
	static final String SOUND_DIE = "sound_sfx_badguy_die.wav";
	static final String SOUND_COUNT = "sound_sfx_deleteme.wav";
	
	//sm2.loadSound("sounds/sound_sfx_badguy_fire.wav");
	//sm2.loadSound("sounds/sound_sfx_badguy_die.wav");
	//sm2.loadSound("sounds/sound_sfx_deleteme.wav");
	
	//Sound Manager
	//SoundManager sm2;

	/**
	 * 
	 * @param x - x pixel position
	 * @param y - y pixel position
	 * @param vx - x velocity
	 * @param vy - y velocity
	 */
	public Mosquito(int x, int y, int vx, int vy){
		
		super(ThingManager.THING_ID_MOSQUITO, x, y, MOSQUITO_SIZE, MOSQUITO_SIZE, new Animation(), new Animation(), new Animation());
		
		_vx = vx;
		_vy = vy;
		_health = MOSQUITO_HEALTH;
		_mainAnimationState = ANIM_FLY_STRAIGHT;
		_effectAnimationState = ANIM_EFFECT_NONE;
		_gameState = GAME_NORMAL;
		
		// and set timers
		_hitTimer = new Timer(HIT_TIMER);
		_dyingTimer = new Timer(DYING_TIMER);
		_weaponTimer = new Timer(WEAPON_TIMER);
		
		// make it collidable
		this.setCollisionsMatter(true);
		
		try{	
			_frameTile = ImageIO.read(new File(OtherStuff.SPRITE_PATH_PREFIX + OtherStuff.SPRITE_MOSQUITO));
			_effectsTile = ImageIO.read(new File(OtherStuff.SPRITE_PATH_PREFIX + OtherStuff.SPRITE_EFFECTS_001));
		}catch(Exception e){
			GameSkeleton.printDebugMessage("Error loading frames for Mosquito ("+e.getMessage()+")");
		}
		
		/////////////////////
		// MAIN ANIMATION //
		/////////////////////
		super.addAnimFrame(_frameTile.getSubimage(0, 0, 8, 8), 250);
		super.addAnimFrame(_frameTile.getSubimage(8, 0, 8, 8), 250);
		
		//TODO: make smaller effects images since this enemy is so small!
		
		///////////////////////
		// EFFECT ANIMATIONS //
		///////////////////////
		// Add frames for our default effect animation (i.e. NOTHING!)
		super.addEffectAnimFrame(_effectsTile.getSubimage(0,0,MOSQUITO_SIZE,MOSQUITO_SIZE), 0);
		
		// index to our damage effect, and create it as fireballs
		super.addEffectAnimation(new Animation());
		super.setCurrentEffectAnimation(ANIM_EFFECT_DAMAGE);
		super.addEffectAnimFrame(_effectsTile.getSubimage(MOSQUITO_SIZE,0,MOSQUITO_SIZE,MOSQUITO_SIZE),250);
		super.addEffectAnimFrame(_effectsTile.getSubimage(2*MOSQUITO_SIZE,0,MOSQUITO_SIZE,MOSQUITO_SIZE),250);
		
		// and our dying effect
		super.addEffectAnimation(new Animation());
		super.setCurrentEffectAnimation(ANIM_EFFECT_DYING);
		super.addEffectAnimFrame(_effectsTile.getSubimage(3*MOSQUITO_SIZE,0,MOSQUITO_SIZE,MOSQUITO_SIZE),250);
		super.addEffectAnimFrame(_effectsTile.getSubimage(4*MOSQUITO_SIZE,0,MOSQUITO_SIZE,MOSQUITO_SIZE),250);
		super.addEffectAnimFrame(_effectsTile.getSubimage(5*MOSQUITO_SIZE,0,MOSQUITO_SIZE,MOSQUITO_SIZE),250);
		super.addEffectAnimFrame(_effectsTile.getSubimage(6*MOSQUITO_SIZE,0,MOSQUITO_SIZE,MOSQUITO_SIZE),250);
		

		_gameState = GAME_NORMAL;
		_mainAnimationState = ANIM_FLY_STRAIGHT;
		super.setCurrentAnimation(ANIM_FLY_STRAIGHT);
		super.startAnimation();
		_effectAnimationState = ANIM_EFFECT_NONE;
		super.setCurrentEffectAnimation(ANIM_EFFECT_NONE);
		super.startEffectAnimation();
		
	}//constructor
	
	public int getVX(){ return _vx; }
	public int getVY(){ return _vy;	}
	public int getLives(){ return _lives; }
	public int getHealth() { return _health; }
	public int getMainAnimationState() { return _mainAnimationState; }
	public int getEffectAnimationState() { return _effectAnimationState; }
	public int getGameState() { return _gameState; }
	
	public void setLives(int newLives){ _lives = newLives; }
	public void setHealth(int newHealth) { _health = newHealth; }
	public void setState(int newState) { _mainAnimationState = newState; }
	
	public void damage(int damageDone) {

		// 0 DAMAGE //
		if(damageDone == 0){
			
		}
		// + DAMAGE //
		else if(damageDone > 0){
		
			//if we're not already in the hit state, damage us and put in hit state
			if(_gameState == GAME_NORMAL){
			
				// decrement health by damage done
				_health -= damageDone;
				
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
					
				}
				
			}//GAMESTATE_NORMAL

		}
		// - DAMAGE //
		else if(damageDone < 0){
			
		}

	}//damage

	/**
	 * our bad guy's fire method
	 */
	public void fire(LevelLayer layer, int targetX, int targetY){
		
		// if allowed to fire and the avatar is still below bad guy (as they shouldn't shoot backwards 
		if(_weaponTimer.isDone() && (targetY > this.getY())){
			
			// launch a new 2x2 orb at our target, at a rate of ORB_PIXELS_PER_SECOND 
			layer.addNewThing(new OrbProjectile2x2( 
									this.getX() + _hardPoint_1_OffsetX,
									this.getY() + _hardPoint_1_OffsetX,
									(int)((targetX - this.getX()) / ((float) Math.max(1,Math.sqrt((targetX - this.getX())*(targetX - this.getX()) + (targetY - this.getY())*(targetY - this.getY()))))*OrbProjectile8x8.SPEED_PIXELS_PER_SECOND),
									(int)((targetY - this.getY()) / ((float) Math.max(1,Math.sqrt((targetX - this.getX())*(targetX - this.getX()) + (targetY - this.getY())*(targetY - this.getY()))))*OrbProjectile8x8.SPEED_PIXELS_PER_SECOND)
							  ));
			
			layer.getThings()[layer.getThings().length-1].setLifeState(Thing.LIFE_STATE_LIVED);
			
			// and launch a second one from the second hardpoint
			layer.addNewThing(new OrbProjectile2x2( 
					this.getX() + _hardPoint_2_OffsetX,
					this.getY() + _hardPoint_2_OffsetX,
					(int)((targetX - this.getX()) / ((float) Math.max(1,Math.sqrt((targetX - this.getX())*(targetX - this.getX()) + (targetY - this.getY())*(targetY - this.getY()))))*OrbProjectile8x8.SPEED_PIXELS_PER_SECOND),
					(int)((targetY - this.getY()) / ((float) Math.max(1,Math.sqrt((targetX - this.getX())*(targetX - this.getX()) + (targetY - this.getY())*(targetY - this.getY()))))*OrbProjectile8x8.SPEED_PIXELS_PER_SECOND)
			  ));

			layer.getThings()[layer.getThings().length-1].setLifeState(Thing.LIFE_STATE_LIVED);

			// reset timer and play sound
			_weaponTimer.reset();	
			//sm2.playSoundAtIndex(0);
			//sm2.playSound(SOUND_FIRE);
			SoundManagerSingleton.getSingletonObject().playSound(SOUND_FIRE);
			
			
			
		}
		
	}//fire

	/**
	 * 
	 */
	public void process(long elapsedTime) {

		_weaponTimer.update(elapsedTime);
		_hitTimer.update(elapsedTime);
		_dyingTimer.update(elapsedTime);
		
		// only process the bad guy if he's on screen (prevents all bad guys from
		// moving down screen which would leave the top of the level empty
		if(this.getY() > -(this.getH())){
			
			// if we haven't initialized, then do that
			if(this.getLifeState() != LIFE_STATE_LIVED){
				this.setReadyToFire(true);
				this.setLifeState(LIFE_STATE_LIVED);
			}
			
			//update position
			this.setY(this.getY() + this.getVY());
			this.setX(this.getX() + this.getVX());
			
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
					
					// TODO: play some sounds and stuff?
					//sm2.playSoundAtIndex(1);
					//sm2.playSound(SOUND_DIE);
					SoundManagerSingleton.getSingletonObject().playSound(SOUND_DIE);
					
				}
				// if we are in dying state
				else if(_gameState == GAME_DYING){
					
					// If we're done dying (lol), then set our bad guy's life state to dead
					// so the cleanup function will get rid of the dead guy
					if(_dyingTimer.isDone()){
						
						// play death sound and set to dead
						//sm2.playSoundAtIndex(1);
						this.setLifeState(LIFE_STATE_DEAD_AND_GONE);
						
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

			
			// Finally, check if the bad guy has gone off the screen. If it 
			// has, then set the life state to dead 
			if(
					(this.getY() > OtherStuff.SCREEN_HEIGHT + OtherStuff.TILE_SIZE)
					&& (this.getLifeState() == Thing.LIFE_STATE_LIVED)
			){
				this.setLifeState(Thing.LIFE_STATE_DEAD_AND_GONE);
			}
			
			
		}//Y>0
		
	}//process


	
}//Mosquito
