package com.arzio.deadessentials.module;

import java.lang.reflect.Constructor;

import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;

import com.arzio.deadessentials.DeadEssentials;
import com.arzio.deadessentials.util.ForgeListener;
import com.arzio.deadessentials.util.YMLFile;

public abstract class Module implements Listener, ForgeListener {
	
	private YMLFile configFile;

	public void onEnable() {
		
	}
	
	public void onDisable() {
		
	}
	
	public Plugin getPlugin() {
		return DeadEssentials.getInstance();
	}
	
	public static Module fromClass(Class<? extends Module> moduleClass) throws Throwable {
		Constructor<? extends Module> constructor = moduleClass.getConstructor();
		
		if (constructor == null) {
			throw new IllegalArgumentException("The class "+moduleClass.getSimpleName()+" does not have a default/empty constructor!");
		}
		
		return constructor.newInstance();
	}

	public String getName() {
		return this.getClass().getAnnotation(RegisterModule.class).name();
	}

	public YMLFile getConfig(){
		if (configFile == null){
			configFile = new YMLFile(this.getPlugin(), "module_configuration/"+this.getName()+"/config.yml");
		}
		return configFile;
	}
}
