package com.rjw.sfx;

import javax.swing.*;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import javax.sound.sampled.*;
import javax.sound.midi.*;

import com.rjw.gameskeleton.GameSkeleton;;

/**
 * custom sound class
 * @author rwalsh
 *
 */
public class GameSound implements Runnable, LineListener, MetaEventListener  {
	
    final int bufSize = 16384;
    Thread thread;
    Sequencer sequencer;
    boolean midiEOM, audioEOM;
    Synthesizer synthesizer;
    MidiChannel channels[]; 
    Object currentSound;
    String currentName;
    double duration;
    int num;
    boolean bump;
    boolean paused = false;
    float gainVal, panVal;
    JSlider seekSlider;
    String errStr;
    Clip testGlobalClip;
    
    private String _filename;
    private File _soundFile;

	public GameSound(String filename){
		
		_filename = filename;
		_soundFile = new File(_filename);
		
		if(!_soundFile.canRead()){
			GameSkeleton.printDebugMessage("Cannot read file '"+_filename+"'!");
		}
		
		//TODO: load sound in contstructor - hmm, this results in the sound only playing once. Perhaps rewind the clip?
		if(loadSound() != true){
			GameSkeleton.printDebugMessage("Cannot load file '"+_filename+"'!");
			System.exit(0);
		}
		
		//run();
		
	}//constructor
	
	/**
	 * Copy constructor - makes sound from sound object passed in
	 * @param toCopy
	 */
	public GameSound(GameSound toCopy){
		
		_filename = toCopy.getFilename();
		_soundFile = toCopy.getSoundFile();
		
		// prep stuff that would have been loaded in loadSound
		currentName = _filename;
		currentSound = toCopy.getCurrentSound();
		duration = toCopy.getDuration();
		
		
	}//copyConstructor
	
    public String getFilename(){ return _filename; }
    public File getSoundFile(){ return _soundFile; }
    public int getBufSize() { return bufSize; }
	public Thread getThread() {	return thread; }
	public Sequencer getSequencer() {return sequencer;}
	public boolean isMidiEOM() {return midiEOM;}
	public boolean isAudioEOM() {return audioEOM;}
	public Synthesizer getSynthesizer() {return synthesizer;}
	public MidiChannel[] getChannels() {return channels;}
	public Object getCurrentSound() {return currentSound;}
	public String getCurrentName() {return currentName;	}
	public int getNum() {return num;}
	public boolean isBump() {return bump;}
	public boolean isPaused() {return paused;}
	public float getGainVal() {return gainVal;}
	public float getPanVal() {return panVal;}
	public JSlider getSeekSlider() {return seekSlider;}
	public String getErrStr() {	return errStr;}
	public Clip getTestGlobalClip() {return testGlobalClip;}
	public String get_filename() {return _filename;}
	public File get_soundFile() {return _soundFile;}

	public boolean isStillPlaying(){ return ((Clip)currentSound).isActive(); }
	
	/**
     * the run method when we invoke this as a thread
     */
    public void run() {
    	
        playSound();
        
    }//run
	

    /**
     * Loads our sounds
     * @return
     */
	public boolean loadSound() {
	
		File soundFile = new File(_filename);
		
		duration = 0.0;

		//GameSkeleton.printDebugMessage("SoundTest2: Trying to load a file called " + _filename);
	
		currentName = _filename;
		
		try {
			currentSound = AudioSystem.getAudioInputStream(_soundFile);
		} catch(Exception e1) {
	
			try { 
				FileInputStream is = new FileInputStream(_soundFile);
				currentSound = new BufferedInputStream(is, 1024);
			} catch (Exception e3) { 
				e3.printStackTrace(); 
				currentSound = null;
				return false;
			}
	
		}//catch

		try {
			
			AudioInputStream stream = (AudioInputStream) currentSound;
			AudioFormat format = stream.getFormat();
	
			/**
			 * we can't yet open the device for ALAW/ULAW playback,
			 * convert ALAW/ULAW to PCM
			 */
			if ((format.getEncoding() == AudioFormat.Encoding.ULAW) ||
					(format.getEncoding() == AudioFormat.Encoding.ALAW)) 
			{
				GameSkeleton.printDebugMessage("covert to PCM");
				AudioFormat tmp = new AudioFormat(
						AudioFormat.Encoding.PCM_SIGNED, 
						format.getSampleRate(),
						format.getSampleSizeInBits() * 2,
						format.getChannels(),
						format.getFrameSize() * 2,
						format.getFrameRate(),
						true);
				stream = AudioSystem.getAudioInputStream(tmp, stream);
				format = tmp;
				
			}//if
			
			DataLine.Info info = new DataLine.Info(
					Clip.class, 
					stream.getFormat(), 
					((int) stream.getFrameLength() *
							format.getFrameSize()));
	
			//Clip clip = (Clip) AudioSystem.getLine(info);
			//clip.addLineListener(this);
			//clip.open(stream);
			
			//currentSound = clip;

			testGlobalClip = (Clip) AudioSystem.getLine(info);
			testGlobalClip.addLineListener(this);
			testGlobalClip.open(stream);
			
			currentSound = testGlobalClip;
			
		} catch (Exception ex) { 
			
			ex.printStackTrace(); 
			currentSound = null;
			return false;
			
		}//catch
	
		
		if(currentSound == null){
			GameSkeleton.printDebugMessage("SoundTest2: currentSound called '"+_filename+"' is null!");
			return false;
		}
	
		duration = getDuration();
		bump = false;
	
		return true;
			
	}//loadSound
	
