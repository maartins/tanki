package Blocks;

import Objects.GameObject;

public class Block extends GameObject {

	private int tileX;
	private int tileY;

	private boolean isSolid;

	public Block(int posX, int posY, boolean isSolid, String name) {
		super(posX, posY, name);

		tileX = posX / 32;
		tileY = posY / 32;

		this.isSolid = isSolid;
	}

	/**
	 * 
	 * @param posX
	 * @param posY
	 * @param isWalkable
	 * @param isShootable
	 * @param isSolid
	 * @param name
	 * @param imagePath
	 */
	public Block(int posX, int posY, boolean isSolid, String name, String imagePath) {
		super(posX, posY, name, imagePath);

		tileX = posX / 32;
		tileY = posY / 32;

		this.isSolid = isSolid;
	}

	/**
	 * @param pos
	 *            x
	 * @param pos
	 *            y
	 * @param name
	 * @param image
	 *            path
	 */
	public Block(int posX, int posY, String name, String imagePath) {
		super(posX, posY, name, imagePath);

		tileX = posX / 32;
		tileY = posY / 32;

		isSolid = false;
	}

	public boolean isSolid() {
		return isSolid;
	}

	public void setSolid(boolean isSolid) {
		this.isSolid = isSolid;
	}

	public int getTileX() {
		return tileX;
	}

	public void setTileX(int x) {
		tileX = x;
	}

	public int getTileY() {
		return tileY;
	}

	public void setTileY(int y) {
		tileY = y;
	}

	public String toString() {
		return getName() + " x" + tileX + " y" + tileY;
	}
}
