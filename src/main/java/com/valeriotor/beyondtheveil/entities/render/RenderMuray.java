package com.valeriotor.beyondtheveil.entities.render;

import com.valeriotor.beyondtheveil.entities.EntityDreadfish;
import com.valeriotor.beyondtheveil.entities.EntityMuray;
import com.valeriotor.beyondtheveil.entities.models.ModelRegistry;
import com.valeriotor.beyondtheveil.lib.References;

import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;

public class RenderMuray extends RenderLiving<EntityMuray>{

	public RenderMuray(RenderManager rendermanagerIn) {
		super(rendermanagerIn, ModelRegistry.muray, 0.5F);
	}
	private static final ResourceLocation texture = new ResourceLocation(References.MODID, "textures/entity/muray.png");
	
	@Override
	protected ResourceLocation getEntityTexture(EntityMuray entity) {
		return texture;
	}

}
