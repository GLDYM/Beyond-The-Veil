package com.valeriotor.BTV.worship;

import java.util.HashMap;
import java.util.Map.Entry;

import com.valeriotor.BTV.capabilities.IPlayerData;
import com.valeriotor.BTV.capabilities.PlayerDataProvider;
import com.valeriotor.BTV.lib.PlayerDataLib;
import com.valeriotor.BTV.network.BTVPacketHandler;
import com.valeriotor.BTV.network.MessageSyncDataToClient;
import com.valeriotor.BTV.util.SyncUtil;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import thaumcraft.api.ThaumcraftApi;
import thaumcraft.api.capabilities.IPlayerKnowledge;
import thaumcraft.api.capabilities.ThaumcraftCapabilities;

import static com.valeriotor.BTV.lib.PlayerDataLib.*;

public class DGWorshipHelper {
	
	public static final int MAX_LEVEL = 5; // Will of course change
	
	public static final HashMap<String, DGResearch> researches = new HashMap<>(); 
	private static final HashMap<EntityPlayer, Double> defenseMultipliers = new HashMap<>();
	private static final HashMap<EntityPlayer, Double> attackMultipliers = new HashMap<>();
	private static final HashMap<EntityPlayer, Integer> dreamPower = new HashMap<>();
	
	public static void loadDreamerResearch() {
		researches.put(SLUGS, new DGResearch("SLUGS", 0, 0, 30, true, 0));
		researches.put(FISHQUEST, new DGResearch("CANOE", 0.5, -0.05, 30, false, 1));
		researches.put(RITUALQUEST, new DGResearch("BAPTISM", 0.1, -0.05, 0, true, 1));
	}
	
	public static void levelUp(EntityPlayer p) {
		IPlayerData data = p.getCapability(PlayerDataProvider.PLAYERDATA, null);
		int slugs = data.getOrSetInteger(PlayerDataLib.SLUGS, 0, false);
		IPlayerKnowledge k = ThaumcraftCapabilities.getKnowledge(p);
		for(Entry<String, DGResearch> entry : researches.entrySet()) {
			DGResearch res = entry.getValue();
			if((data.getString(entry.getKey()) || entry.getKey().equals(SLUGS)) && res.canBeUnlocked(k) && !res.isRequirementUnlocked(k)) {
				if(slugs >= res.getRequiredSlugs()) {
					res.unlock(p);
					slugs -= res.getRequiredSlugs();
					data.removeString(entry.getKey());
				}
			}
		}
		data.setInteger(PlayerDataLib.SLUGS, slugs, false);
		calculateModifier(p, k);
		SyncUtil.syncCapabilityData(p);
		
	}
	
	public static int getRequiredSlugs(int lvl) {
		return (int) Math.ceil(90*(1 - Math.pow(1.5, -lvl)));
	}
	
	public static boolean hasRequiredQuest(EntityPlayer p, int lvl) {
		if(lvl >= MAX_LEVEL) return false;
		return p.getCapability(PlayerDataProvider.PLAYERDATA, null).getString(getRequiredQuest(lvl));
	}
	
	public static String getRequiredQuest(int lvl) {
		switch(lvl) {
			case 2: return FISHQUEST;
			case 3: return RITUALQUEST;
			default: return null;
		}
	}
	
	public static boolean canTransform(EntityPlayer p) {
		return true; // will later use research to track progress
	}
	
	public static void calculateModifier(EntityPlayer p, IPlayerKnowledge k) {
		double attack = 1;
		double defense = 1;
		int dream = 1;
		for(Entry<String, DGResearch> entry : researches.entrySet()) {
			if(entry.getValue().isRequirementUnlocked(k)) {
				attack += entry.getValue().attackIncrement;
				defense += entry.getValue().defenseIncrement;
				if(entry.getValue().improvesDreams) dream++;
			}
		}
		attackMultipliers.put(p, attack);
		defenseMultipliers.put(p, defense);			
		dreamPower.put(p, dream);
	}
	
	public static double getAttackModifier(EntityPlayer p) {
		if(!attackMultipliers.containsKey(p))calculateModifier(p, ThaumcraftCapabilities.getKnowledge(p));
		return attackMultipliers.get(p);
	}
	
	public static double getDefenseModifier(EntityPlayer p) {
		if(!defenseMultipliers.containsKey(p))calculateModifier(p, ThaumcraftCapabilities.getKnowledge(p));
		return defenseMultipliers.get(p);
	}
	
	public static class DGResearch {
		private final String key;
		public final double attackIncrement;
		public final double defenseIncrement;
		private final int requiredSlugs;
		public final boolean improvesDreams;
		private final int stage;
		
		public DGResearch(String key, double attackIncrement, double defenseIncrement, int requiredSlugs, boolean improvesDreams, int stage) {
			this.key = key;
			this.attackIncrement = attackIncrement;
			this.defenseIncrement = defenseIncrement;
			this.requiredSlugs = requiredSlugs;
			this.improvesDreams = improvesDreams;
			this.stage = stage;
		}
		
		public boolean canBeUnlocked(IPlayerKnowledge k) {
			return k.isResearchKnown(key.concat(stage == 0 ? "" : "@".concat(String.valueOf(stage)))) && !k.isResearchComplete("!".concat(key));
		}
		
		public boolean isUnlocked(IPlayerKnowledge k) {
			return k.isResearchComplete(key);
		}
		
		public boolean isRequirementUnlocked(IPlayerKnowledge k) {
			return k.isResearchComplete("!".concat(key));
		}
		
		public int getRequiredSlugs() {
			return this.requiredSlugs;
		}
		
		public void unlock(EntityPlayer p) {
			ThaumcraftApi.internalMethods.progressResearch(p, "!".concat(key));
		}
		
	}
	
	// Remove old keys from pData
}
