package config;

import java.io.Serializable;
import java.time.LocalDateTime;

import com.google.gson.GsonBuilder;

public class ConfigDto implements Serializable {

	private static final long serialVersionUID = 1L;

	private boolean fullScreenOn;
	private int musicVolumeScale;
	private int seVolumeScale;
	private LocalDateTime update;

	public boolean isFullScreenOn() {
		return fullScreenOn;
	}

	public void setFullScreenOn(boolean fullScreenOn) {
		this.fullScreenOn = fullScreenOn;
	}

	public int getMusicVolumeScale() {
		return musicVolumeScale;
	}

	public void setMusicVolumeScale(int musicVolumeScale) {
		this.musicVolumeScale = musicVolumeScale;
	}

	public int getSeVolumeScale() {
		return seVolumeScale;
	}

	public void setSeVolumeScale(int seVolumeScale) {
		this.seVolumeScale = seVolumeScale;
	}

	public LocalDateTime getUpdate() {
		return update;
	}

	public void setUpdate(LocalDateTime update) {
		this.update = update;
	}

	@Override
	public String toString() {
		return new GsonBuilder().setPrettyPrinting()
				.registerTypeAdapter(LocalDateTime.class, new LocalDateTimeSerializer()).create().toJson(this);
	}

}
