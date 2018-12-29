package centipede;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.util.List;

public class Score {
	
	private static int score;
	public static int lives;
	private static Font mediumFont = new Font("Helvetica", Font.BOLD, 16);
	
	
	public Score() {
		
		score = 0;
		lives = 3;
	}
	
	public boolean loseLife() {
		
		lives--;
		
		if (lives == 0) {
			return true;
		}
		
		return false;
	}
	
	public void add(int n) {
		score += n;
	}
	
	public int getScore() {
		return score;
	}
	
	public void drawScore(Graphics g) {
		
		String scoreStr = "Score: " + score;
		String livesStr = "Lives: " + lives;
		FontMetrics fm = g.getFontMetrics(mediumFont);
		
		g.setColor(Color.white);
		g.setFont(mediumFont);
		g.drawString(scoreStr, (500 - fm.stringWidth(scoreStr) - 10), 16);
		g.drawString(livesStr, (500 - fm.stringWidth(livesStr) - 10), 32);
	}
}
