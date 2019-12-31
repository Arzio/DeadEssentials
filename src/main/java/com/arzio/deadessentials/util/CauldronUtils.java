package com.arzio.deadessentials.util;

import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_6_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

import com.arzio.deadessentials.util.reflection.IndirectField;
import com.arzio.deadessentials.util.reflection.finder.ContentFinder;

import guava10.com.google.common.primitives.Ints;
import net.minecraft.server.v1_6_R3.Entity;
import net.minecraft.server.v1_6_R3.EntityTypes;
import net.minecraft.server.v1_6_R3.INetworkManager;

public class CauldronUtils {

	private static IndirectField<Socket> socketField = null;

	public static List<Player> getPlayersWithPermission(String permission){
		List<Player> players = new ArrayList<>();
		
		for (Player player : Bukkit.getOnlinePlayers()){
			if (player.hasPermission(permission)){
				players.add(player);
			}
		}

		return players;
	}
	
	public static boolean isPluginLoaded(String pluginName) {
		return Bukkit.getPluginManager().getPlugin(pluginName) != null;
	}
	
	public static int getEntityTypeIDfromClass(Entity entity) {
		int entityClassToIdValue = EntityTypes.a(entity);
		
		if (entityClassToIdValue != 0) { // The non-present value for the mapping is 0
			return entityClassToIdValue;
		}

		Class<? extends Entity> entityClass = entity.getClass();

		// Math from Cauldron/Forge IDs
		return -Math.abs(entityClass.getName().hashCode() ^ entityClass.getName().hashCode() >>> 16);
	}
	
	@SuppressWarnings("deprecation")
	public static Material[] intArrayToMaterialArray(int[] ids) {
		Material[] materialArray = new Material[ids.length];
		
		for (int i = 0; i < ids.length; i++) {
			materialArray[i] = Material.getMaterial(ids[i]);
		}
		return materialArray;
	}
	
	public static Material[] intListToMaterialArray(List<Integer> ids){
		return intArrayToMaterialArray(Ints.toArray(ids));
	}
	
	@SuppressWarnings("deprecation")
	public static int[] materialArrayToIntArray(Material[] materials) {
		int[] intArray = new int[materials.length];
		
		for (int i = 0; i < materials.length; i++) {
			intArray[i] = materials[i].getId();
		}
		return intArray;
	}
	
	public static List<Integer> materialArrayToIntegerList(Material[] materials){
		return Ints.asList(materialArrayToIntArray(materials));
	}
	
	public static Socket getPlayerSocket(Player player) {
		INetworkManager networkManager = ((CraftPlayer) player).getHandle().playerConnection.networkManager;

		if (socketField == null) {
			socketField = new IndirectField<>(networkManager.getClass(),
					new ContentFinder.FieldBuilder<>().withType(Socket.class).build());
		}
		
		return socketField.getValue(networkManager);
	}
}
