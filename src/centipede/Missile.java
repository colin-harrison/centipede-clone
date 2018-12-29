package centipede;

public class Missile extends Sprite {

	// MISSILE_SPEED must be > 16 here or at least size of mushroom sprite in px
	// because every DELAY ms an ActionEvent occurs which determines if there are
	// collisions.
	private final int MISSILE_SPEED = 20;
	private final int BOARD_HEIGHT = 790;
	
	public Missile(int x, int y) {
		super(x, y);
		
		initMissile();
	}
	
	private void initMissile() {
		
		loadImage("src/resources/missile.png");
		getImageDimensions();
	}
	
	// TODO change to shoot in y direction
	public void move() {
		
		y -= MISSILE_SPEED;
		
		if (y > BOARD_HEIGHT) {
			visible = false;
		}
	}
}
