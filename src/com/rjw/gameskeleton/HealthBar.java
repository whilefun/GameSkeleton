package com.rjw.gameskeleton;

import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

public class HealthBar extends Thing{

	// health bar animation index
	public static final int ANIM_HEALTH_BAR_100 = 0;
	public static final int ANIM_HEALTH_BAR_80 = 1;
	public static final int ANIM_HEALTH_BAR_60 = 2;
	public static final int ANIM_HEALTH_BAR_40 = 3;
	public static final int ANIM_HEALTH_BAR_20 = 4;
	
	private BufferedImage _healthBarTile;
	private int _currentAnimationState;
	
	
	public HealthBar() {
		
		super(0, 0, 0, 0, 0, new Animation());
		
		//try to load our animation sprite tile images
		try{	
			_healthBarTile = ImageIO.read(new File(OtherStuff.SPRITE_PATH_PREFIX + OtherStuff.SPRITE_AVATAR_HEALTH_BAR));
		}catch(Exception e){
			GameSkeleton.printDebugMessage("error loading frames for avatar ("+e.getMessage()+")");
		}
		
		// Load our health bar animations - and set timings so lower health has more frantic blinking! :) //
		
		//100
		addAnimFrame(_healthBarTile.getSubimage(0, 0, 64, 32), 400);
		addAnimFrame(_healthBarTile.getSubimage(0, 32, 64, 32), 250);
		addAnimFrame(_healthBarTile.getSubimage(0, 64, 64, 32), 150);
		addAnimFrame(_healthBarTile.getSubimage(0, 32, 64, 32), 250);
		
		//80
		addAnimation(new Animation());
		setCurrentAnimation(ANIM_HEALTH_BAR_80);
		addAnimFrame(_healthBarTile.getSubimage(64, 0, 64, 32), 350);
		addAnimFrame(_healthBarTile.getSubimage(64, 32, 64, 32), 200);
		addAnimFrame(_healthBarTile.getSubimage(64, 64, 64, 32), 100);
		addAnimFrame(_healthBarTile.getSubimage(64, 32, 64, 32), 200);
		
		//60
		addAnimation(new Animation());
		setCurrentAnimation(ANIM_HEALTH_BAR_60);
		addAnimFrame(_healthBarTile.getSubimage(128, 0, 64, 32), 300);
		addAnimFrame(_healthBarTile.getSubimage(128, 32, 64, 32), 150);
		addAnimFrame(_healthBarTile.getSubimage(128, 64, 64, 32), 50);
		addAnimFrame(_healthBarTile.getSubimage(128, 32, 64, 32), 150);
		
		//40
		addAnimation(new Animation());
		setCurrentAnimation(ANIM_HEALTH_BAR_40);
		addAnimFrame(_healthBarTile.getSubimage(192, 0, 64, 32), 250);
		addAnimFrame(_healthBarTile.getSubimage(192, 32, 64, 32), 100);
		addAnimFrame(_healthBarTile.getSubimage(192, 64, 64, 32), 50);
		addAnimFrame(_healthBarTile.getSubimage(192, 32, 64, 32), 100);
		
		//20
		addAnimation(new Animation());
		setCurrentAnimation(ANIM_HEALTH_BAR_20);
		addAnimFrame(_healthBarTile.getSubimage(256, 0, 64, 32), 200);
		addAnimFrame(_healthBarTile.getSubimage(256, 32, 64, 32), 50);
		addAnimFrame(_healthBarTile.getSubimage(256, 64, 64, 32), 50);
		addAnimFrame(_healthBarTile.getSubimage(256, 32, 64, 32), 50);

		
		setCurrentAnimation(ANIM_HEALTH_BAR_100);
		
	}//constructor

	/**
	 * Sets our health bar state based on the health passed in
	 * @param health - health value from 0 to 100
	 */
	public void setHealthBarStateFromHealth(int health){
		
		if(health > 80){
			setCurrentAnimation(ANIM_HEALTH_BAR_100);
		}else if(health > 60){
			setCurrentAnimation(ANIM_HEALTH_BAR_80);
		}else if(health > 40){
			setCurrentAnimation(ANIM_HEALTH_BAR_60);
		}else if(health > 20){
			setCurrentAnimation(ANIM_HEALTH_BAR_40);
		}else{
			setCurrentAnimation(ANIM_HEALTH_BAR_20);
		}
		
	}//setHealthBarStateFromHealth


	@Override
	protected void damage(int damageDone) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void fire(LevelLayer layer, int targetX, int targetY) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void process(long elapsedTime) {
		// TODO Auto-generated method stub
		
	}
	
}//HealthBar
