package Main;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Toolkit;

import javax.swing.JPanel;

@SuppressWarnings("serial")
public class MainPanel extends JPanel implements Runnable {

	private GameCore game = new Game();

	private long startTime;
	private long currentTime;
	private long totalTime;
	private long waitTime;
	private long frameCount;

	private boolean isRunning;

	private Thread thread;

	public MainPanel() {
		this.setLayout(null);
		this.setFocusable(true);
		this.requestFocus();
		this.setDoubleBuffered(true);
		this.setBackground(new Color(0, 0, 0));

		game.setWindow(this);

		game.setup();

		isRunning = true;

		if (thread == null) {
			thread = new Thread(this);
			thread.start();
		}
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);

		game.draw(g);
	}

	@Override
	public void run() {
		while (isRunning) {
			startTime = System.currentTimeMillis();
			Toolkit.getDefaultToolkit().sync();

			// ----------------------------------- Speles darbibas kods
			game.update();
			// ----------------------------------- Speles darbibas koda beigas

			this.repaint();

			currentTime = System.currentTimeMillis() - startTime;
			waitTime = (1000 / Settings.framesPerSecond) - currentTime;
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
				game.getUI().updateFps(realFPS);
				// System.out.println("fps: " + realFPS);
				frameCount = 0;
				totalTime = 0;
			}
		}
		// database.disconnect();
	}
}
