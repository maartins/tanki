import java.awt.Graphics;
import java.awt.Toolkit;
import java.util.ArrayList;

import javax.swing.JPanel;

@SuppressWarnings("serial")
public class MainPanel extends JPanel implements Runnable{
	
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
	
	private ArrayList<Enemy> deadList = new ArrayList<Enemy>();
	
	private Thread thread;
	
	public MainPanel(){
		this.setLayout(null);
		
		this.setFocusable(true);
		this.requestFocus();
		this.setDoubleBuffered(true);
		
		map1 = new Map("Maps//map1.txt");
		
		if(thread == null){
			thread = new Thread(this);
			thread.start();
		}
	}
	
	private void init(){
		tank = new Tank(100, 400);
		this.addKeyListener(tank);
		
		for(Spawner s : map1.getSpawnerList()){
			s.spawn();
		}
		
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
			
			for(Spawner s : map1.getSpawnerList()){
				s.spawn();
			}
			
			tank.control();
			tank.collisionCheck();
			
			if(!enemies.isEmpty()){
				for(Enemy e : enemies){
					e.pathing();
					e.control();
					e.collisionCheck();
					
					if(e.isDead()){
						e.getSpawner().setCanSpawn(true);
						tank.setScore(tank.getScore() + 20);
						deadList.add(e);
					}
				}
				
				for(Enemy e : deadList){
					enemies.remove(e);
				}
				if(!deadList.isEmpty()){
					deadList.clear();
				}
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
                @SuppressWarnings("unused")
				long realFPS = (long) ((double) frameCount/ (double) totalTime * 1000.0);
                //System.out.println("fps: " + realFPS);
                frameCount = 0;
                totalTime = 0;
            }
		}
	}
}
