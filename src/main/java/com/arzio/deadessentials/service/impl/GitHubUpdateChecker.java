package com.arzio.deadessentials.service.impl;

import java.net.URL;
import java.util.logging.Logger;

import org.apache.commons.io.IOUtils;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import com.arzio.deadessentials.service.UpdateChecker;

/**
 * Works only for GitHub.
 */
public class GitHubUpdateChecker implements UpdateChecker {

	private final String apiURL;
	private final String pluginPageURL;
	private String latestVersionTag;
	private UpdateState state = UpdateState.UP_TO_DATE;
	private final Plugin plugin;
	
	public GitHubUpdateChecker(Plugin plugin, String apiURL, String pluginPageURL) {
		this.apiURL = apiURL;
		this.plugin = plugin;
		this.pluginPageURL = pluginPageURL;
	}

	@Override
	public UpdateState getState() {
		return this.state;
	}

	/**
	 * Gets the latest version previously retrieved from GitHub. In case of
	 * connection errors, it returns the current plugin version.
	 * 
	 * @return String with the latest plugin version
	 */
	@Override
	public String getLatestVersionTag() {
		return this.state == UpdateState.FAILED_TO_CHECK ? this.plugin.getDescription().getVersion()
				: this.latestVersionTag;
	}

	@Override
	public void checkUpdates(CheckMethod method) {
		Logger logger = this.plugin.getLogger();
		logger.info("Checking for "+plugin.getName()+" updates...");

		Runnable runnable = new Runnable() {

			@Override
			public void run() {

				try {
					JSONArray updates = (JSONArray) new JSONParser().parse(IOUtils.toString(new URL(apiURL)));
					
					boolean isUpToDate = false;
					
					if (updates.size() <= 0) {
						isUpToDate = true;
					} else {
						// Gets the first element of the array (most updated version)
						JSONObject release = (JSONObject) updates.get(0); 

						// Updates the latest version tag obtained from GitHub
						GitHubUpdateChecker.this.latestVersionTag = (String) release.get("tag_name");

						isUpToDate = plugin.getDescription().getVersion().equalsIgnoreCase(latestVersionTag);
					}

					if (isUpToDate) {
						GitHubUpdateChecker.this.state = UpdateState.UP_TO_DATE;
						plugin.getLogger().info(plugin.getName()+" is up-to-date!");
					} else {
						GitHubUpdateChecker.this.state = UpdateState.NEEDS_UPDATE;
						plugin.getLogger().info(
								"There is a new "+plugin.getName()+" version to download! Download it at "+pluginPageURL);
					}
				} catch (Exception e) {
					GitHubUpdateChecker.this.state = UpdateState.FAILED_TO_CHECK;
					plugin.getLogger().warning(
							plugin.getName()+" could not get information about the latest updates. Check your firewall or internet connection!");
					e.printStackTrace();
				}
			}
		};

		switch (method) {
		case ASYNC:
			Bukkit.getScheduler().runTaskAsynchronously(this.plugin, runnable);
			break;
		case SYNC:
			runnable.run();
			break;
		}
	}

	@Override
	public String getPluginPageURL() {
		return pluginPageURL;
	}

	@Override
	public String getApiURL() {
		return apiURL;
	}

	@Override
	public Plugin getPlugin() {
		return plugin;
	}


}
