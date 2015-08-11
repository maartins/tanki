
public class Bullet extends GameObject{
	
	private int x;					  // Bullets current x position
	private int y;				  	  // Bullets current y position
	private int direction;		  	  // Bullets moving direction
	private int damage;               // Bullets damage  
	
	private final int SPEED = 5;	  // Bullets speed
	private final int MAXDIST = 500;  // Bullets maximum travel distance
	
	private boolean isMaxDistReached; // Check if the maximum distance is reached
	private boolean isCollision;	  // Check if bullet collides
	
	public Bullet(int posX, int posY, int curDir){
		super(posX, posY, "Bullet", "Images//Bullet01.png");
		x = posX;
		y = posY;
		
		direction = curDir;
		
		damage = 1;
		
		isMaxDistReached = false;
		
		isCollision = false;
	}
	
	public Bullet(int posX, int posY, int curDir, String name){
		super(posX, posY, name, "Images//Bullet01.png");
		x = posX;
		y = posY;
		
		direction = curDir;
		
		damage = 1;
		
		isMaxDistReached = false;
		
		isCollision = false;
	}
	
	public Bullet(int posX, int posY, int curDir, int damage, String name, String imagePath){
		super(posX, posY, name, imagePath);
		x = posX;
		y = posY;
		
		direction = curDir;
		
		this.damage = damage;
		
		isMaxDistReached = false;
		
		isCollision = false;
	}
	
	public void move(){
		switch(direction){
			case 0:
				this.setX(this.getX() + SPEED);
				if(this.getX() - x >= MAXDIST){
					isMaxDistReached = true;
				}
				break;
			case 1:
				this.setY(this.getY() - SPEED);
				if(this.getY() - y <= -MAXDIST){
					isMaxDistReached = true;
				}
				break;
			case 2:
				this.setX(this.getX() - SPEED);
				if(this.getX() - x <= -MAXDIST){
					isMaxDistReached = true;
				}
				break;
			case 3:
				this.setY(this.getY() + SPEED);
				if(this.getY() - y >= MAXDIST){
					isMaxDistReached = true;
				}
				break;
		}
		
		for(Block b : MainPanel.map.getBlockList()){
			if(this.getBounds().intersects(b.getBounds()) && b.isShootable()){
		    	isCollision = true;
		    	b.recieveDamage(8, direction);
		    }
		}
		
		for(Enemy e : MainPanel.enemies){
			if(this.getBounds().intersects(e.getBounds())){
				e.recieveDamage(damage);
		    	isCollision = true;
		    }
		}
		
		if(this.getBounds().intersects(MainPanel.tank.getBounds())){
			MainPanel.tank.recieveDamage(damage);
	    	isCollision = true;
	    }
		
		if(this.getBounds().intersects(MainPanel.bird.getBounds())){
			MainPanel.bird.recieveDamage(damage);
			isCollision = true;
		}
	}
	
	public boolean isMaxDistReached(){
		return isMaxDistReached;
	}
	
	public boolean isCollision(){
		return isCollision;
	}
}
