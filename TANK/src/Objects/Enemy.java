package Objects;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.util.ArrayList;

import Blocks.Block;
import Blocks.NavTile;
import Blocks.Spawner;
import Main.Game;
import Main.Map;
import Main.TransformUtils;

public class Enemy extends GameObject implements Runnable, IDamagable {

	private int veloX = 0;
	private int veloY = 0;
	private int curDirection;
	private int preDirection;
	private int curHp;
	private int bulletCount = 10;
	private int navCounter = 0;

	private final int maxHp = 12;
	private final int UP = 1;
	private final int DOWN = 3;
	private final int LEFT = 2;
	private final int RIGHT = 0;

	private long startTime;
	private long curTime;
	private long waitTime;
	private long shootTime;

	private boolean isPathing = true;
	private boolean isPathingStart = true;
	private boolean isPathDone = true;
	private boolean isRunning = true;

	private ArrayList<NavTile> closedList = new ArrayList<NavTile>();;
	private ArrayList<NavTile> openList = new ArrayList<NavTile>();
	private ArrayList<NavTile> checkList = new ArrayList<NavTile>();
	private ArrayList<NavTile> navList = new ArrayList<NavTile>();
	private ArrayList<Bullet> bulletList = new ArrayList<Bullet>();

	private Spawner spawner;

	private Thread thread;

	public Enemy(int posX, int posY) {
		super(posX, posY, "Enemy", "Images//Enemy01.png");

		curTime = System.currentTimeMillis();

		for (int i = 0; i < bulletCount; i++) {
			bulletList.add(new Bullet(-1, -1));
		}

		curHp = maxHp;

		curDirection = UP;
		preDirection = RIGHT;
		this.setImage(TransformUtils.rotate(this.getImage(), curDirection, preDirection));

		if (thread == null) {
			thread = new Thread(this);
			thread.start();
		}
	}

	public Enemy(Spawner spawner) {
		super(spawner.getX(), spawner.getY(), "Enemy", "Images//Enemy01.png");

		curTime = System.currentTimeMillis();

		this.spawner = spawner;

		for (int i = 0; i < bulletCount; i++) {
			bulletList.add(new Bullet(-1, -1));
		}

		curHp = maxHp;

		curDirection = UP;
		preDirection = RIGHT;
		this.setImage(TransformUtils.rotate(this.getImage(), curDirection, preDirection));

		if (thread == null) {
			thread = new Thread(this);
			thread.start();
		}

	}

	@Override
	public void draw(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2d.drawImage(this.getImage(), this.getX(), this.getY(), null);

		if (!bulletList.isEmpty()) {
			for (Bullet b : bulletList) {
				if (!b.isHidden()) {
					b.draw(g);
				}
			}
		}
	}

