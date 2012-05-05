package com.rjw.gameskeleton;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

public class OtherStuff {
	
	public static final String RELEASE_NUMBER = "0.0.1 Alpha";
	
	// Size of our tiles and grid (MUST BE THE SAME WITHIN A MAP)
	//public static final int TILE_WIDTH = 32;
	//public static final int TILE_HEIGHT = 32;
	public static final int TILE_SIZE = 32;
	public static final int EDITOR_TILE_SIZE = 32;
	public static final int AVATAR_SIZE = 32;
	
	public static final int EDITOR_TILE_ICON_SIZE = 32;
	
	//key press timeouts
	public static final int EDITOR_KEY_TIMEOUT = 200;
	public static final int GAME_KEY_TIMEOUT = 1200;
	
	//Thing eraser range (x,y) within this range gets erased
	public static final int THING_ERASER_COORD_RANGE = 32;
	
	public static final Color BACKGROUND_COLOR = Color.gray;

	// LEVEL LAYER STUFF //
	// Map that is 500 rows by 20 columns is a decent fit
	public static final int DEFAULT_MAP_ROWS = 500;
	public static final int DEFAULT_MAP_COLS = 20;
	public static final int DEFAULT_LAYER_COUNT = 6;
	// layer indices
	public static final int LAYER_EFFECTS_TOP = 5;
	public static final int LAYER_ACTION = 4;
	public static final int LAYER_EFFECTS_MIDDLE = 3;
	public static final int LAYER_SHADOWS = 2;
	public static final int LAYER_GROUND_OBJECTS = 1;
	public static final int LAYER_FLOOR = 0;
	// layer scrollspeeds
	public static final int LAYER_EFFECTS_TOP_SCROLLSPEED = 10;
	public static final int LAYER_ACTION_SCROLLSPEED = 9;
	public static final int LAYER_EFFECTS_MIDDLE_SCROLLSPEED = 8;
	public static final int LAYER_SHADOWS_SCROLLSPEED = 9;
	public static final int LAYER_GROUND_OBJECTS_SCROLLSPEED = 5;
	public static final int LAYER_FLOOR_SCROLLSPEED = 4;
		
	// This timer of 30 ms means we're drawing a max of 1 frame per 0.03s, which translates into 30fps
	public static final int FRAME_RATE_TIMER_IN_MS = 30;
	
	//SPRITES//
	public static final String SPRITE_PATH_PREFIX = "res/";
	//public static final String SFX_PATH_PREFIX = "sounds/";
	
	//LEVELS//
	public static final String GAME_LEVEL_SEQUENCE_FILE = "level_sequence.xml";
	
	// SPRITES //
	public static final String SPRITE_TEST_001 = "sprite_test_002.png";
	public static final String SPRITE_MAP_TEST_001 = "sprite_map_test_frame1.png";
	public static final String SPRITE_MAP_TEST_002 = "sprite_map_test_frame2.png";
	public static final String SPRITE_MAP_TEST_SMALL = "sprite_map_test_frame_small.png";

	public static final String SPRITE_AVATAR_IDLE = "sprite_avatar_idle.png";

	//32 x 32 test for avatar
	public static final String SPRITE_PLANE_TILE_TEST = "sprite_avatar2_32px.png";
	public static final String SPRITE_EFFECTS_001 = "sprite_effects_32px.png";
	
	public static final String SPRITE_LIVES_ICON = "sprite_lives_icon.png";
	public static final String SPRITE_HEALTH_ICON = "sprite_health_icon.png";
	public static final String SPRITE_AVATAR_HEALTH_BAR = "sprite_avatar_health_bar2.png";
	public static final String SPRITE_THING_TILE = "thingTile.png";
	public static final String SPRITE_TALL_STUCTURE_001 = "sprite_tall_structure_001.png";
	public static final String SPRITE_WATER_FRAME_001 = "sprite_water_frame_001.png";
	public static final String SPRITE_WATER_FRAME_002 = "sprite_water_frame_002.png";
	public static final String SPRITE_WATER_FRAME_003 = "sprite_water_frame_003.png";
	public static final String SPRITE_FUNNY_SIZE_TEST = "sprite_funny_size_test.png";
	public static final String SPRITE_RENDER_TEST = "sprite_render_test_001.png";
	public static final String SPRITE_PROJECTILE_TEST = "sprite_projectile_test.png"; 
	public static final String SPRITE_CLOUD_TEST = "sprite_cloud_test.png";
	public static final String SPRITE_BLANK_TILE = "sprite_map_editor_grid.png";
	public static final String SPRITE_RED_STUFF = "sprite_red_stuff.png";
	public static final String SPRITE_GREEN_STUFF = "sprite_green_stuff.png";
	public static final String SPRITE_BLUE_STUFF = "sprite_blue_stuff.png";
	public static final String SPRITE_GRID_TEST_4x4 = "spriteGrid_64x64_001.png";
	
