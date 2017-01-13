package Main;
import java.awt.Dimension;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.RepaintManager;

public class Main{
	
	private JFrame frame = new JFrame("Tank");
	
	public Main(){
		frame.setPreferredSize(new Dimension(Settings.width.value(), Settings.height.value()));
		frame.setResizable(false);
		frame.setLayout(null);
		frame.setContentPane(new MainPanel());
		RepaintManager.currentManager(frame).setDoubleBufferingEnabled(true);
		frame.addWindowListener(new WindowAdapter() {
		       @Override
			public void windowClosing(WindowEvent windowEvent){
		          System.exit(0);
		       }
		    });
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}
	
	public static void main(String[] args) {
		new Main();
	}
}
