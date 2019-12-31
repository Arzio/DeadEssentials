package com.arzio.deadessentials.module.fix;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.ItemStack;

import com.arzio.deadessentials.module.Module;
import com.arzio.deadessentials.module.ModuleType;
import com.arzio.deadessentials.module.RegisterModule;
import com.arzio.deadessentials.util.CDMaterial;
import com.arzio.deadessentials.util.region.Flags;

@RegisterModule(name = "item-usage-on-regions-without-pvp", type = ModuleType.FIX)
public class ModuleFixItemUsageOnRegions extends Module {
	
	@EventHandler
	public void onInteract(PlayerInteractEntityEvent event) {
		// Continue only if the clicked entity is a player
		if (!(event.getRightClicked() instanceof Player)) {
			return;
		}
		
		ItemStack stack = event.getPlayer().getItemInHand();
		
		// Check if the clicker is holding any item
		if (stack == null) {
			return;
		}
		
		// First, checks if its one of the disabled items.
		// Only disable the pvp if it is disabled in any of both locations.
		if (stack.getType() == CDMaterial.BLOODBAG.asMaterial() || stack.getType() == CDMaterial.HANDCUFFS.asMaterial()) {
			if (!Flags.canRegionHavePvP(event.getPlayer().getLocation()) || !Flags.canRegionHavePvP(event.getRightClicked().getLocation())) {
				event.setCancelled(true);
				event.getPlayer().sendMessage("§cPVP is disabled here!");
			}
		}
	}

}
