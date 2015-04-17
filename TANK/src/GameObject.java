import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class GameObject {
	
	private int x;
	private int y;
	
	private String name;
	
	private BufferedImage image;
	
	public GameObject(int x, int y, String name){
		this.x = x;
		this.y = y;
		
		this.name = name;
	}
	
	public GameObject(int x, int y, String name ,String imagePath){
		this.x = x;
		this.y = y;
		
		this.name = name;
		
		try{
			image = ImageIO.read(new File(imagePath));
		}catch(IOException e){
			System.out.println("Failed to load image.");
		}
	}
	
	public void draw(Graphics g){
		Graphics2D g2d = (Graphics2D) g;
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2d.drawImage(image, x, y, null);
	}
	
	public Rectangle getBounds(){
		return new Rectangle(x, y, image.getWidth(), image.getHeight());
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
	
	public int getWidth(){
		return image.getWidth();
	}
	
	public int getHeight(){
		return image.getHeight();
	}

	public BufferedImage getImage() {
		return image;
	}
	
	public void setImage(BufferedImage image) {
		this.image = image;
	}
	
	public void setImage(String imagePath) {
		try{
			image = ImageIO.read(new File(imagePath));
		}catch(IOException e){
			System.out.println("Failed to load image.");
		}
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
