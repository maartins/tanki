package Objects;

import Blocks.Block;
import Main.IDamagable;

public class IronBird extends GameObject implements IDamagable {

	private int curHp;
	private int maxHp;

	public IronBird(int posX, int posY) {
		super(posX, posY, "Iron Bird", "Images//Bird01.png");

		maxHp = 50;
		curHp = maxHp;
	}

	public IronBird(Block pos) {
		super(pos.getX(), pos.getY(), "Iron Bird", "Images//Bird01.png");

		maxHp = 50;
		curHp = maxHp;
	}

	public int getCurHP() {
		return curHp;
	}

	public void recieveDamage2(int damage) {
		curHp -= damage;
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
	public void recieveDamage(int damage, int dir) {
		curHp -= damage;
	}
}
