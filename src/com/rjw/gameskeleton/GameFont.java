package com.rjw.gameskeleton;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

/**
 * This class is currently based on a single image that is 4096x32, with
 * letters A-Z, 0-9, and punctuation ':','.',',','!', and '?'. Each 
 * character is 32x32 pixels, and is indexed based on it's ASCII value
 * (e.g. 'A' = 65).
 * @author rwalsh
 */
class GameFont {
	
	// IMAGE FONT
	static final String SPRITE_IMAGE_FONT_CAPS = "sprite_alphabet_128_small.png";
	static final int CHAR_WIDTH = 16;
	static final int CHAR_HEIGHT = 16;
	static final int CHARS_SUPPORTED = 128;

	
	private Sprite _fontSprite;
	//private java.util.Map<char,int> _characterLookup;
	private Graphics fontGraphics;
	
	public GameFont(){
		_fontSprite = new Sprite(SPRITE_IMAGE_FONT_CAPS); 
	}//constructor
	
	public BufferedImage createImageFromString(String message){
		
		//char[] messageChars = new char[message.length()];
		//messageChars = message.toCharArray();
		char[] messageChars = message.toCharArray();
		
		BufferedImage messageImage = new BufferedImage(message.length()*CHAR_WIDTH, CHAR_HEIGHT, BufferedImage.TYPE_INT_ARGB);
		fontGraphics = messageImage.getGraphics();
		
		for(int charIndex = 0; charIndex < messageChars.length; charIndex++){
			
			//GameSkeleton.printDebugMessage(messageChars[charIndex] + ": " + (int)(messageChars[charIndex]));

			//check character bounds, since we only support the first 128 ASCII chars
			if((int)(messageChars[charIndex]) <= CHARS_SUPPORTED){
			
				//TODO: this makes 3 function calls...perhaps make this a raw image and not a sprite for efficiency's sake
				//fontGraphics.setColor(Color.BLUE);
				//fontGraphics.drawRect(0, 0, messageImage.getWidth(), messageImage.getHeight());
				fontGraphics.drawImage(_fontSprite.getImage().getSubimage((int)(messageChars[charIndex])*CHAR_WIDTH, 0, CHAR_WIDTH, CHAR_HEIGHT), charIndex*CHAR_WIDTH, 0, null);
			
				
				
			}
				
		}
		
		//fontGraphics.set
		
		return messageImage;
		
	}//createImageFromString
	
}//GameFont

