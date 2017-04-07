package Main;

import java.awt.Graphics;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

import Blocks.Block;
import Blocks.Spawner;
import GameStates.GameStateManager;
import GameStates.GameStates;
import Objects.Enemy;
import Objects.IDamagable;
import Objects.IronBird;
import Objects.Tank;

public class Game extends GameCore {

	public static IronBird bird;
	public static Tank tank;
	public static Map map;
	public static CopyOnWriteArrayList<IDamagable> damagableObjects = new CopyOnWriteArrayList<IDamagable>();

	private ArrayList<IDamagable> deadObjects = new ArrayList<IDamagable>();

	private Database database;

	@Override
	public void setup() {
		GameStateManager.addObserver(this);
		GameStateManager.setState(GameStates.MainMenu); // default: MainMenu

		Sound loadShoot = new Sound("Sounds\\tank_shoot01.wav");

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
				damagableObjects.add(s.spawn());
			}
		}

		damagableObjects.add(bird);
		damagableObjects.add(tank);
	}

	@Override
	public void draw(Graphics g) {
		if (GameStateManager.getState() == GameStates.MainGame) {
			map.draw(g);
			tank.draw(g);
			bird.draw(g);

			EnemyManager.draw(g);
		}
	}

	@Override
	public void update() {
		if (GameStateManager.getState() == GameStates.MainGame) {
			List<Spawner> spawners = map.getSpawnerList();

			spawners.stream()
						.filter(s -> s.canSpawn())
						.forEach(s -> damagableObjects.add(s.spawn()));

			int emptySpawnerCounter = (int) spawners.stream()
						.filter(s -> s.isEmpty())
						.count();

			if (emptySpawnerCounter == spawners.size()) {
				GameStateManager.setState(GameStates.LevelFinished);
			} else if (tank.isDead() | bird.isDead()) {
				GameStateManager.setState(GameStates.EndScreen);
			} else {
				tank.control();
				tank.collisionCheck();

				//				if (!EnemyManager.enemies.isEmpty()) {
				//					for (Enemy e : EnemyManager.enemies) {
				//						e.pathing();
				//						e.control();
				//						e.collisionCheck();
				//
				//						if (e.isDead()) {
				//							tank.setScore(tank.getScore() + 20);
				//							e.die();
				//						}
				//					}
				//				}

				EnemyManager.update()
							.forEach(enemy -> {
								enemy.control();
								enemy.collisionCheck();
								//enemy.pathing();
							});

				//for (IDamagable b : damagableObjects) {
				//	if (b.isDead()) {
				//		deadObjects.add(b);
				//	}
				//}

				damagableObjects.stream()
							.filter(obj -> obj.isDead())
							.collect(Collectors.toCollection(() -> deadObjects));

				if (!deadObjects.isEmpty()) {
					for (IDamagable o : deadObjects) {
						damagableObjects.remove(o);
						if (o instanceof Block) {
							map.getBlockList()
										.remove(o);
						}
						if (o instanceof Enemy) {
							EnemyManager.enemies.remove(o);
						}
					}

					deadObjects.clear();
				}

				ui.updateTankHealth(tank.getCurHp());
				ui.updateBirdHealth(bird.getCurHp());
				ui.setScore(tank.getScore());
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
			EnemyManager.enemies.clear();
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

			EnemyManager.enemies.clear();
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
