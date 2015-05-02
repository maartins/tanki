
public class Bullet extends GameObject{
	
	private int dx;
	private int dy;
	private int curDirection;
	
	private final int SPEED = 5;
	private final int MAXDIST = 500;
	
	private boolean maxDistReached;
	private boolean collide;
	
	public Bullet(int posX, int posY, int curDir){
		super(posX, posY, "Bullet", "Images//Test03.png");
		dx = posX;
		dy = posY;
		
		curDirection = curDir;
		
		maxDistReached = false;
		
		collide = false;
	}
	
	public void move(){
		switch(curDirection){
		case 0:
			this.setX(this.getX() + SPEED);
			if(this.getX() - dx >= MAXDIST){
				maxDistReached = true;
			}
			break;
		case 1:
			this.setY(this.getY() - SPEED);
			if(this.getY() - dy <= -MAXDIST){
				maxDistReached = true;
			}
			break;
		case 2:
			this.setX(this.getX() - SPEED);
			if(this.getX() - dx <= -MAXDIST){
				maxDistReached = true;
			}
			break;
		case 3:
			this.setY(this.getY() + SPEED);
			if(this.getY() - dy >= MAXDIST){
				maxDistReached = true;
			}
			break;
		}
		
		for(Block b : MainPanel.map1.getBlockList()){
			if(this.getBounds().intersects(b.getBounds()) && b.isShootable()){
		    	collide = true;
		    }
		}
		
		for(Enemy e : MainPanel.enemies){
			if(this.getBounds().intersects(e.getBounds())){
				System.out.println("Enemy took damage");
				e.damage();
		    	collide = true;
		    }
		}
		
		if(this.getBounds().intersects(MainPanel.tank.getBounds())){
			MainPanel.tank.damage();
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
