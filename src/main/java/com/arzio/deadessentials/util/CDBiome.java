package com.arzio.deadessentials.util;

import org.bukkit.block.Biome;

public enum CDBiome {
	ZOMBIE_FOREST(28, "Zombie Forest"),
	ZOMBIE_DESERT(29, "Zombie Desert"),
	ZOMBIE_PLAINS(30, "Zombie Plains"),
	ZOMBIE_SNOW(31, "Zombie Snow"),
	ZOMBIE_WASTELAND(32, "Zombie Wasteland");
	
	private String name;
	private int id;
	
	private CDBiome(int id, String name) {
		this.id = id;
		this.name = name;
	}
	
	public int getId() {
		return this.id;
	}
	
	public String getName() {
		return this.name;
	}
	
	public Biome asBiome() {
		return Biome.valueOf(this.name);
	}
}
