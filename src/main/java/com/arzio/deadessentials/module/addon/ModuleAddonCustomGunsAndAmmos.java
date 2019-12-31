package com.arzio.deadessentials.module.addon;

import org.bukkit.Material;

import com.arzio.deadessentials.DeadEssentials;
import com.arzio.deadessentials.module.Module;
import com.arzio.deadessentials.module.ModuleType;
import com.arzio.deadessentials.module.RegisterModule;
import com.arzio.deadessentials.util.CauldronUtils;
import com.arzio.deadessentials.util.YMLFile;
import com.arzio.deadessentials.wrapper.item.Ammo;
import com.arzio.deadessentials.wrapper.item.CDItem;
import com.arzio.deadessentials.wrapper.item.Gun;

import net.minecraft.server.v1_6_R3.Item;

@RegisterModule(name = "custom-guns-and-ammos", defaultState = false, type = ModuleType.ADDON)
public class ModuleAddonCustomGunsAndAmmos extends Module {

	private YMLFile yml;

	@Override
	public void onEnable() {
		yml = this.getConfig();
		yml.reload();
		
		for (Item item : Item.byId) {
			if (item == null) {
				continue;
			}

			@SuppressWarnings("deprecation")
			Material material = Material.getMaterial(item.id);
			CDItem cdItem = DeadEssentials.getInstance().getItemProvider().getCDItemFrom(material);
			
			if (cdItem == null) {
				continue;
			}
			
			if (cdItem instanceof Gun) {
				Gun gun = (Gun) cdItem;
				
				gun.setAccuracy(yml.getValueWithDefault("guns."+gun.getName()+".accuracy", gun.getAccuracy()));
				gun.setBodyDamage(yml.getValueWithDefault("guns."+gun.getName()+".body_damage", gun.getBodyDamage()));
				gun.setBulletsPerRound(yml.getValueWithDefault("guns."+gun.getName()+".bullets_per_round", gun.getBulletsPerRound()));
				gun.setCompatibleMagazines(CauldronUtils.intListToMaterialArray(
						yml.getValueWithDefault("guns."+gun.getName()+".compatible_ammos", 
								CauldronUtils.materialArrayToIntegerList(gun.getCompatibleAmmos()))));
				gun.setHeadshotDamage(yml.getValueWithDefault("guns."+gun.getName()+".headshot_damage", gun.getHeadshotDamage()));
				gun.setMaximumBulletDistance(yml.getValueWithDefault("guns."+gun.getName()+".maximum_bullet_distance", gun.getMaximumBulletDistance()));
				gun.setMovementPenalty(yml.getValueWithDefault("guns."+gun.getName()+".movement_penalty", gun.getMovementPenalty()));
				gun.setRecoil(yml.getValueWithDefault("guns."+gun.getName()+".recoil", gun.getRecoil()));
				gun.setRPM(yml.getValueWithDefault("guns."+gun.getName()+".rpm", gun.getRPM()));
				gun.setZoomLevel(yml.getValueWithDefault("guns."+gun.getName()+".zoom_level", gun.getZoomLevel()));
				gun.setSpreadWhileAiming(yml.getValueWithDefault("guns."+gun.getName()+".spread_while_aiming", gun.canSpreadWhileAiming()));
				gun.setCrosshair(yml.getValueWithDefault("guns."+gun.getName()+".crosshair", gun.hasCrosshair()));
				
			} else if (cdItem instanceof Ammo) {
				Ammo ammo = (Ammo) cdItem;
				
				ammo.setBulletAmount(yml.getValueWithDefault("ammo."+ammo.getName()+".bullet_amount", ammo.getBulletAmount()));
				ammo.setPenetration(yml.getValueWithDefault("ammo."+ammo.getName()+".penetration", ammo.getPenetration()));
			}
		}
		
		yml.save();
	}

}
