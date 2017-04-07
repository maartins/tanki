package Main;

import java.awt.Graphics;
import java.awt.Toolkit;
import java.util.ArrayList;

import Objects.Bullet;

public class BulletManager implements Runnable {
	private int superBulletCount = 3;
	private int bulletCount = 10;
	private int bulletDamage;

	private ArrayList<Bullet> bulletList = new ArrayList<>();

	private long startTime;
	private long curTime;
	private long waitTime;

	private boolean isRunning = true;

	private Thread mainThread = new Thread(this);

	public BulletManager() {
		for (int i = 0; i < bulletCount; i++) {
			bulletList.add(new Bullet(-1, -1));
		}

		mainThread.start();
	}

	public BulletManager(int count, int damage) {
		bulletCount = count;

		bulletDamage = damage;

		for (int i = 0; i < bulletCount; i++) {
			bulletList.add(new Bullet(-1, -1, -1, bulletDamage));
		}

		mainThread.start();
	}

	public void draw(Graphics g) {
		if (!bulletList.isEmpty()) {
			for (Bullet b : bulletList) {
				if (!b.isHidden()) {
					b.draw(g);
				}
			}
		}
	}

	public void createShot(int posX, int posY, int curDirection) {
		Bullet temp = bulletList.get(bulletCount - 1);

		temp.setIsHidden(false);
		temp.setX(posX);
		temp.setY(posY);
		temp.setDirection(curDirection);

		if (bulletCount > 1) {
			bulletCount--;
		} else {
			bulletCount = 10;
		}
	}

	@Override
	public void run() {
		while (isRunning) {
			startTime = System.currentTimeMillis();
			Toolkit.getDefaultToolkit()
						.sync();

			if (!bulletList.isEmpty()) {
				bulletList.stream()
							.filter(bullet -> !bullet.isHidden())
							.forEach(bullet -> bullet.move());

				/*
				 * Iterator<Bullet> iterator = bulletList.iterator();
				 * while (iterator.hasNext()) {
				 * Bullet b = iterator.next();
				 * 
				 * if (!b.isMaxDistReached()) {
				 * if (!b.isCollision()) {
				 * b.move();
				 * } else {
				 * b.resetMe();
				 * }
				 * } else {
				 * b.resetMe();
				 * }
				 * }
				 */
			}

			curTime = System.currentTimeMillis() - startTime;
			waitTime = (1000 / Settings.framesPerSecond) - curTime;
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
}
