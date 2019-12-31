package com.arzio.deadessentials.wrapper.item.impl;

import java.lang.reflect.Field;

import org.bukkit.Material;

import com.arzio.deadessentials.util.exception.CDAReflectionException;
import com.arzio.deadessentials.util.reflection.CDClasses;
import com.arzio.deadessentials.wrapper.item.Ammo;

public class AmmoImpl extends CDItemImpl implements Ammo{

	public AmmoImpl(Material material) {
		super(material);
	}

	@Override
	public double getPenetration() {
		return CDClasses.ITEMMAGAZINE_PENETRATION_FIELD.getValue(this.getItemInstance());
	}

	@Override
	public void setPenetration(double value) {
		CDClasses.ITEMMAGAZINE_PENETRATION_FIELD.setValue(this.getItemInstance(), value);
	}

	@Override
	public int getBulletAmount() {
		return CDClasses.ITEMMAGAZINE_BULLET_AMOUNT_FIELD.getValue(this.getItemInstance());
	}

	@Override
	public void setBulletAmount(int amount) {
		CDClasses.ITEMMAGAZINE_BULLET_AMOUNT_FIELD.setValue(this.getItemInstance(), amount);
		
		try {
			Field durabilityField = net.minecraft.server.v1_6_R3.Item.class.getDeclaredField("durability");
			durabilityField.setAccessible(true);
			durabilityField.setInt(this.getItemInstance(), amount + 1);
		} catch (Exception e) {
			throw new CDAReflectionException(e);
		}
		
	}

}
