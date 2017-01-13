package Main;

import java.util.ArrayList;

import Objects.Enemy;

public class EnemyController {
	public static ArrayList<Enemy> enemies = new ArrayList<Enemy>();
	private ArrayList<Enemy> deadEnemies = new ArrayList<Enemy>();

	private ArrayList<Spawner> spawners = new ArrayList<Spawner>();

	public EnemyController() {

	}

	public void addSpawnList(ArrayList<Spawner> list) {
		spawners = list;
	}
}
