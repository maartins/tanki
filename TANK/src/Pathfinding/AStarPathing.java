package Pathfinding;

import java.util.ArrayList;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class AStarPathing {
	private static NavTile[][] navMap;
	private static ExecutorService pool = Executors.newFixedThreadPool(4);

	public static void setNavMap(NavTile[][] navMap) {
		AStarPathing.navMap = navMap;
	}

	public static Future<ArrayList<NavTile>> findPath(NavTile enemyPos, NavTile tankPos, NavTile birdPos) {
		Callable<ArrayList<NavTile>> task = new PathFinder(enemyPos, tankPos, birdPos, navMap);
		return pool.submit(task);
	}

}
