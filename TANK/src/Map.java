import java.awt.Graphics;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class Map extends GameObject{
	
	private final int blockSize = 32;
	
	private String mapPath;
	
	private ArrayList<Spawner> spawnerList;
	private ArrayList<Block> blockList;
	private Block[][] blocks;
	private Block tankSpawnPoint;
	private Block ironBirdSpawnPoint;

	public Map(String mapPath){
		super(0, 0, "Map");
		
		this.mapPath = mapPath;
		
		spawnerList = new ArrayList<Spawner>();
		blockList = new ArrayList<Block>();
		blocks = new Block[16][16];
		
		makeMap();
	}
	
	private void makeMap(){
		BufferedReader br = null;
		
		try {
			String ss;
			br = new BufferedReader(new FileReader(mapPath));

			int i = 0;
			while((ss = br.readLine()) != null){
				int j = 0;
				for(char c : ss.toCharArray()){
					if(c == '#'){
						blockList.add(new Floor(j * blockSize, i * blockSize));
						blockList.add(new Wall(j * blockSize, i * blockSize));
						blocks[j][i] = blockList.get(blockList.size() - 1);
					}else if(c == ' '){
						blockList.add(new Floor(j * blockSize, i * blockSize));
						blocks[j][i] = blockList.get(blockList.size() - 1);
					}else if(c == 's'){
						spawnerList.add(new Spawner(j * blockSize, i * blockSize));
						blockList.add(new Floor(j * blockSize, i * blockSize));
						blocks[j][i] = blockList.get(blockList.size() - 1);
					}else if(c == 't'){
						tankSpawnPoint = new Floor(j * blockSize, i * blockSize);
						blockList.add(tankSpawnPoint);
						blocks[j][i] = blockList.get(blockList.size() - 1);
					}else if(c == '1'){
						blockList.add(new PwrUpSuperBullet(j * blockSize, i * blockSize));
						blocks[j][i] = blockList.get(blockList.size() - 1);
					}else if(c == 'b'){
						ironBirdSpawnPoint = new Floor(j * blockSize, i * blockSize);
						blockList.add(ironBirdSpawnPoint);
						blocks[j][i] = blockList.get(blockList.size() - 1);
					}else{
						tankSpawnPoint = new Floor(j * blockSize, i * blockSize);
						blockList.add(tankSpawnPoint);
						blocks[j][i] = blockList.get(blockList.size() - 1);
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
		for(Block b : blockList){
			b.draw(g);
		}
	}
	
	public ArrayList<Spawner> getSpawnerList(){
		return spawnerList;
	}
	
	public ArrayList<Block> getBlockList(){
		return blockList;
	}
	
	public Block[][] getBlocks(){
		return blocks;
	}
	
	public Block getTankSpawnPoint() {
		return tankSpawnPoint;
	}

	public void setTankSpawnPoint(Block tankSpawnPoint) {
		this.tankSpawnPoint = tankSpawnPoint;
	}
	
	public Block getIronBirdSpawnPoint() {
		return ironBirdSpawnPoint;
	}

	public void setIronBirdSpawnPoint(Block ironBirdSpawnPoint) {
		this.ironBirdSpawnPoint = ironBirdSpawnPoint;
	}
}
