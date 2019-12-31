package com.arzio.deadessentials.module.addon;

import java.util.logging.Level;

import org.bukkit.block.Biome;
import org.bukkit.event.EventHandler;
import org.bukkit.event.world.ChunkLoadEvent;

import com.arzio.deadessentials.DeadEssentials;
import com.arzio.deadessentials.module.Module;
import com.arzio.deadessentials.module.ModuleType;
import com.arzio.deadessentials.module.RegisterModule;
import com.arzio.deadessentials.util.CDBiome;
import com.arzio.deadessentials.util.YMLFile;

@RegisterModule(name = "biome-changer", type = ModuleType.ADDON)
public class ModuleAddonBiomeChanger extends Module {

	private YMLFile yml;
	
	@EventHandler(ignoreCancelled = true)
	public void onChunkLoad(ChunkLoadEvent event){
		String worldName = event.getWorld().getName();
		if (yml.getConfig().isSet("worlds."+worldName)){
			String biomeName = yml.getConfig().getString("worlds."+worldName);
			int chunkX = event.getChunk().getX()*16;
			int chunkZ = event.getChunk().getZ()*16;
			try {
				Biome biome = CDBiome.valueOf(biomeName).asBiome();
				if (biome != null){
					for (int x = chunkX; x < chunkX+16; x++){
						for (int z = chunkZ; z < chunkZ+16; z++){
							event.getWorld().setBiome(x, z, biome);
						}
					}
				}
			} catch (IllegalArgumentException e){
				DeadEssentials.getInstance().getLogger().log(Level.SEVERE, "ERROR: Biome type '"+biomeName+"' does not exists!");
			}
		}
	}

	@Override
	public void onEnable() {
		yml = this.getConfig();
		yml.saveDefaultFile();
	}

}
