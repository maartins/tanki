package Main;

import java.awt.Graphics;

import javax.swing.JPanel;

public interface IGame {
	public void setup(JPanel window);

	public void draw(Graphics g);

	public void update();
}
