package com.rjw.gameskeleton;

/**
 * A very simple and mostly redundant class that houses 2 numbers: an X and a Y.
 * Yes, this has been done a billion times before, but I'm making my own
 * so that I can port this to other languages and platforms, rather than
 * relying solely on Java AWT Point2D or whatever.
 * @author rwalsh
 *
 */
public class Coord2D {

	private int _x;
	private int _y;
	
	public Coord2D(int x, int y){
		
		_x = x;
		_y = y;
		
	}

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
	
	
	
}//Coord2D
