package Pathfinding;

import Blocks.Block;

public class NavTile extends Block {

	private float value;

	private boolean isBlocking;

	private NavTile parent;

	public NavTile(int x, int y, boolean isBlocking) {
		super(x, y, isBlocking, "navtile");

		value = 0;

		this.isBlocking = isBlocking;

		parent = null;
	}

	public float getValue() {
		return value;
	}

	public void setValue(float value) {
		this.value = value;
	}

	public boolean isBlocking() {
		return isBlocking;
	}

	public void setBlocking(boolean isBlocking) {
		this.isBlocking = isBlocking;
	}

	public NavTile getParent() {
		return parent;
	}

	public void setParent(NavTile parent) {
		this.parent = parent;
	}

	public void reset() {
		value = 0;
	}

	@Override
	public String toString() {
		return "isBlocking:" + isBlocking + " navtile:" + getName() + "\t x:" + getTileX() + "\t y:" + getTileY()
				+ "\t value:" + value + " parent:" + parent;
	}
}
