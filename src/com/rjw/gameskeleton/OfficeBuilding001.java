package com.rjw.gameskeleton;

import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;

public class OfficeBuilding001 extends TallStructure{

	private BufferedImage _animationImage;
	
	public OfficeBuilding001(int x, int y){
		
		super(ThingManager.THING_ID_OFFICEBUILDING_001, x, y, new Animation());
		
		// create frames for this specific Thing
		try{	
			_animationImage = ImageIO.read(new File(OtherStuff.SPRITE_PATH_PREFIX + OtherStuff.SPRITE_TALL_STUCTURE_001));
		}catch(Exception e){
			GameSkeleton.printDebugMessage("error loading frames for " + this.getClass().toString() + " ("+e.getMessage()+")");
		}
		super.addAnimFrame(_animationImage.getSubimage(0, 0, 64, 64), 500);
		super.addAnimFrame(_animationImage.getSubimage(64, 0, 64, 64), 500);
		super.addAnimFrame(_animationImage.getSubimage(128, 0, 64, 64), 500);
			
		
		this.setCollisionsMatter(true);
		
	}//constructor

	public void fire(LevelLayer layer, int targetX, int targetY){
		//does nothing
	}
	
}//OfficeBuilding001