    public void playSound() {
    	
    	//GameSkeleton.printDebugMessage("in play method");
    	//Clip clip = (Clip) currentSound;
    	
    	//TODO: add a sleep loop at the start so we don't restart the sound before it's done playing
        //while(clip.isActive() && !bump){
    	while(((Clip)currentSound).isActive() && !bump){
        	try{
        		thread.sleep(99);
        	}catch(Exception e){break;}
        }

    	//if(clip == null)
    	if(((Clip)currentSound) == null)
    		GameSkeleton.printDebugMessage("SoundTest2: clip is null :(");
    	
    	//TODO: try to set to zero so we can play again?
    	//clip.setFramePosition(0);
    	((Clip)currentSound).setFramePosition(0);
    	
        //clip.start();
    	((Clip)currentSound).start();
    	
        try{
        	thread.sleep(99);
        }catch(Exception e){}
        
        //while((paused || clip.isActive()) && thread != null && !bump){
        //while(clip.isActive() && !bump){
        while(((Clip)currentSound).isActive() && !bump){
        	try{
        		thread.sleep(99);
        	}catch(Exception e){break;}
        }
        
        //clip.stop();
        ((Clip)currentSound).stop();
        //clip.close();
            
        //currentSound = null;
        
    }//playSound
    
    public void playSoundThenRewind() {
    	
    	//GameSkeleton.printDebugMessage("in play method");

    	Clip clip = (Clip) currentSound;
        clip.start();
        try{
        	thread.sleep(99);
        }catch(Exception e){}
        
        //while((paused || clip.isActive()) && thread != null && !bump){
        while(clip.isActive() && !bump){
        	try{
        		thread.sleep(99);
        	}catch(Exception e){break;}
        }
        
        clip.stop();
        //clip.setFramePosition(0);
        clip.close();
            
        currentSound = null;
        
    }//playSound
    
    public void update(LineEvent event) {
        if (event.getType() == LineEvent.Type.STOP && !paused) { 
            audioEOM = true;
        }
    }
	
    public void meta(MetaMessage message) {
        if (message.getType() == 47) {  // 47 is end of track
            midiEOM = true;
        }
    }

    public double getDuration() {
    	
    	/*
        double duration = 0.0;

        Clip clip = (Clip) currentSound;
        duration = clip.getBufferSize() / (clip.getFormat().getFrameSize() * clip.getFormat().getFrameRate());
       
        GameSkeleton.printDebugMessage("bufSize["+clip.getBufferSize()+"], frameSize["+clip.getFormat().getFrameSize()+"], frameRate["+clip.getFormat().getFrameRate()+"], format["+clip.getFormat()+"]");

        return duration;
        */
    	
    	//TODO: optimization
        return ((Clip)currentSound).getBufferSize() / (((Clip)currentSound).getFormat().getFrameSize() * ((Clip)currentSound).getFormat().getFrameRate());
    }


    public double getSeconds() {
    	
        double seconds = 0.0;
            Clip clip = (Clip) currentSound;
            seconds = clip.getFramePosition() / clip.getFormat().getFrameRate();

        return seconds;
        
    }//getSeconds
    
    public void stopClip(){
    	((Clip)currentSound).stop();
    }

}//SoundTest2

