package com.valeriotor.beyondtheveil.entities.render;

import com.valeriotor.beyondtheveil.entities.bosses.EntityDeepOneMyrmidon;
import com.valeriotor.beyondtheveil.entities.models.ModelRegistry;
import com.valeriotor.beyondtheveil.lib.References;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;

public class RenderDeepOneMyrmidon extends RenderLiving<EntityDeepOneMyrmidon> {

    public RenderDeepOneMyrmidon(RenderManager rendermanagerIn) {
        super(rendermanagerIn, ModelRegistry.deepOneMyrmidon, 0.5F);
    }

    private static final ResourceLocation TEXTURE = new ResourceLocation(References.MODID, "textures/entity/deep_one_myrmidon.png");

    @Override
    protected ResourceLocation getEntityTexture(EntityDeepOneMyrmidon entity) {
        return TEXTURE;
    }
}
