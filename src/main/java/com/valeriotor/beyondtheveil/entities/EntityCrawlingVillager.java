package com.valeriotor.beyondtheveil.entities;

import java.util.List;
import java.util.UUID;

import javax.vecmath.Point3d;

import com.valeriotor.beyondtheveil.blocks.BlockRegistry;
import com.valeriotor.beyondtheveil.blocks.BlockWateryCradle;
import com.valeriotor.beyondtheveil.entities.dreamfocus.IDreamEntity;
import com.valeriotor.beyondtheveil.events.MemoryUnlocks;
import com.valeriotor.beyondtheveil.items.ItemRegistry;
import com.valeriotor.beyondtheveil.sacrifice.SacrificeHelper;
import com.valeriotor.beyondtheveil.tileEntities.TileDreamFocus;
import com.valeriotor.beyondtheveil.tileEntities.TileWateryCradle;
import com.valeriotor.beyondtheveil.tileEntities.TileWateryCradle.PatientStatus;
import com.valeriotor.beyondtheveil.tileEntities.TileWateryCradle.PatientTypes;
import com.valeriotor.beyondtheveil.util.ItemHelper;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.ItemHandlerHelper;

public class EntityCrawlingVillager extends EntityCreature implements IPlayerMinion, IDreamEntity{
	
	private boolean unconscious = false; // "unconscious" is synonym of "blackjack" and opposite of "spineless"
	private boolean heartless = false;
	private boolean weeper = false;
	private boolean loadFocus = false;
	private int ticksToDie = 0;
	private int ticksToFall = 0;
	private int ticksToRecovery = 200;
	private int introspectionCounter = 0;
	private int pointCounter = 0;
	private List<Point3d> points;
	private BlockPos altar;
	private BlockPos focus;
	private UUID master;
	public static final int DEFAULTTICKSTOFALL = 12;
	private static final DataParameter<Integer> TICKSTOFALL = EntityDataManager.<Integer>createKey(EntityCrawlingVillager.class, DataSerializers.VARINT);
	private static final DataParameter<Integer> PROFESSION = EntityDataManager.<Integer>createKey(EntityCrawlingVillager.class, DataSerializers.VARINT);
    
	public EntityCrawlingVillager(World w) {
		this(w, false);
	}
	
	
	public EntityCrawlingVillager(World worldIn, boolean blackjack) {
		this(worldIn, blackjack, false);
	}
	
	public EntityCrawlingVillager(World worldIn, BlockPos focus, int pointCounter, List<Point3d> points) {
		this(worldIn, true, false);
		this.focus = focus;
		this.pointCounter = pointCounter;
		this.points = points;
	}
	
	public EntityCrawlingVillager(World worldIn, boolean blackjack, boolean heartless) {
		super(worldIn);
		this.heartless = heartless;
		this.unconscious = blackjack;
		this.ticksToFall = blackjack && !heartless ? DEFAULTTICKSTOFALL : 0;
		this.dataManager.set(TICKSTOFALL, this.ticksToFall);
		this.ticksToDie = heartless ? 40 : -1;
	}
	
	@Override
	public void onAddedToWorld() {
		if(this.world.isRemote) this.ticksToFall = this.dataManager.get(TICKSTOFALL);
		super.onAddedToWorld();
	}
	
	@Override
	protected void entityInit() {
		this.dataManager.register(TICKSTOFALL, Integer.valueOf(0));
		this.dataManager.register(PROFESSION, Integer.valueOf(0));
		super.entityInit();
	}
	
