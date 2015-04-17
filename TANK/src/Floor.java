
public class Floor extends Block{
	
	public Floor(int posX, int posY, int tileX, int tileY){
		super(posX, posY, tileX, tileY, true, false, "Floor", "Images//Test01.png");
	}
	
	public Floor(int posX, int posY, int tileX, int tileY, String name){
		super(posX, posY, tileX, tileY, true, false, name, "Images//Test01.png");
	}
}
