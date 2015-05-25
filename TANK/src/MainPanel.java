
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import net.java.balloontip.BalloonTip;

@SuppressWarnings("serial")
public class MainPanel extends JPanel implements Runnable{
	
	private JButton startButton = new JButton("Sakt speli");
	private JLabel healthLable = new JLabel("Dzivibas ");
	private JLabel scoreLable = new JLabel("Punkti ");
	private JLabel calcScoreLable = new JLabel(" ");
	private JLabel totalScoreLable = new JLabel("Kopejie punkti ");
	private JButton nextButton = new JButton("Nakosais limenis");
	private JButton endButton = new JButton("Uz galveno izvelni");
	private JLabel nameLable = new JLabel("Ievadiet savu niku: ");
	private JTextField nameTextField = new JTextField();
	
	private int currentMap;
	
	private final int FPS = 60;

	private long startTime;
	private long currentTime;
	private long totalTime;
	private long waitTime;
	private long frameCount;
	
	private boolean isRunning;
	
	public static Tank tank;
	public static Map map;
	public static ArrayList<Enemy> enemies = new ArrayList<Enemy>();
	
	private ArrayList<String> mapList = new ArrayList<String>();
	private ArrayList<Enemy> deadList = new ArrayList<Enemy>();
	
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
		
		mapList.add("Maps//map1.txt");
		//mapList.add("Maps//map2.txt");
		//mapList.add("Maps//map3.txt");
		
		currentMap = 0;
		changeMap();
		
		if(thread == null){
			thread = new Thread(this);
			thread.start();
		}
	}
	
	private void guiSetUp(){
		healthLable.setBounds(5, 515, 200, 30);
		healthLable.setFont(new Font("Arial", Font.BOLD, 16));
		healthLable.setForeground(new Color(255, 255, 255));
		this.add(healthLable);
		
		scoreLable.setBounds(210, 515, 200, 30);
		scoreLable.setFont(new Font("Arial", Font.BOLD, 16));
		scoreLable.setForeground(new Color(255, 255, 255));
		this.add(scoreLable);
		
		startButton.setBounds(150, 50, 200, 60);
		startButton.setFont(new Font("Arial", Font.BOLD, 16));
		startButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				if(!nameTextField.getText().isEmpty()){
					changeGameState(GameStates.MainGame);
				}else{
					@SuppressWarnings("unused")
					BalloonTip tip = new BalloonTip(nameTextField, "Ievadiet niku!");
				}
			}
		});
		this.add(startButton);
		
		nameLable.setBounds(150, 165, 200, 30);
		nameLable.setFont(new Font("Arial", Font.BOLD, 16));
		nameLable.setForeground(new Color(255, 255, 255));
		this.add(nameLable);
		
		nameTextField.setBounds(150, 200, 200, 30);
		nameTextField.setFont(new Font("Arial", Font.BOLD, 16));
		this.add(nameTextField);
		
		calcScoreLable.setBounds(150, 50, 200, 30);
		calcScoreLable.setFont(new Font("Arial", Font.BOLD, 16));
		calcScoreLable.setForeground(new Color(255, 255, 255));
		this.add(calcScoreLable);
		
		totalScoreLable.setBounds(150, 80, 200, 30);
		totalScoreLable.setFont(new Font("Arial", Font.BOLD, 16));
		totalScoreLable.setForeground(new Color(255, 255, 255));
		this.add(totalScoreLable);
		
		nextButton.setBounds(150, 150, 200, 30);
		nextButton.setFont(new Font("Arial", Font.BOLD, 16));
		nextButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				changeGameState(GameStates.MainGame);
			}
		});
		this.add(nextButton);

		endButton.setBounds(150, 150, 200, 30);
		endButton.setFont(new Font("Arial", Font.BOLD, 16));
		endButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				changeGameState(GameStates.MainMenu);
			}
		});
		this.add(endButton);
	}
	
	private void init(){
		database = new Database();
		database.connect();
		
		tank = new Tank(map.getTankSpawnPoint());
		this.addKeyListener(tank);
		
		for(Spawner s : map.getSpawnerList()){
			s.spawn();
		}
		
		isRunning = true;
	}
	
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		if(currentGameState == GameStates.MainGame){
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
					deadList.clear();
					changeMap();
					changeGameState(GameStates.LevelFinished);
				}else if(emptySpawnerCounter == map.getSpawnerList().size() && currentMap >= mapList.size()){
					database.write(nameTextField.getText(), tank.getScore());
					
					currentMap = 0;
					changeMap();
					changeGameState(GameStates.EndScreen);
				}else if(tank.isDead()){
					database.write(nameTextField.getText(), tank.getScore());
					
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
					Thread.sleep(10);
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
				startButton.setVisible(true);
				nameLable.setVisible(true);
				nameTextField.setVisible(true);
				scoreLable.setVisible(false);
				healthLable.setVisible(false);
				calcScoreLable.setVisible(false);
				totalScoreLable.setVisible(false);
				nextButton.setVisible(false);
				endButton.setVisible(false);
				break;
			case MainGame:
				currentGameState = state;
				startButton.setVisible(false);
				nameLable.setVisible(false);
				nameTextField.setVisible(false);
				scoreLable.setVisible(true);
				healthLable.setVisible(true);
				calcScoreLable.setVisible(false);
				totalScoreLable.setVisible(false);
				nextButton.setVisible(false);
				endButton.setVisible(false);
				break;
			case LevelFinished:
				currentGameState = state;
				startButton.setVisible(false);
				nameLable.setVisible(false);
				nameTextField.setVisible(false);
				scoreLable.setVisible(false);
				healthLable.setVisible(false);
				calcScoreLable.setVisible(true);
				totalScoreLable.setVisible(true);
				nextButton.setVisible(true);
				endButton.setVisible(false);
				calcScoreLable.setText(tank.getScore() + " * " + tank.getCurHp() + " = " + tank.getScore() * tank.getCurHp());
				tank.setScore(tank.getScore() * tank.getCurHp());
				totalScoreLable.setText("Kopejie punkti " + String.format("%08d", tank.getScore()));
				tank.setLocation(map.getTankSpawnPoint());
				break;
			case EndScreen:
				currentGameState = state;
				startButton.setVisible(false);
				nameLable.setVisible(false);
				nameTextField.setVisible(false);
				scoreLable.setVisible(false);
				healthLable.setVisible(false);
				calcScoreLable.setVisible(true);
				totalScoreLable.setVisible(true);
				nextButton.setVisible(false);
				endButton.setVisible(true);
				calcScoreLable.setText(tank.getScore() + " * " + tank.getCurHp() + " = " + tank.getScore() * tank.getCurHp());
				tank.setScore(tank.getScore() * tank.getCurHp());
				totalScoreLable.setText("Kopejie punkti " + String.format("%08d", tank.getScore()));
				tank.reset();
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
