package Main;
public class PwrUpSuperBullet extends PowerUp{
	
	private int bulletCount;
	
	public PwrUpSuperBullet(int posX, int posY){
		super(posX, posY, "Super Bullet", "Images//PwrUpSuperBullet01.png");
		
		bulletCount = 3;
	}
	
	public int getBulletCount(){
		return bulletCount;
	}
}