	@Override
	protected void applyEntityAttributes() {
		super.applyEntityAttributes();
		getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(20.0D);
		getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.25D);		
	}
	
	@Override
	protected void initEntityAI() {
		if(!this.unconscious || this.focus != null) {
			this.tasks.addTask(8, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F));		
			this.tasks.addTask(8, new EntityAILookIdle(this));
			this.tasks.addTask(6, new EntityAIWander(this, 1.0D));
		}
		super.initEntityAI();
	}
	
	@Override
	protected boolean canDespawn() {
		return false;
	}
	
	@Override
	public boolean isAIDisabled() {
		return super.isAIDisabled() || (this.unconscious && this.focus == null);
	}
	
	@Override
	public void onEntityUpdate() {
		if(!this.world.isRemote) {
			if(this.ticksToDie > 0) this.ticksToDie--;
			else if(this.ticksToDie == 0) {
				this.ticksToDie--;
				if(this.altar != null) {
					Block block = world.getBlockState(altar).getBlock();
					if(block == BlockRegistry.BlockSacrificeAltar && master != null) {
						SacrificeHelper.doEffect(world.getPlayerEntityByUUID(master), altar);
					}
				}
				this.setHealth(0);
			}
			
			if(this.loadFocus) {
				this.setFocus(focus);
				this.loadFocus = false;
			}
			
			if(this.unconscious) {
				if(this.ticksToRecovery > 0) this.ticksToRecovery--;
				else {
					EntityVillager vil = new EntityVillager(this.world, this.getProfessionID());
					vil.setPosition(this.posX, this.posY, this.posZ);
					this.world.spawnEntity(vil);
					this.world.removeEntity(this);
				}
			}

			if(this.points != null)
				this.moveToNextPoint(this.points);
			if((this.ticksExisted & 7) == 0 && this.focus != null) {
				TileEntity te = this.world.getTileEntity(focus);
				if(!(te instanceof TileDreamFocus)) {
					this.focus = null;
					this.pointCounter = 0;
				}
			}
			
			this.introspectionCounter++;
			if(this.introspectionCounter > 100) {
				this.introspectionCounter = 0;
				EntityPlayer master = this.getMaster();
				if(master != null && master.getDistance(this) < 10)
					MemoryUnlocks.increaseIntrospection(master);
			}
		} else if(this.ticksToFall > 0) {
			this.ticksToFall--;
		}
		super.onEntityUpdate();
	}
	
	@Override
	public EnumActionResult applyPlayerInteraction(EntityPlayer player, Vec3d vec, EnumHand hand) {
		if(player.isSneaking() && player.getHeldItem(hand).getItem() == Items.AIR) {
			if(!this.world.isRemote) {
				ItemHandlerHelper.giveItemToPlayer(player, this.getItem());
				this.world.removeEntity(this);
			}
			return EnumActionResult.SUCCESS;
		}
		return EnumActionResult.PASS;
	}
	
	@SideOnly(Side.CLIENT)
	public int getTicksToFall() {
		return this.ticksToFall;
	}
	
	public void setTicksToFall(int ticks) {
		this.ticksToFall = ticks;
		this.dataManager.set(TICKSTOFALL, this.ticksToFall);
	}
	
	public void setProfession(int id) {
		this.dataManager.set(PROFESSION, Integer.valueOf(id));
	}
	
	public net.minecraftforge.fml.common.registry.VillagerRegistry.VillagerProfession getProfession(){
		return net.minecraftforge.fml.common.registry.VillagerRegistry.getById(this.dataManager.get(PROFESSION));
	}
	
	public int getProfessionID() {
		return this.dataManager.get(PROFESSION);
	}
	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound) {
		compound.setInteger("profession", this.dataManager.get(PROFESSION));
		compound.setInteger("ticksToRecovery", this.ticksToRecovery);
		compound.setBoolean("unconscious", this.unconscious);
		compound.setInteger("pointCounter", this.pointCounter);
		if(master != null) compound.setString("master", master.toString());
		if(altar != null) compound.setLong("altar", altar.toLong());
		if(focus != null) compound.setLong("focus", focus.toLong());
		return super.writeToNBT(compound);
	}
	
	@Override
	public void readFromNBT(NBTTagCompound compound) {
		this.setProfession(compound.getInteger("profession"));
		this.ticksToRecovery = compound.getInteger("ticksToRecovery");
		this.unconscious = compound.getBoolean("unconscious");
		this.pointCounter = compound.getInteger("pointCounter");
		if(compound.hasKey("master")) this.master = UUID.fromString(compound.getString("master"));
		if(compound.hasKey("altar")) this.altar = BlockPos.fromLong(compound.getLong("altar"));
		if(compound.hasKey("focus")) {
			this.focus = BlockPos.fromLong(compound.getLong("focus"));
			this.loadFocus = true;
		}
		super.readFromNBT(compound);
	}
	
	/** Just for the watery cradle texture, should not be used otherwise.
	 * 
	 */
	public void setWeeper(boolean par1) {
		this.weeper = par1;
	}
	
	public boolean isWeeper() {
		return this.weeper;
	}
	
	public void setAltar(BlockPos pos) {
		this.altar = pos;
	}
	
	public void setMaster(EntityPlayer player) {
		this.master = player.getPersistentID();
	}
	
	public EntityPlayer getMaster() {
		return world.getMinecraftServer().getPlayerList().getPlayerByUUID(this.master);
	}


	@Override
	public UUID getMasterID() {
		return this.master;
	}


	@Override
	public Point3d getNextPoint(List<Point3d> ps) {
		if(this.pointCounter < ps.size()) {
			Point3d p = ps.get(pointCounter);
			this.pointCounter++;
			BlockPos pos = new BlockPos(2*p.x-posX,2*p.y-posY,2*p.z-posZ);
			IBlockState state = world.getBlockState(pos);
			if(state.getBlock() == BlockRegistry.BlockWateryCradle) {
				if(state.getValue(BlockWateryCradle.PART) != BlockWateryCradle.EnumPart.STRUCTURE) {
					TileWateryCradle cradle = BlockRegistry.BlockWateryCradle.getTE(this.world, pos);
					if(cradle.getPatientType() == PatientTypes.NONE) {
						cradle.setPatient(PatientStatus.getPatientFromItem(this.getItem()));
						this.world.removeEntity(this);
					}
				}
			}
			return p;
		}
		this.focus = null;
		this.pointCounter = 0;
		return null;
	}
	
	private ItemStack getItem() {
		ItemStack item = new ItemStack(ItemRegistry.held_villager);
		ItemHelper.checkTagCompound(item).setBoolean("spineless", !this.unconscious);
		item.getTagCompound().setBoolean("heartless", this.heartless);
		item.getTagCompound().setInteger("profession", this.getProfessionID());
		return item;
	}
	
	public BlockPos getFocus() {
		return this.focus;
	}
	
	public void setFocus(BlockPos focus) {
		this.focus = focus;
		TileEntity te = this.world.getTileEntity(focus);
		if(!(te instanceof TileDreamFocus)) this.focus = null;
		else {
			this.points = ((TileDreamFocus)te).getPoints();
		}
	}
	

}
