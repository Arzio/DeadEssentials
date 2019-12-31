package com.arzio.deadessentials.module;

import org.bukkit.Bukkit;
import org.bukkit.event.HandlerList;

import com.arzio.deadessentials.DeadEssentials;

public class ModuleContainer {

	private boolean isEnabled = false;
	private DeadEssentials plugin;
	private boolean errored = false;
	private Module module;
	private final RegisterModule moduleRegistry;

	public ModuleContainer(RegisterModule moduleRegistry) {
		this.moduleRegistry = moduleRegistry;
	}

	public void onEnable() throws Throwable{
		// Wraps with the error in a try-catch and rethrow it.
		try {
			if (this.getModule() == null) {
				throw new IllegalStateException("This module container does not contains a module yet!");
			}
			
			this.getPlugin().getForgeBukkitEventManager().registerEvents(this.getPlugin(), this.getModule());
			Bukkit.getPluginManager().registerEvents(this.getModule(), this.getPlugin());
			
			this.getModule().onEnable();
			this.errored = false;
		} catch (Throwable t) {
			this.errored = true;
			throw t;
		}
	}

	public void onDisable() throws Throwable {
		try {
			this.getPlugin().getForgeBukkitEventManager().unregisterEvents(this.getModule());
			HandlerList.unregisterAll(this.getModule());

			module.onDisable();
			this.errored = false;
		} catch (Throwable t) {
			this.errored = true;
			throw t;
		}
	}

	protected DeadEssentials getPlugin() {
		if (this.plugin == null) {
			this.plugin = DeadEssentials.getInstance();
		}
		return this.plugin;
	}

	public boolean isEnabled() {
		return this.isEnabled;
	}

	public void reload() throws Throwable {
		this.setEnabled(false);
		this.setEnabled(true);
	}

	public void setModule(Module module) {
		this.module = module;
	}
	
	public void setModule(Class<? extends Module> moduleClass) throws Throwable {
		try {
			this.setModule(Module.fromClass(moduleClass));
		} catch (Throwable t) {
			this.errored = true;
			throw t;
		}
	}

	public Module getModule() {
		return module;
	}

	public boolean isErrored() {
		return errored;
	}

	public void setErrored(boolean errored) {
		this.errored = errored;
	}

	public void setEnabled(boolean state) throws Throwable {
		if (isEnabled != state) {
			isEnabled = state;

			if (isEnabled) {
				this.onEnable();
				this.plugin.getLogger().info("Module ("+this.getRegistry().type().name()+") " + this.getName() + " enabled!");
			} else {
				this.onDisable();
				this.plugin.getLogger().info("Module ("+this.getRegistry().type().name()+") " + this.getName() + " disabled!");
			}
		}
	}
	
	public RegisterModule getRegistry() {
		return this.moduleRegistry;
	}

	public String getName() {
		return this.moduleRegistry.name();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((this.getName() == null) ? 0 : this.getName().hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ModuleContainer other = (ModuleContainer) obj;
		if (this.getName() == null) {
			if (other.getName() != null)
				return false;
		} else if (!this.getName().equals(other.getName()))
			return false;
		return true;
	}
}
