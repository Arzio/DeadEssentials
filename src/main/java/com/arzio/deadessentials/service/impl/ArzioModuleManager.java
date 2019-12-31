package com.arzio.deadessentials.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;

import com.arzio.deadessentials.DeadEssentials;
import com.arzio.deadessentials.module.Module;
import com.arzio.deadessentials.module.ModuleContainer;
import com.arzio.deadessentials.module.RegisterModule;
import com.arzio.deadessentials.service.ModuleManager;
import com.arzio.deadessentials.util.YMLFile;

public class ArzioModuleManager implements ModuleManager{

	private Map<Class<? extends Module>, ModuleContainer> moduleMap = new ConcurrentHashMap<>();
	private final YMLFile yml;
	private final DeadEssentials plugin;
	private boolean shouldWarnErrors;
	
	public ArzioModuleManager(DeadEssentials plugin) {
		this.yml = new YMLFile(plugin, "modules.yml");
		this.plugin = plugin;
		this.shouldWarnErrors = yml.getValueWithDefault("should-warn-module-errors-for-ops", true);
	}
	
	@Override
	public ModuleContainer registerModule(Class<? extends Module> moduleClass) {
		try {
			ModuleContainer container = moduleMap.get(moduleClass);
			
			if (container == null) {
				if (!moduleClass.isAnnotationPresent(RegisterModule.class)) {
					throw new IllegalArgumentException("Annotation 'RegisterModule' is not present in "+moduleClass.getSimpleName());
				}
				
				RegisterModule registry = moduleClass.getAnnotation(RegisterModule.class);
				
				if (registry.name().isEmpty()) {
					throw new IllegalArgumentException("Module name from class "+moduleClass.getSimpleName()+" cannot be an empty String");
				}
				
				container = new ModuleContainer(registry);
				container.setModule(moduleClass);
				
				moduleMap.put(moduleClass, container);
			}
			
			return container;
		} catch (Throwable t) {
			this.plugin.getLogger().log(Level.SEVERE, "Error while loading module from class "+moduleClass.getClass().getSimpleName());
			t.printStackTrace();
		}
		return null;
	}
	
	@Override
	public void toggleModule(Class<? extends Module> moduleClass, ToggleAction action) throws Throwable {
		if (!moduleMap.containsKey(moduleClass)) {
			throw new IllegalArgumentException("The module '"+moduleClass.getSimpleName()+"' is not registered in this Module Manager!");
		}
		
		if (action == ToggleAction.TOGGLE_OFF) {
			moduleMap.get(moduleClass).setEnabled(false);
			return;
		}
		
		ModuleContainer container = moduleMap.get(moduleClass);
		
		boolean shouldEnable = true;
		if (action == ToggleAction.TOGGLE_FROM_CONFIG) {
			yml.reload();
			
			String moduleNameWithCategory = container.getRegistry().type().name()+"."+container.getName();
			
			RegisterModule register = container.getModule().getClass().getAnnotation(RegisterModule.class);
			
			if (container.getRegistry().type().canBeDisabled()) {
				shouldEnable = yml.getValueWithDefault("enable-modules."+moduleNameWithCategory, register.defaultState());
			}

			yml.save();
		}

		container.setEnabled(shouldEnable);
	}
	
	@Override
	public void toggleAll(ToggleAction type) {
		for (ModuleContainer module : this.getAllModules()) {
			try {
				if (module.getModule() != null) {
					this.toggleModule(module.getModule().getClass(), type);
				}
			} catch (Throwable t) {
				plugin.getLogger().log(Level.SEVERE, "There was an error while toggling module "+module.getModule().getClass().getSimpleName()+" with action "+type.name(), t);
			}
		}
	}

	@Override
	public boolean shouldWarnForErroredModules() {
		return this.shouldWarnErrors;
	}

	@Override
	public void setShouldWarnForErroredModules(boolean should) {
		this.shouldWarnErrors = should;
	}
	
	@Override
	public List<ModuleContainer> getAllModules() {
		return Collections.unmodifiableList(new ArrayList<>(moduleMap.values()));
	}
	
	@Override
	public List<ModuleContainer> getErroredModules() {
		List<ModuleContainer> erroredModules = new ArrayList<>();
		
		for (ModuleContainer module : this.getAllModules()) {
			if (module.isErrored()) {
				erroredModules.add(module);
			}
		}
		
		return Collections.unmodifiableList(erroredModules);
	}

	@Override
	public List<ModuleContainer> getEnabledModulesByPartialName(String partialName) {
		List<ModuleContainer> found = new ArrayList<>();
		
		for (ModuleContainer container : this.getEnabledModules()) {
			if (isLike(container.getModule().getClass().getSimpleName(), partialName) || isLike(container.getName(), partialName)) {
				found.add(container);
			}
		}
		return Collections.unmodifiableList(found);
	}
	
	private boolean isLike(String origin, String part) {
		origin = origin.toLowerCase().replaceAll("[^A-Za-z0-9]", "").trim();
		part = part.toLowerCase().replaceAll("[^A-Za-z0-9]", "").trim();
		
		if (origin.contains(part)) {
			return true;
		}
		
		return false;
	}
	
	@Override
	public List<ModuleContainer> getEnabledModules() {
		List<ModuleContainer> enabledModules = new ArrayList<>();
		
		for (ModuleContainer module : this.getAllModules()) {
			if (module.isEnabled()) {
				enabledModules.add(module);
			}
		}
		
		return Collections.unmodifiableList(enabledModules);
	}

	@Override
	public ModuleContainer getModuleExact(String moduleName) {
		ModuleContainer found = null;
		
		for (ModuleContainer container : plugin.getModuleManager().getAllModules()) {
			if (container.getClass().getSimpleName().equalsIgnoreCase(moduleName) || container.getName().equalsIgnoreCase(moduleName) || container.getName().replace("-", "").equalsIgnoreCase(moduleName)) {
				found = container;
			}
		}
		return found;
	}
	
}
