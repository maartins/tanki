package GameStates;

public enum GameStates {
	MainMenu(new MainMenu()), MainGame(new MainGame()), LevelFinished(new LevelFinished()), EndScreen(new EndScreen());

	private final IGameState type;

	private GameStates(IGameState type) {
		this.type = type;
	}

	public IGameState getType() {
		return this.type;
	}
}
