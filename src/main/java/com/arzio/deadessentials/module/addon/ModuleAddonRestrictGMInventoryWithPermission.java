package com.arzio.deadessentials.module.addon;

import org.bukkit.entity.HumanEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryCreativeEvent;

import com.arzio.deadessentials.module.Module;
import com.arzio.deadessentials.module.ModuleType;
import com.arzio.deadessentials.module.RegisterModule;

@RegisterModule(name = "restrict-gm-inventory-with-permission", type = ModuleType.ADDON, defaultState = false)
public class ModuleAddonRestrictGMInventoryWithPermission extends Module {

	@EventHandler
	public void onCreativeInventory(InventoryCreativeEvent event) {
		HumanEntity human = event.getWhoClicked();

		boolean hasPermission = human.hasPermission("craftingdead.restrict.gm")
				|| human.hasPermission("deadessentials.restrictgm");

		if (hasPermission && !human.isOp()) {
			event.setCancelled(true);
		}
	}

}
