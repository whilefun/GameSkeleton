package com.rjw.gameskeleton;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Vector;

import javax.sound.sampled.AudioFormat;

public abstract class Thing {

	// Thing Life States //
	// BORN: it's a thing, and it's present (e.g. for projectile, it hasn't been fired yet) 
	public static final int LIFE_STATE_BORN = 0;
	// LIVED: it's still alive but has been active (projectile that has been shot from plane, etc.)
	public static final int LIFE_STATE_LIVED = 1;
	// DEAD: it's been fired, and lived out it's useful life (e.g. projectile that has gone off screen)
	public static final int LIFE_STATE_DEAD_AND_GONE = 2;
	// ZOMBIE: it's been destroyed, but should still be in the game (crashed plane, corpse, etc.)
	public static final int LIFE_STATE_DEAD = 3;
	
	private int _x;
	private int _y;
	private int _w;
	private int _h;
	private boolean _canCollide;
	private int _id;
	private ArrayList<Animation> _animations;
	private int _currentAnimationIndex = 0;
	private ArrayList<Animation> _effectAnimations = null;
	private int _currentEffectAnimationIndex = 0;
	private ArrayList<Animation> _shadowAnimations = null;
	private int _currentShadowAnimationIndex = 0;
	private int _lifeState;
	private boolean _hasEffectAnimation;
	private boolean _hasShadowAnimation;
	private int _shadowDrawOffsetX;
	private int _shadowDrawOffsetY;
	//TODO: IS THIS REALLY NEEDED?
	private boolean _inLoopingLevel;
	
	// TODO: reserved for enemies...perhaps optimize this, or create an array of just things that can fire
	private boolean _readyToFire;
	
	//constructor
	// TODO: make things that can be more than 1 tile in size
	public Thing(int id, int x, int y, int w, int h, Animation mainAnimation){
		
		//_name = name;
		_id = id;
		_x = x;
		_y = y;
		_w = w; //OtherStuff.TILE_SIZE;
		_h = h; //OtherStuff.TILE_SIZE;
		_animations = new ArrayList<Animation>();
		_animations.add(mainAnimation);
		_canCollide = false;
		_lifeState = LIFE_STATE_BORN;
		_readyToFire = false;
		
		_hasEffectAnimation = false;
		_hasShadowAnimation = false;
		
		_shadowDrawOffsetX = -10;
		_shadowDrawOffsetY = -10;

		//initSounds();
		
	}//Constructor1

	//constructor
	// TODO: make things that can be more than 1 tile in size
	public Thing(int id, int x, int y, int w, int h, Animation mainAnimation, Animation effectAnimation, Animation shadowAnimation){
		
		// call our regular constructor
		this(id, x, y, w, h, mainAnimation);

		// and add in our effects
		_effectAnimations = new ArrayList<Animation>();
		_effectAnimations.add(effectAnimation);
		
		_shadowAnimations = new ArrayList<Animation>();
		_shadowAnimations.add(shadowAnimation);
		
		_hasEffectAnimation = true;
		
		_inLoopingLevel = false;
		
		//TODO: will initSounds get called twice? yes.
		initSounds();
		
	}//Constructor2

	//setters
	public void setX(int x){ _x = x; }
	public void setY(int y){ _y = y; }
	public void setW(int w){ _w = w; }
	public void setH(int h){ _h = h; }
	public void setCollisionsMatter(boolean cm){ _canCollide = cm; }
	public void setLifeState(int lifestate){ _lifeState= lifestate; }
	// main animations
	public void setCurrentAnimation(int i){ _currentAnimationIndex = i; }
	public void setAnimationLoopCount(int loops){ _animations.get(_currentAnimationIndex).setLoopCount(loops); }
	public void startAnimation(){ _animations.get(_currentAnimationIndex).start(); }
	// effect animations
	public void setCurrentEffectAnimation(int i){ _currentEffectAnimationIndex = i; }
	public void setEffectAnimationLoopCount(int loops){ _effectAnimations.get(_currentEffectAnimationIndex).setLoopCount(loops); }
	public void startEffectAnimation(){ _effectAnimations.get(_currentEffectAnimationIndex).start(); }
	// shadow animations
	public void setCurrentShadowAnimation(int i){ _currentShadowAnimationIndex = i; }
	public void setShadowAnimationLoopCount(int loops){ _shadowAnimations.get(_currentShadowAnimationIndex).setLoopCount(loops); }
	public void startShadowAnimation(){ _shadowAnimations.get(_currentShadowAnimationIndex).start(); }
	
