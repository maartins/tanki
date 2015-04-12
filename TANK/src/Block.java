
public class Block extends GameObject{
	
	private boolean isWalkable;
	private boolean isShootable;

	public Block(int posX, int posY, boolean isWalkable, boolean isShootable, String name, String imgPath){	
		super(posX, posY, name, imgPath);
		
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

}
