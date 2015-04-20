
public class Block extends GameObject{
	
	private int value;
	private int tileX;
	private int tileY;
	
	private boolean isWalkable;
	private boolean isShootable;

	private Block parent;

	public Block(int posX, int posY, int tileX, int tileY, boolean isWalkable, boolean isShootable, String name, String imgPath){	
		super(posX, posY, name, imgPath);
		
		this.tileX = tileX;
		this.tileY = tileY;
		
		value = 0;
		
		this.isWalkable = isWalkable;
		this.isShootable = isShootable;
		
		parent = this;
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
	
	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
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
	
	public Block getParent() {
		return parent;
	}

	public void setParent(Block parent) {
		this.parent = parent;
	}
	
	public String toString(){
		return getName() + " x" + tileX + " y" + tileY + " h" + value; 
	}
}
