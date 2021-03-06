package trl.map;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import trl.entity.Entity;
import trl.entity.enemy.Enemy;
import trl.entity.enemy.Wolf;
import trl.gamestate.GameplayState;
import trl.main.Game;

public class Map{

	// Maximum width of a room
	public static int MAX_ROOM_WIDTH = 10;

	// Maximum height of a room
	public static int MAX_ROOM_HEIGHT = 10;

	// Minimum width of a room
	public static int MIN_ROOM_WIDTH = 5;

	// Minimum height of a room
	public static int MIN_ROOM_HEIGHT = 5;

	// Nodes to be displayed
	private Node[][] displayedNodes;

	// Value representing the last node
	private boolean endNodeFound;

	// Columns in the game (horizontal)
	private int hSize;

	// Rows in the game
	private int vSize;

	// Image of the complete map
	private BufferedImage imageMap[][];

	// Map Grid
	private Node[][] mapGrid;

	// Minimum of nodes in the x axis
	public static int displayedNodesMinX;

	// Minimum of nodes in the y axis
	public static int displayedNodesMinY;

	// Maximum of nodes in the x axis
	public static int displayedNodesMaxX;

	// Maximum of nodes in the y axis
	public static int displayedNodesMaxY;

	// Rooms in the map
	private Room[][] rooms;

	// Nodes that the pleyer can see
	private List<Node> visibleToPlayer;
	
	private final static Logger LOGGER = Logger.getLogger( Wolf.class.getName() );


	public Map (){

		this.hSize = Game.COLUMNS;
		this.vSize = Game.ROWS;
		init();
	}

	public void addAdjacents ( List<Node> openList , List<Node> closedList , Node endNode , Node node ){
		
		LOGGER.setLevel( Level.CONFIG );
		LOGGER.config("Adding adjacents nodes");

		Set<Node> tempList = new HashSet<Node>();
		// Determine adjacent nodes to argument node
		for ( int x = -1 ; x <= 1 ; x++ ){
			for ( int y = 1 ; y >= -1 ; y-- ){
				int candXPos = node.getAxisX() + x;
				int candYPos = node.getAxisY() + y;
				if ( ( candXPos >= 0 && candXPos < Game.COLUMNS ) && ( candYPos >= 0 && candYPos < Game.ROWS ) ){
					/*
					 * New node at coordinates if it is within bounds of the map
					 * grid
					 */
					Node candidate = new Node( candXPos , candYPos , this );

					/*
					 * Make sure candidate node is not obstructed or in the
					 * closed list
					 */
					if ( isLegalCell( getNode( candidate.getAxisX() , candidate.getAxisY() ) )
							|| getNode( candidate.getAxisX() , candidate.getAxisY() ).getFeature() instanceof trl.map.feature.DoorClosed
							|| getNode( candidate.getAxisX() , candidate.getAxisY() ).getFeature() instanceof trl.map.feature.DoorOpen ){

						candidate.setParent( node );
						candidate.setGScore();
						candidate.setHScore( endNode );
						candidate.setFScore();

						// If not on openList, add it.
						if ( openList.contains( candidate ) ){
							{
								if ( candidate.getGScore() < openList.get( openList.indexOf( candidate ) ).getGScore() ){
									openList.remove( candidate );
									openList.add( candidate );
								}else{
									// Nothing
								}
							}
						}else{
							// Nothing
						}

						if ( closedList.contains( candidate ) ){
							{
								if ( candidate.getGScore() < closedList.get( closedList.indexOf( candidate ) ).getGScore() ){
									openList.remove( candidate );
									openList.add( candidate );
								}else{
									// Nothing
								}
							}
						}else{
							// Nothing
						}

						if ( !openList.contains( candidate ) && !closedList.contains( candidate ) ){
							tempList.add( candidate );
						}else{
							// Nothing
						}

					}else{
						// Nothing
					}
				}else{
					// Nothing
				}
			}
		}
		openList.addAll( tempList );
		tempList.clear();
	}

	public void addRectAdjacents ( List<Node> openList , List<Node> closedList , Node endNode , Node node ){
		
		LOGGER.setLevel( Level.CONFIG );
		LOGGER.config("Adding initial adjacents nodes");

		Set<Node> tempList = new HashSet<Node>();

		// Determine adjacent nodes to argument node
		for ( int x = -1 ; x <= 1 ; x++ ){
			for ( int y = 1 ; y >= -1 ; y-- ){
				if ( ( x == 0 && y == 1 ) || ( x == 0 && y == -1 ) || ( x == -1 && y == 0 ) || ( x == 1 && y == 0 ) ){

					int candXPos = node.getAxisX() + x;
					int candYPos = node.getAxisY() + y;
					if ( ( candXPos >= 0 && candXPos < Game.COLUMNS ) && ( candYPos >= 0 && candYPos < Game.ROWS ) ){
						/*
						 * New node at coordinates if it is within bounds of the
						 * map grid
						 */

						Node candidate = new Node( candXPos , candYPos , this );

						/*
						 * Make sure candidate node is not obstructed or in the
						 * closed list if (isLegalCell(getNode(candidate.getX(),
						 * candidate.getY()))) {
						 */

						getNode( candidate.getAxisX() , candidate.getAxisY() );
						candidate.setParent( node );
						candidate.setGScore();
						candidate.setHScore( endNode );
						candidate.setFScore();

						// If not on openList, add it.
						if ( openList.contains( candidate ) ){
							{
								if ( candidate.getGScore() < openList.get( openList.indexOf( candidate ) ).getGScore() ){
									openList.remove( candidate );
									openList.add( candidate );
								}else{
									// Nothing
								}
							}
						}else{
							// Nothing
						}

						if ( closedList.contains( candidate ) ){
							{
								if ( candidate.getGScore() < closedList.get( closedList.indexOf( candidate ) ).getGScore() ){
									openList.remove( candidate );
									openList.add( candidate );
								}else{
									// Nothing
								}
							}
						}else{
							// Nothing
						}

						if ( !openList.contains( candidate ) && !closedList.contains( candidate ) ){
							tempList.add( candidate );
						}else{
							// Nothing
						}

						// }
					}else{
						// Nothing
					}
				}else{
					// Nothing
				}
			}
		}
		openList.addAll( tempList );
		tempList.clear();
	}

