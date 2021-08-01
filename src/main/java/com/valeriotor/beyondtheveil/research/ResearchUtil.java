package com.valeriotor.beyondtheveil.research;

import java.util.HashMap;
import java.util.Map.Entry;

import com.valeriotor.beyondtheveil.capabilities.IPlayerData;
import com.valeriotor.beyondtheveil.capabilities.PlayerDataProvider;
import com.valeriotor.beyondtheveil.capabilities.ResearchProvider;
import com.valeriotor.beyondtheveil.dreaming.Memory;
import com.valeriotor.beyondtheveil.network.BTVPacketHandler;
import com.valeriotor.beyondtheveil.network.research.MessageSyncResearchToClient;
import com.valeriotor.beyondtheveil.network.research.MessageSyncResearchToServer;
import com.valeriotor.beyondtheveil.network.research.ResearchSyncer;
import com.valeriotor.beyondtheveil.util.SyncUtil;

import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ResearchUtil {
	
	public static void progressResearchServer(EntityPlayer p, String key) {
		p.getCapability(ResearchProvider.RESEARCH, null).getResearch(key).progressStage(p);
		BTVPacketHandler.INSTANCE.sendTo(new MessageSyncResearchToClient(new ResearchSyncer(key).setProgress(true)), (EntityPlayerMP)p);
	}
	
	@SideOnly(Side.CLIENT)
	public static void progressResearchClient(EntityPlayer p, String key) {
		progressResearch(p, key);
		BTVPacketHandler.INSTANCE.sendToServer(new MessageSyncResearchToServer(new ResearchSyncer(key).setProgress(true)));
	}
	
	public static void progressResearch(EntityPlayer p, String key) {
		p.getCapability(ResearchProvider.RESEARCH, null).getResearch(key).progressStage(p);		
	}

	public static void learnResearchServer(EntityPlayer p, String key) {
		learnResearch(p, key);
		BTVPacketHandler.INSTANCE.sendTo(new MessageSyncResearchToClient(new ResearchSyncer(key).setLearn(true)), (EntityPlayerMP)p);
	}

	@SideOnly(Side.CLIENT)
	public static void learnResearchClient(EntityPlayer p, String key) {
		learnResearch(p, key);
		BTVPacketHandler.INSTANCE.sendToServer(new MessageSyncResearchToServer(new ResearchSyncer(key).setLearn(true)));
	}

	public static void openUpdatedResearchClient(EntityPlayer player, String key) {
		setResearchUpdated(player, key, false);
		BTVPacketHandler.INSTANCE.sendToServer(new MessageSyncResearchToServer(new ResearchSyncer(key).setUpdate(false)));
	}

	public static void learnResearch(EntityPlayer p, String key) {
		p.getCapability(ResearchProvider.RESEARCH, null).getResearch(key).learn();
	}

	public static void setResearchUpdated(EntityPlayer p, String key, boolean updated) {
		p.getCapability(ResearchProvider.RESEARCH, null).getResearch(key).setUpdated(updated);
	}

	/** Server-side only
	 */
	public static boolean learn(EntityPlayer p) {
		HashMap<String, ResearchStatus> stati = p.getCapability(ResearchProvider.RESEARCH, null).getResearches();
		IPlayerData data = p.getCapability(PlayerDataProvider.PLAYERDATA, null);
		for(Entry<String, ResearchStatus> entry : stati.entrySet()) {
			if(entry.getValue().isLearnable(stati, data)) {
				ResearchUtil.learnResearchServer(p, entry.getKey());
				if(entry.getKey().equals("CRYSTALDREAMS"))
					Memory.METAL.unlock(p, false);
				return true;
			}
		}
		return false;
	}

	public static ResearchStatus getResearch(EntityPlayer p, String key) {
		if(!(p instanceof FakePlayer))
			return p.getCapability(ResearchProvider.RESEARCH, null).getResearch(key);
		return dummyStatus;
	}

	public static int getResearchStage(EntityPlayer p, String key) {
		ResearchStatus r = getResearch(p, key);
		if(r != null) return r.getStage();
		return -2;
	}

	public static boolean isResearchVisible(EntityPlayer p, String key) {
		return getResearch(p, key).isVisible(p);
	}

	public static boolean isResearchVisible(HashMap<String, ResearchStatus> map, IPlayerData data, String key) {
		return map.get(key).isVisible(map, data);
	}

	public static boolean isResearchComplete(EntityPlayer p, String key) {
		return getResearch(p, key).isComplete();
	}

	public static boolean isResearchKnown(EntityPlayer p, String key) {
		return getResearch(p, key).isKnown(p);
	}
	public static boolean isResearchOpened(EntityPlayer p, String key) {
		return getResearchStage(p, key) >= 0;
	}

	/*public static boolean knowsResearch(EntityPlayer p, String key) {
		return false;
	}*/

	public static void completeResearch(EntityPlayer p, String key) {
		HashMap<String, ResearchStatus> map = p.getCapability(ResearchProvider.RESEARCH, null).getResearches();
		if(map.containsKey(key)) {
			p.getCapability(ResearchProvider.RESEARCH, null).getResearch(key).complete(map, p.getCapability(PlayerDataProvider.PLAYERDATA, null));
			BTVPacketHandler.INSTANCE.sendTo(new MessageSyncResearchToClient(new ResearchSyncer(key).setComplete(true)), (EntityPlayerMP)p);
		} else
			p.sendMessage(new TextComponentString("Research key not found"));
	}

	public static void resetResearch(EntityPlayer p) {
		HashMap<String, ResearchStatus> map = p.getCapability(ResearchProvider.RESEARCH, null).getResearches();
		for(Entry<String, ResearchStatus> entry : map.entrySet()) {
			entry.getValue().unlearn();
			BTVPacketHandler.INSTANCE.sendTo(new MessageSyncResearchToClient(new ResearchSyncer(entry.getKey()).setUnlearn(true)), (EntityPlayerMP)p);
			SyncUtil.removeStringDataOnServer(p, entry.getKey());
		}
	}

	public static void printResearch(EntityPlayer p) {
		HashMap<String, ResearchStatus> map = p.getCapability(ResearchProvider.RESEARCH, null).getResearches();
		IPlayerData data = p.getCapability(PlayerDataProvider.PLAYERDATA, null);
		for(Entry<String, ResearchStatus> entry : map.entrySet()) {
			if(entry.getValue().isVisible(map, data)) {
				p.sendMessage(new TextComponentString(entry.getKey() + " Stage: " + entry.getValue().getStage()));
			}
		}
	}

	public static void markResearchAsUpdated(EntityPlayer p, String newDataString) {
		if (!p.world.isRemote) {
			HashMap<String, ResearchStatus> map = p.getCapability(ResearchProvider.RESEARCH, null).getResearches();
			IPlayerData data = p.getCapability(PlayerDataProvider.PLAYERDATA, null);
			for(Entry<String, ResearchStatus> entry : map.entrySet()) {
				ResearchStatus research = entry.getValue();
				if(research.isKnown(map, data) && research.getStage() > -1) {
					boolean flag = false;
					if (research.canProgressStage(data)) {
						for (String requirement: research.res.getStages()[research.getStage()].required_research) {
							if (requirement.equals(newDataString)) {
								flag = true;
								break;
							}
						}
					}
					if (!flag) {
						for (Research.SubResearch addenda : research.res.getAddenda()) {
							if (addenda.meetsRequirements(data)) {
								for (String s : addenda.getRequirements()) {
									if (s.equals(newDataString)) {
										flag = true;
										break;
									}
								}
							}
						}
					}
					if (flag) {
						research.setUpdated(true);
						BTVPacketHandler.INSTANCE.sendTo(new MessageSyncResearchToClient(new ResearchSyncer(entry.getKey()).setUpdate(true)), (EntityPlayerMP)p);
					}
				}
			}
		}
	}

	private static ResearchStatus dummyStatus = new ResearchStatus(ResearchRegistry.researches.get("CRYSTALDREAMS")) {
		@Override
		public boolean isComplete() {
			return false;
		}
		@Override
		public int getStage() {
			return -2;
		};
		@Override
		public boolean isKnown(EntityPlayer p) {
			return false;
		};
		@Override
		public boolean isVisible(EntityPlayer p) {
			return false;
		}

	};
}
