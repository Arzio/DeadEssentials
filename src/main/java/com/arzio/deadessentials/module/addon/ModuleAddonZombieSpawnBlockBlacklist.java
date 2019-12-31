package com.arzio.deadessentials.module.addon;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;

import com.arzio.deadessentials.module.Module;
import com.arzio.deadessentials.module.ModuleType;
import com.arzio.deadessentials.module.RegisterModule;
import com.arzio.deadessentials.util.CDEntityType;
import com.arzio.deadessentials.util.YMLFile;

@RegisterModule(name = "zombie-spawn-block-blacklist", defaultState = false, type = ModuleType.ADDON)
public class ModuleAddonZombieSpawnBlockBlacklist extends Module {

	private List<Material> materialBlacklist = new ArrayList<>();
	
	@EventHandler
	public void onZombieSpawn(CreatureSpawnEvent event){
		if (event.getSpawnReason() != SpawnReason.NATURAL 
				&& event.getSpawnReason() != SpawnReason.DEFAULT
				&& event.getSpawnReason() != SpawnReason.CHUNK_GEN) {
			return;
		}
		
		for (CDEntityType type : CDEntityType.getZombieTypes()) {
			if (type.isTypeOf(event.getEntity())){
				Material materialBelowEntity = event.getLocation().getBlock().getRelative(BlockFace.DOWN).getType();
				
				if (materialBlacklist.contains(materialBelowEntity)) {
					event.setCancelled(true);
				}
			}
		}
	}
	
	@Override
	@SuppressWarnings("deprecation")
	public void onEnable() {
		YMLFile yml = this.getConfig();
		yml.saveDefaultFile();
		yml.reload();
		
		List<Integer> idList = yml.getConfig().getIntegerList("blacklisted-blocks");
		materialBlacklist.clear();
		for (int id : idList) {
			materialBlacklist.add(Material.getMaterial(id));
		}
	}

}
