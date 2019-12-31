package com.arzio.deadessentials.service;

import org.bukkit.plugin.Plugin;

public interface UpdateChecker {

	public UpdateState getState();
	
	public String getLatestVersionTag();
	
	public void checkUpdates(CheckMethod method);
	
	public String getPluginPageURL();
	
	public String getApiURL();
	
	public Plugin getPlugin();
	
	public static enum UpdateState {
		UP_TO_DATE, NEEDS_UPDATE, FAILED_TO_CHECK;
	}

	public static enum CheckMethod {
		/** It will use the current thread to check for updates */
		SYNC,
		/** It will run in a async way, from Bukkit's scheduler */
		ASYNC;
	}
}
