package com.arzio.deadessentials.module.fix;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;

import com.arzio.deadessentials.module.Module;
import com.arzio.deadessentials.module.ModuleType;
import com.arzio.deadessentials.module.RegisterModule;

/**
 * Fixes the hidden armor durability when receiving bullet damage.
 * 
 * @param event
 */

@RegisterModule(name = "armor-breaking", type = ModuleType.FIX)
public class ModuleFixArmorBreaking extends Module {

	@EventHandler
	public void armorBreakingFix(EntityDamageByEntityEvent event) {
		if (!(event.getEntity() instanceof Player)) {
			return;
		}

		Player player = (Player) event.getEntity();

		ItemStack stackHelmet = player.getEquipment().getHelmet();
		if (stackHelmet != null) {
			stackHelmet.setDurability((short) 1);
			player.getEquipment().setHelmet(stackHelmet);
		}
		ItemStack stackArmor = player.getEquipment().getChestplate();
		if (stackArmor != null) {
			stackArmor.setDurability((short) 1);
			player.getEquipment().setChestplate(stackArmor);
		}
		ItemStack stackLegs = player.getEquipment().getLeggings();
		if (stackLegs != null) {
			stackLegs.setDurability((short) 1);
			player.getEquipment().setLeggings(stackLegs);
		}
	}
}
