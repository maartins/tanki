package Pathfinding;

import Blocks.Block;

public class NavTile extends Block {

	private float value;

	private NavTile parent;

	public NavTile(int x, int y, boolean isSolid) {
		super(x, y, isSolid, "navtile");

		value = 0;

		parent = null;
	}

	public float getValue() {
		return value;
	}

	public void setValue(float value) {
		this.value = value;
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
		return "isBlocking:" + isSolid() + " navtile:" + getName() + "\t x:" + getTileX() + "\t y:" + getTileY()
					+ "\t value:" + value + " parent:" + parent;
	}
}
