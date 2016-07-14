import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;


public class Block extends GameObject{
	
	private int tileX;
	private int tileY;
	
	private boolean isWalkable;
	private boolean isShootable;
	private boolean isSolid;
	private boolean isShot;
	private boolean isDead;
	
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
		
		tileX = posX;
		tileY = posY;
		
		this.isWalkable = isWalkable;
		this.isShootable = isShootable;
		this.isSolid = isSolid;
		isShot = false;
		isDead = false;
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

		tileX = posX;
		tileY = posY;
		
		this.isWalkable = isWalkable;
		this.isShootable = isShootable;
		isSolid = false;
		isShot = false;
		isDead = false;
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
		
		tileX = posX;
		tileY = posY;
		
		this.isWalkable = isWalkable;
		this.isShootable = isShootable;
		isSolid = false;
		isShot = false;
		isDead = false;
	}
	
	public void draw(Graphics g){
		Graphics2D g2d = (Graphics2D) g;
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		
		if(this.getImage() != null){
			g2d.drawImage(this.getImage(), this.getX() * 32, this.getY() * 32, null);
		}
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
			
			if((getWidth() - damage <= 0 && (dir == 0 || dir == 2)) || (getHeight() - damage <= 0 && (dir == 1 || dir == 3))){
				isShootable = false;
				isWalkable = true;
				isDead = true;
			}else{
				if(dir == 0){
					if(getWidth() - damage <= 0)
						damage = damage + (getWidth() - damage);
						
					setX(getX() + damage);
					setImage(crop(getImage(), new Rectangle(getWidth() - damage, getHeight())));
				}else if(dir == 1){
					if(getHeight() - damage <= 0)
						damage = damage + (getHeight() - damage);
					
					setImage(crop(getImage(), new Rectangle(getWidth(), getHeight() - damage)));
				}else if(dir == 2){
					if(getWidth() - damage <= 0)
						damage = damage + (getWidth() - damage);
					
					setImage(crop(getImage(), new Rectangle(getWidth() - damage, getHeight())));
				}else if(dir == 3){
					if(getHeight() - damage <= 0)
						damage = damage + (getHeight() - damage);
					
					setY(getY() + damage);
					setImage(crop(getImage(), new Rectangle(getWidth(), getHeight() - damage)));
				}
			}
		}
	}

	public boolean isDead(){
		return isDead;
	}
	
	public String toString(){
		return getName() + " x" + tileX + " y" + tileY; 
	}
}
