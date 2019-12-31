package com.arzio.deadessentials.module.addon;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import com.arzio.deadessentials.module.Module;
import com.arzio.deadessentials.module.ModuleType;
import com.arzio.deadessentials.module.RegisterModule;
import com.arzio.deadessentials.util.region.EasyStateFlag;
import com.sk89q.worldguard.protection.flags.StateFlag;

import net.sacredlabyrinth.phaed.simpleclans.SimpleClans;
import net.sacredlabyrinth.phaed.simpleclans.managers.SettingsManager;

@RegisterModule(name = "simpleclans-flags", type = ModuleType.ADDON)
public class ModuleAddonSimpleClansFlags extends Module{

	public static final EasyStateFlag FORCE_CLAN_FF = new EasyStateFlag(new StateFlag("force-clan-ff", false));
	private Boolean latestFFState;
	
	@Override
	public void onEnable() {
		FORCE_CLAN_FF.register();
	}
	
	@EventHandler(ignoreCancelled = false, priority = EventPriority.LOWEST)
	public void beforeApplySimpleClansFF(EntityDamageByEntityEvent event) {
		// Both attacker and victim are players
		if (event.getEntity() instanceof Player && event.getDamager() instanceof Player) {
			
			if (FORCE_CLAN_FF.isAllowed(event.getEntity().getLocation())) {
				SettingsManager settings = SimpleClans.getInstance().getSettingsManager();
				
				// Saves the current Global FF state
				this.latestFFState = settings.isGlobalff();
				
				// Allows friendly-fire for a while
				settings.setGlobalff(true);
			}
		}
	}
	
	@EventHandler(ignoreCancelled = false, priority = EventPriority.MONITOR)
	public void afterApplySimpleClansFF(EntityDamageByEntityEvent event) {
		// Both attacker and victim are players
		if (event.getEntity() instanceof Player && event.getDamager() instanceof Player) {
			
			// Sets the Global FF value to its previous state.
			// This listener is called after all the other common listeners due to its high priority.
			if (latestFFState != null) {
				SimpleClans.getInstance().getSettingsManager().setGlobalff(this.latestFFState);
				latestFFState = null;
			}
		}
	}

}
