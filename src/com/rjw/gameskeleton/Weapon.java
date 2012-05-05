package com.rjw.gameskeleton;

//TODO: remove sounds?
//TODO: remove ammo damage, as this is set in the ThingManager damage matrix

public class Weapon {

	private String _name;
	private int _ammoMax;
	private int _ammoLeft;
	private int _ammoThingID;
	private int _ammoDamage;
	private long _fireDelay;
	private Sprite _icon;
	
	/**
	 * Our weapon class constructor, makes a new weapon
	 * @param name - the name, e.g. "WidowMaker"
	 * @param fireDelay - time in ms between shots
	 * @param ammoDamage - how much damage the ammo does - DEPRECATED
	 * @param ammoMax - max ammo that can be held for this weapon
	 * @param ammoStart - starting ammo (to allow for partially empty weapon pickups)
	 * @param ammoThingID - the Thing ID of the ammo that will be shot (See ThingManager)
	 * @param iconFilename - the filename for the icon (32x32) to be displayed on the HUD
	 */
	public Weapon(String name, long fireDelay, int ammoDamage, int ammoMax, int ammoStart, int ammoThingID, String iconFilename){
		
		_name = name;
		_ammoDamage = ammoDamage;
		_ammoMax = ammoMax;
		_ammoLeft = ammoStart;
		_ammoThingID = ammoThingID;
		_fireDelay = fireDelay;
		_icon = new Sprite(iconFilename);
		
	}//constructor


	public void decreasedAmmoBy(int ammoConsumed) {
		_ammoLeft -= ammoConsumed;
	}
	
	public void increaseAmmoBy(int ammoAdded) {
		_ammoLeft += ammoAdded;
	}
	
	public String getName() {
		return _name;
	}

	public void setName(String name) {
		_name = name;
	}

	public int getAmmoMax() {
		return _ammoMax;
	}

	public void setAmmoMax(int ammoMax) {
		_ammoMax = ammoMax;
	}

	public int getAmmoLeft() {
		return _ammoLeft;
	}

	public void setAmmoLeft(int ammoLeft) {
		_ammoLeft = ammoLeft;
	}

	public int getAmmoThingID() {
		return _ammoThingID;
	}

	public void setAmmoThingID(int ammoThingID) {
		_ammoThingID = ammoThingID;
	}

	public int getAmmoDamage() {
		return _ammoDamage;
	}

	public void setAmmoDamage(int ammoDamage) {
		_ammoDamage = ammoDamage;
	}

	public long getFireDelay() {
		return _fireDelay;
	}

	public void setFireDelay(long fireDelay) {
		_fireDelay = fireDelay;
	}
	
	public Sprite getIcon(){
		return _icon;
	}
	
}//Weapon
