package com.valeriotor.beyondtheveil.blocks;

import java.util.List;
import java.util.Random;

import javax.annotation.Nullable;

import com.google.common.base.Predicate;
import com.valeriotor.beyondtheveil.dreaming.Memory;
import com.valeriotor.beyondtheveil.items.ItemRegistry;
import com.valeriotor.beyondtheveil.lib.BlockNames;
import com.valeriotor.beyondtheveil.tileEntities.TileFumeSpreader;
import com.valeriotor.beyondtheveil.util.ItemHelper;

import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.items.ItemHandlerHelper;

public class BlockFumeSpreader extends ModBlock implements ITileEntityProvider{
	
	
	
	
	public static final PropertyBool ISFULL = PropertyBool.create("is_full");
	
	private static final AxisAlignedBB BBox = new AxisAlignedBB(0.0625 * 4, 0, 0.0625 * 4, 0.0625 * 12, 0.0625 * 11, 0.0625 * 12);

	public BlockFumeSpreader() {
		super(Material.GLASS, BlockNames.FUMESPREADER);
		this.setResistance(50.0F);
		this.setHardness(2.0F);
		this.setSoundType(SoundType.GLASS);
		
		this.setDefaultState(this.blockState.getBaseState().withProperty(ISFULL, false));
		
	}
	
	@Override
	public boolean isOpaqueCube(IBlockState state) {
		return false;
	}
	
	
	@Override
	public BlockRenderLayer getBlockLayer() {
		return  BlockRenderLayer.TRANSLUCENT;
	}
	
	@Override
	public boolean isFullCube(IBlockState state) {
		return false;
	}
	
	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
		return BBox;
	}
	
	@Override
	public void addCollisionBoxToList(IBlockState state, World worldIn, BlockPos pos, AxisAlignedBB entityBox,
			List<AxisAlignedBB> collidingBoxes, Entity entityIn, boolean isActualState) {
		super.addCollisionBoxToList(pos, entityBox, collidingBoxes, BBox);
	}
	
	public boolean canPlaceBlockAt(World worldIn, BlockPos pos) {
        IBlockState state = worldIn.getBlockState(pos.down());
        return state.getBlock().canPlaceTorchOnTop(state, worldIn, pos);
    }
	
	public IBlockState getStateFromMeta(int meta)
    {
        IBlockState iblockstate = this.getDefaultState();
        
        switch (meta)
        {
            case 1:
                return iblockstate.withProperty(ISFULL, true);
            case 0:
            default:
                return iblockstate.withProperty(ISFULL, false);
        }
    }
	
	public int getMetaFromState(IBlockState state)
    {
		return state.getValue(ISFULL) ? 1 : 0;
    }
	
	public BlockFaceShape getBlockFaceShape(IBlockAccess worldIn, IBlockState state, BlockPos pos, EnumFacing face)
    {
        return BlockFaceShape.UNDEFINED;
    }
	
	protected BlockStateContainer createBlockState()
    {
        return new BlockStateContainer(this, new IProperty[] {ISFULL});
    }
	
	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn,
			EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		
		if(playerIn.getHeldItem(hand).getItem() == ItemRegistry.oniricIncense && !state.getValue(ISFULL)) {
			if(!playerIn.capabilities.isCreativeMode) playerIn.getHeldItem(hand).shrink(1);
			state = state.withProperty(ISFULL, true);
			worldIn.setBlockState(pos, state, 2);
		}else
		if(worldIn.getTileEntity(pos) instanceof TileFumeSpreader && state.getValue(ISFULL)) {
			if(playerIn.getHeldItem(hand).getItem() == ItemRegistry.memory_phial && getTE(worldIn, pos).getMemory() == null) {
				String memString = ItemHelper.checkStringTag(playerIn.getHeldItem(hand), "memory", "none");
				if(!worldIn.isRemote && !memString.equals("null")) {
					getTE(worldIn, pos).setMemory(Memory.getMemoryFromDataName(memString));
				}
				if(!playerIn.capabilities.isCreativeMode) playerIn.getHeldItem(hand).shrink(1);
			}
			
		}
		if(playerIn.getHeldItem(hand).getItem() == Items.AIR && playerIn.isSneaking() && state.getValue(ISFULL)) {
			worldIn.setBlockState(pos, state.withProperty(ISFULL, false));
			if(!playerIn.capabilities.isCreativeMode) ItemHandlerHelper.giveItemToPlayer(playerIn, new ItemStack(ItemRegistry.oniricIncense));
		}
		
		return true;
	}
	
	@Override
	public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos) {
		return super.getActualState(state, worldIn, pos);
	}

	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		
		return new TileFumeSpreader();
	}
	
	
	public static TileFumeSpreader getTE(World w, BlockPos p) {
		return (TileFumeSpreader) w.getTileEntity(p);
	}
	
	@Override
	public void randomDisplayTick(IBlockState stateIn, World worldIn, BlockPos pos, Random rand) {
		Memory memory = getTE(worldIn,pos).getMemory();
		if(memory != null) {
			int i = memory.getColor();
			double d0 = (double)(i >> 16 & 255) / 255.0D;
	        double d1 = (double)(i >> 8 & 255) / 255.0D;
	        double d2 = (double)(i >> 0 & 255) / 255.0D;
			worldIn.spawnParticle(EnumParticleTypes.SPELL_MOB, pos.getX()+0.5D, pos.getY()+0.7D, pos.getZ()+0.5D, d0, d1, d2);
		}
	}
	
}
