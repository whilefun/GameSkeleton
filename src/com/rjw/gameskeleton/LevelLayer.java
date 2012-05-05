package com.rjw.gameskeleton;

import java.util.Vector;

/**
 * This class represents our Level's layers. Each layer contains a Map (of Tiles) and a vector of Things
 * @author rwalsh
 *
 */
public class LevelLayer {

	private String _name;
	private Map _map;
	//private Vector<Thing> _things;
	private Thing[] _things;
	private int _scrollSpeed;
	private boolean _visible;
	
	//public LevelLayer(String name, Map map, Vector<Thing> things, int scrollSpeed){
	public LevelLayer(String name, Map map, Thing[] things, int scrollSpeed){
		
		_name = name;
		_map = map;
		_things = things;
		_scrollSpeed = scrollSpeed;
		_visible = true;
		
	}//constructor
	
	//getters
	
	//TODO: change getMap and getThings to be getLayerMap and getLayerThings
	public int getScrollSpeed(){ return _scrollSpeed; }
	public Map getMap(){ return _map; }
	//public Vector<Thing> getThings(){ return _things; }
	public Thing[] getThings(){ return _things; }
	public String getName(){ return _name; }
	public boolean isVisible(){ return _visible; }
	
	//setters
	public void setMap(Map m){ _map = m; }
	//public void setThings(Vector<Thing> t){ _things = t; }
	public void setThings(Thing[] t){ _things = t; }
	public void setVisible(boolean vis){ _visible = vis; }
	
	/**
	 * This function does some array management for us, and
	 * adds the new thing to the _things array in our layer
	 * @param newThing - the thing to add
	 */
	public void addNewThing(Thing newThing){
		
		Thing[] thingsCopy = _things;
		_things = new Thing[_things.length + 1];
		
		for(int newThingIndex = 0; newThingIndex < thingsCopy.length; newThingIndex++){
			_things[newThingIndex] = thingsCopy[newThingIndex];
		}
		
		_things[_things.length-1] = newThing;
		
	}//addNewThing
	
	/**
	 * This function remove the thing at the index
	 * specified and shrink our array of things
	 * @param index
	 */
	public void removeThingAtIndex(int killIndex){
		
		Thing[] thingsCopy = _things;
		_things = new Thing[_things.length - 1];
		int tempIndex = 0;
		
		for(int newThingIndex = 0; newThingIndex < thingsCopy.length; newThingIndex++){
			
			// only add it to the new array if it's NOT at the index specified
			if(newThingIndex != killIndex){
				_things[tempIndex] = thingsCopy[newThingIndex];
				tempIndex++;
			}
			
		}//for
		
	}//addNewThing
	
}//LevelLayer
