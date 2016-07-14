import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

public class IronBird extends GameObject{
	
	private int curHp;
	private int maxHp;
	
	public IronBird(int posX, int posY){
		super(posX, posY, "Iron Bird", "Images//Bird01.png");
		
		maxHp = 50;
		curHp = maxHp;
	}
		
	public IronBird(Block pos){
		super(pos.getX(), pos.getY(), "Iron Bird", "Images//Bird01.png");
		
		maxHp = 50;
		curHp = maxHp;
	}
	
	public void draw(Graphics g){
		Graphics2D g2d = (Graphics2D) g;
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		
		if(this.getImage() != null){
			g2d.drawImage(this.getImage(), this.getX() * 32, this.getY() * 32, null);
		}
	}
	
	public void recieveDamage(int damage){
		curHp -= damage;
	}
	
	public boolean isDead(){
		if(curHp <= 0){
			return true;
		}else{
			return false;
		}
	}
}
