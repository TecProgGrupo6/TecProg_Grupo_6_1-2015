package trl.entity.enemy;

import trl.main.Game;
import trl.map.Map;

public class Zombie extends Enemy{

	public Zombie ( Map map ){

		super( map );
		init();
	}

	// Initiliaze Zombie
	public void init (){

		maxHP = 26;
		attack = 5;
		image = Game.getImageManager().zombie;
		hp = maxHP;
		xpReward = 13;
		level = 13;
	}
}
