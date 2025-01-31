package com.valeriotor.beyondtheveil.entity;

import com.google.common.collect.Sets;
import com.mojang.logging.LogUtils;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.Dynamic;
import com.valeriotor.beyondtheveil.Registration;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.Tag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.PanicGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
import net.minecraft.world.entity.npc.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraft.world.item.trading.MerchantOffers;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.slf4j.Logger;

import javax.annotation.Nullable;
import java.util.Set;

public class CrawlerEntity extends PathfinderMob implements VillagerDataHolder {
    private static final Logger LOGGER = LogUtils.getLogger();
    private static final EntityDataAccessor<VillagerData> DATA_VILLAGER_DATA = SynchedEntityData.defineId(CrawlerEntity.class, EntityDataSerializers.VILLAGER_DATA);
    private static final EntityDataAccessor<Boolean> DATA_CRAWLING = SynchedEntityData.defineId(CrawlerEntity.class, EntityDataSerializers.BOOLEAN);
    private int startCrawl = -20;
    @Nullable
    private Tag gossips;
    @Nullable
    private CompoundTag tradeOffers;
    private int villagerXp;


    public CrawlerEntity(EntityType<? extends PathfinderMob> p_21683_, Level p_21684_) {
        super(p_21683_, p_21684_);
        this.moveControl = new CrawlerMoveControl(this);
    }


    public void setData(Villager source) {
        setVillagerData(source.getVillagerData());
        setGossips(source.getGossips().store(NbtOps.INSTANCE).getValue());
        setTradeOffers(source.getOffers().createTag());
        setVillagerXp(source.getVillagerXp());
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(8, new RandomLookAroundGoal(this));
        this.goalSelector.addGoal(8, new LookAtPlayerGoal(this, Player.class, 12));
        this.goalSelector.addGoal(8, new RandomStrollGoal(this, 1.0D));
        this.goalSelector.addGoal(8, new PanicGoal(this, 1.0D));
    }

    public static AttributeSupplier.Builder prepareAttributes() {
        return LivingEntity.createLivingAttributes()
                .add(Attributes.MAX_HEALTH, 20)
                .add(Attributes.MOVEMENT_SPEED, 0.25D)
                .add(Attributes.KNOCKBACK_RESISTANCE, 0.5D)
                .add(Attributes.FOLLOW_RANGE, 64.0D)
                .add(Attributes.ATTACK_DAMAGE, 18.0D);
    }

    @Override
    public InteractionResult interactAt(Player pPlayer, Vec3 pVec, InteractionHand pHand) {
        if (pPlayer.isShiftKeyDown() && pPlayer.getItemInHand(pHand).isEmpty() && pHand == InteractionHand.MAIN_HAND) {
            if(!level.isClientSide) {
                ItemStack heldVillager = new ItemStack(Registration.HELD_VILLAGER.get());
                CompoundTag tag = new CompoundTag();
                addAdditionalSaveData(tag);
                heldVillager.getOrCreateTag().put("data", tag);
                pPlayer.setItemSlot(EquipmentSlot.MAINHAND, heldVillager);
                discard();
            }
            return InteractionResult.SUCCESS;
        }
        return super.interactAt(pPlayer, pVec, pHand);
    }

    @Override
    public VillagerData getVillagerData() {
        return this.entityData.get(DATA_VILLAGER_DATA);
    }


    @Override
    public void addAdditionalSaveData(CompoundTag pCompound) {
        super.addAdditionalSaveData(pCompound);
        VillagerData.CODEC.encodeStart(NbtOps.INSTANCE, this.getVillagerData()).resultOrPartial(LOGGER::error).ifPresent((p_204072_) -> {
            pCompound.put("VillagerData", p_204072_);
        });
        if (this.tradeOffers != null) {
            pCompound.put("Offers", this.tradeOffers);
        }

        if (this.gossips != null) {
            pCompound.put("Gossips", this.gossips);
        }

        pCompound.putInt("Xp", this.villagerXp);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag pCompound) {
        super.readAdditionalSaveData(pCompound);
        if (pCompound.contains("VillagerData", 10)) {
            DataResult<VillagerData> dataresult = VillagerData.CODEC.parse(new Dynamic<>(NbtOps.INSTANCE, pCompound.get("VillagerData")));
            dataresult.resultOrPartial(LOGGER::error).ifPresent(this::setVillagerData);
        }

        if (pCompound.contains("Offers", 10)) {
            this.tradeOffers = pCompound.getCompound("Offers");
        }

        if (pCompound.contains("Gossips", 10)) {
            this.gossips = pCompound.getList("Gossips", 10);
        }

        if (pCompound.contains("Xp", 3)) {
            this.villagerXp = pCompound.getInt("Xp");
        }

    }



    protected void addOffersFromItemListings(MerchantOffers pGivenMerchantOffers, VillagerTrades.ItemListing[] pNewTrades, int pMaxNumbers) {
        Set<Integer> set = Sets.newHashSet();
        if (pNewTrades.length > pMaxNumbers) {
            while(set.size() < pMaxNumbers) {
                set.add(this.random.nextInt(pNewTrades.length));
            }
        } else {
            for(int i = 0; i < pNewTrades.length; ++i) {
                set.add(i);
            }
        }

        for(Integer integer : set) {
            VillagerTrades.ItemListing villagertrades$itemlisting = pNewTrades[integer];
            MerchantOffer merchantoffer = villagertrades$itemlisting.getOffer(this, this.random);
            if (merchantoffer != null) {
                pGivenMerchantOffers.add(merchantoffer);
            }
        }

    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(DATA_VILLAGER_DATA, new VillagerData(VillagerType.PLAINS, VillagerProfession.NONE, 1));
        this.entityData.define(DATA_CRAWLING, false);
    }

    public int getCrawling() {
        return 20 - (tickCount - startCrawl);
    }

    public void setGossips(Tag pGossips) {
        this.gossips = pGossips;
    }

    public void setTradeOffers(CompoundTag pTradeOffers) {
        this.tradeOffers = pTradeOffers;
    }
    public void setVillagerData(VillagerData p_34376_) {
        VillagerData villagerdata = this.getVillagerData();
        if (villagerdata.getProfession() != p_34376_.getProfession()) {
            this.tradeOffers = null;
        }

        this.entityData.set(DATA_VILLAGER_DATA, p_34376_);
    }
    public void setVillagerXp(int pVillagerXp) {
        this.villagerXp = pVillagerXp;
    }

    // Animation sync code became a bit convoluted as I converged on the right technique. Don't look too much into it
    @Override
    public void tick() {
        super.tick();
        if (level.isClientSide) {
            boolean crawl = entityData.get(DATA_CRAWLING);
            if (crawl && getCrawling() < 1) {
                startCrawl = tickCount;
            }
        }
    }

    private static class CrawlerMoveControl extends MoveControl {
        public CrawlerMoveControl(CrawlerEntity pMob) {
            super(pMob);
        }

        @Override
        public void tick() {
            super.tick();
            if (mob.level.isClientSide) {
                return;
            }
            CrawlerEntity mob = (CrawlerEntity) this.mob;
            if(mob.getSpeed() == 0) {
                mob.getEntityData().set(DATA_CRAWLING, false);
                mob.startCrawl = -20;
            } else {
                if (mob.getCrawling() <= 0) {
                    mob.entityData.set(DATA_CRAWLING, true);
                    mob.startCrawl = mob.tickCount;
                } else if (mob.getCrawling() < 10) {
                    mob.setSpeed(0);
                }
            }
        }
    }

}
