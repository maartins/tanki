
public class Block extends GameObject{
	
	private int h;
	private int g;
	private int X;
	private int Y;
	
	private boolean isWalkable;
	private boolean isShootable;

	public Block(int posX, int posY, int tileX, int tileY, boolean isWalkable, boolean isShootable, String name, String imgPath){	
		super(posX, posY, name, imgPath);
		
		X = tileX;
		Y = tileY;
		
		this.isWalkable = isWalkable;
		this.isShootable = isShootable;
	}
	
	public boolean isWalkable() {
		return isWalkable;
	}

	public void setWalkable(boolean isWalkable) {
		this.isWalkable = isWalkable;
	}
	
	public boolean isShootable() {
		return isShootable;
	}

	public void setShootable(boolean isShootable) {
		this.isShootable = isShootable;
	}
	
	public int getH() {
		return h;
	}

	public void setH(int h) {
		this.h = h;
	}

	public int getG() {
		return g;
	}

	public void setG(int g) {
		this.g = g;
	}

	public int getTileX() {
		return X;
	}

	public void setTileX(int x) {
		X = x;
	}

	public int getTileY() {
		return Y;
	}

	public void setTileY(int y) {
		Y = y;
	}
}
