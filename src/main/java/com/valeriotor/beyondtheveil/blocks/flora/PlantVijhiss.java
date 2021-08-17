package com.valeriotor.beyondtheveil.blocks.flora;

import java.util.Random;

import com.valeriotor.beyondtheveil.blocks.BlockRegistry;
import com.valeriotor.beyondtheveil.blocks.EnumHalf;
import com.valeriotor.beyondtheveil.capabilities.PlayerDataProvider;
import com.valeriotor.beyondtheveil.items.ItemRegistry;
import com.valeriotor.beyondtheveil.research.ResearchUtil;
import com.valeriotor.beyondtheveil.tileEntities.TileMutator;
import com.valeriotor.beyondtheveil.util.SyncUtil;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.items.ItemHandlerHelper;

public class PlantVijhiss extends BlockTallPlant implements IMutationCatalyst{
	
	public static final double a = 0.0625;
	public static final AxisAlignedBB LOWER_BBOX = new AxisAlignedBB(a*5, 0, a*6, a*9, 1, a*11);
	public static final AxisAlignedBB UPPER_BBOX = new AxisAlignedBB(a*5, 0, a*6, a*9, a*4, a*11);
	
	public PlantVijhiss(String name) {
		super(Material.WOOD, name);
		this.setSoundType(SoundType.WOOD);
	}
	
	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn,
			EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		if(!worldIn.isRemote)
		if(hand == EnumHand.MAIN_HAND) {
			ItemStack stack = playerIn.getHeldItem(hand);
			if(stack.getItem() == Items.GOLD_INGOT) {
				ItemStack seeds = ItemStack.EMPTY;
				seeds = ItemRegistry.getRandomSeed(RANDOM, 10);
				for(int i = 0; i < 100 && seeds == ItemStack.EMPTY; i++)
				 seeds = ForgeHooks.getGrassSeed(RANDOM, 0);
				if(seeds == ItemStack.EMPTY || seeds.getItem() == Items.WHEAT_SEEDS) seeds = new ItemStack(getRandomSeed(RANDOM));
				if(seeds != ItemStack.EMPTY) {
					stack.shrink(1);
					ItemHandlerHelper.giveItemToPlayer(playerIn, seeds);
				}
				if(!playerIn.getCapability(PlayerDataProvider.PLAYERDATA, null).getString("usedvijhiss")) {
					SyncUtil.addStringDataOnServer(playerIn, false, "usedvijhiss");
				}
			} else if(stack.getItem() == Item.getItemFromBlock(Blocks.GOLD_BLOCK)) {
				stack.shrink(1);
				ItemHandlerHelper.giveItemToPlayer(playerIn, new ItemStack(BlockRegistry.BlockMegydrea));
				if(!playerIn.getCapability(PlayerDataProvider.PLAYERDATA, null).getString("gotmegydrea"))
					SyncUtil.addStringDataOnServer(playerIn, false, "gotmegydrea");
				return true;
			}
		}
		return true;
	}
	
	private static Item getRandomSeed(Random r) {
		int a = r.nextInt(25);
		if(a < 8) return Items.WHEAT_SEEDS;
		else if(a < 15) return Items.BEETROOT_SEEDS;
		else if(a < 20) return Items.MELON_SEEDS;
		else return Items.PUMPKIN_SEEDS;
	}
	
	@Override
	public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer,
			ItemStack stack) {
		//this.updateMutators(worldIn, pos);
		super.onBlockPlacedBy(worldIn, pos, state, placer, stack);
	}
	
	@Override
	public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
		//this.updateMutators(worldIn, pos);
		super.breakBlock(worldIn, pos, state);
	}
	
	public void updateMutators(World worldIn, BlockPos pos) {
		for(int x = -3; x < 4; x++) {
			for(int y = -3; y < 4; y++) {
				for(int z = -3; z < 4; z++) {
					Block b = worldIn.getBlockState(pos.add(x, y, z)).getBlock();
					if(b instanceof BlockMutator) {
						TileEntity te = worldIn.getTileEntity(pos.add(x, y, z));
						if(te != null && te instanceof TileMutator) {
							((TileMutator)te).blockAreaUpdate();
						}
					}
				}
			}
		}
	}

	@Override
	public int mutationIncrease() {
		return 3;
	}
	
	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
		if(state.getValue(EnumHalf.HALF) == EnumHalf.BOTTOM)
			return LOWER_BBOX;
		else
			return UPPER_BBOX;
	}
	
	@Override
	public boolean isOpaqueCube(IBlockState state) {
		return false;
	}
	
	@Override
	public boolean isFullBlock(IBlockState state) {
		return false;
	}
	
}
