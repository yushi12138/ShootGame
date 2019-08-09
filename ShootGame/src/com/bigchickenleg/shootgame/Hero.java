package com.bigchickenleg.shootgame;

import java.awt.image.BufferedImage;

public class Hero extends FlyingObject{

	private int life;
	private int score;
	private Bullet bullet;
	private int extraBulletNum;
	private BufferedImage[] images= new BufferedImage[2];
	private int index;
	
	public Hero() {
		this.image = ShootGame.hero1;
		this.width = image.getWidth();
		this.height = image.getHeight();
		this.xPosition = ShootGame.WIDTH/2-width/2+1;
		this.yPosition = ShootGame.HEIGHT/3*2;
	
		this.life = 3;
		this.score = 0;
		this.bullet = new Bullet(ShootGame.WIDTH/2-ShootGame.bulletYellow.getWidth()/2+1, yPosition);
		this.extraBulletNum = 0;
		this.images[0] = ShootGame.hero1;
		this.images[1] = ShootGame.hero2;
		this.index = 0;
	}

	public Bullet getBullet() {
		return bullet;
	}

	@Override
	public void step() {//change the image of hero every 100ms		
		image = images[(index++/10)%2];
	}

	public Bullet[] shoot() {
		Bullet[] bullets;
		if(extraBulletNum>0) {
			bullets = new Bullet[2];
			bullets[0] = new Bullet(this.xPosition+this.width/6-this.bullet.width/2, this.yPosition);			
			bullets[1] = new Bullet(this.xPosition+this.width/6*5-this.bullet.width/2, this.yPosition);
			extraBulletNum -= 2;	//shoot double bullets will decrease the extraBulletNum
		}else {
			bullets = new Bullet[1];
			bullets[0] = new Bullet(this.xPosition+this.width/2-this.bullet.width/2,this.yPosition);
		}
		return bullets;
	}
	
	public void moveTo(int x, int y) {
		this.xPosition = x-this.width/2;
		this.yPosition = y-this.height/2;
	}
	
	@Override
	public boolean outOfBounds() {
		return false;
	}
	
	public void addScore(Enemy enemy) {
		this.score += enemy.getScore();
	}
	public void addExtraBullet(Supply supply) {
		this.extraBulletNum +=supply.getSupply();
	}
	public void clearExtraBullet() {
		this.extraBulletNum +=0;
	}
	public void substractLife() {
		this.life -= 1;
	}
	public int getLife() {
		return this.life;
	}
	public int getScore() {
		return this.score;
	}
	

}
