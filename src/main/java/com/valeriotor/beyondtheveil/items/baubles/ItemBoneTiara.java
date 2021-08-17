package com.valeriotor.beyondtheveil.items.baubles;

import com.valeriotor.beyondtheveil.capabilities.PlayerDataProvider;
import com.valeriotor.beyondtheveil.items.ModItem;
import com.valeriotor.beyondtheveil.lib.PlayerDataLib;
import com.valeriotor.beyondtheveil.lib.References;
import com.valeriotor.beyondtheveil.potions.PotionRegistry;

import baubles.api.BaubleType;
import baubles.api.IBauble;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class ItemBoneTiara extends ModItem implements IBauble{
	
	public ItemBoneTiara(String name) {
		super(name);
		this.setMaxStackSize(1);
	}
	
	@Override
	public void onWornTick(ItemStack itemstack, EntityLivingBase p) {
		if(p.world.isRemote) return;
		if((p.getActivePotionEffect(PotionRegistry.folly) != null ||  p.getActivePotionEffect(PotionRegistry.terror) != null) && 
			p.getCapability(PlayerDataProvider.PLAYERDATA, null).getOrSetInteger(String.format(PlayerDataLib.PASSIVE_BAUBLE, 4), 0, false) == 1) {
				p.removePotionEffect(PotionRegistry.folly);
				p.removePotionEffect(PotionRegistry.terror);
		}
	}
	
	@Override
	public BaubleType getBaubleType(ItemStack itemstack) {
		return BaubleType.HEAD;
	}

}
