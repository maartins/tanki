import java.awt.Rectangle;


public class Block extends GameObject{
	
	private int tileX;
	private int tileY;
	private int maxHp;
	private int curHp;
	
	private boolean isWalkable;
	private boolean isShootable;
	private boolean isSolid;
	private boolean isShot;
	
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
	public Block(int posX, int posY, boolean isWalkable, boolean isShootable, boolean isSolid, String name, String imagePath){	
		super(posX, posY, name, imagePath);
		
		maxHp = 32;
		curHp = maxHp;
		
		tileX = posX / 32;
		tileY = posY / 32;
		
		this.isWalkable = isWalkable;
		this.isShootable = isShootable;
		this.isSolid = isSolid;
		isShot = false;
	}
	
	/**
	* @param  pos x
	* @param  pos y
	* @param  is walkable
	* @param  is shootable
	* @param  name
	*/
	public Block(int posX, int posY, boolean isWalkable, boolean isShootable, String name){	
		super(posX, posY, name);
		
		maxHp = 32;
		curHp = maxHp;
		
		tileX = posX / 32;
		tileY = posY / 32;
		
		this.isWalkable = isWalkable;
		this.isShootable = isShootable;
		isSolid = false;
		isShot = false;
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
		
		maxHp = 32;
		curHp = maxHp;
		
		tileX = posX / 32;
		tileY = posY / 32;
		
		this.isWalkable = isWalkable;
		this.isShootable = isShootable;
		isSolid = false;
		isShot = false;
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
	
	public void recieveDamage(int damage, int dir){
		if(!isSolid){
			if(!isShot){
				
				isShot = true;
			}
			if(dir == 0){
				curHp -= damage;
				
				if(curHp <= 0){
					isShootable = false;
					isWalkable = true;
				}else{
					setX(getX() + damage);
					setImage(crop(getImage(), new Rectangle(curHp, getHeight())));
				}
			}else if(dir == 1){
				curHp -= damage;
				
				if(curHp <= 0){
					isShootable = false;
					isWalkable = true;
				}else{
					setImage(crop(getImage(), new Rectangle(getWidth(), curHp)));
				}
			}else if(dir == 2){
				curHp -= damage;
				
				if(curHp <= 0){
					isShootable = false;
					isWalkable = true;
				}else{
					setImage(crop(getImage(), new Rectangle(curHp, getHeight())));
				}
			}else if(dir == 3){
				curHp -= damage;
				
				if(curHp <= 0){
					isShootable = false;
					isWalkable = true;
				}else{
					setY(getY() + damage);
					setImage(crop(getImage(), new Rectangle(getWidth(), curHp)));
				}
			}
		}
	}

	public boolean isDead(){
		if(curHp <= 0){
			return true;
		}else{
			return false;
		}
	}
	
	public String toString(){
		return getName() + " x" + tileX + " y" + tileY; 
	}
}
