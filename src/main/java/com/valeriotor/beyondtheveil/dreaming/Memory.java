package com.valeriotor.beyondtheveil.dreaming;

import java.util.Map.Entry;

import com.valeriotor.beyondtheveil.blocks.BlockRegistry;
import com.valeriotor.beyondtheveil.capabilities.IPlayerData;
import com.valeriotor.beyondtheveil.capabilities.PlayerDataProvider;
import com.valeriotor.beyondtheveil.capabilities.ResearchProvider;
import com.valeriotor.beyondtheveil.network.BTVPacketHandler;
import com.valeriotor.beyondtheveil.network.generic.GenericMessageKey;
import com.valeriotor.beyondtheveil.network.generic.MessageGenericToClient;
import com.valeriotor.beyondtheveil.research.Research.SubResearch;
import com.valeriotor.beyondtheveil.research.ResearchStatus;
import com.valeriotor.beyondtheveil.util.SyncUtil;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextComponentTranslation;

public enum Memory {
    ANIMAL(Items.LEATHER, 0xFF401b00),
    BEHEADING(Items.SKULL, 0xFF333333, 1),
    CHANGE(Items.WHEAT_SEEDS, 0xFF4dff00, "metalDream"),
    CRYSTAL(Item.getItemFromBlock(Blocks.GLASS), 0xFFe6d8d8),
    DARKNESS(Items.COAL, 0xFF002233),
    DEATH(Items.ROTTEN_FLESH, 0xFF2a2a2c),
    ELDRITCH(Items.ENDER_EYE, 0xFF400021),
    HEARTBREAK(Item.getItemFromBlock(BlockRegistry.BlockHeart), 0xFFAA0000, "memPOWER"),
    HUMAN(Items.ARMOR_STAND, 0xFFFFFFFF, "metalDream"),
    INTROSPECTION(Items.PAPER, 0xFFFFFFFF),
    LEARNING(Items.GHAST_TEAR, 0xFF998b69),
    METAL(Items.IRON_INGOT, 0xFF8c8c8c),
    PLANT(Item.getItemFromBlock(Blocks.SAPLING), 0xFF00FF00, 3),
    POWER(Items.BLAZE_POWDER, 0xFFff9300, "metalDream"),
    REPAIR(Item.getItemFromBlock(Blocks.ANVIL), 0xFF99f19d),
    SENTIENCE(Items.BOOK, 0xFFd87474, "metalDream"),
    STILLNESS(Item.getItemFromBlock(Blocks.SOUL_SAND), 0xFF444444, "metalDream"),
    TOOL(Items.WOODEN_PICKAXE, 0xFF324eAA, "memHUMAN"),
    VOID(Item.getItemFromBlock(Blocks.OBSIDIAN), 0xFF36111F),
    WATER(Items.WATER_BUCKET, 0xFF1111FF);

    private ItemStack item;
    private int color;
    private String[] reqs;

    private Memory(Item item, int color, String... reqs) {
        this.item = new ItemStack(item, 1, 0);
        this.color = color;
        this.reqs = reqs;
    }

    private Memory(Item item, int color, int meta) {
        this.item = new ItemStack(item, 1, meta);
        this.color = color;
        this.reqs = null;
    }

    public String getDataName() {
        return "mem".concat(this.name());
    }

    public boolean isUnlockable(EntityPlayer p) {
        if (reqs == null || reqs.length == 0) return true;
        IPlayerData data = p.getCapability(PlayerDataProvider.PLAYERDATA, null);
        for (String s : reqs) {
            if (!data.getString(s)) return false;
        }
        return true;
    }

    public boolean isUnlocked(EntityPlayer p) {
        return p.getCapability(PlayerDataProvider.PLAYERDATA, null).getString(this.getDataName());
    }

    public void unlock(EntityPlayer p) {
        this.unlock(p, true);
    }

    public void unlock(EntityPlayer p, boolean sendMessage) {
        if (!this.isUnlocked(p) && this.isUnlockable(p)) {
            String dataName = this.getDataName();
            SyncUtil.addStringDataOnServer(p, false, dataName);
            String s = getFurtherData(this);
            if (s != null) SyncUtil.addStringDataOnServer(p, false, s);
            if (sendMessage) {
                BTVPacketHandler.INSTANCE.sendTo(new MessageGenericToClient(GenericMessageKey.MEMORY_ENTRY, getDataName()), (EntityPlayerMP) p);
                p.sendMessage(new TextComponentTranslation("memory.unlock.message", this.getLocalizedName()));
                for (Entry<String, ResearchStatus> entry : p.getCapability(ResearchProvider.RESEARCH, null).getResearches().entrySet()) {
                    for (SubResearch addendum : entry.getValue().res.getAddenda()) {
                        for (String req : addendum.getRequirements()) {
                            if (req.equals(dataName)) {
                                p.sendMessage(new TextComponentTranslation("memory.unlock.addenda", new TextComponentTranslation(entry.getValue().res.getName()).getFormattedText()));
                                return;
                            }
                        }
                    }
                }
            }
        }
    }

    public int getColor() {
        return this.color;
    }

    /**
     * DO NOT MODIFY THIS STACK.
     */
    public ItemStack getItem() {
        return this.item;
    }

    public String getLocalizedKey() {
        return "memory.".concat(this.name().toLowerCase().concat(".name"));
    }

    public String getLocalizedName() {
        return new TextComponentTranslation(getLocalizedKey()).getFormattedText();
    }

    public static Memory getMemoryFromDataName(String key) {
        for (Memory m : Memory.values()) {
            if (key.equals(m.getDataName()))
                return m;
        }
        return null;
    }

    public static String getFurtherData(Memory m) {
        switch (m) {
            case ANIMAL:
                return null;
            case CHANGE:
                return "effectDream";
            case CRYSTAL:
                return null;
            case DARKNESS:
                return null;
            case DEATH:
                return null;
            case ELDRITCH:
                return null;
            case HUMAN:
                return null;
            case LEARNING:
                return null;
            case METAL:
                return null;
            case POWER:
                return "effectDream";
            case REPAIR:
                return null;
            case SENTIENCE:
                return null;
            case STILLNESS:
                return "effectDream";
            case TOOL:
                return null;
            case VOID:
                return null;
            default:
                return null;

        }
    }

}
