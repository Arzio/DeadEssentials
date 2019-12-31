package com.arzio.deadessentials.util;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public enum CDMaterial {
	BLOODBAG(9931),
	HANDCUFFS(9717),
	M4A1(9256),
	M107(9287),
	AWP(9290),
	M1_GARAND_MAGAZINE(9323),
	TRENCH_GUN(9296),
	MOSSBERG(9297),
	CROSSBOW(9305),
	TASER(9265),
	M1_GARAND(9289),
	RED_DOT_SIGHT(9331),
	ACOG(9332),
	EOTECH(9956),
	LP_SCOPE(9333),
	HP_SCOPE(9334),
	TACTICAL_GRIP(9336),
	BIPOD(9337),
	SUPPRESSOR(9335),
	FLASH_GRENADE(9906),
	DECOY_GRENADE(9907),
	SMOKE_GRENADE(9908),
	FIRE_GRENADE(9909),
	GRENADE(9910),
	PIPE_GRENADE(9911),
	GAS_GRENADE(9912),
	SPETSNAZ_CLOTHING(9559),
	FIREMAN_AXE(9362, CDHarvestType.WOOD),
	HATCHET(9370, CDHarvestType.WOOD),
	MINER_PICKAXE(9375, CDHarvestType.STONE),
	SLEDGE_HAMMER(9367, CDHarvestType.STONE);
	
	public static CDMaterial[] GRENADES = new CDMaterial[] { FLASH_GRENADE, DECOY_GRENADE, SMOKE_GRENADE, FIRE_GRENADE, GRENADE, PIPE_GRENADE, GAS_GRENADE };

	private int id;
	private CDHarvestType harvestType;
	
	private CDMaterial(int id) {
		this.id = id;
		this.harvestType = CDHarvestType.NONE;
	}
	
	private CDMaterial(int id, CDHarvestType harvestType) {
		this(id);
		this.harvestType = harvestType;
	}
	
	public int getId() {
		return this.id;
	}
	
	public CDHarvestType getHarvestType() {
		return this.harvestType;
	}
	
	@SuppressWarnings("deprecation")
	public Material asMaterial() {
		return Material.getMaterial(this.id);
	}
	
	public boolean isTypeOf(ItemStack stack) {
		return this.isTypeOf(stack.getType());
	}
	
	public boolean isTypeOf(Material material) {
		return this.asMaterial() == material;
	}
	
	public static CDMaterial getFrom(Material material) {
		for (CDMaterial cdMaterial : CDMaterial.values()) {
			if (cdMaterial.asMaterial() == material) {
				return cdMaterial;
			}
		}
		return null;
	}
	
}
