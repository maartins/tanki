package Main;

import java.awt.Graphics;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Stream;

import Blocks.Block;
import Blocks.Floor;
import Blocks.PwrUpSuperBullet;
import Blocks.SolidWall;
import Blocks.Spawner;
import Blocks.Wall;
import Objects.GameObject;
import Pathfinding.AStarPathing;
import Pathfinding.NavTile;

public class Map {

	private int currentMap = 0;

	private ArrayList<String> mapFiles = new ArrayList<String>();

	private CopyOnWriteArrayList<GameObject> worldObjects = new CopyOnWriteArrayList<GameObject>();

	private NavTile[][] navMap = new NavTile[15][15];

	private Block tankSpawnPoint;
	private Block ironBirdSpawnPoint;

	public Map() {
	}

	private void makeMap() {
		BufferedReader bufReader = null;

		try {
			String curLine;
			bufReader = new BufferedReader(new FileReader(mapFiles.get(currentMap)));
			int blockSize = 32;
			int globalCounter = 0;

			int i = 0;
			while ((curLine = bufReader.readLine()) != null) {
				int j = 0;
				for (char c : curLine.toCharArray()) {
					if (c == '#') {
						worldObjects.add(new Floor(j * blockSize, i * blockSize));
						worldObjects.add(new Wall(j * blockSize, i * blockSize));
						navMap[i][j] = new NavTile(j * blockSize, i * blockSize, true);
						navMap[i][j].setName(globalCounter + "  wall");
					} else if (c == '%') {
						worldObjects.add(new SolidWall(j * blockSize, i * blockSize));
						navMap[i][j] = new NavTile(j * blockSize, i * blockSize, true);
						navMap[i][j].setName(globalCounter + " swall");
					} else if (c == ' ') {
						worldObjects.add(new Floor(j * blockSize, i * blockSize));
						navMap[i][j] = new NavTile(j * blockSize, i * blockSize, false);
						navMap[i][j].setName(globalCounter + " floor");
					} else if (c == 's') {
						worldObjects.add(new Spawner(j * blockSize, i * blockSize));
						worldObjects.add(new Floor(j * blockSize, i * blockSize));
						navMap[i][j] = new NavTile(j * blockSize, i * blockSize, false);
						navMap[i][j].setName(globalCounter + " spawn");
					} else if (c == 't') {
						tankSpawnPoint = new Floor(j * blockSize, i * blockSize);
						worldObjects.add(tankSpawnPoint);
						navMap[i][j] = new NavTile(j * blockSize, i * blockSize, false);
						navMap[i][j].setName(globalCounter + "  tank");
					} else if (c == '1') {
						worldObjects.add(new PwrUpSuperBullet(j * blockSize, i * blockSize));
						navMap[i][j] = new NavTile(j * blockSize, i * blockSize, false);
						navMap[i][j].setName(globalCounter + " pwrup");
					} else if (c == 'b') {
						ironBirdSpawnPoint = new Floor(j * blockSize, i * blockSize);
						worldObjects.add(ironBirdSpawnPoint);
						navMap[i][j] = new NavTile(j * blockSize, i * blockSize, true);
						navMap[i][j].setName(globalCounter + "  bird");
					} else {
						// never
						tankSpawnPoint = new Floor(j * blockSize, i * blockSize);
						worldObjects.add(tankSpawnPoint);
						navMap[i][j] = new NavTile(j * blockSize, i * blockSize, false);
						navMap[i][j].setName(globalCounter + "  none");
					}
					j++;
					globalCounter++;
				}
				i++;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void changeMap() {
		worldObjects = new CopyOnWriteArrayList<GameObject>();
		navMap = new NavTile[15][15];

		if (currentMap > mapFiles.size() - 1) {
			currentMap = 0;
		}

		makeMap();

		AStarPathing.setNavMap(navMap);

		currentMap++;
	}

	public void getFiles(File folder) {
		for (File f : folder.listFiles()) {
			mapFiles.add(folder.getName() + "//" + f.getName());
		}
	}

	public void draw(Graphics g) {
		worldObjects.stream()
					.filter(obj -> obj instanceof Block)
					.forEach(obj -> obj.draw(g));

		for (NavTile[] array : navMap) {
			for (NavTile n : array) {
				n.draw(g);
			}
		}
	}

	public void add(GameObject obj) {
		worldObjects.add(obj);
	}

	public void remove(GameObject obj) {
		worldObjects.remove(obj);
	}

	public ArrayList<String> getMapList() {
		return mapFiles;
	}

	public Stream<GameObject> getWorld() {
		return worldObjects.stream();
	}

	public NavTile[][] navMap() {
		return navMap;
	}

	public Block getTankSpawnPoint() {
		return tankSpawnPoint;
	}

	public Block getIronBirdSpawnPoint() {
		return ironBirdSpawnPoint;
	}
}
