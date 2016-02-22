package com.rjw.editor;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Vector;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.rjw.gameskeleton.OtherStuff;
import com.rjw.gameskeleton.Thing;
import com.rjw.gameskeleton.ThingManager;
import com.rjw.gameskeleton.GameSkeleton;

/**
 * This class is basic and houses a big tilemap made of Things and some
 * pointers to keep track of the size and dimensions of the grid, which
 * Thing is selected/loaded, etc.
 * @author rwalsh
 * Note: Recommended size of the tileset image is 224px x 320px (or 7 tiles x 10 tiles)
 * Note: thing things will have to have an iconic representation in the ThingSet that
 * is the size of one tile, but when placed, they should be the right size according
 * to the Thing  
 */
public class ThingSet {

	private String _thingSetFilename;
	private BufferedImage _thingsetImage;
	private Graphics _thingsetImageGraphics;
	private Vector<Thing> _things;
	private int _tilemapWidthInTiles;
	private int _tilemapHeightInTiles;
	private int _tilemapImageWidth;
	private int _tilemapImageHeight;
	
	public ThingSet(String thingSetFilename){
		
		//TODO: read xml file
		
		
		
		boolean successful = true;
		File f = new File(thingSetFilename);
		int rowCount = -1;				// keeps track of our rows, since every Map child may not be a row
										// start it at -1 so we can "++" everytime with no check for first time
		int colCount = -1;				// keeps track of our cols, since every Row child may not be a Tile
										// start it at -1 so we can "++" everytime with no check for first time
		
		if(f.canRead()){
		
			//init our things vector
			_things = new Vector<Thing>();
			
			try{
				
				DocumentBuilderFactory dbfac = DocumentBuilderFactory.newInstance();
				DocumentBuilder docBuilder = dbfac.newDocumentBuilder();
				Document doc = docBuilder.parse(f);
				
				//ThingSet//
				Node thingset = doc.getFirstChild();
				NamedNodeMap thingsetAttributes = thingset.getAttributes();
			
				//print level attributes
				//GameSkeleton.printDebugMessage("ThingSet name is: " + thingsetAttributes.getNamedItem("name").getNodeValue());
				//GameSkeleton.printDebugMessage("ThingSet number is: " + thingsetAttributes.getNamedItem("number").getNodeValue());
				//GameSkeleton.printDebugMessage("ThingSet type is: " + thingsetAttributes.getNamedItem("type").getNodeValue());
	
				//Thing(s)//
				
				NodeList thingsetChildren = thingset.getChildNodes();
				//GameSkeleton.printDebugMessage("thingset has children count of: " + thingsetChildren.getLength());
				
				//for all children of the level, check them out
				for(int childCount = 0; childCount < thingsetChildren.getLength(); childCount++){
					
					Node tempNode = thingsetChildren.item(childCount);
					
					//GameSkeleton.printDebugMessage(tempNode.getNodeName());
					
					//Thing//
					if(tempNode.getNodeName() == "Thing"){
						
						NamedNodeMap thingAttr = tempNode.getAttributes();
						
						//GameSkeleton.printDebugMessage("Thing Type: " + thingAttr.getNamedItem("type").getNodeValue());
						
						/*
						// Switch on the Thing type and add the appropriate thing to the ThingSet
						switch (Integer.parseInt(thingAttr.getNamedItem("id").getNodeValue())){
						
							case OtherStuff.THING_ID_AVATAR:
								_things.add(new Avatar(0, 0, 0, 0, 0, 0));
								break;
							case OtherStuff.THING_ID_PROJECTILE:
								_things.add(new Projectile(0, 0, 0, 0, 0, 0));
								break;
							case OtherStuff.THING_ID_OFFICEBUILDING_001:
								_things.add(new OfficeBuilding001(0, 0));
								break;
							case OtherStuff.THING_ID_PLAYERSTART:
								_things.add(new PlayerStart(0,0));
								break;
							case OtherStuff.THING_ID_BAD_GUY_TEST:
								_things.add(new BadGuyTest(0,0,BadGuyTest.VELOCITY_X, BadGuyTest.VELOCITY_Y));
								break;
							case OtherStuff.THING_ID_PROJECTILE_ORB_01:
								_things.add(new Orb(0,0,Orb.VELOCITY_X, Orb.VELOCITY_Y, 0, 0));
								break;
							case OtherStuff.THING_ID_CLOUD_001:
								_things.add(new Cloud_001(0,0,Cloud_001.VELOCITY_X, Cloud_001.VELOCITY_Y, 0, 0));
								break;
							default:
								break; 
						
						}//switchThingID
						*/
						
						//TODO: testing ThingManager
						_things.add((Thing)ThingManager.createThingWithID(Integer.parseInt(thingAttr.getNamedItem("id").getNodeValue()), 0, 0));
						
					}//ifThing

				}//forThingSetNodes	
			
			}catch ( Exception e ){
				GameSkeleton.printDebugMessage( "ThingSet: Error parsing xml file: class("+e.getClass()+"), message:" + e.getMessage() );
			}
		
		}else{
			GameSkeleton.printDebugMessage("ThingSet: Failed to initialize ThingSet");
		}
		
		
		// Check our _things size. If it is greater than the 
		// capacity of our palette, then throw an error.
		if(_things.size() > (OtherStuff.THINGMAP_DEFAULT_WIDTH * OtherStuff.THINGMAP_DEFAULT_HEIGHT)){
			GameSkeleton.printDebugMessage("ThingSet: Cannot load ThingSet in '" + thingSetFilename + "'. It contains more than " + ((int)(OtherStuff.THINGMAP_DEFAULT_WIDTH * OtherStuff.THINGMAP_DEFAULT_HEIGHT)) + " things.");
		}
		
		// we know it will always be THINGMAP_DEFAULT_WIDTH * EDITOR_TILE_SIZE by THINGMAP_DEFAULT_HEIGHT * EDITOR_TILE_SIZE
		_tilemapImageWidth = OtherStuff.THINGMAP_DEFAULT_WIDTH * OtherStuff.EDITOR_TILE_SIZE;
		_tilemapImageHeight = OtherStuff.THINGMAP_DEFAULT_HEIGHT * OtherStuff.EDITOR_TILE_SIZE;
		
		//TODO: create thingset image
		_thingsetImage = new BufferedImage(_tilemapImageWidth, _tilemapImageHeight, BufferedImage.TYPE_INT_RGB);
		_thingsetImageGraphics = _thingsetImage.getGraphics();

		
		// Next, create an image of the Things loaded to overlay on the
		// thingsetpalette in our editor
		for(int thingIndex = 0; thingIndex < _things.size(); thingIndex++){
		
			// just mod (round) off based on our palette width
			_thingsetImageGraphics.drawImage(_things.get(thingIndex).getMainImage().getScaledInstance(OtherStuff.EDITOR_TILE_SIZE, OtherStuff.EDITOR_TILE_SIZE, Image.SCALE_FAST),
											(thingIndex % OtherStuff.THINGMAP_DEFAULT_WIDTH)* OtherStuff.EDITOR_TILE_SIZE,
											((thingIndex / OtherStuff.THINGMAP_DEFAULT_WIDTH) * OtherStuff.EDITOR_TILE_SIZE), 
											null);
			
			_thingsetImageGraphics.drawRect((thingIndex%OtherStuff.THINGMAP_DEFAULT_WIDTH)* OtherStuff.EDITOR_TILE_SIZE, 
											(thingIndex / OtherStuff.THINGMAP_DEFAULT_WIDTH) * OtherStuff.EDITOR_TILE_SIZE,
											OtherStuff.EDITOR_TILE_SIZE,
											OtherStuff.EDITOR_TILE_SIZE);

			
		}//for
	
		_thingSetFilename = thingSetFilename;
		_tilemapWidthInTiles = OtherStuff.TILEMAP_DEFAULT_WIDTH;
		_tilemapHeightInTiles = OtherStuff.TILEMAP_DEFAULT_HEIGHT;
		
	}//constructor
	
