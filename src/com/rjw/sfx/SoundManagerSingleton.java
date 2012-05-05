package com.rjw.sfx;

import java.net.MalformedURLException;
import java.net.URL;

import paulscode.sound.FilenameURL;
import paulscode.sound.SoundSystem;
import paulscode.sound.SoundSystemConfig;
import paulscode.sound.SoundSystemException;
import paulscode.sound.codecs.CodecJOgg;
import paulscode.sound.codecs.CodecWav;
import paulscode.sound.libraries.LibraryJavaSound;
//import paulscode.sound.libraries.LibraryLWJGLOpenAL;


import com.rjw.gameskeleton.GameSkeleton;

public class SoundManagerSingleton {

	private SoundSystem _soundSystem;	
	private static SoundManagerSingleton ref;

	
	private SoundManagerSingleton(){

		
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
			//mySoundSystem = new SoundSystem( LibraryLWJGLOpenAL.class );
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
		 
		 
		
	}//SoundManagerSingleton
	
	public static synchronized SoundManagerSingleton getSingletonObject(){
		
      if (ref == null){
          // it's ok, we can call this constructor
          ref = new SoundManagerSingleton();
      }
    	 
      return ref;     
      
    }//getSingleton

	 public Object clone()
		throws CloneNotSupportedException
	  {
	    throw new CloneNotSupportedException(); 
	    // that'll teach 'em
	  }

	
	public void playSound(String filename){
		
		// TODO: SoundSystem quick play test //
		_soundSystem.quickPlay(true, filename, false, 0, 0, 0, SoundSystemConfig.ATTENUATION_NONE, 0.0f);
		
		//_soundSystem.play(filename);
		
	}//playSound
	
	public int getSoundCount(){ /*return _sounds.size();*/ GameSkeleton.printDebugMessage("****SoundManager.getSoundCount() is deprecated and returns -1!"); return -1;}
	
	/**
	 * Stops the specified sound file, if it's playing
	 * @param filename - the name of the sound file to stop
	 */
	public void stopSound(String filename){
		
		//_sounds.get(index).stopClip();
		//GameSkeleton.printDebugMessage("****SoundManager.stopSoundAt() is deprecated and does nothing!");
		_soundSystem.stop(filename);
		
	}//stopSound
	
	/**
	 * Loads sound (for pre-loading to reduce lag/stutter on first play)
	 * @param filename
	 */
	public void loadSound(String filename){
		_soundSystem.loadSound(filename);
		//_soundSystem.loadSound(new FilenameURL(filename).getURL(), filename);
	}//loadSound
	
	/*
	public void loadSound(String url, String filename){
		
		//_soundSystem.loadSound(filename);
		_soundSystem.loadSound(new FilenameURL(filename).getURL(), filename);
		
	}//loadSound
	*/
	
	/**
	 * Checks the sound system to see if the specified sound is currently playing.
	 * If it is, true is returned. Else, false.
	 * @param filename - the name of the sound file to check for
	 * @return true if specified sound is playing, false otherwise
	 */
	public boolean isSoundPlaying(String filename){
		GameSkeleton.printDebugMessage("***SoundManagerSingleton.isSoundPlaying()...");
		return _soundSystem.playing(filename);
	}//sourcePlaying
	
	/**
	 * Plays the specified filename sound if not already playing
	 * @param filename - the sound file to play
	 */
	public void playSoundIfNotPlaying(String filename){
		
		if(!_soundSystem.playing(filename)){
			//_soundSystem.play(filename);
			_soundSystem.quickPlay(true, filename, false, 0, 0, 0, SoundSystemConfig.ATTENUATION_NONE, 0.0f);
		}else{
			GameSkeleton.printDebugMessage("****Sound " + filename + " is already playing!");
		}
	
	}//playSoundIfNotPlaying
	
	
	
	
}//SoundManagerSingleton