	//32x32 test
	public static final String SPRITE_32_32_01 = "tile_test_32_32_01.png";
	public static final String SPRITE_32_32_02 = "tile_test_32_32_02.png";
	public static final String SPRITE_32_32_BLANK = "tile_test_32_32_blank.png";
	public static final String SPRITE_32_32_AVATAR = "sprite_avatar_32px.png";
	public static final String SPRITE_PLAYER_START = "sprite_player_start.png";
	public static final String SPRITE_BAD_GUY_TEST = "sprite_moving_thing_test.png";
	public static final String SPRITE_SPEEDER = "sprite_speeder.png";
	public static final String SPRITE_ORB_PROJECTILE_8_X_8 = "sprite_projectile_orb_01.png";
	public static final String SPRITE_BULLET_50MM = "sprite_bullet50mm.png";
	public static final String SPRITE_BULLET_50MM_ICON = "sprite_bullet50mm_icon2.png";
	public static final String SPRITE_TURRET_01 = "sprite_turret_01.png";
	public static final String SPRITE_HEALTH_01 = "sprite_powerup_health.png";
	public static final String SPRITE_MISSILES_01 = "sprite_powerup_missiles.png";
	public static final String SPRITE_MISSILE_ICON = "sprite_missile_icon2.png";
	public static final String SPRITE_UFO = "sprite_ufo.png";
	public static final String SPRITE_ORB_PROJECTILE_6_X_6 = "sprite_projectile_orb_02.png";
	public static final String SPRITE_ORB_PROJECTILE_2_X_2 = "sprite_projectile_orb_2x2.png";
	public static final String SPRITE_MOSQUITO = "sprite_mosquito.png";
	public static final String SPRITE_PLASMA_BULLET = "sprite_plasma_bullet.png";
	public static final String SPRITE_BOSS_TEST = "sprite_boss_test_1.png";
	
	
	public static final int STATUS_BAR_X_OFFSET = 10;	
	
	// AVATAR STUFF //
	public static final int AVATAR_X_SPEED = 5;
	public static final int AVATAR_MIN_X_SPEED = 5;
	public static final int AVATAR_MAX_X_SPEED = 5;
	public static final int AVATAR_X_SPEED_CHANGE_INCREMENT = 1;
	
	public static final int AVATAR_Y_SPEED = 5;
	public static final int AVATAR_MIN_Y_SPEED = 3;
	public static final int AVATAR_MAX_Y_SPEED = 5;
	public static final int AVATAR_Y_SPEED_CHANGE_INCREMENT = 1;
	
	public static final int AVATAR_DEFAULT_HEALTH = 100;
	public static final int AVATAR_START_X = 300;
	public static final int AVATAR_START_Y = 300;
	
	// CAMERA STUFF //
	public static final int CAMERA_X_SPEED = AVATAR_X_SPEED;
	public static final int CAMERA_Y_SPEED = 1;//AVATAR_Y_SPEED + 2;
	public static final int CAMERA_WIDTH = 20;
	public static final int CAMERA_HEIGHT = 12;
	public static final int EDITOR_CAMERA_X_SPEED = AVATAR_X_SPEED * 12;
	public static final int EDITOR_CAMERA_Y_SPEED = AVATAR_Y_SPEED * 12;
	public static final int EDITOR_CAMERA_WIDTH = 12;
	public static final int EDITOR_CAMERA_HEIGHT = 10;
	public static final int EDITOR_SCREEN_W = 800;
	public static final int EDITOR_SCREEN_H = 600;
	public static final int JAVA_JFRAME_WINDOW_OFFSET = 35;
	
	// our game screen size
	public static final int SCREEN_WIDTH = CAMERA_WIDTH * TILE_SIZE;
	public static final int SCREEN_HEIGHT = CAMERA_HEIGHT * TILE_SIZE;
	
	// HUD //
	public static final String SPRITE_HUD_BACKGROUND = "sprite_hud.png";
	public static final int HUD_W = 195;
	public static final int HUD_H = 44;
	public static final int HUD_X = 0;
	public static final int HUD_Y = SCREEN_HEIGHT-HUD_H;
	public static final int AVATAR_HEALTH_BAR_X = HUD_X+6;
	public static final int AVATAR_HEALTH_BAR_Y = HUD_Y+3;
	public static final int WEAPON_ICON_X = HUD_X+79;
	public static final int WEAPON_ICON_Y = HUD_Y+3;
	
	// PLAY AREA BOUNDS //
	public static final int AVATAR_MIN_X_COORD = 0;
	public static final int AVATAR_MAX_X_COORD = SCREEN_WIDTH - TILE_SIZE;
	public static final int AVATAR_MIN_Y_COORD = 0;
	public static final int AVATAR_MAX_Y_COORD = SCREEN_HEIGHT - HUD_H - TILE_SIZE;
	
