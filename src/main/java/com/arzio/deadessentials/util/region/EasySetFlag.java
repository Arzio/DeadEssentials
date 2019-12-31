package com.arzio.deadessentials.util.region;

import java.util.Set;

import com.sk89q.worldguard.protection.flags.CommandStringFlag;
import com.sk89q.worldguard.protection.flags.Flag;
import com.sk89q.worldguard.protection.flags.RegionGroup;
import com.sk89q.worldguard.protection.flags.SetFlag;

public class EasySetFlag<T> extends EasyFlag<Set<T>>{
	
	public EasySetFlag(Flag<Set<T>> flag) {
		super(flag);
	}
	
	public EasySetFlag(String flagName) {
		this(new SetFlag(flagName, RegionGroup.ALL, new CommandStringFlag(null)));
	}

}
