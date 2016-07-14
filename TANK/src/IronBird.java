public class IronBird extends GameObject{
	
	private int curHp;
	private int maxHp;
	
	public IronBird(int posX, int posY){
		super(posX, posY, "Iron Bird", "Images//Bird01.png");
		
		maxHp = 50;
		curHp = maxHp;
	}
		
	public IronBird(Block pos){
		super(pos.getX(), pos.getY(), "Iron Bird", "Images//Bird01.png");
		
		maxHp = 50;
		curHp = maxHp;
	}
	
	public void recieveDamage(int damage){
		curHp -= damage;
	}
	
	public boolean isDead(){
		if(curHp <= 0){
			return true;
		}else{
			return false;
		}
	}
}
