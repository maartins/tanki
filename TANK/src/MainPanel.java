
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import net.java.balloontip.BalloonTip;

@SuppressWarnings("serial")
public class MainPanel extends JPanel implements Runnable{
	
	private JButton startButton = new JButton("Sakt speli");
	private JButton multiButton = new JButton("Top 10");
	private JLabel healthLable = new JLabel("Dzivibas ");
	private JLabel scoreLable = new JLabel("Punkti ");
	private JLabel totalScoreLable = new JLabel();
	private JTextField nameTextField = new JTextField("Ievadiet niku");
	private JLabel titleLable = new JLabel();
	private JPanel scorePanel = new JPanel();
	
	private int currentMap;
	
	private final int FPS = 60;

	private long startTime;
	private long currentTime;
	private long totalTime;
	private long waitTime;
	private long frameCount;
	
	private boolean isRunning;
	
	public static IronBird bird;
	public static Tank tank;
	public static Map map;
	public static ArrayList<Enemy> enemies = new ArrayList<Enemy>();
	
	private ArrayList<String> mapList = new ArrayList<String>();
	private ArrayList<Enemy> deadEnemyList = new ArrayList<Enemy>();
	private ArrayList<Block> deadBlockList = new ArrayList<Block>();
	
	private GameStates currentGameState;

	private Database database;
	
	private Thread thread;
	
	public MainPanel(){
		this.setLayout(null);
		
		this.setFocusable(true);
		this.requestFocus();
		this.setDoubleBuffered(true);
		this.setBackground(new Color(0, 0, 0));
		
		changeGameState(GameStates.MainMenu);
			
		guiSetUp();
		
		database = new Database();
		database.connect();

		File mapFolder = new File("Maps//");
		getFiles(mapFolder);
		
		currentMap = 0;
		changeMap();
		
		bird = new IronBird(map.getIronBirdSpawnPoint());
		
		tank = new Tank(map.getTankSpawnPoint());
		this.addKeyListener(tank);
		
		for(Spawner s : map.getSpawnerList()){
			s.spawn();
		}
		
		isRunning = true;
		
		if(thread == null){
			thread = new Thread(this);
			thread.start();
		}
	}
	
	private void getFiles(File folder){
	    for(File f : folder.listFiles()){
	    	mapList.add(folder.getName() + "//" + f.getName());
	    }
	}
	
