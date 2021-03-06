package trl.graphics;

import java.awt.image.BufferedImage;
import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;

import trl.main.Game;

public class ImageManager{

	// Barbarian image
	public BufferedImage barbarian;

	// Stone Tile image
	public BufferedImage stoneTile;

	// Stone wall image
	public BufferedImage stoneWall;

	// Image of boundary Wall
	public BufferedImage boundaryWall;

	// Demon image
	public BufferedImage demon;

	// Corpse image
	public BufferedImage corpse;

	// Bang image
	public BufferedImage bang;

	// Image of the Frog Knight
	public BufferedImage frogKnight;

	// Archer image
	public BufferedImage archer;

	// Miss image
	public BufferedImage miss;

	// Potion image
	public BufferedImage potion;

	// Hammer image
	public BufferedImage hammer;

	// Thief imade
	public BufferedImage thief;

	// Eye image
	public BufferedImage eye;

	// Wizard image
	public BufferedImage wizard;

	// Swords image
	public BufferedImage swords;

	// Fire image
	public BufferedImage fire;

	// Rat image
	public BufferedImage rat;

	// Bat image
	public BufferedImage bat;

	// Spider image
	public BufferedImage spider;

	// Snake image
	public BufferedImage snake;

	// Empty place image
	public BufferedImage voidNode;

	// Image of the open door
	public BufferedImage openDoor;

	// Image of coled door
	public BufferedImage closedDoor;

	// Wolf image
	public BufferedImage wolf;

	// Ant image
	public BufferedImage ant;

	// Image of stair down
	public BufferedImage stairDown;

	// Wyvern image
	public BufferedImage wyvern;

	// Wasp image
	public BufferedImage wasp;

	// Imp image
	public BufferedImage imp;

	// Gelatinous Cube image
	public BufferedImage gelatinousCube;

	// Ogre image
	public BufferedImage ogre;

	// Panther image
	public BufferedImage panther;

	// Worm image
	public BufferedImage worm;

	// Scorpion image
	public BufferedImage scorpion;

	// Goblin image
	public BufferedImage goblin;

	// Gremilin image
	public BufferedImage gremlin;

	// Gargoyle image
	public BufferedImage gargoyle;

	// Zombie image
	public BufferedImage zombie;

	// Goal image
	public BufferedImage goal;

	// Shadow image of the stair down
	public BufferedImage stairDownShadow;

	// Shadow image of the goal
	public BufferedImage goalShadow;

	// Key image
	public BufferedImage key;

	// Lock image
	public BufferedImage lock;

	// Image of open lock
	public BufferedImage lockOpen;

	// Shadow image of the key
	public BufferedImage keyShadow;

	// Shadow image of the lock
	public BufferedImage lockShadow;

	// Shadow image of the open lock
	public BufferedImage lockOpenShadow;

	// Ranger image
	public BufferedImage ranger;

	// Arrows image
	public BufferedImage arrows;

	// Shadow image of the closed door
	public BufferedImage closedDoorShadow;

	// Shadow image of the open door
	public BufferedImage openDoorShadow;

	// Another stile of stone tile image
	public BufferedImage stoneTile1Shadow;

	// Shadow image of the stone wall
	public BufferedImage stoneWallShadow;

	// Shadow image of the potion
	public BufferedImage potionShadow;

	// Shadow image of the hammer
	public BufferedImage hammerShadow;

