import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class GameObject {
	
	private int x;
	private int y;
	
	private String name;
	
	private BufferedImage image;
	
	/**
	 * 
	 * @param x
	 * @param y
	 * @param name
	 */
	
	public GameObject(int x, int y, String name){
		this.x = x;
		this.y = y;
		
		this.name = name;
		
		image = null;
	}
	
	/**
	 * 
	 * @param x
	 * @param y
	 * @param name
	 * @param imagePath
	 */
	
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
		if(image != null){
			g2d.drawImage(image, x, y, null);
		}
	}
	
	public Rectangle getBounds(){
		if(image != null){
			return new Rectangle(x, y, image.getWidth(), image.getHeight());
		}else{
			return new Rectangle(x, y, 32, 32);
		}
	}
	
	public BufferedImage crop(BufferedImage src, Rectangle rect) {
	      BufferedImage dest = src.getSubimage(0, 0, rect.width, rect.height);
	      return dest; 
	}
	
	public BufferedImage rotate(BufferedImage img, int cdir, int pdir){
		AffineTransform transform = new AffineTransform();
	    transform.rotate(Math.toRadians(-(cdir - pdir) * 90), img.getWidth() / 2, img.getHeight() / 2);
	    AffineTransformOp op = new AffineTransformOp(transform, AffineTransformOp.TYPE_BILINEAR);
	    img = op.filter(img, null);
	    
	    return img;
	}
	
	public NavTile getPositionOnMap(){
		int x = this.x / 32;
		int y = this.y / 32;
		
		double xDec = this.x / 32;
		double yDec = this.y / 32;
		
		xDec = xDec - x;
		yDec = yDec - y;
		
		if(xDec > 0.5){
			x = Math.round(this.x / 32);
		}else{
			x = (int)Math.floor(this.x / 32);
		}
		
		if(yDec > 0.5){
			y = Math.round(this.y / 32);
		}else{
			y = (int)Math.floor(this.y / 32);
		}
		
		return new NavTile(x, y, false);
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
		if(image != null){
			return image.getWidth();
		}else{
			return 32;
		}
	}
	
	public int getHeight(){
		if(image != null){
			return image.getHeight();
		}else{
			return 32;
		}
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