	public void addWalkableAdjacents ( List<Node> checked , List<Node> checking ){
		
		LOGGER.setLevel( Level.CONFIG );
		LOGGER.config("Adding walkable initial adjacents nodes");

		Set<Node> tempList = new HashSet<Node>();

		// Determine adjacent nodes to argument node
		Iterator<Node> itChecking = checking.iterator();
		while ( itChecking.hasNext() ){
			Node toCheck = itChecking.next();
			for ( int x = -1 ; x <= 1 ; x++ ){
				for ( int y = 1 ; y >= -1 ; y-- ){
					int candXPos = toCheck.getAxisX() + x;
					int candYPos = toCheck.getAxisY() + y;
					if ( ( candXPos >= 0 && candXPos < this.hSize ) && ( candYPos >= 0 && candYPos < this.vSize ) ){
						/*
						 * Get node at coordinates if it is within bounds of the
						 * map grid
						 */

						Node candidate = getNode( candXPos , candYPos );

						/*
						 * Make sure candidate node is not obstructed or in the
						 * checked list
						 */

						if ( candidate != null
								&& ( candidate.getFeature().isPassable() || candidate.getFeature() instanceof trl.map.feature.DoorClosed )
								&& !checked.contains( candidate ) ){
							// If not on openList, add it.
							if ( !checking.contains( candidate ) ){
								tempList.add( candidate );
							}else{
								// Nothing
							}
						}else{
							// Nothing
						}
					}else{
						// Nothing
					}
				}
			}
			itChecking.remove();
		}
		// System.out.println("tempList size = " + tempList.size());
		checking.addAll( tempList );
		checked.addAll( tempList );
	}

	public boolean allNodesWalkable (){
		
		LOGGER.setLevel( Level.CONFIG );
		LOGGER.config("Adding all walkable adjacents nodes");

		int walkableNodes = 0;
		for ( int x = 0 ; x < this.mapGrid.length ; x++ ){
			for ( int y = 0 ; y < this.mapGrid[0].length ; y++ ){

				if ( this.mapGrid[x][y] != null ){
					if ( this.mapGrid[x][y].getFeature().isPassable() || this.mapGrid[x][y].getFeature() instanceof trl.map.feature.DoorClosed ){
						if ( this.mapGrid[x][y].getFeature() instanceof trl.map.feature.DoorOpen ){
							// Nothing
						}else{
							// Nothing
						}

						walkableNodes++;
					}else{
						// Nothing
					}
				}else{
					// Nothing
				}
			}
		}
		List<Node> checking = new ArrayList<Node>();
		List<Node> checked = new ArrayList<Node>();
		Node start = getRandomNode();

		checking.add( start );
		// First pass
		addWalkableAdjacents( checked , checking );

		// Repeating process
		while ( checked.size() != walkableNodes ){
			addWalkableAdjacents( checked , checking );
			if ( checking.isEmpty() && checked.size() != walkableNodes ){

				return false;
			}else{
				// Nothing
			}
		}
		return true;
	}

	public void connectRooms (){
		
		LOGGER.setLevel( Level.CONFIG );
		LOGGER.config("Possibiliting connect rooms");

		/*
		 * Check if all nodes walkable. If not, select two rooms at random and
		 * try to connect them. Keep trying until all walkable nodes are
		 * connected.
		 */
		while ( !allNodesWalkable() ){
			Random r = new Random();
			int column1 = (int) Math.floor( r.nextDouble() * this.rooms.length );
			int row1 = (int) Math.floor( r.nextDouble() * this.rooms[column1].length );
			int column2 = (int) Math.floor( r.nextDouble() * this.rooms.length );
			int row2 = (int) Math.floor( r.nextDouble() * this.rooms[column2].length );
			this.rooms[column1][row1].connect( this.rooms[column2][row2] );
		}
	}

	public List<Node> findPath ( Node start , Node end ){
		
		LOGGER.setLevel( Level.CONFIG );
		LOGGER.config("Finding paths");

		List<Node> openList = new ArrayList<Node>();
		List<Node> closedList = new ArrayList<Node>();
		this.endNodeFound = false;

		Node startNode = new Node( start.getAxisX() , start.getAxisY() , this );
		startNode.setParent( startNode );

		Node endNode = new Node( end.getAxisX() , end.getAxisY() , this );
		startNode.setGScore( 0 );
		startNode.setHScore( endNode );
		startNode.setFScore();
		openList.add( startNode );

		// First pass
		addAdjacents( openList , closedList , endNode , startNode );
		closedList.add( startNode );
		openList.remove( startNode );

		// Repeating process
		while ( !this.endNodeFound ){
			if ( openList.isEmpty() && !this.endNodeFound ){

				return null;
			}else{
				// Nothing
			}
			processOpenList( openList , closedList , endNode , true );
		}

		return getPath( startNode , endNode , closedList );
	}

