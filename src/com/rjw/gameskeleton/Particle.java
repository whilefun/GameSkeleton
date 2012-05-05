package com.rjw.gameskeleton;

import java.awt.Color;

public class Particle {

	private int _x;
	private int _y;
	private int _xVel;
	private int _yVel;
	private int _size;
	private Sprite _sprite;
	private int _alpha;
	private Color _color;
	
	
	/**
	 * Constructs a new particle
	 * @param x
	 * @param y
	 * @param xVel
	 * @param yVel
	 * @param size
	 * @param sprite
	 * @param alpha
	 */
	public Particle(int x, int y, int xVel, int yVel, int size, Sprite sprite, Color color, int alpha){
		
		_x = x;
		_y = y;
		_xVel = xVel;
		_yVel = yVel;
		_size = size;
		_sprite = sprite;
		_color = color;
		_alpha = alpha;
		
	}//constructor
	
	public void moveParticleBy(int x, int y){
		_x += x;
		_y += y;
	}//moveParticleBy
	
	public void moveParticleTo(int x, int y){
		_x = x;
		_y = y;		
	}//move particleTo

	public int getX() {
		return _x;
	}

	public void setX(int x) {
		_x = x;
	}

	public int getY() {
		return _y;
	}

	public void setY(int y) {
		_y = y;
	}

	public int getXVel() {
		return _xVel;
	}

	public void setXVel(int xVel) {
		_xVel = xVel;
	}

	public int getYVel() {
		return _yVel;
	}

	public void setYVel(int yVel) {
		_yVel = yVel;
	}

	public int getSize() {
		return _size;
	}

	public void setSize(int size) {
		_size = size;
	}

	public Sprite getSprite() {
		return _sprite;
	}

	public void setSprite(Sprite sprite) {
		_sprite = sprite;
	}

	public Color getColor() {
		return _color;
	}

	public void setColor(Color color) {
		_color = color;
	}
	
	public int getAlpha() {
		return _alpha;
	}

	public void setAlpha(int alpha) {
		_alpha = alpha;
	}
	
}//Particle

