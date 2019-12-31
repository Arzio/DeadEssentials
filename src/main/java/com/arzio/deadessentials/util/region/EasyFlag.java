package com.arzio.deadessentials.util.region;

import org.bukkit.Location;

import com.sk89q.worldguard.protection.flags.Flag;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;

public class EasyFlag<T>{

	private Flag<T> flag;

	public EasyFlag(Flag<T> flag) {
		this.setFlag(flag);
	}
	
	public void register() {
		if (!this.isRegistered()) {
			Flags.addCustomFlag(this.flag);
		}
	}
	
	public boolean isRegistered() {
		return Flags.isFlagRegistered(flag);
	}
	
	public Flag<T> getFlag() {
		if (!this.isRegistered()) {
			throw new IllegalStateException("The flag "+flag.getName()+" is not registered yet! Please, call register() during the plugin load phase!");
		}
		return this.flag;
	}
	
	@SuppressWarnings("unchecked")
	public void setFlag(Flag<T> flag) {
		if (Flags.isFlagRegistered(flag)) {
			flag = (Flag<T>) Flags.getExistingFlag(flag.getName());
		}
		this.flag = flag;
	}

	public T getValue(Location location) {
		return Flags.getFlagValue(flag, location);
	}

	public T getValue(ProtectedRegion region){
		return Flags.getFlagValue(flag, region);
	}

	public boolean exists(Location location) {
		return Flags.flagExists(this.getFlag(), location);
	}

	public boolean exists(ProtectedRegion region) {
		return Flags.flagExists(this.getFlag(), region);
	}
	
}
