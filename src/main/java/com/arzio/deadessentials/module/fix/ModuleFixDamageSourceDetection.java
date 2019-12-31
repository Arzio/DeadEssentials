package com.arzio.deadessentials.module.fix;

import java.util.logging.Level;

import org.bukkit.craftbukkit.v1_6_R3.event.CraftEventFactory;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

import com.arzio.deadessentials.DeadEssentials;
import com.arzio.deadessentials.module.Module;
import com.arzio.deadessentials.module.ModuleType;
import com.arzio.deadessentials.module.RegisterModule;
import com.arzio.deadessentials.util.CDDamageCause;
import com.arzio.deadessentials.util.reflection.ReflectionHelper;

import net.minecraft.server.v1_6_R3.DamageSource;
import net.minecraft.server.v1_6_R3.EntityDamageSource;
import net.minecraft.server.v1_6_R3.EntityLiving;
import net.minecraftforge.event.EventPriority;
import net.minecraftforge.event.ForgeSubscribe;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;

@RegisterModule(name = "damage-source-detection", type = ModuleType.FIX)
public class ModuleFixDamageSourceDetection extends Module{
	
	@ForgeSubscribe(priority = EventPriority.HIGHEST)
	public void onAttack(LivingAttackEvent event) {
		DamageSource source = ReflectionHelper.getValueFromEvent(event, DamageSource.class);

		if (isDamageSourceDetectedByBukkit(source)) {
			return;
		}
		
		EntityLiving victim = ReflectionHelper.getValueFromEvent(event, EntityLiving.class);
		
		DamageCause cause = DamageCause.CUSTOM; // The initial cause is CUSTOM to reduce
												// problems in newer versions with new damage types
												// which aren't covered by the custom damage cause enum below.
		CDDamageCause customCause = CDDamageCause.getFrom(source);
		
		if (customCause != null) {
			cause = customCause.asBukkitDamageCause();
		}

		EntityDamageEvent innerEvent = CraftEventFactory.callEntityDamageEvent(source.getEntity(),
				victim, cause, event.ammount);

		if (innerEvent.isCancelled() || innerEvent.getDamage() <= 0) {
			event.setCanceled(true);
		}
	}

	@ForgeSubscribe
	public void onHurt(LivingHurtEvent event) {
		DamageSource source = ReflectionHelper.getValueFromEvent(event, DamageSource.class);

		if (isDamageSourceDetectedByBukkit(source)) {
			return;
		}
		
		EntityLiving entityLiving = ReflectionHelper.getValueFromEvent(event, EntityLiving.class);

		EntityDamageEvent lastDamageCause = entityLiving.getBukkitEntity().getLastDamageCause();

		if (lastDamageCause == null) {
			DeadEssentials.getInstance().getLogger().log(Level.WARNING, "Last damage cause is not present. This is a bug! Please, report it.");
			DeadEssentials.getInstance().getLogger().log(Level.WARNING, "Damage source was "+source.getClass()+".");
			event.setCanceled(true);
			return;
		}
		
		if (lastDamageCause.isCancelled()) {
			event.setCanceled(true);
			return;
		}
		
		event.ammount = (float) lastDamageCause.getDamage();
	}

	public static boolean isDamageSourceDetectedByBukkit(DamageSource source) {
		if ((source instanceof EntityDamageSource)) {
			return true;
		}
		if (source == DamageSource.OUT_OF_WORLD) {
			return true;
		}
		EntityDamageEvent.DamageCause cause = null;
		if (source == DamageSource.FIRE) {
			cause = EntityDamageEvent.DamageCause.FIRE;
		} else if (source == DamageSource.STARVE) {
			cause = EntityDamageEvent.DamageCause.STARVATION;
		} else if (source == DamageSource.WITHER) {
			cause = EntityDamageEvent.DamageCause.WITHER;
		} else if (source == DamageSource.STUCK) {
			cause = EntityDamageEvent.DamageCause.SUFFOCATION;
		} else if (source == DamageSource.DROWN) {
			cause = EntityDamageEvent.DamageCause.DROWNING;
		} else if (source == DamageSource.BURN) {
			cause = EntityDamageEvent.DamageCause.FIRE_TICK;
		} else if (source == CraftEventFactory.MELTING) {
			cause = EntityDamageEvent.DamageCause.MELTING;
		} else if (source == CraftEventFactory.POISON) {
			cause = EntityDamageEvent.DamageCause.POISON;
		} else if (source == DamageSource.MAGIC) {
			cause = EntityDamageEvent.DamageCause.MAGIC;
		}
		if (cause != null) {
			return true;
		}
		return false;
	}
}
