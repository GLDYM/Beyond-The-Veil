package com.valeriotor.beyondtheveil.client.model.entity;// Made with Blockbench 4.6.5
// Exported for Minecraft version 1.17 or later with Mojang mappings
// Paste this class into your mod and generate all required imports


import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.valeriotor.beyondtheveil.lib.References;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;

public class BloodSkeletonModel extends EntityModel<LivingEntity> {
	// This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation(References.MODID, "blood_skeleton"), "main");
	private final ModelPart body;
	private final ModelPart legs;

	public BloodSkeletonModel(ModelPart root) {
		this.body = root.getChild("body");
		this.legs = root.getChild("legs");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition body = partdefinition.addOrReplaceChild("body", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, 4.0F, 14.0F, 1.2217F, 0.0F, 0.0F));

		PartDefinition spine = body.addOrReplaceChild("spine", CubeListBuilder.create().texOffs(0, 0).addBox(-1.0F, -23.0F, -1.0F, 2.0F, 23.0F, 2.0F, new CubeDeformation(0.0F))
				.texOffs(48, 59).addBox(-1.5F, -21.0F, -1.5F, 3.0F, 2.0F, 3.0F, new CubeDeformation(0.0F))
				.texOffs(52, 55).addBox(-1.5F, -17.0F, -1.5F, 3.0F, 2.0F, 3.0F, new CubeDeformation(0.0F))
				.texOffs(47, 59).addBox(-1.5F, -13.0F, -1.5F, 3.0F, 2.0F, 3.0F, new CubeDeformation(0.0F))
				.texOffs(49, 59).addBox(-1.5F, -9.0F, -1.5F, 3.0F, 2.0F, 3.0F, new CubeDeformation(0.0F))
				.texOffs(48, 56).addBox(-1.5F, -6.0F, -1.5F, 3.0F, 2.0F, 3.0F, new CubeDeformation(0.0F))
				.texOffs(47, 57).addBox(-1.5F, -3.0F, -1.5F, 3.0F, 2.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -1.0F, 0.0F));

		PartDefinition spine_upper = spine.addOrReplaceChild("spine_upper", CubeListBuilder.create(), PartPose.offset(0.0F, -22.0F, 0.0F));

