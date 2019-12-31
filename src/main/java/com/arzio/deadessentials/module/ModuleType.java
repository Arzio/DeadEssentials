package com.arzio.deadessentials.module;

public enum ModuleType {
	ADDON(true),
	CORE(false),
	FIX(true);
	
	private boolean canBeDisabled;
	
	private ModuleType(boolean canBeDisabled) {
		this.canBeDisabled = canBeDisabled;
	}
	
	public boolean canBeDisabled() {
		return this.canBeDisabled;
	}
}
