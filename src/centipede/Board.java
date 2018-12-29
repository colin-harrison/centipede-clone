package centipede;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseMotionListener;
import java.util.List;
import java.util.ArrayList;
import java.util.Random;
import java.awt.event.MouseListener;
import java.awt.event.MouseEvent;
import javax.swing.JPanel;
import javax.swing.Timer;

public class Board extends JPanel implements ActionListener {

	private final int B_WIDTH = 512;
	private final int B_HEIGHT = 800;
	private final int ICRAFT_X = 40;
	private final int ICRAFT_Y = 60;
	private final int DELAY = 10;
	private Timer timer;
	private SpaceShip spaceShip;
	private boolean ingame;
	private Score score;
	private Spider spider;
	private ArrayList<Mushroom> mushrooms;
	private List<Centipede> centipede;
	private int turnCount;						// used to slow down the centipede, it moves every other turn
	
	public Board(double likelihood) {
		
		initBoard(likelihood);
	}
	
	private void initBoard(double likelihood) {
		
		this.addMouseMotionListener(new MouseMover());
		this.addMouseListener(new MouseMover());
		this.setFocusable(true);
		this.requestFocusInWindow();
		
		spider = new Spider(16, 656);
		score = new Score();
		ingame = true;
		setBackground(Color.black);
		
		initCentipede();
		turnCount = 1;
		
		spaceShip = new SpaceShip(ICRAFT_X, ICRAFT_Y);
		mushrooms = new ArrayList<Mushroom>();
		generateMushrooms(likelihood);
		
		timer = new Timer(DELAY, this);
		timer.start();
	}

	
	private void initCentipede() {
		
		int x = B_WIDTH - 32;
		int y = 32;
		
		centipede = new ArrayList<Centipede>();
		for (int i = 0; i < 10; i ++) {
			centipede.add(new Centipede(x, y));
			x += 16;
		}
	}
	
