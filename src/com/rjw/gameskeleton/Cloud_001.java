package com.rjw.gameskeleton;

import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

public class Cloud_001 extends Thing {

	//default speeds and stuff
	public static final int VELOCITY_Y = 1;
	public static final int VELOCITY_X = 0;
	
	private BufferedImage _frameTile;
	private int _vx = 0;
	private int _vy = 0;
	// rise and run so we can have angled trajectories :D
	private int _rise = 0;
	private int _run = 0;
	private long _slopeFactor = 0;
	private long _slopeCounter = 0;
	
	/**
	 * @param x and y are start locations , i.e."firing from points"
	 * @param vy and vx are the x and y speeds
	 * @param rise and run are the top down 2D "slope" or angle of the trajectory relative to the firing point
	 */
	public Cloud_001(int x, int y, int vx, int vy, int rise, int run){
		
		
		super(ThingManager.THING_ID_CLOUD_001, x, y, 128, 64, new Animation());
		
		_vx = vx;
		_vy = vy;
		_rise = rise;
		_run = run;
		
		try{	
			_frameTile = ImageIO.read(new File(OtherStuff.SPRITE_PATH_PREFIX + OtherStuff.SPRITE_CLOUD_TEST));
		}catch(Exception e){
			GameSkeleton.printDebugMessage("error loading frames for projectile ("+e.getMessage()+")");
		}
		// our orb is 8x8
		super.addAnimFrame(_frameTile.getSubimage(0, 0, 128, 64), 500);
		
		this.setCollisionsMatter(true);
		
	}//Orb
	
	public int getVX(){ return _vx; }
	public int getVY(){ return _vy;	}
	
	public void damage(int damageDone) {
		// TODO Auto-generated method stub
	}

	public void fire(LevelLayer layer, int targetX, int targetY){
		// does nothing
	}
	
	public void process(long elapsedTime) {
		
		this.setX( this.getX() + this.getVX() );
		this.setY( this.getY() + this.getVY() );
		
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
	
}//Cloud_001
