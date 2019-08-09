package com.bigchickenleg.shootgame;

import java.awt.image.BufferedImage;

public class Bullet extends FlyingObject implements Ammo{
	
	private int ySpeed = 3;
	private int damage;
	
	public Bullet(int xPosition, int yPosition) {
		this.image = ShootGame.bulletYellow;
		this.width = image.getWidth();
		this.height = image.getHeight();
		this.xPosition = xPosition;
		this.yPosition = yPosition-height;
		this.damage = 1;
		
	}

	@Override
	public void step() {
		this.yPosition -= ySpeed;
	}
	
	@Override
	public boolean outOfBounds() {
		return this.yPosition<-this.height;
	}

	@Override
	public int getDamge() {
		return damage;
	}
	

}
