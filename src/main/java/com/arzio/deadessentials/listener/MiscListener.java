package com.arzio.deadessentials.listener;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import com.arzio.deadessentials.DeadEssentials;
import com.arzio.deadessentials.service.UpdateChecker.UpdateState;

public class MiscListener implements Listener {

	@EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
	public void onAdminJoin(PlayerJoinEvent event) {

		// As some servers does not uses OP, we check for any common permission
		if (event.getPlayer().isOp() || event.getPlayer().hasPermission("deadessentials.warnings")) {
			final Player player = event.getPlayer();

			if (DeadEssentials.getInstance().getUpdateChecker().getState() == UpdateState.NEEDS_UPDATE) {
				Bukkit.getScheduler().runTaskLater(DeadEssentials.getInstance(), new Runnable() {

					@Override
					public void run() {
						if (!player.isValid()) {
							return;
						}

						String latestVersion = DeadEssentials.getInstance().getUpdateChecker().getLatestVersionTag();

						player.sendMessage(" ");
						player.sendMessage("§a[DeadEssentials] §eA atualização " + latestVersion
								+ " está pronta para ser baixada!");
						player.sendMessage(
								"§a[DeadEssentials] §e§oThe " + latestVersion + " update is ready to be downloaded!");
						player.sendMessage(
								"§a[DeadEssentials] §fDownload it here NOW: §bhttps://github.com/Arzio/DeadEssentials/releases");
						player.sendMessage(" ");

						player.playSound(player.getLocation(), Sound.NOTE_PLING, 2F, 2F);
					}
					
				}, 5 * 20L);
			}
		}
	}

}
