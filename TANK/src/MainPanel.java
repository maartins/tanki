import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Toolkit;

import javax.swing.JPanel;


@SuppressWarnings("serial")
public class MainPanel extends JPanel implements Runnable{
	
	private final int wWidth = 512;
	private final int wHeight = 512;
	
	private Thread thread;
	private boolean isRunning;
	
	private final int FPS = 60;
	private long startTime;
	private long curTime;
	private long totalTime;
	private long frameCount;
	private long waitTime;
	
	private Tank tank;
	
	
	public MainPanel(){
		this.setPreferredSize(new Dimension(wWidth, wHeight));
		
		this.setFocusable(true);
		this.requestFocus();
		this.setDoubleBuffered(true);
		
		if(thread == null){
			thread = new Thread(this);
			thread.start();
		}
	}
	
	private void init(){		
		tank = new Tank(100, 100);
		this.addKeyListener(tank);
		
		
		isRunning = true;
	}
	
	
	
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		tank.draw(g);
	}

	@Override
	public void run() {
		System.out.println("Initialize");
		init();
		while(isRunning){
			startTime = System.currentTimeMillis();
			Toolkit.getDefaultToolkit().sync();
			
			
			tank.control();
			
			this.repaint();
			
			curTime = System.currentTimeMillis() - startTime;
			waitTime = (1000 / FPS) - curTime;
			try{
				if(waitTime < 0){
					Thread.sleep(5);
				}else{
					Thread.sleep(waitTime);
				}
			}catch(Exception e){
				e.printStackTrace();
			}
			++frameCount;
            totalTime += (System.currentTimeMillis() - startTime);
            if (totalTime > 1000) {
                long realFPS = (long) ((double) frameCount/ (double) totalTime * 1000.0);
                System.out.println("fps: " + realFPS);
                frameCount = 0;
                totalTime = 0;
            }
		}
	}
}
