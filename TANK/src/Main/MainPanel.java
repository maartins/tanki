package Main;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Toolkit;
import java.io.File;
import java.util.ArrayList;

import javax.swing.JPanel;

import Blocks.Block;
import Blocks.Spawner;
import GameStates.GameStateManager;
import GameStates.GameStates;
import GameStates.IGameStateObserver;
import Objects.Enemy;
import Objects.IDamagable;
import Objects.IronBird;
import Objects.Tank;
import UI.UserInterface;

@SuppressWarnings("serial")
public class MainPanel extends JPanel implements Runnable, IGameStateObserver {

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
	public static ArrayList<IDamagable> damagableObjects = new ArrayList<IDamagable>();

	private ArrayList<IDamagable> deadObjects = new ArrayList<IDamagable>();

	private Database database;

	private GameStateManager gsm;

	private UserInterface ui;

	private Thread thread;

	public MainPanel() {
		this.setLayout(null);
		this.setFocusable(true);
		this.requestFocus();
		this.setDoubleBuffered(true);
		this.setBackground(new Color(0, 0, 0));

		gsm = new GameStateManager();
		gsm.addObserver(this);

		ui = new UserInterface(this, gsm);

		Sound loadShoot = new Sound("Sounds\\tank_shoot01.wav");
		loadShoot.play(-100.0f);

		gsm.setState(GameStates.MainMenu); // default: MainMenu

		database = new Database();
		try {
			database.connect();
		} catch (Exception ex) {

		}

		File folder = new File("Maps//");
		map = new Map();
		map.getFiles(folder);
		map.changeMap();

		bird = new IronBird(map.getIronBirdSpawnPoint());
		tank = new Tank(map.getTankSpawnPoint());
		this.addKeyListener(tank);

		damagableObjects = map.getDamagableObjectList();

		for (Spawner s : map.getSpawnerList()) {
			if (s.canSpawn()) {
				Enemy tmp = s.spawn();
				enemies.add(tmp);
				damagableObjects.add(tmp);
			}
		}

		// for (IDamagable iDamagable : map.getDamagableObjectList()) {
		// damagableObjects.add(iDamagable);
		// }

		damagableObjects.add(bird);
		damagableObjects.add(tank);

		isRunning = true;

		if (thread == null) {
			thread = new Thread(this);
			thread.start();
		}
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);

		if (gsm.getState() == GameStates.MainGame) {
			map.draw(g);
			tank.draw(g);
			bird.draw(g);

			synchronized (enemies) {
				for (Enemy e : enemies) {
					if (!e.isDead()) {
						e.draw(g);
					}
				}
			}
		}
	}

	@Override
	public void run() {
		while (isRunning) {
			startTime = System.currentTimeMillis();
			Toolkit.getDefaultToolkit().sync();

			// ----------------------------------- Speles darbibas kods
			if (gsm.getState() == GameStates.MainGame) {
				int emptySpawnerCounter = 0;

				for (Spawner s : map.getSpawnerList()) {
					if (s.canSpawn()) {
						Enemy tmp = s.spawn();
						enemies.add(tmp);
						damagableObjects.add(tmp);
					}
					if (s.isEmpty()) {
						emptySpawnerCounter++;
					}
				}

				if (emptySpawnerCounter == map.getSpawnerList().size()) {
					gsm.setState(GameStates.LevelFinished);
				} else if (tank.isDead() | bird.isDead()) {
					gsm.setState(GameStates.EndScreen);
				} else {
					tank.control();
					tank.collisionCheck();

					if (!enemies.isEmpty()) {
						for (Enemy e : enemies) {
							// e.pathing();
							// e.control();
							e.collisionCheck();

							if (e.isDead()) {
								tank.setScore(tank.getScore() + 20);
								e.die();
							}
						}
					}

					for (IDamagable b : damagableObjects) {
						if (b.isDead()) {
							deadObjects.add(b);
						}
					}

					if (!deadObjects.isEmpty()) {
						for (IDamagable o : deadObjects) {
							damagableObjects.remove(o);
							if (o instanceof Block) {
								map.getBlockList().remove(o);
							}
							if (o instanceof Enemy) {
								enemies.remove(o);
							}
						}

						deadObjects.clear();
					}

					// healthLable1.setText("Tank HP: " + tank.getCurHp());
					// healthLable2.setText("Bird HP: " + bird.getCurHP());
					// scoreLable.setText("Score: " + String.format("%08d", tank.getScore()));
				}
			}
			// ----------------------------------- Speles darbibas koda beigas

			this.repaint();

			currentTime = System.currentTimeMillis() - startTime;
			waitTime = (1000 / Settings.framesPerSecond.value()) - currentTime;
			try {
				if (waitTime < 0) {
					Thread.sleep(10);
				} else {
					Thread.sleep(waitTime);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}

			++frameCount;
			totalTime += (System.currentTimeMillis() - startTime);
			if (totalTime > 1000) {
				@SuppressWarnings("unused")
				long realFPS = (long) ((double) frameCount / (double) totalTime * 1000.0);
				System.out.println("fps: " + realFPS);
				frameCount = 0;
				totalTime = 0;
			}
		}
		database.disconnect();
	}

	@Override
	public void doAction(GameStates state) {
		switch (state) {
		case MainMenu:
			break;
		case MainGame:
			break;
		case LevelFinished:
			enemies.clear();
			deadObjects.clear();
			damagableObjects.clear();

			map.changeMap();

			damagableObjects = map.getDamagableObjectList();

			bird = new IronBird(map.getIronBirdSpawnPoint());
			damagableObjects.add(bird);

			tank.setScore(tank.getScore() * tank.getCurHp());
			tank.setLocation(map.getTankSpawnPoint());
			damagableObjects.add(tank);

			break;
		case EndScreen:
			tank.setScore(tank.getScore() * tank.getCurHp());
			database.write(tank.getName(), tank.getScore());

			enemies.clear();
			deadObjects.clear();
			damagableObjects.clear();

			map.changeMap();

			damagableObjects = map.getDamagableObjectList();

			bird = new IronBird(map.getIronBirdSpawnPoint());
			damagableObjects.add(bird);

			tank.die();
			tank = new Tank(map.getTankSpawnPoint());
			this.addKeyListener(tank);
			damagableObjects.add(tank);
			break;
		default:
			break;
		}
	}
}
