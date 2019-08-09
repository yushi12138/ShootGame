package com.bigchickenleg.shootgame;

import java.awt.image.BufferedImage;

public abstract class FlyingObject {

	protected BufferedImage image;

	protected int xPosition;
	
	protected int yPosition;
	
	protected int width;
	
	protected int height;
	
	public abstract void step();
	
	public abstract boolean outOfBounds();
	


}
