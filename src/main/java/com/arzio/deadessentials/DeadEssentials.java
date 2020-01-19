package com.arzio.deadessentials;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import com.arzio.deadessentials.command.DeadEssentialsCommand;
import com.arzio.deadessentials.command.NoDelayCommand;
import com.arzio.deadessentials.command.PingCommand;
import com.arzio.deadessentials.module.ModuleContainer;
import com.arzio.deadessentials.module.addon.ModuleAddonBiomeChanger;
import com.arzio.deadessentials.module.addon.ModuleAddonCustomFlags;
import com.arzio.deadessentials.module.addon.ModuleAddonCustomGunsAndAmmos;
import com.arzio.deadessentials.module.addon.ModuleAddonRestrictGMInventoryWithPermission;
import com.arzio.deadessentials.module.addon.ModuleAddonSimpleClansFlags;
import com.arzio.deadessentials.module.addon.ModuleAddonStepEmeraldHeal;
import com.arzio.deadessentials.module.addon.ModuleAddonZombieSpawnBlockBlacklist;
import com.arzio.deadessentials.module.core.ModuleCoreWorldGuardRegionEvents;
import com.arzio.deadessentials.module.fix.ModuleFixArmorBreaking;
import com.arzio.deadessentials.module.fix.ModuleFixDamageSourceDetection;
import com.arzio.deadessentials.module.fix.ModuleFixDeathDropsCompatibility;
import com.arzio.deadessentials.module.fix.ModuleFixInvisibleEntities;
import com.arzio.deadessentials.module.fix.ModuleFixItemUsageOnRegions;
import com.arzio.deadessentials.module.fix.ModuleFixPlayersOnlineWhileServerIsStopping;
import com.arzio.deadessentials.module.fix.ModuleFixPlotMeEntityInteraction;
import com.arzio.deadessentials.module.fix.ModuleFixPvPOnWorldsWithoutPvP;
import com.arzio.deadessentials.module.fix.ModuleTCPNoDelay;
import com.arzio.deadessentials.service.ForgeBukkitEventManager;
import com.arzio.deadessentials.service.ModuleManager;
import com.arzio.deadessentials.service.ModuleManager.ToggleAction;
import com.arzio.deadessentials.service.impl.ArzioModuleManager;
import com.arzio.deadessentials.service.impl.ForgeBukkitEventManagerImpl;
import com.arzio.deadessentials.util.CauldronUtils;
import com.arzio.deadessentials.util.reflection.ReflectionHelper;
import com.arzio.deadessentials.wrapper.item.ItemProvider;
import com.arzio.deadessentials.wrapper.item.impl.ItemProviderImpl;
import com.mewin.WGCustomFlags.WGCustomFlagsPlugin;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;

import cpw.mods.fml.common.ModContainer;

public class DeadEssentials extends JavaPlugin {

	public static final ModContainer MOD_CONTAINER = ReflectionHelper.getCraftingDeadModContainer();
	public static final String MOD_ID = "craftingdead";
	public static final String MOD_RESOURCE_NAME = MOD_ID+":";

	private static DeadEssentials instance;
	private ModuleManager moduleManager;
	
	// Forge-Bukkit compatible listener
	private ForgeBukkitEventManager forgeBukkitEventManager;

	// CD Wrappers
	private ItemProvider itemProvider;

	@Override
	public void onLoad() {
	}

