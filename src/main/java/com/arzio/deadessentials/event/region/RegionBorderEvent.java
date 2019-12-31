package com.arzio.deadessentials.event.region;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import com.sk89q.worldguard.protection.regions.ProtectedRegion;

public class RegionBorderEvent extends RegionEvent {

	private final Player player;
	private final Location futureLocation;
	private final Location previousLocation;
	private final CrossType type;
	
	public RegionBorderEvent(ProtectedRegion region, CrossType type, Player player, Location previousLocation, Location futureLocation) {
		super(region);
		this.player = player;
		this.futureLocation = futureLocation;
		this.previousLocation = previousLocation;
		this.type = type;
	}
	
	public CrossType getType() {
		return type;
	}
	
	public Location getPreviousLocation() {
		return previousLocation;
	}

	public Location getFutureLocation() {
		return futureLocation;
	}

	public Player getPlayer() {
		return player;
	}
	
	public static enum CrossType {
		ENTER,
		LEAVE;
	}

}