	public List<Node> findRoomConnection ( Node start , Node end ){
		
		LOGGER.setLevel( Level.CONFIG );
		LOGGER.config("Finding room connection");

		List<Node> openList = new ArrayList<Node>();
		List<Node> closedList = new ArrayList<Node>();
		this.endNodeFound = false;

		Node startNode = new Node( start.getAxisX() , start.getAxisY() , this );
		startNode.setParent( startNode );

		Node endNode = new Node( end.getAxisX() , end.getAxisY() , this );
		startNode.setGScore( 0 );
		startNode.setHScore( endNode );
		startNode.setFScore();
		openList.add( startNode );

		// First pass
		addRectAdjacents( openList , closedList , endNode , startNode );
		closedList.add( startNode );
		openList.remove( startNode );

		// Repeating process
		while ( !this.endNodeFound ){
			if ( openList.isEmpty() && !this.endNodeFound ){
				return null;
			}else{
				// Nothing
			}
			processOpenList( openList , closedList , endNode , false );
		}

		return getConnection( startNode , endNode , closedList );
	}

	public void generateMap (){
		
		LOGGER.setLevel( Level.INFO );
		LOGGER.info("Generating map");

		generateRooms();

		connectRooms();

		placeHallwayWalls();
	}

	public void generateRooms (){
		
		LOGGER.setLevel( Level.INFO );
		LOGGER.info("Generating rooms");

		/*
		 * Divide map into cells and generate a room of random width/height
		 * centered within each cell.
		 */
		this.rooms = new Room[Game.COLUMNS / MAX_ROOM_WIDTH][Game.ROWS / MAX_ROOM_HEIGHT];

		for ( int column = 0 ; column < Game.COLUMNS / MAX_ROOM_WIDTH ; column++ ){
			for ( int row = 0 ; row < Game.ROWS / MAX_ROOM_HEIGHT ; row++ ){
				this.rooms[column][row] = new Room( this , row , column );
			}
		}

		for ( int column = 0 ; column < this.rooms.length ; column++ ){
			for ( int row = 0 ; row < this.rooms[column].length ; row++ ){
				for ( int x = this.rooms[column][row].getX() ; x < this.rooms[column][row].getX() + this.rooms[column][row].getWidth() ; x++ ){
					for ( int y = this.rooms[column][row].getY() ; y < this.rooms[column][row].getY() + this.rooms[column][row].getHeight() ; y++ ){
						/*
						 * Create a new node at x and y. If x and y aren't in
						 * boundary nodes, make them floor.
						 */
						createNode( x , y );
						if ( x != this.rooms[column][row].getX() && x != this.rooms[column][row].getX() + this.rooms[column][row].getWidth() - 1
								&& y != this.rooms[column][row].getY() && y != this.rooms[column][row].getY() + this.rooms[column][row].getHeight() - 1 ){
							getNode( x , y ).makeFloor();
						}
						// Else make them walls.
						else{
							getNode( x , y ).makeWall();
						}
					}
				}
			}
		}

	}

	public List<Node> getAoENodes ( Node origin , int radius ){
		
		LOGGER.setLevel( Level.CONFIG );
		LOGGER.config("Get AeO Nodes");

		List<Node> aoe = new ArrayList<Node>();
		for ( int x = origin.getAxisX() - radius ; x <= origin.getAxisX() + radius ; x++ ){
			for ( int y = origin.getAxisY() - radius ; y <= origin.getAxisY() + radius ; y++ ){
				if ( this.mapGrid[x][y] != null && this.mapGrid[x][y].getFeature().isPassable() && isVisibleToPlayer( this.mapGrid[x][y] ) ){
					aoe.add( this.mapGrid[x][y] );
				}else{
					// Nothing
				}
			}
		}
		return aoe;
	}
	
	/*
	  Returns node with F score from open
	  list lowest
	*/
	public Node getBestFScore ( List<Node> openList ){ 

		LOGGER.setLevel( Level.CONFIG );
		LOGGER.config("Get best F score");
		
		int bestF = 0;
		int currentF = 0;
		Node bestNode = null;
		for ( Node node : openList ){
			currentF = node.getFScore();
			if ( bestNode == null ){
				bestF = currentF;
				bestNode = node;
			}else if ( currentF < bestF ){
				bestF = currentF;
				bestNode = node;
			}else{
				// Nothing
			}
		}
		return bestNode;
	}

	public Node getBestGScore ( ArrayList<Node> openList ){
		
		LOGGER.setLevel( Level.CONFIG );
		LOGGER.config("Get best G score");

		Node bestNode = null;
		for ( Node node : openList ){
			if ( bestNode == null ){
				bestNode = node;
			}else{
				// Nothing
			}
			if ( node.getGScore() <= bestNode.getGScore() ){
				bestNode = node;
			}else{
				// Nothing
			}
		}
		return bestNode;
	}

	/**
	 * @param endNode  
	 */
	public List<Node> getConnection ( Node startNode , Node endNode , List<Node> closedList ){
		
		LOGGER.setLevel( Level.CONFIG );
		LOGGER.config("Getting connection");

		// Start with last item in list. should be destination.

		Node current = closedList.get( closedList.size() - 1 );
		List<Node> path = new ArrayList<Node>();
		int i = 0;

		while ( !current.getParent().equals( current ) ){
			path.add( i , current );
			current = current.getParent();
			i++;
		}

		Collections.reverse( path );
		path.add( 0 , startNode );
		return path;
	}

	public Node[][] getDisplayedNodes (){
		LOGGER.setLevel( Level.CONFIG );
		LOGGER.config("Getting display nodes");
		
		return this.displayedNodes;
	}

