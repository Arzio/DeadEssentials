package com.arzio.deadessentials.command;

import java.net.Socket;
import java.net.SocketException;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.arzio.deadessentials.util.CauldronUtils;

public class NoDelayCommand implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (!(sender instanceof Player)) {
			sender.sendMessage("§cThis command can only be used by players.");
			return true;
		}
		if (!(sender.hasPermission("deadessentials.nodelay"))) {
			sender.sendMessage("§cYou do not have permission.");
			return true;
		}
		
		Socket socket = CauldronUtils.getPlayerSocket((Player) sender);
		
		try {
			socket.setTcpNoDelay(!socket.getTcpNoDelay());
			sender.sendMessage("§aChanged TCP NODELAY option to "+socket.getTcpNoDelay()+" for you.");
		} catch (SocketException e) {
			e.printStackTrace();
			sender.sendMessage("§cFailed to change TCP NODELAY option for you.");
		}

		return true;
	}
}
