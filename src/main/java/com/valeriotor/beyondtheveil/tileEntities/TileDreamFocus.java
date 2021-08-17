package com.valeriotor.beyondtheveil.tileEntities;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import javax.annotation.Nullable;
import javax.vecmath.Point3d;

import com.valeriotor.beyondtheveil.BeyondTheVeil;
import com.valeriotor.beyondtheveil.blocks.BlockDreamFocus;
import com.valeriotor.beyondtheveil.blocks.BlockDreamFocusFluids;
import com.valeriotor.beyondtheveil.blocks.BlockDreamFocusVillagers;
import com.valeriotor.beyondtheveil.blocks.ModBlockFacing;
import com.valeriotor.beyondtheveil.entities.EntityCrawlingVillager;
import com.valeriotor.beyondtheveil.entities.dreamfocus.EntityDreamFluid;
import com.valeriotor.beyondtheveil.entities.dreamfocus.EntityDreamItem;
import com.valeriotor.beyondtheveil.entities.dreamfocus.EntityDreamVillager;
import com.valeriotor.beyondtheveil.entities.dreamfocus.IDreamEntity;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidTankProperties;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

public class TileDreamFocus extends TileEntity implements ITickable{

	private List<Point3d> points = new ArrayList<>();
	private int counter = 0;
	private UUID usingPlayer;
	private int playerCounter = 0;
	private boolean showPath = true;
	private FocusType type = FocusType.ITEM;
	private EnumDyeColor dye = EnumDyeColor.RED;
	
	public TileDreamFocus() {}
	public TileDreamFocus(FocusType type) {
		this.type = type;
	}
	
	public void setDyeColor(EnumDyeColor dye) {
		this.dye = dye;
		markDirty();
		this.sendUpdates(world);
	}
	
	public void toggleShowPath() {
		this.showPath = !this.showPath;
		markDirty();
		this.sendUpdates(world);
	}
	
