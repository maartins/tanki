import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;

public class Tank extends GameObject implements KeyListener, Runnable{
	
	private int veloX;
	private int veloY;
	private int curDirection;
	private int preDirection;
	private int curHp;
	private int score;

	private final int maxHp = 50;
	private final int UP = 1;
	private final int DOWN = 3;
	private final int LEFT = 2;
	private final int RIGHT = 0;

	private long startTime;
	private long curTime;
	private long waitTime;
	private long shootTime;
	
	private boolean keyA;
	private boolean keyD;
	private boolean keyW;
	private boolean keyS;
	private boolean keySPACE;
	private boolean isRunning;
	
	private ArrayList<Bullet> bulletList;
	
	private Sound shootSound;
	@SuppressWarnings("unused")
	private Sound moveSound;
	
	private Thread thread;
	
	public Tank(Block posBlock){
		super(posBlock.getX(), posBlock.getY(), "Tank", "Images//Test02.png");
		
		bulletList = new ArrayList<Bullet>();
		
		curHp = maxHp;
		score = 0;		
		
		veloX = 0;
		veloY = 0;
		
		curDirection = UP;
		preDirection = RIGHT;
		
		keyA = false;
		keyD = false;
		keyW = false;
		keyS = false;
		
		keySPACE = false;
		
		shootSound = new Sound("Sounds//tank_shoot01.wav");
		moveSound = new Sound("Sounds//tank_move01.wav");
		
		isRunning = true;
		
		if(thread == null){
			thread = new Thread(this);
			thread.start();
		}
	}
	
	public Tank(int posX, int posY){
		super(posX, posY, "Tank", "Images//Test02.png");
		
		bulletList = new ArrayList<Bullet>();
		
		curHp = maxHp;
		score = 0;		
		
		veloX = 0;
		veloY = 0;
		
		curDirection = UP;
		preDirection = RIGHT;
		
		keyA = false;
		keyD = false;
		keyW = false;
		keyS = false;
		
		keySPACE = false;
		
		shootSound = new Sound("Sounds//tank_shoot01.wav");
		moveSound = new Sound("Sounds//tank_move01.wav");
		
		isRunning = true;
		
		if(thread == null){
			thread = new Thread(this);
			thread.start();
		}
	}
	
	public int getCurHp() {
		return curHp;
	}

