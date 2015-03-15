import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import javax.imageio.ImageIO;


public class Tank implements KeyListener{
	private BufferedImage image;
	
	private int x;
	private int y;
	
	private int veloX;
	private int veloY;
	
	private boolean keyA;
	private boolean keyD;
	private boolean keyW;
	private boolean keyS;
	
	private boolean keySPACE;
	
	private final int UP = 1;
	private final int DOWN = 3;
	private final int LEFT = 2;
	private final int RIGHT = 0;
	
	private int curDirection;
	private int preDirection;
	
	private ArrayList<Bullet> ba;
	
	public Tank(int posX, int posY){
		super();
		
		ba = new ArrayList<Bullet>();
		
		x = posX;
		y = posY;
		
		veloX = 0;
		veloY = 0;
		
		keyA = false;
		keyD = false;
		keyW = false;
		keyS = false;
		
		keySPACE = false;
		
		curDirection = UP;
		preDirection = 0;
		
		try{
			image = ImageIO.read(new File("Images//Test02.png"));
		}catch(Exception e){
			System.out.println("Failed to load image.");
		}
	}
	
	public void draw(Graphics g){
		Graphics2D g2d = (Graphics2D) g;
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2d.drawImage(image, x, y, null);
		
		if(!ba.isEmpty()){
			for(Bullet b : ba){
				b.draw(g);
				b.move();
			}
		}
	}
	
	public void control(){
		if(keyA == keyD){
			veloX = 0;
		}else if(keyA){
			veloX = -1;
		}else if(keyD){
			veloX = 1;
		}
		
		if(keyW == keyS){
			veloY = 0;
		}else if(keyW){
			veloY = -1;
		}else if(keyS){
			veloY = 1;
		}
		
		if(keyA && keyS || keyA && keyW){
			veloX = 0;
			veloY = 0;
		}else if(keyD && keyS || keyD && keyW){
			veloX = 0;
			veloY = 0;			
		}
		
		if(keySPACE){
			shoot();
			System.out.println("SHOOT");
		}else{
			if(!ba.isEmpty() && ba.get(ba.size() - 1).hit()){
				ba.remove(ba.size() - 1);
				//System.out.println("REMOVE BULLET = " + ba.size());
			}
		}
		
		//System.out.println("cur dir: " + curDirection + " prev dir: " + preDirection);
		
		x += veloX;
		y += veloY;
	}
	
	public void shoot(){
		int posX = 0, posY = 0;
		if(curDirection == RIGHT){
			posX = x + image.getWidth();
			posY = y + (image.getHeight() / 2);
			ba.add(new Bullet(posX, posY, curDirection));
			//System.out.println("ADD BULLET" + ba.size());
		}else if(curDirection == UP){
			posX = x + (image.getWidth() / 2);
			posY = y - 2;
			ba.add(new Bullet(posX, posY, curDirection));
		}else if(curDirection == LEFT){
			posX = x - 2;
			posY = y + (image.getHeight() / 2);
			ba.add(new Bullet(posX, posY, curDirection));
		}else if(curDirection == DOWN){
			posX = x + (image.getWidth() / 2);
			posY = y + image.getHeight();
			ba.add(new Bullet(posX, posY, curDirection));
		}
	}
	
	private BufferedImage rotate(BufferedImage img, int cdir, int pdir){
		AffineTransform transform = new AffineTransform();
	    transform.rotate(Math.toRadians(-(cdir - pdir) * 90), img.getWidth()/2, img.getHeight()/2);
	    AffineTransformOp op = new AffineTransformOp(transform, AffineTransformOp.TYPE_BILINEAR);
	    img = op.filter(img, null);
	    return img;
	}
	
	@Override
	public void keyPressed(KeyEvent e) {
		int keyCode = e.getKeyCode();
		switch(keyCode){
		case KeyEvent.VK_A:
			keyA = true;//System.out.println(KeyEvent.VK_A + " " + keyA);
			if(curDirection != LEFT){
				preDirection = curDirection;
				curDirection = LEFT;
				image = rotate(image, curDirection, preDirection);
			}
			break;
		case KeyEvent.VK_D:
			keyD = true;//System.out.println(KeyEvent.VK_D + " " + keyD);
			if(curDirection != RIGHT){
				preDirection = curDirection;
				curDirection = RIGHT;
				image = rotate(image, curDirection, preDirection);
			}
			break;
		case KeyEvent.VK_W:
			keyW = true;//System.out.println(KeyEvent.VK_W + " " + keyW);
			if(curDirection != UP){
				preDirection = curDirection;
				curDirection = UP;
				image = rotate(image, curDirection, preDirection);
			}
			break;
		case KeyEvent.VK_S:
			keyS = true;//System.out.println(KeyEvent.VK_S + " " + keyS);
			if(curDirection != DOWN){
				preDirection = curDirection;
				curDirection = DOWN;
				image = rotate(image, curDirection, preDirection);
			}
			break;
		case KeyEvent.VK_SPACE:
			keySPACE = true;
			break;
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		int keyCode = e.getKeyCode();
		switch(keyCode){
		case KeyEvent.VK_A:
			keyA = false;//System.out.println(KeyEvent.VK_A + " " + keyA);
			break;
		case KeyEvent.VK_D:
			keyD = false;//System.out.println(KeyEvent.VK_D + " " + keyD);
			break;
		case KeyEvent.VK_W:
			keyW = false;//System.out.println(KeyEvent.VK_W + " " + keyW);
			break;
		case KeyEvent.VK_S:
			keyS = false;//System.out.println(KeyEvent.VK_S + " " + keyS);
			break;
		case KeyEvent.VK_SPACE:
			keySPACE = false;
			break;
		}
	}

	@Override
	public void keyTyped(KeyEvent e) {
		
	}
}
