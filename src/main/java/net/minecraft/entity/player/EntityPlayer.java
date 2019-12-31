package net.minecraft.entity.player;

import net.minecraft.server.v1_6_R3.MinecraftServer;
import net.minecraft.server.v1_6_R3.PlayerInteractManager;
import net.minecraft.server.v1_6_R3.World;

/**
 * For some reason, the EntityPlayer class from Forge is not considered the same EntityPlayer from
 * Bukkit/Cauldron/Spigot EntityPlayer by the JVM. This is just a way to trick the JVM to
 * understand the Forge's EntityPlayer is a Bukkit/Cauldron/Spigot EntityPlayer too.
 */
public class EntityPlayer extends net.minecraft.server.v1_6_R3.EntityPlayer{

	public EntityPlayer(MinecraftServer arg0, World arg1, String arg2, PlayerInteractManager arg3) {
		super(arg0, arg1, arg2, arg3);
	}

}
