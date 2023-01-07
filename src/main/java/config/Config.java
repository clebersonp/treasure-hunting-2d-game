package config;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.Objects;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import main.GamePanel;
import main.Sound;

public class Config {

	private GamePanel gp;
	private Gson gson;

	public Config(GamePanel gp) {
		this.gp = gp;
		this.gson = new GsonBuilder().setPrettyPrinting()
				.registerTypeAdapter(LocalDateTime.class, new LocalDateTimeSerializer())
				.registerTypeAdapter(LocalDateTime.class, new LocalDateTimeDeserializer()).create();
	}

	public void save() {
		try (BufferedWriter bw = Files.newBufferedWriter(Paths.get("config.json"));) {

			ConfigDto configDto = new ConfigDto();
			configDto.setFullScreenOn(this.gp.isFullScreenOn());
			configDto.setMusicVolumeScale(this.gp.getMusic().getMusicVolumeScale());
			configDto.setSeVolumeScale(Sound.getSeVolumeScale());
			configDto.setUpdate(LocalDateTime.now());

			bw.write(gson.toJson(configDto));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void load() {
		try (BufferedReader br = Files.newBufferedReader(Paths.get("config.json"))) {

			ConfigDto configDto = this.gson.fromJson(br, ConfigDto.class);
			if (Objects.nonNull(configDto)) {
				this.gp.setFullScreenOn(configDto.isFullScreenOn());
				this.gp.getMusic().setMusicVolumeScale(configDto.getMusicVolumeScale());
				Sound.setSeVolumeScale(configDto.getSeVolumeScale());
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
