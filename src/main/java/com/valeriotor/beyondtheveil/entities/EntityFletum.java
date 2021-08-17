package com.valeriotor.beyondtheveil.entities;

import java.util.UUID;

import com.valeriotor.beyondtheveil.blocks.BlockRegistry;
import com.valeriotor.beyondtheveil.items.ItemRegistry;
import com.valeriotor.beyondtheveil.lib.BTVSounds;
import com.valeriotor.beyondtheveil.tileEntities.TileLacrymatory;

import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.ItemHandlerHelper;

public class EntityFletum extends EntityCreature implements IWeepingEntity, IPlayerMinion{

	private int animationTicks = 0;
	private int tearTicks;
	private int dialogue = -1;
	private BlockPos lacrymatory;
	private UUID master;
	
	public EntityFletum(World worldIn) {
		super(worldIn);
		this.tearTicks = this.getTearTicks();
		this.setSize(0.8F, 0.7F);
	}
	
	@Override
	public void onLivingUpdate() {
		super.onLivingUpdate();
		if(this.world.isRemote) {
			this.animationTicks++;
			this.animationTicks%=200;
		}else {
			if(!this.isFocusing()) {
				this.tearTicks--;
				if(this.tearTicks <= 0) {
					TileLacrymatory.fillWithTears(this);
					this.tearTicks = this.getTearTicks();
				}
			}
		}
	}
	
	@Override
	public EnumActionResult applyPlayerInteraction(EntityPlayer player, Vec3d vec, EnumHand hand) {
		if(hand == EnumHand.OFF_HAND) return EnumActionResult.PASS;
		if(player.getHeldItem(hand).getItem() == Items.AIR) {
			if(player.isSneaking()) {
				if(this.lacrymatory != null) {
					TileEntity te = this.world.getTileEntity(lacrymatory);
					if(te instanceof TileLacrymatory) {
						TileLacrymatory tl = (TileLacrymatory) te;
						tl.setWeeper(null);
					}
				}
				if(!this.world.isRemote) {
					ItemStack item = new ItemStack(ItemRegistry.held_fletum);
					ItemHandlerHelper.giveItemToPlayer(player, item);
					this.world.removeEntity(this);
				}
				return EnumActionResult.SUCCESS;
			} else {
				if(!this.world.isRemote) {
					if(this.dialogue == -1) this.dialogue = rand.nextInt(36) / 5;
					player.sendMessage(new TextComponentString(new TextComponentTranslation("fletum.dialogue." + dialogue).getFormattedText()));
					return EnumActionResult.SUCCESS;
				}	
			}
		}
		return EnumActionResult.PASS;
	}
	
	@SideOnly(Side.CLIENT)
	public float getAnimationTicks() {
		return this.animationTicks;
	}
	
	@Override
	public void readFromNBT(NBTTagCompound compound) {
		this.tearTicks = compound.getInteger("tearTicks");
		this.dialogue = compound.getInteger("dialogue");
		if(compound.hasKey("master"))
			this.master = UUID.fromString(compound.getString("master"));
		if(compound.hasKey("lacrymatory"))
		if(compound.hasKey("lacrymatory"))
			this.lacrymatory = BlockPos.fromLong(compound.getLong("lacrymatory"));
		super.readFromNBT(compound);
	}
	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound) {
		compound.setInteger("tearTicks", this.tearTicks);
		compound.setInteger("dialogue", dialogue);
		if(this.master != null)
			compound.setString("master", this.master.toString());
		if(this.lacrymatory != null)
			compound.setLong("lacrymatory", this.lacrymatory.toLong());
		return super.writeToNBT(compound);
	}
	
	@Override
	public void onDeath(DamageSource cause) {
		if(this.lacrymatory != null) {
			TileEntity te = this.world.getTileEntity(lacrymatory);
			if(te instanceof TileLacrymatory) {
				TileLacrymatory tl = (TileLacrymatory) te;
				tl.setWeeper(null);
			}
		}
		super.onDeath(cause);
	}
	
	@Override
	protected boolean canDespawn() {
		return false;
	}
	
	@Override
	public boolean canBreatheUnderwater() {
		return true;
	}
	
	@Override
	public void fall(float distance, float damageMultiplier) {}
	
	@Override
	public void setLacrymatory(BlockPos pos) {
		this.lacrymatory = pos;
	}

	@Override
	public BlockPos getLacrymatory() {
		return this.lacrymatory;
	}


	@Override
	public EntityPlayer getMaster() {
		if(master == null) return null;
		return world.getMinecraftServer().getPlayerList().getPlayerByUUID(master);
	}

	@Override
	public UUID getMasterID() {
		return master;
	}

	@Override
	public void setMaster(EntityPlayer p) {
		if(p != null)
			this.master = p.getPersistentID();
	}
	
	@Override
	protected SoundEvent getAmbientSound() {
		if(this.isFocusing()) return null;
		return BTVSounds.fletum_weeping;
	}
	
	@Override
	public int getTalkInterval() {
		return 240;
	}

	@Override
	public int getTearTicks() {
		return 950;
	}
	
	public boolean isFocusing() {
		if(this.world.getBlockState(this.getPosition().down()).getBlock() == BlockRegistry.BlockDreamFocus) return true;
		for(EnumFacing facing : EnumFacing.HORIZONTALS) {
			if(this.world.getBlockState(this.getPosition().offset(facing)).getBlock() == BlockRegistry.BlockDreamFocusFluids) return true;
		}
		if(this.world.getBlockState(this.getPosition().up()).getBlock() == BlockRegistry.BlockDreamFocusVillagers) return true;
		return false;
	}
	
}
