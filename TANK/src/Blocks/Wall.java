package Blocks;

import java.awt.Rectangle;

import Main.TransformUtils;
import Objects.IDamagable;

public class Wall extends Block implements IDamagable {

	private boolean isDead = false;

	public Wall(int posX, int posY) {
		super(posX, posY, true, "Wall", "Images//Wall01.png");
	}

	@Override
	public void recieveDamage(int damageNotNeeded, int dir) {
		int damage = 4;
		if ((getWidth() - damage <= 0 && (dir == 0 || dir == 2))
				|| (getHeight() - damage <= 0 && (dir == 1 || dir == 3))) {
			isDead = true;
		} else {
			if (dir == 0) {
				if (getWidth() - damage <= 0)
					damage = damage + (getWidth() - damage);

				setX(getX() + damage);
				setImage(TransformUtils.crop(getImage(), new Rectangle(getWidth() - damage, getHeight())));
			} else if (dir == 1) {
				if (getHeight() - damage <= 0)
					damage = damage + (getHeight() - damage);

				setImage(TransformUtils.crop(getImage(), new Rectangle(getWidth(), getHeight() - damage)));
			} else if (dir == 2) {
				if (getWidth() - damage <= 0)
					damage = damage + (getWidth() - damage);

				setImage(TransformUtils.crop(getImage(), new Rectangle(getWidth() - damage, getHeight())));
			} else if (dir == 3) {
				if (getHeight() - damage <= 0)
					damage = damage + (getHeight() - damage);

				setY(getY() + damage);
				setImage(TransformUtils.crop(getImage(), new Rectangle(getWidth(), getHeight() - damage)));
			}
		}
	}

	@Override
	public boolean isDead() {
		return isDead;
	}
}
