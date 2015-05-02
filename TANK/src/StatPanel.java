import java.awt.Font;
import java.awt.GridLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;


@SuppressWarnings("serial")
public class StatPanel extends JPanel implements Runnable{
	
	private boolean isRunning;
	
	private JLabel healthLable = new JLabel();
	private JLabel scoreLable = new JLabel();
	
	private Thread thread;
	
	public StatPanel(){
		this.setLayout(new GridLayout(0, 2));
		
		healthLable.setText("Dzivibas " + MainPanel.tank.getCurHp());
		healthLable.setFont(new Font("Arial", Font.BOLD, 16));
		this.add(healthLable);
		scoreLable.setText("Punkti " + String.format("%08d", MainPanel.tank.getScore()));
		scoreLable.setFont(new Font("Arial", Font.BOLD, 16));
		this.add(scoreLable);
		
		if(thread == null){
			thread = new Thread(this);
			thread.start();
		}
		
		isRunning = true;
	}

	@Override
	public void run() {
		while(isRunning){
			
			healthLable.setText("Dzivibas " + MainPanel.tank.getCurHp());
			scoreLable.setText("Punkti " + String.format("%08d", MainPanel.tank.getScore()));
			
			try {
				Thread.sleep(50);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
