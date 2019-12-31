package com.arzio.deadessentials.util;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

public class YMLFile {

	private final File file;
	private FileConfiguration config;
	private final Plugin plugin;
	private final String filePath;
	
	public YMLFile(Plugin plugin, String filePath) {
		this.filePath = filePath;
		this.plugin = plugin;
		this.file = new File(plugin.getDataFolder(), filePath);
		
		// Makes every parent dirs
		File parent = file.getParentFile();
		if (!parent.exists() && !parent.mkdirs()) {
			throw new IllegalStateException("Could not create dir: " + parent);
		}
		
		this.reload();
	}
	
	public void saveDefaultFile() {
		if (!this.file.exists()) {
			plugin.saveResource(this.filePath, false);
			this.reload();
		}
	}
	
	@SuppressWarnings("unchecked")
	public <U> U getValueWithDefault(String path, U firstValue) {
		if (!config.contains(path)) {
			config.set(path, firstValue);
		}
		try {
			return (U) config.get(path);
		} catch (Throwable t) {
			this.plugin.getLogger().log(Level.SEVERE, "Failed to get the current value of the config file for the path '"+path+"'. Getting the default value instead.", t);
		}
		return firstValue;
	}
	
	public FileConfiguration getConfig() {
		return this.config;
	}
	
	public File getFile() {
		return this.file;
	}
	
	public Plugin getPlugin() {
		return this.plugin;
	}
	
	public String getFilePath() {
		return this.filePath;
	}
	
	public void reload() {
		this.config = YamlConfiguration.loadConfiguration(this.file);
	}
	
	public void save() {
		try {
			this.config.save(this.file);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
