package com.arzio.deadessentials.module.addon;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.scheduler.BukkitTask;

import com.arzio.deadessentials.DeadEssentials;
import com.arzio.deadessentials.module.Module;
import com.arzio.deadessentials.module.ModuleType;
import com.arzio.deadessentials.module.RegisterModule;
import com.arzio.deadessentials.service.UpdateChecker;
import com.arzio.deadessentials.service.UpdateChecker.CheckMethod;
import com.arzio.deadessentials.service.UpdateChecker.UpdateState;
import com.arzio.deadessentials.service.impl.GitHubUpdateChecker;

@RegisterModule(name = "update-checker", type = ModuleType.ADDON)
public class ModuleAddonUpdateChecker extends Module {

	private UpdateChecker updateChecker;
	private BukkitTask checkerTask;

	@Override
	public void onEnable() {
		this.updateChecker = new GitHubUpdateChecker(this.getPlugin(), "https://api.github.com/repos/Arzio/DeadEssentials/releases",
		"https://github.com/Arzio/DeadEssentials/releases");

		// Checks for new updates using another thread
		this.updateChecker.checkUpdates(CheckMethod.ASYNC);

		// Automatically checks for updates in a async way every 10 minutes
		this.checkerTask = Bukkit.getScheduler().runTaskTimer(this.getPlugin(), new Runnable() {

			@Override
			public void run() {
				updateChecker.checkUpdates(CheckMethod.ASYNC);
			}

		}, 600L * 20L, 600L * 20L); // Checks for update every 10 minutes
	}

	@Override
	public void onDisable() {
		if (this.checkerTask != null) this.checkerTask.cancel();
	}

	@EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
	public void onAdminJoin(PlayerJoinEvent event) {
		if (event.getPlayer().isOp() || event.getPlayer().hasPermission("deadessentials.warnings")) {
			final Player player = event.getPlayer();

			if (updateChecker.getState() == UpdateState.NEEDS_UPDATE) {
				Bukkit.getScheduler().runTaskLater(DeadEssentials.getInstance(), new Runnable() {

					@Override
					public void run() {
						if (!player.isValid()) {
							return;
						}

						String latestVersion = updateChecker.getLatestVersionTag();

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
