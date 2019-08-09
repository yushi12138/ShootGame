package com.bigchickenleg.shootgame;

import java.util.Random;

public class Aircraft extends FlyingObject implements Enemy{

	private int xSpeed;
	private int ySpeed;
	private int type;
	private int armor;
	
	public Aircraft() {
		this.image = ShootGame.aircraft1;
		this.width = image.getWidth();
		this.height = image.getHeight();
		Random rand = new Random();
		this.xPosition = rand.nextInt(ShootGame.WIDTH-width);
		this.yPosition = -height;
		
		this.xSpeed = 0;
		this.ySpeed = 2;
		this.type = 1; //if we have multiple types of enemy we can generate by random method
		this.armor = type;
		
	}

	@Override
	public int getScore() {
		return this.type*10;
	}

	@Override
	public int getArmor() {
		return armor;
	}
	
	public void reduceArmor(FlyingObject flying) {
		if(flying instanceof Bullet) {
			this.armor -= ((Bullet) flying).getDamge();
		}
		if(flying instanceof Hero) {
			this.armor -= 1;
		}
	}

	@Override
	public void step() {
		this.yPosition+=ySpeed;
	}

	@Override
	public boolean outOfBounds() {
		return this.yPosition>ShootGame.HEIGHT;
	}

	public boolean shotBy(Bullet bullet) {
		int[] xRange = new int[] {this.xPosition, this.xPosition+this.width};
		int[] yRange = new int[] {this.yPosition, this.yPosition+this.height};
		int x = bullet.xPosition;
		int y = bullet.yPosition;
		return x>xRange[0] && x<xRange[1] && y>yRange[0] && y<yRange[1];
	}
	
	@Override
	public boolean hitBy(Hero hero) {
		int[] xRange = new int[] {this.xPosition-hero.width/2, this.xPosition+this.width+hero.width/2};
		int[] yRange = new int[] {this.yPosition-hero.height/2, this.yPosition+this.height+hero.height/2};
		int x = hero.xPosition+hero.width/2;
		int y = hero.yPosition+hero.height/2;
		return x>xRange[0] && x<xRange[1] && y>yRange[0] && y<yRange[1];
	}
		
}
