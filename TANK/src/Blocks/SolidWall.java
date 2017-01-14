package Blocks;

import Objects.IDamagable;

public class SolidWall extends Block implements IDamagable {

	public SolidWall(int posX, int posY) {
		super(posX, posY, true, "SolidWall", "Images//Wall02.png");
	}

	@Override
	public void recieveDamage(int damage, int dir) {
	}

	@Override
	public boolean isDead() {
		return false;
	}
}
