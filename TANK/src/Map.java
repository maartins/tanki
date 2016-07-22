import java.awt.Graphics;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class Map extends GameObject{
	private String mapPath;
	
	private ArrayList<String> mapList;
	private ArrayList<Spawner> spawnerList;
	private ArrayList<Block> blockList;
	private ArrayList<NavTile> navList;
	
	private NavTile[][] navMap;
	private Block tankSpawnPoint;
	private Block ironBirdSpawnPoint;

	public Map(){
		super(0, 0, "Map");
		
		mapList = new ArrayList<String>();
		spawnerList = new ArrayList<Spawner>();
		blockList = new ArrayList<Block>();
		navMap = new NavTile[15][15];
		navList = new ArrayList<NavTile>();
	}
	
	private void makeMap(){
		BufferedReader bufReader = null;
		
		try {
			String curLine;
			bufReader = new BufferedReader(new FileReader(mapPath));
			int blockSize = 32;
			int globalCounter = 0;
			
			int i = 0;
			while((curLine = bufReader.readLine()) != null){
				int j = 0;
				for(char c : curLine.toCharArray()){
					if(c == '#'){
						blockList.add(new Floor(j * blockSize, i * blockSize));
						blockList.add(new Wall(j * blockSize, i * blockSize));
						navMap[i][j] = new NavTile(j * blockSize,i * blockSize, true);
						navMap[i][j].setName(globalCounter + "  wall");
						navList.add(navMap[i][j]);
					}else if(c == '%'){
						blockList.add(new SolidWall(j * blockSize, i * blockSize));
						navMap[i][j] = new NavTile(j * blockSize,i * blockSize, true);
						navMap[i][j].setName(globalCounter + " swall");
						navList.add(navMap[i][j]);
					}else if(c == ' '){
						blockList.add(new Floor(j * blockSize, i * blockSize));
						navMap[i][j] = new NavTile(j * blockSize,i * blockSize, false);
						navMap[i][j].setName(globalCounter + " floor");
						navList.add(navMap[i][j]);
					}else if(c == 's'){
						spawnerList.add(new Spawner(j * blockSize, i * blockSize));
						blockList.add(new Floor(j * blockSize, i * blockSize));
						navMap[i][j] = new NavTile(j * blockSize,i * blockSize, false);
						navMap[i][j].setName(globalCounter + " spawn");
						navList.add(navMap[i][j]);
					}else if(c == 't'){
						tankSpawnPoint = new Floor(j * blockSize, i * blockSize);
						blockList.add(tankSpawnPoint);
						navMap[i][j] = new NavTile(j * blockSize,i * blockSize, false);
						navMap[i][j].setName(globalCounter + "  tank");
						navList.add(navMap[i][j]);
					}else if(c == '1'){
						blockList.add(new PwrUpSuperBullet(j * blockSize, i * blockSize));
						navMap[i][j] = new NavTile(j * blockSize,i * blockSize, false);
						navMap[i][j].setName(globalCounter + " pwrup");
						navList.add(navMap[i][j]);
					}else if(c == 'b'){
						ironBirdSpawnPoint = new Floor(j * blockSize, i * blockSize);
						blockList.add(ironBirdSpawnPoint);
						navMap[i][j] = new NavTile(j * blockSize,i * blockSize, true);
						navMap[i][j].setName(globalCounter + "  bird");
						navList.add(navMap[i][j]);
					}else{
						// never
						tankSpawnPoint = new Floor(j * blockSize, i * blockSize);
						blockList.add(tankSpawnPoint);
						navMap[i][j] = new NavTile(j * blockSize,i * blockSize, false);
						navMap[i][j].setName(globalCounter + "  none");
						navList.add(navMap[i][j]);
					}
					j++;
					globalCounter++;
				}
				i++;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		// Print map
		//for (int k = 0; k < 15; k++) {
		//	for (int l = 0; l < 15; l++) {
		//		System.out.print(navMap[k][l] + "  <>  ");
		//	}
		//	System.out.println("");
		//}
	}
	
	public void changeMap(int currentMap){
		spawnerList = new ArrayList<Spawner>();
		blockList = new ArrayList<Block>();
		navMap = new NavTile[15][15];
		navList = new ArrayList<NavTile>();
		
		if(currentMap < mapList.size()){
			mapPath = mapList.get(currentMap);
		}else{
			mapPath = mapList.get(0);
		}
		
		makeMap();
	}
	
	public void getFiles(File folder){
	    for(File f : folder.listFiles()){
	    	mapList.add(folder.getName() + "//" + f.getName());
	    	//System.out.println(folder.getName() + "//" + f.getName());
	    }
	}
	
	public void draw(Graphics g){
		for(Block b : blockList){
			b.draw(g);
		}
		
		//for(NavTile n : navList){
		//	n.draw(g);
		//}
	}
	
	public ArrayList<String> getMapList(){
		return mapList;
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
