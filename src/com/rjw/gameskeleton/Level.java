package com.rjw.gameskeleton;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileWriter;
import java.io.StringWriter;
import java.util.Vector;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Comment;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.rjw.editor.PlayerStart;
import com.rjw.editor.TileSet;

/**
 * Houses our level stuff
 * @author rwalsh
 *
 */
public class Level {

	public static int VIEWABLE_TOP_LEFT_X = 0;
	public static int VIEWABLE_TOP_LEFT_Y = 1;
	public static int VIEWABLE_BOTTOM_RIGHT_X = 2;
	public static int VIEWABLE_BOTTOM_RIGHT_Y = 3;
	
	public static final int LEVEL_LOOP_FALSE = 0;
	public static final int LEVEL_LOOP_ONCE = 1;
	public static final int LEVEL_LOOP_FOREVER= 2;
	
	private String _filename;
	private String _name;
	private Vector<LevelLayer> _layers;
	private int _currentLayer;
	private int _loopState;
	
	// TODO:
	// the end of our level as a relative Y coordinate. E.g. if map is
	// 100 pixels high and the level end is 80, then the level will end
	// when the avatar hits Y value in the map at 80
	private int _levelEndYCoord;
	
	
	//private short[] _viewablePixelArea;
	
	
	
	// TILESETS //
	// example xml
	// <TileSets count="3">
	// <TileSet name="sprite_tilemap_blank.png" width="7" height="10"/>
	// <TileSet name="sprite_tilemap_test_01.png" width="7" height="10"/>
	// <TileSet name="sprite_tilemap_test_02.png" width="7" height="10"/>
	// </TileSets>
	//TODO: change MapEditor to only load tile sets in the palette that are 
	// used in the map?? Or maybe I want them all for creativities sake? But
	// it's important that the ones I use in addition are also included in 
	// the Level's tilesets so they can be saved witht he level! Hmm...
	//
	//TODO-2: Currently, each time a tileset is created and added to the
	// palette, it must also be manually added to the level xml. This should
	// be fixed so that when saving, all used tilesets are saved too.
	private TileSet[] _tilesets;
	
	//private Hashtable<String, short>
	
	/*
	 * Creates a level from the xml file called filename
	 */
	public Level(String filename){
		
		//does nothing-ish for now
		_filename = filename;
		_layers = new Vector<LevelLayer>();
		_levelEndYCoord = -200;
		_loopState = -1;
		
    	//_viewablePixelArea = new short[4];
    	//this.setViewablePixelArea((short)0, (short)0, (short)32, (short)32);
		
	}//Constructor
	

	public String getFilename(){ return _filename; }	
	// our new layer based level
	public int layerCount(){ return _layers.size(); }
	public Map getMapAtIndex(int index){ return _layers.get(index).getMap(); }
	//public Vector<Thing> getThingsAtIndex(int index){ return _layers.get(index).getThings(); }
	public Thing[] getThingsAtIndex(int index){ return _layers.get(index).getThings(); }
	public int getNumberOfLayers(){ return _layers.size(); }
	public LevelLayer getLayerAtIndex(int index){ return _layers.get(index); }
	public void changeFileNameTo(String newFilename){ _filename = newFilename; }
	
	public int getLevelEndYCoord(){ return _levelEndYCoord; }
	
	public int getLoopState(){ return _loopState; }
	public void setLoopState(int loopState){ _loopState = loopState; }
	
	public String getLevelName(){ return _name; }
	public void setLevelName(String newName){ _name = newName; }
	
