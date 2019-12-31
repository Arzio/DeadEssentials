package com.arzio.deadessentials.module.fix;

import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitTask;

import com.arzio.deadessentials.DeadEssentials;
import com.arzio.deadessentials.module.Module;
import com.arzio.deadessentials.module.ModuleType;
import com.arzio.deadessentials.module.RegisterModule;
import com.arzio.deadessentials.util.YMLFile;

@RegisterModule(name = "gun-damage-on-server-freeze", type = ModuleType.FIX)
public class ModuleFixGunDamageOnServerFreeze extends Module{

	private BukkitTask task;
	private long lastTick = 0L;
	private long suspendedMillis = 0L;
	private YMLFile yml;
	
	private int minimumFreezeTime;
	private int bulletSuspensionDuration;
	
	@Override
	public void onEnable() {
		super.onEnable();
		
		this.yml = this.getConfig();
		yml.reload();
		
		this.minimumFreezeTime = yml.getValueWithDefault("minimum-freeze-time-in-millis-until-suspend", 700);
		this.bulletSuspensionDuration = yml.getValueWithDefault("bullet-suspension-duration-in-millis", 400);
		
		yml.save();
		
		task = Bukkit.getScheduler().runTaskTimer(DeadEssentials.getInstance(), new Runnable() {
			
			@Override
			public void run() {
				if (suspendedMillis > 0L) {
					suspendedMillis -= getDifferenceBetweenLastTick();
				}
				
				recalculateSuspendedMillis();
				
				lastTick = System.currentTimeMillis();
			}
			
		}, 1L, 1L);
	}
	
	@Override
	public void onDisable() {
		super.onDisable();
		if (task != null) {
			task.cancel();
		}
	}
	
	private long getDifferenceBetweenLastTick() {
		long timeNow = System.currentTimeMillis();
		long timeDifferenceBetweenLastTick = timeNow - lastTick;
		return timeDifferenceBetweenLastTick;
	}
	
	private void recalculateSuspendedMillis() {
		if (getDifferenceBetweenLastTick() > this.minimumFreezeTime) {
			suspendedMillis = this.bulletSuspensionDuration; // Suspends every bullet and gun trigger for some millis
		}
	}
	
	public boolean canFire() {
		this.recalculateSuspendedMillis(); // Bullet packets happens before Bukkit's tick task, so we need to recalculate
		return 0 >= this.suspendedMillis;
	}

}