	public Node getDisplayedNodesOrigin (){

		LOGGER.setLevel( Level.CONFIG );
		LOGGER.config("Getting display nodes origin");

		// Returns origin node for drawing game window
		int x = 0 , y = 0;
		@SuppressWarnings ( "unused" )
		boolean nonNullFound = false;
		Node firstNonNull = this.displayedNodes[0][0];
		if ( firstNonNull == null ){
			for ( x = 0 ; x < Game.W_COLS ; x++ ){
				for ( y = 0 ; y < Game.W_ROWS ; y++ ){
					if ( this.displayedNodes[x][y] != null ){
						return new Node( this.displayedNodes[x][y].getAxisX() - x , this.displayedNodes[x][y].getAxisY() - y , this );
					}else{
						// Nothing
					}
				}
			}
		}
		return this.displayedNodes[0][0];
	}

	public int getDisplayedX ( Node node ){
		
		LOGGER.setLevel( Level.CONFIG );
		LOGGER.config("Getting displayed X");

		/*
		 * Return difference between supplied node's x position and the visible
		 * node list's origin x. This should be the relative column in the game
		 * window.
		 */
		// return node.getX() - displayedNodes[0][0].getX();

		/*
		 * Call getDisplayedNodesOrigin() instead of accessing mapGrid[x][y].
		 * The method call will handle the possibility that the origin node is
		 * null.
		 */
		return node.getAxisX() - displayedNodesMinX;
	}

	public int getDisplayedY ( Node node ){
		
		LOGGER.setLevel( Level.CONFIG );
		LOGGER.config("Getting displayed Y");

		/*
		 * Return difference between supplied node's y position and the visible
		 * node list's origin y. This should be the relative column in the game
		 * window. return node.getY() - displayedNodes[0][0].getY();
		 */
		return node.getAxisY() - displayedNodesMinY;
	}

	public List<Node> getRoomConnection ( Node start , Node end ){
		
		LOGGER.setLevel( Level.CONFIG );
		LOGGER.config("Getting room connection");

		List<Node> line = new ArrayList<Node>();
		int x0 = start.getAxisX();
		int y0 = start.getAxisY();
		int x1 = end.getAxisX();
		int y1 = end.getAxisY();
		int dx = Math.abs( x1 - x0 );
		int dy = Math.abs( y1 - y0 );

		int sx = x0 < x1 ? 1 : -1;
		int sy = y0 < y1 ? 1 : -1;
		int err = dx - dy;

		while ( true ){
			line.add( new Node( x0 , y0 , this ) );

			if ( x0 == x1 && y0 == y1 ){
				break;
			}else{
				// Nothing
			}
			int e2 = err * 2;
			if ( e2 > -dx ){
				err -= dy;
				x0 += sx;
			}else{
				// Nothing
			}
			if ( e2 < dx ){
				err += dx;
				y0 += sy;
			}else{
				// Nothing
			}
		}
		return line;
	}

	public List<Node> getLine ( Node start , Node end ){
		
		LOGGER.setLevel( Level.CONFIG );
		LOGGER.config("Getting line");

		List<Node> line = new ArrayList<Node>();
		int x0 = start.getAxisX();
		int y0 = start.getAxisY();
		int x1 = end.getAxisX();
		int y1 = end.getAxisY();
		int dx = Math.abs( x1 - x0 );
		int dy = Math.abs( y1 - y0 );

		int sx = x0 < x1 ? 1 : -1;
		int sy = y0 < y1 ? 1 : -1;
		int err = dx - dy;

		while ( true ){
			line.add( getNode( x0 , y0 ) );

			if ( x0 == x1 && y0 == y1 ){
				break;
			}else{
				// Nothing
			}
			int e2 = err * 2;
			if ( e2 > -dx ){
				err -= dy;
				x0 += sx;
			}else{
				// Nothing
			}
			if ( e2 < dx ){
				err += dx;
				y0 += sy;
			}else{
				// Nothing
			}
		}
		return line;
	}

	public Node getNode ( int x , int y ){
		
		LOGGER.setLevel( Level.CONFIG );
		LOGGER.config("Getting node");

		if ( x >= 0 && x < Game.COLUMNS && y >= 0 && y < Game.ROWS ){
			return this.mapGrid[x][y];
		}else{
			return null;
		}
	}

	public Node getNodeWith ( Entity entity ){
		
		LOGGER.setLevel( Level.CONFIG );
		LOGGER.config("Getting node with one entity");

		for ( int x = 0 ; x < Game.ROWS ; x++ ){
			for ( int y = 0 ; y < Game.COLUMNS ; y++ ){
				if ( this.mapGrid[x][y] != null ){
					if ( this.mapGrid[x][y].nodeContains( entity ) ){
						return this.mapGrid[x][y];
					}else{
						// Nothing
					}
				}else{
					// Nothing
				}
			}
		}
		return null;
	}

	/**
	 * @param startNode  
	 * @param endNode 
	 */
	public List<Node> getPath ( Node startNode , Node endNode , List<Node> closedList ){
		
		LOGGER.setLevel( Level.CONFIG );
		LOGGER.config("Getting path");

		// start with last item in list. should be destination.
		// System.out.println("Closed list size = " + closedList.size());
		Node current = closedList.get( closedList.size() - 1 );
		List<Node> path = new ArrayList<Node>();
		int i = 0;

		while ( !current.getParent().equals( current ) ){
			path.add( i , getNode( current.getAxisX() , current.getAxisY() ) );
			current = current.getParent();
			i++;
		}
		Collections.reverse( path );
		return path;
	}

