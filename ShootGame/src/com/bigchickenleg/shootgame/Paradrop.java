package com.bigchickenleg.shootgame;

import java.util.Random;

public class Paradrop extends FlyingObject implements Supply{

	private int xSpeed;
	private int ySpeed;
	private int supply;
	
	
	
	
	public Paradrop() {
		this.image = ShootGame.supply1;
		this.width = image.getWidth();
		this.height = image.getHeight();
		Random rand = new Random();
		this.xPosition = rand.nextInt(ShootGame.WIDTH-width);
		this.yPosition = -height;
		
		this.xSpeed = 1;
		this.ySpeed = 2;
		this.supply = 40;
	}


	@Override
	public int getSupply() {
		return supply;
	}
	
	@Override
	public void step() {
		this.yPosition += ySpeed;
		//zigzag pattern movement
		this.xPosition += xSpeed;
		if(xPosition+width>=ShootGame.WIDTH)
			xSpeed = -1;
		else if(xPosition<=0)
			xSpeed = 1;
	}
	
	
	@Override
	public boolean outOfBounds() {
		return this.yPosition>ShootGame.HEIGHT;
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
