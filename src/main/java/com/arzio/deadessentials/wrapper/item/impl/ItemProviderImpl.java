package com.arzio.deadessentials.wrapper.item.impl;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import com.arzio.deadessentials.util.reflection.CDClasses;
import com.arzio.deadessentials.wrapper.item.CDItem;
import com.arzio.deadessentials.wrapper.item.ItemProvider;

import net.minecraft.server.v1_6_R3.Item;

public class ItemProviderImpl implements ItemProvider {

	private Map<Material, CDItem> materialProviderMap = new ConcurrentHashMap<>();
	
	@Override
	public <T extends CDItem> T getStackAs(Class<T> clazz, ItemStack stack) {
		return stack == null ? null : this.getMaterialAs(clazz, stack.getType());
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public <T extends CDItem> T getMaterialAs(Class<T> clazz, Material material) {
		CDItem item = this.getCDItemFrom(material);
		
		if (item == null) return null;
		
		if (clazz.isInstance(item)) {
			return (T) item;
		}
		
		return null;
	}
	
	@Override
	public CDItem getCDItemFrom(ItemStack stack) {
		return this.getCDItemFrom(stack.getType());
	}
	
	@Override
	public CDItem getCDItemFrom(Material material) {
		if (!isMaterialFromClass(material, CDClasses.ITEMCD_CLASS)) {
			return null;
		}
		
		CDItem item = materialProviderMap.get(material);
		
		if (item == null) {
			if (isMaterialFromClass(material, CDClasses.ITEMGUN_CLASS)) {
				item = new GunImpl(material);
			} else if (isMaterialFromClass(material, CDClasses.ITEMMAGAZINE_CLASS)) {
				item = new AmmoImpl(material);
			} else {
				item = new CDItemImpl(material);
			}
			
			materialProviderMap.put(material, item);
		}
		
		return item;
	}

	@SuppressWarnings("deprecation")
	private static boolean isMaterialFromClass(Material material, Class<?> clazz) {
		if (material == null) return false;
		return clazz.isInstance(Item.byId[material.getId()]);
	}
}
