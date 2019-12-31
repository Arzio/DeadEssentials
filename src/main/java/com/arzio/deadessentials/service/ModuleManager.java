package com.arzio.deadessentials.service;

import java.util.List;

import com.arzio.deadessentials.module.Module;
import com.arzio.deadessentials.module.ModuleContainer;

public interface ModuleManager {

	public ModuleContainer registerModule(Class<? extends Module> moduleClass);

	public ModuleContainer getModuleExact(String moduleName);

	public List<ModuleContainer> getAllModules();
	
	public List<ModuleContainer> getErroredModules();
	
	public List<ModuleContainer> getEnabledModules();

	public List<ModuleContainer> getEnabledModulesByPartialName(String partialName);
	
	public void toggleModule(Class<? extends Module> moduleClass, ToggleAction type) throws Throwable;
	
	public void toggleAll(ToggleAction type);

	public boolean shouldWarnForErroredModules();

	public void setShouldWarnForErroredModules(boolean should);

	public static enum ToggleAction {
		TOGGLE_OFF, TOGGLE_FROM_CONFIG;
	}
}
