import java.awt.event.*;

import javax.swing.*;


public class Main{
	public static void main(String[] args) {
		JFrame frame = new JFrame("Tank");
		frame.setResizable(false);
		frame.setContentPane(new MainPanel());
		frame.pack();
		RepaintManager.currentManager(frame).setDoubleBufferingEnabled(true);
		frame.addWindowListener(new WindowAdapter() {
		       public void windowClosing(WindowEvent windowEvent){
		          System.exit(0);
		       }        
		    });
		frame.setVisible(true);
	}
}