	/**
	 * Loads a level from xml file called filename
	 * @param filename
	 */
	//TODO: change name once layers are working
	public boolean loadLevelWithLayers(String filename){
		
		Map _tempMap = null;
		//Vector<Thing> _tempThings = new Vector<Thing>();
		Thing[] _tempThings;
		int thingIndex = 0;
		boolean successful = true;
		File f = new File(filename);
		//int rowCount = -1;				// keeps track of our rows, since every Map child may not be a row
										// start it at -1 so we can "++" everytime with no check for first time
		//int colCount = -1;				// keeps track of our cols, since every Row child may not be a Tile
										// start it at -1 so we can "++" everytime with no check for first time
		
		if(f.canRead()){
		
			try{
				
				DocumentBuilderFactory dbfac = DocumentBuilderFactory.newInstance();
				DocumentBuilder docBuilder = dbfac.newDocumentBuilder();
				Document doc = docBuilder.parse(f);
				
				//Level//
				Node level = doc.getFirstChild();
				NamedNodeMap levelAttributes = level.getAttributes();
			
				// print level attributes
				_name = levelAttributes.getNamedItem("name").getNodeValue();
				//GameSkeleton.printDebugMessage("Level name is: " + levelAttributes.getNamedItem("name").getNodeValue());
				//GameSkeleton.printDebugMessage("Level number is: " + levelAttributes.getNamedItem("number").getNodeValue());
				//GameSkeleton.printDebugMessage("Level type is: " + levelAttributes.getNamedItem("type").getNodeValue());
	
				_loopState = Integer.parseInt(levelAttributes.getNamedItem("looping").getNodeValue());
				//GameSkeleton.printDebugMessage("****Loading level and setting loopState to " + _loopState);
				
				
				NodeList levelChildren = level.getChildNodes();
				//GameSkeleton.printDebugMessage("level has children count of: " + levelChildren.getLength());
				
				//for all children of the level, check them out
				for(int childCount = 0; childCount < levelChildren.getLength(); childCount++){
					
					Node tempNode = levelChildren.item(childCount);

					// Layer //
					if(tempNode.getNodeName() == "Layer"){
						
						NamedNodeMap layerAttr = tempNode.getAttributes();
						NodeList layerChildren = tempNode.getChildNodes();
						
						// create a new layer, and set the map and things to null for now
						_layers.add(new LevelLayer(layerAttr.getNamedItem("name").getNodeValue(),null,null,Integer.parseInt(layerAttr.getNamedItem("scrollspeed").getNodeValue())));
						
						// for all the children of the layer
						for(int layerChildCount = 0; layerChildCount < layerChildren.getLength(); layerChildCount++){
						
							
							//get the next child node of the layer
							Node tempLayerNode = layerChildren.item(layerChildCount);
							//GameSkeleton.printDebugMessage("tempLayerNode: " + tempLayerNode.getNodeName());
							
							//Map//
							if(tempLayerNode.getNodeName() == "Map"){
								
								// clear our tempMap and it's counters
								//_tempMap = new Map(0,0);
								//rowCount = -1;
								//colCount = -1;
								
								NamedNodeMap mapAttr = tempLayerNode.getAttributes();
								//GameSkeleton.printDebugMessage("rows: " + mapAttr.getNamedItem("rows").getNodeValue());
								//GameSkeleton.printDebugMessage("cols: " + mapAttr.getNamedItem("cols").getNodeValue());
								
								//make our new map reflect the actual
								_tempMap = new Map(Integer.parseInt(mapAttr.getNamedItem("rows").getNodeValue()), Integer.parseInt(mapAttr.getNamedItem("cols").getNodeValue()));
								_tempMap.setRowValue(Integer.parseInt(mapAttr.getNamedItem("rows").getNodeValue()));
								_tempMap.setColValue(Integer.parseInt(mapAttr.getNamedItem("cols").getNodeValue()));
								
								NodeList mapChildren = tempLayerNode.getChildNodes();
								
								// for all Map children (i.e. all Rows)
								for(int mapChildCount = 0; mapChildCount < mapChildren.getLength(); mapChildCount++){
									
									//get child nodes (which all need to be Rows!)
									Node tempMapChildNode = mapChildren.item(mapChildCount);
									//GameSkeleton.printDebugMessage(tempMapChildNode.getNodeName());
									
									//TODO: attempt at optimized map format with map coords and tile name and tilemap coords only. no rows, or null tiles stored 
									
									if(tempMapChildNode.getNodeName() == "Tile"){
										

										NamedNodeMap tempAttr = tempMapChildNode.getAttributes();
										
										int tempRow = Integer.parseInt(tempAttr.getNamedItem("mapRow").getNodeValue());
										int tempCol = Integer.parseInt(tempAttr.getNamedItem("mapCol").getNodeValue());
										
										try{

											//TODO: optimize by checking which tilemaps and tiles have already been loaded and load from cache rather than loading tilemap from disk, etc.
											//load the tiles in the style of tilemap loading, it works with all tiles now
											//_tempMap.getMap()[tempRow][tempCol] = new Tile(tempCol*OtherStuff.TILE_SIZE, tempRow*OtherStuff.TILE_SIZE, OtherStuff.TILE_SIZE,OtherStuff.TILE_SIZE, tempRow, tempCol, tempAttr.getNamedItem("name").getNodeValue(), Integer.parseInt(tempAttr.getNamedItem("tilemapX").getNodeValue()), Integer.parseInt(tempAttr.getNamedItem("tilemapY").getNodeValue()));
											
											
											// Note: we're assuming the map found will ALWAYS be there since we are loading all used tilesets from the xml
											// file above. So, just load the tile from the tileset image.
											_tempMap.getMap()[tempRow][tempCol] = new Tile(tempCol*OtherStuff.TILE_SIZE, tempRow*OtherStuff.TILE_SIZE, OtherStuff.TILE_SIZE,OtherStuff.TILE_SIZE, tempRow, tempCol, tempAttr.getNamedItem("name").getNodeValue(), _tilesets[getIndexOfTileSetByName(tempAttr.getNamedItem("name").getNodeValue())].getImage() ,Integer.parseInt(tempAttr.getNamedItem("tilemapX").getNodeValue()), Integer.parseInt(tempAttr.getNamedItem("tilemapY").getNodeValue()), false);
											
											//TODO:
											//GameSkeleton.printDebugMessage("Level: debug001");
											
										}catch(Exception e){
											GameSkeleton.printDebugMessage("Level - loadLevelWithLayers(): Error creating Tile called '" + OtherStuff.SPRITE_PATH_PREFIX + tempAttr.getNamedItem("name").getNodeValue() + "':" + e.getMessage());
											GameSkeleton.printDebugMessage("Map size is W="+_tempMap.getColCount()+" x H="+_tempMap.getRowCount()+":" + e.getMessage());
											GameSkeleton.printDebugMessage("Possible Cause: TileSet was not found...which means it was used in the map and not saved in the xml file");
											System.exit(-1);
										}

									}//ifTile
									
								}//for map children
								
								// then add the map to our current layer being loaded
								_layers.get(_layers.size()-1).setMap(_tempMap);
								
							}//ifMap
							
							//Things//
							if(tempLayerNode.getNodeName() == "Things"){

								NamedNodeMap tempThingsAttr = tempLayerNode.getAttributes();
								
								// clear our tempThings
								//_tempThings = new Vector<Thing>();
								_tempThings = new Thing[Integer.parseInt(tempThingsAttr.getNamedItem("count").getNodeValue())];
								thingIndex = 0;
								
								NodeList thingsChildren = tempLayerNode.getChildNodes();
								
								for(int thingsChildCount = 0; thingsChildCount < thingsChildren.getLength(); thingsChildCount++){
									
									//GameSkeleton.printDebugMessage("for thingsChildren");
									
									Node tempThingsChildNode = thingsChildren.item(thingsChildCount);
									
									//Thing
									if(tempThingsChildNode.getNodeName() == "Thing"){
										
										//GameSkeleton.printDebugMessage("Encountered a THING");
										
										NamedNodeMap tempThingAttr = tempThingsChildNode.getAttributes();

										// get our tempX/Y values, but also don't forget they are relative to the top left
										// of the map, so add the map's X/Y to each of our tempX/Y values to compensate
										int tempMapX = Integer.parseInt(tempThingAttr.getNamedItem("map_x").getNodeValue()) + _tempMap.getX();
										int tempMapY = Integer.parseInt(tempThingAttr.getNamedItem("map_y").getNodeValue()) + _tempMap.getY();								
										
										//GameSkeleton.printDebugMessage("Loading Thing @("+tempMapX+","+tempMapY+"), and map is at ["+_tempMap.getX()+","+_tempMap.getY()+"]");
										
										//TODO: testing ThingManager
										_tempThings[thingIndex] = (Thing)ThingManager.createThingWithID(Integer.parseInt(tempThingAttr.getNamedItem("id").getNodeValue()), tempMapX,tempMapY);
										
										//TODO: if the level is looping, set the latest thing to have isLooping set to true;
										//TODO: IS THIS REALLY NEEDED?
										if(_loopState != Level.LEVEL_LOOP_FALSE){
											_tempThings[thingIndex].setInLoopingLevel(true);
										}
										
										//increment our thingIndex since we added one
										thingIndex++;
										
									}
									
								}//forThings

								// then add the things to our current layer being loaded
								_layers.get(_layers.size()-1).setThings(_tempThings);

							}//IfThings

						}//forLayerChildren
						
					}//ifLayer
					
					// TILESETS //
					if(tempNode.getNodeName() == "TileSets"){
						
						// get our tilesets attributes and children
						NamedNodeMap tilesetsAttr = tempNode.getAttributes();
						NodeList tilesetsChildren = tempNode.getChildNodes();

						//init our tileset array to be the size indicated in the xml "count" attribute
						_tilesets = new TileSet[Integer.parseInt(tilesetsAttr.getNamedItem("count").getNodeValue())];
						
						//to keep track of how many TileSets children are actual "TileSet" nodes and not text, etc.
						int tilesetCount = 0;
						
						// for all the children of the layer
						for(int tilesetsChildCount = 0; tilesetsChildCount < tilesetsChildren.getLength(); tilesetsChildCount++){
						
							//get the next child node of the layer
							Node tempTileSetsNode = tilesetsChildren.item(tilesetsChildCount);
							NamedNodeMap tilesetAttr = tempTileSetsNode.getAttributes();
							
							//TileSet//
							if(tempTileSetsNode.getNodeName() == "TileSet"){
								
								_tilesets[tilesetCount] = new TileSet(tilesetAttr.getNamedItem("name").getNodeValue(),Integer.parseInt(tilesetAttr.getNamedItem("width").getNodeValue()),Integer.parseInt(tilesetAttr.getNamedItem("height").getNodeValue()));
								tilesetCount++;
								
							}//ifTileSet
						
						}//for
						
					}//ifTileSets
						
				}//forLevelNodes				
			
			}catch ( Exception e ){
				GameSkeleton.printDebugMessage( "Level: Error parsing xml file: class("+e.getClass()+"), message:" + e.getMessage() );
			}
		
		}else{
			successful = false;
		}
		
		return successful;
		
	}//loadLevelWithLayers

