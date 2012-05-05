package com.rjw.gameskeleton;

import com.rjw.editor.PlayerStart;


// A note About Trajectory Calculations (Since the onces in the code are inline and hard to read:
// -- Calculate 2D differences in coords
// float dirX = 200 - this.getX();
// float dirY = 200 - this.getY();
// float pixelsPerSecond = 5;
// -- Get max of value and 1 to prevent divide by zero error 
// float vectorLength = (float) Math.max(1,Math.sqrt(dirX*dirX + dirY*dirY));
// -- Normalize dirX and dirY
// GameSkeleton.printDebugMessage("DirX/DirY=("+dirX+","+dirY+")");
// dirX = dirX/vectorLength;
// dirY = dirY/vectorLength;
// GameSkeleton.printDebugMessage("DirX/DirY=("+dirX+","+dirY+"), vector length is " + vectorLength);
// -- Add new projectile, but don't forget to change it so the rise/run crap is gone!
// things.add(new Projectile(this.getX(),this.getY(), (int)(dirX*pixelsPerSecond), (int)(dirY*pixelsPerSecond), 1, 0));


/**
 * 'Cuz Things got to big to manage poorly
 * @author rwalsh
 *
 */
public class ThingManager {

	//////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// NOTE: Process for adding new things to the game is:
	// 1) Create a new Thing subclass such as "Projectile" 
	// 2) Add static THING_ID int to the THING ID NUMBERS section below 
	// 3) Add ThingID such as "THING_ID_PROJECTILE" to the case statement in createThingWithID()
	// 4) Add ThingID as row in the THING_DAMAGE_MATRIX below and set damage interactions with all other things
	// 5) Add ThingID as line to "things_01.xml", such as "<Thing type="Projectile" id="2"/>". This makes 
	//    the new Thing class available in the editor.
	// 6) To test, add a thing to a level, then run the level in game and verify that the new Thing is there.
	//////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	// THING ID NUMBERS //
	public static final int THING_COUNT = 8;
	public static final int THING_ID_NOTHING = 0;
	public static final int THING_ID_AVATAR = 1;
	public static final int THING_ID_PROJECTILE = 2;
	public static final int THING_ID_THROWAWAY = 3;
	public static final int THING_ID_OFFICEBUILDING_001 = 4;
	public static final int THING_ID_PLAYERSTART = 5;
	public static final int THING_ID_BAD_GUY_TEST = 6;
	public static final int THING_ID_ORB_PROJECTILE_8_X_8 = 7;
	public static final int THING_ID_CLOUD_001 = 8;
	public static final int THING_ID_BULLET_50MM = 9;
	public static final int THING_ID_TURRET_01 = 10;
	public static final int THING_ID_HEALTH = 11;
	public static final int THING_ID_MISSILES = 12;
	public static final int THING_ID_UFO = 13;
	public static final int THING_ID_ORB_PROJECTILE_6_X_6 = 14;
	public static final int THING_ID_SPEEDER = 15;
	public static final int THING_ID_MOSQUITO = 16;
	public static final int THING_ID_ORB_PROJECTILE_2_X_2 = 17;
	public static final int THING_ID_BOSS_TEST = 18;
	
