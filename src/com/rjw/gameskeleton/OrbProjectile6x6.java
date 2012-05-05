package com.rjw.gameskeleton;

import java.io.File;

import javax.imageio.ImageIO;

public class OrbProjectile6x6 extends OrbProjectile{

	public static final int ORB_PROJECTILE_6_X_6_W = 6;
	public static final int ORB_PROJECTILE_6_X_6_H = 6;
	public static final int SPEED_PIXELS_PER_SECOND = 5;
	
	public OrbProjectile6x6(int x, int y, int vx, int vy){
		
		super(ThingManager.THING_ID_ORB_PROJECTILE_6_X_6, ORB_PROJECTILE_6_X_6_W, ORB_PROJECTILE_6_X_6_H, x, y, vx, vy);
		
	}//constructor

	@Override
	public void initAnimationFrames() {
	
		try{	
			_frameTile = ImageIO.read(new File(OtherStuff.SPRITE_PATH_PREFIX + OtherStuff.SPRITE_ORB_PROJECTILE_6_X_6));
		}catch(Exception e){
			GameSkeleton.printDebugMessage("Error loading frames for OrbProjectile6x6 ("+e.getMessage()+")");
		}
		// our orb is 6x6
		super.addAnimFrame(_frameTile.getSubimage(0, 0, 6, 6), 300);
		super.addAnimFrame(_frameTile.getSubimage(6, 0, 6, 6), 300);
		
	}//initAnimationFrames
	
}//SmallBlueOrbProjectile
