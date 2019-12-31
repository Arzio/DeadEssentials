package com.arzio.deadessentials.wrapper.item.impl;

import org.bukkit.Material;

import com.arzio.deadessentials.wrapper.item.CDItem;

import net.minecraft.server.v1_6_R3.Item;

public class CDItemImpl implements CDItem {

	private Material material;
	
	public CDItemImpl(Material material) {
		this.material = material;
	}

	@Override
	public Material getItem() {
		return material;
	}
	
	@SuppressWarnings("deprecation")
	public Item getItemInstance() {
		return Item.byId[material.getId()];
	}

	@Override
	public String getName() {
		return getItemInstance().getName().replace("item.", "");
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((material == null) ? 0 : material.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CDItemImpl other = (CDItemImpl) obj;
		if (material != other.material)
			return false;
		return true;
	}
	
}
