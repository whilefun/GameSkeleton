package com.rjw.gameskeleton;

// a basic timer for events and stuff.
public class Timer {

	private long _timeGone;
	private long _totalDuration;
	private boolean _isDone;

	/**
	 * Creates a new, empty Animation.
	 */
	public Timer(long duration) {
		_totalDuration = duration;
		_isDone = false;
	}

	//updates the timer based on time now vs. start time
	public synchronized void update(long elapsedTime) {
		
		_timeGone += elapsedTime;

		if(_timeGone >= _totalDuration){
			_isDone = true;
		}
		
	}//update
	
	// resets the timer
	public void reset(){
		_isDone = false;
		_timeGone = 0;
	}

	//checks if the timer is done
	public boolean isDone(){
		return _isDone;
	}

}//Timer
