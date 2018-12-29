package centipede;

import java.io.File;
import java.io.IOException;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class AudioPlayer implements Runnable {

	public AudioPlayer() {
		
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		AudioInputStream audioInputStream;
		try {
			
			audioInputStream = AudioSystem.getAudioInputStream(new File("src/resources/pew.wav").getAbsoluteFile());
			Clip clip;
			try {
				clip = AudioSystem.getClip();
				clip.open(audioInputStream);
				clip.start();
			} catch (LineUnavailableException e) {
				
				e.printStackTrace();
			}
		} catch (UnsupportedAudioFileException e) {
			
			e.printStackTrace();
		} catch (IOException e) {
			
			e.printStackTrace();
		}
		
	}

}
