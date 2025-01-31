package com.valeriotor.beyondtheveil.client.render.blockentity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Quaternion;
import com.valeriotor.beyondtheveil.tile.MemorySieveBE;
import com.valeriotor.beyondtheveil.tile.WateryCradleBE;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;

public class WateryCradleBER implements BlockEntityRenderer<WateryCradleBE> {

    public WateryCradleBER(BlockEntityRendererProvider.Context context) {
    }

    @Override
    public void render(WateryCradleBE pBlockEntity, float pPartialTick, PoseStack pPoseStack, MultiBufferSource pBufferSource, int pPackedLight, int pPackedOverlay) {
        if (pBlockEntity.getEntity() != null) {
            pPoseStack.pushPose();
            pPoseStack.scale(0.85F, 0.8F, 0.85F);
            //pPoseStack.mulPose(Quaternion.fromYXZ((float) Math.PI/2, 0, 0));
            Minecraft.getInstance().getEntityRenderDispatcher().render(pBlockEntity.getEntity(), 0.55, 0.28, 0.5, 0, pPartialTick, pPoseStack, pBufferSource, pPackedLight);
            pPoseStack.popPose();
        }
    }

}