	@Override
	public void onEnable() {
		instance = this;

		this.itemProvider = new ItemProviderImpl();
		this.forgeBukkitEventManager = new ForgeBukkitEventManagerImpl(this);

		// Initialize the module manager
		this.moduleManager = new ArzioModuleManager(this);

		// Addons
		this.moduleManager.registerModule(ModuleAddonBiomeChanger.class);
		this.moduleManager.registerModule(ModuleAddonCustomFlags.class);
		this.moduleManager.registerModule(ModuleAddonCustomGunsAndAmmos.class);
		this.moduleManager.registerModule(ModuleFixPlayersOnlineWhileServerIsStopping.class);
		this.moduleManager.registerModule(ModuleAddonRestrictGMInventoryWithPermission.class);
		this.moduleManager.registerModule(ModuleAddonStepEmeraldHeal.class);
		this.moduleManager.registerModule(ModuleAddonZombieSpawnBlockBlacklist.class);
		if (CauldronUtils.isPluginLoaded("SimpleClans")) {
			this.moduleManager.registerModule(ModuleAddonSimpleClansFlags.class);
		}

		// Core - Do not disable them
		this.moduleManager.registerModule(ModuleCoreWorldGuardRegionEvents.class);

		// Fixes
		this.moduleManager.registerModule(ModuleFixArmorBreaking.class);
		this.moduleManager.registerModule(ModuleFixDamageSourceDetection.class);
		this.moduleManager.registerModule(ModuleFixDeathDropsCompatibility.class);
		this.moduleManager.registerModule(ModuleFixInvisibleEntities.class);
		this.moduleManager.registerModule(ModuleFixItemUsageOnRegions.class);
		if (CauldronUtils.isPluginLoaded("PlotMe") && CauldronUtils.isPluginLoaded("AuthMe")) {
			this.moduleManager.registerModule(ModuleFixPlotMeEntityInteraction.class);
		}
		this.moduleManager.registerModule(ModuleFixPvPOnWorldsWithoutPvP.class);
		this.moduleManager.registerModule(ModuleTCPNoDelay.class);

		// Toggle them all!
		this.moduleManager.toggleAll(ToggleAction.TOGGLE_FROM_CONFIG);

		this.getCommand("deadessentials").setExecutor(new DeadEssentialsCommand(this));
		this.getCommand("ping").setExecutor(new PingCommand());
		this.getCommand("nodelay").setExecutor(new NoDelayCommand());

		this.getLogger().info("Loading done! :3");
		this.getLogger().info("This plugin was made by Arzio <3");
		this.getLogger().info("Please, use '/deadessentials' to check all available commands");
		
		// Warn about errored modules to some admins
		Bukkit.getScheduler().runTaskTimer(this, new Runnable() {

			@Override
			public void run() {
				List<ModuleContainer> erroredModules = getModuleManager().getErroredModules();
				
				if (!erroredModules.isEmpty()) {
					if (getModuleManager().shouldWarnForErroredModules()) {
						for (Player player : CauldronUtils.getPlayersWithPermission("deadessentials.warnings")) {
							player.sendMessage(" ");
							player.sendMessage("§e["+getName()+"] §cThe following modules could not get loaded or unloaded:");
							for (ModuleContainer module : erroredModules) {
								player.sendMessage("§7 - "+module.getName());
							}
							player.sendMessage("§cCheck console for possible errors during server load phase.");
							player.sendMessage(" ");
						}
					}
				}
			}

		}, 20L * 20L, 20L * 20L); // Warns admins every 20 seconds in case of load errors in modules
	}

	@Override
	public void reloadConfig() {
		super.reloadConfig();
		this.moduleManager.toggleAll(ToggleAction.TOGGLE_FROM_CONFIG);
	}

	@Override
	public void onDisable() {
		this.moduleManager.toggleAll(ToggleAction.TOGGLE_OFF);
		instance = null;
		this.getLogger().info("Plugin unloaded: " + this.getName() + " by Arzio");
	}

	public WGCustomFlagsPlugin getWGCustomFlags() {
		Plugin plugin = getServer().getPluginManager().getPlugin("WGCustomFlags");

		if (!(plugin instanceof WGCustomFlagsPlugin)) {
			return null;
		}

		return (WGCustomFlagsPlugin) plugin;
	}

	public WorldGuardPlugin getWorldGuard() {
		Plugin plugin = getServer().getPluginManager().getPlugin("WorldGuard");

		if (!(plugin instanceof WorldGuardPlugin)) {
			return null;
		}

		return (WorldGuardPlugin) plugin;
	}

	public ForgeBukkitEventManager getForgeBukkitEventManager() {
		return forgeBukkitEventManager;
	}

	public ItemProvider getItemProvider() {
		return this.itemProvider;
	}

	public ModuleManager getModuleManager() {
		return this.moduleManager;
	}

	public static DeadEssentials getInstance() {
		if (instance == null)
			throw new IllegalStateException(
					"The plugin is not enabled yet! Maybe you need to make your plugin wait until the plugin loads. Add this plugin to your plugin's dependencies in your plugin.yml");
		return instance;
	}
}
