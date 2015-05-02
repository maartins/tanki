import java.awt.BorderLayout;
import java.awt.event.*;

import javax.swing.*;


public class Main{
	public static void main(String[] args) {
		JFrame frame = new JFrame("Tank");
		frame.setLocation(20, 20);
		frame.setSize(518, 582);
		frame.setResizable(false);
		frame.setLayout(new BorderLayout());
		frame.add(new MainPanel(), BorderLayout.CENTER);
		RepaintManager.currentManager(frame).setDoubleBufferingEnabled(true);
		frame.addWindowListener(new WindowAdapter() {
		       public void windowClosing(WindowEvent windowEvent){
		          System.exit(0);
		       }        
		    });
		frame.add(new StatPanel(), BorderLayout.SOUTH);
		frame.setVisible(true);
	}
}
