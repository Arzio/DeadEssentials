package com.arzio.deadessentials.util;

import java.util.List;

import org.bukkit.craftbukkit.v1_6_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_6_R3.entity.CraftEntity;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;

import net.minecraft.server.v1_6_R3.EntityTrackerEntry;
import net.minecraft.server.v1_6_R3.WorldServer;

public class EntityUtil {
	
	@SuppressWarnings("deprecation")
	public static EntityType getCompatibleEntityType(Entity entity) {
		return EntityType.fromId(CauldronUtils.getEntityTypeIDfromClass(((CraftEntity) entity).getHandle()));
	}
	
	@SuppressWarnings("unchecked")
	public static void refreshEntityTrackers(Entity entity) {
		WorldServer worldServer = ((CraftWorld) entity.getWorld()).getHandle();
		EntityTrackerEntry entry = (EntityTrackerEntry) worldServer.tracker.trackedEntities.get(entity.getEntityId());
		List<?> nmsPlayers = worldServer.players;

		// Remove the players from the observer list
		entry.trackedPlayers.removeAll(nmsPlayers);
		
		// Add the players again to the observer list
		entry.scanPlayers(nmsPlayers);
	}
}
