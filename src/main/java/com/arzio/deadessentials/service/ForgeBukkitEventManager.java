package com.arzio.deadessentials.service;

import java.util.Collection;

import com.arzio.deadessentials.listener.ForgeListener;

import org.bukkit.plugin.Plugin;

public interface ForgeBukkitEventManager {

	public void registerEvents(Plugin plugin, ForgeListener listener);

	public void unregisterEvents(ForgeListener listener);

	public void unregisterEvents(Plugin plugin);

	public void unregisterAll();

	public Collection<Plugin> getRegisteredPlugins();

	public Collection<ForgeListener> getListeners(Plugin plugin);
}
