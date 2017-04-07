package Main;

import java.awt.Graphics;
import java.util.ArrayList;
import java.util.stream.Stream;

import Objects.Enemy;

public class EnemyManager {
	public static ArrayList<Enemy> enemies = new ArrayList<>();

	public static void draw(Graphics g) {
		if (!enemies.isEmpty()) {
			enemies.stream()
						.filter(obj -> !obj.isDead())
						.forEach(obj -> obj.draw(g));
		}
	}

	public static Stream<Enemy> update() {
		if (!enemies.isEmpty()) {
			return enemies.stream()
						.filter(obj -> !obj.isDead());
		}
		return null;
	}
}
