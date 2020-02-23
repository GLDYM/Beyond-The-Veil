package com.valeriotor.BTV.dreaming.dreams;

import java.util.List;

import com.valeriotor.BTV.capabilities.IPlayerData;
import com.valeriotor.BTV.capabilities.PlayerDataProvider;
import com.valeriotor.BTV.dreaming.DreamHandler;
import com.valeriotor.BTV.lib.PlayerDataLib;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;

public class DreamDeath extends Dream{

	public DreamDeath(String name, int priority) {
		super(name, priority);
	}
	
	@Override
	public boolean activatePos(EntityPlayer p, World w, BlockPos pos) {
		return this.activatePlayer(p, null, w);
	}
	
	@Override
	public boolean activatePlayer(EntityPlayer p, EntityPlayer target, World w) {
		if(!DreamHandler.youDontHaveLevel(p, 2)) return false;
		if(target == null) {
			List<EntityPlayerMP> list = w.getPlayers(EntityPlayerMP.class, player -> !player.equals(p));
			if(list.isEmpty()) {
				p.sendMessage(new TextComponentTranslation("dreams.playersearch.fail"));
				return false;
			}
			target = list.get(w.rand.nextInt(list.size()));
		}
		int lvl = DreamHandler.getDreamPowerLevel(p);
		if(DreamHandler.getDreamAttack(p, target) >= 0) {
			IPlayerData cap = target.getCapability(PlayerDataProvider.PLAYERDATA, null);
			Integer x = cap.getInteger(PlayerDataLib.DEATH_X);
			Integer y = cap.getInteger(PlayerDataLib.DEATH_Y);
			Integer z = cap.getInteger(PlayerDataLib.DEATH_Z);
			if(x != null && y != null && z != null) {
				p.sendMessage(new TextComponentTranslation("dreams.deathsearch.found", new Object[] {target.getName(), x, y, z}));
			} else {
				p.sendMessage(new TextComponentTranslation("dreams.deathsearch.notrecently", new Object[] {target.getName()}));
			}
		} else {
			p.sendMessage(new TextComponentTranslation("dreams.deathsearch.toostrong", new Object[] {target.getName()}));
		}
		return true;
	}

}
