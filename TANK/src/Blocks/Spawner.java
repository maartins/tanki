package Blocks;

import Objects.Enemy;

public class Spawner extends Block {

	private int enemyCount;

	private boolean canSpawn;

	public Spawner(int posX, int posY) {
		super(posX, posY, false, "Spawner");

		enemyCount = 3;

		canSpawn = true;
	}

	public int getEnemyCount() {
		return enemyCount;
	}

	public void setEnemyCount(int enemyCount) {
		this.enemyCount = enemyCount;
	}

	public boolean canSpawn() {
		if (enemyCount > 0) {
			return canSpawn;
		} else {
			return false;
		}
	}

	public void setCanSpawn(boolean canSpawn) {
		this.canSpawn = canSpawn;
	}

	public boolean isEmpty() {
		if (enemyCount == 0) {
			return true;
		} else {
			return false;
		}
	}

	public Enemy spawn() {
		canSpawn = false;
		return new Enemy(this);
	}
}
