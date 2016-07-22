import java.io.File;
import javax.sound.sampled.*;

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
			FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
			gainControl.setValue(0.0f);
			clip.start(); 
		} catch (Exception e) {
			e.getMessage();
		}
	}
	
	public void play(float value){
		try {
			Clip clip = AudioSystem.getClip();
			AudioInputStream inputStream = AudioSystem.getAudioInputStream(new File(soundPath));
			clip.open(inputStream);
			FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
			gainControl.setValue(value);
			clip.start(); 
		} catch (Exception e) {
			e.getMessage();
		}
	}
}