	@Override
	public void run() {
		while (isRunning) {
			startTime = System.currentTimeMillis();
			Toolkit.getDefaultToolkit().sync();

			if (!bulletList.isEmpty()) {
				for (Bullet b : bulletList) {
					if (!b.isMaxDistReached()) {
						if (!b.isCollision()) {
							b.move();
						} else {
							b.resetMe();
						}
					} else {
						b.resetMe();
					}
				}
			}

			curTime = System.currentTimeMillis() - startTime;
			waitTime = (1000 / Main.Settings.framesPerSecond) - curTime;
			try {
				if (waitTime < 0) {
					Thread.sleep(10);
				} else {
					Thread.sleep(waitTime);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	private void shoot() {
		int posX = 0, posY = 0;
		if (curDirection == RIGHT) {
			posX = this.getX() + this.getWidth();
			posY = this.getY() + (this.getHeight() / 2);

			bulletList.get(bulletCount - 1).setIsHidden(false);
			bulletList.get(bulletCount - 1).setX(posX);
			bulletList.get(bulletCount - 1).setY(posY);
			bulletList.get(bulletCount - 1).setDirection(curDirection);
		} else if (curDirection == UP) {
			posX = this.getX() + (this.getWidth() / 2);
			posY = this.getY() - 2;

			bulletList.get(bulletCount - 1).setIsHidden(false);
			bulletList.get(bulletCount - 1).setX(posX);
			bulletList.get(bulletCount - 1).setY(posY);
			bulletList.get(bulletCount - 1).setDirection(curDirection);
		} else if (curDirection == LEFT) {
			posX = this.getX() - 2;
			posY = this.getY() + (this.getHeight() / 2);

			bulletList.get(bulletCount - 1).setIsHidden(false);
			bulletList.get(bulletCount - 1).setX(posX);
			bulletList.get(bulletCount - 1).setY(posY);
			bulletList.get(bulletCount - 1).setDirection(curDirection);
		} else if (curDirection == DOWN) {
			posX = this.getX() + (this.getWidth() / 2);
			posY = this.getY() + this.getHeight();

			bulletList.get(bulletCount - 1).setIsHidden(false);
			bulletList.get(bulletCount - 1).setX(posX);
			bulletList.get(bulletCount - 1).setY(posY);
			bulletList.get(bulletCount - 1).setDirection(curDirection);
		}

		if (bulletCount > 1)
			bulletCount--;
		else
			bulletCount = 10;
	}

	public void control() {

		if (System.currentTimeMillis() - shootTime > 1250) {
			shootTime = System.currentTimeMillis();
			shoot();
		}

		if (!navList.isEmpty() && navCounter > 0) {
			// System.out.println("Moving");
			veloX = 0;
			veloY = 0;
			preDirection = curDirection;

			// System.out.println(navCounter);
			if (navList.get(navCounter - 1).getX() < this.getX()) {
				curDirection = LEFT;
				veloX = -1;

				this.setImage(TransformUtils.rotate(this.getImage(), curDirection, preDirection));
			} else if (navList.get(navCounter - 1).getX() > this.getX()) {
				curDirection = RIGHT;
				veloX = 1;

				this.setImage(TransformUtils.rotate(this.getImage(), curDirection, preDirection));
			} else if (navList.get(navCounter - 1).getY() < this.getY()) {
				curDirection = UP;
				veloY = -1;

				this.setImage(TransformUtils.rotate(this.getImage(), curDirection, preDirection));
			} else if (navList.get(navCounter - 1).getY() > this.getY()) {
				curDirection = DOWN;
				veloY = 1;

				this.setImage(TransformUtils.rotate(this.getImage(), curDirection, preDirection));
			} else if (navList.get(navCounter - 1).getX() == this.getX()
					&& navList.get(navCounter - 1).getY() == this.getY()) {
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

	public void pathing() {
		Map mainMap = Game.map;
		NavTile enemyPos = mainMap.navMap()[this.getPositionOnMap().getY()][this.getPositionOnMap().getX()];
		enemyPos.setImage("Images\\Nav01.png");
		NavTile tankPos = mainMap.navMap()[Game.tank.getPositionOnMap().getY()][Game.tank.getPositionOnMap().getX()];
		tankPos.setImage("Images\\Nav01.png");
		NavTile birdPos = mainMap.navMap()[Game.bird.getPositionOnMap().getY()][Game.bird.getPositionOnMap().getX()];
		birdPos.setImage("Images\\Nav01.png");
		NavTile targetPos;

		float tankValue = ((Math.abs((enemyPos.getTileX()) - tankPos.getTileX()))
				+ Math.abs(enemyPos.getTileY() - tankPos.getTileY())) + 10;
		float birdValue = ((Math.abs((enemyPos.getTileX()) - birdPos.getTileX()))
				+ Math.abs(enemyPos.getTileY() - birdPos.getTileY())) + 10;
		// System.out.println("Tank: " + tankValue + " Bird: " + birdValue);

		// enemyPos.setValue(tankValue);

		if (tankValue > birdValue) {
			targetPos = birdPos;
			enemyPos.setValue(birdValue);
		} else if (birdValue > tankValue) {
			targetPos = tankPos;
			enemyPos.setValue(tankValue);
		} else {
			targetPos = birdPos;
			enemyPos.setValue(birdValue);
		}

		if (isPathingStart) {

			// System.out.println("Tank: " + tankValue);
			// targetPos = tankPos;

			// System.out.println(targetPos.getTileX());
			// System.out.println(enemyPos);

			for (NavTile b : navList) {
				b.reset();
			}
			navList.clear();

			closedList.add(targetPos); // important: set iterators to 1 in closed list loops
			closedList.add(enemyPos);

			isPathing = true;
			isPathingStart = false;
		}

		if (isPathing) {
			NavTile tempb = null;

			for (int i = 1; i < closedList.size(); i++) {
				// int i = closedList.size() - 1;
				tempb = mainMap.navMap()[closedList.get(i).getTileY() + 1][closedList.get(i).getTileX()];
				valueTile(tempb, targetPos);

				tempb = mainMap.navMap()[closedList.get(i).getTileY() - 1][closedList.get(i).getTileX()];
				valueTile(tempb, targetPos);

				tempb = mainMap.navMap()[closedList.get(i).getTileY()][closedList.get(i).getTileX() + 1];
				valueTile(tempb, targetPos);

				tempb = mainMap.navMap()[closedList.get(i).getTileY()][closedList.get(i).getTileX() - 1];
				valueTile(tempb, targetPos);
			}

			// System.out.println(openList.get(0));

			if (!openList.isEmpty()) {
				// System.out.println("CALCULATE BLOCK WIEGHT: " + openList.size());
				NavTile tempBlock = openList.get(0);
				// System.out.println("----------------------------------");
				// for (NavTile b : closedList) {
				// System.out.println(b);
				// }

				for (NavTile b : openList) {
					// System.out.println(b);
					if (b.getValue() <= tempBlock.getValue()) {
						tempBlock = b;
					}
				}

				tempBlock.setImage("Images//Nav01.png");

				closedList.add(tempBlock);

				openList.clear();

				if (tempBlock.getTileX() + 1 == targetPos.getTileX() && tempBlock.getTileY() == targetPos.getTileY()) {
					isPathing = false;
					isPathDone = false;
					// System.out.println("PATHING DONE " + targetPos);
				} else if (tempBlock.getTileX() - 1 == targetPos.getTileX()
						&& tempBlock.getTileY() == targetPos.getTileY()) {
					isPathing = false;
					isPathDone = false;
					// System.out.println("PATHING DONE " + targetPos);
				} else if (tempBlock.getTileY() + 1 == targetPos.getTileY()
						&& tempBlock.getTileX() == targetPos.getTileX()) {
					isPathing = false;
					isPathDone = false;
					// System.out.println("PATHING DONE " + targetPos);
				} else if (tempBlock.getTileY() - 1 == targetPos.getTileY()
						&& tempBlock.getTileX() == targetPos.getTileX()) {
					isPathing = false;
					isPathDone = false;
					// System.out.println("PATHING DONE " + targetPos);
				}
			}
		}
		if (!isPathDone) {
			// System.out.println("asfasg");

			navList.add(closedList.get(closedList.size() - 1));

			NavTile tester = closedList.get(closedList.size() - 1);
			for (int i = closedList.size() - 2; i > 0; i--) {
				if (testIfNeighbor(tester, closedList.get(i))) {
					// System.out.println(tester);
					navList.add(tester);
					tester = closedList.get(i);
				}
			}

			isPathDone = true;
			navCounter = navList.size();

			// System.out.println("NAV LIST DONE");
			for (NavTile n : navList) {
				n.setImage("Images//Nav03.png");
				// System.out.println(n);
			}
		}

		boolean reset = false;
		if (targetPos.getTileX() != closedList.get(0).getTileX()
				|| targetPos.getTileY() != closedList.get(0).getTileY()) {
			reset = true;
		}

		if (reset) {
			System.out.println("Reset");
			for (NavTile b : closedList) {
				b.reset();
			}
			for (NavTile b : openList) {
				b.reset();
			}
			for (NavTile b : navList) {
				b.reset();
			}

			closedList.clear();
			openList.clear();
			navList.clear();

			isPathingStart = true;
			isPathing = false;
			isPathDone = true;
		}
	}

	private boolean testIfNeighbor(NavTile current, NavTile next) {
		int downY = current.getTileY() + 1;
		int downX = current.getTileX();

		int upY = current.getTileY() - 1;
		int upX = current.getTileX();

		int rightY = current.getTileY();
		int rightX = current.getTileX() + 1;

		int leftY = current.getTileY();
		int leftX = current.getTileX() - 1;

		if (downY == next.getTileY() & downX == next.getTileX()) {
			return true;
		} else if (upY == next.getTileY() & upX == next.getTileX()) {
			return true;
		} else if (rightY == next.getTileY() & rightX == next.getTileX()) {
			return true;
		} else if (leftY == next.getTileY() & leftX == next.getTileX()) {
			return true;
		} else {
			return false;
		}
	}

	private void valueTile(NavTile testable, NavTile traget) {
		if (!closedList.contains(testable)) {
			if (!testable.isBlocking()) {
				float value = ((Math.abs((testable.getTileX()) - traget.getTileX()))
						+ Math.abs(testable.getTileY() - traget.getTileY())) + 10;

				testable.setValue(value);
				testable.setImage("Images//Nav02.png");

				openList.add(testable);
			}
		}
	}

	private void valueNeighborTile(NavTile current, NavTile neighbor) {
		if (!closedList.contains(neighbor)) {
			if (!neighbor.isBlocking()) {
				float value = ((Math.abs((current.getTileX()) - neighbor.getTileX()))
						+ Math.abs(current.getTileY() - neighbor.getTileY()))
						+ ((Math.abs((current.getTileX()) - closedList.get(1).getTileX()))
								+ Math.abs(current.getTileY() - closedList.get(1).getTileY()))
						+ 10;

				neighbor.setValue(value);
				// testable.setImage("Images//Nav02.png");
				checkList.add(neighbor);
			}
		}
	}

	public void collisionCheck() {
		for (Block b : Game.map.getBlockList()) {
			if (this.getBounds().intersects(b.getBounds()) && b.isSolid()) {
				Rectangle insect = this.getBounds().intersection(b.getBounds());

				boolean vertical = false;
				boolean horizontal = false;
				boolean isLeft = false;
				boolean isTop = false;

				if (insect.getX() == this.getX()) {
					horizontal = true;
					isLeft = true;
				} else if (insect.getX() + insect.getWidth() == this.getX() + this.getWidth()) {
					horizontal = true;
				}
				if (insect.getY() == this.getY()) {
					vertical = true;
					isTop = true;
				} else if (insect.getY() + insect.getHeight() == this.getY() + this.getHeight()) {
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
						this.setX(b.getX() + b.getWidth());
					} else {
						this.setX(b.getX() - this.getWidth());
					}
				} else if (vertical) {
					if (isTop) {
						this.setY(b.getY() + b.getHeight());
					} else {
						this.setY(b.getY() - this.getHeight());
					}
				}
			}
		}

		for (Enemy e : Game.enemies) {
			if (!this.equals(e) && this.getBounds().intersects(e.getBounds())) {
				Rectangle insect = this.getBounds().intersection(e.getBounds());

				boolean vertical = false;
				boolean horizontal = false;
				boolean isLeft = false;
				boolean isTop = false;

				if (insect.getX() == this.getX()) {
					horizontal = true;
					isLeft = true;
				} else if (insect.getX() + insect.getWidth() == this.getX() + this.getWidth()) {
					horizontal = true;
				}
				if (insect.getY() == this.getY()) {
					vertical = true;
					isTop = true;
				} else if (insect.getY() + insect.getHeight() == this.getY() + this.getHeight()) {
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
						this.setX(e.getX() + e.getWidth());
					} else {
						this.setX(e.getX() - this.getWidth());
					}
				} else if (vertical) {
					if (isTop) {
						this.setY(e.getY() + e.getHeight());
					} else {
						this.setY(e.getY() - this.getHeight());
					}
				}
			}
		}

		if (this.getBounds().intersects(Game.tank.getBounds())) {
			Rectangle insect = this.getBounds().intersection(Game.tank.getBounds());

			boolean vertical = false;
			boolean horizontal = false;
			boolean isLeft = false;
			boolean isTop = false;

			if (insect.getX() == this.getX()) {
				horizontal = true;
				isLeft = true;
			} else if (insect.getX() + insect.getWidth() == this.getX() + this.getWidth()) {
				horizontal = true;
			}
			if (insect.getY() == this.getY()) {
				vertical = true;
				isTop = true;
			} else if (insect.getY() + insect.getHeight() == this.getY() + this.getHeight()) {
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
					this.setX(Game.tank.getX() + Game.tank.getWidth());
				} else {
					this.setX(Game.tank.getX() - this.getWidth());
				}
			} else if (vertical) {
				if (isTop) {
					this.setY(Game.tank.getY() + Game.tank.getHeight());
				} else {
					this.setY(Game.tank.getY() - this.getHeight());
				}
			}
		}

		if (this.getBounds().intersects(Game.bird.getBounds())) {
			Rectangle insect = this.getBounds().intersection(Game.bird.getBounds());

			boolean vertical = false;
			boolean horizontal = false;
			boolean isLeft = false;
			boolean isTop = false;

			if (insect.getX() == this.getX()) {
				horizontal = true;
				isLeft = true;
			} else if (insect.getX() + insect.getWidth() == this.getX() + this.getWidth()) {
				horizontal = true;
			}
			if (insect.getY() == this.getY()) {
				vertical = true;
				isTop = true;
			} else if (insect.getY() + insect.getHeight() == this.getY() + this.getHeight()) {
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
					this.setX(Game.bird.getX() + Game.bird.getWidth());
				} else {
					this.setX(Game.bird.getX() - this.getWidth());
				}
			} else if (vertical) {
				if (isTop) {
					this.setY(Game.bird.getY() + Game.bird.getHeight());
				} else {
					this.setY(Game.bird.getY() - this.getHeight());
				}
			}
		}
	}

	public void recieveDamage2(int damage) {
		curHp -= damage;
	}

	@Override
	public void recieveDamage(int damage, int dir) {
		curHp -= damage;
		// System.out.println("HP " + curHp);
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
		spawner.setEnemyCount(spawner.getEnemyCount() - 1);

		isRunning = false;
		try {
			thread.join();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		closedList.clear();
		openList.clear();
		for (NavTile b : navList) {
			b.reset();
		}
		navList.clear();
	}

}