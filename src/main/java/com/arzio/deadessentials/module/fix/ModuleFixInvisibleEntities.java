package com.arzio.deadessentials.module.fix;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.scheduler.BukkitRunnable;

import com.arzio.deadessentials.DeadEssentials;
import com.arzio.deadessentials.module.Module;
import com.arzio.deadessentials.module.ModuleType;
import com.arzio.deadessentials.module.RegisterModule;
import com.arzio.deadessentials.util.EntityUtil;

@RegisterModule(name = "invisible-entities", type = ModuleType.FIX)
public class ModuleFixInvisibleEntities extends Module{

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerTeleport(PlayerTeleportEvent event) {
		// If the distance of teleport is less than 20, simply do nothing.
		if (event.getTo().getWorld() == event.getFrom().getWorld() && event.getFrom().distance(event.getTo()) < 20.0D) {
			return;
		}
		
		final Player player = event.getPlayer();
		
		// Refresh all the trackers for this player
		BukkitRunnable runnable = new BukkitRunnable() {
			public void run() {
				if (player.isValid()) {
					EntityUtil.refreshEntityTrackers(player);
				}
			}
		};
		runnable.runTaskLater(DeadEssentials.getInstance(), 20L);
	}

}
