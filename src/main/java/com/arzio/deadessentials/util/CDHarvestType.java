package com.arzio.deadessentials.util;

import org.bukkit.Material;
import org.bukkit.block.Block;

@SuppressWarnings("deprecation")
public enum CDHarvestType {
	
	WOOD(new Material[] { Material.LOG }, new Material[] { Constants.PLANKS, Constants.CHEST, Constants.WOODEN_DOOR, Constants.BARBED_WIRE, Constants.REINFORCED_BARBED_WIRE }),
	STONE(new Material[] { Material.STONE }, new Material[] { Constants.STONE_BRICK, Constants.CAMPFIRE, Constants.SAND_BARRIER }),
	NONE;
	
	private Material[] sourceBlocks;
	private Material[] baseBlocks;
	
	private CDHarvestType(Material[] sourceBlocks, Material[] baseBlocks) {
		this.sourceBlocks = sourceBlocks;
		this.baseBlocks = baseBlocks;
	}
	
	private CDHarvestType() {
		this(new Material[0], new Material[0]);
	}
	
	public boolean canHarvest(Block block) {
		return this.canHarvest(block.getType());
	}
	
	public boolean canHarvest(Material material) {
		for (Material mat : sourceBlocks) {
			if (mat == material) {
				return true;
			}
		}
		
		for (Material baseMaterial : baseBlocks) {
			if (baseMaterial == material) {
				return true;
			}
		}
		return false;
	}

	private static class Constants {
		public static final Material PLANKS = Material.getMaterial(5);
		public static final Material STONE_BRICK = Material.getMaterial(98);
		public static final Material CHEST = Material.getMaterial(54);
		public static final Material BARBED_WIRE = Material.getMaterial(1226);
		public static final Material REINFORCED_BARBED_WIRE = Material.getMaterial(1227);
		public static final Material CAMPFIRE = Material.getMaterial(1225);
		public static final Material SAND_BARRIER = Material.getMaterial(1228);
		public static final Material WOODEN_DOOR = Material.getMaterial(64);
	}
}