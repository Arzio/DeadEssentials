package com.arzio.deadessentials.module.fix;

import java.net.Socket;
import java.net.SocketException;

import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;

import com.arzio.deadessentials.module.Module;
import com.arzio.deadessentials.module.ModuleType;
import com.arzio.deadessentials.module.RegisterModule;
import com.arzio.deadessentials.util.CauldronUtils;

@RegisterModule(name = "use-tcp-nodelay", type = ModuleType.FIX)
public class ModuleTCPNoDelay extends Module {
	
	@EventHandler(ignoreCancelled = true)
	public void disableTCPNagleAlgorithm(PlayerJoinEvent event){
		Socket socket = CauldronUtils.getPlayerSocket(event.getPlayer());
		
		try {
			socket.setTcpNoDelay(true);
		} catch (SocketException e) {
			e.printStackTrace();
		}
	}

}
