package com.arzio.deadessentials.module.addon;

import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.hanging.HangingBreakByEntityEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffectType;

import com.arzio.deadessentials.event.CDPlayerPreDropEvent;
import com.arzio.deadessentials.event.region.RegionBorderEvent;
import com.arzio.deadessentials.event.region.RegionBorderEvent.CrossType;
import com.arzio.deadessentials.module.Module;
import com.arzio.deadessentials.module.ModuleType;
import com.arzio.deadessentials.module.RegisterModule;
import com.arzio.deadessentials.util.CDMaterial;
import com.arzio.deadessentials.util.CDPotionEffectType;
import com.arzio.deadessentials.util.region.EasySetFlag;
import com.arzio.deadessentials.util.region.EasyStateFlag;

@RegisterModule(name = "custom-flags", type = ModuleType.ADDON)
public class ModuleAddonCustomFlags extends Module {

	public static final EasyStateFlag KEEP_INVENTORY_FLAG = new EasyStateFlag("keep-inventory");
	public static final EasyStateFlag REPAIR_LEG_FLAG = new EasyStateFlag("repair-leg");
	public static final EasyStateFlag HEAL_BLEEDING_FLAG = new EasyStateFlag("heal-bleeding");
	public static final EasyStateFlag HEAL_INFECTION_FLAG = new EasyStateFlag("heal-infection");
	public static final EasyStateFlag OLD_CD_BUILD_FLAG = new EasyStateFlag("cd-build");
	public static final EasyStateFlag HANDCUFFS_FLAG = new EasyStateFlag("handcuffs");
	public static final EasySetFlag<String> PLAYER_ENTER_COMMANDS_FLAG = new EasySetFlag<>("player-enter-commands");
	public static final EasySetFlag<String> PLAYER_LEAVE_COMMANDS_FLAG = new EasySetFlag<>("player-leave-commands");
	public static final EasySetFlag<String> SERVER_ENTER_COMMANDS_FLAG = new EasySetFlag<>("server-enter-commands");
	public static final EasySetFlag<String> SERVER_LEAVE_COMMANDS_FLAG = new EasySetFlag<>("server-leave-commands");
	
	public ModuleAddonCustomFlags() {

	}
	
	@Override
	public void onEnable() {
		KEEP_INVENTORY_FLAG.register();
		REPAIR_LEG_FLAG.register();
		HEAL_BLEEDING_FLAG.register();
		HEAL_INFECTION_FLAG.register();
		OLD_CD_BUILD_FLAG.register();
		HANDCUFFS_FLAG.register();
		PLAYER_ENTER_COMMANDS_FLAG.register();
		PLAYER_LEAVE_COMMANDS_FLAG.register();
		SERVER_ENTER_COMMANDS_FLAG.register();
		SERVER_LEAVE_COMMANDS_FLAG.register();
	}
	
	@EventHandler(ignoreCancelled = true, priority = EventPriority.LOW)
	public void disableHandcuffs(PlayerInteractEntityEvent event) {
		// Continue only if the clicked entity is a player
		if (!(event.getRightClicked() instanceof Player)) {
			return;
		}
		
		ItemStack stack = event.getPlayer().getItemInHand();
		
		// Check if the clicker is holding any item
		if (stack != null && CDMaterial.HANDCUFFS.isTypeOf(stack)) {
			if (HANDCUFFS_FLAG.isDenied(event.getPlayer().getLocation()) || HANDCUFFS_FLAG.isDenied(event.getRightClicked().getLocation())) {
				event.setCancelled(true);
				event.getPlayer().sendMessage("§cYou cannot use handcuffs here!");
			}
		}
	}
	
	@EventHandler
	public void cdBuildHanging(HangingBreakByEntityEvent event) {
		if (event.getRemover() instanceof Player) {
			Player player = (Player) event.getRemover();
			
			if (player.isOp()) {
				return;
			}
			
			if (OLD_CD_BUILD_FLAG.isAllowed(player.getLocation())) {
				event.setCancelled(true);
			}
		}
	}
	
