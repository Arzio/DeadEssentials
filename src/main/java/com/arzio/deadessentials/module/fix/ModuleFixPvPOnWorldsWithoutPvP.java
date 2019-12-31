package com.arzio.deadessentials.module.fix;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import com.arzio.deadessentials.module.Module;
import com.arzio.deadessentials.module.ModuleType;
import com.arzio.deadessentials.module.RegisterModule;

@RegisterModule(name = "damage-on-worlds-without-pvp", type = ModuleType.FIX)
public class ModuleFixPvPOnWorldsWithoutPvP extends Module {

	@EventHandler(priority = EventPriority.LOWEST)
	public void onAttack(EntityDamageByEntityEvent event) {
		if (event.getEntity() instanceof Player && event.getDamager() instanceof Player) {
			if (!event.getEntity().getWorld().getPVP()) {
				event.setCancelled(true);
			}
		}
	}

}
