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
import trl.entity.item.Key;
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

		LOGGER.setLevel( Level.INFO );
		LOGGER.info("Map Initialized");
		
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
					if ( ( candXPos >= 0 && candXPos < hSize ) && ( candYPos >= 0 && candYPos < vSize ) ){
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
		for ( int x = 0 ; x < mapGrid.length ; x++ ){
			for ( int y = 0 ; y < mapGrid[0].length ; y++ ){

				if ( mapGrid[x][y] != null ){
					if ( mapGrid[x][y].getFeature().isPassable() || mapGrid[x][y].getFeature() instanceof trl.map.feature.DoorClosed ){
						if ( mapGrid[x][y].getFeature() instanceof trl.map.feature.DoorOpen ){
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
			int column1 = (int) Math.floor( r.nextDouble() * rooms.length );
			int row1 = (int) Math.floor( r.nextDouble() * rooms[column1].length );
			int column2 = (int) Math.floor( r.nextDouble() * rooms.length );
			int row2 = (int) Math.floor( r.nextDouble() * rooms[column2].length );
			rooms[column1][row1].connect( rooms[column2][row2] );
		}
	}

	public List<Node> findPath ( Node start , Node end ){
		
		LOGGER.setLevel( Level.CONFIG );
		LOGGER.config("Finding paths");

		List<Node> openList = new ArrayList<Node>();
		List<Node> closedList = new ArrayList<Node>();
		endNodeFound = false;

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
		while ( !endNodeFound ){
			if ( openList.isEmpty() && !endNodeFound ){

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
		endNodeFound = false;

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
		while ( !endNodeFound ){
			if ( openList.isEmpty() && !endNodeFound ){
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

		// Place stairway

		@SuppressWarnings ( "unused" )
		Key key = new Key( this );
	}

	public void generateRooms (){
		
		LOGGER.setLevel( Level.INFO );
		LOGGER.info("Generating rooms");

		/*
		 * Divide map into cells and generate a room of random width/height
		 * centered within each cell.
		 */
		rooms = new Room[(int) Game.COLUMNS / MAX_ROOM_WIDTH][(int) Game.ROWS / MAX_ROOM_HEIGHT];

		for ( int column = 0 ; column < (int) Game.COLUMNS / MAX_ROOM_WIDTH ; column++ ){
			for ( int row = 0 ; row < Game.ROWS / MAX_ROOM_HEIGHT ; row++ ){
				rooms[column][row] = new Room( this , row , column );
			}
		}

		for ( int column = 0 ; column < rooms.length ; column++ ){
			for ( int row = 0 ; row < rooms[column].length ; row++ ){
				for ( int x = rooms[column][row].getX() ; x < rooms[column][row].getX() + rooms[column][row].getWidth() ; x++ ){
					for ( int y = rooms[column][row].getY() ; y < rooms[column][row].getY() + rooms[column][row].getHeight() ; y++ ){
						/*
						 * Create a new node at x and y. If x and y aren't in
						 * boundary nodes, make them floor.
						 */
						createNode( x , y );
						if ( x != rooms[column][row].getX() && x != rooms[column][row].getX() + rooms[column][row].getWidth() - 1
								&& y != rooms[column][row].getY() && y != rooms[column][row].getY() + rooms[column][row].getHeight() - 1 ){
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

		List<Node> aoe = new ArrayList<Node>();
		for ( int x = origin.getAxisX() - radius ; x <= origin.getAxisX() + radius ; x++ ){
			for ( int y = origin.getAxisY() - radius ; y <= origin.getAxisY() + radius ; y++ ){
				if ( mapGrid[x][y] != null && mapGrid[x][y].getFeature().isPassable() && isVisibleToPlayer( mapGrid[x][y] ) ){
					aoe.add( mapGrid[x][y] );
				}else{
					// Nothing
				}
			}
		}
		return aoe;
	}

	public Node getBestFScore ( List<Node> openList ){ // Returns node with

														// lowest

		// F score from open
		// list
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

	public List<Node> getConnection ( Node startNode , Node endNode , List<Node> closedList ){

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

		return displayedNodes;
	}

	public Node getDisplayedNodesOrigin (){

		// Returns origin node for drawing game window
		int x = 0 , y = 0;
		@SuppressWarnings ( "unused" )
		boolean nonNullFound = false;
		Node firstNonNull = displayedNodes[0][0];
		if ( firstNonNull == null ){
			for ( x = 0 ; x < Game.W_COLS ; x++ ){
				for ( y = 0 ; y < Game.W_ROWS ; y++ ){
					if ( displayedNodes[x][y] != null ){
						return new Node( displayedNodes[x][y].getAxisX() - x , displayedNodes[x][y].getAxisY() - y , this );
					}else{
						// Nothing
					}
				}
			}
		}
		return displayedNodes[0][0];
	}

	public int getDisplayedX ( Node node ){

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

		/*
		 * Return difference between supplied node's y position and the visible
		 * node list's origin y. This should be the relative column in the game
		 * window. return node.getY() - displayedNodes[0][0].getY();
		 */
		return node.getAxisY() - displayedNodesMinY;
	}

	public List<Node> getRoomConnection ( Node start , Node end ){

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

		if ( x >= 0 && x < Game.COLUMNS && y >= 0 && y < Game.ROWS ){
			return mapGrid[x][y];
		}else{
			return null;
		}
	}

	public Node getNodeWith ( Entity entity ){

		for ( int x = 0 ; x < Game.ROWS ; x++ ){
			for ( int y = 0 ; y < Game.COLUMNS ; y++ ){
				if ( mapGrid[x][y] != null ){
					if ( mapGrid[x][y].nodeContains( entity ) ){
						return mapGrid[x][y];
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

	public List<Node> getPath ( Node startNode , Node endNode , List<Node> closedList ){

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

		// Returns a random, unoccupied floor node.
		Random r = new Random();
		boolean gotNode = false;
		while ( !gotNode ){
			int x = (int) ( r.nextDouble() * hSize );
			int y = (int) ( r.nextDouble() * vSize );
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

		/*
		 * Returns a random, unoccupied floor node. Checks to make sure node is
		 * within the boundaries of a room.
		 */

		Random r = new Random();
		// Select random room
		Room room = getRandomRoom();

		boolean gotNode = false;
		while ( !gotNode ){
			int x = (int) ( r.nextDouble() * hSize );
			int y = (int) ( r.nextDouble() * vSize );
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

		Random r = new Random();
		int roomColumn = (int) ( r.nextDouble() * rooms.length );
		int roomRow = (int) ( r.nextDouble() * rooms[0].length );
		return rooms[roomColumn][roomRow];
	}

	public List<Node> getVisibleToPlayer (){

		return visibleToPlayer;
	}

	public boolean hammerOnMap (){

		for ( int x = 0 ; x < Game.ROWS ; x++ ){
			for ( int y = 0 ; y < Game.COLUMNS ; y++ ){
				if ( mapGrid[x][y] != null ){
					if ( mapGrid[x][y].checkEntityByID( (byte) 4 ) ){
						for ( Entity entity : mapGrid[x][y].getEntities() ){
							if ( entity instanceof trl.entity.item.Hammer ){
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
		return false;
	}

	public void init (){

		mapGrid = new Node[vSize][hSize];
		displayedNodes = new Node[Game.W_ROWS][Game.W_COLS];
		visibleToPlayer = new ArrayList<Node>();
		imageMap = new BufferedImage[Game.W_COLS][Game.W_ROWS];
		generateMap();
	}

	public boolean isLegalCell ( Node node ){

		if ( node.getAxisX() >= 0 && node.getAxisX() < Game.COLUMNS ){
			if ( node.getAxisY() >= 0 && node.getAxisY() < Game.ROWS ){
				if ( node.getFeature().isPassable() ){
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
		return false;
	}

	public boolean isVisibleToPlayer ( Node node ){

		if ( visibleToPlayer.contains( node ) ){
			return true;
		}else{
			// Nothing
		}
		return false;
	}

	public Node placeEntity ( Entity entity , Node node ){

		node.addEntity( entity );
		return node;
	}

	public void placeHallwayWalls (){

		/*
		 * Check for newly created hallways that border on void nodes. Change
		 * the bordering nodes to walls.
		 */
		for ( int x = 0 ; x < mapGrid.length ; x++ ){
			for ( int y = 0 ; y < mapGrid[0].length ; y++ ){
				if ( mapGrid[x][y] != null && mapGrid[x][y].isFloor() ){
					for ( int neighborX = x - 1 ; neighborX <= x + 1 ; neighborX++ ){
						for ( int neighborY = y - 1 ; neighborY <= y + 1 ; neighborY++ ){
							if ( neighborX >= 0 && neighborY >= 0 && neighborX < mapGrid.length && neighborY < mapGrid[0].length ){
								if ( mapGrid[neighborX][neighborY] == null ){
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

		for ( int x = 0 ; x < Game.ROWS ; x++ ){
			for ( int y = 0 ; y < Game.COLUMNS ; y++ ){
				if ( mapGrid[x][y] != null ){
					if ( mapGrid[x][y].checkEntityByID( (byte) 4 ) ){
						for ( Entity entity : mapGrid[x][y].getEntities() ){
							if ( entity instanceof trl.entity.item.Potion ){
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
		return false;
	}

	public void printFeatureList (){

		for ( int x = 0 ; x < hSize ; x++ ){
			for ( int y = 0 ; y < vSize ; y++ ){
				System.out.println( x + "," + y + ": " + getNode( x , y ).getFeature().toString() + ", passable = "
						+ getNode( x , y ).getFeature().isPassable() );
			}
		}
	}

	public void processOpenList ( List<Node> openList , List<Node> closedList , Node endNode , boolean diagAllowed ){

		Node bestNode = getBestFScore( openList );

		openList.remove( bestNode );
		closedList.add( bestNode );
		if ( diagAllowed ){
			addAdjacents( openList , closedList , endNode , bestNode );
		}else{
			addRectAdjacents( openList , closedList , endNode , bestNode );
		}
		if ( bestNode.equals( endNode ) ){
			endNodeFound = true;
		}else{
			// Nothing
		}
	}

	public void render ( Graphics g ){

		g.setColor( Color.BLACK );
		g.fillRect( 0 , 0 , Game.W_WIDTH , Game.W_HEIGHT );
		for ( int x = 0 ; x < Game.W_COLS ; x++ ){
			for ( int y = 0 ; y < Game.W_ROWS ; y++ ){
				// Skip null (void) nodes
				if ( imageMap[x][y] != null ){
					// Draw features
					g.drawImage( imageMap[x][y] , x * Game.SCALED_TILE_SIZE , ( Game.W_HEIGHT - Game.SCALED_TILE_SIZE ) - y
							* Game.SCALED_TILE_SIZE , Game.SCALED_TILE_SIZE , Game.SCALED_TILE_SIZE , null );
					// Draw entities
					if ( visibleToPlayer.contains( displayedNodes[x][y] ) ){
						if ( displayedNodes[x][y].getEntities() != null && displayedNodes[x][y].getEntities().size() > 0 ){
							for ( Entity entity : displayedNodes[x][y].getEntities() ){
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
				if ( mapGrid[x][y] != null ){
					mapGrid[x][y].setSeenByPlayer( true );
					if ( mapGrid[x][y].getEntities() != null && mapGrid[x][y].getEntities().size() > 0 ){
						for ( Entity entity : mapGrid[x][y].getEntities() ){
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
			startX = GameplayState.getPlayer().getAxisX() - (int) ( Game.W_COLS / 2 );
		}
		if ( GameplayState.getPlayer().getAxisY() <= Game.W_ROWS / 2 ){
			startY = 0;
		}else if ( GameplayState.getPlayer().getAxisY() + (int) ( Game.W_ROWS / 2 ) >= Game.COLUMNS ){
			startY = Game.COLUMNS - Game.W_COLS;
		}else{
			startY = GameplayState.getPlayer().getAxisY() - Game.W_ROWS / 2;
		}

		for ( int x = 0 ; x < Game.W_COLS ; x++ ){
			for ( int y = 0 ; y < Game.W_ROWS ; y++ ){
				displayedNodes[x][y] = mapGrid[startX + x][startY + y];
			}
		}

		setDisplayedNodesMinX();
		setDisplayedNodesMinY();
		setDisplayedNodesMaxX();
		setDisplayedNodesMaxY();
	}

	public void updateImageMap (){

		for ( int x = 0 ; x < displayedNodes.length ; x++ ){
			for ( int y = 0 ; y < displayedNodes[0].length ; y++ ){
				Node current = displayedNodes[x][y];
				if ( current != null ){
					if ( current.isFloor() ){
						if ( visibleToPlayer.contains( current ) ){
							imageMap[x][y] = Game.getImageManager().stoneTile;
						}else if ( current.seenByPlayer() ){
							imageMap[x][y] = Game.getImageManager().stoneTile1Shadow;
						}else{
							imageMap[x][y] = null;
						}
					}else if ( current.isWall() ){
						if ( visibleToPlayer.contains( current ) ){
							imageMap[x][y] = Game.getImageManager().stoneWall;
						}else if ( current.seenByPlayer() ){
							imageMap[x][y] = Game.getImageManager().stoneWallShadow;
						}else{
							imageMap[x][y] = null;
						}
					}else if ( current.isOpenDoor() ){
						if ( visibleToPlayer.contains( current ) ){
							imageMap[x][y] = Game.getImageManager().openDoor;
						}else if ( current.seenByPlayer() ){
							imageMap[x][y] = Game.getImageManager().openDoorShadow;
						}else{
							imageMap[x][y] = null;
						}
					}else if ( current.isClosedDoor() ){
						if ( visibleToPlayer.contains( current ) ){
							imageMap[x][y] = Game.getImageManager().closedDoor;
						}else if ( current.seenByPlayer() ){
							imageMap[x][y] = Game.getImageManager().closedDoorShadow;
						}else{
							imageMap[x][y] = null;
						}
					}else if ( current.isStairDown() ){
						if ( visibleToPlayer.contains( current ) ){
							imageMap[x][y] = Game.getImageManager().stairDown;
						}else if ( current.seenByPlayer() ){
							imageMap[x][y] = Game.getImageManager().stairDownShadow;
						}else{
							imageMap[x][y] = null;
						}
					}else if ( current.isGoal() ){
						if ( visibleToPlayer.contains( current ) ){
							imageMap[x][y] = Game.getImageManager().goal;
						}else if ( current.seenByPlayer() ){
							imageMap[x][y] = Game.getImageManager().goalShadow;
						}else{
							imageMap[x][y] = null;
						}
					}
				}else{
					imageMap[x][y] = null;
				}
			}
		}
	}

	public void updateVisibleToPlayer (){

		/*
		 * New strategy: Just clear visibleToPlayer and set all enemieseach time
		 * this method is called.
		 */
		visibleToPlayer.clear();
		for ( Enemy enemy : GameplayState.getEnemyGroup().getEnemies() ){
			enemy.setVisibleToPlayer( false );
		}

		// Check line of sight to nodes currently in displayedNodes[][]

		// Loop through displayed nodes
		for ( int x = 0 ; x < Game.W_COLS ; x++ ){
			for ( int y = 0 ; y < Game.W_ROWS ; y++ ){
				Node displayedNode = displayedNodes[x][y];
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
						if ( !visibleToPlayer.contains( displayedNode ) ){
							visibleToPlayer.add( displayedNode );
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

		mapGrid[x][y] = new Node( x , y , this );
		mapGrid[x][y].makeFloor();
	}

	public void createNode ( Node node ){

		if ( mapGrid == null ){
			// System.out.println("mapGrid NULL");
		}else{
			// Nothing
		}
		if ( node == null ){
			// System.out.println("node NULL");
		}else{
			// Nothing
		}
		mapGrid[node.getAxisX()][node.getAxisY()] = node;
	}

	public void setDisplayedNodesMinX (){

		/*
		 * Return x value of origin node in displayedNodes. Since
		 * displayedNodes[0][0] could be null, we step through x'es until we
		 * find a non-null node, get its x value, then subtract the number of x
		 * steps it took to find it.
		 */
		int x = 0 , y = 0;
		boolean nonNullFound = false;
		Node firstNonNull = displayedNodes[0][0];
		/* If origin node is null */
		if ( firstNonNull == null ){
			for ( x = 0 ; x < Game.W_COLS ; x++ ){
				for ( y = 0 ; y < Game.W_ROWS ; y++ ){
					if ( displayedNodes[x][y] != null ){

						displayedNodesMinX = displayedNodes[x][y].getAxisX() - x;
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
			displayedNodesMinX = displayedNodes[0][0].getAxisX();
		}
	}

	public void setDisplayedNodesMinY (){

		/*
		 * Return y value of origin node in displayedNodes. Since
		 * displayedNodes[0][0] could be null, we step through y's until we find
		 * a non-null node, get its y value, then subtract the number of y steps
		 * it took to find it.
		 */
		int x = 0 , y = 0;
		boolean nonNullFound = false;
		Node firstNonNull = displayedNodes[0][0];
		/* If origin node is null */
		if ( firstNonNull == null ){
			for ( x = 0 ; x < Game.W_COLS ; x++ ){
				for ( y = 0 ; y < Game.W_ROWS ; y++ ){
					if ( displayedNodes[x][y] != null ){
						displayedNodesMinY = displayedNodes[x][y].getAxisY() - y;
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
			displayedNodesMinY = displayedNodes[0][0].getAxisY();
		}
	}

	public void setDisplayedNodesMaxX (){

		displayedNodesMaxX = displayedNodesMinX + Game.W_COLS;
	}

	public void setDisplayedNodesMaxY (){

		displayedNodesMaxY = displayedNodesMinY + Game.W_ROWS;
	}

	public int getNonNullNodeCount (){

		int count = 0;
		for ( int x = 0 ; x < Game.COLUMNS ; x++ ){
			for ( int y = 0 ; y < Game.ROWS ; y++ ){
				if ( mapGrid[x][y] != null ){
					count++;
				}else{
					// Nothing
				}
			}
		}
		return count;
	}

	public Room[][] getRooms (){

		return rooms;
	}

	public boolean inRoom ( Node node ){

		Point loc = new Point( node.getAxisX() , node.getAxisY() );
		for ( int x = 0 ; x < rooms.length ; x++ ){
			for ( int y = 0 ; y < rooms[0].length ; y++ ){
				if ( rooms[x][y].getBoundary().contains( loc ) ){
					return true;
				}else{
					// Nothing
				}
			}
		}
		return false;
	}

	public Room getNearestRoom ( Node loc ){

		int roomColumn = Game.COLUMNS / MAX_ROOM_WIDTH - 1;
		int roomRow = Game.ROWS / MAX_ROOM_HEIGHT - 1;
		Room nearest = rooms[roomColumn][roomRow];
		return nearest;
	}
}
