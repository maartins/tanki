package Objects;

import Main.Game;

public class Bullet extends GameObject {

	private int x; // Bullets current x position
	private int y; // Bullets current y position
	private int direction = -1; // Bullets moving direction
	private int damage = 1; // Bullets damage

	private final int SPEED = 5; // Bullets speed
	private final int MAXDIST = 500; // Bullets maximum travel distance

	public Bullet(int posX, int posY) {
		super(posX, posY, "Bullet", "Images//Bullet01.png");
		x = posX;
		y = posY;
	}

	public Bullet(int posX, int posY, int curDir) {
		super(posX, posY, "Bullet", "Images//Bullet01.png");
		x = posX;
		y = posY;

		direction = curDir;
	}

	public Bullet(int posX, int posY, int curDir, int damage) {
		super(posX, posY, "Bullet", "Images//Bullet01.png");
		x = posX;
		y = posY;

		direction = curDir;
		this.damage = damage;
	}

	public Bullet(int posX, int posY, int curDir, String name) {
		super(posX, posY, name, "Images//Bullet01.png");
		x = posX;
		y = posY;

		direction = curDir;
	}

	public Bullet(int posX, int posY, int curDir, int damage, String name, String imagePath) {
		super(posX, posY, name, imagePath);
		x = posX;
		y = posY;

		direction = curDir;
		this.damage = damage;
	}

	public synchronized void move() {
		if (!isHidden()) {
			switch (direction) {
			case 0:
				setX(getX() + SPEED);
				if (getX() - x >= MAXDIST) {
					resetMe();
				}
				break;
			case 1:
				setY(getY() - SPEED);
				if (getY() - y <= -MAXDIST) {
					resetMe();
				}
				break;
			case 2:
				setX(getX() - SPEED);
				if (getX() - x <= -MAXDIST) {
					resetMe();
				}
				break;
			case 3:
				setY(getY() + SPEED);
				if (getY() - y >= MAXDIST) {
					resetMe();
				}
				break;
			default:
				break;
			}
			if (!Game.damagableObjects.isEmpty()) {
				Game.damagableObjects.stream()
							.parallel()
							.filter(obj -> !obj.isDead())
							.filter(obj -> ((GameObject) obj).getBounds()
										.intersects(getBounds()))
							.forEach(obj -> {
								obj.recieveDamage(damage, direction);
								resetMe();
							});

				/*
				 * for (IDamagable damagable : Game.damagableObjects) {
				 * if (!damagable.isDead()) {
				 * if (getBounds().intersects(((GameObject) damagable).getBounds())) {
				 * isCollision = true;
				 * resetMe();
				 * damagable.recieveDamage(3, direction);
				 * }
				 * }
				 * }
				 */
			}
		}
	}

	public void setDirection(int value) {
		direction = value;
	}

	public void resetMe() {
		setIsHidden(true);
		setX(-1);
		setY(-1);
	}

	@Override
	public String toString() {
		return getName() + ": " + getX() + ", " + getY() + " h: " + isHidden();
	}

}
