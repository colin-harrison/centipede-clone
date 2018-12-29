package centipede;

public class Centipede extends Sprite {
	
	private int lives;
	private int direction;		// direction of 1 means moving right, -1 means moving left

	public Centipede(int x, int y) {
		super(x, y);

		lives = 2;
		loadImage("src/resources/circle.png");
		getImageDimensions();
		direction = -1;
	}
	
	public Centipede(int x, int y, int lives, int direction) {
		
		super (x,y);
		this.lives = lives;
		this.direction = direction;
		loadImage("src/resources/circle.png");
		getImageDimensions();
	}
	
	public boolean loseLife() {
		
		lives--;
		if (lives == 0) {
			setVisible(false);
			return true;
		}
		
		return false;
	}
	
	public void move(int x, int y) {
		
		this.x = x;
		this.y = y;
	}
	
	public int getDirection() {
		return direction;
	}
	
	public int getLives() {
		return lives;
	}
	
	public void switchDirection() {
		direction *= -1;
	}
}
