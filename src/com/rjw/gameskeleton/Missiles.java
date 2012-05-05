package com.rjw.gameskeleton;

import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

public class Missiles extends Thing{

	//default speeds and stuff
	public static final int VELOCITY_Y = 5;
	public static final int VELOCITY_X = 5;
	// a default value for Missiles to give back to avatar
	public static final int AMMO_COUNT = 50;
	
	private BufferedImage _frameTile;
	private int _vx = 0;
	private int _vy = 0;
	private int _ammoCount = 0;
	
	public static final int Missiles_W = 32;
	public static final int Missiles_H = 32;
	
	/**
	 * @param x and y are start locations , i.e."firing from points"
	 * @param vy and vx are the x and y speeds
	 * @param rise and run are the top down 2D "slope" or angle of the trajectory relative to the firing point
	 */
	public Missiles(int x, int y, int vx, int vy, int rise, int run, int healAmount){
		
		super(ThingManager.THING_ID_MISSILES, x, y, Missiles_W, Missiles_H, new Animation());
		
		_vx = vx;
		_vy = vy;
		_ammoCount = healAmount;
		
		// set our orb's width and height
		this.setW(Missiles_W);
		this.setH(Missiles_H);
		
		try{	
			_frameTile = ImageIO.read(new File(OtherStuff.SPRITE_PATH_PREFIX + OtherStuff.SPRITE_MISSILES_01));
		}catch(Exception e){
			GameSkeleton.printDebugMessage("Error loading frames for Missiles ("+e.getMessage()+")");
		}
		// our Missiles is 32x32
		super.addAnimFrame(_frameTile.getSubimage(0, 0, Missiles_W, Missiles_H), 200);
		super.addAnimFrame(_frameTile.getSubimage(Missiles_H*1, 0, Missiles_W, Missiles_H), 200);
		super.addAnimFrame(_frameTile.getSubimage(Missiles_H*2, 0, Missiles_W, Missiles_H), 200);
		super.addAnimFrame(_frameTile.getSubimage(Missiles_H*3, 0, Missiles_W, Missiles_H), 200);
		
		this.setCollisionsMatter(true);
		
	}//Orb
	
	public int getVX(){ return _vx; }
	public int getVY(){ return _vy;	}
	public int getAmmoCount(){ return _ammoCount;	}
	
	
	
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
	
}//Missiles

