package com.arzio.deadessentials.event.region;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import com.sk89q.worldguard.protection.regions.ProtectedRegion;

public abstract class RegionEvent extends Event{

	private static final HandlerList handlers = new HandlerList();
	private final ProtectedRegion region;

	public RegionEvent(ProtectedRegion region) {
		this.region = region;
	}

	public ProtectedRegion getRegion() {
		return this.region;
	}
	
	@Override
	public HandlerList getHandlers() {
		return handlers;
	}
	
	public static HandlerList getHandlerList() {
		return handlers;
	}
}
