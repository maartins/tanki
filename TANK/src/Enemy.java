import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class Enemy extends GameObject{
	
	private int veloX;
	private int veloY;
	private int curDirection;
	private int preDirection;
	
	private final int UP = 1;
	private final int DOWN = 3;
	private final int LEFT = 2;
	private final int RIGHT = 0;
	
	public Enemy(int posX, int posY){
		super(posX, posY, "Enemy", "Images//Enemy01.png");
		
		veloX = 0;
		veloY = 0;
		
		curDirection = UP;
		preDirection = RIGHT;
		this.setImage(rotate(this.getImage(), curDirection, preDirection));
	}
	
	public void control(){
		collisionCheck();
		
		
		//preDirection = curDirection;
		
/*		if(MainPanel.tank.getX() < this.getX()){
			curDirection = LEFT;
			veloX = -1;
			this.setImage(rotate(this.getImage(), curDirection, preDirection));
		}else if(MainPanel.tank.getX() > this.getX()){
			curDirection = RIGHT;
			veloX = 1;
			this.setImage(rotate(this.getImage(), curDirection, preDirection));
		}else if(MainPanel.tank.getY() < this.getY()){
			curDirection = UP;
			veloY = -1;
			this.setImage(rotate(this.getImage(), curDirection, preDirection));
		}else if(MainPanel.tank.getY() > this.getY()){
			curDirection = DOWN;
			veloY = 1;
			this.setImage(rotate(this.getImage(), curDirection, preDirection));
		}else{
			veloX = 0;
		}
		*/
		this.setX(this.getX() + veloX);
		this.setY(this.getY() + veloY);
	}
	
	public Block getPositionOnMap(){
		Block closestTile = new Block(-32, -32, -1, -1, true, false, "test", "Images//Test01.png");
		int temp = 0;
		int distance = (int) Math.sqrt(Math.pow(this.getX() - MainPanel.map1.getBlocks().get(0).getX(), 2) + Math.pow(this.getY() - MainPanel.map1.getBlocks().get(0).getY(), 2));
		for(Block b : MainPanel.map1.getBlocks()){
			temp = (int) Math.sqrt(Math.pow(this.getX() - b.getX(), 2) + Math.pow(this.getY() - b.getY(), 2));
			if(temp < distance){
				distance = temp;
				closestTile = b;
			}
		}
		//System.out.println(closestTile.getName() + " x" + closestTile.getTileX() + " y" + closestTile.getTileY());
		return closestTile;
	}
	
	private void collisionCheck(){
		for(Block b : MainPanel.map1.getBlocks()){
			if(this.getBounds().intersects(b.getBounds()) && !b.isWalkable()){
			    Rectangle insect = this.getBounds().intersection(b.getBounds());

			    boolean vertical = false;
			    boolean horizontal = false;
			    boolean isLeft = false;
			    boolean isTop = false;
	
			    if(insect.getX() == this.getX()){
			        horizontal = true;
			        isLeft = true;
			    }else if(insect.getX() + insect.getWidth() == this.getX() + this.getWidth()){
			        horizontal = true;
			    }
			    if(insect.getY() == this.getY()){
			        vertical = true;
			        isTop = true;
			    }else if(insect.getY() + insect.getHeight() == this.getY() + this.getHeight()){
			        vertical = true;
			    }

			    if(horizontal && vertical){
			        if(insect.getWidth() > insect.getHeight()){
			            horizontal = false;
			        }else{
			            vertical = false;
			        }
			    }
				
			    if(horizontal){
			        if(isLeft){
			        	this.setX(b.getX() + b.getWidth());
			        }else{
			        	this.setX(b.getX() - this.getWidth());
			        }
			    }else if(vertical){
			        if(isTop){
			        	this.setY(b.getY() + b.getHeight());
			        }else{
			        	this.setY(b.getY() - this.getHeight());
			        }
			    }
			}
		}
		
		for(Enemy e : MainPanel.enemies){
			if(!this.equals(e) && this.getBounds().intersects(e.getBounds())){
			    Rectangle insect = this.getBounds().intersection(e.getBounds());

			    boolean vertical = false;
			    boolean horizontal = false;
			    boolean isLeft = false;
			    boolean isTop = false;
	
			    if(insect.getX() == this.getX()){
			        horizontal = true;
			        isLeft = true;
			    }else if(insect.getX() + insect.getWidth() == this.getX() + this.getWidth()){
			        horizontal = true;
			    }
			    if(insect.getY() == this.getY()){
			        vertical = true;
			        isTop = true;
			    }else if(insect.getY() + insect.getHeight() == this.getY() + this.getHeight()){
			        vertical = true;
			    }

			    if(horizontal && vertical){
			        if(insect.getWidth() > insect.getHeight()){
			            horizontal = false;
			        }else{
			            vertical = false;
			        }
			    }
				
			    if(horizontal){
			        if(isLeft){
			        	this.setX(e.getX() + e.getWidth());
			        }else{
			        	this.setX(e.getX() - this.getWidth());
			        }
			    }else if(vertical){
			        if(isTop){
			        	this.setY(e.getY() + e.getHeight());
			        }else{
			        	this.setY(e.getY() - this.getHeight());
			        }
			    }
			}
		}
		
		if(this.getBounds().intersects(MainPanel.tank.getBounds())){
		    Rectangle insect = this.getBounds().intersection(MainPanel.tank.getBounds());

		    boolean vertical = false;
		    boolean horizontal = false;
		    boolean isLeft = false;
		    boolean isTop = false;

		    if(insect.getX() == this.getX()){
		        horizontal = true;
		        isLeft = true;
		    }else if(insect.getX() + insect.getWidth() == this.getX() + this.getWidth()){
		        horizontal = true;
		    }
		    if(insect.getY() == this.getY()){
		        vertical = true;
		        isTop = true;
		    }else if(insect.getY() + insect.getHeight() == this.getY() + this.getHeight()){
		        vertical = true;
		    }

		    if(horizontal && vertical){
		        if(insect.getWidth() > insect.getHeight()){
		            horizontal = false;
		        }else{
		            vertical = false;
		        }
		    }
			
		    if(horizontal){
		        if(isLeft){
		        	this.setX(MainPanel.tank.getX() + MainPanel.tank.getWidth());
		        }else{
		        	this.setX(MainPanel.tank.getX() - this.getWidth());
		        }
		    }else if(vertical){
		        if(isTop){
		        	this.setY(MainPanel.tank.getY() + MainPanel.tank.getHeight());
		        }else{
		        	this.setY(MainPanel.tank.getY() - this.getHeight());
		        }
		    }
		}
	}
	
	private BufferedImage rotate(BufferedImage img, int cdir, int pdir){
		AffineTransform transform = new AffineTransform();
	    transform.rotate(Math.toRadians(-(cdir - pdir) * 90), img.getWidth()/2, img.getHeight()/2);
	    AffineTransformOp op = new AffineTransformOp(transform, AffineTransformOp.TYPE_BILINEAR);
	    img = op.filter(img, null);
	    return img;
	}
}
