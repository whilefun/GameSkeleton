package com.rjw.sfx;

import paulscode.sound.SoundSystem;
import paulscode.sound.SoundSystemConfig;
import paulscode.sound.SoundSystemException;
import paulscode.sound.libraries.LibraryLWJGLOpenAL;
import paulscode.sound.libraries.LibraryJavaSound;
import paulscode.sound.codecs.CodecWav;
import paulscode.sound.codecs.CodecJOgg;
/**
 * Plays background music and explosions in 3D.
 **/
public class Example_2
{
	public static void main( String[] args )
	{
		new Example_2();
	}
	public Example_2()
	{
		// Load some library and codec plug-ins:
		try
		{
			SoundSystemConfig.addLibrary( LibraryLWJGLOpenAL.class );
			SoundSystemConfig.addLibrary( LibraryJavaSound.class );
			SoundSystemConfig.setCodec( "wav", CodecWav.class );
			SoundSystemConfig.setCodec( "ogg", CodecJOgg.class );
		}
		catch( SoundSystemException e )
		{
			System.err.println("error linking with the plug-ins" );
		}
		// Instantiate the SoundSystem:
		SoundSystem mySoundSystem = new SoundSystem();
		// play some background music:
		mySoundSystem.backgroundMusic( "Music 1", "beats.ogg", true );
		// wait a bit before playing the explosions:
		sleep( 2000 );
		// play 15 explosions, right and left:
		for( int x = 0; x < 15; x++ )
		{
			// If x is divisible by 2, play to the right:
			if( x % 2 == 0 )
				mySoundSystem.quickPlay( false, "explosion.wav", false,
						20, 0, 0,
						SoundSystemConfig.ATTENUATION_ROLLOFF,
						SoundSystemConfig.getDefaultRolloff()
				);
			// Otherwise play to the left:
			else
				mySoundSystem.quickPlay( false, "explosion.wav", false,
						-20, 0, 0,
						SoundSystemConfig.ATTENUATION_ROLLOFF,
						SoundSystemConfig.getDefaultRolloff()
				);
			// wait a bit so the explosions don't all start at once
			sleep( 125 );
		}
		// Wait a few seconds:
		sleep( 10000 );
		// Shut down:
		mySoundSystem.cleanup();
	}
	public void sleep( long milliseconds )
	{
		try
		{
			Thread.sleep( milliseconds );
		}
		catch( Exception e )
		{}
	}
}