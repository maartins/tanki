
public class Spawner extends Block{
	
	private int enemyCount;

	private boolean canSpawn;
	
	public Spawner(int posX, int posY){
		super(posX, posY, true, false, "Spawner");
		
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
		return canSpawn;
	}

	public void setCanSpawn(boolean canSpawn) {
		enemyCount--;
		this.canSpawn = canSpawn;
	}
	
	public void spawn(){
		if(enemyCount > 0 && canSpawn){
			MainPanel.enemies.add(new Enemy(this.getX(), this.getY()));
			MainPanel.enemies.get(MainPanel.enemies.size() - 1).setSpawner(this);
			canSpawn = false;
		}
	}
}
