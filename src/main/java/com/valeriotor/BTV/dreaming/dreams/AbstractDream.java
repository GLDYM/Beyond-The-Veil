package com.valeriotor.BTV.dreaming.dreams;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import thaumcraft.api.capabilities.IPlayerKnowledge;

public abstract class AbstractDream{
	
	public final int priority;
	public final String name;
	
	public AbstractDream(String name, int priority) {
		this.priority = priority;
		this.name = name;
	}
	
	public abstract boolean activate(EntityPlayer p, World w, IPlayerKnowledge k);
	
	public String getName() {
		return this.name;
	}
	
	public boolean activateBottle(EntityPlayer p, World w, IPlayerKnowledge k) {
		return this.activate(p, w, k);
	}
	
}