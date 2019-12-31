package com.arzio.deadessentials.command;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;

import com.arzio.deadessentials.DeadEssentials;
import com.arzio.deadessentials.module.ModuleContainer;
import com.arzio.deadessentials.util.CDEntityType;
import com.arzio.deadessentials.util.ThrowableHelper;

public class DeadEssentialsCommand implements CommandExecutor{

	private final DeadEssentials plugin;
	
	public DeadEssentialsCommand(DeadEssentials plugin) {
		this.plugin = plugin;
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command arg1, String arg2, String[] args) {

		if (args.length > 0) {
			if (args[0].equalsIgnoreCase("reload")) {
				if (!(sender.hasPermission("deadessentials.reload"))) {
					sender.sendMessage("§cYou don't have permission to use this command.");
					return true;
				}
				
				if (args.length > 1) {
					
					List<ModuleContainer> modulesFound = plugin.getModuleManager().getEnabledModulesByPartialName(args[1]);
					
					if (modulesFound.isEmpty()) {
						sender.sendMessage("§cModule not found.");
						return true;
					}
					
					if (modulesFound.size() > 1) {
						sender.sendMessage("§4Found more than 1 module with this partial name:");
						
						for (ModuleContainer container : modulesFound) {
							sender.sendMessage("§c - "+container.getName());
						}
						
						return true;
					}
					
					try {
						modulesFound.get(0).reload();
						sender.sendMessage("§aModule reloaded!");
					} catch (Throwable t) {
						sender.sendMessage("§4Failed to reload the module. Check console for error logs.");
						sender.sendMessage("§cStacktrace:");
						ThrowableHelper.printStackTrace(t, sender);
						t.printStackTrace();
					}
					
				} else {
					sender.sendMessage("§aReloading configuration...");
					plugin.reloadConfig();
					sender.sendMessage("§aConfiguration reloaded!");
				}
				return true;
			}

			if (args[0].equalsIgnoreCase("clearground")) {
				if (!(sender.hasPermission("deadessentials.clearground"))) {
					sender.sendMessage("§cYou don't have permission to use this command.");
					return true;
				}

				int amount = 0;
				for (World world : Bukkit.getWorlds()) {
					for (Entity entity : world.getEntities()) {
						CDEntityType type = CDEntityType.getTypeOf(entity);
						if (type == CDEntityType.GROUND_ITEM || type == CDEntityType.CORPSE) {
							entity.remove();
							amount++;
						}
					}
				}
				
				sender.sendMessage("§aAffected a total amount of "+amount+" entities");
				
				return true;
			}
			if (args[0].equalsIgnoreCase("modules")) {
				if (!(sender.hasPermission("deadessentials.modules"))) {
					sender.sendMessage("§cYou don't have permission to use this command.");
					return true;
				}
				
				List<ModuleContainer> modules = plugin.getModuleManager().getAllModules();

				sender.sendMessage("§6Total of "+modules.size()+" modules:");
				for (ModuleContainer module : modules) {
					String status = "[ENABLED]";
					String color = "§a";
					
					if (!module.isEnabled()) {
						status = "[DISABLED]";
						color = "§7";
					}
					if (module.isErrored()) {
						status = "[ERRORED]";
						color = "§c";
					}
					
					sender.sendMessage("§7 - "+color+module.getName()+" §l"+status);
				}
				
				return true;
			}
			if (args[0].equalsIgnoreCase("clearall")) {
				if (!(sender.hasPermission("deadessentials.clearall"))) {
					sender.sendMessage("§cYou don't have permission to use this command.");
					return true;
				}

				int amount = 0;
				for (World world : Bukkit.getWorlds()) {
					for (Entity entity : world.getEntities()) {
						CDEntityType type = CDEntityType.getTypeOf(entity);
						if (type != null) {
							entity.remove();
							amount++;
						}
					}
				}
				
				sender.sendMessage("§aAffected a total amount of "+amount+" entities");
				
				return true;
			}
		}
		
		sender.sendMessage("§aCommands for DeadEssentials:");
		sender.sendMessage("§f/deadessentials modules §8- §fShows every module");
		sender.sendMessage("§f/deadessentials reload §8- §fReloads the module.yml file");
		sender.sendMessage("§f/deadessentials reload <part of module name>§8- §fReloads the module");
		sender.sendMessage("§f/deadessentials clearground §8- §fRemoves corpses and ground items");
		sender.sendMessage("§f/deadessentials clearall §8- §fRemoves every CD entity");
		
		return true;
	}

}
