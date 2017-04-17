package Objects;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import Blocks.Block;
import GameStates.GameStateManager;
import GameStates.GameStates;
import Main.BulletManager;
import Main.Game;
import Main.Sound;
import Main.TransformUtils;

public class Tank extends GameObject implements KeyListener, IDamagable {

	private int veloX = 0;
	private int veloY = 0;
	private int curDirection;
	private int preDirection;
	private int curHp;
	private int score = 0;

	private final int maxHp = 50;
	private final int UP = 1;
	private final int DOWN = 3;
	private final int LEFT = 2;
	private final int RIGHT = 0;

	private long shootTime;

	private boolean keyA = false;
	private boolean keyD = false;
	private boolean keyW = false;
	private boolean keyS = false;
	private boolean keySPACE = false;

	private BulletManager bulletManager = new BulletManager(10, 3);

	private Sound shootSound = new Sound("Sounds//tank_shoot01.wav");
	@SuppressWarnings("unused")
	private Sound moveSound = new Sound("Sounds//tank_move01.wav");

	public Tank(Block posBlock) {
		super(posBlock.getX(), posBlock.getY(), "Tank", "Images//Tank01.png");

		curHp = maxHp;

		curDirection = UP;
		preDirection = RIGHT;
	}

	public Tank(int posX, int posY) {
		super(posX, posY, "Tank", "Images//Test02.png");

		curHp = maxHp;

		curDirection = UP;
		preDirection = RIGHT;
	}

	public int getCurHp() {
		return curHp;
	}

	public void setCurHp(int curHp) {
		this.curHp = curHp;
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}

	public int getMaxHp() {
		return maxHp;
	}

	public void setLocation(Block posBlock) {
		setX(posBlock.getX());
		setY(posBlock.getY());
	}

	@Override
	public void draw(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2d.drawImage(getImage(), getX(), getY(), null);

		bulletManager.draw(g);
	}

	public void control() {
		if (keyA == keyD) {
			veloX = 0;
		} else if (keyA) {
			veloX = -1;
			// moveSound.play();
		} else if (keyD) {
			veloX = 1;
			// moveSound.play();
		}

		if (keyW == keyS) {
			veloY = 0;
		} else if (keyW) {
			veloY = -1;
			// moveSound.play();
		} else if (keyS) {
			veloY = 1;
			// moveSound.play();
		}

		if (keyA && keyS || keyA && keyW) {
			veloX = 0;
			veloY = 0;
		} else if (keyD && keyS || keyD && keyW) {
			veloX = 0;
			veloY = 0;
		}

		if (keySPACE) {
			if (System.currentTimeMillis() - shootTime > 250) {
				shootTime = System.currentTimeMillis();
				shootSound.play();

				shoot();
			}
		}

		setX(getX() + veloX);
		setY(getY() + veloY);
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

	private void shoot() {
		int posX = 0, posY = 0;
		if (curDirection == RIGHT) {
			posX = getX() + getWidth();
			posY = getY() + (getHeight() / 2);

			bulletManager.createShot(posX, posY, curDirection);
		} else if (curDirection == UP) {
			posX = getX() + (getWidth() / 2);
			posY = getY() - 2;

			bulletManager.createShot(posX, posY, curDirection);
		} else if (curDirection == LEFT) {
			posX = getX() - 2;
			posY = getY() + (getHeight() / 2);

			bulletManager.createShot(posX, posY, curDirection);
		} else if (curDirection == DOWN) {
			posX = getX() + (getWidth() / 2);
			posY = getY() + getHeight();

			bulletManager.createShot(posX, posY, curDirection);
		}
	}

	@Override
	public void keyPressed(KeyEvent e) {
		int keyCode = e.getKeyCode();
		switch (keyCode) {
		case KeyEvent.VK_A:
			keyA = true;// System.out.println(KeyEvent.VK_A + " " + keyA);
			if (curDirection != LEFT) {
				preDirection = curDirection;
				curDirection = LEFT;
				this.setImage(TransformUtils.rotate(this.getImage(), curDirection, preDirection));
			}
			break;
		case KeyEvent.VK_D:
			keyD = true;// System.out.println(KeyEvent.VK_D + " " + keyD);
			if (curDirection != RIGHT) {
				preDirection = curDirection;
				curDirection = RIGHT;
				this.setImage(TransformUtils.rotate(this.getImage(), curDirection, preDirection));
			}
			break;
		case KeyEvent.VK_W:
			keyW = true;// System.out.println(KeyEvent.VK_W + " " + keyW);
			if (curDirection != UP) {
				preDirection = curDirection;
				curDirection = UP;
				this.setImage(TransformUtils.rotate(this.getImage(), curDirection, preDirection));
			}
			break;
		case KeyEvent.VK_S:
			keyS = true;// System.out.println(KeyEvent.VK_S + " " + keyS);
			if (curDirection != DOWN) {
				preDirection = curDirection;
				curDirection = DOWN;
				this.setImage(TransformUtils.rotate(this.getImage(), curDirection, preDirection));
			}
			break;
		case KeyEvent.VK_SPACE:
			keySPACE = true;
			break;
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		int keyCode = e.getKeyCode();
		switch (keyCode) {
		case KeyEvent.VK_A:
			keyA = false;// System.out.println(KeyEvent.VK_A + " " + keyA);
			break;
		case KeyEvent.VK_D:
			keyD = false;// System.out.println(KeyEvent.VK_D + " " + keyD);
			break;
		case KeyEvent.VK_W:
			keyW = false;// System.out.println(KeyEvent.VK_W + " " + keyW);
			break;
		case KeyEvent.VK_S:
			keyS = false;// System.out.println(KeyEvent.VK_S + " " + keyS);
			break;
		case KeyEvent.VK_SPACE:
			keySPACE = false;
			break;
		}
	}

	@Override
	public void keyTyped(KeyEvent e) {
	}

	@Override
	public void recieveDamage(int damage, int dir) {
		curHp -= damage;

		if (isDead()) {
			GameStateManager.setState(GameStates.EndScreen);
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

	@Override
	public String toString() {
		return getName() + " - x:" + getX() + " y:" + getY() + " veloX:" + veloX + " veloY:" + veloY;
	}
}
