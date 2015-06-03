
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
	private JLabel healthLable = new JLabel("Dzivibas ");
	private JLabel scoreLable = new JLabel("Punkti ");
	private JLabel calcScoreLable = new JLabel(" ");
	private JLabel totalScoreLable = new JLabel();
	private JButton nextButton = new JButton("Nakosais limenis");
	private JButton endButton = new JButton("Uz galveno izvelni");
	private JLabel nameLable = new JLabel("Ievadiet savu niku: ");
	private JTextField nameTextField = new JTextField();
	private JLabel titleLable = new JLabel();
	
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
			
		guiSetUp();
		
		changeGameState(GameStates.MainMenu);

		File mapFolder = new File("Maps//");
		getFiles(mapFolder);
		
		currentMap = 0;
		changeMap();
		
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
				if(!nameTextField.getText().isEmpty()){
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
		
		nameLable.setBounds(156, 315, 200, 30);
		nameLable.setFont(new Font("Arial", Font.BOLD, 16));
		nameLable.setForeground(new Color(255, 255, 255));
		this.add(nameLable);
		
		nameTextField.setBounds(156, 350, 200, 30);
		nameTextField.setFont(new Font("Arial", Font.BOLD, 16));
		nameTextField.setBorder(javax.swing.BorderFactory.createEmptyBorder());
		this.add(nameTextField);
		
		healthLable.setBounds(5, 515, 200, 30);
		healthLable.setFont(new Font("Arial", Font.BOLD, 16));
		healthLable.setForeground(new Color(255, 255, 255));
		this.add(healthLable);
		
		scoreLable.setBounds(210, 515, 200, 30);
		scoreLable.setFont(new Font("Arial", Font.BOLD, 16));
		scoreLable.setForeground(new Color(255, 255, 255));
		this.add(scoreLable);
		
		calcScoreLable.setBounds(156, 200, 200, 30);
		calcScoreLable.setFont(new Font("Arial", Font.BOLD, 16));
		calcScoreLable.setForeground(new Color(255, 255, 255));
		this.add(calcScoreLable);
		
		totalScoreLable.setBounds(156, 250, 200, 30);
		totalScoreLable.setFont(new Font("Arial", Font.BOLD, 16));
		totalScoreLable.setForeground(new Color(255, 255, 255));
		this.add(totalScoreLable);
		
		nextButton.setBounds(156, 350, 200, 30);
		nextButton.setFont(new Font("Arial", Font.BOLD, 16));
		nextButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				changeGameState(GameStates.MainGame);
			}
		});
		this.add(nextButton);

		endButton.setBounds(156, 350, 200, 30);
		endButton.setFont(new Font("Arial", Font.BOLD, 16));
		endButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				changeGameState(GameStates.MainMenu);
			}
		});
		this.add(endButton);
		
		titleLable.setBounds(0, 0, 512, 582);
		titleLable.setIcon(new ImageIcon("Images//tank_title.png"));
		this.add(titleLable);
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
		database = new Database();
		database.connect();
		
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
					for(Enemy e : enemies){
						e.die();
					}
					
					enemies.clear();
					deadList.clear();
					
					currentMap = 0;
					changeMap();
					changeGameState(GameStates.EndScreen);
				}else if(tank.isDead()){
					for(Enemy e : enemies){
						e.die();
					}
					
					enemies.clear();
					deadList.clear();
					
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
				titleLable.setVisible(true);
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
				titleLable.setVisible(false);
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
				titleLable.setVisible(true);
				startButton.setVisible(false);
				nameLable.setVisible(false);
				nameTextField.setVisible(false);
				scoreLable.setVisible(false);
				healthLable.setVisible(false);
				calcScoreLable.setVisible(true);
				totalScoreLable.setVisible(true);
				nextButton.setVisible(true);
				endButton.setVisible(false);
				calcScoreLable.setText(tank.getScore() + " * " + tank.getCurHp() + " = ");
				tank.setScore(tank.getScore() * tank.getCurHp());
				totalScoreLable.setText(String.format("%08d", tank.getScore()));
				tank.setLocation(map.getTankSpawnPoint());
				break;
			case EndScreen:
				currentGameState = state;
				titleLable.setVisible(true);
				startButton.setVisible(false);
				nameLable.setVisible(false);
				nameTextField.setVisible(false);
				scoreLable.setVisible(false);
				healthLable.setVisible(false);
				calcScoreLable.setVisible(true);
				totalScoreLable.setVisible(true);
				endButton.setVisible(true);
				calcScoreLable.setText(tank.getScore() + " * " + tank.getCurHp() + " = ");
				tank.setScore(tank.getScore() * tank.getCurHp());
				totalScoreLable.setText(String.format("%08d", tank.getScore()));
				database.write(tank.getName(), tank.getScore());
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