	// Fixed. Likelihood determines chance at viable spots
	private void generateMushrooms(double likelihood) {
		boolean west;		// west 1 = moving left, west 0 = moving right
		Random rand = new Random();
		boolean[][] grid = new boolean[(B_HEIGHT) / 16][(B_WIDTH) / 16]; // initialized to 0
		
		
		// start at (16, 32)
		for (int x = 16; x < B_WIDTH - 32; x+=16) {
			west = false;	// check this. should be false i think
			for (int y = 48; y < B_HEIGHT - 160; y+=16) {
				if (!west) {
					
					// check for empty spot directly above and above to the left
					// ALSO NEED TO CHECK BELOW AND TO THE LEFT
					if (!grid[(y - 16) / 16][x/16] && grid[(y-16) / 16][(x-16) / 16]
							&& !grid[(y+16) / 16][(x-16) / 16]) {
						// do nothing
					}
					else {
						// take a chance at placing a mushroom here
						int randomInt = rand.nextInt(1000);
						if (randomInt < (likelihood * 1000)) {
							// place a mushroom
							grid[y/16][x/16] = true;
							mushrooms.add(new Mushroom(x, y));
						}
					}
					
				} else {
					
					// check for empty spot directly above and above to the right
					// DONT NEED TO CHECK BELOW AND RIGHT BC NOT HAPPENED YET
					if (!grid[(y-16) / 16][x/16] /*&& grid[(y-16) / 16][(x+16) / 16]
							&& !grid[(y+16) / 16][(x+16) / 16]*/) {
						// do nothing
					}
					else {
						int randomInt = rand.nextInt(1000);
						if (randomInt < (likelihood * 1000)) {
							grid[y/16][x/16] = true;
							mushrooms.add(new Mushroom(x,y));
						}
					}
				}
				west = !west;
			}
		}
	}
	
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		if (ingame) {
			doDrawing(g);
		} else {
			drawGameOver(g);
		}
		Toolkit.getDefaultToolkit().sync();
	}
	
	// Order in which things are drawn determines what goes on top.
	// Draw spiders after mushrooms to make them visible on top of mushrooms.
	private void doDrawing(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;
		
		score.drawScore(g);	// draws lives as well
		
		drawCentipede(g2d);
		
		for (Mushroom mushroom : mushrooms) {
			g2d.drawImage(mushroom.getImage(), mushroom.getX(), mushroom.getY(), this);
		}
		
		g2d.drawImage(spaceShip.getImage(), spaceShip.getX(), spaceShip.getY(), this);
		g2d.drawImage(spider.getImage(), spider.getX(), spider.getY(), this);
		
		List<Missile> missiles = spaceShip.getMissiles();
		
		for (Missile missile : missiles) {
			g2d.drawImage(missile.getImage(), missile.getX(), missile.getY(), this);
		}
	}
	
	private void drawCentipede(Graphics2D g2d) {
		for (Centipede segment : centipede) {
			if (segment.isVisible() ) {
				g2d.drawImage(segment.getImage(), segment.getX(), segment.getY(), this);
			}
		}
	}
	
	private void drawGameOver(Graphics g) {
		
		String msg = "Game Over";
		Font small = new Font("Helvetica", Font.BOLD, 24);
		FontMetrics fm = getFontMetrics(small);
		
		g.setColor(Color.white);
		g.setFont(small);
		g.drawString(msg, (500 - fm.stringWidth(msg)) / 2, 800 / 2 - 24);
		
		String scoreStr = "Score: " + score.getScore();
		g.drawString(scoreStr, (500 - fm.stringWidth(scoreStr)) / 2, (800 / 2) + 24);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {

//		updateSpaceShip();
		updateMissiles();
		updateMushrooms();	
		updateSpider();
		
		if (turnCount == 4) {
			updateCentipede();	// need to update every turn and move on turncount
			turnCount = 1;
		}
//		turnCount *= -1;
		turnCount++;
		
		checkCollisions();
		
		repaint();
	}
	
	private void checkCollisions() {
		
		List<Missile> missiles = spaceShip.getMissiles();
		Rectangle rSpaceShip = spaceShip.getBounds();
		
		// Check for collisions between centipedes and spaceship
		for (int i = 0; i < centipede.size(); i++) {
			Centipede segment = centipede.get(i);
			Rectangle rCentipede = segment.getBounds();
			
			if (rCentipede.intersects(rSpaceShip)) {
				boolean dead = score.loseLife();
				resetBoard();
				if (dead) {
					ingame = false;
				}
				break;
			}
		}
		
		// Check for collisions between missiles and centipedes
		for (int i = 0; i < centipede.size(); i++) {
			Centipede segment = centipede.get(i);
			Rectangle rCentipede = segment.getBounds();
				
			for (int j = 0; j < missiles.size(); j++) {
				Rectangle rMissile = missiles.get(j).getBounds();
				if (rMissile.intersects(rCentipede)) {
						
					missiles.remove(j);
					j--;
					
					if (segment.loseLife()) {		// if it died
						// it's been marked invisible
						// may need to move this off screen too right away for collision purposes
						score.add(5);
						centipede.remove(segment);
						i--;
						if (centipede.size() == 0) {
							initCentipede();
							score.add(600);
						}
					}
					else {
						score.add(2);
					}
				}
			}
		}
		
		
		// Check for collisions between missiles and mushrooms
		for (Mushroom mushroom : mushrooms) {
			Rectangle rMushroom = mushroom.getBounds();
			
			for (int i = 0; i < missiles.size(); i++) {
				Rectangle rMissile = missiles.get(i).getBounds();
				if (rMissile.intersects(rMushroom)) {
					mushroom.loseLife(score);
					missiles.remove(i);		// missiles should only collide once
					break;
				}
			}
		}

		// Check for collisions between Spider and SpaceShip
		Rectangle rSpider = spider.getBounds();
		if (rSpaceShip.intersects(rSpider)) {
			// Need to hide spider once it intersects with spaceship so player
			// can move out of the way.
			// resetting all of the mushrooms should happen in Board.java not Score.java
			boolean dead = score.loseLife();
			spider.setVisible(false);
			resetBoard();
			if (dead) {
				ingame = false;
			}
		}
		
		// Check for collisions between missiles and spider
		for (int i = 0; i < missiles.size(); i++) {
			Rectangle rMissile = missiles.get(i).getBounds();
			if (rMissile.intersects(rSpider)) {
				spider.loseLife(score);
				missiles.remove(i);		// missiles should only collide once
				break;
			}
		}
	}
	
	// Should reset mushrooms and re spawn spider and centipedes
	private void resetBoard() {
		
		restoreMushrooms();
		spider = new Spider(237, 368);	// roughly center of the board
		
//		centipede.clear();
		initCentipede();
	}
	
	private void restoreMushrooms() {
		for (Mushroom mushroom : mushrooms) {
			if (mushroom.getLives() != 3) {
				mushroom.restore(score);
			}
		}
	}
	
	private boolean nextSpotOpen(Rectangle nextSpot) {
		
		for (Mushroom mushroom : mushrooms) {
			
			Rectangle rMushroom = mushroom.getBounds();
			if (rMushroom.intersects(nextSpot)) {
				return false;
			}
		}
		
		return true;
	}
	
	private boolean nextSpotInBounds(int x, int y) {
		
		// When a new centipede is initialized most of it is out of bounds
		// so this statement lets them move in bounds.
		if (y == 32 && x > 32)
			return true;
		
		if (x >= 0 && x <= B_WIDTH - 32 && y <= B_HEIGHT - 160 + 32) {
			return true;
		}
		return false;
	}
	
	private void updateCentipede() {
	
	int x, y, newX, width, height, dir;
		
		for (Centipede segment : centipede) {
			
			dir = segment.getDirection();
			x = segment.getX();
			y = segment.getY();
			newX = x + (dir * 16);
			width = segment.getWidth();
			height = segment.getHeight();
			Rectangle nextSpot = new Rectangle(newX, y, width, height);
			
			// If we can move to the right/left then move
			if (nextSpotInBounds(newX, y) && nextSpotOpen(nextSpot)) {
				segment.move(newX, y);
			}
			else {
				
				// This segment has hit a mushroom/wall or player area 
				// and needs to move vertically
				if (nextSpotInBounds(x, y + 16)) {
					segment.move(x, y + 16);
				}
				else {
					segment.move(x, y - 16);
				}

				segment.switchDirection();
			}
		}
	}
	
	// Not working, find a way to remove spider from the game
	private void updateSpider() {
		if (!spider.isVisible()) {
			spider.hide();
		}
		
		spider.move();
	}
	
	private void updateMushrooms() {
		for (int i = 0; i < mushrooms.size(); i++) {
			if (!mushrooms.get(i).isVisible()) {
				mushrooms.remove(i);
				i--;
			}
		}
	}
	
	private void updateMissiles() {
		
		List<Missile> missiles = spaceShip.getMissiles();
		
		for (int i = 0; i < missiles.size(); i++) {
			
			Missile missile = missiles.get(i);
			
			if (missile.isVisible()) {
				missile.move();
			} else {
				missiles.remove(i);
			}
		}
	}
	
	private void updateSpaceShip() {
		
		spaceShip.move(); // change to mouseMove (x,y)
	}
	
	private class MouseMover implements MouseMotionListener, MouseListener {
		
		@Override
		public void mouseMoved(MouseEvent e) {
			
			spaceShip.mouseMove(e.getX(), e.getY());
			// repaint(spaceShip.getX() - 70, spaceShip.getY() - 70, spaceShip.getWidth() + 50, spaceShip.getHeight() + 2);
		}
		
		@Override
		public void mouseDragged(MouseEvent e) {
			mouseMoved(e);
		}
		
		// All below are from MouseListener Interface
		@Override
		public void mousePressed(MouseEvent e) {
			spaceShip.fire();
			new Thread (new AudioPlayer()).start();
		}
		
		@Override
		public void mouseReleased(MouseEvent e) {
			// do nothing
		}
		
		@Override
		public void mouseClicked(MouseEvent e) {
			// called after mouse is released - ignore it
		}
		
		@Override
		public void mouseEntered(MouseEvent e) {
			mouseMoved(e);
		}
		
		@Override
		public void mouseExited(MouseEvent e) {
			mouseMoved(e);
		}
	}
}















