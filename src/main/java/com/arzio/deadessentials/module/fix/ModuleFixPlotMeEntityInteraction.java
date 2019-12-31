package com.arzio.deadessentials.module.fix;

import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerInteractEntityEvent;

import com.arzio.deadessentials.module.Module;
import com.arzio.deadessentials.module.ModuleType;
import com.arzio.deadessentials.module.RegisterModule;
import com.arzio.deadessentials.util.CDEntityType;
import com.worldcretornica.plotme.PlotManager;

import fr.xephi.authme.api.API;

@RegisterModule(name = "plotme-entity-interaction", type = ModuleType.FIX)
public class ModuleFixPlotMeEntityInteraction extends Module {

	@EventHandler(ignoreCancelled = false, priority = EventPriority.MONITOR)
	public void fixInteraction(PlayerInteractEntityEvent event) {
		if (API.isAuthenticated(event.getPlayer())) {
			if (PlotManager.isPlotWorld(event.getPlayer())) {
				Entity clickedEntity = event.getRightClicked();
				
				if (CDEntityType.GROUND_ITEM.isTypeOf(clickedEntity)
						|| CDEntityType.C4.isTypeOf(clickedEntity)
						|| CDEntityType.CORPSE.isTypeOf(clickedEntity)) {
					event.setCancelled(false);
				}
			}
		}
	}

}
