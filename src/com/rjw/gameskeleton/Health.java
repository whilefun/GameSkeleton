package com.rjw.gameskeleton;

import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

public class Health extends Thing{

	//default speeds and stuff
	public static final int VELOCITY_Y = 5;
	public static final int VELOCITY_X = 5;
	// a default value for health to give back to avatar
	public static final int HEAL_AMOUNT = 50;
	
	private BufferedImage _frameTile;
	private int _vx = 0;
	private int _vy = 0;
	// rise and run so we can have angled trajectories :D
	private int _rise = 0;
	private int _run = 0;
	private long _slopeFactor = 0;
	private long _slopeCounter = 0;
	private int _healAmount = 0;
	
	public static final int HEALTH_W = 32;
	public static final int HEALTH_H = 32;
	
	/**
	 * @param x and y are start locations , i.e."firing from points"
	 * @param vy and vx are the x and y speeds
	 * @param rise and run are the top down 2D "slope" or angle of the trajectory relative to the firing point
	 */
	public Health(int x, int y, int vx, int vy, int rise, int run, int healAmount){
		
		super(ThingManager.THING_ID_HEALTH, x, y, HEALTH_W, HEALTH_H, new Animation());
		
		_vx = vx;
		_vy = vy;
		_rise = rise;
		_run = run;
		_healAmount = healAmount;
		
		// set our orb's width and height
		//this.setW(HEALTH_W);
		//this.setH(HEALTH_H);
		
		try{	
			_frameTile = ImageIO.read(new File(OtherStuff.SPRITE_PATH_PREFIX + OtherStuff.SPRITE_HEALTH_01));
		}catch(Exception e){
			GameSkeleton.printDebugMessage("Error loading frames for Health ("+e.getMessage()+")");
		}
		// our health is 32x32
		super.addAnimFrame(_frameTile.getSubimage(0, 0, HEALTH_W, HEALTH_H), 200);
		super.addAnimFrame(_frameTile.getSubimage(HEALTH_H*1, 0, HEALTH_W, HEALTH_H), 200);
		super.addAnimFrame(_frameTile.getSubimage(HEALTH_H*2, 0, HEALTH_W, HEALTH_H), 200);
		super.addAnimFrame(_frameTile.getSubimage(HEALTH_H*3, 0, HEALTH_W, HEALTH_H), 200);
		
		this.setCollisionsMatter(true);
		
	}//Orb
	
	public int getVX(){ return _vx; }
	public int getVY(){ return _vy;	}
	public int getHealAmount(){ return _healAmount;	}
	
	
	
	public void damage(int damageDone) {
		// does nothing
	}

	public void fire(LevelLayer layer, int targetX, int targetY){
		// does nothing
	}
	
	public void process(long elapsedTime) {
		
		//TODO: Move this logic to a more generic method in Thing?
		
		// The last thing we're going to do is check if the projectile 
		// has gone off the screen. If it has, we can kill it. We know
		// since it is a projectile, it's useful gameplay life is only
		// in the space where the avatar is, or perhaps where on screen
		// enemies are. So let's leave a bit of a threshold around the
		// screen for a buffer to make the deletion smoother
		if(
				((this.getX() < -OtherStuff.TILE_SIZE) || 
				(this.getX() > OtherStuff.SCREEN_WIDTH + OtherStuff.TILE_SIZE) || 
				(this.getY() < -OtherStuff.TILE_SIZE) || 
				(this.getY() > OtherStuff.SCREEN_HEIGHT + OtherStuff.TILE_SIZE))
				&& (this.getLifeState() == Thing.LIFE_STATE_LIVED)
				
		){
			this.setLifeState(Thing.LIFE_STATE_DEAD_AND_GONE);
		}//if

	}//process
	
}//Health

