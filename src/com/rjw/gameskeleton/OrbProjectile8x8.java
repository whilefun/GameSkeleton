package com.rjw.gameskeleton;

import java.io.File;

import javax.imageio.ImageIO;

public class OrbProjectile8x8 extends OrbProjectile{

	public static final int ORB_PROJECTILE_8_X_8_W = 8;
	public static final int ORB_PROJECTILE_8_X_8_H = 8;
	public static final int SPEED_PIXELS_PER_SECOND = 5;
	
	public OrbProjectile8x8(int x, int y, int vx, int vy){
		
		super(ThingManager.THING_ID_ORB_PROJECTILE_8_X_8, ORB_PROJECTILE_8_X_8_W, ORB_PROJECTILE_8_X_8_H, x, y, vx, vy);
		
	}//constructor

	@Override
	public void initAnimationFrames() {
	
		try{	
			_frameTile = ImageIO.read(new File(OtherStuff.SPRITE_PATH_PREFIX + OtherStuff.SPRITE_ORB_PROJECTILE_8_X_8));
		}catch(Exception e){
			GameSkeleton.printDebugMessage("Error loading frames for OrbProjectile8x8 ("+e.getMessage()+")");
		}
		// our orb is 8x8
		super.addAnimFrame(_frameTile.getSubimage(0, 0, 8, 8), 500);
		super.addAnimFrame(_frameTile.getSubimage(8, 0, 8, 8), 500);
		
	}//initAnimationFrames
	
}//SmallBlueOrbProjectile
