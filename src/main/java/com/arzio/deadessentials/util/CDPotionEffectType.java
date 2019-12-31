package com.arzio.deadessentials.util;

import org.bukkit.potion.PotionEffectType;

public class CDPotionEffectType {

	public static final PotionEffectType INFECTION = new ModdedPotionEffect(27);
	public static final PotionEffectType BLEEDING = new ModdedPotionEffect(28);
	public static final PotionEffectType BROKEN_LEG = new ModdedPotionEffect(29);

	private int id;

	private CDPotionEffectType(int id) {
		this.id = id;
	}

	public int getId() {
		return this.id;
	}

	public static class ModdedPotionEffect extends PotionEffectType {
		public ModdedPotionEffect(int id) {
			super(id);
		}

		public double getDurationModifier() {
			return getType().getDurationModifier();
		}

		public String getName() {
			return getType().getName();
		}

		public PotionEffectType getType() {
			return this;
		}

		public boolean isInstant() {
			return getType().isInstant();
		}
	}
}