	public Node getRandomNode (){
		
		LOGGER.setLevel( Level.CONFIG );
		LOGGER.config("Getting random node");

		// Returns a random, unoccupied floor node.
		Random r = new Random();
		boolean gotNode = false;
		while ( !gotNode ){
			int x = (int) ( r.nextDouble() * this.hSize );
			int y = (int) ( r.nextDouble() * this.vSize );
			// Using isFLoor() prevents entities from spawning in open doorways
			if ( getNode( x , y ) != null && getNode( x , y ).isFloor() ){
				if ( !getNode( x , y ).checkEntityByID( (byte) 4 ) ){
					gotNode = true;
					return getNode( x , y );
				}else{
					// Nothing
				}
			}else{
				// Nothing
			}
		}
		return null;
	}

	public Node getRandomNodeInRoom (){
		
		LOGGER.setLevel( Level.CONFIG );
		LOGGER.config("Getting random node in room");

		/*
		 * Returns a random, unoccupied floor node. Checks to make sure node is
		 * within the boundaries of a room.
		 */

		Random r = new Random();
		// Select random room
		Room room = getRandomRoom();

		boolean gotNode = false;
		while ( !gotNode ){
			int x = (int) ( r.nextDouble() * this.hSize );
			int y = (int) ( r.nextDouble() * this.vSize );
			if ( x > room.getX() && x < room.getX() + room.getWidth() && y > room.getY() && y < room.getY() + room.getHeight() ){
				// Using isFLoor() prevents entities from spawning in open
				// doorways
				if ( getNode( x , y ) != null && getNode( x , y ).isFloor() ){
					if ( !getNode( x , y ).checkEntityByID( (byte) 4 ) ){
						gotNode = true;
						return getNode( x , y );
					}else{
						// Nothing
					}
				}else{
					// Nothing
				}
			}else{
				// Nothing
			}
		}
		return null;
	}

	public Room getRandomRoom (){
		
		LOGGER.setLevel( Level.CONFIG );
		LOGGER.config("Getting random room");

		Random r = new Random();
		int roomColumn = (int) ( r.nextDouble() * this.rooms.length );
		int roomRow = (int) ( r.nextDouble() * this.rooms[0].length );
		return this.rooms[roomColumn][roomRow];
	}

	public List<Node> getVisibleToPlayer (){

		return this.visibleToPlayer;
	}

	public boolean hammerOnMap (){
		
		LOGGER.setLevel( Level.INFO );
		LOGGER.info("Verifing if contains one hammer on map");

		for ( int x = 0 ; x < Game.ROWS ; x++ ){
			for ( int y = 0 ; y < Game.COLUMNS ; y++ ){
				if ( this.mapGrid[x][y] != null ){
					if ( this.mapGrid[x][y].checkEntityByID( (byte) 4 ) ){
						for ( Entity entity : this.mapGrid[x][y].getEntities() ){
							if ( entity instanceof trl.entity.item.Hammer ){
								LOGGER.setLevel( Level.INFO );
								LOGGER.info("Hammer found");
								return true;
							}else{
								// Nothing
							}
						}
					}else{
						// Nothing
					}
				}else{
					// Nothing
				}
			}
		}
		LOGGER.setLevel( Level.INFO );
		LOGGER.info("Hammer not found");
		return false;
	}

	public void init (){
		
		LOGGER.setLevel( Level.INFO );
		LOGGER.info("Initializing map");

		this.mapGrid = new Node[this.vSize][this.hSize];
		this.displayedNodes = new Node[Game.W_ROWS][Game.W_COLS];
		this.visibleToPlayer = new ArrayList<Node>();
		this.imageMap = new BufferedImage[Game.W_COLS][Game.W_ROWS];
		generateMap();
	}

	public boolean isLegalCell ( Node node ){
		
		LOGGER.setLevel( Level.CONFIG );
		LOGGER.config("Verifying if is a legal cell");

		if ( node.getAxisX() >= 0 && node.getAxisX() < Game.COLUMNS ){
			if ( node.getAxisY() >= 0 && node.getAxisY() < Game.ROWS ){
				if ( node.getFeature().isPassable() ){
					LOGGER.setLevel( Level.CONFIG);
					LOGGER.config("Is a legal cell");
					return true;
				}else{
					// Nothing
				}
			}else{
				// Nothing
			}
		}else{
			// Nothing
		}
		LOGGER.setLevel( Level.CONFIG);
		LOGGER.config("Isn't a legal cell");
		return false;
	}

	public boolean isVisibleToPlayer ( Node node ){

		if ( this.visibleToPlayer.contains( node ) ){
			LOGGER.setLevel( Level.CONFIG);
			LOGGER.config("Node visible to player");
			return true;
		}else{
			LOGGER.setLevel( Level.CONFIG);
			LOGGER.config("Node isn't visible to player");
			return false;
		}
		
	}

	public Node placeEntity ( Entity entity , Node node ){

		node.addEntity( entity );
		return node;
	}

	public void placeHallwayWalls (){
		
		LOGGER.setLevel( Level.INFO);
		LOGGER.info("Placing hallway walls");

		/*
		 * Check for newly created hallways that border on void nodes. Change
		 * the bordering nodes to walls.
		 */
		for ( int x = 0 ; x < this.mapGrid.length ; x++ ){
			for ( int y = 0 ; y < this.mapGrid[0].length ; y++ ){
				if ( this.mapGrid[x][y] != null && this.mapGrid[x][y].isFloor() ){
					for ( int neighborX = x - 1 ; neighborX <= x + 1 ; neighborX++ ){
						for ( int neighborY = y - 1 ; neighborY <= y + 1 ; neighborY++ ){
							if ( neighborX >= 0 && neighborY >= 0 && neighborX < this.mapGrid.length && neighborY < this.mapGrid[0].length ){
								if ( this.mapGrid[neighborX][neighborY] == null ){
									Node neighbor = new Node( neighborX , neighborY , this );
									createNode( neighbor );
									neighbor.makeWall();
								}else{
									// Nothing
								}
							}else{
								// Nothing
							}
						}
					}
				}else{
					// Nothing
				}
			}
		}
	}