	/**
	 * Writes current level to filename specified
	 * @return true if write succeeded, false otherwise
	 */
	public boolean writeLevelWithLayers(){
		
		Map _tempMap;
		Vector<Thing> _tempThings;
		boolean successful = true;
		FileWriter levelFileTest;
		
		try{
			
			/////////////////////////////
			//Creating an empty XML Document
			
			//We need a Document
			DocumentBuilderFactory dbfac = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = dbfac.newDocumentBuilder();
			Document doc = docBuilder.newDocument();
			
			////////////////////////
			//Creating the XML tree
			
			//add our level as the root of the document
			Element level = doc.createElement("Level");
			doc.appendChild(level);
			
			//add our level meta data
			level.setAttribute("name", _name);
			level.setAttribute("number", "1");
			level.setAttribute("type", "single player");
			level.setAttribute("looping", ""+_loopState);
			
			//create a comment and put it in the root element
			Comment comment = doc.createComment("this is the first level");
			level.appendChild(comment);
			
			
			// TILESETS //
			
			// add our overall "TileSets" node
			Element tempTileSets = doc.createElement("TileSets");
			tempTileSets.setAttribute("count", ""+_tilesets.length);
			level.appendChild(tempTileSets);
			
			// then for all the actual tilesets, write their info to the file
			for(int tileSetCount = 0; tileSetCount < _tilesets.length; tileSetCount++){
				
				Element tileset = doc.createElement("TileSet");
				tileset.setAttribute("name", ""+_tilesets[tileSetCount].getName());
				tileset.setAttribute("width", ""+_tilesets[tileSetCount].getTilemapWidthInTiles());
				tileset.setAttribute("height", ""+_tilesets[tileSetCount].getTilemapHeightInTiles());
				tempTileSets.appendChild(tileset);				
				
			}//TILESETS
			
			
			
			// LAYERS //
			for(int layerCounter = 0; layerCounter < _layers.size(); layerCounter++){

				//TODO: put actual layer values into the xml for name/num/speed
				// create layer as the child of the level
				Element tempLayer = doc.createElement("Layer");
				tempLayer.setAttribute("name", _layers.get(layerCounter).getName());
				tempLayer.setAttribute("number", ""+layerCounter);
				tempLayer.setAttribute("scrollspeed", ""+_layers.get(layerCounter).getScrollSpeed());
				level.appendChild(tempLayer);
				
				//create map child of LAYER
				Element tempMap = doc.createElement("Map");
				tempMap.setAttribute("rows", ""+_layers.get(layerCounter).getMap().getRowCount());
				tempMap.setAttribute("cols", ""+_layers.get(layerCounter).getMap().getColCount());
				tempLayer.appendChild(tempMap);
			
				// for all tiles in the map
				for(int row = 0; row < _layers.get(layerCounter).getMap().getRowCount(); row++){
					
					//and for all cols in the map
					for(int col = 0; col < _layers.get(layerCounter).getMap().getColCount(); col++){
						
						//TODO: null tile check
						if(_layers.get(layerCounter).getMap().getMap()[row][col] != null){
						
							// create Tile element for this row and col
							Element tile = doc.createElement("Tile");
							tile.setAttribute("name", _layers.get(layerCounter).getMap().getMap()[row][col].getName());
							tile.setAttribute("mapRow", ""+row);
							tile.setAttribute("mapCol", ""+col);
							tile.setAttribute("tilemapX", ""+_layers.get(layerCounter).getMap().getMap()[row][col].getTilemapX());
							tile.setAttribute("tilemapY", ""+_layers.get(layerCounter).getMap().getMap()[row][col].getTilemapY());
							tempMap.appendChild(tile);
							
						}
						
					}//for
					
				}//forMapTiles
				
				//create things child of level
				Element tempThings = doc.createElement("Things");
				//TODO: delete this attr?
				tempThings.setAttribute("count", ""+_layers.get(layerCounter).getThings().length);
				tempLayer.appendChild(tempThings);
				
				//for all our things
				for(int i = 0; i < _layers.get(layerCounter).getThings().length; i++){
					
					//TODO: make this have other info like x,y, etc.
					Element tempThing = doc.createElement("Thing");
					tempThing.setAttribute("id", ""+_layers.get(layerCounter).getThings()[i].getID());
					tempThing.setAttribute("map_x", "" + (_layers.get(layerCounter).getThings()[i].getX() - _layers.get(layerCounter).getMap().getX()));
					tempThing.setAttribute("map_y", "" + (_layers.get(layerCounter).getThings()[i].getY() - _layers.get(layerCounter).getMap().getY()));
					tempThing.setAttribute("name", _layers.get(layerCounter).getThings()[i].getClass().toString());
					
					//GameSkeleton.printDebugMessage("Writing Thing @(" + (_layers.get(layerCounter).getThings().get(i).getX() - _layers.get(layerCounter).getMap().getX()) +","+ (_layers.get(layerCounter).getThings().get(i).getY() - _layers.get(layerCounter).getMap().getY()) +")");
					
					tempThings.appendChild(tempThing);
					
				}//for things
				

				
				/*
				//TODO: put actual layer values into the xml for name/num/speed
				// create layer as the child of the level
				Element tempLayer = doc.createElement("Layer");
				tempLayer.setAttribute("name", _layers.get(layerCounter).getName());
				tempLayer.setAttribute("number", ""+layerCounter);
				tempLayer.setAttribute("scrollspeed", ""+_layers.get(layerCounter).getScrollSpeed());
				level.appendChild(tempLayer);
				
				//create map child of LAYER
				Element tempMap = doc.createElement("Map");
				tempMap.setAttribute("rows", ""+_layers.get(layerCounter).getMap().getRows());
				tempMap.setAttribute("cols", ""+_layers.get(layerCounter).getMap().getCols());
				tempLayer.appendChild(tempMap);
			
				// for all rows in the map
				for(int row = 0; row < _layers.get(layerCounter).getMap().getRows(); row++){
					
					//make a new row in the xml file
					Element tempRow = doc.createElement("Row");
					tempMap.appendChild(tempRow);
					
					//and for all cols in the map
					for(int col = 0; col < _layers.get(layerCounter).getMap().getCols(); col++){
						
						
						
						
						// create Tile element for this row and col
						Element tile = doc.createElement("Tile");
						
						//TODO: experimental null tile handling
						//if(_layers.get(layerCounter).getMap().getMap().get(row).get(col) == null){
						//	tile.setAttribute("name", "null");
						//	tile.setAttribute("tilemapX", "0");
						//	tile.setAttribute("tilemapY", "0");
						//}else{
						//tile.setAttribute("name", _layers.get(layerCounter).getMap().getMap().get(row).get(col).getName());
						//tile.setAttribute("tilemapX", ""+_layers.get(layerCounter).getMap().getMap().get(row).get(col).getTilemapX());
						//tile.setAttribute("tilemapY", ""+_layers.get(layerCounter).getMap().getMap().get(row).get(col).getTilemapY());
						//}
						//tempRow.appendChild(tile);
						
						tile.setAttribute("name", _layers.get(layerCounter).getMap().getMap()[row][col].getName());
						tile.setAttribute("tilemapX", ""+_layers.get(layerCounter).getMap().getMap()[row][col].getTilemapX());
						tile.setAttribute("tilemapY", ""+_layers.get(layerCounter).getMap().getMap()[row][col].getTilemapY());
						tempRow.appendChild(tile);
						
					}
					
				}//forMapTiles
				
				//create things child of level
				Element tempThings = doc.createElement("Things");
				//TODO: delete this attr?
				tempThings.setAttribute("count", "000");
				tempLayer.appendChild(tempThings);
				
				//for all our things
				for(int i = 0; i < _layers.get(layerCounter).getThings().size(); i++){
					
					//TODO: make this have other info like x,y, etc.
					Element tempThing = doc.createElement("Thing");
					tempThing.setAttribute("id", ""+_layers.get(layerCounter).getThings().get(i).getID());
					tempThing.setAttribute("map_x", "" + (_layers.get(layerCounter).getThings().get(i).getX() - _layers.get(layerCounter).getMap().getX()));
					tempThing.setAttribute("map_y", "" + (_layers.get(layerCounter).getThings().get(i).getY() - _layers.get(layerCounter).getMap().getY()));
					tempThing.setAttribute("name", _layers.get(layerCounter).getThings().get(i).getClass().toString());
					
					GameSkeleton.printDebugMessage("Writing Thing @(" + (_layers.get(layerCounter).getThings().get(i).getX() - _layers.get(layerCounter).getMap().getX()) +","+ (_layers.get(layerCounter).getThings().get(i).getY() - _layers.get(layerCounter).getMap().getY()) +")");
					
					tempThings.appendChild(tempThing);
					
				}//for things
			
				*/
			}//forLayers
			
			//add a text element to the child
			//TODO: is this ever needed for my maps?
			//Text text = doc.createTextNode("some text for the map");
			//level.appendChild(text);
			
			/////////////////
			//Output the XML
			//TODO: write this to a file
			//set up a transformer
			TransformerFactory transfac = TransformerFactory.newInstance();
			Transformer trans = transfac.newTransformer();
			trans.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "no");
			trans.setOutputProperty(OutputKeys.INDENT, "yes");
			
			//create string from xml tree
			StringWriter sw = new StringWriter();
			StreamResult result = new StreamResult(sw);
			DOMSource source = new DOMSource(doc);
			trans.transform(source, result);
			String xmlString = sw.toString();
			
			//DEBUG: print xml
			//GameSkeleton.printDebugMessage(xmlString);
			
			//write xml to file
			try{
				levelFileTest = new FileWriter(_filename);
				levelFileTest.write(xmlString);
				levelFileTest.close();
			}catch(Exception e){
				GameSkeleton.printDebugMessage("Error writing to file '" + _filename + "': " + e.getMessage());
			}
		
			//lastly, open and parse the level we just made
			//openLevelFile(_filename);
			
		}catch ( Exception e ){
			GameSkeleton.printDebugMessage( e.getMessage() );
			successful = false; 
		}

