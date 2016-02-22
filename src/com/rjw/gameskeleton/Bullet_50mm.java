package com.rjw.gameskeleton;

import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

// a projectile class
public class Bullet_50mm extends Thing {

	private BufferedImage _frameTile;
	private int _vx = 0;
	private int _vy = 0;
	// rise and run so we can have angled trajectories :D
	private int _rise = 0;
	private int _run = 0;
	private long _slopeFactor = 0;
	private long _slopeCounter = 0;
	
	public static final int BULLET_50MM_W = 4;
	public static final int BULLET_50MM_H = 8;
	
	/**
	 * @param x and y are start locations , i.e."firing from points"
	 * @param vy and vx are the x and y speeds
	 * @param rise and run are the top down 2D "slope" or angle of the trajectory relative to the firing point
	 */
	public Bullet_50mm(int x, int y, int vx, int vy, int rise, int run){
		
		super(ThingManager.THING_ID_BULLET_50MM, x, y, BULLET_50MM_W, BULLET_50MM_H,  new Animation());
		
		_vx = vx;
		_vy = vy;
		_rise = rise;
		_run = run;
		
		// set our projectile's width and height
		this.setW(BULLET_50MM_W);
		this.setH(BULLET_50MM_H);
		
		try{	
			_frameTile = ImageIO.read(new File(OtherStuff.SPRITE_PATH_PREFIX + OtherStuff.SPRITE_BULLET_50MM));
		}catch(Exception e){
			GameSkeleton.printDebugMessage("error loading frames for projectile ("+e.getMessage()+")");
		}
		
		// our projectile is 16x16
		super.addAnimFrame(_frameTile.getSubimage(0, 0, 4, 8), 100);
		
		this.setCollisionsMatter(true);
		
	}//constructor
	
	public int getVX(){ return _vx; }
	public int getVY(){ return _vy;	}
	
	public void damage(int damageDone) {
		// TODO Auto-generated method stub
	}
	
	public void fire(LevelLayer layer, int targetX, int targetY){
		// does nothing
	}

	// our 50mm bullet only travels straight
	public void process(long elapsedTime) {

		this.setX(this.getX() + this.getVX());
		this.setY(this.getY() + this.getVY());
		
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
		}

	}//process

}//Projectile
