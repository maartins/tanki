import java.awt.Dimension;
import java.awt.event.*;
import javax.swing.*;

public class Main{
	
	private JFrame frame = new JFrame("Tank");
	
	public Main(){
		frame.setLocation(20, 20);
		frame.setPreferredSize(new Dimension(518, 582));
		frame.setResizable(false);
		frame.setLayout(null);
		frame.setContentPane(new MainPanel());
		RepaintManager.currentManager(frame).setDoubleBufferingEnabled(true);
		frame.addWindowListener(new WindowAdapter() {
		       public void windowClosing(WindowEvent windowEvent){
		          System.exit(0);
		       }        
		    });
		frame.pack();
		frame.setVisible(true);
	}
	
	public static void main(String[] args) {
		new Main();
	}
}