	public boolean potionOnMap (){
		
		LOGGER.setLevel( Level.INFO);
		LOGGER.info("Verifying if have potion on map");

		for ( int x = 0 ; x < Game.ROWS ; x++ ){
			for ( int y = 0 ; y < Game.COLUMNS ; y++ ){
				if ( this.mapGrid[x][y] != null ){
					if ( this.mapGrid[x][y].checkEntityByID( (byte) 4 ) ){
						for ( Entity entity : this.mapGrid[x][y].getEntities() ){
							if ( entity instanceof trl.entity.item.Potion ){
								LOGGER.setLevel( Level.INFO);
								LOGGER.info("Have potion on map");
								return true;
							}else{
								// Nothing
							}
						}
					}else{
						// Nothing
					}
				}else{
					// Nothing
				}
			}
		}
		LOGGER.setLevel( Level.INFO);
		LOGGER.info("Have not potion on map");
		return false;
	}

	public void printFeatureList (){

		LOGGER.setLevel( Level.INFO);
		LOGGER.info("Printing feature list");
		
		for ( int x = 0 ; x < this.hSize ; x++ ){
			for ( int y = 0 ; y < this.vSize ; y++ ){
				System.out.println( x + "," + y + ": " + getNode( x , y ).getFeature().toString() + ", passable = "
						+ getNode( x , y ).getFeature().isPassable() );
			}
		}
	}

	public void processOpenList ( List<Node> openList , List<Node> closedList , Node endNode , boolean diagAllowed ){

		LOGGER.setLevel( Level.CONFIG);
		LOGGER.config("Processing open list");
		
		Node bestNode = getBestFScore( openList );

		openList.remove( bestNode );
		closedList.add( bestNode );
		if ( diagAllowed ){
			addAdjacents( openList , closedList , endNode , bestNode );
		}else{
			addRectAdjacents( openList , closedList , endNode , bestNode );
		}
		if ( bestNode.equals( endNode ) ){
			this.endNodeFound = true;
		}else{
			// Nothing
		}
	}

	public void render ( Graphics g ){
		
		LOGGER.setLevel( Level.CONFIG);
		LOGGER.config("Rendering graph");

		g.setColor( Color.BLACK );
		g.fillRect( 0 , 0 , Game.W_WIDTH , Game.W_HEIGHT );
		for ( int x = 0 ; x < Game.W_COLS ; x++ ){
			for ( int y = 0 ; y < Game.W_ROWS ; y++ ){
				// Skip null (void) nodes
				if ( this.imageMap[x][y] != null ){
					// Draw features
					g.drawImage( this.imageMap[x][y] , x * Game.SCALED_TILE_SIZE , ( Game.W_HEIGHT - Game.SCALED_TILE_SIZE ) - y
							* Game.SCALED_TILE_SIZE , Game.SCALED_TILE_SIZE , Game.SCALED_TILE_SIZE , null );
					// Draw entities
					if ( this.visibleToPlayer.contains( this.displayedNodes[x][y] ) ){
						if ( this.displayedNodes[x][y].getEntities() != null && this.displayedNodes[x][y].getEntities().size() > 0 ){
							for ( Entity entity : this.displayedNodes[x][y].getEntities() ){
								// Skip actors. They are drawn in their
								// Respective classes.
								if ( !( entity instanceof trl.entity.actor.Actor ) ){
									g.drawImage( entity.getImage() , x * Game.SCALED_TILE_SIZE , ( Game.W_HEIGHT - Game.SCALED_TILE_SIZE )
											- y * Game.SCALED_TILE_SIZE , Game.SCALED_TILE_SIZE , Game.SCALED_TILE_SIZE , null );
								}else{
									// Nothing
								}
							}
						}else{
							// Nothing
						}
					}else{
						// Nothing
					}
				}else{
					// Nothing
				}
			}
		}
	}

	public void revealAll (){

		for ( int x = 0 ; x < Game.ROWS ; x++ ){
			for ( int y = 0 ; y < Game.COLUMNS ; y++ ){
				if ( this.mapGrid[x][y] != null ){
					this.mapGrid[x][y].setSeenByPlayer( true );
					if ( this.mapGrid[x][y].getEntities() != null && this.mapGrid[x][y].getEntities().size() > 0 ){
						for ( Entity entity : this.mapGrid[x][y].getEntities() ){
							entity.setSeenByPlayer( true );
						}
					}else{
						// Nothing
					}
				}else{
					// Nothing
				}
			}
		}
	}

	public void updateDisplayedNodes (){
		
		LOGGER.setLevel( Level.CONFIG);
		LOGGER.config("Updating displayed nodes");

		// Can't be called until player is instantiated

		/*
		 * Position the "frame" of nodes relative to the player's position.
		 * startX and startY represent the cartesian coordinates (lower left
		 * origin) of the frame.
		 */

		int startX = 0 , startY = 0;
		if ( GameplayState.getPlayer().getAxisX() <= Game.W_COLS / 2 ){
			startX = 0;
		}else if ( GameplayState.getPlayer().getAxisX() + ( Game.W_COLS / 2 ) >= Game.COLUMNS ){
			startX = Game.COLUMNS - Game.W_COLS;
		}else{
			startX = GameplayState.getPlayer().getAxisX() - Game.W_COLS / 2;
		}
		if ( GameplayState.getPlayer().getAxisY() <= Game.W_ROWS / 2 ){
			startY = 0;
		}else if ( GameplayState.getPlayer().getAxisY() + Game.W_ROWS / 2 >= Game.COLUMNS ){
			startY = Game.COLUMNS - Game.W_COLS;
		}else{
			startY = GameplayState.getPlayer().getAxisY() - Game.W_ROWS / 2;
		}

		for ( int x = 0 ; x < Game.W_COLS ; x++ ){
			for ( int y = 0 ; y < Game.W_ROWS ; y++ ){
				this.displayedNodes[x][y] = this.mapGrid[startX + x][startY + y];
			}
		}

		setDisplayedNodesMinX();
		setDisplayedNodesMinY();
		setDisplayedNodesMaxX();
		setDisplayedNodesMaxY();
	}

