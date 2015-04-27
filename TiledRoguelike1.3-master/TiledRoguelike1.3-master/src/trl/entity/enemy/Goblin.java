package trl.entity.enemy;

import trl.main.Game;
import trl.map.Map;

public class Goblin extends Enemy{

	public Goblin ( Map map ){

		super( map );
		init();
	}

	// Initiliaze Goblin
	public void init (){

		maxHP = 16;
		attack = 5;
		image = Game.getImageManager().goblin;
		hp = maxHP;
		xpReward = 8;
		level = 8;
	}
}
