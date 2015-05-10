import java.io.File;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;


public class Sound {

	private String soundPath;
	
	public Sound(String soundPath){
		this.soundPath = soundPath;
	}
	
	public void play(){
		try {
			Clip clip = AudioSystem.getClip();
			AudioInputStream inputStream = AudioSystem.getAudioInputStream(new File(soundPath));
			clip.open(inputStream);
			clip.start(); 
		} catch (Exception e) {
			e.getMessage();
		}
	}
}