	private void guiSetUp(){		
		startButton.setBounds(156, 200, 200, 60);
		startButton.setFont(new Font("Arial", Font.BOLD, 16));
		startButton.setBorderPainted(false);
		startButton.setFocusPainted(false);
		startButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				if(!nameTextField.getText().isEmpty() && !nameTextField.getText().contains("Ievadiet niku")){
					tank.setName(nameTextField.getText());
					nameTextField.setText("");
					changeGameState(GameStates.MainGame);
				}else{
					@SuppressWarnings("unused")
					BalloonTip tip = new BalloonTip(nameTextField, "Ievadiet niku!");
				}
			}
		});
		this.add(startButton);
		
		nameTextField.setBounds(156, 290, 200, 30);
		nameTextField.setFont(new Font("Arial", Font.BOLD, 16));
		nameTextField.setBorder(javax.swing.BorderFactory.createEmptyBorder());
		nameTextField.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				startButton.doClick();
			}
		});
		this.add(nameTextField);
		
		scorePanel.setBounds(156, 200, 200, 170);
		scorePanel.setLayout(null);
		this.add(scorePanel);
		
		multiButton.setBounds(156, 380, 200, 25);
		multiButton.setFont(new Font("Arial", Font.BOLD, 16));
		multiButton.setBorderPainted(false);
		multiButton.setFocusPainted(false);
		multiButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				if(currentGameState == GameStates.MainMenu){
					if(multiButton.getText() == "Top 10"){
						startButton.setVisible(false);
						nameTextField.setVisible(false);
						multiButton.setText("Atpakal");
						ArrayList<String> topTen = database.getTopScore();
						
						int count = 0;
						
						for(String s : topTen){
							JLabel item = new JLabel(s);
							item.setBounds(0, count * 15, 200, 30);
							scorePanel.add(item);
							count++;
						}
						
						scorePanel.setVisible(true);
					}else{
						scorePanel.removeAll();
						scorePanel.setVisible(false);
						startButton.setVisible(true);
						nameTextField.setVisible(true);
						multiButton.setText("Top 10");
					}
				}else if(currentGameState == GameStates.LevelFinished){
					changeGameState(GameStates.MainGame);
					multiButton.setText("Top 10");
				}else{
					changeGameState(GameStates.MainMenu);
					multiButton.setText("Top 10");
				}
			}
		});
		this.add(multiButton);
		
		healthLable.setBounds(5, 515, 200, 30);
		healthLable.setFont(new Font("Arial", Font.BOLD, 16));
		healthLable.setForeground(new Color(255, 255, 255));
		this.add(healthLable);
		
		scoreLable.setBounds(210, 515, 200, 30);
		scoreLable.setFont(new Font("Arial", Font.BOLD, 16));
		scoreLable.setForeground(new Color(255, 255, 255));
		this.add(scoreLable);
		
		totalScoreLable.setBounds(156, 200, 200, 30);
		totalScoreLable.setFont(new Font("Arial", Font.BOLD, 16));
		totalScoreLable.setForeground(new Color(255, 255, 255));
		this.add(totalScoreLable);
		
		titleLable.setBounds(0, 0, 512, 582);
		titleLable.setIcon(new ImageIcon("Images//tank_title.png"));
		this.add(titleLable);
	}
	
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		if(currentGameState == GameStates.MainGame){
			map.draw(g);
			bird.draw(g);
			tank.draw(g);
			
			synchronized (enemies) {
				for(Enemy e : enemies){
					e.draw(g);
				}
			}
		}
	}

	@Override
	public void run() {
		while(isRunning){
			startTime = System.currentTimeMillis();
			Toolkit.getDefaultToolkit().sync();
			
			// ----------------------------------- Speles darbibas kods
			if(currentGameState == GameStates.MainGame){
				int emptySpawnerCounter = 0;
				
				for(Spawner s : map.getSpawnerList()){
					s.spawn();
					if(s.isEmpty()){
						emptySpawnerCounter++;
					}
				}
				
				if(emptySpawnerCounter == map.getSpawnerList().size() && currentMap < mapList.size()){
					for(Enemy e : enemies){
						e.die();
					}
					
					enemies.clear();
					deadEnemyList.clear();
					changeMap();
					changeGameState(GameStates.LevelFinished);
				}else if(emptySpawnerCounter == map.getSpawnerList().size() && currentMap >= mapList.size()){
					for(Enemy e : enemies){
						e.die();
					}
					
					enemies.clear();
					deadEnemyList.clear();
					
					currentMap = 0;
					changeMap();
					changeGameState(GameStates.EndScreen);
				}else if(tank.isDead()){
					for(Enemy e : enemies){
						e.die();
					}
					
					enemies.clear();
					deadEnemyList.clear();
					
					currentMap = 0;
					changeMap();
					changeGameState(GameStates.EndScreen);
				}else if(bird.isDead()){
					for(Enemy e : enemies){
						e.die();
					}
					
					enemies.clear();
					deadEnemyList.clear();
					
					currentMap = 0;
					changeMap();
					changeGameState(GameStates.EndScreen);
				}else{
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
								deadEnemyList.add(e);
							}
						}
						
						if(!deadEnemyList.isEmpty()){
							for(Enemy e : deadEnemyList){
								e.die();
								enemies.remove(e);
							}
						
							deadEnemyList.clear();
						}
					}
					
					for(Block b : map.getBlockList()){
						if(b.isDead()){
							deadBlockList.add(b);
						}
					}
					
					if(!deadBlockList.isEmpty()){
						for(Block b : deadBlockList){
							map.getBlockList().remove(b);
						}
					
						deadBlockList.clear();
					}
					
					healthLable.setText("Dzivibas " + tank.getCurHp());
					scoreLable.setText("Punkti " + String.format("%08d", tank.getScore()));
				}
			}
			// ----------------------------------- Speles darbibas koda beigas
			
			this.repaint();
			
			currentTime = System.currentTimeMillis() - startTime;
			waitTime = (1000 / FPS) - currentTime;
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
		database.disconnect();
	}
	
	private void changeGameState(GameStates state){
		switch(state){
			case MainMenu:
				currentGameState = state;
				titleLable.setVisible(true);
				startButton.setVisible(true);
				nameTextField.setVisible(true);
				scoreLable.setVisible(false);
				healthLable.setVisible(false);
				totalScoreLable.setVisible(false);
				scorePanel.setVisible(false);
				break;
			case MainGame:
				currentGameState = state;
				titleLable.setVisible(false);
				startButton.setVisible(false);
				nameTextField.setVisible(false);
				scoreLable.setVisible(true);
				healthLable.setVisible(true);
				totalScoreLable.setVisible(false);
				multiButton.setVisible(false);
				break;
			case LevelFinished:
				currentGameState = state;
				titleLable.setVisible(true);
				scoreLable.setVisible(false);
				healthLable.setVisible(false);
				totalScoreLable.setVisible(true);
				multiButton.setVisible(true);
				multiButton.setText("Turpinat");
				tank.setScore(tank.getScore() * tank.getCurHp());
				totalScoreLable.setText("Punkti " + String.format("%08d", tank.getScore()));
				tank.setLocation(map.getTankSpawnPoint());
				break;
			case EndScreen:
				currentGameState = state;
				titleLable.setVisible(true);
				scoreLable.setVisible(false);
				healthLable.setVisible(false);
				totalScoreLable.setVisible(true);
				multiButton.setVisible(true);
				multiButton.setText("Beigt speli");
				tank.setScore(tank.getScore() * tank.getCurHp());
				totalScoreLable.setText("Punkti " + String.format("%08d", tank.getScore()));
				database.write(tank.getName(), tank.getScore());
				tank.die();
				tank = new Tank(map.getTankSpawnPoint());
				this.addKeyListener(tank);
				for(Enemy e : enemies){
					e.die();
				}
				enemies.clear();
				break;
			default:
				break;
		}
	}
	
	private void changeMap(){
		if(currentMap < mapList.size()){
			map = new Map(mapList.get(currentMap));
			currentMap++;
		}
	}
}
