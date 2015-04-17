import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Toolkit;
import java.util.ArrayList;

import javax.swing.JLabel;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class MainPanel extends JPanel implements Runnable{
	
	private final int WWIDTH = 512;
	private final int WHEIGHT = 512;
	private final int FPS = 60;

	private long startTime;
	private long curTime;
	private long totalTime;
	private long frameCount;
	private long waitTime;
	
	private boolean isRunning;
	
	public static Tank tank;
	public static Map map1;
	public static ArrayList<Enemy> enemies = new ArrayList<Enemy>();
	
	private ArrayList<Block> closedList = new ArrayList<Block>();
	private ArrayList<Block> openList = new ArrayList<Block>();
	
	private Thread thread;
	
	public MainPanel(){
		this.setPreferredSize(new Dimension(WWIDTH, WHEIGHT));
		this.setLayout(null);
		
		this.setFocusable(true);
		this.requestFocus();
		this.setDoubleBuffered(true);
		
		map1 = new Map();
		if(thread == null){
			thread = new Thread(this);
			thread.start();
		}
	}
	
	private void init(){
		tank = new Tank(100, 100);
		this.addKeyListener(tank);
		
		for(Block b : map1.getBlocks()){
			if(b.getTileX() == tank.getPositionOnMap().getTileX() && b.getTileY() == tank.getPositionOnMap().getTileY()){
				b.setImage("Images//Nav01.png");
				closedList.add(b);
			}
		}
		
		enemies.add(new Enemy(300, 150));
		//enemies.add(new Enemy(200, 150));
		//enemies.add(new Enemy(200, 200));
		//enemies.add(new Enemy(150, 200));
		
		for(Enemy e : enemies){
			for(Block b : map1.getBlocks()){
				if(b.getTileX() == e.getPositionOnMap().getTileX() && b.getTileY() == e.getPositionOnMap().getTileY()){
					b.setImage("Images//Nav01.png");
					closedList.add(b);
				}
			}
			for(Block b : map1.getBlocks()){
				if(b.getTileX() == e.getPositionOnMap().getTileX() + 1 && b.getTileY() == e.getPositionOnMap().getTileY()){
					int temp1 = closedList.get(0).getTileX() - b.getTileX();
					if(temp1 < 0){
						temp1 = -temp1;
					}
					int temp2 = closedList.get(0).getTileY() - b.getTileY();
					if(temp2 < 0){
						temp2 = -temp2;
					}
					b.setH(temp1 + temp2);
					b.setImage("Images//Nav01.png");
					openList.add(b);
					System.out.println(b.getH() + " x" + b.getTileX() + " y" + b.getTileY());
				}else if(b.getTileX() == e.getPositionOnMap().getTileX() - 1 && b.getTileY() == e.getPositionOnMap().getTileY()){
					int temp1 = closedList.get(0).getTileX() - b.getTileX();
					if(temp1 < 0){
						temp1 = -temp1;
					}
					int temp2 = closedList.get(0).getTileY() - b.getTileY();
					if(temp2 < 0){
						temp2 = -temp2;
					}
					b.setH(temp1 + temp2);
					b.setImage("Images//Nav01.png");
					openList.add(b);
					System.out.println(b.getH() + " x" + b.getTileX() + " y" + b.getTileY());
				}else if(b.getTileX() == e.getPositionOnMap().getTileX() && b.getTileY() == e.getPositionOnMap().getTileY() + 1){
					int temp1 = closedList.get(0).getTileX() - b.getTileX();
					if(temp1 < 0){
						temp1 = -temp1;
					}
					int temp2 = closedList.get(0).getTileY() - b.getTileY();
					if(temp2 < 0){
						temp2 = -temp2;
					}
					b.setH(temp1 + temp2);
					b.setImage("Images//Nav01.png");
					openList.add(b);
					System.out.println(b.getH() + " x" + b.getTileX() + " y" + b.getTileY());
				}else if(b.getTileX() == e.getPositionOnMap().getTileX() && b.getTileY() == e.getPositionOnMap().getTileY() - 1){
					int temp1 = closedList.get(0).getTileX() - b.getTileX();
					if(temp1 < 0){
						temp1 = -temp1;
					}
					int temp2 = closedList.get(0).getTileY() - b.getTileY();
					if(temp2 < 0){
						temp2 = -temp2;
					}
					b.setH(temp1 + temp2);
					b.setImage("Images//Nav01.png");
					openList.add(b);
					System.out.println(b.getH() + " x" + b.getTileX() + " y" + b.getTileY());
				}
			}
		}
		
		// open list  // if H1  <= H2  add to closedlist
		
		
		isRunning = true;
	}
	
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		map1.draw(g);
		tank.draw(g);
		
		for(Enemy e : enemies){
			e.draw(g);
		}
	}

	@Override
	public void run() {
		init();
		while(isRunning){
			startTime = System.currentTimeMillis();
			Toolkit.getDefaultToolkit().sync();
			
			tank.control();
			
			for(Block b : map1.getBlocks()){
				// check closed list +  add new path
			}
			
			for(Enemy e : enemies){
				e.control();
			}
			
			this.repaint();
			
			curTime = System.currentTimeMillis() - startTime;
			waitTime = (1000 / FPS) - curTime;
			try{
				if(waitTime < 0){
					Thread.sleep(5);
				}else{
					Thread.sleep(waitTime);
				}
			}catch(Exception e){
				e.printStackTrace();
			}
			
			++frameCount;
            totalTime += (System.currentTimeMillis() - startTime);
            if (totalTime > 1000) {
                long realFPS = (long) ((double) frameCount/ (double) totalTime * 1000.0);
                System.out.println("fps: " + realFPS);
                frameCount = 0;
                totalTime = 0;
            }
		}
	}
}
