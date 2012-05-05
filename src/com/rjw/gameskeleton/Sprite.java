package com.rjw.gameskeleton;

import java.awt.AlphaComposite;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.RasterFormatException;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

//TODO: make this class a little more efficient...eventually...

public class Sprite {
	
	private int _x;
	private int _y;
	private int _w;
	private int _h;
	private String _filename;
	private BufferedImage _img;

	/*
	 * @param filename of sprite image
	 * This constructor uses default width and height, and starts with x and y at zero
	 */
	public Sprite(String filename){
		
		_filename = filename;
		
		// get and crop the image
		try {
			
			//_img = new BufferedImage(32, 64, BufferedImage.TYPE_INT_ARGB, null);
			_img = ImageIO.read(new File(OtherStuff.SPRITE_PATH_PREFIX + filename));
			_h = _img.getHeight();
			_w = _img.getWidth();
			
		}catch (Exception e){
			GameSkeleton.printDebugMessage("Error loading sprite '"+OtherStuff.SPRITE_PATH_PREFIX + filename+"': " + e.getMessage());
		}
		
	}//ConstructorSimple
	
	/*
	 * @param filename of sprite image, starting x, starting y, width, height 
	 */
	public Sprite(String filename, int x, int y, int w, int h) throws Exception{
		
		this(filename);
		if(!this.cropSprite(x, y, w, h)){
			throw new Exception("Crop didn't work for " + filename);
		}
		
	}//Constructor
	
	/*
	 * @param filename of sprite image, starting x, starting y, width, height 
	 */
	public Sprite(String filename, int x, int y){
		
		this(filename);
		_x = x;
		_y = y;
		
	}//Constructor
	
	//TODO: make contstructor that takes BufferedImage directly
	
	//public void drawSprite(Graphics g){
		//
	//}
	
	public BufferedImage getImage(){
		return _img;
	}
	
	/*
	 * function to crop the sprite to a subimage of the file given
	 */
	private boolean cropSprite(int x, int y, int w, int h){
		
		boolean cropWorked = true;
		
		try{
			_img = _img.getSubimage(x, y, w, h);
		}catch(RasterFormatException rfe){
			GameSkeleton.printDebugMessage("Error cropping sprite '"+_filename+"' (" + rfe.getMessage() + ")");
			cropWorked = false;
		}
		
		return cropWorked;
		
	}//cropSprite

	/*
	 * From the web: http://java.sun.com/products/java-media/2D/reference/faqs/index.html#Q_How_do_I_create_a_resized_copy
	 * For use later when optimizing
	 */
	/*
	BufferedImage createResizedCopy(Image originalImage, int scaledWidth, int scaledHeight, boolean preserveAlpha){
		
		int imageType = preserveAlpha ? BufferedImage.TYPE_INT_RGB : BufferedImage.TYPE_INT_ARGB;
		BufferedImage scaledBI = new BufferedImage(scaledWidth, scaledHeight, imageType);
		Graphics2D g = scaledBI.createGraphics();
		
		if (preserveAlpha) {
			g.setComposite(AlphaComposite.Src);
		}
		
		g.drawImage(originalImage, 0, 0, scaledWidth, scaledHeight, null); 
		g.dispose();
		return scaledBI;
		
	}//createResizedCopy
	*/

	public void setX(int x){ _x = x; }
	public void setY(int y){ _y = y; }
	
	public int getX() { return _x; }
	public int getY() { return _y; }
	public int getW() { return _w; }
	public int getH() { return _h; }

}//Sprite
