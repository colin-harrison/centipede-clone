package centipede;

public class Mushroom extends Sprite {
	
	private int lives;

	public Mushroom(int x, int y) {
		super(x, y);

		lives = 3;
		loadImage("src/resources/mushroom.png");
		getImageDimensions();
	}
	
	// Mushrooms need to change their sprite when they are hit by a missile.
	public void loseLife(Score score) {
		lives--;
		if (lives == 2) {
			loadImage("src/resources/mushroom_1.png");
			getImageDimensions();
			score.add(1);;
		} else if (lives == 1) {
			loadImage("src/resources/mushroom_2.png");
			getImageDimensions();
			score.add(1);
		} else {
			visible = false;
			score.add(5);
		}
	}
	
	// When a player dies all mushrooms must be restored for the next player.
	public void restore(Score score) {
		// Spec dictates adding 10 points if a mushroom is being restored and was hit
		if (lives != 3) {
			loadImage("src/resources/mushroom.png");
			getImageDimensions();
			score.add(10);
		}
		lives = 3;
	}
	
	public int getLives() {
		return lives;
	}
}
