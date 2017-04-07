package Main;

import java.awt.Graphics;

import javax.swing.JPanel;

import GameStates.GameStateManager;
import GameStates.IGameStateObserver;
import UI.UserInterface;

public abstract class GameCore implements IGameStateObserver {

	protected UserInterface ui = new UserInterface();

	protected JPanel window;

	public GameCore() {
		GameStateManager.addObserver(ui);
	}

	public void setWindow(JPanel panel) {
		window = panel;
		ui.guiSetUp(window);
	}

	public UserInterface getUI() {
		return ui;
	}

	public abstract void setup();

	public abstract void draw(Graphics g);

	public abstract void update();
}
