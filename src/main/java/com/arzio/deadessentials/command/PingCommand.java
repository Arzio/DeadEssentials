package com.arzio.deadessentials.command;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_6_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

public class PingCommand implements CommandExecutor {

	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if ((sender instanceof Player)) {
			if (args.length >= 1) {
				Player playerAlvo = Bukkit.getPlayer(args[0]);
				if (playerAlvo != null) {
					sender.sendMessage("§aPing of §l"+playerAlvo.getName()+"§a: §e"+getPingString(playerAlvo));
				} else {
					sender.sendMessage("§cPlayer '"+args[0]+"' not found.");
				}
			} else {
				sender.sendMessage("§aYour ping: §e"+getPingString((Player) sender));
			}
			return true;
		}
		sender.sendMessage("§cThis command can only be used by players.");

		return true;
	}

	private static String getPingString(Player player) {
		int playerPing = ((CraftPlayer) player).getHandle().ping;
		String ping;
		if ((playerPing < 0) || (playerPing > 3000)) {
			ping = "Calculating...";
		} else {
			ping = Integer.toString(playerPing)+"ms";
		}
		return ping;
	}
}