	/**
	 * This function returns a Thing based on the position clicked 
	 * in the grid, represented by xPos/yPos. If 2,3 is passed, then
	 * the Thing from position (2,3) is returned
	 * @param xPos
	 * @param yPos
	 * @return the Thing from the position clicked, or null if xPos/yPos 
	 * lead to out of bounds index
	 */
	public Thing getThingFromSet(int xPos, int yPos){ 
		
		//TODO: make this return the right thing
		//GameSkeleton.printDebugMessage("ThingSet: getting tile from set @ ["+xPos+","+yPos+"]");
		//return new OfficeBuilding001(0,0);
		
		
		// Since we know the width of our palette, just translate the x,y
		// into a linear index using (y*width + x), assumging it's in bounds
		if( ((yPos * OtherStuff.THINGMAP_DEFAULT_WIDTH) + xPos) < _things.size()){
			return _things.get( (yPos * OtherStuff.THINGMAP_DEFAULT_WIDTH) + xPos );
		}else{
			return null;
		}
		
	}//getTileFromSet

	/**
	 * This function will return an image based on the grid of Things 
	 * @return an Image comprised of all the icons of the Things in the set
	 */
	public BufferedImage getImage(){
		
		//TODO: return an image based on the set
		return _thingsetImage;
		
	}//getImage
	
	public int getImageWidth(){ return _tilemapImageWidth; }
	public int getImageHeight(){ return _tilemapImageHeight; }
	public int getTilemapWidthInTiles(){ return _tilemapWidthInTiles; }
	public int getTilemapHeightInTiles(){ return _tilemapHeightInTiles; }
	
	
}//ThingSet
