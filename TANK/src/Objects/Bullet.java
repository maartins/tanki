package Objects;

import Main.Game;

public class Bullet extends GameObject {

	private int x; // Bullets current x position
	private int y; // Bullets current y position
	private int direction; // Bullets moving direction
	private int damage; // Bullets damage

	private final int SPEED = 5; // Bullets speed
	private final int MAXDIST = 500; // Bullets maximum travel distance

	private boolean isMaxDistReached = false; // Check if the maximum distance
	private boolean isCollision = false; // Check if bullet collides

	public Bullet(int posX, int posY) {
		super(posX, posY, "Bullet", "Images//Bullet01.png");
		x = posX;
		y = posY;

		direction = -1;

		damage = 1;
	}

	public Bullet(int posX, int posY, int curDir) {
		super(posX, posY, "Bullet", "Images//Bullet01.png");
		x = posX;
		y = posY;

		direction = curDir;

		damage = 1;
	}

	public Bullet(int posX, int posY, int curDir, String name) {
		super(posX, posY, name, "Images//Bullet01.png");
		x = posX;
		y = posY;

		direction = curDir;

		damage = 1;
	}

	public Bullet(int posX, int posY, int curDir, int damage, String name, String imagePath) {
		super(posX, posY, name, imagePath);
		x = posX;
		y = posY;

		direction = curDir;

		this.damage = damage;
	}

	public void move() {
		if (!isHidden()) {
			switch (direction) {
			case 0:
				this.setX(this.getX() + SPEED);
				if (this.getX() - x >= MAXDIST) {
					isMaxDistReached = true;
				}
				break;
			case 1:
				this.setY(this.getY() - SPEED);
				if (this.getY() - y <= -MAXDIST) {
					isMaxDistReached = true;
				}
				break;
			case 2:
				this.setX(this.getX() - SPEED);
				if (this.getX() - x <= -MAXDIST) {
					isMaxDistReached = true;
				}
				break;
			case 3:
				this.setY(this.getY() + SPEED);
				if (this.getY() - y >= MAXDIST) {
					isMaxDistReached = true;
				}
				break;
			default:
				break;
			}

			for (IDamagable damagable : Game.damagableObjects) {
				if (this.getBounds().intersects(((GameObject) damagable).getBounds())) {
					isCollision = true;
					damagable.recieveDamage(3, direction);
				}
			}
		}
	}

	public void setDirection(int value) {
		direction = value;
	}

	public boolean isMaxDistReached() {
		return isMaxDistReached;
	}

	public boolean isCollision() {
		return isCollision;
	}

	public void resetMe() {
		isCollision = false;
		isMaxDistReached = false;
		setIsHidden(true);
		setX(-1);
		setY(-1);
	}

	@Override
	public String toString() {
		return getName() + ": " + getX() + ", " + getY() + " h: " + isHidden();
	}

}
