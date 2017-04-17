package Objects;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import Blocks.Spawner;
import Main.BulletManager;
import Main.EnemyManager;
import Main.Game;
import Main.TransformUtils;
import Pathfinding.AStarPathing;
import Pathfinding.NavTile;

public class Enemy extends GameObject implements IDamagable {

	private int veloX = 0;
	private int veloY = 0;
	private int curDirection;
	private int preDirection;
	private int curHp;
	private int navCounter = 0;

	private final int maxHp = 12;
	private final int UP = 1;
	private final int DOWN = 3;
	private final int LEFT = 2;
	private final int RIGHT = 0;

	private long shootTime;

	private boolean hasPath = false;

	private ArrayList<NavTile> navList = new ArrayList<NavTile>();

	private BulletManager bulletManager = new BulletManager(10, 2);

	private Spawner spawner;

	public Enemy(int posX, int posY) {
		super(posX, posY, "Enemy", "Images//Enemy01.png");

		curHp = maxHp;

		curDirection = UP;
		preDirection = RIGHT;

		setImage(TransformUtils.rotate(getImage(), curDirection, preDirection));

		EnemyManager.enemies.add(this);
	}

	public Enemy(Spawner spawner) {
		super(spawner.getX(), spawner.getY(), "Enemy", "Images//Enemy01.png");

		this.spawner = spawner;

		curHp = maxHp;

		curDirection = UP;
		preDirection = RIGHT;
		setImage(TransformUtils.rotate(getImage(), curDirection, preDirection));

		EnemyManager.enemies.add(this);
	}

	@Override
	public void draw(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2d.drawImage(this.getImage(), this.getX(), this.getY(), null);

		bulletManager.draw(g);
	}

	public void control() {

		if (System.currentTimeMillis() - shootTime > 1250) {
			shootTime = System.currentTimeMillis();
			shoot();
		}

		if (!navList.isEmpty() && navCounter > 0) {
			//System.out.println("Moving");
			veloX = 0;
			veloY = 0;
			preDirection = curDirection;

			// System.out.println(navCounter);
			if (navList.get(navCounter - 1)
						.getX() < getX()) {
				curDirection = LEFT;
				veloX = -1;

				this.setImage(TransformUtils.rotate(getImage(), curDirection, preDirection));
			} else if (navList.get(navCounter - 1)
						.getX() > this.getX()) {
				curDirection = RIGHT;
				veloX = 1;

				this.setImage(TransformUtils.rotate(getImage(), curDirection, preDirection));
			} else if (navList.get(navCounter - 1)
						.getY() < this.getY()) {
				curDirection = UP;
				veloY = -1;

				this.setImage(TransformUtils.rotate(getImage(), curDirection, preDirection));
			} else if (navList.get(navCounter - 1)
						.getY() > this.getY()) {
				curDirection = DOWN;
				veloY = 1;

				this.setImage(TransformUtils.rotate(getImage(), curDirection, preDirection));
			} else if (navList.get(navCounter - 1)
						.getX() == getX()
						&& navList.get(navCounter - 1)
									.getY() == getY()) {
				veloX = 0;
				veloY = 0;

				navCounter--;

				// navList.get(navList.size() - 1).reset();
				// navList.remove(navList.size() - 1);
				// System.out.println("TILE REACHED");
			}
		}

		this.setX(this.getX() + veloX);
		this.setY(this.getY() + veloY);
	}

	private void shoot() {
		int posX = 0, posY = 0;
		if (curDirection == RIGHT) {
			posX = this.getX() + this.getWidth();
			posY = this.getY() + (this.getHeight() / 2);

			bulletManager.createShot(posX, posY, curDirection);
		} else if (curDirection == UP) {
			posX = this.getX() + (this.getWidth() / 2);
			posY = this.getY() - 2;

			bulletManager.createShot(posX, posY, curDirection);
		} else if (curDirection == LEFT) {
			posX = this.getX() - 2;
			posY = this.getY() + (this.getHeight() / 2);

			bulletManager.createShot(posX, posY, curDirection);
		} else if (curDirection == DOWN) {
			posX = this.getX() + (this.getWidth() / 2);
			posY = this.getY() + this.getHeight();

			bulletManager.createShot(posX, posY, curDirection);
		}
	}

	public void pathing() {
		if (!hasPath) {
			ArrayList<NavTile> path = null;

			NavTile enemyPos = getPositionOnMap();
			enemyPos.setImage("Images\\Nav01.png");
			NavTile tankPos = Game.tank.getPositionOnMap();
			tankPos.setImage("Images\\Nav01.png");
			NavTile birdPos = Game.bird.getPositionOnMap();
			birdPos.setImage("Images\\Nav01.png");

			try {
				path = AStarPathing.findPath(enemyPos, tankPos, birdPos)
							.get(3, TimeUnit.SECONDS);
			} catch (InterruptedException | ExecutionException | TimeoutException e) {
				e.printStackTrace();
			} finally {
				if (path != null) {
					navList.clear();
					navList = path;

					navCounter = navList.size();

					hasPath = true;
				} else {
					hasPath = false;
				}
			}
		}
	}

	public void collisionCheck() {
		Game.map.getWorld()
					.filter(obj -> !obj.equals(this))
					.filter(GameObject::isSolid)
					.filter(obj -> getBounds().intersects(obj.getBounds()))
					.forEach(obj -> {
						Rectangle insect = getBounds().intersection(obj.getBounds());

						boolean vertical = false;
						boolean horizontal = false;
						boolean isLeft = false;
						boolean isTop = false;

						if (insect.getX() == getX()) {
							horizontal = true;
							isLeft = true;
						} else if (insect.getX() + insect.getWidth() == getX() + getWidth()) {
							horizontal = true;
						}
						if (insect.getY() == getY()) {
							vertical = true;
							isTop = true;
						} else if (insect.getY() + insect.getHeight() == getY() + getHeight()) {
							vertical = true;
						}

						if (horizontal && vertical) {
							if (insect.getWidth() > insect.getHeight()) {
								horizontal = false;
							} else {
								vertical = false;
							}
						}

						if (horizontal) {
							if (isLeft) {
								setX(obj.getX() + obj.getWidth());
							} else {
								setX(obj.getX() - getWidth());
							}
						} else if (vertical) {
							if (isTop) {
								setY(obj.getY() + obj.getHeight());
							} else {
								setY(obj.getY() - getHeight());
							}
						}
					});
	}

	@Override
	public void recieveDamage(int damage, int dir) {
		curHp -= damage;
		if (isDead()) {
			die();
		}
	}

	@Override
	public boolean isDead() {
		if (curHp <= 0) {
			return true;
		} else {
			return false;
		}
	}

	public void setSpawner(Spawner spawner) {
		this.spawner = spawner;
	}

	public Spawner getSpawner() {
		return spawner;
	}

	public void die() {
		spawner.setCanSpawn(true);

		for (NavTile b : navList) {
			b.reset();
		}
		navList.clear();
	}

}