	public void updateImageMap (){
		
		LOGGER.setLevel( Level.CONFIG);
		LOGGER.config("Updating image map");

		for ( int x = 0 ; x < this.displayedNodes.length ; x++ ){
			for ( int y = 0 ; y < this.displayedNodes[0].length ; y++ ){
				Node current = this.displayedNodes[x][y];
				if ( current != null ){
					if ( current.isFloor() ){
						if ( this.visibleToPlayer.contains( current ) ){
							this.imageMap[x][y] = Game.getImageManager().stoneTile;
						}else if ( current.seenByPlayer() ){
							this.imageMap[x][y] = Game.getImageManager().stoneTile1Shadow;
						}else{
							this.imageMap[x][y] = null;
						}
					}else if ( current.isWall() ){
						if ( this.visibleToPlayer.contains( current ) ){
							this.imageMap[x][y] = Game.getImageManager().stoneWall;
						}else if ( current.seenByPlayer() ){
							this.imageMap[x][y] = Game.getImageManager().stoneWallShadow;
						}else{
							this.imageMap[x][y] = null;
						}
					}else if ( current.isOpenDoor() ){
						if ( this.visibleToPlayer.contains( current ) ){
							this.imageMap[x][y] = Game.getImageManager().openDoor;
						}else if ( current.seenByPlayer() ){
							this.imageMap[x][y] = Game.getImageManager().openDoorShadow;
						}else{
							this.imageMap[x][y] = null;
						}
					}else if ( current.isClosedDoor() ){
						if ( this.visibleToPlayer.contains( current ) ){
							this.imageMap[x][y] = Game.getImageManager().closedDoor;
						}else if ( current.seenByPlayer() ){
							this.imageMap[x][y] = Game.getImageManager().closedDoorShadow;
						}else{
							this.imageMap[x][y] = null;
						}
					}else if ( current.isStairDown() ){
						if ( this.visibleToPlayer.contains( current ) ){
							this.imageMap[x][y] = Game.getImageManager().stairDown;
						}else if ( current.seenByPlayer() ){
							this.imageMap[x][y] = Game.getImageManager().stairDownShadow;
						}else{
							this.imageMap[x][y] = null;
						}
					}else if ( current.isGoal() ){
						if ( this.visibleToPlayer.contains( current ) ){
							this.imageMap[x][y] = Game.getImageManager().goal;
						}else if ( current.seenByPlayer() ){
							this.imageMap[x][y] = Game.getImageManager().goalShadow;
						}else{
							this.imageMap[x][y] = null;
						}
					}
				}else{
					this.imageMap[x][y] = null;
				}
			}
		}
	}

	public void updateVisibleToPlayer (){
		
		LOGGER.setLevel( Level.CONFIG);
		LOGGER.config("Updating if is visible to player");

		/*
		 * New strategy: Just clear visibleToPlayer and set all enemieseach time
		 * this method is called.
		 */
		this.visibleToPlayer.clear();
		for ( Enemy enemy : GameplayState.getEnemyGroup().getEnemies() ){
			enemy.setVisibleToPlayer( false );
		}

		// Check line of sight to nodes currently in displayedNodes[][]

		// Loop through displayed nodes
		for ( int x = 0 ; x < Game.W_COLS ; x++ ){
			for ( int y = 0 ; y < Game.W_ROWS ; y++ ){
				Node displayedNode = this.displayedNodes[x][y];
				// Don't bother with LoS for null nodes.
				if ( displayedNode != null ){
					// PlayerLoS contains all nodes in a Bresenham line
					// Between player loc and target node
					List<Node> playerLoS = getLine( GameplayState.getPlayer().getLoc() , displayedNode );
					boolean losObstructed = false;
					// Step through LoS list looking for obstacles
					for ( Node losNode : playerLoS ){
						/*
						 * If any node, excluding the displayed node we are
						 * trying to draw LoS to, has an impassible feature, LoS
						 * is obstructed. If we're trying to get LoS to a
						 * visible wall, its presence in playerLoS would make
						 * any LoS to it obstructed.
						 */
						if ( !losNode.getFeature().isPassable() && !losNode.equals( displayedNode ) ){
							losObstructed = true;
							break;
						}else{
							// Nothing
						}
					}

					// If LoS is clear as checked above
					if ( !losObstructed ){
						// Set node's seenByPlayer flag to true
						displayedNode.setSeenByPlayer( true );
						/*
						 * If the unobstructed node isn't already in
						 * visibleToPlayer, add it.
						 */
						if ( !this.visibleToPlayer.contains( displayedNode ) ){
							this.visibleToPlayer.add( displayedNode );
						}else{
							// Nothing
						}
						/*
						 * Flag entities on this node seenByPlayer and
						 * visibleToPlayer
						 */
						if ( displayedNode.getEntities() != null && displayedNode.getEntities().size() > 0 ){
							for ( Entity entity : displayedNode.getEntities() ){
								if ( !entity.getSeenByPlayer() ){
									GameplayState.getPlayer().setNewEnemies( true );
								}else{
									// Nothing
								}
								entity.setSeenByPlayer( true );
								entity.setVisibleToPlayer( true );
							}
						}else{
							// Nothing
						}
					}
				}else{
					// Nothing
				}
			}
		}
	}

