package com.arzio.deadessentials.wrapper.item;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public interface ItemProvider {
	
	public <T extends CDItem> T getStackAs(Class<T> clazz, ItemStack stack);
	
	public <T extends CDItem> T getMaterialAs(Class<T> clazz, Material stack);
	
	public CDItem getCDItemFrom(ItemStack stack);
	
	public CDItem getCDItemFrom(Material material);
}