		PartDefinition cube_r1 = spine_upper.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(52, 58).addBox(-1.5F, -5.0F, -1.5F, 3.0F, 2.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.3054F, 0.0F, 0.0F));

		PartDefinition cube_r2 = spine_upper.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(0, 0).addBox(-0.9F, -5.0F, -3.0F, 2.0F, 12.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -7.0F, 0.0F, 0.2618F, 0.0F, 0.0F));

		PartDefinition head = spine_upper.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 44).addBox(-6.0F, -9.0F, -10.0F, 12.0F, 9.0F, 11.0F, new CubeDeformation(0.0F))
				.texOffs(48, 0).addBox(3.5F, -5.5F, -10.1F, 3.0F, 3.0F, 0.0F, new CubeDeformation(0.0F))
				.texOffs(48, 9).addBox(-6.5F, -5.5F, -10.1F, 3.0F, 3.0F, 0.0F, new CubeDeformation(0.0F))
				.texOffs(49, 4).addBox(-1.0F, -5.5F, -10.05F, 2.0F, 3.0F, 0.0F, new CubeDeformation(0.0F))
				.texOffs(45, 8).addBox(-3.0F, -1.5F, -10.1F, 6.0F, 2.0F, 0.0F, new CubeDeformation(0.0F))
				.texOffs(46, 3).addBox(-6.25F, -2.5F, -10.1F, 5.0F, 1.0F, 0.0F, new CubeDeformation(0.0F))
				.texOffs(46, 8).addBox(1.25F, -2.5F, -10.1F, 5.0F, 1.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -10.0F, -2.0F, -0.8727F, 0.0F, 0.0F));

		PartDefinition cube_r3 = head.addOrReplaceChild("cube_r3", CubeListBuilder.create().texOffs(36, 44).addBox(-3.0F, -2.0F, -6.0F, 6.0F, 2.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 2.0F, -4.0F, 0.5236F, 0.0F, 0.0F));

		PartDefinition cube_r4 = head.addOrReplaceChild("cube_r4", CubeListBuilder.create().texOffs(28, 0).addBox(-6.0F, 0.5F, -5.5F, 12.0F, 0.0F, 11.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -9.5F, -4.5F, -0.0873F, 0.0F, 0.0F));

		PartDefinition cube_r5 = head.addOrReplaceChild("cube_r5", CubeListBuilder.create().texOffs(39, -9).addBox(0.5F, -3.0F, -5.5F, 0.0F, 7.0F, 11.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-6.5F, -6.0F, -4.5F, -0.0611F, 0.0873F, 0.0F));

		PartDefinition cube_r6 = head.addOrReplaceChild("cube_r6", CubeListBuilder.create().texOffs(39, -7).addBox(-0.5F, -3.0F, -5.5F, 0.0F, 7.0F, 11.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(6.5F, -6.0F, -4.5F, -0.0611F, -0.0873F, 0.0F));

		PartDefinition cube_r7 = head.addOrReplaceChild("cube_r7", CubeListBuilder.create().texOffs(38, 0).addBox(-6.5F, -2.0F, 0.0F, 13.0F, 4.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -7.5F, -10.1F, -0.0175F, 0.0F, 0.0F));

		PartDefinition left_clavicle = spine.addOrReplaceChild("left_clavicle", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, -22.0F, 0.0F, 0.0F, 0.0F, 0.0873F));

		PartDefinition cube_r8 = left_clavicle.addOrReplaceChild("cube_r8", CubeListBuilder.create().texOffs(0, 0).addBox(-2.0F, -1.5F, -5.0F, 11.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.054F, -0.3892F, -0.1415F));

		PartDefinition cube_r9 = left_clavicle.addOrReplaceChild("cube_r9", CubeListBuilder.create().texOffs(0, 0).addBox(-1.0F, -3.0F, -0.25F, 11.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.1309F, 0.0F));

		PartDefinition left_arm = left_clavicle.addOrReplaceChild("left_arm", CubeListBuilder.create().texOffs(0, 0).addBox(-0.25F, -1.0F, -1.0F, 2.0F, 19.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(9.0F, -1.5F, -0.5F, -1.4835F, 0.0F, -0.2182F));

		PartDefinition left_lower_arm = left_arm.addOrReplaceChild("left_lower_arm", CubeListBuilder.create().texOffs(0, 0).addBox(-1.0F, -1.5F, -1.75F, 2.0F, 19.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.75F, 17.0F, 0.75F, -0.3927F, 0.0F, 0.0F));

		PartDefinition right_clavicle = spine.addOrReplaceChild("right_clavicle", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, -22.0F, 0.0F, 0.0F, 0.0F, -0.0873F));

		PartDefinition cube_r10 = right_clavicle.addOrReplaceChild("cube_r10", CubeListBuilder.create().texOffs(0, 0).addBox(-9.25F, -1.5F, -5.0F, 11.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.054F, 0.3892F, 0.1415F));

		PartDefinition cube_r11 = right_clavicle.addOrReplaceChild("cube_r11", CubeListBuilder.create().texOffs(0, 0).addBox(-10.0F, -3.0F, -0.5F, 11.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, -0.1309F, 0.0F));

		PartDefinition right_arm = right_clavicle.addOrReplaceChild("right_arm", CubeListBuilder.create().texOffs(0, 0).addBox(-1.0F, -1.0F, -1.0F, 2.0F, 19.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-9.0F, -1.5F, -0.5F, -1.4835F, 0.0F, 0.2182F));

		PartDefinition right_lower_arm = right_arm.addOrReplaceChild("right_lower_arm", CubeListBuilder.create(), PartPose.offsetAndRotation(0.25F, 17.0F, 0.0F, -0.6981F, 0.0F, 0.0F));

		PartDefinition cube_r12 = right_lower_arm.addOrReplaceChild("cube_r12", CubeListBuilder.create().texOffs(0, 0).addBox(-1.0F, -2.0F, -1.0F, 2.0F, 19.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.3054F, 0.0F, 0.0F));

		PartDefinition left_rib1 = spine.addOrReplaceChild("left_rib1", CubeListBuilder.create(), PartPose.offset(0.6566F, -18.0F, 0.4395F));

		PartDefinition cube_r13 = left_rib1.addOrReplaceChild("cube_r13", CubeListBuilder.create().texOffs(0, 0).addBox(-0.5F, -1.0F, 0.0F, 7.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.1566F, 0.0F, -0.1895F, 0.0F, 0.2618F, 0.0F));

		PartDefinition left_rib11 = left_rib1.addOrReplaceChild("left_rib11", CubeListBuilder.create(), PartPose.offset(6.0F, 0.0F, 0.0F));

		PartDefinition cube_r14 = left_rib11.addOrReplaceChild("cube_r14", CubeListBuilder.create().texOffs(0, 0).addBox(-1.0F, -0.75F, -7.0F, 2.0F, 2.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.48F, 0.0F));

		PartDefinition left_rib2 = spine.addOrReplaceChild("left_rib2", CubeListBuilder.create(), PartPose.offset(0.6566F, -14.0F, 0.4395F));

		PartDefinition cube_r15 = left_rib2.addOrReplaceChild("cube_r15", CubeListBuilder.create().texOffs(0, 0).addBox(-0.5F, -1.0F, 0.0F, 7.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.1566F, 0.0F, -0.1895F, 0.0F, 0.2618F, 0.0F));

		PartDefinition left_rib21 = left_rib2.addOrReplaceChild("left_rib21", CubeListBuilder.create(), PartPose.offset(6.0F, 0.0F, 0.0F));

		PartDefinition cube_r16 = left_rib21.addOrReplaceChild("cube_r16", CubeListBuilder.create().texOffs(0, 0).addBox(-1.0F, -0.75F, -7.0F, 2.0F, 2.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.48F, 0.0F));

		PartDefinition left_rib3 = spine.addOrReplaceChild("left_rib3", CubeListBuilder.create(), PartPose.offset(0.6566F, -10.0F, 0.4395F));

		PartDefinition cube_r17 = left_rib3.addOrReplaceChild("cube_r17", CubeListBuilder.create().texOffs(0, 0).addBox(-0.5F, -1.0F, 0.0F, 7.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.1566F, 0.0F, -0.1895F, 0.0F, 0.2618F, 0.0F));

		PartDefinition left_rib31 = left_rib3.addOrReplaceChild("left_rib31", CubeListBuilder.create(), PartPose.offset(6.0F, 0.0F, 0.0F));

		PartDefinition cube_r18 = left_rib31.addOrReplaceChild("cube_r18", CubeListBuilder.create().texOffs(0, 0).addBox(-1.0F, -0.75F, -7.0F, 2.0F, 2.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.48F, 0.0F));

		PartDefinition left_rib4 = spine.addOrReplaceChild("left_rib4", CubeListBuilder.create(), PartPose.offset(0.6566F, -6.75F, 0.4395F));

		PartDefinition cube_r19 = left_rib4.addOrReplaceChild("cube_r19", CubeListBuilder.create().texOffs(0, 0).addBox(-0.5F, 0.0F, 0.0F, 5.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.1566F, 0.0F, -0.1895F, 0.0F, 0.2618F, 0.0F));

		PartDefinition left_rib41 = left_rib4.addOrReplaceChild("left_rib41", CubeListBuilder.create(), PartPose.offset(6.0F, 0.0F, 0.0F));

		PartDefinition cube_r20 = left_rib41.addOrReplaceChild("cube_r20", CubeListBuilder.create().texOffs(0, 0).addBox(-2.0F, 0.05F, -6.25F, 1.0F, 1.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.48F, 0.0F));

		PartDefinition right_rib1 = spine.addOrReplaceChild("right_rib1", CubeListBuilder.create(), PartPose.offsetAndRotation(-0.8434F, -18.0F, 0.4395F, 0.0F, 0.0F, -3.1416F));

		PartDefinition cube_r21 = right_rib1.addOrReplaceChild("cube_r21", CubeListBuilder.create().texOffs(0, 0).addBox(-0.5F, -1.0F, 0.0F, 7.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.1566F, 0.0F, -0.1895F, 0.0F, 0.2618F, 0.0F));

		PartDefinition right_rib11 = right_rib1.addOrReplaceChild("right_rib11", CubeListBuilder.create(), PartPose.offset(6.0F, 0.0F, 0.0F));

		PartDefinition cube_r22 = right_rib11.addOrReplaceChild("cube_r22", CubeListBuilder.create().texOffs(0, 0).addBox(-1.0F, -1.25F, -7.0F, 2.0F, 2.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.48F, 0.0F));

		PartDefinition right_rib2 = spine.addOrReplaceChild("right_rib2", CubeListBuilder.create(), PartPose.offsetAndRotation(-0.8434F, -14.0F, 0.4395F, 0.0F, 0.0F, -3.1416F));

		PartDefinition cube_r23 = right_rib2.addOrReplaceChild("cube_r23", CubeListBuilder.create().texOffs(0, 0).addBox(-0.5F, -1.0F, 0.0F, 7.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.1566F, 0.0F, -0.1895F, 0.0F, 0.2618F, 0.0F));

		PartDefinition right_rib21 = right_rib2.addOrReplaceChild("right_rib21", CubeListBuilder.create(), PartPose.offset(6.0F, 0.0F, 0.0F));

		PartDefinition cube_r24 = right_rib21.addOrReplaceChild("cube_r24", CubeListBuilder.create().texOffs(0, 0).addBox(-1.0F, -1.25F, -7.0F, 2.0F, 2.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.48F, 0.0F));

		PartDefinition right_rib3 = spine.addOrReplaceChild("right_rib3", CubeListBuilder.create(), PartPose.offsetAndRotation(-0.8434F, -10.0F, 0.4395F, 0.0F, 0.0F, -3.1416F));

		PartDefinition cube_r25 = right_rib3.addOrReplaceChild("cube_r25", CubeListBuilder.create().texOffs(0, 0).addBox(-0.5F, -1.0F, 0.0F, 7.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.1566F, 0.0F, -0.1895F, 0.0F, 0.2618F, 0.0F));

		PartDefinition right_rib31 = right_rib3.addOrReplaceChild("right_rib31", CubeListBuilder.create(), PartPose.offset(6.0F, 0.0F, 0.0F));

		PartDefinition cube_r26 = right_rib31.addOrReplaceChild("cube_r26", CubeListBuilder.create().texOffs(0, 0).addBox(-1.0F, -1.25F, -7.0F, 2.0F, 2.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.48F, 0.0F));

		PartDefinition right_rib4 = spine.addOrReplaceChild("right_rib4", CubeListBuilder.create(), PartPose.offsetAndRotation(-0.8434F, -6.0F, 0.4395F, 0.0F, 0.0F, -3.1416F));

		PartDefinition cube_r27 = right_rib4.addOrReplaceChild("cube_r27", CubeListBuilder.create().texOffs(0, 0).addBox(-0.5F, 0.0F, 0.0F, 5.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.1566F, 0.0F, -0.1895F, 0.0F, 0.2618F, 0.0F));

		PartDefinition right_rib41 = right_rib4.addOrReplaceChild("right_rib41", CubeListBuilder.create(), PartPose.offset(6.0F, 0.0F, 0.0F));

		PartDefinition cube_r28 = right_rib41.addOrReplaceChild("cube_r28", CubeListBuilder.create().texOffs(0, 0).addBox(-2.25F, -0.1F, -6.25F, 1.0F, 1.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.48F, 0.0F));

		PartDefinition spine_lower = body.addOrReplaceChild("spine_lower", CubeListBuilder.create().texOffs(0, 0).addBox(-1.0F, -1.25F, -1.0F, 2.0F, 8.0F, 2.0F, new CubeDeformation(0.0F))
				.texOffs(49, 58).addBox(-1.5F, -0.75F, -1.5F, 3.0F, 2.0F, 3.0F, new CubeDeformation(0.0F))
				.texOffs(50, 57).addBox(-1.5F, 2.25F, -1.5F, 3.0F, 2.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.2182F, 0.0F, 0.0F));

		PartDefinition legs = partdefinition.addOrReplaceChild("legs", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, 8.5F, 19.0F, 0.2182F, 0.0F, 0.0F));

		PartDefinition cube_r29 = legs.addOrReplaceChild("cube_r29", CubeListBuilder.create().texOffs(0, 0).addBox(1.0F, -2.0F, 1.5F, 4.0F, 3.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.9163F, 0.0F));

		PartDefinition cube_r30 = legs.addOrReplaceChild("cube_r30", CubeListBuilder.create().texOffs(0, 0).addBox(-4.0F, -2.0F, -5.0F, 2.0F, 3.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.5672F, 0.0F));

		PartDefinition cube_r31 = legs.addOrReplaceChild("cube_r31", CubeListBuilder.create().texOffs(0, 0).addBox(-3.5F, -4.0F, 0.0F, 7.0F, 5.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.3054F, 0.0F, 0.0F));

		PartDefinition left_leg = legs.addOrReplaceChild("left_leg", CubeListBuilder.create().texOffs(0, 0).addBox(-2.0F, -2.0F, -1.0F, 2.0F, 18.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(6.0F, 0.0F, 0.0F, -0.1745F, 0.0F, 0.0F));

		PartDefinition left_lower_leg = left_leg.addOrReplaceChild("left_lower_leg", CubeListBuilder.create().texOffs(0, 0).addBox(-1.75F, -2.0F, -1.0F, 2.0F, 24.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 15.0F, 0.0F, 1.4835F, 0.0F, 0.0F));

		PartDefinition right_leg = legs.addOrReplaceChild("right_leg", CubeListBuilder.create().texOffs(0, 0).addBox(-2.0F, -2.0F, -1.0F, 2.0F, 18.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-3.0F, 0.0F, 0.0F, -0.1745F, 0.0F, 0.0F));

		PartDefinition right_lower_leg = right_leg.addOrReplaceChild("right_lower_leg", CubeListBuilder.create().texOffs(0, 0).addBox(-2.25F, -2.0F, -1.0F, 2.0F, 24.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 15.0F, 0.0F, 1.4835F, 0.0F, 0.0F));

		return LayerDefinition.create(meshdefinition, 64, 64);
	}

	@Override
	public void setupAnim(LivingEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {

	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		body.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		legs.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}
}