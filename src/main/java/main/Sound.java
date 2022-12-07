package main;

import java.net.URL;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

public class Sound {

	private Clip clip;
	private URL[] soundURL = new URL[30];

	public Sound() {
		this.soundURL[0] = getClass().getResource("/sounds/BlueBoyAdventure.wav");
		this.soundURL[1] = getClass().getResource("/sounds/coin.wav");
		this.soundURL[2] = getClass().getResource("/sounds/powerup.wav");
		this.soundURL[3] = getClass().getResource("/sounds/unlock.wav");
		this.soundURL[4] = getClass().getResource("/sounds/fanfare.wav");
		this.soundURL[5] = getClass().getResource("/sounds/receivedamage.wav");
	}

	public void setFile(int index) {
		try {
			AudioInputStream ais = AudioSystem.getAudioInputStream(this.soundURL[index]);
			this.clip = AudioSystem.getClip();
			this.clip.open(ais);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void play() {
		this.clip.start();
	}

	public void loop() {
		this.clip.loop(Clip.LOOP_CONTINUOUSLY);
	}

	public void stop() {
		this.clip.stop();
	}

}













