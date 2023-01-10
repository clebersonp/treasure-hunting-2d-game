package main;

import java.net.URL;
import java.util.Objects;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineEvent;

public class Sound {

	private Clip clip;
	private FloatControl floatControl;
	private URL[] soundURL = new URL[30];

	public static int musicVolumeScale = 2;
	public static float musicVolume;
	public static int seVolumeScale = musicVolumeScale;
	public static float seVolume;

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
		this.soundURL[BURNING] = getClass().getResource("/sounds/burning.wav");
		this.soundURL[CUT_TREE] = getClass().getResource("/sounds/cuttree.wav");
		this.soundURL[GAME_OVER] = getClass().getResource("/sounds/gameover.wav");
		this.soundURL[STAIRS] = getClass().getResource("/sounds/stairs.wav");
		this.soundURL[MERCHANT_MUSIC] = getClass().getResource("/sounds/Merchant.wav");
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

	/**
	 * Index {@code 10}
	 */
	public static int BURNING = 10;

	/**
	 * Index {@code 11}
	 */
	public static int CUT_TREE = 11;
	
	/**
	 * Index {@code 12}
	 */
	public static int GAME_OVER = 12;

	/**
	 * Index {@code 13}
	 */
	public static int STAIRS = 13;

	/**
	 * Index {@code 14}
	 */
	public static int MERCHANT_MUSIC = 14;

	private boolean music;

	public Sound(int indexSound, boolean isMusic) {
		this.music = isMusic;
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

			this.floatControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
			if (this.music) {
				this.checkMusicVolume();
			} else {
				this.checkSeVolume();
			}

			this.clip.start();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void loop() {
		this.clip.loop(Clip.LOOP_CONTINUOUSLY);
	}

	public int getMusicVolumeScale() {
		return Sound.musicVolumeScale;
	}

	public void setMusicVolumeScale(int musicVolumeScale) {
		Sound.musicVolumeScale = musicVolumeScale;
	}

	public static int getSeVolumeScale() {
		return Sound.seVolumeScale;
	}

	public static void setSeVolumeScale(int seVolumeScale) {
		Sound.seVolumeScale = seVolumeScale;
	}

	public float getMusicVolume() {
		return Sound.musicVolume;
	}

	public void setMusicVolume(float musicVolume) {
		Sound.musicVolume = musicVolume;
	}

	public static float getSeVolume() {
		return Sound.seVolume;
	}

	public static void setSeVolume(float seVolume) {
		Sound.seVolume = seVolume;
	}

	public void stop() {
		this.clip.stop();
		this.clip.flush();
		this.clip.close();
	}

	public void checkMusicVolume() {
		switch (Sound.musicVolumeScale) {
		case 0 -> Sound.musicVolume = -80F;
		case 1 -> Sound.musicVolume = -20F;
		case 2 -> Sound.musicVolume = -12F;
		case 3 -> Sound.musicVolume = -5F;
		case 4 -> Sound.musicVolume = 1F;
		case 5 -> Sound.musicVolume = 6F;
		}
		this.floatControl.setValue(Sound.musicVolume);
	}

	private void checkSeVolume() {
		switch (Sound.seVolumeScale) {
		case 0 -> Sound.seVolume = -80F;
		case 1 -> Sound.seVolume = -20F;
		case 2 -> Sound.seVolume = -12F;
		case 3 -> Sound.seVolume = -5F;
		case 4 -> Sound.seVolume = 1F;
		case 5 -> Sound.seVolume = 6F;
		}
		this.floatControl.setValue(Sound.seVolume);
	}

}