		return successful;
		
	}//writeLevelWithLayers
	
	/**
	 * This moves the level and all it's map and thing elements by the 
	 * specified amount. It's typically used for the beginning of the 
	 * level when we want the bottom left corner of the level aligned
	 * with the bottom left corner of our "camera" frame.
	 * @param xDelta - amount to move in X
	 * @param yDelta - amount to move in Y
	 */
	public void moveXY(int xDelta, int yDelta){
		
		//for all layers, move map and things
		for(int layerCounter = 0; layerCounter < _layers.size(); layerCounter++){
			
			// move our map and it's elements
			_layers.get(layerCounter).getMap().moveXY(xDelta, yDelta);
			
			//GameSkeleton.printDebugMessage("Things for layer "+layerCounter+" is of null? " + (_layers.get(layerCounter).getThings()==null));
			//GameSkeleton.printDebugMessage("Things for layer "+layerCounter+" is of size " + _layers.get(layerCounter).getThings().size());
			
			//then move all our things
			for(int thingIndex = 0; thingIndex < _layers.get(layerCounter).getThings().length; thingIndex++){
				
				//GameSkeleton.printDebugMessage("WTF^^?");
				_layers.get(layerCounter).getThings()[thingIndex].setX(_layers.get(layerCounter).getThings()[thingIndex].getX() + xDelta);
				_layers.get(layerCounter).getThings()[thingIndex].setY(_layers.get(layerCounter).getThings()[thingIndex].getY() + yDelta);
				
			}//for
			
		}

	}//moveXY
	
	/**
	 * This function kills all level assets and creates a completed new 
	 * level of the size specified by the OtherStuff DEFAULT... variables
	 */
	public void clearAndMakeNew(){
		
		_layers = new Vector<LevelLayer>();
		
		_layers.add(new LevelLayer("Effects Top("+5+")", new Map(OtherStuff.DEFAULT_MAP_ROWS,OtherStuff.DEFAULT_MAP_COLS), new Thing[0], OtherStuff.LAYER_EFFECTS_TOP_SCROLLSPEED));
		_layers.add(new LevelLayer("Effects Top("+4+")", new Map(OtherStuff.DEFAULT_MAP_ROWS,OtherStuff.DEFAULT_MAP_COLS), new Thing[0], OtherStuff.LAYER_ACTION_SCROLLSPEED));
		_layers.add(new LevelLayer("Effects Top("+3+")", new Map(OtherStuff.DEFAULT_MAP_ROWS,OtherStuff.DEFAULT_MAP_COLS), new Thing[0], OtherStuff.LAYER_EFFECTS_MIDDLE_SCROLLSPEED));
		_layers.add(new LevelLayer("Effects Top("+2+")", new Map(OtherStuff.DEFAULT_MAP_ROWS,OtherStuff.DEFAULT_MAP_COLS), new Thing[0], OtherStuff.LAYER_SHADOWS_SCROLLSPEED));
		_layers.add(new LevelLayer("Effects Top("+1+")", new Map(OtherStuff.DEFAULT_MAP_ROWS,OtherStuff.DEFAULT_MAP_COLS), new Thing[0], OtherStuff.LAYER_GROUND_OBJECTS_SCROLLSPEED));
		_layers.add(new LevelLayer("Effects Top("+0+")", new Map(OtherStuff.DEFAULT_MAP_ROWS,OtherStuff.DEFAULT_MAP_COLS), new Thing[0], OtherStuff.LAYER_FLOOR_SCROLLSPEED));
		
	}//clearAndMakeNew
	
	/**
	 * This function looks for a string in the array of tilesets that we have cached and returns the 
	 * index if found. If not found, it returns -1
	 * @param the name of the tileset to search for
	 */
	public int getIndexOfTileSetByName(String name){

		int result = -1;
		
		for(int i = 0; i < _tilesets.length; i++){
			if(_tilesets[i].getName().compareTo(name) == 0){
				return i;
			}
		}
		
		return result;
		
	}//getIndexOfTileSetByName
	
	/**
	 * This function
	 * @param x
	 * @param y
	 */
	public void eraseThingAtCoords(int x, int y, int layerIndex, int tolerance){
		
		Thing[] tempThingArray;
		boolean intersect;
		
		//for(int layerIndex = 0; layerIndex < _layers.size(); layerIndex++){
			
		tempThingArray = _layers.get(layerIndex).getThings();
		
		for(int thingIndex = 0; thingIndex < tempThingArray.length; thingIndex++){
			
			//if the thing:
			// has x coord 32 pixels above tolerance
			//has x coord 32 pixels above tolerance
			//has x coord 32 pixels above tolerance
			//has x coord 32 pixels above tolerance
			//if((tempThingArray[thingIndex].getX())
					
					
            // Let's assume our thing is within tolerance, and set to false if they have not
            intersect = true;

            // check the left and right sides of the Thing's bounding boxes
            if (!(Math.abs((tempThingArray[thingIndex].getX() + tempThingArray[thingIndex].getW()/2) - (x + tolerance/2)) <= tempThingArray[thingIndex].getW() / 2 + tolerance / 2)){
            	intersect = false;
            }
             

            // check the top and bottom of the Thing's bounding boxes
            if (!(Math.abs((tempThingArray[thingIndex].getY() + tempThingArray[thingIndex].getH()/2) - (y + tolerance/2)) <= tempThingArray[thingIndex].getH() / 2 + tolerance / 2)){
                intersect = false;
            }

            // If the objects collided, damage each one.
            if(intersect)
            {
            	//TODO: clicking this in extremely rapid succession sometimes causes an array out of bounds exception
				try{
					getThingsAtIndex(layerIndex)[thingIndex] = null;
					getLayerAtIndex(layerIndex).removeThingAtIndex(thingIndex);
				}catch(Exception e){
					GameSkeleton.printDebugMessage("Level: Exception while erasing a thing: " + e.getMessage());
				}
            }
			
		}//for
			
		//}//for
		
		
	}//eraseThingAtCoords

	
}//Level
