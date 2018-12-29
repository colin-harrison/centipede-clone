package centipede;

import java.awt.Cursor;
import java.awt.EventQueue;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import javax.swing.JFrame;

public class Main extends JFrame {

	private final int B_WIDTH = 512;
	private final int B_HEIGHT = 800;
	
	public Main(double likelihood) {
		
		initUI(likelihood);
	}
	
	private void initUI(double likelihood) {
		
		add(new Board(likelihood));
		
		// https://stackoverflow.com/questions/1984071/how-to-hide-cursor-in-a-swing-application
		// Transparent 16 x 16 pixel cursor image.
		BufferedImage cursorImg = new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB);

		// Create a new blank cursor.
		Cursor blankCursor = Toolkit.getDefaultToolkit().createCustomCursor(
		    cursorImg, new Point(0, 0), "blank cursor");

		// Set the blank cursor to the JFrame.
		this.getContentPane().setCursor(blankCursor);
		
		setTitle("Centipede");
		setSize(B_WIDTH, B_HEIGHT);
		
		setLocationRelativeTo(null);
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
	public static void main(String[] args) {
		
		EventQueue.invokeLater(() -> {
            Main ex = new Main(Double.parseDouble(args[0]));
            ex.setVisible(true);
        });
	}

}
