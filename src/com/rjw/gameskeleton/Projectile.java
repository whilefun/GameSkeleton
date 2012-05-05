package com.rjw.gameskeleton;

import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

// a projectile class
public class Projectile extends Thing {

	private BufferedImage _frameTile;
	private int _vx = 0;
	private int _vy = 0;
	// rise and run so we can have angled trajectories :D
	private int _rise = 0;
	private int _run = 0;
	private long _slopeFactor = 0;
	private long _slopeCounter = 0;
	
	public static final int PROJECTILE_W = 16;
	public static final int PROJECTILE_H = 16;
	
	/**
	 * @param x and y are start locations , i.e."firing from points"
	 * @param vy and vx are the x and y speeds
	 * @param rise and run are the top down 2D "slope" or angle of the trajectory relative to the firing point
	 */
	public Projectile(int x, int y, int vx, int vy, int rise, int run){
		
		super(ThingManager.THING_ID_PROJECTILE, x, y, PROJECTILE_W, PROJECTILE_H, new Animation());
		
		_vx = vx;
		_vy = vy;
		_rise = rise;
		_run = run;
		
		// set our projectile's width and height
		this.setW(PROJECTILE_W);
		this.setH(PROJECTILE_H);
		
		try{	
			_frameTile = ImageIO.read(new File(OtherStuff.SPRITE_PATH_PREFIX + OtherStuff.SPRITE_PROJECTILE_TEST));
		}catch(Exception e){
			GameSkeleton.printDebugMessage("error loading frames for projectile ("+e.getMessage()+")");
		}
		
		// our projectile is 16x16
		super.addAnimFrame(_frameTile.getSubimage(0, 0, 16, 16), 300);
		super.addAnimFrame(_frameTile.getSubimage(16, 0, 16, 16), 300);
		super.addAnimFrame(_frameTile.getSubimage(32, 0, 16, 16), 300);
		super.addAnimFrame(_frameTile.getSubimage(48, 0, 16, 16), 300);
		
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

	public void process(long elapsedTime) {
		// TODO other stuff?
		
		// move the projectile
		
		// TODO: calculate the new x and y coords based on the speed and slope
		// basically, we want to move the projectile "up and over" the right 
		// amount to make the shot angled in the originally desired direction
		
		

		// rise over run is essentially "Y movement" over "X movement". Any
		// ratio of that can be used to multiply to get the "Y change" relative
		// to the "X change", or vice versa. If the slope factor (namely, the 
		// decimal representation of rise/run is 1 or more, we need to move 
		// in the X axis first, then move the "Y * slope factor" equivalent
		// in the Y axis. Conversely, if the slope factor is less than 1,
		// we need to move in the Y axis first, then move the "X * slope factor"
		// equivalent in the X axis. I think. :)

		
		// to cover divide by zero errors, and zero slope
		
		
		
		// if they are both zero, do a linear movement on vx and vy only
		if(_rise == 0 && _run == 0){

			//GameSkeleton.printDebugMessage(_rise + "," + _run);
			this.setY(this.getY() + (this.getVY() * 0));
			this.setX(this.getX() + (this.getVX() * 0));
			
		}
		//if run is zero, move vx*0, vy*1
		else if(_run == 0){
			
			this.setY(this.getY() + this.getVY() * 1);
			this.setX(this.getX() + this.getVX() * 0);
			
		}
		//if rise is zero, move vx*1, vy*0
		else if(_rise == 0){

			this.setY(this.getY() + this.getVY() * 0);
			this.setX(this.getX() + this.getVX() * 1);
			
		}
		//if rise and run are both not zero, do some tricky stuff
		else{
			
			// If the slope factor (namely, the 
			// decimal representation of rise/run is 1 or more, we need to move 
			// in the X axis first, then move the "Y * slope factor" equivalent
			// in the Y axis. Conversely, if the slope factor is less than 1,
			// we need to move in the Y axis first, then move the "X * slope factor"
			// equivalent in the X axis. I think. :)
			_slopeFactor = (_rise/_run);

			// make sure we only move the non-slope affected dimension every 
			//1/slopefactor times. In other words, if our slope is 20/1, don't
			// move 1 unit in X until you've moved 20 in Y
			if(_slopeFactor >= 1){
				this.setY(this.getY() + this.getVY());
				//_slopeCounter += _slopeFactor;
				_slopeCounter++;
				if(_slopeCounter >= _slopeFactor){
					this.setX(this.getX() + this.getVX());
					_slopeCounter = 0;
				}
			}else{
				
				this.setX(this.getX() + this.getVX());
				//_slopeCounter += _slopeFactor;
				_slopeCounter++;
				if(_slopeCounter >= _slopeFactor){
					this.setY(this.getY() + this.getVY());
					_slopeCounter = 0;
				}
				
			}//else

			
		}//else
		
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