	public void setReadyToFire(boolean rtf){ _readyToFire = rtf; }
	public void setInLoopingLevel(boolean inLoopingLevel){ _inLoopingLevel = inLoopingLevel; }
		
	//getters
	public int getX(){ return _x; }
	public int getY(){ return _y; }
	public int getW(){ return _w; }
	public int getH(){ return _h; }
	//public String getName(){ return _name; }
	public int getID(){ return _id; }
	public boolean collisionsMatter(){ return _canCollide; }
	public int getLifeState(){ return _lifeState; }
	public boolean readyToFire(){ return _readyToFire; }
	public boolean hasEffectAnimation(){ return _hasEffectAnimation; }
	public boolean hasShadowAnimation(){ return _hasShadowAnimation; }
	public boolean isInLoopingLevel(){ return _inLoopingLevel; }
		 
	
	
	public int getShadowDrawOffsetX() {
		return _shadowDrawOffsetX;
	}

	public void setShadowDrawOffsetX(int shadowDrawOffsetX) {
		_shadowDrawOffsetX = shadowDrawOffsetX;
	}

	public int getShadowDrawOffsetY() {
		return _shadowDrawOffsetY;
	}

	public void setShadowDrawOffsetY(int shadowDrawOffsetY) {
		_shadowDrawOffsetY = shadowDrawOffsetY;
	}

	/**
	 * @return returns an image made from the current main animation frame of the Thing
	 */
	public BufferedImage getMainImage(){ return _animations.get(_currentAnimationIndex).getImage(); }
	/**
	 * @return returns an image made from the current effect animation frame of the Thing
	 */
	public BufferedImage getEffectImage(){ return _effectAnimations.get(_currentEffectAnimationIndex).getImage(); }
	/**
	 * @return returns an image made from the current shadow animation frame of the Thing
	 */
	public BufferedImage getShadowImage(){ return _shadowAnimations.get(_currentShadowAnimationIndex).getImage(); }
	
	
	public String toString(){
		return "" + _id + "("+ _x +","+ _y +")";
	}
	
	// main animations
	public void addAnimFrame(BufferedImage image, long duration){
		_animations.get(_currentAnimationIndex).addFrame(image, duration);
	}
	public void addAnimation(Animation anim){
		_animations.add(anim);
	}
	//effect animations
	public void addEffectAnimFrame(BufferedImage image, long duration){
		_effectAnimations.get(_currentEffectAnimationIndex).addFrame(image, duration);
	}
	public void addEffectAnimation(Animation anim){
		_effectAnimations.add(anim);
	}
	//shadow animations
	public void addShadowAnimFrame(BufferedImage image, long duration){
		_shadowAnimations.get(_currentShadowAnimationIndex).addFrame(image, duration);
	}
	public void addShadowAnimation(Animation anim){
		_shadowAnimations.add(anim);
	}
	
	// all animations
	public void updateAnimations(long elapsedTime){
		_animations.get(_currentAnimationIndex).update(elapsedTime);

		//update effects if there are any
		if(_effectAnimations != null)
			_effectAnimations.get(_currentEffectAnimationIndex).update(elapsedTime);
		
		//update effects if there are any
		if(_shadowAnimations != null)
			_shadowAnimations.get(_currentShadowAnimationIndex).update(elapsedTime);

	}//updateAnimations

	/**
	 * Make abstract to push the implementation of the damage method to
	 * the thing that makes sense to receive damage
	 * @param damageDone - the amount of damage
	 */
	protected abstract void damage(int damageDone);
	
	//TODO: stuff?
	protected abstract void process(long elapsedTime);	
	
	/**
	 * Function to add projectiles to the layer passed
	 * @param layer - the layer where the projectiles will live
	 */
	protected abstract void fire(LevelLayer layer, int targetX, int targetY);
	
