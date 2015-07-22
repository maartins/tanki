
public class NavTile {

	private int x;
	private int y;
	private int value;
	
	private boolean isBlocking;
	
	private NavTile parent;

	public NavTile(int x, int y, boolean isBlocking){
		this.x = x;
		this.y = y;
		
		value = 0;
		
		this.isBlocking = isBlocking;
		
		parent = null;
	}
	
	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}

	public boolean isBlocking() {
		return isBlocking;
	}

	public void setBlocking(boolean isBlocking) {
		this.isBlocking = isBlocking;
	}

	public NavTile getParent() {
		return parent;
	}

	public void setParent(NavTile parent) {
		this.parent = parent;
	}
	
	public void reset(){
		value = 0;
	}
	
	public String toString(){
		return "nav tile at " + x + "x " + y + "y" + value + " value";
	}
}