	@Override
	public void update() {
		if(this.world.isRemote) {
			if((this.counter & 1) == 0 && this.showPath)
				BeyondTheVeil.proxy.renderEvents.renderDreamFocusPath(points, world, dye);
			this.counter++;
			if(this.counter > 400)
				this.counter = 0;
		} else {
			this.counter++;
			if(this.counter > 150) {
				if(this.usingPlayer != null && this.world.getPlayerEntityByUUID(usingPlayer) == null) this.usingPlayer = null;

				int redstone = 0;
				for(EnumFacing facing : EnumFacing.VALUES) {
					int a = world.getRedstonePower(pos.offset(facing), facing);
					if(a > redstone) redstone = a;
				}
				if(this.type == FocusType.ITEM) {
					if(redstone == 0 && BlockDreamFocus.hasFletum(world, pos)) {
						EnumFacing facing = this.getState().getValue(ModBlockFacing.FACING).getOpposite();
						TileEntity te = world.getTileEntity(pos.offset(facing));
						if(te != null && te.hasCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, facing.getOpposite())) {
							IItemHandler cap = te.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, facing.getOpposite());
							for(int i = 0; i < cap.getSlots(); i++) {
								ItemStack stack = cap.extractItem(i, 64, false);
								if(!stack.isEmpty()) {
									BlockPos front = this.pos.offset(facing.getOpposite());
									EntityDreamItem edi =new EntityDreamItem(this.world, front.getX()+0.5, front.getY(), front.getZ()+0.5, stack, this.pos, Collections.unmodifiableList(this.points));
									this.world.spawnEntity(edi);
									break;
								}
							}
						}
					}
				} else if(this.type == FocusType.FLUID) {
					int fleti = BlockDreamFocusFluids.hasFleti(world, pos);
					if(redstone == 0 && fleti > 0) {
						TileEntity te = world.getTileEntity(pos.offset(EnumFacing.UP));
						if(te != null && te.hasCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, EnumFacing.DOWN)) {
							IFluidHandler f = te.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, EnumFacing.DOWN);
							//FluidStack stack = f.drain(new FluidStac, doDrain)
							Fluid fluid = null;
							for(IFluidTankProperties ftp : f.getTankProperties()) {
								FluidStack test = ftp.getContents();
								if(test != null && test.amount > 0) {
									fluid = test.getFluid();
									break;
								}
							}
							if(fluid != null) {
								int a = 0;
								for(int i = 1; i < fleti+1; i++) a+=i;
								FluidStack toDrain = f.drain(new FluidStack(fluid, a*100), true);
								if(toDrain.amount > 0) {
									EntityDreamFluid edf = new EntityDreamFluid(this.world, toDrain, this.pos, Collections.unmodifiableList(this.points));
									edf.setPosition(this.pos.getX()+0.5, this.pos.getY()-1, this.pos.getZ()+0.5);
									this.world.spawnEntity(edf);
								}
							}
						}
						
					}
				} else if(this.type == FocusType.VILLAGER) {
					if(redstone < 15 && BlockDreamFocusVillagers.hasFletum(world, pos)) {
						IDreamEntity toMove = null;
						AxisAlignedBB bbox = new AxisAlignedBB(pos.add(-5, 0, -5), pos.add(6, 5, 6));
						List<EntityCrawlingVillager> worms = world.getEntitiesWithinAABB(EntityCrawlingVillager.class, bbox);
						for(EntityCrawlingVillager worm : worms) {
							if(worm.getFocus() == null) {
								worm.setFocus(this.pos);
								toMove = worm;
								break;
							}
						}
						if(toMove == null) {
							List<EntityVillager> ents = world.getEntitiesWithinAABB(EntityVillager.class, bbox, e -> !e.isChild());
							if(ents.size() > redstone) {
								EntityDreamVillager vil = new EntityDreamVillager(world, this.pos, Collections.unmodifiableList(this.points));
								EntityVillager v = ents.get(0);
								NBTTagCompound nbt = new NBTTagCompound();
								v.writeEntityToNBT(nbt);
								vil.setData(nbt);
								vil.setPosition(v.posX, v.posY, v.posZ);
								vil.setProfession(v.getProfession());
								toMove = vil;
								world.spawnEntity(vil);
								world.removeEntity(v);
							}
						}
					}
				}
				this.counter = 0;
			}
			
		}
	}
	
	public void clearList() {
		this.points = new ArrayList<>();
		if(this.world != null && !this.world.isRemote) {
			markDirty();
			this.sendUpdates(world);
		}
	}
	
	public void addPoint(double x, double y, double z) {
		this.points.add(new Point3d(x, y, z));
	}
	
	public List<Point3d> getPoints() {
		return this.points;
	}
	
	public void finish() {
		markDirty();
		this.usingPlayer = null;
		this.sendUpdates(world);
	}
	
	public boolean setPlayer(EntityPlayer p) {
		if(this.usingPlayer == null) {
			this.usingPlayer = p.getPersistentID();
			return true;
		}
		return false;
	}
	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound) {
		for(int i = 0; i < this.points.size(); i++) {
			Point3d p = this.points.get(i);
			compound.setDouble(String.format("pointx%d", i), p.x);
			compound.setDouble(String.format("pointy%d", i), p.y);
			compound.setDouble(String.format("pointz%d", i), p.z);
		}
		compound.setInteger("type", this.type.ordinal());
		compound.setInteger("counter", this.counter);
		compound.setInteger("dye", this.dye.ordinal());
		compound.setBoolean("showPath", this.showPath);
		return super.writeToNBT(compound);
	}
	
	@Override
	public void readFromNBT(NBTTagCompound compound) {
		this.clearList();
		int i = 0;
		while(compound.hasKey(String.format("pointx%d", i))) {
			double x = compound.getDouble(String.format("pointx%d", i));
			double y = compound.getDouble(String.format("pointy%d", i));
			double z = compound.getDouble(String.format("pointz%d", i));
			this.addPoint(x, y, z);
			i++;
		}
		this.type = FocusType.values()[compound.getInteger("type")];
		this.counter = compound.getInteger("counter");
		this.dye = EnumDyeColor.values()[compound.getInteger("dye")];
		this.showPath = compound.getBoolean("showPath");
		super.readFromNBT(compound);
	}
	
	@Nullable
	@Override
	public SPacketUpdateTileEntity getUpdatePacket() {
		return new SPacketUpdateTileEntity(this.pos, 3, this.getUpdateTag());
	}
	
	@Override
	public NBTTagCompound getUpdateTag() {
		return this.writeToNBT(new NBTTagCompound());
	}
	
	@Override
	public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt) {
		super.onDataPacket(net, pkt);
		handleUpdateTag(pkt.getNbtCompound());
	}
	
	private void sendUpdates(World worldObj) {
		world.markBlockRangeForRenderUpdate(pos, pos);
		world.notifyBlockUpdate(pos, getState(), getState(), 3);
		world.scheduleBlockUpdate(pos,this.getBlockType(),0,0);
	}
	
	private IBlockState getState() {
		return world.getBlockState(pos);
	}
	
	public enum FocusType {
		ITEM, FLUID, VILLAGER;
	}

}
