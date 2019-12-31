package com.arzio.deadessentials.module.fix;

import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_6_R3.CraftServer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.server.PluginDisableEvent;

import com.arzio.deadessentials.module.Module;
import com.arzio.deadessentials.module.ModuleType;
import com.arzio.deadessentials.module.RegisterModule;

@RegisterModule(name = "kick-players-while-server-stop", type = ModuleType.FIX)
public class ModuleFixPlayersOnlineWhileServerIsStopping extends Module {

	@EventHandler(ignoreCancelled = true, priority = EventPriority.LOW)
	public void onPluginDisable(PluginDisableEvent event){
		CraftServer craftServer = (CraftServer) Bukkit.getServer();
		boolean isServerStopping = !craftServer.getServer().isRunning();

		if (isServerStopping){
			for (Player player : Bukkit.getOnlinePlayers()){
				player.kickPlayer(Bukkit.getServer().getShutdownMessage());
			}
		}
	}

}
