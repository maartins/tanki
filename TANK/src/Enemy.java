import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class Enemy extends GameObject{
	
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
	
	private boolean isPathing;
	private boolean isPathingStart;
	private boolean isPathDone;
	
	private ArrayList<Block> closedList;
	private ArrayList<Block> openList;
	private ArrayList<Block> navList;
	
	public Enemy(int posX, int posY){
		super(posX, posY, "Enemy", "Images//Enemy01.png");
		
		curHp = maxHp;
		
		veloX = 0;
		veloY = 0;
		
		curDirection = UP;
		preDirection = RIGHT;
		this.setImage(rotate(this.getImage(), curDirection, preDirection));
		
		closedList = new ArrayList<Block>();
		openList = new ArrayList<Block>();
		navList = new ArrayList<Block>();
		
		isPathingStart = true;
	}
	
	public void control(){
		//collisionCheck();
		
		if(!navList.isEmpty()){
			//System.out.println("Moving");
			
			preDirection = curDirection;
			
			//if(targeting()){
				//for(Block b : navList){
				//	b.reset();
			//	}
			//	navList.clear();
			//	
			//	veloX = 0;
			//	veloY = 0;
			//	
			//	System.out.println("TARGETED");
			//}else{
				//System.out.println("Going for: " + navList.get(navList.size() - 1));
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
				}else if(navList.get(navList.size() - 1).getX() == this.getX() && navList.get(navList.size() - 1).getY() == this.getY()){
					veloX = 0;
					veloY = 0;
					navList.get(navList.size() - 1).reset();
					navList.remove(navList.size() - 1);
					//System.out.println("TILE REACHED");
				}
			//}
		}
		
		this.setX(this.getX() + veloX);
		this.setY(this.getY() + veloY);
	}
	
	private boolean targeting(){
		Block top = MainPanel.map1.getBlocks()[this.getPositionOnMap().getTileX() + 1][this.getPositionOnMap().getTileY()];
		Block bot = MainPanel.map1.getBlocks()[this.getPositionOnMap().getTileX() - 1][this.getPositionOnMap().getTileY()];
		Block left = MainPanel.map1.getBlocks()[this.getPositionOnMap().getTileX()][this.getPositionOnMap().getTileY() + 1];
		Block right = MainPanel.map1.getBlocks()[this.getPositionOnMap().getTileX()][this.getPositionOnMap().getTileY() - 1];
		
		if(top.equals(MainPanel.tank)){
			return true;
		}else if(bot.equals(MainPanel.tank)){
			return true;
		}else if(left.equals(MainPanel.tank)){
			return true;
		}else if(right.equals(MainPanel.tank)){
			return true;
		}else{
			return false;
		}
	}
	
	public void pathing(){
		Map mainMap = MainPanel.map1;
		Block tankPos = MainPanel.tank.getPositionOnMap();
		Block enemyPos = this.getPositionOnMap();
		
		if(isPathingStart){
			//System.out.println("PATHING START   size of closed " + closedList.size());
			//System.out.println("PATHING START   size of open   " + openList.size());
			//System.out.println("PATHING START   size of nav    " + navList.size());
			for(Block b : navList){
				b.reset();
			}
			navList.clear();
			
			Block tempTank = mainMap.getBlocks()[tankPos.getTileX()][tankPos.getTileY()];
			if(tempTank.isWalkable()){
				//tempTank.setImage("Images//Nav01.png");
				closedList.add(tempTank);
			}
			
			Block enemyTemp = mainMap.getBlocks()[enemyPos.getTileX()][enemyPos.getTileY()];
			if(enemyTemp.isWalkable()){
				//enemyTemp.setImage("Images//Nav01.png");
				closedList.add(enemyTemp);
			}
			
			
			if(!closedList.contains(mainMap.getBlocks()[closedList.get(1).getTileX() + 1][closedList.get(1).getTileY()])){
				if(mainMap.getBlocks()[closedList.get(1).getTileX() + 1][closedList.get(1).getTileY()].isWalkable()){
					Block tempb = mainMap.getBlocks()[closedList.get(1).getTileX() + 1][closedList.get(1).getTileY()];
					//System.out.println(tempb + " 1");
					int temp = (10 * (Math.abs(closedList.get(0).getTileX() - (closedList.get(1).getTileX() + 1)) 
								   + Math.abs(closedList.get(0).getTileY() - closedList.get(1).getTileY()))) + 10;
					
					tempb.setValue(temp);
					//tempb.setImage("Images//Nav01.png");
					tempb.setParent(closedList.get(1));
					openList.add(tempb);
				}	
			}
			if(!closedList.contains(mainMap.getBlocks()[closedList.get(1).getTileX() - 1][closedList.get(1).getTileY()])){
				if(mainMap.getBlocks()[closedList.get(1).getTileX() - 1][closedList.get(1).getTileY()].isWalkable()){
					Block tempb = mainMap.getBlocks()[closedList.get(1).getTileX() - 1][closedList.get(1).getTileY()];
					//System.out.println(tempb + " 2");
					int temp = (10 * (Math.abs(closedList.get(0).getTileX() - (closedList.get(1).getTileX() - 1)) 
							   + Math.abs(closedList.get(0).getTileY() - closedList.get(1).getTileY()))) + 10;
					
					tempb.setValue(temp);
					//tempb.setImage("Images//Nav01.png");
					tempb.setParent(closedList.get(1));
					openList.add(tempb);
				}
			}
			if(!closedList.contains(mainMap.getBlocks()[closedList.get(1).getTileX()][closedList.get(1).getTileY() + 1])){
				if(mainMap.getBlocks()[closedList.get(1).getTileX()][closedList.get(1).getTileY() + 1].isWalkable()){
					Block tempb = mainMap.getBlocks()[closedList.get(1).getTileX()][closedList.get(1).getTileY() + 1];
					//System.out.println(tempb + " 3");
					int temp = (10 * (Math.abs(closedList.get(0).getTileX() - (closedList.get(1).getTileX())) 
							   + Math.abs(closedList.get(0).getTileY() - (closedList.get(1).getTileY() + 1)))) + 10;
					
					tempb.setValue(temp);
					//tempb.setImage("Images//Nav01.png");
					tempb.setParent(closedList.get(1));
					openList.add(tempb);
				}
			}
			if(!closedList.contains(mainMap.getBlocks()[closedList.get(1).getTileX()][closedList.get(1).getTileY() - 1])){
				if(mainMap.getBlocks()[closedList.get(1).getTileX()][closedList.get(1).getTileY() - 1].isWalkable()){
					Block tempb = mainMap.getBlocks()[closedList.get(1).getTileX()][closedList.get(1).getTileY() - 1];
					//System.out.println(tempb + " 4");
					int temp = (10 * (Math.abs(closedList.get(0).getTileX() - (closedList.get(1).getTileX())) 
							   + Math.abs(closedList.get(0).getTileY() - (closedList.get(1).getTileY() - 1)))) + 10;
					
					tempb.setValue(temp);
					//tempb.setImage("Images//Nav01.png");
					tempb.setParent(closedList.get(1));
					openList.add(tempb);
				}
			}
			
			
			
			Block tempBlock = openList.get(0);
			for(Block b : openList){
				if(b.getValue() <= tempBlock.getValue()){
					tempBlock = b;
				}
			}
			
			//tempBlock.setImage("Images//Test03.png");
			closedList.add(tempBlock);
			
			openList.clear();
			
			isPathing = true;
			isPathingStart = false;
		}
		
		if(isPathing){
			//System.out.println("PATHING   size of closed " + closedList.size());
			//System.out.println("PATHING   size of open   " + openList.size());
			//System.out.println("PATHING   size of nav    " + navList.size());
			for(int i = 2; i < closedList.size(); i++){
				if(!closedList.contains(mainMap.getBlocks()[closedList.get(i).getTileX() + 1][closedList.get(i).getTileY()])){
					if(mainMap.getBlocks()[closedList.get(i).getTileX() + 1][closedList.get(i).getTileY()].isWalkable()){
						Block tempb = mainMap.getBlocks()[closedList.get(i).getTileX() + 1][closedList.get(i).getTileY()];
						//System.out.println(tempb + " 1");
						int temp = (10 * (Math.abs(closedList.get(0).getTileX() - (closedList.get(i).getTileX() + 1)) 
								   + Math.abs(closedList.get(0).getTileY() - closedList.get(i).getTileY()))) + 10;
						
						tempb.setValue(temp);
						//tempb.setImage("Images//Nav01.png");
						if(tempb.getParent() == null)
							tempb.setParent(mainMap.getBlocks()[closedList.get(i).getTileX()][closedList.get(i).getTileY()]);
						openList.add(tempb);
						
						//System.out.println("U adding - " + tempb);
					}	
				}
				if(!closedList.contains(mainMap.getBlocks()[closedList.get(i).getTileX() - 1][closedList.get(i).getTileY()])){
					if(mainMap.getBlocks()[closedList.get(i).getTileX() - 1][closedList.get(i).getTileY()].isWalkable()){
						Block tempb = mainMap.getBlocks()[closedList.get(i).getTileX() - 1][closedList.get(i).getTileY()];
						//System.out.println(tempb + " 2");
						int temp = (10 * (Math.abs(closedList.get(0).getTileX() - (closedList.get(i).getTileX() - 1)) 
								   + Math.abs(closedList.get(0).getTileY() - closedList.get(i).getTileY()))) + 10;
						
						tempb.setValue(temp);
						//tempb.setImage("Images//Nav01.png");
						if(tempb.getParent() == null)
							tempb.setParent(mainMap.getBlocks()[closedList.get(i).getTileX()][closedList.get(i).getTileY()]);
						openList.add(tempb);
						
						//System.out.println("D adding - " + tempb);
					}
				}
				if(!closedList.contains(mainMap.getBlocks()[closedList.get(i).getTileX()][closedList.get(i).getTileY() + 1])){
					if(mainMap.getBlocks()[closedList.get(i).getTileX()][closedList.get(i).getTileY() + 1].isWalkable()){
						Block tempb = mainMap.getBlocks()[closedList.get(i).getTileX()][closedList.get(i).getTileY() + 1];
						//System.out.println(tempb + " 3");
						int temp = (10 * (Math.abs(closedList.get(0).getTileX() - (closedList.get(i).getTileX())) 
								   + Math.abs(closedList.get(0).getTileY() - (closedList.get(i).getTileY() + 1)))) + 10;
						
						tempb.setValue(temp);
						//tempb.setImage("Images//Nav01.png");
						if(tempb.getParent() == null)
							tempb.setParent(mainMap.getBlocks()[closedList.get(i).getTileX()][closedList.get(i).getTileY()]);
						openList.add(tempb);
						
						//System.out.println("L adding - " + tempb);
					}
				}
				if(!closedList.contains(mainMap.getBlocks()[closedList.get(i).getTileX()][closedList.get(i).getTileY() - 1])){
					if(mainMap.getBlocks()[closedList.get(i).getTileX()][closedList.get(i).getTileY() - 1].isWalkable()){
						Block tempb = mainMap.getBlocks()[closedList.get(i).getTileX()][closedList.get(i).getTileY() - 1];
						//System.out.println(tempb + " 4");
						int temp = (10 * (Math.abs(closedList.get(0).getTileX() - (closedList.get(i).getTileX())) 
								   + Math.abs(closedList.get(0).getTileY() - (closedList.get(i).getTileY() - 1)))) + 10;
						
						tempb.setValue(temp);
						//tempb.setImage("Images//Nav01.png");
						if(tempb.getParent() == null)
							tempb.setParent(mainMap.getBlocks()[closedList.get(i).getTileX()][closedList.get(i).getTileY()]);
						openList.add(tempb);
						
						//System.out.println("R adding - " + tempb);
					}
				}				
			}
			
		
			
			if(!openList.isEmpty()){
				//System.out.println("CALCULATE BLOCK WIEGHT");
			    Block tempBlock = openList.get(0);
				for(Block b : openList){
					if(b.getValue() <= tempBlock.getValue()){
						tempBlock = b;
					}
				}
				
				//tempBlock.setImage("Images//Test03.png");
				closedList.add(tempBlock);
				
				openList.clear();
				
				if(tempBlock.getTileX() + 1 == tankPos.getTileX() && tempBlock.getTileY() == tankPos.getTileY()){
					isPathing = false;
					isPathDone = false;
					//System.out.println("PATHING DONE");
				}else if(tempBlock.getTileX() - 1 == tankPos.getTileX() && tempBlock.getTileY() == tankPos.getTileY()){
					isPathing = false;
					isPathDone = false;
					//System.out.println("PATHING DONE");
				}else if(tempBlock.getTileY() + 1 == tankPos.getTileY() && tempBlock.getTileX() == tankPos.getTileX()){
					isPathing = false;
					isPathDone = false;
					//System.out.println("PATHING DONE");
				}else if(tempBlock.getTileY() - 1 == tankPos.getTileY() && tempBlock.getTileX() == tankPos.getTileX()){
					isPathing = false;
					isPathDone = false;
					//System.out.println("PATHING DONE");
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
							//closedList.get(i).getParent().setImage("Images//Test02.png");
							
							//System.out.println(closedList.get(i) + " -> " +closedList.get(i).getParent());
							navList.add(closedList.get(i).getParent());
						}
					}
				}
				//System.out.println("NAV LIST DONE");
				isPathDone = true;
			}
		}
		
		if(tankPos.getTileX() != closedList.get(0).getTileX() || tankPos.getTileY() != closedList.get(0).getTileY()){
			for(Block b : closedList){
				b.reset();
			}
			for(Block b : openList){
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
		for(Block b : MainPanel.map1.getBlockList()){
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
	
	public void damage(){
		curHp--;
	}
	
	public boolean isDead(){
		if(curHp <= 0){
			return true;
		}else{
			return false;
		}
	}
}


