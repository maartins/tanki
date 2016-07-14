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
			
			if(navList.get(navList.size() - 1).getX() * 32 < this.getX()){
				curDirection = LEFT;
				veloX = -1;
				this.setImage(rotate(this.getImage(), curDirection, preDirection));
			}else if(navList.get(navList.size() - 1).getX() * 32 > this.getX()){
				curDirection = RIGHT;
				veloX = 1;
				this.setImage(rotate(this.getImage(), curDirection, preDirection));
			}else if(navList.get(navList.size() - 1).getY() * 32 < this.getY()){
				curDirection = UP;
				veloY = -1;
				this.setImage(rotate(this.getImage(), curDirection, preDirection));
			}else if(navList.get(navList.size() - 1).getY() * 32 > this.getY()){
				curDirection = DOWN;
				veloY = 1;
				this.setImage(rotate(this.getImage(), curDirection, preDirection));
			}else if(navList.get(navList.size() - 1).getX() * 32 == this.getX() 
				  && navList.get(navList.size() - 1).getY() * 32 == this.getY()){
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
		NavTile enemyPos = this.getPositionOnMap();
		NavTile targetPos;
		
		NavTile tankPos = MainPanel.tank.getPositionOnMap();
		NavTile birdPos = MainPanel.bird.getPositionOnMap();
		
		//System.out.println("-------------------"
		//				+ "\ntank pos: " + tankPos 
		//				+ "\nenemy pos: " + enemyPos 
		//				+ "\nbird pos: " + birdPos);
		
		int tankValue = (10 * (Math.abs(tankPos.getX() - (enemyPos.getX())) 
				   			+ Math.abs(tankPos.getY() - enemyPos.getY()))) + 10;
		int birdValue = (10 * (Math.abs(birdPos.getX() - (enemyPos.getX())) 
	   						+ Math.abs(birdPos.getY() - enemyPos.getY()))) + 10;
		
		//System.out.println("Tank: " + tankValue + " Bird: " + birdValue);
		
		if(tankValue > birdValue){
			targetPos = birdPos;
		}else if(birdValue > tankValue){
			targetPos = tankPos;
		}else{
			targetPos = birdPos;
		}
		
		targetPos = tankPos;
		
		if(isPathingStart){
			//System.out.println("PATHING START   size of closed " + closedList.size());
			//System.out.println("PATHING START   size of open   " + openList.size());
			//System.out.println("PATHING START   size of nav    " + navList.size());
			for(NavTile b : navList){
				b.reset();
			}
			navList.clear();
			
			closedList.add(targetPos);
			
			closedList.add(enemyPos);
			
			
			if(!closedList.contains(mainMap.navMap()[closedList.get(1).getX() + 1][closedList.get(1).getY()])){
				if(!mainMap.navMap()[closedList.get(1).getX() + 1][closedList.get(1).getY()].isBlocking()){
					NavTile tempb = mainMap.navMap()[closedList.get(1).getX() + 1][closedList.get(1).getY()];
					//System.out.println(tempb + " 1");
					int value = (10 * (Math.abs(closedList.get(0).getX() - (closedList.get(1).getX() + 1)) 
								   + Math.abs(closedList.get(0).getY() - closedList.get(1).getY()))) + 10;
					
					tempb.setValue(value);

					openList.add(tempb);
				}	
			}
			if(!closedList.contains(mainMap.navMap()[closedList.get(1).getX() - 1][closedList.get(1).getY()])){
				if(!mainMap.navMap()[closedList.get(1).getX() - 1][closedList.get(1).getY()].isBlocking()){
					NavTile tempb = mainMap.navMap()[closedList.get(1).getX() - 1][closedList.get(1).getY()];
					//System.out.println(tempb + " 2");
					int value = (10 * (Math.abs(closedList.get(0).getX() - (closedList.get(1).getX() - 1)) 
							   + Math.abs(closedList.get(0).getY() - closedList.get(1).getY()))) + 10;
				
					tempb.setValue(value);

					openList.add(tempb);
				}
			}
			if(!closedList.contains(mainMap.navMap()[closedList.get(1).getX()][closedList.get(1).getY() + 1])){
				if(!mainMap.navMap()[closedList.get(1).getX() - 1][closedList.get(1).getY() + 1].isBlocking()){
					NavTile tempb = mainMap.navMap()[closedList.get(1).getX()][closedList.get(1).getY() + 1];
					//System.out.println(tempb + " 2");
					int value = (10 * (Math.abs(closedList.get(0).getX() - closedList.get(1).getX()) 
							   + Math.abs(closedList.get(0).getY() - (closedList.get(1).getY() + 1)))) + 10;
				
					tempb.setValue(value);

					openList.add(tempb);
				}
			}
			if(!closedList.contains(mainMap.navMap()[closedList.get(1).getX()][closedList.get(1).getY() - 1])){
				if(!mainMap.navMap()[closedList.get(1).getX() - 1][closedList.get(1).getY() - 1].isBlocking()){
					NavTile tempb = mainMap.navMap()[closedList.get(1).getX()][closedList.get(1).getY() - 1];
					//System.out.println(tempb + " 2");
					int value = (10 * (Math.abs(closedList.get(0).getX() - closedList.get(1).getX()) 
							   + Math.abs(closedList.get(0).getY() - (closedList.get(1).getY() - 1)))) + 10;
				
					tempb.setValue(value);

					openList.add(tempb);
				}
			}
			
			NavTile tempBlock = openList.get(0);
			for(NavTile b : openList){
				if(b.getValue() <= tempBlock.getValue()){
					tempBlock = b;
				}
			}
			
			tempBlock.setParent(closedList.get(1));
			closedList.add(tempBlock);
			
			for(NavTile b : openList){
				b.reset();
			}
			openList.clear();
			
			isPathing = true;
			isPathingStart = false;
		}
		
		if(isPathing){
			//System.out.println("PATHING   size of closed " + closedList.size());
			//System.out.println("PATHING   size of open   " + openList.size());
			//System.out.println("PATHING   size of nav    " + navList.size());
			
			int stuckCounter = 0;
			
			for(int i = 2; i < closedList.size(); i++){
				boolean test1 = false, test2 = false, test3 = false, test4 = false;
				if(!closedList.contains(mainMap.navMap()[closedList.get(i).getX() + 1][closedList.get(i).getY()])){
					if(!mainMap.navMap()[closedList.get(i).getX() + 1][closedList.get(i).getY()].isBlocking()){
						NavTile tempb = mainMap.navMap()[closedList.get(i).getX() + 1][closedList.get(i).getY()];
						//System.out.println(tempb + " 1");
						int value = (10 * (Math.abs(closedList.get(0).getX() - (closedList.get(i).getX() + 1)) 
								   + Math.abs(closedList.get(0).getY() - closedList.get(i).getY()))) + 10;
						
						tempb.setValue(value);
						tempb.setImage("Images//Nav01.png");
						
						openList.add(tempb);
						
						//System.out.println("U adding - " + tempb);
					}else{
						test1 = true;
					}
				}else{
					test1 = true;
				}
				if(!closedList.contains(mainMap.navMap()[closedList.get(i).getX() - 1][closedList.get(i).getY()])){
					if(!mainMap.navMap()[closedList.get(i).getX() - 1][closedList.get(i).getY()].isBlocking()){
						NavTile tempb = mainMap.navMap()[closedList.get(i).getX() - 1][closedList.get(i).getY()];
						//System.out.println(tempb + " 1");
						int value = (10 * (Math.abs(closedList.get(0).getX() - (closedList.get(i).getX() - 1)) 
								   + Math.abs(closedList.get(0).getY() - closedList.get(i).getY()))) + 10;
						
						tempb.setValue(value);
						tempb.setImage("Images//Nav01.png");

						openList.add(tempb);
						
						//System.out.println("U adding - " + tempb);
					}else{
						test2 = true;
					}
				}else{
					test2 = true;
				}
				if(!closedList.contains(mainMap.navMap()[closedList.get(i).getX()][closedList.get(i).getY() + 1])){
					if(!mainMap.navMap()[closedList.get(i).getX()][closedList.get(i).getY() + 1].isBlocking()){
						NavTile tempb = mainMap.navMap()[closedList.get(i).getX()][closedList.get(i).getY() + 1];
						//System.out.println(tempb + " 1");
						int value = (10 * (Math.abs(closedList.get(0).getX() - closedList.get(i).getX()) 
								   + Math.abs(closedList.get(0).getY() - (closedList.get(i).getY() + 1)))) + 10;
						
						tempb.setValue(value);
						tempb.setImage("Images//Nav01.png");

						openList.add(tempb);
						
						//System.out.println("U adding - " + tempb);
					}else{
						test3 = true;
					}
				}else{
					test3 = true;
				}
				if(!closedList.contains(mainMap.navMap()[closedList.get(i).getX()][closedList.get(i).getY() - 1])){
					if(!mainMap.navMap()[closedList.get(i).getX()][closedList.get(i).getY() - 1].isBlocking()){
						NavTile tempb = mainMap.navMap()[closedList.get(i).getX()][closedList.get(i).getY() - 1];
						//System.out.println(tempb + " 1");
						int value = (10 * (Math.abs(closedList.get(0).getX() - closedList.get(i).getX()) 
								   + Math.abs(closedList.get(0).getY() - (closedList.get(i).getY() - 1)))) + 10;
						
						tempb.setValue(value);
						tempb.setImage("Images//Nav01.png");

						openList.add(tempb);
						
						//System.out.println("U adding - " + tempb);
					}else{
						test4 = true;
					}
				}else{
					test4 = true;
				}
				
				if(test1){
					if(test2){
						if(test3){
							if(test4){
								stuckCounter++;
								if(stuckCounter == closedList.size() - 2){
									//System.out.println("Stuck");
									NavTile newPos = closedList.get(closedList.size() - 1);
									//System.out.println("" + newPos.getX() + " " + newPos.getY());
									newPos.setParent(null);
									closedList.clear();
									closedList.add(targetPos);
									closedList.add(newPos);
									closedList.add(newPos);
									
									for(NavTile b : openList){
										b.reset();
									}
									openList.clear();
									stuckCounter = 0;
								}
							}
						}
					}
				}
			}
			
			//System.out.println("" + targetPos.getX() + " " + targetPos.getY());
			
			if(!openList.isEmpty()){
				//System.out.println("CALCULATE BLOCK WIEGHT");
			    NavTile tempBlock = openList.get(0);
				for(NavTile b : openList){
					if(b.getValue() <= tempBlock.getValue()){
						tempBlock = b;
					}
				}
				//System.out.println(tempBlock);
				if(tempBlock.getParent() == null){
					tempBlock.setParent(closedList.get(closedList.size() - 1));
				}
				closedList.add(tempBlock);
				
				for(NavTile b : openList){
					b.reset();
				}
				openList.clear();
				
				if(tempBlock.getX() + 1 == targetPos.getX() && tempBlock.getY() == targetPos.getY()){
					isPathing = false;
					isPathDone = false;
					//System.out.println("PATHING DONE " + targetPos);
				}else if(tempBlock.getX() - 1 == targetPos.getX() && tempBlock.getY() == targetPos.getY()){
					isPathing = false;
					isPathDone = false;
					//System.out.println("PATHING DONE " + targetPos);
				}else if(tempBlock.getY() + 1 == targetPos.getY() && tempBlock.getX() == targetPos.getX()){
					isPathing = false;
					isPathDone = false;
					//System.out.println("PATHING DONE " + targetPos);
				}else if(tempBlock.getY() - 1 == targetPos.getY() && tempBlock.getX() == targetPos.getX()){
					isPathing = false;
					isPathDone = false;
					//System.out.println("PATHING DONE " + targetPos);
				}
			}
		}else{
			if(!isPathDone){
				navList.add(closedList.get(closedList.size() - 1));
				
				for(int i = closedList.size() - 1; i > 0; i--){
					if(closedList.get(i).getParent() == null){
						break;
					}else{	
						if(closedList.get(i).getParent().equals(enemyPos)){
							break;
						}else{
							//System.out.println("CREATING NAV   size of closed " + closedList.size());
							//System.out.println("CREATING NAV   size of open   " + openList.size());
							//System.out.println("CREATING NAV   size of nav    " + navList.size());

							//System.out.println(closedList.get(i) + " -> " +closedList.get(i).getParent());
							navList.add(closedList.get(i).getParent());
						}
					}
				}
				//System.out.println("NAV LIST DONE");
				isPathDone = true;
			}
		}
		
		if(targetPos.getX() != closedList.get(0).getX() || targetPos.getY() != closedList.get(0).getY()){
			for(NavTile b : closedList){
				b.reset();
			}
			for(NavTile b : openList){
				b.reset();
			}
			
			closedList.clear();
			openList.clear();
			
			isPathingStart = true;
			
			//System.out.println("RESET   size of closed " + closedList.size());
			//System.out.println("RESET   size of open   " + openList.size());
			//System.out.println("RESET   size of nav    " + navList.size());
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