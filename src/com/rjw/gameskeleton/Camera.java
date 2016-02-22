package com.rjw.gameskeleton;

public class Camera {

	private int _x;
	private int _y;
	private int _w;
	private int _h;

	private int _ySpeed;
	private int _xSpeed;
		
	public Camera(){
		
		// Set our x/y to be the left/top of where we want our
		
		// set the camera to point at the bottom-most section
		// of our map. So if our map is 10 wide by 30 high, 
		// we want to point it at the bottom 10 x 10
		setX(0);
		setY(0);
		//set our w/h to be the width/height of our screen
		setW(OtherStuff.CAMERA_WIDTH);
		setH(OtherStuff.CAMERA_HEIGHT);
		
		_ySpeed = OtherStuff.CAMERA_Y_SPEED;
		_xSpeed = OtherStuff.CAMERA_X_SPEED;
		
	}//constructor

	//TODO: shake the camera
	public void shake(){
		//do stuff.
	}//shake

	//setters and getters
	public void setX(int _x) { this._x = _x; }
	public int getX() { return _x; }
	public void setY(int _y) { this._y = _y; }
	public int getY() { return _y; }
	public void setW(int _w) { this._w = _w; }
	public int getW() { return _w; }
	public void setH(int _h) { this._h = _h; }
	public int getH() { return _h; }

	public void setYSpeed(int _ySpeed) {
		this._ySpeed = _ySpeed;
	}

	public int getYSpeed() {
		return _ySpeed;
	}

}//Camera
