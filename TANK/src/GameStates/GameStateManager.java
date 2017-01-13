package GameStates;

import java.util.ArrayList;

public class GameStateManager {
	private GameStates state;
	private ArrayList<IGameStateObserver> observers = new ArrayList<IGameStateObserver>();

	public GameStateManager() {
		state = null;
	}

	public void addObserver(IGameStateObserver observer) {
		observers.add(observer);
	}

	public void setState(GameStates state) {

		this.state = state;

		for (IGameStateObserver obs : observers) {
			obs.doAction(state);
		}
	}

	public GameStates getState() {
		return state;
	}
}
