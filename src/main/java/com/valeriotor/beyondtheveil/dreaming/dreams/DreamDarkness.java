package com.valeriotor.beyondtheveil.dreaming.dreams;

import com.valeriotor.beyondtheveil.dreaming.DreamHandler;
import com.valeriotor.beyondtheveil.util.WorldHelper;
import com.valeriotor.beyondtheveil.world.HamletList;
import com.valeriotor.beyondtheveil.world.biomes.BiomeRegistry;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;

public class DreamDarkness extends Dream{

	public DreamDarkness(String name, int priority) {
		super(name, priority);
	}

	@Override
	public boolean activatePos(EntityPlayer p, World w, BlockPos pos) {
		boolean flag = false;
		BlockPos newPos = WorldHelper.findClosestBiomeOfType(BiomeRegistry.innsmouth, pos, w, 6000);
		if(newPos != null) {
			p.sendMessage(new TextComponentTranslation("dreams.biomesearch.innsmouth", new Object[] {newPos.getX(), newPos.getZ()}));			
			flag = true;
		} else {
			p.sendMessage(new TextComponentTranslation("dreams.biomesearch.fail"));
		}
		
		if(DreamHandler.hasDreamtOfVoid(p)) {
			BlockPos hamletPos = HamletList.get(w).getClosestHamlet(pos);
			if(hamletPos != null) {
				p.sendMessage(new TextComponentTranslation("dreams.biomesearch.hamlet", new Object[] {Integer.valueOf(hamletPos.getX()), Integer.valueOf(hamletPos.getZ())}));
				flag = true;
			}
		}
		
		return flag;
	}

}
