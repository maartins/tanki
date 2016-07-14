import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

public class NavTile extends GameObject{

	private int x;
	private int y;
	private int value;
	
	private boolean isBlocking;
	
	private NavTile parent;

	public NavTile(int x, int y, boolean isBlocking){
		super(x, y, "navtile");
		this.x = x;
		this.y = y;
		
		value = 0;
		
		this.isBlocking = isBlocking;
		
		parent = null;
	}
	
	public void draw(Graphics g){
		Graphics2D g2d = (Graphics2D) g;
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		
		if(this.getImage() != null){
			g2d.drawImage(this.getImage(), this.getX() * 32, this.getY() * 32, null);
		}
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
	
	public void setImage(String path){
		this.setImage(path);
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
