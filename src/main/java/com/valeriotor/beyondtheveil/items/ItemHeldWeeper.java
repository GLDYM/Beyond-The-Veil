package com.valeriotor.beyondtheveil.items;

import java.util.List;

import com.valeriotor.beyondtheveil.blocks.BlockRegistry;
import com.valeriotor.beyondtheveil.entities.EntityWeeper;
import com.valeriotor.beyondtheveil.lib.References;
import com.valeriotor.beyondtheveil.tileEntities.TileLacrymatory;
import com.valeriotor.beyondtheveil.util.ItemHelper;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;

public class ItemHeldWeeper extends ModItem {
	
	public ItemHeldWeeper(String name) {
		super(name);
		this.setMaxStackSize(1);
	}
	
	@Override
	public EnumActionResult onItemUse(EntityPlayer player, World w, final BlockPos pos, EnumHand hand,
			EnumFacing facing, float hitX, float hitY, float hitZ) {
		
		TileLacrymatory tl = null;
		if(w.getTileEntity(pos) instanceof TileLacrymatory) {
			tl = (TileLacrymatory)w.getTileEntity(pos);
		}
		
		if(facing != EnumFacing.UP && (facing == EnumFacing.DOWN || tl == null)) 
			return EnumActionResult.FAIL;
		
		if(w.isRemote) return EnumActionResult.SUCCESS;
		
		boolean spineless = ItemHelper.checkBooleanTag(player.getHeldItem(hand), "spineless", false);
		boolean heartless = ItemHelper.checkBooleanTag(player.getHeldItem(hand), "heartless", false);
		
		EntityWeeper weeper = new EntityWeeper(w, spineless);
		
		
		BlockPos weeperPos = pos;
		if(tl == null) weeperPos = weeperPos.offset(EnumFacing.UP);
		else weeperPos = weeperPos.offset(facing);
		
		for(int y = 0; y <= 2; y++) {
			IBlockState b = w.getBlockState(weeperPos.add(0, y, 0));
			if((b.causesSuffocation() || b.isFullBlock()) && b.getBlock() != BlockRegistry.BlockLacrymatory) {
				return EnumActionResult.FAIL;
			}
		}
		
		if(tl != null && tl.setWeeper(weeper)) {
			weeper.setLacrymatory(pos);
		}else if(tl != null) {
			player.sendMessage(new TextComponentTranslation("interact.lacrymatory.full"));
			return EnumActionResult.FAIL;
		}
		
		if(tl != null) player.sendMessage(new TextComponentTranslation("interact.lacrymatory.success", new TextComponentTranslation("entity.weeper.name")));
		
		weeper.setPosition(weeperPos.getX() + 0.5, weeperPos.getY(), weeperPos.getZ() + 0.5);
		weeper.setHeartless(heartless);
		weeper.setMaster(player);
		w.spawnEntity(weeper);
		player.getHeldItem(hand).shrink(1);
		
		return super.onItemUse(player, w, pos, hand, facing, hitX, hitY, hitZ);
	}
	
	@Override
	public void addInformation(ItemStack stack, World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
		boolean spineless = ItemHelper.checkBooleanTag(stack, "spineless", false);
		boolean heartless = ItemHelper.checkBooleanTag(stack, "heartless", false);
		tooltip.add(I18n.format(String.format("tooltip.held_weeper.%s%s", spineless ? "spineless" : "spineful", heartless ? "heartless" : "")));
		super.addInformation(stack, worldIn, tooltip, flagIn);
	}
	
}
