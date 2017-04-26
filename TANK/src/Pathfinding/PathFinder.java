package Pathfinding;

import java.util.ArrayList;
import java.util.concurrent.Callable;

public class PathFinder implements Callable<ArrayList<NavTile>> {

	private NavTile enemyPos;
	private NavTile tankPos;
	private NavTile birdPos;

	private NavTile[][] navMap;

	private ArrayList<NavTile> closedList = new ArrayList<NavTile>();
	private ArrayList<NavTile> openList = new ArrayList<NavTile>();
	private ArrayList<NavTile> navList = new ArrayList<NavTile>();

	public PathFinder(NavTile enemyPos, NavTile tankPos, NavTile birdPos, NavTile[][] navMap) {
		this.enemyPos = enemyPos;
		this.tankPos = tankPos;
		this.birdPos = birdPos;
		this.navMap = navMap;
	}

	@Override
	public ArrayList<NavTile> call() {
		boolean isPathDone = false;

		NavTile targetPos;

		float tankValue = ((Math.abs((enemyPos.getTileX()) - tankPos.getTileX()))
					+ Math.abs(enemyPos.getTileY() - tankPos.getTileY())) + 10;
		float birdValue = ((Math.abs((enemyPos.getTileX()) - birdPos.getTileX()))
					+ Math.abs(enemyPos.getTileY() - birdPos.getTileY())) + 10;
		//System.out.println("Tank: " + tankValue + " Bird: " + birdValue);

		//System.out.println(enemyPos.getName() + " " + enemyPos.getTileX() + " " + enemyPos.getTileY());
		//System.out.println(birdPos.getName() + " " + birdPos.getTileX() + " " + birdPos.getTileY());
		//System.out.println(tankPos.getName() + " " + tankPos.getTileX() + " " + tankPos.getTileY());

		if (tankValue > birdValue) {
			targetPos = birdPos;
			enemyPos.setValue(birdValue);
		} else {
			targetPos = tankPos;
			enemyPos.setValue(tankValue);
		}

		closedList.add(targetPos); // important: set iterators to 1 in closed list loops
		closedList.add(enemyPos);

		while (!isPathDone) {
			NavTile tempb = null;

			for (int i = 1; i < closedList.size(); i++) {
				tempb = navMap[closedList.get(i)
							.getTileY() + 1][closedList.get(i)
										.getTileX()];

				if (isValidForValue(tempb, closedList)) {
					tempb.setValue(valueTile(tempb, targetPos));
					tempb.setImage("Images//Nav02.png");

					openList.add(tempb);
				}

				tempb = navMap[closedList.get(i)
							.getTileY() - 1][closedList.get(i)
										.getTileX()];

				if (isValidForValue(tempb, closedList)) {
					tempb.setValue(valueTile(tempb, targetPos));
					tempb.setImage("Images//Nav02.png");

					openList.add(tempb);
				}
				tempb = navMap[closedList.get(i)
							.getTileY()][closedList.get(i)
										.getTileX() + 1];

				if (isValidForValue(tempb, closedList)) {
					tempb.setValue(valueTile(tempb, targetPos));
					tempb.setImage("Images//Nav02.png");

					openList.add(tempb);
				}

				tempb = navMap[closedList.get(i)
							.getTileY()][closedList.get(i)
										.getTileX() - 1];

				if (isValidForValue(tempb, closedList)) {
					tempb.setValue(valueTile(tempb, targetPos));
					tempb.setImage("Images//Nav02.png");

					openList.add(tempb);
				}
			}

			// System.out.println(openList.get(0));

			if (!openList.isEmpty()) {
				//System.out.println("CALCULATE BLOCK WIEGHT: " + openList.size());

				NavTile tempBlock = openList.get(0);

				//System.out.println("----------------------------------");
				//for (NavTile b : closedList) {
				//	System.out.println(b);
				//}

				for (NavTile b : openList) {
					// System.out.println(b);
					if (b.getValue() <= tempBlock.getValue()) {
						tempBlock = b;
					}
				}

				tempBlock.setImage("Images//Nav01.png");

				closedList.add(tempBlock);

				openList.clear();

				if (tempBlock.getTileX() + 1 == targetPos.getTileX() && tempBlock.getTileY() == targetPos.getTileY()) {
					isPathDone = true;
					// System.out.println("PATHING DONE " + targetPos);
				} else if (tempBlock.getTileX() - 1 == targetPos.getTileX()
							&& tempBlock.getTileY() == targetPos.getTileY()) {
					isPathDone = true;
					// System.out.println("PATHING DONE " + targetPos);
				} else if (tempBlock.getTileY() + 1 == targetPos.getTileY()
							&& tempBlock.getTileX() == targetPos.getTileX()) {
					isPathDone = true;
					// System.out.println("PATHING DONE " + targetPos);
				} else if (tempBlock.getTileY() - 1 == targetPos.getTileY()
							&& tempBlock.getTileX() == targetPos.getTileX()) {
					isPathDone = true;
					// System.out.println("PATHING DONE " + targetPos);
				}
			}

			if (isPathDone) {
				navList.add(closedList.get(closedList.size() - 1));

				NavTile tester = closedList.get(closedList.size() - 1);
				for (int i = closedList.size() - 2; i > 0; i--) {
					if (testIfNeighbor(tester, closedList.get(i))) {
						// System.out.println(tester);
						navList.add(tester);
						tester = closedList.get(i);
					}
				}

				//System.out.println("NAV LIST DONE");
				for (NavTile n : navList) {
					n.setImage("Images//Nav03.png");
					// System.out.println(n);
				}
			}
		}

		return navList;
	}

	private static synchronized boolean testIfNeighbor(NavTile current, NavTile next) {
		int downY = current.getTileY() + 1;
		int downX = current.getTileX();

		int upY = current.getTileY() - 1;
		int upX = current.getTileX();

		int rightY = current.getTileY();
		int rightX = current.getTileX() + 1;

		int leftY = current.getTileY();
		int leftX = current.getTileX() - 1;

		if (downY == next.getTileY() & downX == next.getTileX()) {
			return true;
		} else if (upY == next.getTileY() & upX == next.getTileX()) {
			return true;
		} else if (rightY == next.getTileY() & rightX == next.getTileX()) {
			return true;
		} else if (leftY == next.getTileY() & leftX == next.getTileX()) {
			return true;
		} else {
			return false;
		}
	}

	private static synchronized float valueTile(NavTile testable, NavTile traget) {
		return ((Math.abs((testable.getTileX()) - traget.getTileX()))
					+ Math.abs(testable.getTileY() - traget.getTileY())) + 10;
	}

	private static synchronized boolean isValidForValue(NavTile testable, ArrayList<NavTile> closedList) {
		if (!closedList.contains(testable)) {
			if (!testable.isSolid()) {
				return true;
			}
		}

		return false;
	}
}