	public ImageManager ( SpriteSheet ss ){

		// Row 1
		this.barbarian = ss.crop( 0 , 0 , Game.TILE_SIZE , Game.TILE_SIZE );
		this.thief = ss.crop( 1 , 0 , Game.TILE_SIZE , Game.TILE_SIZE );
		this.wizard = ss.crop( 2 , 0 , Game.TILE_SIZE , Game.TILE_SIZE );
		this.ranger = ss.crop( 3 , 0 , Game.TILE_SIZE , Game.TILE_SIZE );
		// Row 2
		this.rat = ss.crop( 0 , 1 , Game.TILE_SIZE , Game.TILE_SIZE );
		this.spider = ss.crop( 1 , 1 , Game.TILE_SIZE , Game.TILE_SIZE );
		this.bat = ss.crop( 2 , 1 , Game.TILE_SIZE , Game.TILE_SIZE );
		this.snake = ss.crop( 3 , 1 , Game.TILE_SIZE , Game.TILE_SIZE );
		this.wolf = ss.crop( 4 , 1 , Game.TILE_SIZE , Game.TILE_SIZE );
		this.wyvern = ss.crop( 5 , 1 , Game.TILE_SIZE , Game.TILE_SIZE );
		// Row 3
		this.ant = ss.crop( 0 , 2 , Game.TILE_SIZE , Game.TILE_SIZE );
		this.wasp = ss.crop( 1 , 2 , Game.TILE_SIZE , Game.TILE_SIZE );
		this.imp = ss.crop( 2 , 2 , Game.TILE_SIZE , Game.TILE_SIZE );
		this.frogKnight = ss.crop( 3 , 2 , Game.TILE_SIZE , Game.TILE_SIZE );
		this.archer = ss.crop( 4 , 2 , Game.TILE_SIZE , Game.TILE_SIZE );
		this.gelatinousCube = ss.crop( 5 , 2 , Game.TILE_SIZE , Game.TILE_SIZE );
		// Row 4
		this.worm = ss.crop( 0 , 3 , Game.TILE_SIZE , Game.TILE_SIZE );
		this.scorpion = ss.crop( 1 , 3 , Game.TILE_SIZE , Game.TILE_SIZE );
		this.goblin = ss.crop( 2 , 3 , Game.TILE_SIZE , Game.TILE_SIZE );
		this.gremlin = ss.crop( 3 , 3 , Game.TILE_SIZE , Game.TILE_SIZE );
		this.gargoyle = ss.crop( 4 , 3 , Game.TILE_SIZE , Game.TILE_SIZE );
		this.zombie = ss.crop( 5 , 3 , Game.TILE_SIZE , Game.TILE_SIZE );
		// Row 5
		this.ogre = ss.crop( 0 , 4 , Game.TILE_SIZE , Game.TILE_SIZE );
		this.panther = ss.crop( 1 , 4 , Game.TILE_SIZE , Game.TILE_SIZE );
		// Row 6

		// Row 7
		this.stoneTile = ss.crop( 0 , 6 , Game.TILE_SIZE , Game.TILE_SIZE );
		this.stoneWall = ss.crop( 1 , 6 , Game.TILE_SIZE , Game.TILE_SIZE );
		this.closedDoor = ss.crop( 2 , 6 , Game.TILE_SIZE , Game.TILE_SIZE );
		this.openDoor = ss.crop( 3 , 6 , Game.TILE_SIZE , Game.TILE_SIZE );
		this.stairDown = ss.crop( 4 , 6 , Game.TILE_SIZE , Game.TILE_SIZE );
		this.goal = ss.crop( 5 , 6 , Game.TILE_SIZE , Game.TILE_SIZE );
		// Row 8
		this.potion = ss.crop( 0 , 7 , Game.TILE_SIZE , Game.TILE_SIZE );
		this.hammer = ss.crop( 1 , 7 , Game.TILE_SIZE , Game.TILE_SIZE );
		this.key = ss.crop( 2 , 7 , Game.TILE_SIZE , Game.TILE_SIZE );
		this.lock = ss.crop( 3 , 7 , Game.TILE_SIZE , Game.TILE_SIZE );
		this.lockOpen = ss.crop( 4 , 7 , Game.TILE_SIZE , Game.TILE_SIZE );
		// Row 9
		this.bang = ss.crop( 0 , 8 , Game.TILE_SIZE , Game.TILE_SIZE );
		this.corpse = ss.crop( 1 , 8 , Game.TILE_SIZE , Game.TILE_SIZE );
		this.swords = ss.crop( 2 , 8 , Game.TILE_SIZE , Game.TILE_SIZE );
		this.fire = ss.crop( 3 , 8 , Game.TILE_SIZE , Game.TILE_SIZE );
		this.arrows = ss.crop( 4 , 8 , Game.TILE_SIZE , Game.TILE_SIZE );

		// Shadow transforms
		this.stoneWallShadow = transformAlpha( this.stoneWall );
		this.stoneTile1Shadow = transformAlpha( this.stoneTile );
		this.openDoorShadow = transformAlpha( this.openDoor );
		this.closedDoorShadow = transformAlpha( this.closedDoor );
		this.potionShadow = transformAlpha( this.potion );
		this.hammerShadow = transformAlpha( this.hammer );
		this.stairDownShadow = transformAlpha( this.stairDown );
		this.goalShadow = transformAlpha( this.goal );
		this.keyShadow = transformAlpha( this.key );
		this.lockShadow = transformAlpha( this.lock );
	}

	public BufferedImage transformAlpha ( BufferedImage image ){

		BufferedImage destImage = null;
		float[] transparency = { .7f };
		Kernel kernel = new Kernel( 1 , 1 , transparency );
		ConvolveOp co = new ConvolveOp( kernel );
		destImage = co.filter( image , null );
		return destImage;
	}
}
