package com.rjw.gameskeleton;

import java.awt.image.BufferedImage;

public abstract class OrbProjectile extends Thing{
	
	//default speeds and stuff
	protected BufferedImage _frameTile;
	private int _vx = 0;
	private int _vy = 0;
	
	/**
	 * @param x and y are start locations , i.e."firing from points"
	 * @param vy and vx are the x and y speeds
	 * @param rise and run are the top down 2D "slope" or angle of the trajectory relative to the firing point
	 */
	public OrbProjectile(int ObjectID, int w, int h, int x, int y, int vx, int vy){
		
		super(ObjectID, x, y, w, h, new Animation());
		
		_vx = vx;
		_vy = vy;
		
		// set our orb projectile's width and height
		this.setW(w);
		this.setH(h);
		
		this.setCollisionsMatter(true);		
		
		initAnimationFrames();

	}//OrbProjectile

	public abstract void initAnimationFrames();
	
	
	public int getVX(){ return _vx; }
	public int getVY(){ return _vy;	}
	
	public void damage(int damageDone) {
		// TODO Auto-generated method stub
	}

	public void fire(LevelLayer layer, int targetX, int targetY){
		// does nothing
	}
	
	/**
	 * Processes our OrbProjectile
	 */
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
	
}//OrbProjectile
