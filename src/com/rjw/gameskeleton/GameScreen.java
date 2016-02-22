package com.rjw.gameskeleton;

import com.rjw.sfx.SoundManagerSingleton;

//import com.rjw.sfx.SoundManager_Deprecated;

/**
 * A generic container for game screens with an animation and sound
 * @author rwalsh
 *
 */
public class GameScreen{

	private AnimatedSprite _mainAnimation;
	//private SoundManager_Deprecated _soundMan;
	
	public GameScreen(){
		
		_mainAnimation = new AnimatedSprite(0,0,0,0, new Animation(), true);
		//_soundMan = new SoundManager_Deprecated();
	}
	
	public void updateAnimation(long elapsedTime){
		
		_mainAnimation.updateAnimation(elapsedTime);
		
	}
	
	public AnimatedSprite getMainAnimatedSprite() {
		return _mainAnimation;
	}
	
	
	public void loadSound(String filename){
		//_soundMan.loadSound(filename);
		SoundManagerSingleton.getSingletonObject().loadSound(filename);
	}
	
	public void playSound(String filename){
		//_soundMan.playSoundAtIndex(0);
		//_soundMan.playSound(filename);
		SoundManagerSingleton.getSingletonObject().playSound(filename);
	}
	
	
	public void stopSound(String filename){
		//_soundMan.stopSoundAtIndex(0);
		//GameSkeleton.printDebugMessage("TODO: Make GameScreen.stopSound() actually stop the sound...");
		SoundManagerSingleton.getSingletonObject().stopSound(filename);
	}
	
	
	//public void playSound(int index){
	//	_soundMan.playSoundAtIndex(index);
	//}
	
	//public void stopSound(int index){
	//	_soundMan.stopSoundAtIndex(index);
	//}
	
}//GameScreen
