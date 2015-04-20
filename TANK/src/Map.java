import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class Map extends GameObject{
	
	private ArrayList<Block> blockList;
	private Block[][] blocks;
	
	public Map(){
		super(0, 0, "Map");
		
		blockList = new ArrayList<Block>();
		blocks = new Block[16][16];
		
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
						blockList.add(new Wall(j * 32, i * 32, i, j));
						blocks[i][j] = new Wall(j * 32, i * 32, i, j);
					}else if(c == ' '){
						blockList.add(new Floor(j * 32, i * 32, i, j,"Floor"));
						blocks[i][j] = new  Floor(j * 32, i * 32, i, j,"Floor");
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
		
		
		//for(Block b : blockList){
			//b.draw(g);
		//}
		
		for(Block[] bb : blocks){
			for(Block b : bb){
				b.draw(g);
			}
		}
	}
	
	public ArrayList<Block> getBlockList(){
		return blockList;
	}
	
	public Block[][] getBlocks(){
		return blocks;
	}
}
