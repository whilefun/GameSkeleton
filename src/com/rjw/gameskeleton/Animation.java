package com.rjw.gameskeleton;


import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class Animation {

	private ArrayList _frames;
	private int _currentFrameIndex;
	private long _animTime;
	private long _totalDuration;
	private int _loopsLeft;

	/**
	 * Creates a new, empty Animation.
	 */
	public Animation() {
		_frames = new ArrayList();
		_totalDuration = 0;
		
		//by default, let it loop forever
		setLoopCount(-1);
		
		start();
	}

	/**
	 * Adds an image to the animation with the specified duration (time to
	 * display the image).
	 */
	public synchronized void addFrame(BufferedImage image, long duration) {
		_totalDuration += duration;
		_frames.add(new AnimFrame(image, _totalDuration));
	}

	/**
	 * Starts this animation over from the beginning.
	 */
	public synchronized void start() {
		_animTime = 0;
		_currentFrameIndex = 0;
	}

	/**
	 * Updates this animation's current image (frame), if neccesary.
	 */
	public synchronized void update(long elapsedTime) {
		
		if (_frames.size() > 1) {
			_animTime += elapsedTime;
			
			//if we're out of loops, just return the last frame
			if(_loopsLeft == 0){
				
				_currentFrameIndex = _frames.size() - 1;
				
			}else{

				// if we've played out the total animation, loop it
				if ((_animTime >= _totalDuration)) {
					_animTime = _animTime % _totalDuration;
					_loopsLeft--;
					//only reset this if we have loops left
					if(_loopsLeft != 0){
						_currentFrameIndex = 0;
					}
				}
	
				//basically "while there is more time on this frame before it's supposed to end"
				while ((_animTime > getFrame(_currentFrameIndex).endTime) ){
					_currentFrameIndex++;
				}
				
			}
			
		}//if
		
	}//update
	
	/**
	 * Gets this Animation's current image. Returns null if this animation has
	 * no images.
	 */
	public synchronized BufferedImage getImage() {
		if (_frames.size() == 0) {
			return null;
		} else {
			return getFrame(_currentFrameIndex)._image;
		}
	}

	private AnimFrame getFrame(int i) {
		return (AnimFrame) _frames.get(i);
	}

	public void setLoopCount(int loops){
		_loopsLeft = loops;
	}
	
	private class AnimFrame {

		BufferedImage _image;
		long endTime;

		public AnimFrame(BufferedImage image, long endTime) {
			this._image = image;
			this.endTime = endTime;
		}
		
	}//AnimFrame

}//Animation
