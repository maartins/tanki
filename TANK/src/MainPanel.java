
import java.awt.Graphics;
import java.awt.Toolkit;
import java.util.ArrayList;

import javax.swing.JPanel;

@SuppressWarnings("serial")
public class MainPanel extends JPanel implements Runnable{
	
	private int curMap;
	
	private final int FPS = 60;

	private long startTime;
	private long curTime;
	private long totalTime;
	private long frameCount;
	private long waitTime;
	
	private boolean isRunning;
	
	public static Tank tank;
	public static Map map;
	public static ArrayList<Enemy> enemies = new ArrayList<Enemy>();
	
	private ArrayList<String> mapList = new ArrayList<String>();
	private ArrayList<Enemy> deadList = new ArrayList<Enemy>();
	
	private Thread thread;
	
	// NEED TO ADD GAME STATES FOR       -         Main Menu; Main Game; Game End Screen; 
	
	public MainPanel(){
		this.setLayout(null);
		
		this.setFocusable(true);
		this.requestFocus();
		this.setDoubleBuffered(true);
		
		mapList.add("Maps//map1.txt");
		mapList.add("Maps//map2.txt");
		
		curMap = 0;
		
		changeMap();
		
		if(thread == null){
			thread = new Thread(this);
			thread.start();
		}
	}
	
	private void init(){
		tank = new Tank(100, 400);
		this.addKeyListener(tank);
		
		for(Spawner s : map.getSpawnerList()){
			s.spawn();
		}
		
		isRunning = true;
	}
	
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		map.draw(g);
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
			
			int emptySpawnerCounter = 0;
			
			for(Spawner s : map.getSpawnerList()){
				s.spawn();
				if(s.getEnemyCount() <= 0){
					emptySpawnerCounter++;
				}
			}
			
			if(emptySpawnerCounter == map.getSpawnerList().size()){
				changeMap();
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
					e.die();
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
	
	private void changeMap(){
		if(curMap != mapList.size()){
			map = new Map(mapList.get(curMap));
			curMap++;
		}
	}
}