	public void setCurHp(int curHp) {
		this.curHp = curHp;
	}
	
	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}
	
	public int getMaxHp() {
		return maxHp;
	}
	
	public void setLocation(Block posBlock) {
		setX(posBlock.getX());
		setY(posBlock.getY());
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
		
		//System.out.println(bulletList.size());
	}
	
	@Override
	public void run() {
		while(isRunning){
			startTime = System.currentTimeMillis();
			Toolkit.getDefaultToolkit().sync();
			
			if(!bulletList.isEmpty()){
				for(Bullet b : bulletList){
					if(!b.isMaxDistReached()){
						if(!b.isCollision()){
							b.move();
							//System.out.println("move");
						}
					}
				}
				if(bulletList.get(bulletList.size() - 1).isMaxDistReached() || bulletList.get(bulletList.size() - 1).isCollision()){
					//System.out.println("delete");
					bulletList.remove(bulletList.size() - 1);
				}
			}
			
			
			curTime = System.currentTimeMillis() - startTime;
			waitTime = (1000 / 60) - curTime;
			try{
				if(waitTime < 0){
					Thread.sleep(5);
				}else{
					Thread.sleep(waitTime);
				}
			}catch(Exception e){
				e.printStackTrace();
			}
		}
	}
	
	public void control(){
		if(keyA == keyD){
			veloX = 0;
		}else if(keyA){
			veloX = -1;
			//moveSound.play();
		}else if(keyD){
			veloX = 1;
			//moveSound.play();
		}
		
		if(keyW == keyS){
			veloY = 0;
		}else if(keyW){
			veloY = -1;
			//moveSound.play();
		}else if(keyS){
			veloY = 1;
			//moveSound.play();
		}
		
		if(keyA && keyS || keyA && keyW){
			veloX = 0;
			veloY = 0;
		}else if(keyD && keyS || keyD && keyW){
			veloX = 0;
			veloY = 0;			
		}
		
		if(keySPACE){
			if(System.currentTimeMillis() - shootTime > 250){
				shootTime = System.currentTimeMillis();
				shootSound.play();
				shoot();
				//System.out.println("SHOOT");
			}
		}
		
		//System.out.println("cur dir: " + curDirection + " prev dir: " + preDirection);
		
		this.setX(this.getX() + veloX);
		this.setY(this.getY() + veloY);
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
			if(this.getBounds().intersects(e.getBounds())){
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
	}
	
	private void shoot(){
		int posX = 0, posY = 0;
		if(curDirection == RIGHT){
			posX = this.getX() + this.getWidth();
			posY = this.getY() + (this.getHeight() / 2);
			bulletList.add(new Bullet(posX + 1, posY, curDirection, "t-bullet"));
		}else if(curDirection == UP){
			posX = this.getX() + (this.getWidth() / 2);
			posY = this.getY() - 2;
			bulletList.add(new Bullet(posX, posY - 1, curDirection, "t-bullet"));
		}else if(curDirection == LEFT){
			posX = this.getX() - 2;
			posY = this.getY() + (this.getHeight() / 2);
			bulletList.add(new Bullet(posX - 1, posY, curDirection, "t-bullet"));
		}else if(curDirection == DOWN){
			posX = this.getX() + (this.getWidth() / 2);
			posY = this.getY() + this.getHeight();
			bulletList.add(new Bullet(posX, posY + 1, curDirection, "t-bullet"));
		}
	}

	@Override
	public void keyPressed(KeyEvent e) {
		int keyCode = e.getKeyCode();
		switch(keyCode){
		case KeyEvent.VK_A:
			keyA = true;//System.out.println(KeyEvent.VK_A + " " + keyA);
			if(curDirection != LEFT){
				preDirection = curDirection;
				curDirection = LEFT;
				this.setImage(rotate(this.getImage(), curDirection, preDirection));
			}
			break;
		case KeyEvent.VK_D:
			keyD = true;//System.out.println(KeyEvent.VK_D + " " + keyD);
			if(curDirection != RIGHT){
				preDirection = curDirection;
				curDirection = RIGHT;
				this.setImage(rotate(this.getImage(), curDirection, preDirection));
			}
			break;
		case KeyEvent.VK_W:
			keyW = true;//System.out.println(KeyEvent.VK_W + " " + keyW);
			if(curDirection != UP){
				preDirection = curDirection;
				curDirection = UP;
				this.setImage(rotate(this.getImage(), curDirection, preDirection));
			}
			break;
		case KeyEvent.VK_S:
			keyS = true;//System.out.println(KeyEvent.VK_S + " " + keyS);
			if(curDirection != DOWN){
				preDirection = curDirection;
				curDirection = DOWN;
				this.setImage(rotate(this.getImage(), curDirection, preDirection));
			}
			break;
		case KeyEvent.VK_SPACE:
			keySPACE = true;
			break;
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		int keyCode = e.getKeyCode();
		switch(keyCode){
		case KeyEvent.VK_A:
			keyA = false;//System.out.println(KeyEvent.VK_A + " " + keyA);
			break;
		case KeyEvent.VK_D:
			keyD = false;//System.out.println(KeyEvent.VK_D + " " + keyD);
			break;
		case KeyEvent.VK_W:
			keyW = false;//System.out.println(KeyEvent.VK_W + " " + keyW);
			break;
		case KeyEvent.VK_S:
			keyS = false;//System.out.println(KeyEvent.VK_S + " " + keyS);
			break;
		case KeyEvent.VK_SPACE:
			keySPACE = false;
			break;
		}
	}

	@Override
	public void keyTyped(KeyEvent e) {}
	
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
	
	public void reset(){
		isRunning = false;
		try {
			thread.join();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		bulletList.clear();
		curHp = maxHp;
		score = 0;
	}
}
