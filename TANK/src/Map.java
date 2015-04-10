import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class Map extends GameObject{
	
	private ArrayList<Block> blocks; 
	
	public Map(){
		super(0, 0, "Map");
		
		blocks = new ArrayList<Block>();
		
		makeMap();
	}
	
	private void makeMap(){
		BufferedReader br = null;
		
		try {
			String ss;
			br = new BufferedReader(new FileReader("Maps//map1.txt"));

			int i = 0;
			while((ss = br.readLine()) != null){
				int j = 0;
				for(char c : ss.toCharArray()){
					if(c == '#'){
						blocks.add(new Block(j * 32, i * 32));
					}
					j++;
				}
				
				i++;
			}
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}
	
	public void draw(Graphics g){
		//Graphics2D g2d = (Graphics2D) g;
		//g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		//g2d.drawImage(super.image, super.x, super.y, null);
		
		if(!blocks.isEmpty()){
			for(Block b : blocks){
				b.draw(g);
			}
		}
	}
	
	public ArrayList<Block> getBlocks(){
		return blocks;
	}
}
