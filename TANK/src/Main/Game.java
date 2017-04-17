package Main;

import java.awt.Graphics;
import java.io.File;
import java.util.ArrayList;
import java.util.stream.Collectors;

import Blocks.Spawner;
import GameStates.GameStateManager;
import GameStates.GameStates;
import Objects.Enemy;
import Objects.GameObject;
import Objects.IDamagable;
import Objects.IronBird;
import Objects.Tank;

public class Game extends GameCore {

	public static IronBird bird;
	public static Tank tank;
	public static Map map = new Map();

	private ArrayList<IDamagable> deadObjects = new ArrayList<IDamagable>();

	private Database database;

	@Override
	public void setup() {
		GameStateManager.addObserver(this);
		GameStateManager.setState(GameStates.MainMenu); // default: MainMenu

		map.getFiles(new File("Maps//"));
		map.changeMap();

		bird = new IronBird(map.getIronBirdSpawnPoint());
		tank = new Tank(map.getTankSpawnPoint());
		window.addKeyListener(tank);

		map.getWorld()
					.filter(obj -> obj instanceof Spawner)
					.map(obj -> (Spawner) obj)
					.filter(obj -> obj.canSpawn())
					.forEach(obj -> map.add(obj.spawn()));

		map.add(bird);
		map.add(tank);
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
			map.getWorld()
						.filter(obj -> obj instanceof Spawner)
						.map(obj -> (Spawner) obj)
						.filter(obj -> obj.canSpawn())
						.forEach(obj -> map.add(obj.spawn()));

			int emptySpawnerCounter = (int) map.getWorld()
						.filter(obj -> obj instanceof Spawner)
						.map(obj -> (Spawner) obj)
						.filter(s -> s.isEmpty())
						.count();

			int spawnerSize = (int) map.getWorld()
						.filter(obj -> obj instanceof Spawner)
						.map(obj -> (Spawner) obj)
						.count();

			if (emptySpawnerCounter == spawnerSize) {
				GameStateManager.setState(GameStates.LevelFinished);
			} else {
				tank.control();
				tank.collisionCheck();

				EnemyManager.update()
							.forEach(enemy -> {
								enemy.control();
								enemy.collisionCheck();
								enemy.pathing();
							});

				map.getWorld()
							.filter(obj -> obj instanceof IDamagable)
							.map(obj -> (IDamagable) obj)
							.filter(obj -> obj.isDead())
							.collect(Collectors.toCollection(() -> deadObjects));

				if (!deadObjects.isEmpty()) {
					for (IDamagable o : deadObjects) {
						map.remove((GameObject) o);
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

			map.changeMap();

			bird = new IronBird(map.getIronBirdSpawnPoint());
			tank.setScore(tank.getScore() * tank.getCurHp());
			tank.setLocation(map.getTankSpawnPoint());

			map.getWorld()
						.filter(obj -> obj instanceof Spawner)
						.map(obj -> (Spawner) obj)
						.filter(obj -> obj.canSpawn())
						.forEach(obj -> map.add(obj.spawn()));

			map.add(bird);
			map.add(tank);
			break;
		case EndScreen:
			tank.setScore(tank.getScore() * tank.getCurHp());
			database.write(tank.getName(), tank.getScore());

			EnemyManager.enemies.clear();
			deadObjects.clear();

			map.changeMap();

			bird = new IronBird(map.getIronBirdSpawnPoint());
			tank = new Tank(map.getTankSpawnPoint());
			window.addKeyListener(tank);

			map.getWorld()
						.filter(obj -> obj instanceof Spawner)
						.map(obj -> (Spawner) obj)
						.filter(obj -> obj.canSpawn())
						.forEach(obj -> map.add(obj.spawn()));

			map.add(bird);
			map.add(tank);
			break;
		default:
			break;
		}
	}
}
