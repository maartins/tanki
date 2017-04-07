package GameStates;

import java.util.ArrayList;

public class GameStateManager {
	private static GameStates state;
	private static ArrayList<IGameStateObserver> observers = new ArrayList<IGameStateObserver>();

	private GameStateManager() {
		state = null;
	}

	public static void addObserver(IGameStateObserver observer) {
		observers.add(observer);
	}

	public static void setState(GameStates state) {

		GameStateManager.state = state;

		for (IGameStateObserver obs : observers) {
			obs.doAction(GameStateManager.state);
		}
	}

	public static GameStates getState() {
		return state;
	}
}
