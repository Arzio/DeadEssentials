package com.arzio.deadessentials.wrapper.item;

public interface Ammo extends CDItem {

	public double getPenetration();
	
	public void setPenetration(double value);
	
	public int getBulletAmount();
	
	public void setBulletAmount(int amount);
}
