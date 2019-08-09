package com.bigchickenleg.shootgame;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import java.util.LinkedList;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class ShootGame extends JPanel{
	
	public static final int WIDTH = 480;
	public static final int HEIGHT = 852;
	//status of the program
	public static final int START = 0;
	public static final int RUNNING = 1;
	public static final int PAUSE = 2;
	public static final int GAME_OVER = 3;
	private int status = START;
	
	public static BufferedImage background;
	public static BufferedImage hero1;
	public static BufferedImage hero2;
	public static BufferedImage aircraft1;
	public static BufferedImage bulletYellow;
	public static BufferedImage supply1;
	public static BufferedImage life;
	
	
	private Hero hero;
	private LinkedList<Bullet> bullets;
	private LinkedList<FlyingObject> enemies;
	private LinkedList<FlyingObject> supply;

	public ShootGame() {
		this.hero = new Hero();
		this.bullets = new LinkedList<>();
		this.enemies = new LinkedList<>();
		this.supply = new LinkedList<>();
	}
	
	private static void loadImages() {
		try {
			background = ImageIO.read(ShootGame.class.getResource("../resources/images/background.png"));
			hero1 = ImageIO.read(ShootGame.class.getResource("../resources/images/hero1.png"));
			hero2 = ImageIO.read(ShootGame.class.getResource("../resources/images/hero2.png"));
			aircraft1 = ImageIO.read(ShootGame.class.getResource("../resources/images/enemy1.png"));
			bulletYellow = ImageIO.read(ShootGame.class.getResource("../resources/images/bullet1.png"));
			supply1 = ImageIO.read(ShootGame.class.getResource("../resources/images/ufo1.png"));
			life = ImageIO.read(ShootGame.class.getResource("../resources/images/life.png"));
			
		}catch(Exception e) {
			e.printStackTrace();		
		}
		
	}
	

	public static void main(String[] args) {
		loadImages();//use static method to load the images
		JFrame frame = new JFrame();		//create the frame
		JPanel shootGame = new ShootGame();	//create the panel(ShootGame class extends JPanel)
		frame.add(shootGame);				//add the panel to frame
		frame.setSize(WIDTH, HEIGHT);		//set the size of frame
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);	//close the window and exit the programe
		frame.setLocationRelativeTo(null);						//set the location relative to the top-left corner (0,0), null is midde
		frame.setVisible(true);				//set the frame visible and automatically call the paint() method
		
		((ShootGame) shootGame).action();	//start the program
			
	}

	
	public void action() {
		MouseAdapter l = new MouseAdapter() {

			@Override
			public void mouseClicked(MouseEvent e) {
				switch(status) {
				case START:
					status=RUNNING;break;
				case GAME_OVER:
					restart();
					status=START;break;
				}
				
			}
			private void restart() {
				hero = new Hero();
				bullets = new LinkedList<>();
				supply = new LinkedList<>();
			}

			@Override
			public void mouseEntered(MouseEvent e) {
				if(status==PAUSE)
					status = RUNNING;
			}

			@Override
			public void mouseExited(MouseEvent e) {
				if(status==RUNNING)
					status = PAUSE;
			}

			@Override
			public void mouseMoved(MouseEvent e) {
				if(status==RUNNING) {	
					int x = e.getX();
					int y = e.getY();
					hero.moveTo(x, y);
				}
			}
			
		};
		this.addMouseListener(l);
		this.addMouseMotionListener(l);
		
		Timer timer = new Timer();
		int interval = 10;	//mili-second
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				if(status==RUNNING) {
					enterAction();
					stepAction();
					shootAction();
					deleteAction();
					hitAction();
					checkGameOverAction();
				}
				repaint();		//defined in the JPanel, repaint automatically call the paint() method
			}
		},interval,interval); 	//every interval invoke the enterAction() method
	}
	
	/* check whether game over: life<=0 */
	public void checkGameOverAction() {
		if(isGameOver()) {
			status=GAME_OVER;
		}
	}
	private boolean isGameOver() {
		return hero.getLife()<=0;
	}
	
	//bullet hit the Aircraft
	public void hitAction() {
		//enemies hit events: by bullet/by hero
		for(int i=0;i<enemies.size();i++) {
			Enemy enemy = (Enemy)enemies.get(i);
			if(hitByBullet(enemy) && enemy.getArmor()<=0) {
				enemies.remove(i);		//delete the bullet hit the aircraft
				i--;
			}else if(hitByHero(enemy)&&enemy.getArmor()<=0) {
				enemies.remove(i);
				i--;
			}
		}
		//supply hit events
		for(int i=0;i<supply.size();i++){
			Paradrop a = (Paradrop)supply.get(i);
			if(a.hitBy(hero)) {
				hero.addExtraBullet(a);
				supply.remove(i);
				i--;
			}
		}
	}
	//aircraft hit by hero
	public boolean hitByHero(Enemy enemy) {
		if(enemy.hitBy(hero)) {
			hero.substractLife();
			hero.clearExtraBullet();
			enemy.reduceArmor(hero);
			return true;
		}
		return false;
	}
	//aircraft hit by bullet
	public boolean hitByBullet(Enemy enemy) {
		int index = -1;
		for(int i=0;i<bullets.size();i++) {
			if(enemy.shotBy(bullets.get(i))) {
				index=i;
				break;
			}
		}
		//if bullet hits the aircraft, hero add score/delete aircraft
		if(index!=-1) {
			hero.addScore(enemy);
			enemy.reduceArmor(bullets.get(index));
			bullets.remove(index);
			return true;
		}
		return false;
	}
	//delete the object out of bound
	public void deleteAction() {
		//based on queue, remove the peek of the queue, if it is out of bounds
		while(!enemies.isEmpty()&&enemies.peek().outOfBounds()) {
			enemies.poll();
		}
		while(!supply.isEmpty()&&supply.peek().outOfBounds()) {
			supply.poll();
		}
		while(!bullets.isEmpty()&&bullets.peek().outOfBounds()) {
			bullets.poll();
		}
//		System.out.printf("%d,%d,%d\n", enemies.size(),supply.size(), bullets.size());

	}
	//hero shoot the bullet
	private void shootAction() {
		if(enterActionCounter%30==0) {
			for(Bullet bullet:hero.shoot()) {
				bullets.add(bullet);
			}
		}
	}
	//move the images every 10ms
	private void stepAction() {
		hero.step();
		for(FlyingObject enemy:enemies) {
			enemy.step();
		}
		for(FlyingObject a:supply) {
			a.step();
		}
		for(Bullet bullet:bullets) {
			bullet.step();
		}
	}
	
	//flyingObjects enter the window
	private int enterActionCounter = 0;
	private void enterAction() {
		if(++enterActionCounter%40==0) {
			Random rand = new Random();
			int type = rand.nextInt(20);
			if(type<2) {//supply
				supply.add(new Paradrop());
			}else {
				enemies.add(new Aircraft());
			}
		}

	}
	
	
	@Override
	public void paint(Graphics g) {
		g.drawImage(background,0,0,null);
		paintHero(g);
		paintBullets(g);
		paintFlyingObjects(g);
		paintScoreAndLife(g);
		paintStatus(g);
	}
	
	private void paintHero(Graphics g) {
		g.drawImage(hero.image,hero.xPosition,hero.yPosition,null);
	}
	
	private void paintBullets(Graphics g) {
		for(Bullet bullet : bullets) {
			g.drawImage(bullet.image,bullet.xPosition,bullet.yPosition,null);	
		}
	}
	
	private void paintFlyingObjects(Graphics g) {
		for(FlyingObject o : enemies) {
			g.drawImage(o.image,o.xPosition,o.yPosition,null);
		}
		for(FlyingObject o : supply) {
			g.drawImage(o.image,o.xPosition,o.yPosition,null);
		}
	}
	
	private void paintScoreAndLife(Graphics g) {
		g.setColor(new Color(0x0000FF));
		g.setFont(new Font(Font.SANS_SERIF,Font.BOLD,24));
		g.drawString("SCORE: "+hero.getScore(),10,25);
		for(int i=0;i<hero.getLife();i++) {
			g.drawImage(life,i*life.getWidth(),this.HEIGHT-life.getHeight()-46,null);
		}
	}
	
	private void paintStatus(Graphics g) {
		g.setColor(new Color(0x555555));
		g.setFont(new Font(Font.SANS_SERIF,Font.BOLD,50));
		switch(status) {
		case START:
			g.drawString("START", WIDTH/3-10, HEIGHT/2);break;
		case PAUSE:
			g.drawString("PAUSE", WIDTH/3-10, HEIGHT/2);break;
		case GAME_OVER:
			g.drawString("GAME OVER", WIDTH/6, HEIGHT/2);break;
		}
	}
	
}
