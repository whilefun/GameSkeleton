package com.rjw.editor;

import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

import com.rjw.gameskeleton.Animation;
import com.rjw.gameskeleton.LevelLayer;
import com.rjw.gameskeleton.OtherStuff;
import com.rjw.gameskeleton.Thing;
import com.rjw.gameskeleton.ThingManager;
import com.rjw.gameskeleton.GameSkeleton;
/**
 * This class is for placing the player start in the editor 
 * @author rwalsh
 */
public class PlayerStart extends Thing{

	private BufferedImage _animationImage;
	
	public PlayerStart(int x, int y){
		
		super(ThingManager.THING_ID_PLAYERSTART, x, y, 32, 32, new Animation());
		
		// create frames for this specific Thing
		try{	
			_animationImage = ImageIO.read(new File(OtherStuff.SPRITE_PATH_PREFIX + OtherStuff.SPRITE_PLAYER_START));
			super.addAnimFrame(_animationImage.getSubimage(0, 0, OtherStuff.TILE_SIZE, OtherStuff.TILE_SIZE), 0);
		}catch(Exception e){
			GameSkeleton.printDebugMessage("error loading frames for " + this.getClass().toString() + " ("+e.getMessage()+")");
		}
		
	}

	protected void damage(int damageDone) {
		// TODO Auto-generated method stub
	}
	
	public void fire(LevelLayer layer, int targetX, int targetY){
		// does nothing
	}
	
	protected void process(long elapsedTime) {
		// TODO Auto-generated method stub
	}

}//PlayerStart