	// EDITOR IMAGES //
	public static final String SPRITE_TOOL_PALETTE_LABEL = "tool_palette_label.png";
	public static final String SPRITE_TOOL_PALETTE_BRUSH1x1 = "tool_palette_brush1x1.png";
	public static final String SPRITE_TOOL_PALETTE_BRUSH2x2 = "tool_palette_brush2x2.png";
	public static final String SPRITE_TOOL_PALETTE_BRUSH4x4 = "tool_palette_brush4x4.png";
	public static final String SPRITE_TOOL_PALETTE_ERASER = "tool_palette_eraser.png";
	public static final String SPRITE_TOOL_PALETTE_REPLACER = "tool_palette_replacer.png";
	public static final String SPRITE_TOOL_PALETTE_FILL = "tool_palette_fill.png";
	public static final String SPRITE_TOOL_PALETTE_PICKER = "tool_palette_picker.png";
	public static final String SPRITE_TOOL_PALETTE_THING_TOOL = "tool_palette_thing_tool.png";
	public static final String SPRITE_TOOL_PALETTE_SUSPENDED_TOOL = "tool_palette_suspended.png";
	public static final String SPRITE_TOOL_CURSOR_BLANK = "tool_cursor_blank.png";
	public static final String SPRITE_TOOL_CURSOR_ERASER = "tool_cursor_eraser.png";
	public static final String SPRITE_TOOL_CURSOR_REPLACER = "tool_cursor_replacer.png";
	public static final String SPRITE_TOOL_CURSOR_THING_TOOL = "tool_cursor_thing_tool.png";
	public static final String SPRITE_TOOL_CURSOR_FILL = "tool_cursor_fill.png";
	public static final String SPRITE_TOOL_CURSOR_PICKER = "tool_cursor_picker.png";
	public static final String SPRITE_TOOL_CURSOR_SUSPENDED = "tool_cursor_suspended_tool.png";
	public static final String SPRITE_TOOL_OVERLAY = "tool_palette_overlay.png";
	public static final String SPRITE_TOOL_TILE_PALETTE = "tool_tile_palette.png";
	public static final String SPRITE_TOOL_THING_PALETTE = "tool_thing_palette.png";
	public static final String SPRITE_TOOL_FILE_MENU = "tool_file_menu.png";
	
	// LAYER MANAGER (Sprite is 116x20//
	// first box TL corner is @ 31, 12
	public static final String SPRITE_TOOL_LAYER_MANAGER = "tool_layermanager.png";
	public static final int EDITOR_LAYER_MANAGER_X = (EDITOR_SCREEN_W / 2) - (116/2);
	public static final int EDITOR_LAYER_MANAGER_Y = EDITOR_SCREEN_H - 24;
	public static final int EDITOR_LAYER_MANAGER_LAYERS_X_OFFSET = EDITOR_LAYER_MANAGER_X + 31;
	public static final int EDITOR_LAYER_MANAGER_LAYERS_VIS_BOX_Y_OFFSET = EDITOR_LAYER_MANAGER_Y + 12;
	public static final int EDITOR_LAYER_MANAGER_LAYERS_SELECTION_INDICATOR_Y_OFFSET = EDITOR_LAYER_MANAGER_Y + 2;
	public static final int EDITOR_LAYER_MANAGER_BOX_W = 9;
	public static final int EDITOR_LAYER_MANAGER_SELECTION_INDICATOR_BOX_H = 8;
	public static final int EDITOR_LAYER_MANAGER_VIS_BOX_H = 6;
	public static final int EDITOR_LAYER_MANAGER_BOX_GAP = 5;
	
	// TilesetPalette
	//public static final String SPRITE_TOOL_TILESET_PALETTE = "tool_tilesetpalette.png";
	public static final String SPRITE_TOOL_TILESET_PALETTE = "tool_tilesetpalette_8x12.png";
	public static final String SPRITE_TILEMAP_BLANK = "sprite_tilemap_blank.png";
	public static final String SPRITE_TILEMAP_TEST_01 = "sprite_tilemap_test_01.png";
	public static final String SPRITE_TILEMAP_TEST_02 = "sprite_tilemap_test_02.png";
	public static final String SPRITE_TILEMAP_TEST_03 = "sprite_tilemap_test_03.png";
	public static final String SPRITE_TILEMAP_TEST_04 = "MaterialTileTemplate_8x12.png";
	public static final String SPRITE_TILEMAP_TEST_05 = "sprite_tileset_water_dirt_8.png";
	public static final int TILEMAP_DEFAULT_WIDTH = 8;
	public static final int TILEMAP_DEFAULT_HEIGHT = 12;
	
	//ThingsetPalette
	public static final String SPRITE_TOOL_THINGSET_PALETTE = "tool_thingsetpalette.png";
	public static final String THINGSETFILENAME_001 = "things_01.xml";
	public static final String THINGSETFILENAME_002 = "things_02.xml";
	public static final int THINGMAP_DEFAULT_WIDTH = 7;
	public static final int THINGMAP_DEFAULT_HEIGHT = 10;
	//public static final int[][] THING_DAMAGE_MATRIX = null;

	// MENU STUFF
	public static final String SPRITE_MAIN_GAME_MENU = "sprite_main_menu_test.png";
	public static final String SPRITE_MAIN_GAME_MENU_BACKGROUND = "sprite_main_menu_background.png";
	
}//OtherStuff
