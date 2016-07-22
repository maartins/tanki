import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.util.ArrayList;

public class Enemy extends GameObject implements Runnable{
	
	private int veloX;
	private int veloY;
	private int curDirection;
	private int preDirection;
	private int curHp;
	
	private final int maxHp = 3;
	private final int UP = 1;
	private final int DOWN = 3;
	private final int LEFT = 2;
	private final int RIGHT = 0;
	
	private long startTime;
	private long curTime;
	private long waitTime;
	private long shootTime;
	
	private boolean isPathing;
	private boolean isPathingStart;
	private boolean isPathDone;
	private boolean isRunning;
	
	private ArrayList<NavTile> closedList;
	private ArrayList<NavTile> openList;
	private ArrayList<NavTile> navList;
	private ArrayList<Bullet> bulletList;
	
	private Spawner spawner;
	
	private Thread thread;
	
	public Enemy(int posX, int posY){
		super(posX, posY, "Enemy", "Images//Enemy01.png");
		
		curTime = System.currentTimeMillis();
		
		closedList = new ArrayList<NavTile>();
		openList = new ArrayList<NavTile>();
		navList = new ArrayList<NavTile>();
		bulletList = new ArrayList<Bullet>();
		
		curHp = maxHp;
		
		veloX = 0;
		veloY = 0;
		
		curDirection = UP;
		preDirection = RIGHT;
		this.setImage(rotate(this.getImage(), curDirection, preDirection));
		
		isPathingStart = true;
		isRunning = true;
		
		if(thread == null){
			thread = new Thread(this);
			thread.start();
		}
	}
	
	public Enemy(Block posBlock){
		super(posBlock.getX(), posBlock.getY(), "Enemy", "Images//Enemy01.png");
		
		curTime = System.currentTimeMillis();
		
		closedList = new ArrayList<NavTile>();
		openList = new ArrayList<NavTile>();
		navList = new ArrayList<NavTile>();
		bulletList = new ArrayList<Bullet>();
		
		curHp = maxHp;
		
		veloX = 0;
		veloY = 0;
		
		curDirection = UP;
		preDirection = RIGHT;
		this.setImage(rotate(this.getImage(), curDirection, preDirection));
		
		isPathingStart = true;
		isRunning = true;
		
		if(thread == null){
			thread = new Thread(this);
			thread.start();
		}
	}
	
	public void draw(Graphics g){
		Graphics2D g2d = (Graphics2D) g;
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2d.drawImage(this.getImage(), this.getX(), this.getY(), null);
		
		if(!bulletList.isEmpty()){
			for(Bullet b : bulletList){
				if(!b.isMaxDistReached()){
					if(!b.isCollision()){
						b.draw(g);
					}
				}
			}
		}
	}
	
	@Override
	public void run() {
		while(isRunning){
			startTime = System.currentTimeMillis();
			Toolkit.getDefaultToolkit().sync();
			
			if(System.currentTimeMillis() - shootTime > 1250){
				shootTime = System.currentTimeMillis();
				shoot();
			}
			
			ArrayList<Bullet> deadBulletList = new ArrayList<Bullet>();
			
			if(!bulletList.isEmpty()){
				for(Bullet b : bulletList){
					if(!b.isMaxDistReached()){
						if(!b.isCollision()){
							b.move();
						}else{
							deadBulletList.add(b);
						}
					}else{
						deadBulletList.add(b);
					}
				}
				
				if(!deadBulletList.isEmpty()){
					for(Bullet b : deadBulletList){
						bulletList.remove(b);
					}
				
					deadBulletList.clear();
				}
				//if(bulletList.get(bulletList.size() - 1).isMaxDistReached() || bulletList.get(bulletList.size() - 1).isCollision()){
					//System.out.println("delete");
					//bulletList.remove(bulletList.size() - 1);
				//}
			}
			
			
			curTime = System.currentTimeMillis() - startTime;
			waitTime = (1000 / Settings.framesPerSecond.value()) - curTime;
			try{
				if(waitTime < 0){
					Thread.sleep(10);
				}else{
					Thread.sleep(waitTime);
				}
			}catch(Exception e){
				e.printStackTrace();
			}
		}
	}
	