	public void createNode ( int x , int y ){
		
		LOGGER.setLevel( Level.CONFIG);
		LOGGER.config("Creating one node");

		this.mapGrid[x][y] = new Node( x , y , this );
		this.mapGrid[x][y].makeFloor();
	}

	@SuppressWarnings ( "null" )
	public void createNode ( Node node ){
		
		LOGGER.setLevel( Level.CONFIG);
		LOGGER.config("Creating one node");

		if ( this.mapGrid == null ){
			// System.out.println("mapGrid NULL");
		}else{
			// Nothing
		}
		if ( node == null ){
			// System.out.println("node NULL");
		}else{
			// Nothing
		}
		this.mapGrid[node.getAxisX()][node.getAxisY()] = node;
	}

	public void setDisplayedNodesMinX (){
		
		LOGGER.setLevel( Level.CONFIG);
		LOGGER.config("Set displayed nodes (Min X)");

		/*
		 * Return x value of origin node in displayedNodes. Since
		 * displayedNodes[0][0] could be null, we step through x'es until we
		 * find a non-null node, get its x value, then subtract the number of x
		 * steps it took to find it.
		 */
		int x = 0 , y = 0;
		boolean nonNullFound = false;
		Node firstNonNull = this.displayedNodes[0][0];
		/* If origin node is null */
		if ( firstNonNull == null ){
			for ( x = 0 ; x < Game.W_COLS ; x++ ){
				for ( y = 0 ; y < Game.W_ROWS ; y++ ){
					if ( this.displayedNodes[x][y] != null ){

						displayedNodesMinX = this.displayedNodes[x][y].getAxisX() - x;
						nonNullFound = true;
						break;
					}else{
						// Nothing
					}
				}
				if ( nonNullFound ){
					break;
				}else{
					// Nothing
				}
			}
		}else{
			// If origin node is non-null, just return its x value
			displayedNodesMinX = this.displayedNodes[0][0].getAxisX();
		}
	}

	public void setDisplayedNodesMinY (){
		
		LOGGER.setLevel( Level.CONFIG);
		LOGGER.config("Set displayed nodes (Min Y)");

		/*
		 * Return y value of origin node in displayedNodes. Since
		 * displayedNodes[0][0] could be null, we step through y's until we find
		 * a non-null node, get its y value, then subtract the number of y steps
		 * it took to find it.
		 */
		int x = 0 , y = 0;
		boolean nonNullFound = false;
		Node firstNonNull = this.displayedNodes[0][0];
		/* If origin node is null */
		if ( firstNonNull == null ){
			for ( x = 0 ; x < Game.W_COLS ; x++ ){
				for ( y = 0 ; y < Game.W_ROWS ; y++ ){
					if ( this.displayedNodes[x][y] != null ){
						displayedNodesMinY = this.displayedNodes[x][y].getAxisY() - y;
						nonNullFound = true;
						break;
					}else{
						// Nothing
					}
				}
				if ( nonNullFound ){
					break;
				}else{
					// Nothing
				}
			}
		}else{
			// If origin node non-null, return its y value
			displayedNodesMinY = this.displayedNodes[0][0].getAxisY();
		}
	}

	public void setDisplayedNodesMaxX (){
		
		LOGGER.setLevel( Level.CONFIG);
		LOGGER.config("Set displayed nodes (Max X)");

		displayedNodesMaxX = displayedNodesMinX + Game.W_COLS;
	}

	public void setDisplayedNodesMaxY (){
		
		LOGGER.setLevel( Level.CONFIG);
		LOGGER.config("Set displayed nodes (Max Y)");

		displayedNodesMaxY = displayedNodesMinY + Game.W_ROWS;
	}

	public int getNonNullNodeCount (){
		
		LOGGER.setLevel( Level.CONFIG);
		LOGGER.config("Getting non null node count");

		int count = 0;
		for ( int x = 0 ; x < Game.COLUMNS ; x++ ){
			for ( int y = 0 ; y < Game.ROWS ; y++ ){
				if ( this.mapGrid[x][y] != null ){
					count++;
				}else{
					// Nothing
				}
			}
		}
		return count;
	}

	public Room[][] getRooms (){

		LOGGER.setLevel( Level.CONFIG);
		LOGGER.config("Getting rooms");
		return this.rooms;
	}

	public boolean inRoom ( Node node ){
		
		LOGGER.setLevel( Level.CONFIG);
		LOGGER.config("Verifying if node is in a room");

		Point loc = new Point( node.getAxisX() , node.getAxisY() );
		for ( int x = 0 ; x < this.rooms.length ; x++ ){
			for ( int y = 0 ; y < this.rooms[0].length ; y++ ){
				if ( this.rooms[x][y].getBoundary().contains( loc ) ){
					return true;
				}else{
					// Nothing
				}
			}
		}
		return false;
	}

	/**
	 * @param loc  
	 */
	public Room getNearestRoom ( Node loc ){
		
		LOGGER.setLevel( Level.CONFIG);
		LOGGER.config("Getting nearest room");


		int roomColumn = Game.COLUMNS / MAX_ROOM_WIDTH - 1;
		int roomRow = Game.ROWS / MAX_ROOM_HEIGHT - 1;
		Room nearest = this.rooms[roomColumn][roomRow];
		return nearest;
	}
}
