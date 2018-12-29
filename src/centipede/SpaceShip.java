package centipede;

import java.util.List;
import java.util.ArrayList;


// TODO: MAKE THIS RESPOND TO MOUSE INSTEAD OF KEYS
// COLIN
// COLIN
// COLIN

public class SpaceShip extends Sprite {

	private int dx;
	private int dy;
	private List<Missile> missiles;
	
	public SpaceShip(int x, int y) {
		super(x, y);
		
		
		initSpaceShip();
	}
	
	private void initSpaceShip() {
		
		missiles = new ArrayList<>();
		
		loadImage("src/resources/craft.png"); // credit: http://millionthvector.blogspot.com/p/free-sprites.html
		getImageDimensions();
	}
	
	public void move() {
		
		x += dx;
		y += dy;
	}
	
	public List<Missile> getMissiles() {
		return missiles;
	}
	
	public void mouseMove(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	public void fire() {
		missiles.add(new Missile(x + (width / 2) - 2, y - 2)); // Change this line to center it at top of ship
	}
	
	public void resetPosition() {
		this.x = 512 / 2;
		this.y = 700;
	}
}
