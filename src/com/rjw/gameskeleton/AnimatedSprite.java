package com.rjw.gameskeleton;

import java.awt.image.BufferedImage;

/**
 * AnimatedSprite is basically a sprite, but with a single animation sequence. Animated sprites are not
 * "active" elements in the game, in that they should not be controlled, and have no life,attacks, etc.
 * They are basically meant for special effects and decorations in the game world
 */
public class AnimatedSprite {

	private int _x;
	private int _y;
	private int _w;
	private int _h;
	private Animation _animation;
	private boolean _visible;
	
	/*
	//Constructor
	public AnimatedSprite(int x, int y, int w, int h, Animation animation, int x) {
	
		_x = x;
		_y = y;
		_w = w;
		_h = h;
		_animation = animation;
		_visible = true;
	
	}//constructor
	*/
	
	//Constructor with option for visibility
	public AnimatedSprite(int x, int y, int w, int h, Animation animation, boolean isVisible) {
	
		_x = x;
		_y = y;
		_w = w;
		_h = h;
		_animation = animation;
		_visible = isVisible;
	
	}//constructor
	
	// this is our copy constructor
	public AnimatedSprite(AnimatedSprite animatedSpriteToCopy){
		
		_x = animatedSpriteToCopy._x;
		_y = animatedSpriteToCopy._y;
		_w = animatedSpriteToCopy._w;
		_h = animatedSpriteToCopy._h;
		_animation = animatedSpriteToCopy._animation;
		_visible = animatedSpriteToCopy.isVisible();
		
	}//copyConstructor
	
	public BufferedImage getImage(){
		return _animation.getImage();
	}
	
	public void updateAnimation(long elapsedTime){
		_animation.update(elapsedTime);
	}
	
	public void setAnimation(Animation newAnim){
		_animation = newAnim;
	}
	
	public boolean isVisible() {
		return _visible;
	}
	
	public int getX(){ return _x; }
	public int getY(){ return _y; }
	public int getH(){ return _h; }
	public int getW(){ return _w; }
	public Animation getAnimation(){ return _animation; }

	public void setY(int y) { this._y = y; }
	public void setX(int x) { this._x = x; }
	public void changeXBy(int xDelta){ _x += xDelta; }
	public void changeYBy(int yDelta){ _y += yDelta; }
	public void setVisible(boolean vis) { this._visible = vis; }

	
}//AnimatedSprite
