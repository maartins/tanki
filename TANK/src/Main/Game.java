package Main;

import java.awt.Graphics;
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

public class Game implements IGame, IGameStateObserver {

	public static IronBird bird;
	public static Tank tank;
	public static Map map;
	public static ArrayList<Enemy> enemies = new ArrayList<Enemy>();
	public static ArrayList<IDamagable> damagableObjects = new ArrayList<IDamagable>();

	private ArrayList<IDamagable> deadObjects = new ArrayList<IDamagable>();

	private Database database;

	private GameStateManager gsm;

	private UserInterface ui;
	private JPanel window;

	@Override
	public void setup(JPanel window) {
		this.window = window;

		gsm = new GameStateManager();
		gsm.addObserver(this);

		ui = new UserInterface(window, gsm);

		Sound loadShoot = new Sound("Sounds\\tank_shoot01.wav");

		gsm.setState(GameStates.MainMenu); // default: MainMenu

		File folder = new File("Maps//");
		map = new Map();
		map.getFiles(folder);
		map.changeMap();

		bird = new IronBird(map.getIronBirdSpawnPoint());
		tank = new Tank(map.getTankSpawnPoint());
		window.addKeyListener(tank);

		damagableObjects = map.getDamagableObjectList();

		for (Spawner s : map.getSpawnerList()) {
			if (s.canSpawn()) {
				Enemy tmp = s.spawn();
				enemies.add(tmp);
				damagableObjects.add(tmp);
			}
		}

		damagableObjects.add(bird);
		damagableObjects.add(tank);
	}

	@Override
	public void draw(Graphics g) {
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
	public void update() {
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
			window.addKeyListener(tank);
			damagableObjects.add(tank);
			break;
		default:
			break;
		}
	}
}
