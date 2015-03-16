import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;



public class Bullet{
	
	private int x;
	private int y;
	private int dx;
	private int dy;
	
	private final long test = 1000000000;
	
	private final int SPEED = 2;
	
	private BufferedImage image;
	
	private int curDirection;
	
	private final int MAXDIST = 150;
	private boolean hit;
	
	public Bullet(int posX, int posY, int curDir){
		x = posX;
		y = posY;
		dx = posX;
		dy = posY;
		
		curDirection = curDir;
		
		hit = false;
		
		try{
			image = ImageIO.read(new File("Images//Test03.png"));
		}catch(Exception e){
			System.out.println("Failed to load image.");
		}
	}
	
	public void draw(Graphics g){
		Graphics2D g2d = (Graphics2D) g;
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2d.drawImage(image, x, y, null);
	}
	
	public void move(){
		switch(curDirection){
		case 0:
			x += SPEED;
			if(x - dx >= MAXDIST){
				hit = true;
			}
			break;
		case 1:
			y -= SPEED;
			if(y - dy <= -MAXDIST){
				hit = true;
			}
			break;
		case 2:
			x -= SPEED;
			if(x - dx <= -MAXDIST){
				hit = true;
			}
			break;
		case 3:
			y += SPEED;
			if(y - dy >= MAXDIST){
				hit = true;
			}
			break;
		}
	}
	
	public boolean hit(){
		return hit;
	}
}
