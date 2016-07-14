import java.awt.Graphics;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class Map extends GameObject{
	private String mapPath;
	
	private ArrayList<Spawner> spawnerList;
	private ArrayList<Block> blockList;
	private ArrayList<NavTile> navList;
	private NavTile[][] navMap;
	private Block tankSpawnPoint;
	private Block ironBirdSpawnPoint;

	public Map(String mapPath){
		super(0, 0, "Map");
		
		this.mapPath = mapPath;
		
		spawnerList = new ArrayList<Spawner>();
		blockList = new ArrayList<Block>();
		navMap = new NavTile[15][15];
		navList = new ArrayList<NavTile>();
		
		makeMap();
	}
	
	private void makeMap(){
		BufferedReader bufReader = null;
		
		try {
			String curLine;
			bufReader = new BufferedReader(new FileReader(mapPath));

			int i = 0;
			while((curLine = bufReader.readLine()) != null){
				int j = 0;
				for(char c : curLine.toCharArray()){
					if(c == '#'){
						blockList.add(new Floor(j, i));
						blockList.add(new Wall(j, i));
						navMap[i][j] = new NavTile(i, j, true);
						navList.add(navMap[i][j]);
					}else if(c == '%'){
						blockList.add(new SolidWall(j, i));
						navMap[i][j] = new NavTile(i, j, true);
						navList.add(navMap[i][j]);
					}else if(c == ' '){
						blockList.add(new Floor(j, i));
						navMap[i][j] = new NavTile(i, j, false);
						navList.add(navMap[i][j]);
					}else if(c == 's'){
						spawnerList.add(new Spawner(j, i));
						blockList.add(new Floor(j, i));
						navMap[i][j] = new NavTile(i, j, false);
						navList.add(navMap[i][j]);
					}else if(c == 't'){
						tankSpawnPoint = new Floor(j, i);
						blockList.add(tankSpawnPoint);
						navMap[i][j] = new NavTile(i, j, false);
						navList.add(navMap[i][j]);
					}else if(c == '1'){
						blockList.add(new PwrUpSuperBullet(j, i));
						navMap[i][j] = new NavTile(i, j, false);
						navList.add(navMap[i][j]);
					}else if(c == 'b'){
						ironBirdSpawnPoint = new Floor(j, i);
						blockList.add(ironBirdSpawnPoint);
						navMap[i][j] = new NavTile(i, j, true);
						navList.add(navMap[i][j]);
					}else{
						// never
						tankSpawnPoint = new Floor(j, i);
						blockList.add(tankSpawnPoint);
						navMap[i][j] = new NavTile(i, j, false);
						navList.add(navMap[i][j]);
					}
					j++;
				}
				i++;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void draw(Graphics g){
		for(Block b : blockList){
			b.draw(g);
		}
		
		for(NavTile n : navList){
			n.draw(g);
		}
	}
	
	public ArrayList<Spawner> getSpawnerList(){
		return spawnerList;
	}
	
	public ArrayList<Block> getBlockList(){
		return blockList;
	}
	
	public NavTile[][] navMap(){
		return navMap;
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
