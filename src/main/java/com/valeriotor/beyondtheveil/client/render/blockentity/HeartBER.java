package com.valeriotor.beyondtheveil.client.render.blockentity;

import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Matrix3f;
import com.mojang.math.Matrix4f;
import com.valeriotor.beyondtheveil.client.MiscModels;
import com.valeriotor.beyondtheveil.tile.HeartBE;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.resources.ResourceLocation;

public class HeartBER implements BlockEntityRenderer<HeartBE>{

    private static final float HEIGHT = 0.0625F * 2;
    private final TextureAtlasSprite sprite = Minecraft.getInstance().getTextureAtlas(new ResourceLocation("minecraft","textures/atlas/blocks.png")).apply(new ResourceLocation("beyondtheveil:block/heart"));

    public HeartBER(BlockEntityRendererProvider.Context context) {
    }

    @Override
    public void render(HeartBE heart, float partialTicks, PoseStack pPoseStack, MultiBufferSource buffers, int pPackedLight, int overlay) {
        int counter = heart.getCounter();

        float radius = 0.05F + 0.15625F / 120 * (Math.abs(counter - 15 + partialTicks) + 3.5F);

        pPoseStack.pushPose();
        Matrix4f model = pPoseStack.last().pose();
        Matrix3f normal = pPoseStack.last().normal();

        VertexConsumer buffer = buffers.getBuffer(Sheets.solidBlockSheet());
        buffer.vertex(model,0.4675F + radius, HEIGHT + radius, 0.5325F + radius).color(1, 0,0, 1F).uv(sprite.getU(0), sprite.getV(0)).overlayCoords(overlay).uv2(0xFFFFFF).normal(1,0,0)./*lightmap(upLMa, upLMb).*/endVertex();
        buffer.vertex(model,0.4675F + radius, HEIGHT + radius, 0.5325F - radius).color(1, 0,0, 1F).uv(sprite.getU(3), sprite.getV(0)).overlayCoords(overlay).uv2(0xFFFFFF).normal(1,0,0)./*lightmap(upLMa, upLMb).*/endVertex();
        buffer.vertex(model,0.4675F + radius, HEIGHT - radius, 0.5325F - radius).color(1, 0,0, 1F).uv(sprite.getU(3), sprite.getV(4)).overlayCoords(overlay).uv2(0xFFFFFF).normal(1,0,0)./*lightmap(upLMa, upLMb).*/endVertex();
        buffer.vertex(model,0.4675F + radius, HEIGHT - radius, 0.5325F + radius).color(1, 0,0, 1F).uv(sprite.getU(0), sprite.getV(4)).overlayCoords(overlay).uv2(0xFFFFFF).normal(1,0,0)./*lightmap(upLMa, upLMb).*/endVertex();

        buffer.vertex(model,0.4675F - radius, HEIGHT + radius, 0.5325F + radius).color(1, 0,0, 1F).uv(sprite.getU(0), sprite.getV(0)).overlayCoords(overlay).uv2(0xFFFFFF).normal(1,0,0)./*lightmap(upLMa, upLMb).*/endVertex();
        buffer.vertex(model,0.4675F - radius, HEIGHT + radius, 0.5325F - radius).color(1, 0,0, 1F).uv(sprite.getU(3), sprite.getV(0)).overlayCoords(overlay).uv2(0xFFFFFF).normal(1,0,0)./*lightmap(upLMa, upLMb).*/endVertex();
        buffer.vertex(model,0.4675F - radius, HEIGHT - radius, 0.5325F - radius).color(1, 0,0, 1F).uv(sprite.getU(3), sprite.getV(4)).overlayCoords(overlay).uv2(0xFFFFFF).normal(1,0,0)./*lightmap(upLMa, upLMb).*/endVertex();
        buffer.vertex(model,0.4675F - radius, HEIGHT - radius, 0.5325F + radius).color(1, 0,0, 1F).uv(sprite.getU(0), sprite.getV(4)).overlayCoords(overlay).uv2(0xFFFFFF).normal(1,0,0)./*lightmap(upLMa, upLMb).*/endVertex();

        buffer.vertex(model,0.4675F + radius, HEIGHT + radius, 0.5325F + radius).color(1, 0,0, 1F).uv(sprite.getU(0), sprite.getV(0)).overlayCoords(overlay).uv2(0xFFFFFF).normal(1,0,0)./*lightmap(upLMa, upLMb).*/endVertex();
        buffer.vertex(model,0.4675F + radius, HEIGHT + radius, 0.5325F - radius).color(1, 0,0, 1F).uv(sprite.getU(3), sprite.getV(0)).overlayCoords(overlay).uv2(0xFFFFFF).normal(1,0,0)./*lightmap(upLMa, upLMb).*/endVertex();
        buffer.vertex(model,0.4675F - radius, HEIGHT + radius, 0.5325F - radius).color(1, 0,0, 1F).uv(sprite.getU(3), sprite.getV(4)).overlayCoords(overlay).uv2(0xFFFFFF).normal(1,0,0)./*lightmap(upLMa, upLMb).*/endVertex();
        buffer.vertex(model,0.4675F - radius, HEIGHT + radius, 0.5325F + radius).color(1, 0,0, 1F).uv(sprite.getU(0), sprite.getV(4)).overlayCoords(overlay).uv2(0xFFFFFF).normal(1,0,0)./*lightmap(upLMa, upLMb).*/endVertex();

        buffer.vertex(model,0.4675F + radius, HEIGHT - radius, 0.5325F + radius).color(1, 0,0, 1F).uv(sprite.getU(0), sprite.getV(0)).overlayCoords(overlay).uv2(0xFFFFFF).normal(1,0,0)./*lightmap(upLMa, upLMb).*/endVertex();
        buffer.vertex(model,0.4675F + radius, HEIGHT - radius, 0.5325F - radius).color(1, 0,0, 1F).uv(sprite.getU(3), sprite.getV(0)).overlayCoords(overlay).uv2(0xFFFFFF).normal(1,0,0)./*lightmap(upLMa, upLMb).*/endVertex();
        buffer.vertex(model,0.4675F - radius, HEIGHT - radius, 0.5325F - radius).color(1, 0,0, 1F).uv(sprite.getU(3), sprite.getV(4)).overlayCoords(overlay).uv2(0xFFFFFF).normal(1,0,0)./*lightmap(upLMa, upLMb).*/endVertex();
        buffer.vertex(model,0.4675F - radius, HEIGHT - radius, 0.5325F + radius).color(1, 0,0, 1F).uv(sprite.getU(0), sprite.getV(4)).overlayCoords(overlay).uv2(0xFFFFFF).normal(1,0,0)./*lightmap(upLMa, upLMb).*/endVertex();

        buffer.vertex(model,0.4675F + radius, HEIGHT + radius, 0.5325F + radius).color(1, 0,0, 1F).uv(sprite.getU(0), sprite.getV(0)).overlayCoords(overlay).uv2(0xFFFFFF).normal(1,0,0)./*lightmap(upLMa, upLMb).*/endVertex();
        buffer.vertex(model,0.4675F - radius, HEIGHT + radius, 0.5325F + radius).color(1, 0,0, 1F).uv(sprite.getU(3), sprite.getV(0)).overlayCoords(overlay).uv2(0xFFFFFF).normal(1,0,0)./*lightmap(upLMa, upLMb).*/endVertex();
        buffer.vertex(model,0.4675F - radius, HEIGHT - radius, 0.5325F + radius).color(1, 0,0, 1F).uv(sprite.getU(3), sprite.getV(4)).overlayCoords(overlay).uv2(0xFFFFFF).normal(1,0,0)./*lightmap(upLMa, upLMb).*/endVertex();
        buffer.vertex(model,0.4675F + radius, HEIGHT - radius, 0.5325F + radius).color(1, 0,0, 1F).uv(sprite.getU(0), sprite.getV(4)).overlayCoords(overlay).uv2(0xFFFFFF).normal(1,0,0)./*lightmap(upLMa, upLMb).*/endVertex();

        buffer.vertex(model,0.4675F + radius, HEIGHT + radius, 0.5325F - radius).color(1, 0,0, 1F).uv(sprite.getU(0), sprite.getV(0)).overlayCoords(overlay).uv2(0xFFFFFF).normal(1,0,0)./*lightmap(upLMa, upLMb).*/endVertex();
        buffer.vertex(model,0.4675F - radius, HEIGHT + radius, 0.5325F - radius).color(1, 0,0, 1F).uv(sprite.getU(3), sprite.getV(0)).overlayCoords(overlay).uv2(0xFFFFFF).normal(1,0,0)./*lightmap(upLMa, upLMb).*/endVertex();
        buffer.vertex(model,0.4675F - radius, HEIGHT - radius, 0.5325F - radius).color(1, 0,0, 1F).uv(sprite.getU(3), sprite.getV(4)).overlayCoords(overlay).uv2(0xFFFFFF).normal(1,0,0)./*lightmap(upLMa, upLMb).*/endVertex();
        buffer.vertex(model,0.4675F + radius, HEIGHT - radius, 0.5325F - radius).color(1, 0,0, 1F).uv(sprite.getU(0), sprite.getV(4)).overlayCoords(overlay).uv2(0xFFFFFF).normal(1,0,0)./*lightmap(upLMa, upLMb).*/endVertex();

        pPoseStack.popPose();

    }
}
