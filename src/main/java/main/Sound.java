package main;

import java.net.URL;
import java.util.Objects;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineEvent;

public class Sound {

	private Clip clip;
	private URL[] soundURL = new URL[30];
	{
		this.soundURL[BLUE_BOY_ADVENTURE] = getClass().getResource("/sounds/BlueBoyAdventure.wav");
		this.soundURL[COIN] = getClass().getResource("/sounds/coin.wav");
		this.soundURL[POWER_UP] = getClass().getResource("/sounds/powerup.wav");
		this.soundURL[UNLOCK] = getClass().getResource("/sounds/unlock.wav");
		this.soundURL[FANFARE] = getClass().getResource("/sounds/fanfare.wav");
		this.soundURL[HIT_MONSTER] = getClass().getResource("/sounds/hitmonster.wav");
		this.soundURL[RECEIVE_DAMAGE] = getClass().getResource("/sounds/receivedamage.wav");
		this.soundURL[SWING_WEAPON] = getClass().getResource("/sounds/swingweapon.wav");
		this.soundURL[LEVEL_UP] = getClass().getResource("/sounds/levelup.wav");
		this.soundURL[INVENTORY_CURSOR] = getClass().getResource("/sounds/cursor.wav");
	}
	private int index;

	/**
	 * Index {@code 0}
	 */
	public static int BLUE_BOY_ADVENTURE = 0;

	/**
	 * Index {@code 1}
	 */
	public static int COIN = 1;

	/**
	 * Index {@code 2}
	 */
	public static int POWER_UP = 2;

	/**
	 * Index {@code 3}
	 */
	public static int UNLOCK = 3;

	/**
	 * Index {@code 4}
	 */
	public static int FANFARE = 4;

	/**
	 * Index {@code 5}
	 */
	public static int HIT_MONSTER = 5;

	/**
	 * Index {@code 6}
	 */
	public static int RECEIVE_DAMAGE = 6;

	/**
	 * Index {@code 7}
	 */
	public static int SWING_WEAPON = 7;

	/**
	 * Index {@code 8}
	 */
	public static int LEVEL_UP = 8;

	/**
	 * Index {@code 9}
	 */
	public static int INVENTORY_CURSOR = 9;

	public Sound(int indexSound) {
		if (indexSound < 0 || indexSound > this.soundURL.length - 1) {
			throw new IllegalArgumentException("Index Sound Not Exists!");
		}
		this.index = indexSound;
	}

	public void play() {
		try (AudioInputStream ais = AudioSystem.getAudioInputStream(this.soundURL[this.index]);) {
			if (Objects.isNull(this.clip)) {
				this.clip = AudioSystem.getClip();
				this.clip.addLineListener(event -> {
					if (LineEvent.Type.STOP.equals(event.getType())) {
						this.clip.close();
					}
				});
			}
			if (!this.clip.isOpen()) {
				this.clip.open(ais);
			}
			this.clip.start();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void loop() {
		this.clip.loop(Clip.LOOP_CONTINUOUSLY);
	}

	public void stop() {
		this.clip.stop();
		this.clip.flush();
		this.clip.close();
	}

}
