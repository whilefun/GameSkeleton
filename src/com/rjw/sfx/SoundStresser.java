package com.rjw.sfx;

import javax.sound.midi.MidiSystem;
import javax.sound.sampled.AudioSystem;
import com.rjw.gameskeleton.GameSkeleton;;

public class SoundStresser {

	public static void main(String argc[]){
		
		try { 
            if (MidiSystem.getSequencer() == null) {
                System.err.println("MidiSystem Sequencer Unavailable, exiting!");
                System.exit(1);
            } else if (AudioSystem.getMixer(null) == null) {
                System.err.println("AudioSystem Unavailable, exiting!");
                System.exit(1);
            }
        } catch (Exception ex) { ex.printStackTrace(); System.exit(1); }
		
        // Threads
		//GameSkeleton.printDebugMessage("Stressing SoundTest Class...");
		Thread t1 = new Thread (new GameSound("sounds/sound_sfx_avatar_death.wav"), "sound 1");
		Thread t2 = new Thread (new GameSound("sounds/sound_sfx_weapon_missile_fire.wav"), "sound 2");
		Thread t3 = new Thread (new GameSound("sounds/sound_sfx_avatar_engine_44100_mono.wav"), "sound 3");
		
		t1.start();
		t2.start();
		t3.start();

		
		GameSkeleton.printDebugMessage("Testing SoundMan class with 2 sounds...");
		
		//SoundManager_Deprecated sm = new SoundManager_Deprecated();
		//sm.loadSound("sounds/sound_sfx_avatar_death.wav");
		//sm.loadSound("sounds/sound_sfx_weapon_missile_fire.wav");

		//sm.playSoundAtIndex(0);
		//sm.playSoundAtIndex(1);


		// ghetto sleep for testing quickly
		for(long i = 0; i < 999999999; i++){}
		
		//sm.playSoundAtIndex(0);
		//sm.playSoundAtIndex(1);
		
		
	}//main
	
}//SoundStresser
