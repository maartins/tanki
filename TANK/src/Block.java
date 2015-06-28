
public class Block extends GameObject{
	
	private int value;
	private int tileX;
	private int tileY;
	private int maxHp;
	private int curHp;
	
	private boolean isWalkable;
	private boolean isShootable;

	private Block parent;
	
	/**
	* @param  pos x
	* @param  pos y
	* @param  is walkable
	* @param  is shootable
	* @param  name
	*/
	public Block(int posX, int posY, boolean isWalkable, boolean isShootable, String name){	
		super(posX, posY, name);
		
		maxHp = 3;
		curHp = maxHp;
		
		tileX = posX / 32;
		tileY = posY / 32;
		
		value = 0;
		
		this.isWalkable = isWalkable;
		this.isShootable = isShootable;
		
		parent = null;
	}
	
	/**
	* @param  pos x
	* @param  pos y
	* @param  is walkable
	* @param  is shootable
	* @param  name
	* @param image path
	*/
	public Block(int posX, int posY, boolean isWalkable, boolean isShootable, String name, String imagePath){	
		super(posX, posY, name, imagePath);
		
		maxHp = 3;
		curHp = maxHp;
		
		tileX = posX / 32;
		tileY = posY / 32;
		
		value = 0;
		
		this.isWalkable = isWalkable;
		this.isShootable = isShootable;
		
		parent = null;
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
	
	public void reset(){
		value = 0;
		parent = null;
	}
	
	public void recieveDamage(int damage){
		curHp -= damage;
	}
	
	public String toString(){
		return getName() + " x" + tileX + " y" + tileY + " h" + value; 
	}
}