	private void shoot(){
		int posX = 0, posY = 0;
		if(curDirection == RIGHT){
			posX = this.getX() + this.getWidth();
			posY = this.getY() + (this.getHeight() / 2);
			bulletList.add(new Bullet(posX + 2, posY, curDirection));
		}else if(curDirection == UP){
			posX = this.getX() + (this.getWidth() / 2);
			posY = this.getY() - 2;
			bulletList.add(new Bullet(posX, posY - 2, curDirection));
		}else if(curDirection == LEFT){
			posX = this.getX() - 2;
			posY = this.getY() + (this.getHeight() / 2);
			bulletList.add(new Bullet(posX - 2, posY, curDirection));
		}else if(curDirection == DOWN){
			posX = this.getX() + (this.getWidth() / 2);
			posY = this.getY() + this.getHeight();
			bulletList.add(new Bullet(posX, posY + 2, curDirection));
		}
	}
	
	public void control(){	
		if(!navList.isEmpty()){
			//System.out.println("Moving");
			
			preDirection = curDirection;
			
			if(navList.get(navList.size() - 1).getX() < this.getX()){
				curDirection = LEFT;
				veloX = -1;
				this.setImage(rotate(this.getImage(), curDirection, preDirection));
			}else if(navList.get(navList.size() - 1).getX() > this.getX()){
				curDirection = RIGHT;
				veloX = 1;
				this.setImage(rotate(this.getImage(), curDirection, preDirection));
			}else if(navList.get(navList.size() - 1).getY() < this.getY()){
				curDirection = UP;
				veloY = -1;
				this.setImage(rotate(this.getImage(), curDirection, preDirection));
			}else if(navList.get(navList.size() - 1).getY() > this.getY()){
				curDirection = DOWN;
				veloY = 1;
				this.setImage(rotate(this.getImage(), curDirection, preDirection));
			}else if(navList.get(navList.size() - 1).getX() == this.getX() 
				  && navList.get(navList.size() - 1).getY() == this.getY()){
				veloX = 0;
				veloY = 0;
				navList.get(navList.size() - 1).reset();
				navList.remove(navList.size() - 1);
				//System.out.println("TILE REACHED");
			}
		}
		
		this.setX(this.getX() + veloX);
		this.setY(this.getY() + veloY);
	}
	
