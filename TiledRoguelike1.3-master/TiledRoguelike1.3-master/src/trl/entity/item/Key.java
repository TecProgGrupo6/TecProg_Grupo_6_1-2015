package trl.entity.item;

import java.util.logging.Level;
import java.util.logging.Logger;

import trl.entity.enemy.Demon;
import trl.main.Game;
import trl.map.Map;

public class Key extends Item{

	public Key ( Map map ){

		super( map );
		init();
	}
	
	// Log system from Key Class
	private final static Logger LOGGER = Logger.getLogger( Key.class.getName() );

	// Initiliaze Key
	public void init (){
		
		LOGGER.setLevel( Level.INFO );
		LOGGER.info("Key intialized");

		this.image = Game.getImageManager().key;
		this.loc = map.placeEntity( this , map.getRandomNodeInRoom() );
	}
}
