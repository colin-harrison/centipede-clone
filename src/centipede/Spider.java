package centipede;

import java.util.Random;

public class Spider extends Sprite {
	
	private int lives;
	private Random rand;
	private int dx;
	private int dy;
	private int turnsLeft;

	public Spider(int x, int y) {
		super(x, y);
		
		lives = 2;
		loadImage("src/resources/spider.png");
		getImageDimensions();
		rand = new Random();
		turnsLeft = 5;
		startMoving();
	}
	
	private void startMoving() {
		
		dx = rand.nextInt(11) - 5; // number 0 thru 10
		dy = rand.nextInt(11) - 5;
	}
	
	public void move() {
		
		turnsLeft--;
		
		if (turnsLeft == 0) {
			dx = rand.nextInt(11) - 5; // number -5 thru 5
			dy = rand.nextInt(11) - 5;
			turnsLeft = 10;
		}
		
		// Unfortunately hard coded values. It seems that B_WIDTH - width of spider still cuts off
		// some spider. So these values had to be found by manual testing.
		if ((x + dx > 472) || (x + dx < 0)) {
			dx = -dx;
			turnsLeft += 2;
		}
		if ((y + dy > 747) || (y + dy < 0)) {
			dy = -dy;
			turnsLeft += 2;
		}
		
		x += dx;
		y += dy;
	}
	
	public void loseLife(Score score) {
		
		lives--;
		if (lives == 1) {
			
			score.add(100);
		} 
		else {
			
			visible = false;
			score.add(600);
		}
	}
	
	public void hide() {
		x = -100;
		y = -100;
	}

}