	public void pathing(){
		Map mainMap = MainPanel.map;
		
		NavTile enemyPos = mainMap.navMap()[this.getPositionOnMap().getY()][this.getPositionOnMap().getX()];
		enemyPos.setImage("Images\\Nav01.png");	
		NavTile tankPos = mainMap.navMap()[MainPanel.tank.getPositionOnMap().getY()][MainPanel.tank.getPositionOnMap().getX()];
		tankPos.setImage("Images\\Nav01.png");
		NavTile birdPos = mainMap.navMap()[MainPanel.bird.getPositionOnMap().getY()][MainPanel.bird.getPositionOnMap().getX()];
		birdPos.setImage("Images\\Nav01.png");
		NavTile targetPos;
		
		int tankValue = ((Math.abs((enemyPos.getTileX()) - tankPos.getTileX()))
				   + Math.abs(enemyPos.getTileY() - tankPos.getTileY() )) + 10;
		int birdValue = ((Math.abs((enemyPos.getTileX()) - birdPos.getTileX()))
				   + Math.abs(enemyPos.getTileY() - birdPos.getTileY() )) + 10;
		//System.out.println("Tank: " + tankValue + " Bird: " + birdValue);
		
		//enemyPos.setValue(tankValue);
		
		if(tankValue > birdValue){
			targetPos = birdPos;
			enemyPos.setValue(birdValue);
		}else if(birdValue > tankValue){
			targetPos = tankPos;
			enemyPos.setValue(tankValue);
		}else{
			targetPos = birdPos;
			enemyPos.setValue(birdValue);
		}
		//System.out.println("Tank: " + tankValue);
		//targetPos = tankPos;
		
		//System.out.println(targetPos.getTileX());
		//System.out.println(enemyPos);
		
		if(isPathingStart){
			for(NavTile b : navList){
				b.reset();
			}
			navList.clear();
			
			closedList.add(targetPos); // important: set iterators to 1 in closed list loops
			closedList.add(enemyPos);
			
			//System.out.println("Enemy: " + enemyPos);
		
			NavTile tempb = mainMap.navMap()[enemyPos.getTileY() + 1][enemyPos.getTileX()];
			if(!tempb.isBlocking()){			
				int value = ((Math.abs((tempb.getTileX()) - targetPos.getTileX()))
						   + Math.abs(tempb.getTileY() - targetPos.getTileY() )) + 10;
				
				tempb.setValue(value);
				openList.add(tempb);
			}	
			
			tempb = mainMap.navMap()[enemyPos.getTileY() - 1][enemyPos.getTileX()];
			if(!tempb.isBlocking()){			
				int value = ((Math.abs((tempb.getTileX()) - targetPos.getTileX()))
						   + Math.abs(tempb.getTileY() - targetPos.getTileY() )) + 10;
				
				tempb.setValue(value);
				openList.add(tempb);
			}
			
			tempb = mainMap.navMap()[enemyPos.getTileY()][enemyPos.getTileX() + 1];
			if(!tempb.isBlocking()){			
				int value = ((Math.abs((tempb.getTileX()) - targetPos.getTileX()))
						   + Math.abs(tempb.getTileY() - targetPos.getTileY() )) + 10;
				
				tempb.setValue(value);
				openList.add(tempb);
			}
			
			tempb = mainMap.navMap()[enemyPos.getTileY()][enemyPos.getTileX() - 1];
			if(!tempb.isBlocking()){			
				int value = ((Math.abs((tempb.getTileX()) - targetPos.getTileX()))
						   + Math.abs(tempb.getTileY() - targetPos.getTileY() )) + 10;
				
				tempb.setValue(value);
				openList.add(tempb);
			}
			
			NavTile tempBlock = openList.get(0);
			for(NavTile b : openList){
				if(b.getValue() <= tempBlock.getValue()){
					tempBlock = b;
				}
			}
			
			tempBlock.setParent(enemyPos);
			tempBlock.setImage("Images\\Nav01.png");
			closedList.add(tempBlock);
			
			//System.out.println("$ " + tempBlock + " " + closedList.size());
			
			for(NavTile b : openList){
				b.reset();
			}
			openList.clear();
			
			isPathing = true;
			isPathingStart = false;
		}
		
		if(isPathing){
			for(int i = 1; i < closedList.size(); i++){
				
				NavTile tempb = mainMap.navMap()[closedList.get(i).getTileY() + 1][closedList.get(i).getTileX()];
				if(!closedList.contains(tempb)){
					if(!tempb.isBlocking()){
						int value = ((Math.abs((tempb.getTileX()) - targetPos.getTileX()))
								   + Math.abs(tempb.getTileY() - targetPos.getTileY() )) + 10;
						
						tempb.setValue(value);
						tempb.setImage("Images//Nav02.png");
						
						openList.add(tempb);
					}
				}
				
				tempb = mainMap.navMap()[closedList.get(i).getTileY() - 1][closedList.get(i).getTileX()];
				if(!closedList.contains(tempb)){
					if(!tempb.isBlocking()){
						int value = ((Math.abs((tempb.getTileX()) - targetPos.getTileX()))
								   + Math.abs(tempb.getTileY() - targetPos.getTileY() )) + 10;
						
						tempb.setValue(value);
						tempb.setImage("Images//Nav02.png");

						openList.add(tempb);
					}
				}
				
				tempb = mainMap.navMap()[closedList.get(i).getTileY()][closedList.get(i).getTileX() + 1];
				if(!closedList.contains(tempb)){
					if(!tempb.isBlocking()){
						int value = ((Math.abs((tempb.getTileX()) - targetPos.getTileX()))
								   + Math.abs(tempb.getTileY() - targetPos.getTileY() )) + 10;
						
						tempb.setValue(value);
						tempb.setImage("Images//Nav02.png");

						openList.add(tempb);
					}
				}
				
				tempb = mainMap.navMap()[closedList.get(i).getTileY()][closedList.get(i).getTileX() - 1];
				if(!closedList.contains(tempb)){
					if(!tempb.isBlocking()){
						int value = ((Math.abs((tempb.getTileX()) - targetPos.getTileX()))
								   + Math.abs(tempb.getTileY() - targetPos.getTileY() )) + 10;
						
						tempb.setValue(value);
						tempb.setImage("Images//Nav02.png");

						openList.add(tempb);
					}
				}			
			}
			
			//System.out.println(openList.get(0));
			
			if(!openList.isEmpty()){
				//System.out.println("CALCULATE BLOCK WIEGHT");
			    NavTile tempBlock = openList.get(0);
			    //System.out.println("----------------------------------");
			    //for(NavTile b : openList){
			    //	System.out.println(b);
				//}
			    
			    for(NavTile b : openList){
					if(b.getValue() <= tempBlock.getValue()){
						tempBlock = b;
					}
				}
				
				//System.out.println("$ " + tempBlock);
				//System.out.println(mainMap.navMap()[6][10]);
				
				if(tempBlock.getParent() == null){
					tempBlock.setParent(closedList.get(closedList.size() - 1));
				}
				
				closedList.add(tempBlock);

				openList.clear();
				
				if(tempBlock.getTileX() + 1 == targetPos.getTileX() && tempBlock.getTileY()  == targetPos.getTileY()){
					isPathing = false;
					isPathDone = false;
					//System.out.println("PATHING DONE " + targetPos);
				}else if(tempBlock.getTileX() - 1 == targetPos.getTileX() && tempBlock.getTileY()  == targetPos.getTileY()){
					isPathing = false;
					isPathDone = false;
					//System.out.println("PATHING DONE " + targetPos);
				}else if(tempBlock.getTileY() + 1 == targetPos.getTileY() && tempBlock.getTileX() == targetPos.getTileX()){
					isPathing = false;
					isPathDone = false;
					//System.out.println("PATHING DONE " + targetPos);
				}else if(tempBlock.getTileY()  - 1 == targetPos.getTileY() && tempBlock.getTileX() == targetPos.getTileX()){
					isPathing = false;
					isPathDone = false;
					//System.out.println("PATHING DONE " + targetPos);
				}
			}
		}else{
			if(!isPathDone){
				//for (NavTile n : closedList) {
				//	System.out.println(n);
				//}
				navList.add(closedList.get(closedList.size() - 1));
				
				for(int i = closedList.size() - 1; i > 1; i--){
					if(closedList.get(i).getParent() == null){
						//System.out.println("parent null");
						break;
					}else{	
						if(closedList.get(i).getParent().equals(enemyPos)){
							//System.out.println("last parent");
							break;
						}else{
							navList.add(closedList.get(i).getParent());
						}
					}
				}	
				
				isPathDone = true;
				
				//System.out.println("NAV LIST DONE");
				for (NavTile n : navList) {
					n.setImage("Images//Nav01.png");
					//System.out.println(n);
				}
			}
		}
		
		
		
		boolean reset = false;
		if(targetPos.getTileX() != closedList.get(0).getTileX() || targetPos.getTileY() != closedList.get(0).getTileY()){
			reset = true;
		}
		
		if(reset){
			for(NavTile b : closedList){
				b.reset();
			}
			for(NavTile b : openList){
				b.reset();
			}
			for(NavTile b : navList){
				b.reset();
			}
			
			closedList.clear();
			openList.clear();
			navList.clear();
			
			isPathingStart = true;
			isPathing = false;
			isPathDone = false;
		}
	}
	