	// This is our damage matrix, and it's "THING_COUNT" x "THING_COUNT" in size
	// when two Things collide, call each damage function with the lookup
	// at the index of the two where the collidee is the first index. In other
	// words, if Thing A and B collide, call A.damage(THING_DAMAGE_MATRIX[A][B]);
	// So, if this array at position [1][7] would be 5, meaning that the 
	// avatar takes 5 damage when it collides with an orb
	public static final int[][] THING_DAMAGE_MATRIX = new int[][]{
	    
		// | | |
		// | |P|
		// | |r|
		// | |o|
		//N| |j|
		//o|A|e|
		//t|v|c|
		//h|a|t|
		//i|t|i|
		//n|a|l|
		//g|r|e|
		//-|-|-|-|-|-|-|-|-|-|--|--|
		//0|1|2|3|4|5|6|7|8|9|10|11|
		{ 0,0,0,0,0,0,0,0,0,0,0, 0, 0, 0, 0, 0, 0, 0, 0}, // 0 - Nothing
		{ 0,0,0,0,0,0,0,5,0,0,0, 0, 0, 0, 3, 0, 0, 1, 999}, // 1 - Avatar
		{ 0,0,0,0,0,0,0,0,0,0,0, 0, 0, 0, 0, 0, 0, 0, 0}, // 2 - Projectile
		{ 0,0,0,0,0,0,0,0,0,0,0, 0, 0, 0, 0, 0, 0, 0, 0}, // 3 - Throw Away
		{ 0,0,0,0,0,0,0,0,0,0,0, 0, 0, 0, 0, 0, 0, 0, 0}, // 4 - OfficeBuilding_001
		{ 0,0,0,0,0,0,0,0,0,0,0, 0, 0, 0, 0, 0, 0, 0, 0}, // 5 - PlayerStart
		{ 0,0,30,0,0,0,0,0,0,20,0, 0, 0, 0, 0, 0, 0, 0, 0}, // 6 - BadGuyTest
		{ 0,0,0,0,0,0,0,0,0,0,0, 0, 0, 0, 0, 0, 0, 0, 0}, // 7 - OrbProjectile8x8
		{ 0,0,0,0,0,0,0,0,0,0,0, 0, 0, 0, 0, 0, 0, 0, 0}, // 8 - Cloud
		{ 0,0,0,0,0,0,0,0,0,0,0, 0, 0, 0, 0, 0, 0, 0, 0}, // 9 - Bullet 50mm
		{ 0,0,9,0,0,0,0,0,0,20,0, 0, 0, 0, 0, 0, 0, 0, 0},  // 10 - Turret01
		{ 0,0,0,0,0,0,0,0,0,7,0, 0, 0, 0, 0, 0, 0, 0, 0},  // 11 - Health
		{ 0,0,0,0,0,0,0,0,0,0,0, 0, 0, 0, 0, 0, 0, 0, 0},  // 12 - Missiles
		{ 0,0,30,0,0,0,0,0,0,15,0, 0, 0, 0, 0, 0, 0, 0, 0},  // 13 - UFO
		{ 0,0,0,0,0,0,0,0,0,0,0, 0, 0, 0, 0, 0, 0, 0, 0},  // 14 - OrbProjectile6x6
		{ 0,0,50,0,0,0,0,0,0,30,0, 0, 0, 0, 0, 0, 0, 0, 0},  // 15 - Speeder
		{ 0,0,50,0,0,0,0,0,0,50,0, 0, 0, 0, 0, 0, 0, 0, 0},  // 16 - Mosquito
		{ 0,0,0,0,0,0,0,0,0,0,0, 0, 0, 0, 0, 0, 0, 0, 0},  // 17 - OrbProjectile2x2
		{ 0,0,20,0,0,0,0,0,0,20,0, 0, 0, 0, 0, 0, 0, 0, 0}  // 18 - BossTest


		
	};//THING_DAMAGE_MATRIX
	
	/**
	 * Creates (returns) a new Object of class id passed
	 * @param id
	 * @param x
	 * @param y
	 * @return - new thing of class same as ID passed in, with x and y same as that passed in
	 */
	public static Object createThingWithID(int id, int x, int y){
		
		//TODO: analyze efficiency of returning Thing each case, or making a tempThing and returning it/or a copy
		// Also note that the return statement acts as the break statement in each case
		switch(id){
		
			case THING_ID_AVATAR:
				return new Avatar(x, y, 0, 0, 0, 0);
			case THING_ID_PROJECTILE:
				return new Projectile(x, y, 0, 0, 0, 0);
			case THING_ID_OFFICEBUILDING_001:
				return new OfficeBuilding001(x, y);
			case THING_ID_PLAYERSTART:
				return new PlayerStart(x,y);
			case THING_ID_BAD_GUY_TEST:
				return new BadGuyTest(x,y,BadGuyTest.VELOCITY_X, BadGuyTest.VELOCITY_Y);
			case THING_ID_ORB_PROJECTILE_8_X_8:
				return new OrbProjectile8x8(x,y,0, 0);
			case THING_ID_CLOUD_001:
				return new Cloud_001(x,y,Cloud_001.VELOCITY_X, Cloud_001.VELOCITY_Y, 0, 0);
			case THING_ID_BULLET_50MM:
				return new Bullet_50mm(x, y, 0, 0, 0, 0);
			case THING_ID_TURRET_01:
				return new Turret01(x,y,Turret01.VELOCITY_X, Turret01.VELOCITY_Y);
			case THING_ID_HEALTH:
				return new Health(x,y,Health.VELOCITY_X, Health.VELOCITY_Y, 0, 0, Health.HEAL_AMOUNT);
			case THING_ID_MISSILES:
				return new Missiles(x,y,Missiles.VELOCITY_X, Missiles.VELOCITY_Y, 0, 0, Missiles.AMMO_COUNT);
			case THING_ID_UFO:
				return new UFO(x,y,UFO.VELOCITY_X, UFO.VELOCITY_Y);
			case THING_ID_ORB_PROJECTILE_6_X_6:
				return new OrbProjectile6x6(x,y,0, 0);
			case THING_ID_SPEEDER:
				return new Speeder(x, y, Speeder.VELOCITY_X, Speeder.VELOCITY_Y);
			case THING_ID_MOSQUITO:
				return new Mosquito(x, y, Mosquito.VELOCITY_X, Mosquito.VELOCITY_Y);
			case THING_ID_ORB_PROJECTILE_2_X_2:
				return new OrbProjectile2x2(x,y,0, 0);
			case THING_ID_BOSS_TEST:
				return new BossTest(x,y,0,0);
			default:
				GameSkeleton.printDebugMessage("ThingManager: ***ERROR - Encountered Unknown Thing ID. This should never happen. Returning null...");
				return null;
	
		}//switch

		
	}//createThingWithID
	
	
}//ThingManager
