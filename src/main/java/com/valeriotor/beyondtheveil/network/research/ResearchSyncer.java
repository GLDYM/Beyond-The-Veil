package com.valeriotor.beyondtheveil.network.research;

import com.valeriotor.beyondtheveil.capabilities.ResearchProvider;
import com.valeriotor.beyondtheveil.research.Research;
import com.valeriotor.beyondtheveil.research.ResearchRegistry;
import com.valeriotor.beyondtheveil.research.ResearchStatus;
import com.valeriotor.beyondtheveil.research.ResearchUtil;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ResearchSyncer {

	public String key;
	public boolean learn = false;
	public boolean progress = false;
	public boolean complete = false;
	public boolean unlearn = false;
	private boolean updateMark = false;
	private boolean updateValue = false;
	public ResearchStatus status;

	public ResearchSyncer() {}

	public ResearchSyncer(String key) {
		this.key = key;
	}

	public ResearchSyncer setLearn(boolean set) {
		this.learn = set;
		return this;
	}

	public ResearchSyncer setProgress(boolean set) {
		this.progress = set;
		return this;
	}

	public ResearchSyncer setUnlearn(boolean set) {
		this.unlearn = set;
		return this;
	}

	public ResearchSyncer setComplete(boolean set) {
		this.complete = set;
		return this;
	}

	public ResearchSyncer setUpdate(boolean update) {
		this.updateMark = true;
		this.updateValue = update;
		return this;
	}

	public ResearchSyncer setStatus(ResearchStatus status) {
		this.status = status;
		return this;
	}

	public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
		nbt.setString("key", key);
		nbt.setBoolean("learn", learn);
		nbt.setBoolean("progress", progress);
		nbt.setBoolean("unlearn", unlearn);
		nbt.setBoolean("complete", complete);
		nbt.setBoolean("updateMark", updateMark);
		nbt.setBoolean("updateValue", updateValue);
		if(status != null) {
			nbt.setTag("res", this.status.writeToNBT(new NBTTagCompound()));
		}
		return nbt;
	}

	public ResearchSyncer readFromNBT(NBTTagCompound nbt) {
		this.key = nbt.getString("key");
		this.learn = nbt.getBoolean("learn");
		this.progress = nbt.getBoolean("progress");
		this.unlearn = nbt.getBoolean("unlearn");
		this.complete = nbt.getBoolean("complete");
		this.updateMark = nbt.getBoolean("updateMark");
		this.updateValue = nbt.getBoolean("updateValue");
		if(nbt.hasKey("res")) {
			this.status = new ResearchStatus(ResearchRegistry.researches.get(this.key)).readFromNBT((NBTTagCompound) nbt.getTag("res"));
		}
		return this;
	}

	@SideOnly(Side.CLIENT)
	public void processClient() {
		EntityPlayer p = Minecraft.getMinecraft().player;
		if(progress) ResearchUtil.progressResearch(p, key);
		if(learn) ResearchUtil.learnResearch(p, key);
		if(status != null) p.getCapability(ResearchProvider.RESEARCH, null).addResearchStatus(status);
		if(complete) ResearchUtil.getResearch(p, key).complete(p);
		if(unlearn) ResearchUtil.getResearch(p, key).unlearn();
		if(updateMark) ResearchUtil.setResearchUpdated(p, key, updateValue);
	}

	public void processServer(EntityPlayer p) {
		if(progress) ResearchUtil.progressResearch(p, key);
		if(learn) ResearchUtil.learnResearch(p, key);
		if(status != null) p.getCapability(ResearchProvider.RESEARCH, null).addResearchStatus(status);
		if(updateMark) ResearchUtil.setResearchUpdated(p, key, updateValue);
	}

}
