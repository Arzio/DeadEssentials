package com.arzio.deadessentials.wrapper.item.impl;

import org.bukkit.Material;

import com.arzio.deadessentials.DeadEssentials;
import com.arzio.deadessentials.util.CDMaterial;
import com.arzio.deadessentials.util.CauldronUtils;
import com.arzio.deadessentials.util.reflection.CDClasses;
import com.arzio.deadessentials.wrapper.item.Gun;

public class GunImpl extends CDItemImpl implements Gun{

	public GunImpl(Material material) {
		super(material);
	}

	@Override
	public int getBodyDamage() {
		return CDClasses.ITEMGUN_BODY_DAMAGE_FIELD.getValue(this.getItemInstance());
	}

	@Override
	public void setBodyDamage(int value) {
		CDClasses.ITEMGUN_BODY_DAMAGE_FIELD.setValue(this.getItemInstance(), value);
	}

	@Override
	public int getHeadshotDamage() {
		return CDClasses.ITEMGUN_HEADSHOT_DAMAGE_FIELD.getValue(this.getItemInstance());
	}

	@Override
	public void setHeadshotDamage(int value) {
		CDClasses.ITEMGUN_HEADSHOT_DAMAGE_FIELD.setValue(this.getItemInstance(), value);
	}

	@Override
	public Material[] getCompatibleAmmos() {
		return CauldronUtils.intArrayToMaterialArray(CDClasses.ITEMGUN_COMPATIBLE_MAGAZINES_FIELD.getValue(this.getItemInstance()));
	}

	@Override
	public void setCompatibleMagazines(Material... materials) {
		CDClasses.ITEMGUN_COMPATIBLE_MAGAZINES_FIELD.setValue(this.getItemInstance(), CauldronUtils.materialArrayToIntArray(materials));
	}

	@Override
	public double getRPM() {
		return CDClasses.ITEMGUN_ROUNDS_PER_MINUTE_FIELD.getValue(this.getItemInstance());
	}

	@Override
	public long getMillisPerRound() {
		double maxPerSecond = this.getRPM() / 60.0D;
		double delayPerSecond = 1.0D / maxPerSecond;
		return (long) (delayPerSecond * 1000.0D);
	}

	@Override
	public int getBulletsPerRound() {
		return CDClasses.ITEMGUN_BULLETS_PER_ROUND_FIELD.getValue(this.getItemInstance());
	}

	@Override
	public double getSoundLevel() {
		return CDClasses.ITEMGUN_SOUND_LEVEL_FIELD.getValue(this.getItemInstance());
	}

	@Override
	public boolean isFireBased() {
		return (this.getItem() != CDMaterial.CROSSBOW.asMaterial()) && (this.getItem() != CDMaterial.TASER.asMaterial());
	}

	@Override
	public String getShootSound() {
		return DeadEssentials.MOD_RESOURCE_NAME+CDClasses.ITEMGUN_SOUND_NAME_FIELD.getValue(this.getItemInstance());
	}

	@Override
	public String getSilencedSound() {
		return DeadEssentials.MOD_RESOURCE_NAME+CDClasses.ITEMGUN_SUPRESSED_SOUND_NAME_FIELD.getValue(this.getItemInstance());
	}

	@Override
	public void setShootSound(String soundName) {
		CDClasses.ITEMGUN_SOUND_NAME_FIELD.setValue(this.getItemInstance(), soundName);
	}

	@Override
	public void setSilencedSound(String soundName) {
		CDClasses.ITEMGUN_SUPRESSED_SOUND_NAME_FIELD.setValue(this.getItemInstance(), soundName);
	}

	@Override
	public String getReloadSound() {
		return DeadEssentials.MOD_RESOURCE_NAME+CDClasses.ITEMGUN_RELOAD_SOUND_NAME_FIELD.getValue(this.getItemInstance());
	}

	@Override
	public void setReloadSound(String soundName) {
		CDClasses.ITEMGUN_RELOAD_SOUND_NAME_FIELD.setValue(this.getItemInstance(), soundName);
	}

	@Override
	public void setRPM(double rpm) {
		CDClasses.ITEMGUN_ROUNDS_PER_MINUTE_FIELD.setValue(this.getItemInstance(), (float) rpm);
	}

	@Override
	public void setBulletsPerRound(int amount) {
		CDClasses.ITEMGUN_BULLETS_PER_ROUND_FIELD.setValue(this.getItemInstance(), amount);
	}

	@Override
	public int getMaximumBulletDistance() {
		return CDClasses.ITEMGUN_BULLET_DISTANCE_FIELD.getValue(this.getItemInstance());
	}

	@Override
	public void setMaximumBulletDistance(int distance) {
		CDClasses.ITEMGUN_BULLET_DISTANCE_FIELD.setValue(this.getItemInstance(), distance);
	}

	@Override
	public double getRecoil() {
		return CDClasses.ITEMGUN_RECOIL_FIELD.getValue(this.getItemInstance());
	}

	@Override
	public void setRecoil(double recoil) {
		CDClasses.ITEMGUN_RECOIL_FIELD.setValue(this.getItemInstance(), (float) recoil);
	}

	@Override
	public double getAccuracy() {
		return CDClasses.ITEMGUN_ACCURACY_FIELD.getValue(this.getItemInstance());
	}

	@Override
	public void setAccuracy(double accuracy) {
		CDClasses.ITEMGUN_ACCURACY_FIELD.setValue(this.getItemInstance(), (float) accuracy);
	}

	@Override
	public double getMovementPenalty() {
		return CDClasses.ITEMGUN_MOVEMENT_PENALTY_FIELD.getValue(this.getItemInstance());
	}

	@Override
	public void setMovementPenalty(double amount) {
		CDClasses.ITEMGUN_MOVEMENT_PENALTY_FIELD.setValue(this.getItemInstance(), amount);
	}

	@Override
	public double getZoomLevel() {
		return CDClasses.ITEMGUN_ZOOM_LEVEL_FIELD.getValue(this.getItemInstance());
	}

	@Override
	public void setZoomLevel(double amount) {
		CDClasses.ITEMGUN_ZOOM_LEVEL_FIELD.setValue(this.getItemInstance(), (float) amount);
	}

	@Override
	public void setSoundLevel(double soundLevel) {
		CDClasses.ITEMGUN_SOUND_LEVEL_FIELD.setValue(this.getItemInstance(), (float) soundLevel);
	}

	@Override
	public boolean canSpreadWhileAiming() {
		return CDClasses.ITEMGUN_SPREAD_WHILE_AIMING_FIELD.getValue(this.getItemInstance());
	}

	@Override
	public void setSpreadWhileAiming(boolean canSpread) {
		CDClasses.ITEMGUN_SPREAD_WHILE_AIMING_FIELD.setValue(this.getItemInstance(), canSpread);
	}

	@Override
	public boolean hasCrosshair() {
		return CDClasses.ITEMGUN_RENDER_CROSSHAIRS_FIELD.getValue(this.getItemInstance());
	}

	@Override
	public void setCrosshair(boolean hasCrosshair) {
		CDClasses.ITEMGUN_RENDER_CROSSHAIRS_FIELD.setValue(this.getItemInstance(), hasCrosshair);
	}

}
