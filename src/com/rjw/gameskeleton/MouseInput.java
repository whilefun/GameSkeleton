package com.rjw.gameskeleton;

import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

//Thanks to Tim Wright for getting me started on this damn class!
//(see http://www.gamedev.net/reference/programming/features/javainput/page3.asp)
//TODO: make your own mouse input class (or rather, make yours again and as easy to use and accurate)
//TODO: Clean this class up a little
public class MouseInput implements MouseListener, MouseMotionListener{

	private static final int BUTTON_COUNT = 3;
	// Polled position of the mouse cursor
	private Point _mousePosition = null;
	// Current position of the mouse cursor
	private Point _currentPosition = null;
	// Current state of mouse buttons
	private boolean[] _state = null;
	// Polled mouse buttons
	private Mouse_state[] _poll = null;
	
	private enum Mouse_state{
		RELEASED, 			// Released (Up)
		PRESSED,			// Down, but not the first time
		ONCE,				// Down for the first time
		RELEASEDONCE		// Just Released (Up)
	}
	
	public MouseInput(){
		// Create default mouse positions
		_mousePosition = new Point( 0, 0);
		_currentPosition = new Point( 0, 0);
		// Setup initial button _states
		_state = new boolean[BUTTON_COUNT];
		_poll = new Mouse_state[BUTTON_COUNT];
		
		for( int i = 0; i < BUTTON_COUNT; ++i){
			_poll[i] = Mouse_state.RELEASED;
		}
		
	}//MouseInput
	
	public synchronized void poll(){

		// Save our current position
		_mousePosition = new Point( _currentPosition);
		
		// Check each mouse button
		for( int i = 0; i < BUTTON_COUNT; ++i){
			
			// If the button is down for the first
			// time, it is ONCE, otherwise it is
			// PRESSED.	
			if( _state[i]){
				if( _poll[i] == Mouse_state.RELEASED)
					_poll[i] = Mouse_state.ONCE;
				else
					_poll[i] = Mouse_state.PRESSED;
			}else{
				
				// if it was down last time and just released.
				// set state to RELEASEDONCE
				if(_poll[i] == Mouse_state.ONCE || _poll[i] == Mouse_state.PRESSED){
					_poll[i] = Mouse_state.RELEASEDONCE;
				}else{
					_poll[i] = Mouse_state.RELEASED;
				}

			}
		}
	}//poll

	public Point getPosition(){
		return _mousePosition;
	}
	public boolean buttonDownOnce( int button){
		return _poll[button-1] == Mouse_state.ONCE;
	}
	public boolean buttonDown( int button){
		return _poll[button-1] == Mouse_state.ONCE ||
					 _poll[button-1] == Mouse_state.PRESSED;
	}
	public boolean buttonUp( int button){
		return _poll[button-1] == Mouse_state.RELEASED;
	}
	public boolean buttonUpOnce( int button){
		return _poll[button-1] == Mouse_state.RELEASEDONCE;
	}
	
	
	
	public synchronized void mousePressed(MouseEvent e){
		_state[e.getButton()-1] = true;
	}
	public synchronized void mouseReleased(MouseEvent e){
		_state[e.getButton()-1] = false;
	}
	public void mouseClicked(MouseEvent e){
		// Don't need it and don't want it.
	}
	public synchronized void mouseEntered(MouseEvent e){
		mouseMoved(e);
	}
	public synchronized void mouseExited(MouseEvent e){
		mouseMoved(e);
	}
	public synchronized void mouseDragged(MouseEvent e){
		mouseMoved(e);
	}
	public synchronized void mouseMoved(MouseEvent e){
		_currentPosition = e.getPoint();
	}

}//MouseInput