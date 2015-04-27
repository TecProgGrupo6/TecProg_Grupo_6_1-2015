package trl.entity.enemy;

import trl.main.Game;
import trl.map.Map;

public class Demon extends Enemy{

	public Demon ( Map map ){

		super( map );
		init();
	}

	// Initiliaze demon
	public void init (){

		maxHP = 15;
		attack = 10;
		image = Game.getImageManager().demon;
		hp = maxHP;
		level = 1;
		xpReward = 1;
	}
}