	public void collisionCheck(){
		for(Block b : MainPanel.map.getBlockList()){
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
		
		if(this.getBounds().intersects(MainPanel.bird.getBounds())){
		    Rectangle insect = this.getBounds().intersection(MainPanel.bird.getBounds());

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
		        	this.setX(MainPanel.bird.getX() + MainPanel.bird.getWidth());
		        }else{
		        	this.setX(MainPanel.bird.getX() - this.getWidth());
		        }
		    }else if(vertical){
		        if(isTop){
		        	this.setY(MainPanel.bird.getY() + MainPanel.bird.getHeight());
		        }else{
		        	this.setY(MainPanel.bird.getY() - this.getHeight());
		        }
		    }
		}
	}
	
	public void recieveDamage(int damage){
		curHp -= damage;
	}
	
	public boolean isDead(){
		if(curHp <= 0){
			return true;
		}else{
			return false;
		}
	}
	
	public void setSpawner(Spawner spawner){
		this.spawner = spawner;
	}
	
	public Spawner getSpawner(){
		return spawner;
	}
	
	public void die(){
		isRunning = false;
		try {
			thread.join();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		closedList.clear();
		openList.clear();
		for (NavTile b : navList) {
			b.reset();
		}
		navList.clear();
		bulletList.clear();
	}
}