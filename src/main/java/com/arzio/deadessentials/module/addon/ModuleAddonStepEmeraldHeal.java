package com.arzio.deadessentials.module.addon;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerMoveEvent;

import com.arzio.deadessentials.module.Module;
import com.arzio.deadessentials.module.ModuleType;
import com.arzio.deadessentials.module.RegisterModule;
import com.arzio.deadessentials.util.CDPotionEffectType;

@RegisterModule(name = "step-emerald-heal", defaultState = false, type = ModuleType.ADDON)
public class ModuleAddonStepEmeraldHeal extends Module {
	
	@EventHandler(ignoreCancelled = true)
	public void onWalkIntoEmerald(PlayerMoveEvent event) {
		if (event.getTo().getBlock().getRelative(BlockFace.DOWN).getType() != Material.EMERALD_BLOCK) {
			return;
		}

		Player player = event.getPlayer();
		if (player.hasPotionEffect(CDPotionEffectType.BLEEDING) 
				|| player.hasPotionEffect(CDPotionEffectType.BROKEN_LEG)
				|| player.hasPotionEffect(CDPotionEffectType.INFECTION)) {

			player.removePotionEffect(CDPotionEffectType.BLEEDING);
			player.removePotionEffect(CDPotionEffectType.BROKEN_LEG);
			player.removePotionEffect(CDPotionEffectType.INFECTION);
			event.getPlayer().playSound(player.getLocation(), Sound.ZOMBIE_UNFECT, 1.0F, 2.0F);
		}
		event.getPlayer().setFoodLevel(20);
	}

}
