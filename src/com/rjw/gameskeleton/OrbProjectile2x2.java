package com.rjw.gameskeleton;

import java.io.File;
import javax.imageio.ImageIO;

public class OrbProjectile2x2 extends OrbProjectile{

	public static final int ORB_PROJECTILE_2_X_2_W = 2;
	public static final int ORB_PROJECTILE_2_X_2_H = 2;
	public static final int SPEED_PIXELS_PER_SECOND = 5;
	
	public OrbProjectile2x2(int x, int y, int vx, int vy){
		
		super(ThingManager.THING_ID_ORB_PROJECTILE_2_X_2, ORB_PROJECTILE_2_X_2_W, ORB_PROJECTILE_2_X_2_H, x, y, vx, vy);
		
	}//constructor

	@Override
	public void initAnimationFrames() {
	
		try{	
			_frameTile = ImageIO.read(new File(OtherStuff.SPRITE_PATH_PREFIX + OtherStuff.SPRITE_ORB_PROJECTILE_2_X_2));
		}catch(Exception e){
			GameSkeleton.printDebugMessage("Error loading frames for OrbProjectile6x6 ("+e.getMessage()+")");
		}
		// our orb is 6x6
		super.addAnimFrame(_frameTile.getSubimage(0, 0, 2, 2), 200);
		super.addAnimFrame(_frameTile.getSubimage(2, 0, 2, 2), 200);
		
	}//initAnimationFrames
	
}//SmallBlueOrbProjectile
