
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Toolkit;
import java.util.ArrayList;

import javax.swing.JLabel;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class MainPanel extends JPanel implements Runnable{
	
	private JLabel healthLable = new JLabel("Dzivibas ");
	private JLabel scoreLable = new JLabel("Punkti ");
	
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
	
	private GameStates curState;
	
	private Thread thread;
	
	public MainPanel(){
		this.setLayout(null);
		
		this.setFocusable(true);
		this.requestFocus();
		this.setDoubleBuffered(true);
		
		healthLable.setBounds(5, 515, 200, 30);
		healthLable.setFont(new Font("Arial", Font.BOLD, 16));
		this.add(healthLable);
		scoreLable.setBounds(210, 515, 200, 30);
		scoreLable.setFont(new Font("Arial", Font.BOLD, 16));
		this.add(scoreLable);
		
		mapList.add("Maps//map1.txt");
		mapList.add("Maps//map2.txt");
		
		curMap = 0;
		
		changeMap();
		
		curState = GameStates.MainGame;
		
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
		
		if(curState == GameStates.MainGame){
			map.draw(g);
			tank.draw(g);
			
			for(Enemy e : enemies){
				e.draw(g);
			}
		}
	}

	@Override
	public void run() {
		init();
		while(isRunning){
			startTime = System.currentTimeMillis();
			Toolkit.getDefaultToolkit().sync();
			
			// ----------------------------------- Speles darbibas kods
			if(curState == GameStates.MainGame){
				int emptySpawnerCounter = 0;
				
				for(Spawner s : map.getSpawnerList()){
					s.spawn();
					if(s.getEnemyCount() <= 0){
						emptySpawnerCounter++;
					}
				}
				
				if(emptySpawnerCounter == map.getSpawnerList().size()){
					for(Enemy e : enemies){
						e.die();
					}
					enemies.clear();
					deadList.clear();
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
				
				healthLable.setText("Dzivibas " + MainPanel.tank.getCurHp());
				scoreLable.setText("Punkti " + String.format("%08d", MainPanel.tank.getScore()));
			}
			// ----------------------------------- Speles darbibas koda beigas
			
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
