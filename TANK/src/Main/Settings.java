package Main;

public enum Settings {
	framesPerSecond (60),
	width  (486),
	height (570),
	scale  (1);
	
	private final int value;
	
	Settings(int value) {
        this.value = value;
    }
	
	public int value() { return value; }
}