    // A basic collision detection method using "axis align bounding box" testing.
	// Quick and dirty, and works for rectangely or small sprites. Per pixel would 
	// be nice, but let's not get too carried away just yet :)
    public static void collisionDetection(Avatar avatar, Thing[] otherThings){
    	
    	//TODO: a bit hacky: add avatar to front of things array and proceed as usual
    	Thing[] worldThings = new Thing[otherThings.length+1];
    	worldThings[0] = avatar;
    	for (int i = 0 ; i < otherThings.length; i++)
    		worldThings[i+1] = otherThings[i];
    	
    	
        Thing currentThing;
        Thing tempThing;
        boolean intersect;

        // for all the other things in our vector,
        // check for collisions and react accordingly
        for (int currentThingIndex = 0 ; currentThingIndex < worldThings.length; currentThingIndex++)
        {
            // Set currentThing to the current object
            //currentThing = worldThings.elementAt(currentThingIndex);
        	currentThing = worldThings[currentThingIndex];

            // Now check the current Thing against all the other Things (and index starting at the next thing!
            for(int otherThingsIndex = currentThingIndex + 1 ; otherThingsIndex < worldThings.length; otherThingsIndex++)
            {
            	
                // Set tempThing to the next Thing being checked against the current Thing 
               //tempThing = worldThings.elementAt(otherThingsIndex);
            	tempThing = worldThings[otherThingsIndex];
                
                // if both our objects can collide, then check for a collision
                if ((currentThing.collisionsMatter() && tempThing.collisionsMatter()))
                {

                    // Let's assume our objects have collided, and set to false if they have not
                    intersect = true;

                    // check the left and right sides of the Thing's bounding boxes
                    if (!(Math.abs((currentThing.getX() + currentThing.getW()/2) - (tempThing.getX() + tempThing.getW()/2)) <= currentThing.getW() / 2 + tempThing.getW() / 2)){
                    	intersect = false;
                    }
                     

                    // check the top and bottom of the Thing's bounding boxes
                    if (!(Math.abs((currentThing.getY() + currentThing.getH()/2) - (tempThing.getY() + tempThing.getH()/2)) <= currentThing.getH() / 2 + tempThing.getH() / 2)){
                        intersect = false;
                    }

                    // If the objects collided, damage each one.
                    if(intersect)
                    {

                        //DEBUG
                        //GameSkeleton.printDebugMessage("Thing: Intersection between: " + currentThing.getID() + " and " + tempThing.getID());
                    	//GameSkeleton.printDebugMessage("Things Collided? (size=" + worldThings.length + " " + intersect+"[" + currentThingIndex + "," + otherThingsIndex + "]): " + currentThing.toString() + tempThing.toString());
                        currentThing.damage(ThingManager.THING_DAMAGE_MATRIX[currentThing.getID()][tempThing.getID()]);
                        tempThing.damage(ThingManager.THING_DAMAGE_MATRIX[tempThing.getID()][currentThing.getID()]);


                        // special cases for avatar powerups, etc.
                        
                        // AVATAR - HEALTH
                        if(currentThing.getID() == ThingManager.THING_ID_AVATAR && tempThing.getID() == ThingManager.THING_ID_HEALTH){
                        	((Avatar)currentThing).heal( (((Health)tempThing).getHealAmount()) );
                        	((Health)tempThing).setLifeState(LIFE_STATE_DEAD_AND_GONE);
                        }
                        // AVATAR - MISSILES
                        else if(currentThing.getID() == ThingManager.THING_ID_AVATAR && tempThing.getID() == ThingManager.THING_ID_MISSILES){
                        	((Avatar)currentThing).reload( ((Missiles)tempThing).getAmmoCount(), Avatar.WEAPON_MISSILES );
                        	((Missiles)tempThing).setLifeState(LIFE_STATE_DEAD_AND_GONE);
                        }
                        
                        
                    }//if
                }//if
            }//for
        }//for
        
    }//collisionDetection

    /**
     * This function updates our thing's position basd on it's own movement 
     * patterns and the movement of our map. For example, if the thing has a 
     * velocity of 5, and the map is moving at a velocity of 3 every frame,
     * then we would update the Thing's y location to be it's current y 
     * location + (5 + 3) 
     * @param worldMovement - the amount our world has moved in 
     * absolute terms (the 3 referenced in the description above)
     */
    public void updatePosition(int worldMovementX, int worldMovementY){
    	
    	_x += worldMovementX;
    	_y += worldMovementY;
    	
    }//updatePosition
    
    /**
     * This function is called by the contstructor and is used to initialize
     * the Thing's sound manager object (load sounds, etc.)
     */
	public void initSounds(){
		//nothing in parent class of Thing
	}
	
    
}//Thing
