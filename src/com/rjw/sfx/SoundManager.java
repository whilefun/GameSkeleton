package com.rjw.sfx;

import java.util.Vector;

import com.rjw.gameskeleton.GameSkeleton;

import paulscode.sound.SoundSystem;
import paulscode.sound.SoundSystemConfig;
import paulscode.sound.SoundSystemException;
import paulscode.sound.codecs.CodecJOgg;
import paulscode.sound.codecs.CodecWav;
import paulscode.sound.libraries.LibraryJavaSound;

public class SoundManager {

	//private Vector<Thread> _soundThreads;
	//private Vector<GameSound> _sounds;
	//private int _maxSounds = 5;
	
	private SoundSystem _soundSystem;
	
	public SoundManager(){
		
		//_sounds = new Vector<GameSound>();
		//_soundThreads = new Vector<Thread>();
		
		// TODO: Sound system test //
		
		
		// make a new sound system
		_soundSystem = new SoundSystem();
		
        // Load some library and codec pluggins:
        try
        {
            //SoundSystemConfig.addLibrary( LibraryLWJGLOpenAL.class );
            SoundSystemConfig.addLibrary( LibraryJavaSound.class );
            SoundSystemConfig.setCodec( "wav", CodecWav.class );
            SoundSystemConfig.setCodec( "ogg", CodecJOgg.class );
        }
        catch( SoundSystemException e )
        {
            GameSkeleton.printDebugMessage("error linking with the pluggins" );
        }

		// Instantiate the SoundSystem:
		try
		{
			_soundSystem = new SoundSystem( LibraryJavaSound.class );
			//	            mySoundSystem = new SoundSystem( LibraryLWJGLOpenAL.class );
		}
		catch( SoundSystemException e )
		{
			GameSkeleton.printDebugMessage( "JavaSound library is not compatible on " +
			"this computer" );
			//	            GameSkeleton.printDebugMessage( "LWJGL OpenAL library is not compatible on " +
			//	                                "this computer" );
			e.printStackTrace();
			return;
		}
		
		// End of SoundSystem Test //
		 
		 
		
	}//constructor
	
	//public void loadSound(String filename){
		
		//_sounds.add(new GameSound(filename));
		
		/*
		if(!_sounds.get(_sounds.size()-1).loadSound()){
			GameSkeleton.printDebugMessage("SoundMan: Could not load sound called " + filename);
		}else{
			GameSkeleton.printDebugMessage("SoundMan: yay, successfully loaded sounds called " + filename + "\n");
		}
		*/
		
	//	GameSkeleton.printDebugMessage("****SoundManager.loadSound() is deprecated and does nothing!");
		
	//}//loadSound

	
	public void playSound(String filename){
		
		/*
		for(int i=0; i < _sounds.size(); i++){
			if(_sounds.get(i).getFilename().compareTo(filename) == 0){
				
				//_sounds.get(i).playSound();
				Thread tempThread = new Thread (_sounds.get(i), "sound 1");
				tempThread.start();
			}
		}//for
		*/
		
		// TODO: SoundSystem quick play test //
		_soundSystem.quickPlay(true, filename, false, 0, 0, 0, SoundSystemConfig.ATTENUATION_NONE, 0.0f);
		
	}//playSound
	
	public int getSoundCount(){ /*return _sounds.size();*/ GameSkeleton.printDebugMessage("****SoundManager.getSoundCount() is deprecated and returns -1!"); return -1;}
	
	/**
	 * Plays sound at index specified
	 * @warning this function uses a system call to get current milliseconds to randomize thread name
	 * @param index
	 */
	//public void playSoundAtIndex(int index){
		
		/*
		//_sounds.get(index).playSound();
		//_sounds.get(index).playSoundThenRewind();
		
		// this works
		//Thread tempThread = new Thread (new SoundTest2(_sounds.get(index)), "sound " + index + "_" + System.currentTimeMillis());
		//tempThread.start();
		
		// if the sound is not still playing, play it again
		if(!_sounds.get(index).isStillPlaying()){
			Thread tempThread = new Thread (new GameSound(_sounds.get(index)), "sound " + index);
			tempThread.start();
		}
		*/
	
	//	GameSkeleton.printDebugMessage("****SoundManager.playSoundAt() is deprecated and does nothing!");
		
	//}//playSound
	
	public void stopSoundAtIndex(int index){
		
		//_sounds.get(index).stopClip();
		GameSkeleton.printDebugMessage("****SoundManager.stopSoundAt() is deprecated and does nothing!");
		
	}
	

	
}//SoundMan
