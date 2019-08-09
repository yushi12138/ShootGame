package com.bigchickenleg.shootgame;

public interface Enemy{
	
	public int getScore();
	
	public int getArmor();
	
	public void reduceArmor(FlyingObject flying);
	
	public boolean shotBy(Bullet bullet);
	
	public boolean hitBy(Hero hero);
}
