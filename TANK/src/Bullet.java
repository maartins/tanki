
public class Bullet extends GameObject{
	
	private int disx;
	private int disy;
	private int curDirection;
	private int damage;
	
	private final int SPEED = 5;
	private final int MAXDIST = 200;
	
	private boolean maxDistReached;
	private boolean collide;
	
	public Bullet(int posX, int posY, int curDir, int damage, String name, String imagePath){
		super(posX, posY, name, imagePath);
		disx = posX;
		disy = posY;
		
		curDirection = curDir;
		
		this.damage = damage;
		
		maxDistReached = false;
		
		collide = false;
	}
	
	public Bullet(int posX, int posY, int curDir, String name){
		super(posX, posY, name, "Images//Bullet01.png");
		disx = posX;
		disy = posY;
		
		curDirection = curDir;
		
		damage = 1;
		
		maxDistReached = false;
		
		collide = false;
	}
	
	public Bullet(int posX, int posY, int curDir){
		super(posX, posY, "Bullet", "Images//Bullet01.png");
		disx = posX;
		disy = posY;
		
		curDirection = curDir;
		
		damage = 1;
		
		maxDistReached = false;
		
		collide = false;
	}
	
	public void move(){
		switch(curDirection){
		case 0:
			this.setX(this.getX() + SPEED);
			if(this.getX() - disx >= MAXDIST){
				maxDistReached = true;
			}
			break;
		case 1:
			this.setY(this.getY() - SPEED);
			if(this.getY() - disy <= -MAXDIST){
				maxDistReached = true;
			}
			break;
		case 2:
			this.setX(this.getX() - SPEED);
			if(this.getX() - disx <= -MAXDIST){
				maxDistReached = true;
			}
			break;
		case 3:
			this.setY(this.getY() + SPEED);
			if(this.getY() - disy >= MAXDIST){
				maxDistReached = true;
			}
			break;
		}
		
		for(Block b : MainPanel.map.getBlockList()){
			if(this.getBounds().intersects(b.getBounds()) && b.isShootable()){
		    	collide = true;
		    }
		}
		
		for(Enemy e : MainPanel.enemies){
			if(this.getBounds().intersects(e.getBounds())){
				e.recieveDamage(damage);
		    	collide = true;
		    }
		}
		
		if(this.getBounds().intersects(MainPanel.tank.getBounds())){
			MainPanel.tank.recieveDamage(damage);
	    	collide = true;
	    }
	}
	
	public boolean isMaxDistReached(){
		return maxDistReached;
	}
	
	public boolean isCollision(){
		return collide;
	}
}
