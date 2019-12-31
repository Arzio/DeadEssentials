package com.arzio.deadessentials.module.core;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerPortalEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

import com.arzio.deadessentials.event.region.RegionBorderEvent;
import com.arzio.deadessentials.event.region.RegionBorderEvent.CrossType;
import com.arzio.deadessentials.module.Module;
import com.arzio.deadessentials.module.ModuleType;
import com.arzio.deadessentials.module.RegisterModule;
import com.arzio.deadessentials.util.region.Flags;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;

@RegisterModule(name = "worldguard-region-events", type = ModuleType.CORE)
public class ModuleCoreWorldGuardRegionEvents extends Module{

	private Map<Player, Set<ProtectedRegion>> playerRegions = new HashMap<>();
	
	public Set<ProtectedRegion> getRegionsAtPlayer(Player player){
		Set<ProtectedRegion> regions = this.playerRegions.get(player);
		
		if (regions == null) {
			regions = new HashSet<>();
			this.playerRegions.put(player, regions);
		}
		
		return regions;
	}
	
	private void updateRegions(Player player, Location locationFrom, Location locationTo) {
		ProtectedRegion globalFrom = Flags.getRegionManager(locationFrom.getWorld()).getRegion("__global__");
		ProtectedRegion globalTo = Flags.getRegionManager(locationTo.getWorld()).getRegion("__global__");

		Set<ProtectedRegion> currentPlayerRegions = this.getRegionsAtPlayer(player);
		Set<ProtectedRegion> futureRegions = new HashSet<>();

		boolean isGoingToAnotherWorld = (globalFrom != globalTo);
		
		// Player is going to another world.
		// Call Leave Event for every region before checking for regions!
		if (isGoingToAnotherWorld){
			for (ProtectedRegion currentRegion : currentPlayerRegions){
				RegionBorderEvent leaveEvent = new RegionBorderEvent(currentRegion, CrossType.LEAVE, player, locationFrom, locationTo);
				Bukkit.getPluginManager().callEvent(leaveEvent);
			}

			currentPlayerRegions.clear();
		}
		
		// Detecting Enter event in non-global regions
		for (ProtectedRegion futureRegion : Flags.getRegionSet(locationTo)) {
			futureRegions.add(futureRegion);

			if (!currentPlayerRegions.contains(futureRegion)) {
				currentPlayerRegions.add(futureRegion);

				RegionBorderEvent enterEvent = new RegionBorderEvent(futureRegion, CrossType.ENTER, player, locationFrom, locationTo);
				Bukkit.getPluginManager().callEvent(enterEvent);
			}
		}

		// Detecting __global__ region Enter event
		futureRegions.add(globalTo);
		if (!currentPlayerRegions.contains(globalTo)) {
			currentPlayerRegions.add(globalTo);
			
			RegionBorderEvent enterEvent = new RegionBorderEvent(globalTo, CrossType.ENTER, player, locationFrom, locationTo);
			Bukkit.getPluginManager().callEvent(enterEvent);
		}
		
		// Detecting Leave event in non-global regions
		Iterator<ProtectedRegion> currentRegionsIterator = currentPlayerRegions.iterator();
		while (currentRegionsIterator.hasNext()) {
			ProtectedRegion currentRegion = currentRegionsIterator.next();
			
			if (!futureRegions.contains(currentRegion)) {
				currentRegionsIterator.remove();

				RegionBorderEvent leaveEvent = new RegionBorderEvent(currentRegion, CrossType.LEAVE, player, locationFrom, locationTo);
				Bukkit.getPluginManager().callEvent(leaveEvent);
			}
		}

		// Detecting __global__ region Leave event
		if (!futureRegions.contains(globalFrom)) {
			currentPlayerRegions.remove(globalFrom);
			
			RegionBorderEvent leaveEvent = new RegionBorderEvent(globalFrom, CrossType.LEAVE, player, locationFrom, locationTo);
			Bukkit.getPluginManager().callEvent(leaveEvent);
		}
	}
	
	@EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
	public void onMove(PlayerMoveEvent event) {
		this.updateRegions(event.getPlayer(), event.getFrom(), event.getTo());
	}
	
	@EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
	public void onTeleport(PlayerTeleportEvent event) {
		this.updateRegions(event.getPlayer(), event.getFrom(), event.getTo());
	}

	@EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
	public void onPortal(PlayerPortalEvent event) {
		this.updateRegions(event.getPlayer(), event.getFrom(), event.getTo());
	}

	@EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
	public void onJoin(PlayerJoinEvent event){
		this.updateRegions(event.getPlayer(), event.getPlayer().getLocation(), event.getPlayer().getLocation());
	}

	@EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
	public void onRespawn(PlayerRespawnEvent event){
		this.updateRegions(event.getPlayer(), event.getPlayer().getLocation(), event.getRespawnLocation());
	}
	
	@EventHandler
	public void onQuit(PlayerQuitEvent event) {
		// This needs to happen. Otherwise, it will cause a memory leak.
		this.playerRegions.remove(event.getPlayer());
	}
	
	@Override
	public void onEnable() {
		super.onEnable();
		
		this.playerRegions.clear();
	}
	
	@Override
	public void onDisable() {
		super.onDisable();
		
		this.playerRegions.clear();
	}

}
