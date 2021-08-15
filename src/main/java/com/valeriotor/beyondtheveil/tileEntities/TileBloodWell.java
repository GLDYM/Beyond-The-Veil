package com.valeriotor.beyondtheveil.tileEntities;

import java.util.List;
import java.util.function.Function;

import com.valeriotor.beyondtheveil.blackmirror.MirrorUtil;
import com.valeriotor.beyondtheveil.blocks.BlockRegistry;
import com.valeriotor.beyondtheveil.entities.EntityBloodSkeleton;
import com.valeriotor.beyondtheveil.entities.EntityBloodZombie;
import com.valeriotor.beyondtheveil.entities.IPlayerGuardian;
import com.valeriotor.beyondtheveil.events.ServerTickEvents;
import com.valeriotor.beyondtheveil.items.ItemRegistry;
import com.valeriotor.beyondtheveil.lib.BTVSounds;
import com.valeriotor.beyondtheveil.lib.PlayerDataLib;
import com.valeriotor.beyondtheveil.util.PlayerTimer;
import com.valeriotor.beyondtheveil.util.Teleport;
import com.valeriotor.beyondtheveil.world.DimensionRegistry;
import com.valeriotor.beyondtheveil.world.Structures.arche.BloodHomeList;

import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.monster.EntityPigZombie;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.entity.monster.EntityWitherSkeleton;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.MobEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;

public class TileBloodWell extends TileEntity implements ITickable{
	
	public TileBloodWell() {}
	

	private int counter = 29;
	private int soundCounter = 30;
	private int bloodZombies = 0;
	private int bloodSkellies = 0;
	
	@Override
	public void update() {
		if(!this.world.isRemote) {
			counter--;
			if(counter % 10 == 0) {
				if(counter <= 0) {
					if(!BlockRegistry.BlockBloodWell.checkStructure(pos, world)) {
						world.setBlockState(pos, Blocks.AIR.getDefaultState());
						return;
					}
					teleportPlayer();
					counter = 29;
					List<EntityLiving> undead = this.world.getEntities(EntityLiving.class, e -> e.isEntityUndead() && e.getDistanceSq(pos) < 1024 && !(e instanceof IPlayerGuardian));
					for(EntityLiving e : undead) {
						ServerTickEvents.insertDamned(e.getEntityId());
						e.getNavigator().setPath(e.getNavigator().getPathToPos(this.pos), 0.9);
					}
				} else {
					List<EntityLiving> undead = this.world.getEntities(EntityLiving.class, e -> e.isEntityUndead() && e.getDistanceSq(pos) < 3 && !(e instanceof IPlayerGuardian));
					for(EntityLiving ent : undead) {
						if(ent instanceof EntityZombie || ent instanceof EntityPigZombie) {
							bloodZombies++;
						} else if(ent instanceof EntitySkeleton || ent instanceof EntityWitherSkeleton) {
							bloodSkellies++;
						}
						if(ent.isNonBoss()) world.removeEntity(ent);
					}
				}
			}
		} else {
			soundCounter--;
			if(soundCounter <= 0) {
				soundCounter = 20;
				this.world.playSound(pos.getX(), pos.getY(), pos.getZ(), BTVSounds.heartbeat, SoundCategory.AMBIENT, 1, 1, true);
			}
		}
	}
	
	private void teleportPlayer() {
		EntityPlayer p = world.getClosestPlayer(this.pos.getX(), this.pos.getY(), this.pos.getZ(), 1, false);
		if(p != null && PlayerDataLib.getCap(p).getString("canEnterArche")) {
			for (ItemStack i : p.inventory.mainInventory) {
				if (i.getItem() == ItemRegistry.fleshcarbontoken) {
					WorldServer w = world.getMinecraftServer().getWorld(DimensionRegistry.ARCHE.getId());
					BlockPos toPos = BloodHomeList.get(w).findPlayerHome(p);
					MirrorUtil.updateDefaultDialogue(p, "bloodhome");
					p.changeDimension(DimensionRegistry.ARCHE.getId(), new Teleport(w, toPos.getX(), toPos.getY(), toPos.getZ()));
					break;
				}
			}
		}
	}
	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound) {
		compound.setInteger("zombies", bloodZombies);
		compound.setInteger("skellies", bloodSkellies);
		return super.writeToNBT(compound);
	}
	
	@Override
	public void readFromNBT(NBTTagCompound compound) {
		if(compound.hasKey("zombies")) this.bloodZombies = compound.getInteger("zombies");
		if(compound.hasKey("skellies")) this.bloodSkellies = compound.getInteger("skellies");
		super.readFromNBT(compound);
	}
	
	public boolean takeMob(BloodMobs type) {
		if(type == BloodMobs.BLOOD_ZOMBIE && this.bloodZombies > 0) {
			this.bloodZombies--;
			return true;
		} else if(type == BloodMobs.BLOOD_SKELLIE && this.bloodSkellies > 0) {
			this.bloodSkellies--;
			return true;
		}
		return false;
	}
	
	public void addMob(BloodMobs type) {
		switch(type) {
		case BLOOD_SKELLIE: this.bloodSkellies++;
			break;
		case BLOOD_ZOMBIE: 	this.bloodZombies++;
			break;
		default:
			break;
		
		}
	}
	
	public void sendInfo(EntityPlayer p) {
		p.sendMessage(new TextComponentTranslation("interact.blood_well.amount", this.bloodSkellies, this.bloodZombies));
	}
	
	public static enum BloodMobs {
		BLOOD_ZOMBIE(w -> new EntityBloodZombie(w)),
		BLOOD_SKELLIE(w -> new EntityBloodSkeleton(w));
		
		private Function<World, IPlayerGuardian> func;
		private BloodMobs(Function<World, IPlayerGuardian> func) {
			this.func = func;
		}
		
		public IPlayerGuardian getEntity(World w, EntityPlayer master) {
			IPlayerGuardian guardian = this.func.apply(w);
			guardian.setMaster(master);
			return guardian;
		}
		
	}
	
	private final AxisAlignedBB BBOX = new AxisAlignedBB(-1, 0, -1, 1, 1, 1);
	@Override
	public AxisAlignedBB getRenderBoundingBox() {
		return BBOX;
	}

}