	@EventHandler
	public void onBlockBreak(BlockBreakEvent event) {
		Player player = event.getPlayer();
		if (player.isOp()) {
			return;
		}
		
		Block block = event.getBlock();	   
		
		if (OLD_CD_BUILD_FLAG.isAllowed(block.getLocation())) {

			if (player.getGameMode() != GameMode.CREATIVE) {
				ItemStack heldStack = player.getItemInHand();
				if (heldStack != null) {
					CDMaterial material = CDMaterial.getFrom(heldStack.getType());
					if (material != null && material.getHarvestType().canHarvest(block)) {
						return; // do not cancel the event
					}
				}
			}
			
			event.setCancelled(true);
		}
	}
	
	@EventHandler
	public void onBlockPlaced(BlockPlaceEvent event) {
		if (event.getPlayer().isOp()) {
			return;
		}
		if (OLD_CD_BUILD_FLAG.isAllowed(event.getBlock().getLocation())) {
			event.setCancelled(true);
		}
	}

	@EventHandler(ignoreCancelled = true, priority = EventPriority.HIGH)
	public void handleServerCommandFlag(RegionBorderEvent event){
		Set<String> commandsToExecute = (event.getType() == CrossType.ENTER) ?
			 SERVER_ENTER_COMMANDS_FLAG.getValue(event.getRegion()) : SERVER_LEAVE_COMMANDS_FLAG.getValue(event.getRegion());

		if (commandsToExecute != null){
			for (String command : commandsToExecute){
				Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command.replace("/", "").replace("%player%", event.getPlayer().getName()));
			}
		}
	}

	@EventHandler(ignoreCancelled = true, priority = EventPriority.HIGH)
	public void handlePlayerCommandFlag(RegionBorderEvent event){
		Set<String> commandsToExecute = (event.getType() == CrossType.ENTER) ? 
			PLAYER_ENTER_COMMANDS_FLAG.getValue(event.getRegion()) : PLAYER_LEAVE_COMMANDS_FLAG.getValue(event.getRegion());

		if (commandsToExecute != null){
			for (String command : commandsToExecute){
				Bukkit.dispatchCommand(event.getPlayer(), command.replace("/", ""));
			}
		}
	}
	
	@EventHandler(priority = EventPriority.LOWEST)
	public void flagKeepInventory(CDPlayerPreDropEvent event) {
		if (KEEP_INVENTORY_FLAG.isAllowed(event.getPlayer().getLocation())) {
			event.setKeepInventory(true);
		}
	}
	
	@EventHandler(ignoreCancelled = false, priority = EventPriority.HIGHEST)
	public void repairLegOnFall(EntityDamageEvent event) {
		if (event.getCause() == DamageCause.FALL && event.getEntity() instanceof Player) {
			Player player = (Player) event.getEntity();
			if (player.hasPotionEffect(CDPotionEffectType.BROKEN_LEG)) {
				if (REPAIR_LEG_FLAG.isAllowed(player.getLocation())) {
					player.removePotionEffect(CDPotionEffectType.BROKEN_LEG);
					player.removePotionEffect(PotionEffectType.BLINDNESS);
				}
			}
		}
	}
	
	@EventHandler(ignoreCancelled = true)
	public void onMovement(PlayerMoveEvent event) {
		Player player = event.getPlayer();
		if (player.hasPotionEffect(CDPotionEffectType.BROKEN_LEG)) {
			if (REPAIR_LEG_FLAG.isAllowed(event.getTo())) {
				player.removePotionEffect(CDPotionEffectType.BROKEN_LEG);
				player.removePotionEffect(PotionEffectType.BLINDNESS);
			}
		}
		if (player.hasPotionEffect(CDPotionEffectType.BLEEDING)) {
			if (HEAL_BLEEDING_FLAG.isAllowed(event.getTo())) {
				player.removePotionEffect(CDPotionEffectType.BLEEDING);
			}
		}
		if (player.hasPotionEffect(CDPotionEffectType.INFECTION)) {
			if (HEAL_INFECTION_FLAG.isAllowed(event.getTo())) {
				player.removePotionEffect(CDPotionEffectType.INFECTION);
			}
		}
	}

}
