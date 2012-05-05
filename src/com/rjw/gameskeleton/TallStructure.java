package com.rjw.gameskeleton;

import java.awt.image.BufferedImage;

/**
 * TallStructure is an abstract class derived from Thing, that represents 
 * a tall single animation obstacle in the game world
 * @author rwalsh
 */
public abstract class TallStructure extends Thing {

	//TODO: make this a self contained structure, OR an abstract "building" type, and make self contained variants
	// This will make it compatible with our editor and map, as we can have all the aniamtions and other
	// garbage inside each variant, and each variant will be very small, with the only difference
	// being the building's animation and damage, etc.
	
	//private Animation _thingAnimation;
	//private BufferedImage _thingsImage;
	
	//public TallStructure(int x, int y, Animation animation) {
	public TallStructure(int id, int x, int y, Animation animation) {
	
		super(id, x, y, 64, 64, animation);

		//add some "collidability"
		this.setCollisionsMatter(true);
	
	}

	public void damage(int damageDone) {
		//GameSkeleton.printDebugMessage("TallStructureDamaged");
	}
	
	public void process(long elapsedTime){
		//Does nothing...it's a static non-damagable building
	};

}
